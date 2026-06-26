package com.xailab.vehicle.xaivehicledata.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xailab.vehicle.feign.vo.BasicAbilityScoreSyncVO;
import com.xailab.vehicle.feign.vo.DomainScoreSyncVO;
import com.xailab.vehicle.feign.vo.TertiaryMetricScoreSyncVO;
import com.xailab.vehicle.xaivehicledata.dao.*;
import com.xailab.vehicle.xaivehicledata.entity.*;
import com.xailab.vehicle.xaivehicledata.service.DataSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 数据同步服务实现
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
@Slf4j
@Service
public class DataSyncServiceImpl implements DataSyncService {

    @Autowired
    private VehicleDomainScoreDao vehicleDomainScoreDao;

    @Autowired
    private FunctionalDomainDao functionalDomainDao;

    @Autowired
    private DomainIndexDao domainIndexDao;

    @Autowired
    private BaseInfoDao baseInfoDao;

    @Autowired
    private DomainTreeDao domainTreeDao;

    @Autowired
    private BeeevalOpenSourceCaseDao beeevalOpenSourceCaseDao;

    @Autowired
    private BeeevalOpenCaseScoreDao beeevalOpenCaseScoreDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncDomainScore(List<DomainScoreSyncVO> syncData) {
        if (CollectionUtils.isEmpty(syncData)) {
            log.warn("同步的功能域分数数据为空");
            return;
        }

        log.info("开始同步功能域分数，共{}条数据", syncData.size());

        for (DomainScoreSyncVO vo : syncData) {
            try {
                // 验证车辆是否存在
                BaseInfoEntity vehicle = baseInfoDao.selectById(vo.getVehicleBaseInfoId());
                if (vehicle == null) {
                    log.warn("车辆不存在，vehicleBaseInfoId: {}", vo.getVehicleBaseInfoId());
                    continue;
                }

                // 根据功能域名称查找功能域ID
                FunctionalDomainEntity functionalDomain = functionalDomainDao.selectOne(
                        Wrappers.<FunctionalDomainEntity>lambdaQuery()
                                .eq(FunctionalDomainEntity::getFunctionalDomainName, vo.getFunctionalDomainName()));

                if (functionalDomain == null) {
                    log.warn("功能域不存在，functionalDomainName: {}", vo.getFunctionalDomainName());
                    continue;
                }

                // 保存或更新功能域分数
                VehicleDomainScoreEntity scoreEntity = vehicleDomainScoreDao.selectOne(
                        Wrappers.<VehicleDomainScoreEntity>lambdaQuery()
                                .eq(VehicleDomainScoreEntity::getVehicleId, vo.getVehicleBaseInfoId())
                                .eq(VehicleDomainScoreEntity::getDomainId, functionalDomain.getId()));

                if (scoreEntity == null) {
                    scoreEntity = new VehicleDomainScoreEntity();
                    scoreEntity.setVehicleId(vo.getVehicleBaseInfoId());
                    scoreEntity.setDomainId(functionalDomain.getId());
                    scoreEntity.setType((short) 1); // 功能域分数类型
                }

                // 将BigDecimal转换为Double类型，保留两位小数
                scoreEntity.setScore(vo.getDomainScore().setScale(2, RoundingMode.HALF_UP).doubleValue());
                vehicleDomainScoreDao.insertOrUpdate(scoreEntity);

                log.debug("同步功能域分数成功，vehicleId: {}, domainId: {}, score: {}",
                        vo.getVehicleBaseInfoId(), functionalDomain.getId(), vo.getDomainScore());

            } catch (Exception e) {
                log.error("同步单条功能域分数失败，vo: {}", vo, e);
                // 继续处理其他数据，不因单条失败而中断整个同步过程
            }
        }

        log.info("功能域分数同步完成");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncTertiaryMetricScore(List<TertiaryMetricScoreSyncVO> syncData) {
        if (CollectionUtils.isEmpty(syncData)) {
            log.warn("同步的三级指标分数数据为空");
            return;
        }

        log.info("开始同步三级指标分数，共{}条数据", syncData.size());

        for (TertiaryMetricScoreSyncVO vo : syncData) {
            try {
                // 验证车辆是否存在
                BaseInfoEntity vehicle = baseInfoDao.selectById(vo.getVehicleBaseInfoId());
                if (vehicle == null) {
                    log.warn("车辆不存在，vehicleBaseInfoId: {}", vo.getVehicleBaseInfoId());
                    continue;
                }

                // 根据指标名称查找三级指标ID
                DomainIndexEntity domainIndex = domainIndexDao.selectOne(
                        Wrappers.<DomainIndexEntity>lambdaQuery()
                                .eq(DomainIndexEntity::getIndexName, vo.getIndexName())
                                .eq(DomainIndexEntity::getFunctionalDomain, vo.getFunctionalDomainId()));

                if (domainIndex == null) {
                    log.warn("三级指标不存在，indexName: {}", vo.getIndexName());
                    continue;
                }

                // 保存或更新三级指标分数
                // 注意：三级指标分数可能需要存储在专门的表中，或者扩展现有的分数表
                // 这里暂时使用VehicleDomainScoreEntity，需要根据实际需求调整
                VehicleDomainScoreEntity scoreEntity = vehicleDomainScoreDao.selectOne(
                        Wrappers.<VehicleDomainScoreEntity>lambdaQuery()
                                .eq(VehicleDomainScoreEntity::getVehicleId, vo.getVehicleBaseInfoId())
                                .eq(VehicleDomainScoreEntity::getDomainId, domainIndex.getId()));

                if (scoreEntity == null) {
                    scoreEntity = new VehicleDomainScoreEntity();
                    scoreEntity.setVehicleId(vo.getVehicleBaseInfoId());
                    scoreEntity.setDomainId(domainIndex.getId());
                    scoreEntity.setType((short) 2); // 三级指标分数类型
                }

                scoreEntity.setScore(vo.getIndexScore().setScale(2, RoundingMode.HALF_UP).doubleValue());
                vehicleDomainScoreDao.insertOrUpdate(scoreEntity);

                log.debug("同步三级指标分数成功，vehicleId: {}, indexId: {}, score: {}",
                        vo.getVehicleBaseInfoId(), domainIndex.getId(), vo.getIndexScore());

            } catch (Exception e) {
                log.error("同步单条三级指标分数失败，vo: {}", vo, e);
                // 继续处理其他数据
            }
        }

        log.info("三级指标分数同步完成");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncBasicAbilityScore(List<BasicAbilityScoreSyncVO> syncData) {
        if (CollectionUtils.isEmpty(syncData)) {
            log.warn("同步的基础能力分数数据为空");
            return;
        }

        log.info("开始同步基础能力分数，共{}条数据", syncData.size());

        for (BasicAbilityScoreSyncVO vo : syncData) {
            try {
                // 验证车辆是否存在
                BaseInfoEntity vehicle = baseInfoDao.selectById(vo.getVehicleBaseInfoId());
                if (vehicle == null) {
                    log.warn("车辆不存在，vehicleBaseInfoId: {}", vo.getVehicleBaseInfoId());
                    continue;
                }

                // 根据指标名称查找对应的domain_index_id
                // 基础能力存储在功能域ID为450679990120349700（行动能力）的指标下
                DomainIndexEntity domainIndex = domainIndexDao.selectOne(
                        Wrappers.<DomainIndexEntity>lambdaQuery()
                                .eq(DomainIndexEntity::getIndexName, vo.getIndexName())
                // .eq(DomainIndexEntity::getFunctionalDomain, 450679990120349700L) // 行动能力功能域
                );

                if (domainIndex == null) {
                    log.warn("基础能力指标不存在，indexName: {}", vo.getIndexName());
                    continue;
                }

                // 保存或更新基础能力分数到vehicle_domain_score表
                VehicleDomainScoreEntity scoreEntity = vehicleDomainScoreDao.selectOne(
                        Wrappers.<VehicleDomainScoreEntity>lambdaQuery()
                                .eq(VehicleDomainScoreEntity::getVehicleId, vo.getVehicleBaseInfoId())
                                .eq(VehicleDomainScoreEntity::getDomainId, domainIndex.getId()));

                if (scoreEntity == null) {
                    scoreEntity = new VehicleDomainScoreEntity();
                    scoreEntity.setVehicleId(vo.getVehicleBaseInfoId());
                    scoreEntity.setDomainId(domainIndex.getId());
                    scoreEntity.setType((short) 3); // 基础能力分数类型
                }

                if (Objects.nonNull(vo.getIndexScore())) {
                    scoreEntity.setScore(vo.getIndexScore().setScale(2, RoundingMode.HALF_UP).doubleValue());
                }
                vehicleDomainScoreDao.insertOrUpdate(scoreEntity);

                log.debug("同步基础能力分数成功，vehicleId: {}, indexId: {}, score: {}",
                        vo.getVehicleBaseInfoId(), domainIndex.getId(), vo.getIndexScore());

            } catch (Exception e) {
                log.error("同步单条基础能力分数失败，vo: {}", vo, e);
                // 继续处理其他数据
            }
        }

        log.info("基础能力分数同步完成");
    }

    @Override
    public List<Map<String, Object>> getOpenSourceCases() {
        try {
            // 查询所有开源用例
            List<BeeevalOpenSourceCaseEntity> cases = beeevalOpenSourceCaseDao.selectList(null);

            // 转换为Map格式
            return cases.stream().map(entity -> {
                Map<String, Object> map = new java.util.HashMap<>();
                map.put("id", entity.getId());
                map.put("function_domain_id", entity.getFunctionDomainId());
                map.put("domain_index_id", entity.getDomainIndexId());
                map.put("test_case_content", entity.getTestCaseContent());
                map.put("test_case_content_en", entity.getTestCaseContentEn());
                return map;
            }).collect(java.util.stream.Collectors.toList());

        } catch (Exception e) {
            log.error("获取BeeEval开源用例失败", e);
            return new java.util.ArrayList<>();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncOpenSourceScore(List<Map<String, Object>> syncData) {
        if (CollectionUtils.isEmpty(syncData)) {
            log.warn("同步的开源题目分数数据为空");
            return;
        }

        log.info("开始同步开源题目分数，共{}条数据", syncData.size());

        for (Map<String, Object> data : syncData) {
            try {
                Long vehicleId = (Long) data.get("vehicleId");
                Long threeTagId = (Long) data.get("threeTagId");
                Integer caseId = (Integer) data.get("caseId");
                Integer score = (Integer) data.get("score");

                // 验证车辆是否存在
                BaseInfoEntity vehicle = baseInfoDao.selectById(vehicleId);
                if (vehicle == null) {
                    log.warn("车辆不存在，vehicleId: {}", vehicleId);
                    continue;
                }

                // 保存或更新开源用例分数
                BeeevalOpenCaseScoreEntity scoreEntity = beeevalOpenCaseScoreDao.selectOne(
                        Wrappers.<BeeevalOpenCaseScoreEntity>lambdaQuery()
                                .eq(BeeevalOpenCaseScoreEntity::getVehicleId, vehicleId)
                                .eq(BeeevalOpenCaseScoreEntity::getThreeTagId, threeTagId)
                                .eq(BeeevalOpenCaseScoreEntity::getCaseId, caseId));

                if (scoreEntity == null) {
                    scoreEntity = new BeeevalOpenCaseScoreEntity();
                    scoreEntity.setVehicleId(vehicleId);
                    scoreEntity.setThreeTagId(threeTagId);
                    scoreEntity.setCaseId(caseId);
                }

                scoreEntity.setScore(score);
                beeevalOpenCaseScoreDao.insertOrUpdate(scoreEntity);

                log.debug("同步开源题目分数成功，vehicleId: {}, caseId: {}, score: {}",
                        vehicleId, caseId, score);

            } catch (Exception e) {
                log.error("同步单条开源题目分数失败，data: {}", data, e);
                // 继续处理其他数据
            }
        }

        log.info("开源题目分数同步完成");
    }
}
