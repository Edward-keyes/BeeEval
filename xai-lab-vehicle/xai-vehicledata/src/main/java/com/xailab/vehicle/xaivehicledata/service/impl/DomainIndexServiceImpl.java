package com.xailab.vehicle.xaivehicledata.service.impl;

import com.xailab.vehicle.xaicommon.utils.Query;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.dao.BaseInfoDao;
import com.xailab.vehicle.xaivehicledata.dao.BrandDao;
import com.xailab.vehicle.xaivehicledata.entity.BaseInfoEntity;
import com.xailab.vehicle.xaivehicledata.entity.BrandEntity;
import com.xailab.vehicle.xaivehicledata.entity.request.DomainTreeQueryScoreRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.*;
import com.xailab.vehicle.xaivehicledata.entity.vo.*;
import com.xailab.vehicle.xaivehicledata.service.DomainTreeService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.util.CollectionUtils.isEmpty;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.R;
import lombok.extern.slf4j.Slf4j;

import com.xailab.vehicle.xaivehicledata.dao.DomainIndexDao;
import com.xailab.vehicle.xaivehicledata.dao.VehicleDomainScoreDao;
import com.xailab.vehicle.xaivehicledata.entity.DomainIndexEntity;
import com.xailab.vehicle.xaivehicledata.entity.VehicleDomainScoreEntity;
import com.xailab.vehicle.xaivehicledata.service.DomainIndexService;
import org.springframework.util.CollectionUtils;

