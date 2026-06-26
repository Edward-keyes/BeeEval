package com.xailab.vehicle.xaivehicledata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xailab.vehicle.feign.vo.OpenSourceVo;
import com.xailab.vehicle.xaivehicledata.dao.BeeevalOpenCaseScoreDao;
import com.xailab.vehicle.xaivehicledata.dao.BeeevalOpenSourceCaseDao;
import com.xailab.vehicle.xaivehicledata.entity.BeeevalOpenCaseScoreEntity;
import com.xailab.vehicle.xaivehicledata.entity.vo.CaseIdIndexVo;
import com.xailab.vehicle.xaivehicledata.service.BeeevalOpenCaseScoreService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("beeevalOpenCaseScoreService")
@RequiredArgsConstructor
public class BeeevalOpenCaseScoreServiceImpl extends ServiceImpl<BeeevalOpenCaseScoreDao, BeeevalOpenCaseScoreEntity> implements BeeevalOpenCaseScoreService {

    @Resource
    BeeevalOpenSourceCaseDao beeevalOpenSourceCaseDao;

    @Override
    public Boolean saveCaseScoreByVehicleId(String vehicleId, List<OpenSourceVo> openSourceVos) {
        // 1. 查询现有数据库记录
        LambdaQueryWrapper<BeeevalOpenCaseScoreEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BeeevalOpenCaseScoreEntity::getVehicleId, Long.parseLong(vehicleId));
        List<BeeevalOpenCaseScoreEntity> existingScores = this.list(queryWrapper);

        // 2. 构建复合键映射（vehicleId_caseId -> 分数）
        Map<String, Integer> existingScoreMap = existingScores.stream()
                .collect(Collectors.toMap(
                        score -> score.getVehicleId() + "_" + score.getCaseId(),
                        BeeevalOpenCaseScoreEntity::getScore,
                        (oldVal, newVal) -> oldVal // 处理重复键
                ));

        // 3. 获取用例映射关系
        Map<Integer, Long> caseTagMap = queryOpenSourceCase();

        // 4. 准备待保存列表（包含新记录和需要更新的记录）
        List<BeeevalOpenCaseScoreEntity> toSaveList = new ArrayList<>();
        List<Long> toDeleteIds = new ArrayList<>(); // 需要删除的旧记录ID

        for (OpenSourceVo vo : openSourceVos) {
            String compositeKey = Long.parseLong(vehicleId) + "_" + vo.getBeeevalCaseId();
            Integer existingScore = existingScoreMap.get(compositeKey);

            // 情况1：记录不存在 -> 新增
            // 情况2：记录存在但分数不同 -> 更新（先删旧再增新）
            if (existingScore == null || !existingScore.equals(vo.getScore())) {
                // 如果记录已存在，先标记删除
                if (existingScore != null) {
                    existingScores.stream()
                            .filter(s -> compositeKey.equals(s.getVehicleId() + "_" + s.getCaseId()))
                            .findFirst()
                            .ifPresent(s -> toDeleteIds.add(Long.valueOf(s.getId())));
                }

                // 创建新记录
                BeeevalOpenCaseScoreEntity newScore = new BeeevalOpenCaseScoreEntity();
                newScore.setVehicleId(Long.parseLong(vehicleId));
                newScore.setCaseId(vo.getBeeevalCaseId());
                newScore.setScore(vo.getScore());
                newScore.setThreeTagId(caseTagMap.get(vo.getBeeevalCaseId()));
                toSaveList.add(newScore);
            }
        }

        // 5. 删除需要更新的旧记录
        if (!toDeleteIds.isEmpty()) {
            this.removeByIds(toDeleteIds);
        }

        // 6. 批量保存新记录
        return !toSaveList.isEmpty() && this.saveBatch(toSaveList);
    }

    private Map<Integer,Long> queryOpenSourceCase(){
        List<CaseIdIndexVo> caseIdIndexVos = beeevalOpenSourceCaseDao.queryOpenSourceCase();
        Map<Integer, Long> collect = caseIdIndexVos.stream().collect(Collectors.toMap(CaseIdIndexVo::getId, CaseIdIndexVo::getDomainIndexId));
        return collect;
    }
}
