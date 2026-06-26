package com.xailab.vehicle.xaivehicledata.service.impl;

import com.xailab.vehicle.feign.vehicledata.ALiYunOSSFeign;
import com.xailab.vehicle.xaicommon.utils.JsonUtils;
import com.xailab.vehicle.xaicommon.utils.Okhttp3Utils;
import com.xailab.vehicle.xaivehicledata.config.ALiYunOssConfig;
import com.xailab.vehicle.xaivehicledata.config.ConstantConfig;
import com.xailab.vehicle.xaivehicledata.entity.BrandEntity;
import com.xailab.vehicle.feign.vo.AddVehicleInfoRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.OSSResponse;
import com.xailab.vehicle.xaivehicledata.entity.vo.BrandVehicleSystemVo;
import com.xailab.vehicle.xaivehicledata.entity.vo.VehicleConutThreeTagVo;
import com.xailab.vehicle.xaivehicledata.entity.vo.VehicleDetailInfoVo;
import com.xailab.vehicle.xaivehicledata.entity.vo.VehicleInfoVo;
import com.xailab.vehicle.xaivehicledata.service.ALiYunOSSService;
import com.xailab.vehicle.xaivehicledata.service.BrandService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.Query;

import com.xailab.vehicle.xaivehicledata.dao.BaseInfoDao;
import com.xailab.vehicle.xaivehicledata.entity.BaseInfoEntity;
import com.xailab.vehicle.xaivehicledata.service.BaseInfoService;
import org.springframework.web.multipart.MultipartFile;


@Service("baseInfoService")
@RequiredArgsConstructor
public class BaseInfoServiceImpl extends ServiceImpl<BaseInfoDao, BaseInfoEntity> implements BaseInfoService {

    @Autowired
    private BrandService brandService;

    @Autowired
    private BaseInfoDao baseInfoDao;

    private final ALiYunOssConfig ossConfig;

