package com.xailab.vehicle.operation.testplatform.controller;


import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.system.service.ALiYunOssService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("test_platform/oss")
@Tag(name="阿里OSS")
@AllArgsConstructor
public class TestPlatformALiController {

    @Resource
    private ALiYunOssService aLiYunOssService;

    //测试用户原始视频数据上传
    @PostMapping("/downloadAndUploadRawVideo")
    @PreAuthorize("hasAuthority('test_platform:test_case:update')")
    public Result<String> downloadFiles(@RequestParam("file") MultipartFile file, @RequestParam String sourceFolder, @RequestParam String targetFolder) {
        return Result.ok(aLiYunOssService.downloadAndUploadRawVideo(file,sourceFolder,targetFolder));
    }

    @PostMapping("/getVideoUrl")
    @PreAuthorize("hasAuthority('test_platform:test_case:page')")
    public Result<String> getVideoUrl(@RequestParam String name) {
        return Result.ok(aLiYunOssService.queryVideo(name));
    }
}
