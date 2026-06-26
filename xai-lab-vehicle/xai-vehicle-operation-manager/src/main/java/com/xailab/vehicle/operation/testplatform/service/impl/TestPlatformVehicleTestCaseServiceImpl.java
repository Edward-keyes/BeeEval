package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xailab.vehicle.feign.vehicledata.BeeevalOpenCaseScoreFeign;
import com.xailab.vehicle.feign.vehicledata.FunctionalDomainFeign;
import com.xailab.vehicle.feign.vo.DomainRelevancyVo;
import com.xailab.vehicle.feign.vo.OpenSourceVo;
import com.xailab.vehicle.feign.vo.VehicleIdAndOpenSourceVo;
import com.xailab.vehicle.framework.common.exception.ServerException;
import com.xailab.vehicle.framework.common.utils.JsonUtils;
import com.xailab.vehicle.feign.vo.TestCaseByFunctionIdVo;
import com.xailab.vehicle.operation.system.utils.poi.ExcelUtils;
import com.xailab.vehicle.operation.testplatform.convert.TestPlatformVehicleTestScenarioConvert;
import com.xailab.vehicle.operation.testplatform.dao.*;
import com.xailab.vehicle.operation.testplatform.entity.PcafeRelevancyDomainIndex;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehiclePlanDetailEntity;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestScenarioEntity;
import com.xailab.vehicle.operation.testplatform.pojo.excel.TestCaseBathAddImportTemplate;
import com.xailab.vehicle.operation.testplatform.pojo.response.TestPlatformImportByExcelResultResponse;
import com.xailab.vehicle.operation.testplatform.vo.*;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.convert.TestPlatformVehicleTestCaseConvert;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestCaseEntity;
import com.xailab.vehicle.operation.testplatform.query.TestPlatformVehicleTestCaseQuery;
import com.xailab.vehicle.operation.testplatform.service.TestPlatformVehicleTestCaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 测试用例总表
 *
 * @author mumu 
 * @since 1.0.0 2025-04-16
 */
@Service
@DS("test_platform")
@RequiredArgsConstructor
@Slf4j
public class TestPlatformVehicleTestCaseServiceImpl extends BaseServiceImpl<TestPlatformVehicleTestCaseDao, TestPlatformVehicleTestCaseEntity> implements TestPlatformVehicleTestCaseService {

    private final TestPlatformVehicleTestCaseDao testPlatformVehicleTestCaseDao;
    
    private final TestPlatformVehiclePlanDetailDao testPlatformVehiclePlanDetailDao;

    private final TestPlatformVehicleTestStateDao testPlatformVehicleTestStateDao;

    private final TestPlatformVehicleTestScenarioDao testPlatformVehicleTestScenarioDao;

    private final FunctionalDomainFeign functionalDomainFeign;

    private final PcafeRelevancyDomainIndexDao pcafeRelevancyDomainIndexDao;

    private final BeeevalOpenCaseScoreFeign beeevalOpenCaseScoreFeign;