    @Resource
    private ALiYunOSSService aLiYunOSSService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BaseInfoEntity> page = this.page(
                new Query<BaseInfoEntity>().getPage(params),
                new QueryWrapper<BaseInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Long getVehicleIdByBMV(String brand, String model, String version) {

        Long brandId = brandService.getBrandIdByName(brand);

        BaseInfoEntity baseInfoEntity = baseInfoDao.selectOne(new QueryWrapper<BaseInfoEntity>()
                .eq("brand_id", brandId)
                .eq("vehicle_model", model)
                .eq("vehicle_system_version", version));

        return baseInfoEntity.getId();
    }

    @Override
    public List<VehicleInfoVo> queryAllVehicle(String language) {

        List<VehicleInfoVo> baseInfoEntities = baseInfoDao.queryAllVehicle(language);

        return baseInfoEntities;
    }

    @Override
    public List<VehicleDetailInfoVo> queryAllVehicleDetailInfo(String language) {

        ConstantConfig constantConfig = new ConstantConfig();

        List<VehicleDetailInfoVo> vehicleDetailInfoVos = baseInfoDao.queryAllVehicleDetailInfo(language);

        List<VehicleConutThreeTagVo> countMap=baseInfoDao.queryAllVehicleConutThreeTag(language);

        Map<String, String> collect = countMap.stream().collect(Collectors.toMap(VehicleConutThreeTagVo::getVehicleName, VehicleConutThreeTagVo::getCounta));

        List<VehicleDetailInfoVo> vehicleData = new ArrayList<>();

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        for (VehicleDetailInfoVo vehicleDetailInfoVo : vehicleDetailInfoVos){
            executorService.submit(() -> {
                Okhttp3Utils okhttp3Utils = new Okhttp3Utils();
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                String url = "";
                try {
                    String data = okhttp3Utils.getData(ossConfig.getGetImageUrl() + "?file_id=" + vehicleDetailInfoVo.getVehiclePicture(), headers);
                    OSSResponse ossResponse = JsonUtils.jsonToObj(data, OSSResponse.class);
                    url = ossResponse.getUrl();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                vehicleDetailInfoVo.setVehiclePicture(url);

                vehicleDetailInfoVo.setBigModelFunctionCount(collect.get(vehicleDetailInfoVo.getVehicleName()));

                vehicleData.add(vehicleDetailInfoVo);
            });
        }

        executorService.shutdown(); // 关闭线程池
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // 等待所有任务完成
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 定义优先级列表
        Set<String> priorityIds = new HashSet<>(Arrays.asList("448078678031597629", "448078678031597632", "448078678031597641"));

        // 自定义排序规则
        vehicleData.sort((v1, v2) -> {
            boolean v1Priority = priorityIds.contains(v1.getId());
            boolean v2Priority = priorityIds.contains(v2.getId());

            if (v1Priority && !v2Priority) {
                return -1; // v1 优先级高，排在前面
            } else if (!v1Priority && v2Priority) {
                return 1; // v2 优先级高，排在前面
            } else {
                return 0; // 优先级相同，保持原顺序
            }
        });

        return vehicleData;
    }

    @Override
    public VehicleDetailInfoVo queryVehicleInfoByVehicleId(String id,String language) {

        VehicleDetailInfoVo vehicleDetailInfoVo = baseInfoDao.queryVehicleInfoByVehicleId(id,language);

        Okhttp3Utils okhttp3Utils = new Okhttp3Utils();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        String url ="";
        try {
            String data = okhttp3Utils.getData(ossConfig.getGetImageUrl() + "?file_id=" + vehicleDetailInfoVo.getVehiclePicture(), headers);
            OSSResponse ossResponse = JsonUtils.jsonToObj(data, OSSResponse.class);
            url = ossResponse.getUrl();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        vehicleDetailInfoVo.setVehiclePicture(url);

        return vehicleDetailInfoVo;
    }

    @Override
    public Map<String, String> getBrandVehicleVersionMap() {

        List<BrandVehicleSystemVo> brandVehicleSystemVos = baseInfoDao.queryBrandVehicleVersion();

        Map<String,String> collect = brandVehicleSystemVos.stream().collect(
                Collectors.toMap(BrandVehicleSystemVo::getVehicleName, BrandVehicleSystemVo::getVehicleId)
        );

        return collect;
    }

    @Override
    public Map<String, String> getBrandVehicleVersionMap2() {

        List<BrandVehicleSystemVo> brandVehicleSystemVos = baseInfoDao.queryBrandVehicleVersion();

        Map<String,String> collect = brandVehicleSystemVos.stream().collect(
                Collectors.toMap(BrandVehicleSystemVo::getVehicleId,BrandVehicleSystemVo::getVehicleName)
        );

        return collect;
    }

    @Override
    public Map<String, String> getBrandVehicleVersionEnMap2() {
        List<BrandVehicleSystemVo> brandVehicleSystemVos = baseInfoDao.queryBrandVehicleEnVersion();

        Map<String,String> collect = brandVehicleSystemVos.stream().collect(
                Collectors.toMap(BrandVehicleSystemVo::getVehicleId,BrandVehicleSystemVo::getVehicleName)
        );

        return collect;
    }

    @Override
    public Boolean addVehicleInfo(AddVehicleInfoRequest addVehicleInfoRequest, MultipartFile image) {

        Map<String, Long> brandCollect = brandService.list().stream().collect(Collectors.toMap(BrandEntity::getBrand, BrandEntity::getId));
        Long brandId = brandCollect.get(addVehicleInfoRequest.getBrand());
        if (Objects.nonNull(brandId)){
            BaseInfoEntity extracted = extracted(addVehicleInfoRequest, image, brandId);
            return save(extracted);
        }else {
            //新增车辆品牌
            BrandEntity brand = new BrandEntity();

            brand.setBrand(addVehicleInfoRequest.getBrand());
            brand.setBrandEn(addVehicleInfoRequest.getBrand_en());

            brandService.save(brand);

            Long brandId1 = brand.getId();

            BaseInfoEntity extracted = extracted(addVehicleInfoRequest, image, brandId1);
            return save(extracted);
        }
    }

    private BaseInfoEntity extracted(AddVehicleInfoRequest addVehicleInfoRequest, MultipartFile image, Long brandId) {
        //新增车辆数据
        BaseInfoEntity baseInfoEntity = new BaseInfoEntity();

        baseInfoEntity.setBrandId(brandId);
        baseInfoEntity.setVehicleModel(addVehicleInfoRequest.getVehicleModel());
        baseInfoEntity.setVehicleVersion(addVehicleInfoRequest.getVehicleVersion());
        baseInfoEntity.setVehicleVersionEn(addVehicleInfoRequest.getVehicleVersionEn());

        String s = aLiYunOSSService.uploadPhoto(image);
        // 图片上传
        baseInfoEntity.setVehiclePicture(s);

        baseInfoEntity.setTimeToMarket(addVehicleInfoRequest.getTimeToMarket());
        baseInfoEntity.setTestDate(addVehicleInfoRequest.getTestDate());
        baseInfoEntity.setVehicleSystemVersion(addVehicleInfoRequest.getVehicleSystemVersion());
        baseInfoEntity.setCreateDate(new Date());
        baseInfoEntity.setUpdateDate(new Date());
        baseInfoEntity.setStatus(addVehicleInfoRequest.getStatus());
        baseInfoEntity.setIsDelete(0);
        baseInfoEntity.setEnergyType(addVehicleInfoRequest.getEnergyType());
        baseInfoEntity.setEnergyTypeEn(addVehicleInfoRequest.getEnergyTypeEn());
        baseInfoEntity.setEnduranceMileage(addVehicleInfoRequest.getEnduranceMileage());
        baseInfoEntity.setModelName(addVehicleInfoRequest.getModelName());

        return baseInfoEntity;
    }

}