@Service("domainIndexService")
@Slf4j
public class DomainIndexServiceImpl extends ServiceImpl<DomainIndexDao, DomainIndexEntity>
        implements DomainIndexService {

    @Autowired
    DomainIndexDao domainIndexDao;

    @Autowired
    DomainTreeService domainTreeService;

    @Autowired
    VehicleDomainScoreDao vehicleDomainScoreDao;

    @Resource
    private BrandDao brandDao;
    @Resource
    private BaseInfoDao baseInfoDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<DomainIndexEntity> page = this.page(
                new Query<DomainIndexEntity>().getPage(params),
                new QueryWrapper<DomainIndexEntity>());

        return new PageUtils(page);
    }

    @Override
    public List<BigModelCapabilityAssessmentBase> largeModelCapabilityAssessment(List<String> vehicleId,
            String language) {

        Map<String, String> unitMap = new HashMap<>();

        if (language.equals("zh_home_top")) {
            unitMap.put("首字响应时长", "秒");
            unitMap.put("图像生成速度", "秒/张");
            unitMap.put("免唤醒准确率", "%");
            unitMap.put("拒识准确率", "%");
            unitMap.put("文本生成速度", "词/秒");
            unitMap.put("任务完成率", "%");
            unitMap.put("跨域协作能力", "分");
        }

        if (language.equals("en")) {
            unitMap.put("Time To First Token", "s");
            unitMap.put("Image Generation Speed", "s/ piece");
            unitMap.put("Wake-up-Free Accuracy Rate", "%");
            unitMap.put("Rejection Accuracy", "%");
            unitMap.put("Text Generation Speed", "word/s");
            unitMap.put("Task Completion Rate", "%");
            unitMap.put("Cross-Domain Collaboration Capability", "point");
        }

        // 获取原有业务数据
        List<GeneralAbilityVo> generalAbilityVos = domainTreeService.queryGeneralAbilityVos(vehicleId, language);
        List<GeneralAbilityVo> generalAbilityAvgVos = domainTreeService.queryGeneralAbilityAvgVos(vehicleId, language);
        List<ActionAbilityVo> actionAbilityVos = domainTreeService.queryActionAbilityVos(vehicleId, language);
        List<ActionAbilityVo> actionAbilityAVGVos = domainTreeService.queryActionAbilityAVGVos(vehicleId, language);

        // 为请求的车辆添加同步的基础能力数据（type=3）
        for (String vehicleIdStr : vehicleId) {
            Long vehicleIdLong = Long.parseLong(vehicleIdStr);

            // 查询该车辆的所有基础能力分数（type=3）
            List<VehicleDomainScoreEntity> syncScores = vehicleDomainScoreDao.selectList(
                    Wrappers.<VehicleDomainScoreEntity>lambdaQuery()
                            .eq(VehicleDomainScoreEntity::getVehicleId, vehicleIdLong)
                            .eq(VehicleDomainScoreEntity::getType, (short) 3));

            if (!CollectionUtils.isEmpty(syncScores)) {
                // 根据指标ID获取指标信息，用于区分认知能力和行动能力
                for (VehicleDomainScoreEntity syncScore : syncScores) {
                    Long domainIndexId = syncScore.getDomainId();

                    // 这里需要根据domainIndexId判断是认知能力还是行动能力
                    // 暂时通过一些已知的指标ID来判断，实际应该从domain_index表查询
                    if (isCognitiveAbility(domainIndexId)) {
                        // 认知能力
                        GeneralAbilityVo syncGeneralAbility = createGeneralAbilityFromSync(
                                vehicleIdStr, syncScore, language);
                        if (syncGeneralAbility != null) {
                            // 检查是否已存在相同的指标，避免重复添加
                            boolean alreadyExists = generalAbilityVos.stream()
                                    .anyMatch(g -> g.getVehicleId().equals(vehicleIdStr) &&
                                            g.getGeneralAbilityTag().equals(syncGeneralAbility.getGeneralAbilityTag()));
                            if (!alreadyExists) {
                                generalAbilityVos.add(syncGeneralAbility);
                                log.debug("为车辆{}添加同步的认知能力数据: {}", vehicleIdStr,
                                        syncGeneralAbility.getGeneralAbilityTag());
                            }
                        }
                    } else if (isActionAbility(domainIndexId)) {
                        // 行动能力
                        ActionAbilityVo syncActionAbility = createActionAbilityFromSync(
                                vehicleIdStr, syncScore, unitMap, language);
                        if (syncActionAbility != null) {
                            // 检查是否已存在相同的指标，避免重复添加
                            boolean alreadyExists = actionAbilityVos.stream()
                                    .anyMatch(a -> a.getVehicleId().equals(vehicleIdStr) &&
                                            a.getActionDescriptionId()
                                                    .equals(syncActionAbility.getActionDescriptionId()));
                            if (!alreadyExists) {
                                actionAbilityVos.add(syncActionAbility);
                                log.debug("为车辆{}添加同步的行动能力数据: {}", vehicleIdStr,
                                        syncActionAbility.getActionDescription());
                            }
                        }
                    }
                }
            }
        }
        List<BigModelCapabilityAssessmentBase> transform1 = transform(generalAbilityAvgVos, actionAbilityAVGVos,
                unitMap);
        List<BigModelCapabilityAssessmentBase> transform = transform(generalAbilityVos, actionAbilityVos, unitMap);
        for (BigModelCapabilityAssessmentBase bigModelCapabilityAssessmentBase : transform1) {
            transform.add(bigModelCapabilityAssessmentBase);
        }
        // ------------------------------------------------------------------------------------------------------------
        // TODO: 调用接口获取数据
        /**
         * List<BigModelCapabilityAssessmentBase> bigModelCapabilityAssessmentBases =
         * new ArrayList<>();
         * 
         * for (String id : vehicleId)
         * {
         * if (id.equals("448078678031597629")) {
         * bigModelCapabilityAssessmentBases.add(getBigModelCapabilityAssessment(id,"极氪-007"));
         * }else if (id.equals("438735847190167560")) {
         * bigModelCapabilityAssessmentBases.add(getBigModelCapabilityAssessment(id,"乐道-L60"));
         * }
         * }
         * 
         * bigModelCapabilityAssessmentBases.add(getBigModelCapabilityAssessment("111","行業均值"));
         **/

        return transform;
    }

    public static List<BigModelCapabilityAssessmentBase> transform(
            List<GeneralAbilityVo> generalAbilityVos,
            List<ActionAbilityVo> actionAbilityVos,
            Map<String, String> unitMap) {

        // 使用Map按vehicleId分组
        Map<String, BigModelCapabilityAssessmentBase> resultMap = new HashMap<>();

        // 处理GeneralAbilityVo列表
        for (GeneralAbilityVo generalAbilityVo : generalAbilityVos) {
            String vehicleId = generalAbilityVo.getVehicleId();
            BigModelCapabilityAssessmentBase base = resultMap.getOrDefault(vehicleId,
                    new BigModelCapabilityAssessmentBase());
            base.setVehicleId(vehicleId);
            base.setVehicleName(generalAbilityVo.getVehicleName());

            // 初始化generalAbilities列表（如果尚未初始化）
            if (base.getGeneralAbilities() == null) {
                base.setGeneralAbilities(new ArrayList<>());
            }

            // 添加通识能力
            GeneralAbilityResponse generalAbilityResponse = new GeneralAbilityResponse();
            generalAbilityResponse.setGeneralAbilityTag(generalAbilityVo.getGeneralAbilityTag());
            generalAbilityResponse.setGeneralAbilityValue(
                    Double.valueOf(String.format("%.1f", generalAbilityVo.getGeneralAbilityValue())));
            base.getGeneralAbilities().add(generalAbilityResponse);

            // 将更新后的对象存回map
            resultMap.put(vehicleId, base);
        }

        // 处理ActionAbilityVo列表
        for (ActionAbilityVo actionAbilityVo : actionAbilityVos) {
            String vehicleId = actionAbilityVo.getVehicleId();
            BigModelCapabilityAssessmentBase base = resultMap.getOrDefault(vehicleId,
                    new BigModelCapabilityAssessmentBase());
            base.setVehicleId(vehicleId);
            base.setVehicleName(actionAbilityVo.getVehicleName());

            // 初始化actionAbilities列表（如果尚未初始化）
            if (base.getActionAbilities() == null) {
                base.setActionAbilities(new ArrayList<>());
            }
            // 添加行动能力
            ActionAbilityResponse actionAbilityResponse = new ActionAbilityResponse();
            actionAbilityResponse.setActionNumber(
                    getSwitch(actionAbilityVo.getActionDescriptionId(), actionAbilityVo.getActionNumber()));

            actionAbilityResponse.setActionDescription(actionAbilityVo.getActionDescription());
            actionAbilityResponse.setUnit(unitMap.get(actionAbilityVo.getActionDescription()));
            base.getActionAbilities().add(actionAbilityResponse);

            // 将更新后的对象存回map
            resultMap.put(vehicleId, base);
        }

        // 将Map中的values转换为List
        return new ArrayList<>(resultMap.values());
    }

    public static Double getSwitch(String id, Double d) {
        if (d == -1) {
            return 0.0;
        } else if (id.equals("451751102942019595")) {
            if (d <= 1) {
                return Double.valueOf(String.format("%.1f", d * 100));
            } else {
                return Double.valueOf(String.format("%.1f", d));
            }
        } else if (id.equals("451751102942019596")) {
            if (d <= 1) {
                return Double.valueOf(String.format("%.1f", d * 100));
            } else {
                return Double.valueOf(String.format("%.1f", d));
            }
        } else if (id.equals("451751102942019597")) {
            if (d <= 1) {
                return Double.valueOf(String.format("%.1f", d * 100));
            } else {
                return Double.valueOf(String.format("%.1f", d));
            }
        } else if (id.equals("451751102942019598")) {
            return Double.valueOf(String.format("%.1f", d));
        } else if (id.equals("451751102942019599")) {
            if (d >= 1000) {
                return Double.valueOf(String.format("%.1f", d / 1000));
            } else {
                return Double.valueOf(String.format("%.1f", d));
            }
        } else if (id.equals("451751102942019601")) {
            if (d >= 1000) {
                return Double.valueOf(String.format("%.1f", d / 1000));
            } else {
                return Double.valueOf(String.format("%.1f", d));
            }
        } else {
            return Double.valueOf(String.format("%.1f", d));
        }
    }

    @Override
    public List<FunctionalDomainVehicleVo> largeModelCapabilityAssessmentFunction(List<String> ids) {

        for (String id : ids) {

            FunctionalDomainVehicleVo functionalDomainVehicleVo = new FunctionalDomainVehicleVo();

            functionalDomainVehicleVo.setVehicleId(id);

            functionalDomainVehicleVo.setVehicleName("极氪-007");

            List<FunctionalDomainRepresentation> functionalDomainRepresentations = new ArrayList<>();

            // for (){
            //
            // }

            functionalDomainVehicleVo.setFunctionalDomains(functionalDomainRepresentations);

        }

        /** ---------------------------------------------- **/

        FunctionalDomainRepresentation functionalDomainRepresentation = new FunctionalDomainRepresentation();

        functionalDomainRepresentation.setFunctionalDomainId("450679990120349705");

        functionalDomainRepresentation.setFunctionalDomainName("出行域");

        functionalDomainRepresentation.setFunctionalDomainTotalScore(86.86);

        List<FunctionalDomainIndicator> functionalDomainIndicatorList = new ArrayList<>();
        /** ------------------------------------------- **/

        FunctionalDomainIndicator functionalDomainIndicator1 = new FunctionalDomainIndicator();

        // functionalDomainIndicator1.setIndicatorName();
        //
        // functionalDomainIndicator1.setIndicatorValue();
        //
        // functionalDomainIndicator1.setIndicatorId();
        //
        // functionalDomainIndicator1.setResponseScreenshot();
        //
        // functionalDomainIndicator1.setResultAnalysis();
        //
        // functionalDomainIndicator1.setTestCaseName();
        //
        // functionalDomainIndicator1.setTestInstruction();

        /** ------------------------------------------- **/
        functionalDomainRepresentation.setFunctionalDomainIndicators(functionalDomainIndicatorList);

        return null;
    }

    @Override
    public Map<String, String> getDomainIndexMap() {

        List<DomainNameIndexVo> domainNameIndexVos = domainIndexDao.getDomainIndexMap();

        Map<String, String> collect = domainNameIndexVos.stream().collect(
                Collectors.toMap(DomainNameIndexVo::getDomainName, DomainNameIndexVo::getIndexId));

        return collect;
    }

    @Override
    public DomainIndexDetail queryDomainIndexDetail(String language) {

        DomainIndexDetail domainIndexDetail = new DomainIndexDetail();

        List<IndexDetailVo> cognitiveAbility = domainIndexDao.queryDomainIndexDetail(language, "450679990120349699");

        List<IndexDetailVo> actionAbility = domainIndexDao.queryDomainIndexDetail(language, "450679990120349700");

        domainIndexDetail.setActionAbility(actionAbility);

        domainIndexDetail.setCognitiveAbility(cognitiveAbility);

        return domainIndexDetail;
    }

    @Override
    public List<IndexDetailVo> queryIndexDetailVoList(String language, String domainId) {

        List<IndexDetailVo> cognitiveAbility = domainIndexDao.queryDomainIndexDetail(language, domainId);

        return cognitiveAbility;
    }

    /**
     * 判断指标是否为认知能力
     */
    private boolean isCognitiveAbility(Long domainIndexId) {
        // 认知能力的指标ID列表（根据实际数据调整）
        List<Long> cognitiveAbilityIds = Arrays.asList(
                451751102942019588L, // 文化伦理
                451751102942019589L, // 通识知识
                451751102942019590L, // 安全性
                451751102942019592L, // 信息提取能力
                451751102942019593L, // 语言推理能力
                451751102942019594L, // 跨语言理解能力
                452214351944744960L // 自然语言理解准确率
        );
        return cognitiveAbilityIds.contains(domainIndexId);
    }

    /**
     * 判断指标是否为行动能力
     */
    private boolean isActionAbility(Long domainIndexId) {
        // 行动能力的指标ID列表（根据实际数据调整）
        List<Long> actionAbilityIds = Arrays.asList(
                451751102942019595L, // 拒识准确率
                451751102942019596L, // 免唤醒准确率
                451751102942019597L, // 任务完成率
                451751102942019598L, // 跨域协作能力
                451751102942019599L, // 首字响应时长
                451751102942019600L, // 文本生成速度
                451751102942019601L // 图像生成速度
        );
        return actionAbilityIds.contains(domainIndexId);
    }

    /**
     * 从同步数据创建认知能力对象
     */
    private GeneralAbilityVo createGeneralAbilityFromSync(String vehicleId, VehicleDomainScoreEntity syncScore,
            String language) {
        try {
            Long domainIndexId = syncScore.getDomainId();

            // 根据指标ID获取指标名称
            DomainIndexEntity domainIndex = domainIndexDao.selectById(domainIndexId);
            if (domainIndex == null) {
                return null;
            }

            GeneralAbilityVo generalAbility = new GeneralAbilityVo();
            generalAbility.setVehicleId(vehicleId);
            generalAbility.setVehicleName(getVehicleNameById(vehicleId, language));
            generalAbility.setGeneralAbilityTag(domainIndex.getIndexName());
            generalAbility.setGeneralAbilityValue(syncScore.getScore().doubleValue());

            return generalAbility;
        } catch (Exception e) {
            log.error("创建同步认知能力数据失败，vehicleId: {}, domainIndexId: {}", vehicleId, syncScore.getDomainId(), e);
            return null;
        }
    }

    /**
     * 从同步数据创建行动能力对象
     */
    private ActionAbilityVo createActionAbilityFromSync(String vehicleId, VehicleDomainScoreEntity syncScore,
            Map<String, String> unitMap, String language) {
        try {
            Long domainIndexId = syncScore.getDomainId();

            // 根据指标ID获取指标名称
            DomainIndexEntity domainIndex = domainIndexDao.selectById(domainIndexId);
            if (domainIndex == null) {
                return null;
            }

            ActionAbilityVo actionAbility = new ActionAbilityVo();
            actionAbility.setVehicleId(vehicleId);
            actionAbility.setVehicleName(getVehicleNameById(vehicleId, language));
            actionAbility.setActionDescription(domainIndex.getIndexName());
            actionAbility.setActionDescriptionId(domainIndexId.toString());
            actionAbility.setActionNumber(syncScore.getScore().doubleValue());
            actionAbility.setUnit(unitMap.getOrDefault(domainIndex.getIndexName(), ""));

            return actionAbility;
        } catch (Exception e) {
            log.error("创建同步行动能力数据失败，vehicleId: {}, domainIndexId: {}", vehicleId, syncScore.getDomainId(), e);
            return null;
        }
    }

    /**
     * 根据车辆ID获取车辆名称
     */
    private String getVehicleNameById(String vehicleId, String language) {
        try {
            // 这里可以复用DomainTreeServiceImpl中的逻辑
            // 暂时使用简单的实现
            // return "车辆-" + vehicleId;
            BaseInfoEntity vehicle = baseInfoDao.selectById(Long.parseLong(vehicleId));
            BrandEntity brand = brandDao.selectById(vehicle.getBrandId());
            if (language.equals("zh_home_top")) {
                return vehicle != null ? brand.getBrand() + "-" + vehicle.getVehicleModel() : "未知车辆";
            } else {
                return vehicle != null ? brand.getBrandEn() + "-" + vehicle.getVehicleModel() : "Unknown Vehicle";
            }

        } catch (Exception e) {
            log.error("获取车辆名称失败，vehicleId: {}", vehicleId, e);
            return "未知车辆";
        }
    }

}