    @Value("${testCase.add.retryNumber:3}")
    private Integer retryNumber;

//    @DS("test_platform")
    @Override
    public PageResult<TestPlatformVehicleTestCaseVO> page(TestPlatformVehicleTestCaseQuery query) {
        log.info("分页查询的测试用例参数为：{}", JsonUtils.toJsonString(query));
//        IPage<TestPlatformVehicleTestCaseEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));
        IPage<TestPlatformTestCasePageVo> page = testPlatformVehicleTestCaseDao.selectTestCaseByPage(getPage(query), query);

        List<TestPlatformVehicleTestCaseVO> list = page.getRecords().stream().map(it -> {
            TestPlatformVehicleTestCaseVO testCaseVO = TestPlatformVehicleTestCaseConvert.INSTANCE.convert(it);
            TestPlatformVehiclePlanDetailEntity planDetail = testPlatformVehiclePlanDetailDao.selectById(it.getFunctionId());
            if (Objects.nonNull(planDetail)) {
                testCaseVO.setFunctionName(planDetail.getPlanDetailName());
            }
            TestPlatformVehicleTestScenarioEntity  scenarioEntities = testPlatformVehicleTestScenarioDao.selectOne(Wrappers.<TestPlatformVehicleTestScenarioEntity>lambdaQuery()
                    .eq(TestPlatformVehicleTestScenarioEntity::getId,it.getScenarioId())
            );
            if(Objects.nonNull(scenarioEntities)){
                testCaseVO.setScenarioName(scenarioEntities.getScenarioName());
            }
            //设置车辆对应的
            if (!CollectionUtils.isEmpty(query.getCompareVehicleIds())){
                //查询车辆的评分
                List<TestPlatformVehicleCorePageVo> pageVo = testPlatformVehicleTestCaseDao.selectVehicleCore(it.getId(), query.getCompareVehicleIds());
                testCaseVO.setVehicleInfos(pageVo);
                //查询多个车辆之间的方差
                Double variance = testPlatformVehicleTestCaseDao.selectVehicleVariance(it.getId(), query.getCompareVehicleIds());
                if (Objects.nonNull(variance)){
                    // 创建 BigDecimal 对象（建议用 String 构造避免精度问题）
                    BigDecimal bd = new BigDecimal(String.valueOf(variance));
                    // 保留两位小数，四舍五入
                    BigDecimal result = bd.setScale(2, RoundingMode.HALF_UP);
                    testCaseVO.setVehicleVariance(result);
                }

            }
            if (Objects.nonNull(it.getIndustryAverage())){
                //设置小数
                BigDecimal industryAvg = new BigDecimal(String.valueOf(it.getIndustryAverage()));
                // 保留两位小数，四舍五入
                BigDecimal industryAvgResult = industryAvg.setScale(2, RoundingMode.HALF_UP);
                testCaseVO.setIndustryAverage(industryAvgResult);
            }
            if (Objects.nonNull(it.getVarianceValue())){
                BigDecimal variance = new BigDecimal(String.valueOf(it.getVarianceValue()));
                BigDecimal varianceResult = variance.setScale(2, RoundingMode.HALF_UP);
                testCaseVO.setVarianceValue(varianceResult);
            }
            return testCaseVO;
        }).toList();
        return new PageResult<>(list, page.getTotal());
    }
    private LambdaQueryWrapper<TestPlatformVehicleTestCaseEntity> getWrapper(TestPlatformVehicleTestCaseQuery query){
        LambdaQueryWrapper<TestPlatformVehicleTestCaseEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Objects.nonNull(query.getFunctionId()), TestPlatformVehicleTestCaseEntity::getFunctionId, query.getFunctionId());
        wrapper.like(StringUtils.isNotEmpty(query.getTestcaseContent()), TestPlatformVehicleTestCaseEntity::getTestcaseContent, query.getTestcaseContent());
        return wrapper;
    }


    /**
     * 根据 test_case 查询 State （分数详情）
     * @param id
     * @return
     */
    @Override
    public List<TestPlatformTestStateQueryResVo> findTestStateInfo(Integer id) {
        //判断当前id是否存在
        TestPlatformVehicleTestCaseEntity testPlatformVehicleTestCaseEntity = testPlatformVehicleTestCaseDao.selectById(id);
        if (Objects.isNull(testPlatformVehicleTestCaseEntity)){
            throw new ServerException("数据查询异常");
        }
        //查询所有的测试 结果 分数
        return testPlatformVehicleTestStateDao.selectAllAndVehicleByCaseId(testPlatformVehicleTestCaseEntity.getId());
    }

    /**
     * 获取当前 test_case 的指标树
     * @return
     */
    @Override
    public List<TestPlatformTestCaseTreeVo> findTestCasIndexTree() {
        QueryWrapper<TestPlatformVehicleTestCaseEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT primary_metric");
        //查询一级指标
        List<TestPlatformVehicleTestCaseEntity> entities = testPlatformVehicleTestCaseDao.selectList(queryWrapper);
        List<TestPlatformTestCaseTreeVo> list = entities.stream().map(it -> {
            TestPlatformTestCaseTreeVo treeVo = new TestPlatformTestCaseTreeVo();
            treeVo.setName(it.getPrimaryMetric());
            return treeVo;
        }).toList();
        getChild(list,2,null);
        return list;
    }

    /**
     * 查询所有测试场景
     * @return
     */
    @Override
    public List<TestPlatformTestScenarioResVo> findTestScenarioList() {
        List<TestPlatformVehicleTestScenarioEntity>  scenarioEntities = testPlatformVehicleTestScenarioDao.selectList(Wrappers.lambdaQuery());
        log.info("查询的测试场景数据为：{}",JsonUtils.toJsonString(scenarioEntities));
        return TestPlatformVehicleTestScenarioConvert.INSTANCE.convert(scenarioEntities);
    }

    /**
     * 批量excel导入
     * @param file
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TestPlatformImportByExcelResultResponse importByExcel(MultipartFile file) {
        //读取出数据
        List<TestCaseBathAddImportTemplate> importTemplates = null;
        try {
            importTemplates = ExcelUtils.readMultipartFile(file, TestCaseBathAddImportTemplate.class);
        } catch (Exception e) {
            log.error("表格解析错误：{}",e.getMessage(),e);
            throw new ServerException("表格解析异常");
        }
        log.info("导入数据解析长度：{}",importTemplates.size());
        if (CollectionUtils.isEmpty(importTemplates)){
            log.info("导入解析数据为空");
            throw new ServerException("表格导入解析数据为空");
        }
        List<Integer> functionIds = testPlatformVehiclePlanDetailDao.selectIdList();
        List<Integer> selectIdList = testPlatformVehicleTestScenarioDao.selectIdList();
        List<String> strings = testPlatformVehicleTestCaseDao.selectTestCaseInfo();
        //存储
        List<TestPlatformVehicleTestCaseEntity> entities = new ArrayList<>();
        List<TestPlatformImportByExcelResultResponse.FailNumberInfo> errorResponses = new ArrayList<>();
        for (TestCaseBathAddImportTemplate template : importTemplates) {
            //判断必填项是否为空
            if (StringUtils.isNotBlank(template.getRowTips())){
                errorResponses.add(new TestPlatformImportByExcelResultResponse.FailNumberInfo(template.getRowNum(),template.getDataSerial(),template.getRowTips()));
                continue;
            }
            //判断 功能域id是否存在
            if (!functionIds.contains(template.getFunctionId())) {
                errorResponses.add(new TestPlatformImportByExcelResultResponse.FailNumberInfo(template.getRowNum(),template.getDataSerial(),"功能域id不存在"));
                continue;
            }
            if (!selectIdList.contains(template.getScenarioId())) {
                errorResponses.add(new TestPlatformImportByExcelResultResponse.FailNumberInfo(template.getRowNum(),template.getDataSerial(),"场景id不存在"));
                continue;
            }
            if (strings.contains(template.getTestcaseContent())){
                errorResponses.add(new TestPlatformImportByExcelResultResponse.FailNumberInfo(template.getRowNum(),template.getDataSerial(),"测试用例内容重复"));
                continue;
            }
            TestPlatformVehicleTestCaseEntity convert = TestPlatformVehicleTestCaseConvert.INSTANCE.convert(template);
            entities.add(convert);
        }
        if (!CollectionUtils.isEmpty(entities)){
            log.info("成功保存数据的长度为：{}",entities.size());
            //TODO: 异步
            saveBatch(entities);
        }
        TestPlatformImportByExcelResultResponse response = new TestPlatformImportByExcelResultResponse();
        response.setSuccessNumber(entities.size());
        response.setFailNumber(errorResponses.size());
        response.setFailInfo(errorResponses);
        return response;
    }

    //递归查询
    private void getChild(List<TestPlatformTestCaseTreeVo> treeVos, int level,String parentName) {
        for (TestPlatformTestCaseTreeVo treeVo : treeVos) {
            List<TestPlatformTestCaseTreeVo> list = null;
            int nextLevel = level;
            switch (level){
                case 2:
                    QueryWrapper<TestPlatformVehicleTestCaseEntity> queryWrapper = new QueryWrapper<>();
                    queryWrapper.select("DISTINCT secondary_metric").lambda()
                            .eq(TestPlatformVehicleTestCaseEntity::getPrimaryMetric,treeVo.getName());
                    //查询二级指标
                    List<TestPlatformVehicleTestCaseEntity> entities = testPlatformVehicleTestCaseDao.selectList(queryWrapper);
                    list = entities.stream().map(it -> {
                        TestPlatformTestCaseTreeVo treeVo1 = new TestPlatformTestCaseTreeVo();
                        treeVo1.setName(it.getSecondaryMetric());
                        return treeVo1;
                    }).toList();
                    nextLevel = 3;
                    break;
                case 3:
                    QueryWrapper<TestPlatformVehicleTestCaseEntity> queryWrapper3 = new QueryWrapper<>();
                    queryWrapper3.select("DISTINCT tertiary_metric").lambda()
                            .eq(TestPlatformVehicleTestCaseEntity::getPrimaryMetric,parentName)
                            .eq(TestPlatformVehicleTestCaseEntity::getSecondaryMetric,treeVo.getName());
                    //查询三级指标
                    List<TestPlatformVehicleTestCaseEntity> entities2 = testPlatformVehicleTestCaseDao.selectList(queryWrapper3);
                    list = entities2.stream().map(it -> {
                        TestPlatformTestCaseTreeVo treeVo1 = new TestPlatformTestCaseTreeVo();
                        treeVo1.setName(it.getTertiaryMetric());
                        return treeVo1;
                    }).toList();
                    nextLevel = 4;
                    break;
                default:
                    return;
            }
            if (CollectionUtils.isEmpty(list)){
                return;
            }
            treeVo.setChild(list);
            getChild(list,nextLevel,treeVo.getName());
        }
    }


    @Override
    public void save(TestPlatformVehicleTestCaseAddVO vo) {
        //判断功能域是否存在
        TestPlatformVehiclePlanDetailEntity planDetail = testPlatformVehiclePlanDetailDao.selectById(vo.getFunctionId());
        if (Objects.isNull(planDetail)) {
            throw new ServerException("功能域不存在");
        }
        //判断 场景是否存在
        TestPlatformVehicleTestScenarioEntity scenarioEntity = testPlatformVehicleTestScenarioDao.selectById(vo.getScenarioId());
        if (Objects.isNull(scenarioEntity)) {
            throw new ServerException("场景不存在");
        }
        int retry = 0;
        do {
            try {
                //自增测试用例序列ID
                int i = testPlatformVehiclePlanDetailDao.updateCaseSerialIdIdIncrease(planDetail.getId());
                log.info("更新自增测试用例序列ID,数量：{}",i);
                //查询id
                Integer caseSerialId = testPlatformVehiclePlanDetailDao.selectCaseSerialId(planDetail.getId());
                TestPlatformVehicleTestCaseEntity entity = TestPlatformVehicleTestCaseConvert.INSTANCE.convert(vo);
                entity.setId(caseSerialId);
                baseMapper.insert(entity);
                return;
            }catch (Exception e){
                log.error("自增测试用例序列ID失败,重试次数：{}",retry);
                retry++;
            }
        }while (retry<retryNumber);
    }

    @Override
    public void update(TestPlatformVehicleTestCaseUpdateVO vo) {
        //判断功能域是否存在
        TestPlatformVehiclePlanDetailEntity planDetail = testPlatformVehiclePlanDetailDao.selectById(vo.getFunctionId());
        if (Objects.isNull(planDetail)) {
            throw new ServerException("功能域不存在");
        }
        //判断 场景是否存在
        TestPlatformVehicleTestScenarioEntity scenarioEntity = testPlatformVehicleTestScenarioDao.selectById(vo.getScenarioId());
        if (Objects.isNull(scenarioEntity)) {
            throw new ServerException("场景不存在");
        }
        TestPlatformVehicleTestCaseEntity entity = TestPlatformVehicleTestCaseConvert.INSTANCE.convert(vo);
        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

    @Override
    public List<TestCaseByFunctionIdVo> caseQueryByFunctionId(String functionId) {

        List<TestCaseByFunctionIdVo> list=testPlatformVehicleTestCaseDao.caseQueryByFunctionId(functionId);

        return list;
    }

    @Override
    public List<TestCaseByFunctionIdVo> caseQuery() {
        return testPlatformVehicleTestCaseDao.caseQuery();
    }

    @Override
    public TestPlatformVehicleTestCaseEntity selectById(Integer testCaseId) {
        return testPlatformVehicleTestCaseDao.selectById(testCaseId);
    }

    @Override
    public void queryRelevancySynchronization() {
        List<DomainRelevancyVo> domainRelevancyVos = functionalDomainFeign.queryRelevancy();

        List<CaseRelevancyVo> relevancyVos = testPlatformVehicleTestCaseDao.queryRelevancy();

        Map<String, CaseRelevancyVo> collect = relevancyVos.stream().collect(Collectors.toMap(v ->v.getPlanDetailName()+"-"+v.getTertiaryMetric()+"-"+v.getTestcaseContent(), v -> v));

        List<PcafeRelevancyDomainIndex> pcafeRelevancyDomainIndices = new ArrayList<>();

        for (DomainRelevancyVo domainRelevancyVo : domainRelevancyVos) {

            if (collect.containsKey(domainRelevancyVo.getFunctionalDomainName()+"-"+domainRelevancyVo.getIndexName()+"-"+domainRelevancyVo.getTestCaseContent())) {

                PcafeRelevancyDomainIndex pcafeRelevancyDomainIndex = new PcafeRelevancyDomainIndex();

                pcafeRelevancyDomainIndex.setBeeevalCaseId(domainRelevancyVo.getFunctionIndexId());

                Integer subId = collect.get(domainRelevancyVo.getFunctionalDomainName()+"-"+domainRelevancyVo.getIndexName()+"-"+domainRelevancyVo.getTestCaseContent()).getSubId();

                pcafeRelevancyDomainIndex.setCaseId(subId);

                pcafeRelevancyDomainIndices.add(pcafeRelevancyDomainIndex);

            }

        }

        pcafeRelevancyDomainIndexDao.insert(pcafeRelevancyDomainIndices);
    }

    @Override
    public void queryOpenSourceSynchronization(String recordId,String beeevalVehicleId) {

        /**
         * 基于与beeeval关联用例表数据查询该车分数
         */
        List<OpenSourceVo> list=testPlatformVehicleTestCaseDao.queryOpenSourceSynchronization(recordId);
        //TODO 将同步操作进行记录
        VehicleIdAndOpenSourceVo vo = new VehicleIdAndOpenSourceVo();
        vo.setVehicleId(beeevalVehicleId);
        vo.setOpenSourceVos(list);
        Boolean boo = beeevalOpenCaseScoreFeign.saveCaseScoreByVehicleId(vo);

        log.info("车辆id为：{} 车辆分数同步状态：{}",recordId,boo);

    }
}