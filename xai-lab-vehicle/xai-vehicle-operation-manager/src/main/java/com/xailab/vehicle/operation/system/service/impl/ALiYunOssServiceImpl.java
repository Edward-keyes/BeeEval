package com.xailab.vehicle.operation.system.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xailab.vehicle.framework.common.exception.ServerException;
import com.xailab.vehicle.framework.common.utils.SnowflakeIdGenerator;
import com.xailab.vehicle.operation.config.ALiYunOssConfig;
import com.xailab.vehicle.operation.system.service.ALiYunOssService;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleInfoEntity;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestStateEntity;
import com.xailab.vehicle.operation.testplatform.service.TestPlatformVehicleInfoService;
import com.xailab.vehicle.operation.testplatform.service.TestPlatformVehicleTestStateService;
import com.xailab.vehicle.operation.testplatform.service.impl.ZipFileService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ALiYunOssServiceImpl implements ALiYunOssService {

    private final ALiYunOssConfig ossConfig;

    @Resource
    TestPlatformVehicleInfoService testPlatformVehicleInfoService;

    @Resource
    TestPlatformVehicleTestStateService testPlatformVehicleTestStateService;

    @Resource
    ZipFileService zipFileService;

    private static final SnowflakeIdGenerator snowflakeIdGenerator
            = new SnowflakeIdGenerator(0, 0);

    @Override
    public String download(MultipartFile file, String unzipPath,String uploadPath) {
        Path uploadLocation = Paths.get(uploadPath).toAbsolutePath().normalize();
        // 保存原始文件
        String originalFileName = file.getOriginalFilename();
        Path targetLocation = uploadLocation.resolve(originalFileName);
        try {
            file.transferTo(targetLocation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 2. 创建唯一解压目录（防止文件名冲突）
        String extractDirName = originalFileName;
        Path extractLocation = Paths.get(unzipPath).toAbsolutePath().normalize();
        String dir = extractDirName.replace(".zip", "");
        Path finalExtractPath = extractLocation.resolve(dir);
        try {
            Files.createDirectories(finalExtractPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 3. 执行解压操作
        try (ZipInputStream zipIn = new ZipInputStream(
                new FileInputStream(targetLocation.toFile()))) {
            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                Path filePath = finalExtractPath.resolve(entry.getName());
                if (!filePath.normalize().startsWith(finalExtractPath)) {
                    throw new IOException("非法路径: " + entry.getName());
                }
                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                } else {
                    Files.copy(zipIn, filePath);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return dir;

    }

    @Override
    public String downloadAndUploadRawVideo(MultipartFile file,String sourceFolder, String targetFolder) {

        //将目标文件夹下载至服务器
        String download = download(file,targetFolder,sourceFolder);

        System.out.println("File downloaded and unzipped successfully to:" +download);

        //将服务器视频上传至oss
        saveRawVideo(targetFolder,download);

        return null;
    }

    public String saveRawVideo(String targetFolder,String dir){

        File folder = new File(targetFolder);

        if (folder.isDirectory()) {

            String folderName = folder.getName();

            TestPlatformVehicleInfoEntity testPlatformVehicleInfo
                    = testPlatformVehicleInfoService.getOne(new QueryWrapper<TestPlatformVehicleInfoEntity>()
                    .eq("vehicle_name"
                            ,dir.substring(0,dir.lastIndexOf("-")))
                    .eq("infotainment_software_version"
                            ,dir.split("-")[dir.split("-").length-1]));

            // 获取文件夹中的所有文件和子文件夹
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()&& file.getName().contains(dir)) {
                        log.info("文件夹: " + file.getName());
                        processFolder(file,testPlatformVehicleInfo.getId());
                    }
                }
            }
        }

        return null;
    }

    public void processFolder(File folder,Integer id) {
        // 获取文件夹中的所有文件和子文件夹
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (isVideoFile(file)) {
                        log.info("视频文件: " + file.getName());
                        String caseId=file.getName().split("-")[0];
                        TestPlatformVehicleTestStateEntity testState=testPlatformVehicleTestStateService
                                .queryOneTestStateByCaseIdAndVehicleId(caseId,id);
                        long videoName = snowflakeIdGenerator.nextId();
                        if(Objects.nonNull(testState)){
                            testState.setVideoName(videoName+".mp4");
                            testPlatformVehicleTestStateService.updateById(testState);
                            MultipartFile multipartFile = fileConvertMultipartFile(file);
                            uploadFile(multipartFile,videoName+"");
                        }
                    }
                } else if (file.isDirectory()) {
                    processFolder(file,id);
                }
            }
        }else {
            log.info("文件夹为空: " + folder.getAbsolutePath());
        }
    }

    // 判断文件是否为视频
    private boolean isVideoFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".mp4") || fileName.endsWith(".avi") ||
                fileName.endsWith(".mov") || fileName.endsWith(".mkv") ||
                fileName.endsWith(".flv");
    }

    //文件上传oss
    public String uploadFile(MultipartFile file,String videoName) {
        // 获取阿里云OSS参数
        String endpoint = ossConfig.getEndpoint();

        String bucketName = ossConfig.getBucketName();

        String accessKeyId = ossConfig.getAccessKeyId();

        String accessKeySecret = ossConfig.getAccessKeySecret();

        String subtitle = ossConfig.getSubtitle();

        // 获取上传的文件的输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, subtitle+videoName+".mp4", inputStream);

        // 关闭ossClient
        ossClient.shutdown();
        return "上传成功";
    }

    /**
     * 将文件转换为MultipartFile对象
     * @param file
     * @return
     */
    public static MultipartFile fileConvertMultipartFile(File file){
        // 创建一个DiskFileItemFactory对象
        // DiskFileItemFactory factory = new DiskFileItemFactory();

        // 创建一个DiskFileItem对象，注意：这里的文件名会作为上传文件的名字
        DiskFileItem fileItem = new DiskFileItem("file", "application/octet-stream", false, file.getName(), (int) file.length(), file.getParentFile());

        // 将文件内容写入DiskFileItem
        try {
            fileItem.getOutputStream().write(java.nio.file.Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 返回MultipartFile
        return new MultipartFile() {
            @Override
            public String getName() {
                return fileItem.getFieldName();
            }

            @Override
            public String getOriginalFilename() {
                return fileItem.getName();
            }

            @Override
            public String getContentType() {
                return fileItem.getContentType();
            }

            @Override
            public boolean isEmpty() {
                return fileItem.getSize() == 0;
            }

            @Override
            public long getSize() {
                return fileItem.getSize();
            }

            @Override
            public byte[] getBytes() throws IOException {
                return java.nio.file.Files.readAllBytes(file.toPath());
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return fileItem.getInputStream();
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                try {
                    fileItem.write(dest);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Override
    public String queryVideo(String strName) {

        // 获取阿里云OSS参数
        String endpoint = ossConfig.getEndpoint();

        String bucketName = ossConfig.getBucketName();

        String accessKeyId = ossConfig.getAccessKeyId();

        String accessKeySecret = ossConfig.getAccessKeySecret();

        String subtitle = ossConfig.getSubtitle();

        // 设置预签名URL的过期时间（例如1小时后过期）
        Date expiration = new Date(new Date().getTime() + 360 * 1000L);

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 生成预签名URL
        URL url = ossClient.generatePresignedUrl(bucketName, subtitle+strName, expiration);
        System.out.println("预签名URL: " + url);

        // 关闭ossClient
        ossClient.shutdown();

        return url+"";
    }
}
