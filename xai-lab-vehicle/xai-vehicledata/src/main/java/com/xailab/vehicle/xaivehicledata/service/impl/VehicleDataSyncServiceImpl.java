package com.xailab.vehicle.xaivehicledata.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xailab.vehicle.xaivehicledata.dao.BaseInfoDao;
import com.xailab.vehicle.xaivehicledata.dao.BrandDao;
import com.xailab.vehicle.xaivehicledata.dao.QaKnowledgeBaseDao;
import com.xailab.vehicle.xaivehicledata.entity.BaseInfoEntity;
import com.xailab.vehicle.xaivehicledata.entity.BrandEntity;
import com.xailab.vehicle.xaivehicledata.entity.QaKnowledgeBaseEntity;
import com.xailab.vehicle.xaivehicledata.service.VehicleDataSyncService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 车辆数据同步服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleDataSyncServiceImpl implements VehicleDataSyncService {

    private final BaseInfoDao baseInfoDao;
    private final BrandDao brandDao;
    private final QaKnowledgeBaseDao qaKnowledgeBaseDao;

    private static final String VEHICLE_CATEGORY = "vehicle";
    private static final String VEHICLE_SOURCE = "vehicle_base_info";
    private static final String VEHICLE_AUTHOR = "system";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int syncAllVehiclesToKnowledgeBase() {
        log.info("开始同步所有车辆数据到知识库");

        List<BaseInfoEntity> vehicles = baseInfoDao.selectList(new QueryWrapper<>());
        if (vehicles == null || vehicles.isEmpty()) {
            log.warn("没有找到车辆数据");
            return 0;
        }

        List<QaKnowledgeBaseEntity> knowledgeBaseEntities = new ArrayList<>();
        Map<Long, BrandEntity> brandMap = loadBrandMap();

        for (BaseInfoEntity vehicle : vehicles) {
            try {
                QaKnowledgeBaseEntity entity = convertToKnowledgeBaseEntity(vehicle, brandMap);
                knowledgeBaseEntities.add(entity);
            } catch (Exception e) {
                log.error("转换车辆数据失败: vehicleId={}, error={}", vehicle.getId(), e.getMessage());
            }
        }

        if (!knowledgeBaseEntities.isEmpty()) {
            knowledgeBaseEntities.forEach(entity -> qaKnowledgeBaseDao.insert(entity));
            log.info("成功同步{}条车辆数据到知识库", knowledgeBaseEntities.size());
            return knowledgeBaseEntities.size();
        }

        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int syncVehiclesToKnowledgeBase(List<Long> vehicleIds) {
        log.info("开始同步指定车辆数据到知识库: vehicleIds={}", vehicleIds);

        if (vehicleIds == null || vehicleIds.isEmpty()) {
            log.warn("车辆ID列表为空");
            return 0;
        }

        List<BaseInfoEntity> vehicles = baseInfoDao.selectBatchIds(vehicleIds);
        if (vehicles == null || vehicles.isEmpty()) {
            log.warn("没有找到指定的车辆数据: vehicleIds={}", vehicleIds);
            return 0;
        }

        List<QaKnowledgeBaseEntity> knowledgeBaseEntities = new ArrayList<>();
        Map<Long, BrandEntity> brandMap = loadBrandMap();

        for (BaseInfoEntity vehicle : vehicles) {
            try {
                QaKnowledgeBaseEntity entity = convertToKnowledgeBaseEntity(vehicle, brandMap);
                knowledgeBaseEntities.add(entity);
            } catch (Exception e) {
                log.error("转换车辆数据失败: vehicleId={}, error={}", vehicle.getId(), e.getMessage());
            }
        }

        if (!knowledgeBaseEntities.isEmpty()) {
            knowledgeBaseEntities.forEach(entity -> qaKnowledgeBaseDao.insert(entity));
            log.info("成功同步{}条车辆数据到知识库", knowledgeBaseEntities.size());
            return knowledgeBaseEntities.size();
        }

        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int clearVehicleDataFromKnowledgeBase() {
        log.info("开始清空知识库中的车辆数据");

        QueryWrapper<QaKnowledgeBaseEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("category", VEHICLE_CATEGORY);

        List<QaKnowledgeBaseEntity> entities = qaKnowledgeBaseDao.selectList(wrapper);
        if (entities == null || entities.isEmpty()) {
            log.info("知识库中没有车辆数据");
            return 0;
        }

        int count = 0;
        for (QaKnowledgeBaseEntity entity : entities) {
            qaKnowledgeBaseDao.deleteById(entity.getId());
            count++;
        }

        log.info("成功删除{}条车辆数据", count);
        return count;
    }

    @Override
    public String generateVehicleDataInsertSql() {
        log.info("生成车辆数据INSERT SQL语句");

        List<BaseInfoEntity> vehicles = baseInfoDao.selectList(new QueryWrapper<>());
        if (vehicles == null || vehicles.isEmpty()) {
            log.warn("没有找到车辆数据");
            return "-- 没有车辆数据可同步";
        }

        Map<Long, BrandEntity> brandMap = loadBrandMap();
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("-- 车辆数据INSERT模板\n");
        sqlBuilder.append("-- 执行前请先清空旧数据: DELETE FROM qa_knowledge_base WHERE category = 'vehicle';\n");
        sqlBuilder.append("-- 执行以下INSERT语句:\n\n");

        for (BaseInfoEntity vehicle : vehicles) {
            try {
                BrandEntity brand = brandMap.get(vehicle.getBrandId());
                if (brand == null) {
                    log.warn("未找到品牌信息: brandId={}", vehicle.getBrandId());
                    continue;
                }

                String fullVehicleName = brand.getBrand() + vehicle.getVehicleModel();
                String content = String.format("车辆ID: %d, 品牌: %s, 型号: %s, 完整车名: %s",
                        vehicle.getId(), brand.getBrand(), vehicle.getVehicleModel(), fullVehicleName);

                List<String> tags = new ArrayList<>();
                tags.add(brand.getBrand());
                tags.add(vehicle.getVehicleModel());
                tags.add("车辆");
                if (vehicle.getEnergyType() != null) {
                    tags.add(vehicle.getEnergyType());
                }

                String tagsJson = JSON.toJSONString(tags);

                sqlBuilder.append(String.format(
                        "INSERT INTO qa_knowledge_base (title, content, category, tags, source, author) VALUES ('%s', '%s', '%s', '%s', '%s', '%s');\n",
                        "车辆列表-" + fullVehicleName,
                        content,
                        VEHICLE_CATEGORY,
                        tagsJson,
                        VEHICLE_SOURCE,
                        VEHICLE_AUTHOR));

            } catch (Exception e) {
                log.error("生成INSERT SQL失败: vehicleId={}, error={}", vehicle.getId(), e.getMessage());
            }
        }

        String sql = sqlBuilder.toString();
        log.info("生成INSERT SQL完成，共{}条记录", vehicles.size());
        return sql;
    }

    private Map<Long, BrandEntity> loadBrandMap() {
        List<BrandEntity> brands = brandDao.selectList(new QueryWrapper<>());
        if (brands == null || brands.isEmpty()) {
            log.warn("没有找到品牌数据");
            return Map.of();
        }
        return brands.stream().collect(Collectors.toMap(BrandEntity::getId, brand -> brand));
    }

    private QaKnowledgeBaseEntity convertToKnowledgeBaseEntity(BaseInfoEntity vehicle,
            Map<Long, BrandEntity> brandMap) {
        BrandEntity brand = brandMap.get(vehicle.getBrandId());
        if (brand == null) {
            throw new IllegalArgumentException("未找到品牌信息: brandId=" + vehicle.getBrandId());
        }

        String fullVehicleName = brand.getBrand() + vehicle.getVehicleModel();
        String content = String.format("车辆ID: %d, 品牌: %s, 型号: %s, 完整车名: %s",
                vehicle.getId(), brand.getBrand(), vehicle.getVehicleModel(), fullVehicleName);

        List<String> tags = new ArrayList<>();
        tags.add(brand.getBrand());
        tags.add(vehicle.getVehicleModel());
        tags.add("车辆");
        if (vehicle.getEnergyType() != null) {
            tags.add(vehicle.getEnergyType());
        }

        QaKnowledgeBaseEntity entity = new QaKnowledgeBaseEntity();
        entity.setTitle("车辆列表-" + fullVehicleName);
        entity.setContent(content);
        entity.setCategory(VEHICLE_CATEGORY);
        entity.setTags(JSON.toJSONString(tags));
        entity.setSource(VEHICLE_SOURCE);
        entity.setAuthor(VEHICLE_AUTHOR);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());

        return entity;
    }
}
