package com.xailab.vehicle.xaivehicledata.service.impl;

import com.xailab.vehicle.xaivehicledata.dao.BaseInfoDao;
import com.xailab.vehicle.xaivehicledata.entity.BaseInfoEntity;
import com.xailab.vehicle.xaivehicledata.entity.response.VehicleInfoOpResponse;
import com.xailab.vehicle.xaivehicledata.service.ALiYunOSSService;
import com.xailab.vehicle.xaivehicledata.service.BaseInfoService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.Query;

import com.xailab.vehicle.xaivehicledata.dao.BrandDao;
import com.xailab.vehicle.xaivehicledata.entity.BrandEntity;
import com.xailab.vehicle.xaivehicledata.service.BrandService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    private BrandDao brandDao;

    @Resource
    private ALiYunOSSService aLiYunOSSService;

    @Autowired
    private BaseInfoDao vehicleInfoDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                new QueryWrapper<BrandEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Long getBrandIdByName(String brand) {
        return brandDao.getBrandIdByName(brand);
    }

    @Override
    public List<VehicleInfoOpResponse> queryVehicleInfolist() {
        return brandDao.queryVehicleInfolist();
    }

    @Override
    public String queryVehiclePicture(String pictureName) {
        return aLiYunOSSService.queryPhoto(pictureName);
    }

    @Override
    public Integer updateVehicleInfo(VehicleInfoOpResponse vehicleInfoOpResponse) {

        BaseInfoEntity baseInfoEntity = new BaseInfoEntity();
        baseInfoEntity.setVehicleModel(vehicleInfoOpResponse.getVehicleModel());
        baseInfoEntity.setVehicleVersion(vehicleInfoOpResponse.getVehicleVersion());
        baseInfoEntity.setTimeToMarket(vehicleInfoOpResponse.getTimeToMarket());
        baseInfoEntity.setTestDate(vehicleInfoOpResponse.getTestDate());
        baseInfoEntity.setVehicleSystemVersion(vehicleInfoOpResponse.getVehicleSystemVersion());
        baseInfoEntity.setUpdateDate(new Date());
        baseInfoEntity.setStatus(vehicleInfoOpResponse.getStatus());
        baseInfoEntity.setEnergyType(vehicleInfoOpResponse.getEnergyType());
        baseInfoEntity.setEnergyTypeEn(vehicleInfoOpResponse.getEnergyTypeEn());
        baseInfoEntity.setId(Long.parseLong(vehicleInfoOpResponse.getId()));
        baseInfoEntity.setEnduranceMileage(vehicleInfoOpResponse.getEnduranceMileage());
        baseInfoEntity.setVehicleVersionEn(vehicleInfoOpResponse.getVehicleVersionEn());
        baseInfoEntity.setModelName(vehicleInfoOpResponse.getModelName());
        baseInfoEntity.setChipInformation(vehicleInfoOpResponse.getChipInformation());
        baseInfoEntity.setChipInformationEn(vehicleInfoOpResponse.getChipInformationEn());
        int b = vehicleInfoDao.updateById(baseInfoEntity);
        return b!=0?1:-1;
    }

}