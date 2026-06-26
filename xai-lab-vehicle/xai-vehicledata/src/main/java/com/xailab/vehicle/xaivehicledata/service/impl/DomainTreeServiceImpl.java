package com.xailab.vehicle.xaivehicledata.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xailab.vehicle.feign.vo.FunctionDomainVideoVo;
import com.xailab.vehicle.feign.vo.FunctionalVideoVoF;
import com.xailab.vehicle.xaicommon.utils.*;
import com.xailab.vehicle.xaivehicledata.dao.*;
import com.xailab.vehicle.xaivehicledata.entity.*;
import com.xailab.vehicle.xaivehicledata.entity.request.DomainTreeQueryScoreRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.*;
import com.xailab.vehicle.xaivehicledata.entity.vo.*;
import com.xailab.vehicle.xaivehicledata.service.ALiYunOSSService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xailab.vehicle.xaivehicledata.service.DomainTreeService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Service("domainTreeService")
@Slf4j
public class DomainTreeServiceImpl extends ServiceImpl<DomainTreeDao, DomainTreeEntity> implements DomainTreeService {

    @Resource
    private BrandDao brandDao;
    @Resource
    private BaseInfoDao baseInfoDao;

    @Resource
    private DomainIndexDao domainIndexDao;

    @Resource
    private DomainTreeDao domainTreeDao;

    @Resource
    private FunctionalDomainDao functionalDomainDao;

    @Resource
    private FunctionDomainVideoDao functionDomainVideoDao;

    @Resource
    private ALiYunOSSService aliYunOSSService;

    @Resource
    private VehicleDomainScoreDao vehicleDomainScoreDao;

    SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(0, 0);

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<DomainTreeEntity> page = this.page(
                new Query<DomainTreeEntity>().getPage(params),
                new QueryWrapper<DomainTreeEntity>());

        return new PageUtils(page);
    }

    @Override
    public FunctionalVideoListResponse getFunctionalVideoList(String vehicleId, String language, Integer page) {

        // 计算分页参数
        int pageSize;
        int offset;

        if (page == null || page <= 1) {
            // 第一页：每种视频返回5条
            pageSize = 5;
            offset = 0;
        } else {
            // 后续页：每种视频返回2条
            pageSize = 2;
            offset = 5 + (page - 2) * 2; // 第一页5条 + (当前页-2)*2条
        }

        // 获取功能表现视频列表数据（带分页）
        List<FunctionVideoListVo> goodVideoList = functionDomainVideoDao.selectListQWithPagination(vehicleId, 1,
                language, offset, pageSize);

        // bad
        List<FunctionVideoListVo> badVideoList = functionDomainVideoDao.selectListQWithPagination(vehicleId, 0,
                language, offset, pageSize);

        List<FunctionalVideoVo> height = getFunctionalVideoVos(goodVideoList);

        List<FunctionalVideoVo> bad = getFunctionalVideoVos(badVideoList);

        FunctionalVideoListResponse response = new FunctionalVideoListResponse();

        response.setHighFrequencyFunctionalVideoList(addUrl(bad));

        response.setHighScoreFunctionalVideoList(addUrl(height));

        return response;

    }

    @Override
    public FunctionalVideoListResponse getFunctionalVideoEditList(String vehicleId, String language) {

        // TODO 获取功能表现视频列表数据
        List<FunctionVideoListVo> goodVideoList = functionDomainVideoDao.selectListQ(vehicleId, 1, language);

        // bad
        List<FunctionVideoListVo> badVideoList = functionDomainVideoDao.selectListQ(vehicleId, 0, language);

        List<FunctionalVideoVo> height = getFunctionalVideoVos(goodVideoList);

        List<FunctionalVideoVo> bad = getFunctionalVideoVos(badVideoList);

        FunctionalVideoListResponse response = new FunctionalVideoListResponse();

        response.setHighFrequencyFunctionalVideoList(bad);

        response.setHighScoreFunctionalVideoList(height);

        return response;

    }

    @Override
    public FunctionDomainVideoVo getVideoUrlDetail(FunctionalVideoVoF functionalVideoVo) {

        FunctionDomainVideoVo functionDomainVideoVo = new FunctionDomainVideoVo();
        functionDomainVideoVo.setVideoId(functionalVideoVo.getId());
        functionDomainVideoVo.setVideoCoverUrl(aliYunOSSService.queryPhoto(functionalVideoVo.getVideoCover()));
        functionDomainVideoVo.setVideoUrl(aliYunOSSService.queryVideo(functionalVideoVo.getVideoUrl()));

        FunctionDomainVideoEntity functionDomainVideoEntity = functionDomainVideoDao
                .selectById(functionalVideoVo.getId());
        /**
         * 非空判断
         */
        if (Objects.nonNull(functionDomainVideoEntity.getDescription())) {
            functionDomainVideoVo.setDescription(functionDomainVideoEntity.getDescription());
        }
        if (Objects.nonNull(functionDomainVideoEntity.getTitle())) {
            functionDomainVideoVo.setTitle(functionDomainVideoEntity.getTitle());
        }
        if (Objects.nonNull(functionDomainVideoEntity.getTaskType())) {
            functionDomainVideoVo.setTaskType(functionDomainVideoEntity.getTaskType());
        }
        if (Objects.nonNull(functionDomainVideoEntity.getDescriptionEn())) {
            functionDomainVideoVo.setDescriptionEn(functionDomainVideoEntity.getDescriptionEn());
        }
        if (Objects.nonNull(functionDomainVideoEntity.getTitleEn())) {
            functionDomainVideoVo.setTitleEn(functionDomainVideoEntity.getTitleEn());
        }
        if (Objects.nonNull(functionDomainVideoEntity.getTaskTypeEn())) {
            functionDomainVideoVo.setTaskTypeEn(functionDomainVideoEntity.getTaskTypeEn());
        }
        if (Objects.nonNull(functionDomainVideoEntity.getStatus())) {
            functionDomainVideoVo.setStatus(functionDomainVideoEntity.getStatus());
        }

        return functionDomainVideoVo;
    }

    @Override
    public Boolean updateFunctionDomainVideoInfo(FunctionDomainVideoVo functionDomainVideoVo) {

        FunctionDomainVideoEntity functionDomainVideoEntity = new FunctionDomainVideoEntity();

        functionDomainVideoEntity.setId(Long.valueOf(functionDomainVideoVo.getVideoId()));

        functionDomainVideoEntity.setTaskType(functionDomainVideoVo.getTaskType());

        functionDomainVideoEntity.setTaskTypeEn(functionDomainVideoVo.getTaskTypeEn());

        functionDomainVideoEntity.setTitle(functionDomainVideoVo.getTitle());

        functionDomainVideoEntity.setTitleEn(functionDomainVideoVo.getTitleEn());

        functionDomainVideoEntity.setDescription(functionDomainVideoVo.getDescription());

        functionDomainVideoEntity.setDescriptionEn(functionDomainVideoVo.getDescriptionEn());

        functionDomainVideoEntity.setStatus(functionDomainVideoVo.getStatus());

        return functionDomainVideoDao.updateById(functionDomainVideoEntity) > 0;
    }

    @Override
    public FunctionalVideoNewListResponse getFunctionalVideoNewList(String vehicleId, String language, Integer page) {

        // 计算分页参数
        int pageSize;
        int offset;

        if (page == null || page <= 1) {
            // 第一页：每种视频返回5条
            pageSize = 5;
            offset = 0;
        } else {
            // 后续页：每种视频返回2条
            pageSize = 2;
            offset = 5 + (page - 2) * 2; // 第一页5条 + (当前页-2)*2条
        }

        List<FunctionalVideoNewVo> goodVideoList = functionDomainVideoDao.selectListNewQWithPagination(vehicleId, 1,
                language, offset, pageSize);

        // bad
        List<FunctionalVideoNewVo> badVideoList = functionDomainVideoDao.selectListNewQWithPagination(vehicleId, 0,
                language, offset, pageSize);

        FunctionalVideoNewListResponse response = new FunctionalVideoNewListResponse();

        response.setHighFrequencyFunctionalVideoList(addNewUrl(badVideoList));

        response.setHighScoreFunctionalVideoList(addNewUrl(goodVideoList));

        return response;

    }

    private List<FunctionalVideoVo> addUrl(List<FunctionalVideoVo> height) {
        ConcurrentLinkedQueue<FunctionalVideoVo> heightUrl = new ConcurrentLinkedQueue<>();
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        for (FunctionalVideoVo functionalVideoVo : height) {
            executorService.submit(() -> {

                FunctionalVideoVo functionalVideoVo1 = new FunctionalVideoVo();
                functionalVideoVo1.setId(functionalVideoVo.getId());
                functionalVideoVo1.setSrtUrl(aliYunOSSService.queryStr(functionalVideoVo.getId() + "-en.srt"));
                // functionalVideoVo1.setVideoName(functionalVideoVo.getVideoName());
                functionalVideoVo1.setVideoName(functionalVideoVo.getTitle());
                functionalVideoVo1.setVideoId(functionalVideoVo.getVideoId());
                functionalVideoVo1.setVideoCover(aliYunOSSService.queryPhoto(functionalVideoVo.getVideoCover()));
                functionalVideoVo1.setVideoUrl(aliYunOSSService.queryVideo(functionalVideoVo.getVideoUrl()));
                functionalVideoVo1.setTitle(functionalVideoVo.getTitle());
                if (functionalVideoVo1.getVideoUrl() != null && functionalVideoVo1.getVideoCover() != null) {
                    heightUrl.add(functionalVideoVo1); // 线程安全的添加操作
                }
            });
        }

        executorService.shutdown(); // 关闭线程池
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // 等待所有任务完成
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new ArrayList<>(heightUrl); // 转换为 List 返回
    }

    private List<FunctionalVideoNewVo> addNewUrl(List<FunctionalVideoNewVo> height) {
        ConcurrentLinkedQueue<FunctionalVideoNewVo> heightUrl = new ConcurrentLinkedQueue<>();
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        for (FunctionalVideoNewVo functionalVideoVo : height) {
            // executorService.submit(() -> {

            FunctionalVideoNewVo functionalVideoVo1 = new FunctionalVideoNewVo();
            functionalVideoVo1.setFunctionalDomain(functionalVideoVo.getFunctionalDomain());
            functionalVideoVo1.setId(functionalVideoVo.getId());
            functionalVideoVo1.setTitle(functionalVideoVo.getTitle());
            functionalVideoVo1.setDescription(functionalVideoVo.getDescription());
            if (!"".equals(functionalVideoVo.getTaskType())) {
                functionalVideoVo1.setTaskType(functionalVideoVo.getTaskType());
            }
            functionalVideoVo1.setThreeTag(functionalVideoVo.getThreeTag());
            // functionalVideoVo1.setSrtUrl(aliYunOSSService.queryStr(functionalVideoVo.getId()+"-en.srt"));
            functionalVideoVo1.setVideoName(functionalVideoVo.getTitle());
            functionalVideoVo1.setName(functionalVideoVo.getName());
            functionalVideoVo1.setVideoId(functionalVideoVo.getVideoId());
            functionalVideoVo1.setVideoUrl(aliYunOSSService.queryVideo(functionalVideoVo.getVideoUrl()));
            if (functionalVideoVo1.getVideoUrl() != null) {
                heightUrl.add(functionalVideoVo1); // 线程安全的添加操作
            }
            // });
        }

        executorService.shutdown(); // 关闭线程池
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // 等待所有任务完成
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new ArrayList<>(heightUrl); // 转换为 List 返回
    }

    private List<FunctionalVideoVo> getFunctionalVideoVos(List<FunctionVideoListVo> goodVideoList) {

        Map<String, List<FunctionVideoListVo>> groupedData = goodVideoList.stream()
                .collect(Collectors.groupingBy(FunctionVideoListVo::getName));

        List<FunctionalVideoVo> result = new ArrayList<>();

        for (Map.Entry<String, List<FunctionVideoListVo>> entry : groupedData.entrySet()) {
            String functionDomainName = entry.getKey();
            List<FunctionVideoListVo> group = entry.getValue();

            Map<Integer, List<FunctionVideoListVo>> groupedData1 = group.stream()
                    .collect(Collectors.groupingBy(FunctionVideoListVo::getFileType));

            // List<String> urlq = new ArrayList<>();

            if (Objects.nonNull(groupedData1.get(0))) {
                groupedData1.get(0).stream().forEach(it -> {
                    FunctionalVideoVo functionalVideoVo = new FunctionalVideoVo();

                    functionalVideoVo.setId(it.getId());

                    functionalVideoVo.setVideoId(it.getUrlName());

                    functionalVideoVo.setVideoName(functionDomainName);

                    functionalVideoVo.setVideoUrl(it.getUrlName());

                    functionalVideoVo.setTitle(it.getTitle());

                    List<FunctionVideoListVo> functionVideoListVos = groupedData1.get(1);
                    Map<String, String> resultMap = new HashMap<>();
                    for (FunctionVideoListVo s : functionVideoListVos) {
                        resultMap.put(
                                s.getUrlName().substring(18), s.getUrlName());
                    }
                    if (resultMap.get(it.getUrlName().substring(0, it.getUrlName().length() - 18)) != null) {
                        String s = resultMap.get(it.getUrlName().substring(0, it.getUrlName().length() - 18));
                        functionalVideoVo.setVideoCover(s);
                    }
                    result.add(functionalVideoVo);
                });
            }

        }

        return result;
    }

    /**
     * 数据批量导入
     * 
     * @param multipartFile
     * @return
     */
    @Override
    public Result<Void> batchDataImport(MultipartFile multipartFile) {
        try {
            log.info("daomain tree 批量数据导入 开始");
            List<DomainTreeDataImportTemplateVo> importTemplateVos = ExcelUtils.readMultipartFile(multipartFile,
                    DomainTreeDataImportTemplateVo.class);
            if (CollectionUtils.isEmpty(importTemplateVos)) {
                return Result.error("解析数据为空");
            }
            log.info("表解析出的数据为：{}", JsonUtils.objToJson(importTemplateVos));
            Map<String, Long> functionMap = null;
            // 查询所有的function domain
            List<FunctionalDomainEntity> functionalDomainEntities = functionalDomainDao.queryIdAndDomainName();
            functionMap = functionalDomainEntities.stream().collect(
                    Collectors.toMap(FunctionalDomainEntity::getFunctionalDomainName, FunctionalDomainEntity::getId));

            // 拆分数据
            List<DomainTreeDataImportVo> collect = importTemplateVos.stream().map(it -> {
                DomainTreeDataImportVo importVo = new DomainTreeDataImportVo();
                BeanUtils.copyProperties(it, importVo);
                // 解析车型 车名称和型号
                if (importVo.getVehicleName().contains("-")) {
                    int i = importVo.getVehicleName().indexOf("-");
                    importVo.setVehicleName(it.getVehicleName().substring(0, i));
                    importVo.setVehicleModel(it.getVehicleName().substring(i + 1));
                }
                // 解析功能域
                if (importVo.getFunctionDomain().contains("-")) {
                    String[] split = importVo.getFunctionDomain().split("-");
                    importVo.setDomainTag(split[0]);
                    importVo.setFunctionDomain(split.length >= 2 ? split[1] : null);
                }
                return importVo;
            }).collect(Collectors.toList());

            // 获取功能域
            if (CollectionUtils.isEmpty(functionMap)) {
                List<FunctionalDomainEntity> entityList = collect.stream()
                        .map(DomainTreeDataImportVo::getFunctionDomain).distinct().map(it -> {
                            FunctionalDomainEntity functionalDomainEntity = new FunctionalDomainEntity();
                            functionalDomainEntity.setId(idWorker.nextId());
                            functionalDomainEntity.setFunctionalDomainName(it);
                            functionalDomainEntity.setStatus(1);
                            return functionalDomainEntity;
                        }).collect(Collectors.toList());
                functionalDomainDao.insert(entityList);
                functionMap = entityList.stream().collect(Collectors
                        .toMap(FunctionalDomainEntity::getFunctionalDomainName, FunctionalDomainEntity::getId));
            }
            // 数据处理
            for (DomainTreeDataImportVo importVo : collect) {
                // 判断当前品牌是否存在
                Long brandId = brandDao.getBrandIdByName(importVo.getVehicleName());
                if (Objects.isNull(brandId)) {
                    brandId = idWorker.nextId();
                    BrandEntity entity = new BrandEntity();
                    entity.setId(brandId);
                    entity.setBrand(importVo.getVehicleName());
                    brandDao.insert(entity);
                }
                importVo.setVehicleId(brandId);

                // 设置 汽车详情
                Long brandInfoId = baseInfoDao.findByBrandIdAndVehicleModel(importVo.getVehicleId(),
                        importVo.getVehicleModel());
                if (Objects.isNull(brandInfoId)) {
                    brandInfoId = idWorker.nextId();
                    BaseInfoEntity entity = new BaseInfoEntity();
                    entity.setId(brandInfoId);
                    entity.setStatus(1);
                    entity.setBrandId(importVo.getVehicleId());
                    entity.setVehicleModel(importVo.getVehicleModel());
                    entity.setVehicleSystemVersion(importVo.getSystemVersion());
                    entity.setTestDate(DateUtils.stringToDate(importVo.getTestDate(), "yyyy.MM.dd"));
                    entity.setCreateDate(new Date());
                    entity.setUpdateDate(new Date());
                    entity.setIsDelete(0);
                    baseInfoDao.insert(entity);
                }
                importVo.setVehicleInfoId(brandInfoId);

                // 设置指标 判断当前
                Long functionId = functionMap.get(importVo.getFunctionDomain());
                if (Objects.isNull(functionId)) {
                    functionId = idWorker.nextId();
                    FunctionalDomainEntity functionalDomainEntity = new FunctionalDomainEntity();
                    functionalDomainEntity.setId(functionId);
                    functionalDomainEntity.setFunctionalDomainName(importVo.getFunctionDomain());
                    functionalDomainEntity.setStatus(1);
                    functionalDomainDao.insert(functionalDomainEntity);
                    functionMap.put(importVo.getFunctionDomain(), functionId);
                }
                Long indexId = domainIndexDao.findIdByFunctionalDomainAndIndexName(functionId,
                        importVo.getDomainIndex());
                if (Objects.isNull(indexId)) {
                    indexId = idWorker.nextId();
                    DomainIndexEntity indexEntity = new DomainIndexEntity();
                    indexEntity.setId(indexId)
                            .setStatus(1)
                            .setFunctionalDomain(functionId)
                            .setIndexName(importVo.getDomainIndex());
                    domainIndexDao.insert(indexEntity);
                }
                importVo.setDomainIndexId(indexId);
                // 判断是否存在
                Long treeId = domainTreeDao.findIdByDomainIndexIdAndVehicleBaseId(importVo.getDomainIndexId(),
                        importVo.getVehicleInfoId());
                if (Objects.isNull(treeId)) {
                    // 设置tree
                    DomainTreeEntity treeEntity = new DomainTreeEntity();
                    treeEntity.setDomainIndexId(importVo.getDomainIndexId())
                            .setId(idWorker.nextId())
                            .setVehicleBaseId(importVo.getVehicleInfoId())
                            .setTestDate(DateUtils.stringToDate(importVo.getTestDate(), "yyyy.MM.dd"))
                            .setPresentationValue(importVo.getShowData());
                    if (Objects.nonNull(importVo.getCalculateData())) {
                        double calculateActual = importVo.getCalculateData();
                        BigDecimal two = new BigDecimal(calculateActual);
                        double three = two.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        treeEntity.setCalculateScore(three);
                    }
                    // 保存
                    this.save(treeEntity);
                }
            }
            log.info("daomain tree 批量数据导入 结束");
        } catch (Exception e) {
            log.error("批量导入数据解析失败：{}", e.getMessage(), e);
            return Result.error();
        }
        return Result.ok();
    }

    /**
     * 根据车品牌id 获取每个域下的平均值和个体值
     * 
     * @return
     */
    @Override
    public Result<List<DomainTreeQueryScoreResponse>> queryScoreByBrandInfo(DomainTreeQueryScoreRequest request) {

        log.info("功能域表现总体查询 请求参数：{}", JsonUtils.objToJson(request));
        // 查询所有的功能域的 的名称和平均值
        List<FunctionalDomainAvgScoreVo> functionalDomainAvgScoreVos = functionalDomainDao.queryDomainAveScore("功能域表现",
                request.getLanguage());
        if (CollectionUtils.isEmpty(functionalDomainAvgScoreVos)) {
            log.info("查询出域对应的平均值的数据为空");
            return Result.error("查询数据为空");
        }
        List<DomainTreeQueryScoreResponse> queryScoreResponses = new ArrayList<>(
                functionalDomainAvgScoreVos.stream().map(it -> {
                    BigDecimal two = new BigDecimal(it.getIndustryAvgScore());
                    double three = two.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                    // 返回主要信息
                    DomainTreeQueryScoreResponse response = new DomainTreeQueryScoreResponse();
                    response.setIndustryAvgScore(three)
                            .setFunctionDomainId(it.getFunctionDomainId())
                            .setFunctionDomainName(it.getFunctionalDomainName());
                    // 查询车辆的信息
                    List<VehicleInfoContScoreVo> vehicleInfoContScoreVos = baseInfoDao.queryVehicleInfoAndCountScore(
                            request.getBrandBaseInfoId(),
                            it.getFunctionDomainId(), "功能域表现", request.getLanguage());
                    List<DomainTreeQueryScoreResponse.VehicleScoreResponse> vehicleScoreResponses = new ArrayList<>(
                            vehicleInfoContScoreVos.stream().map(vo -> {
                                BigDecimal decimal = new BigDecimal(vo.getAvgScore());
                                double score = decimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                                DomainTreeQueryScoreResponse.VehicleScoreResponse vehicleResponse = new DomainTreeQueryScoreResponse.VehicleScoreResponse(
                                        null, null, null);
                                vehicleResponse.setVehicleInfoScore(score)
                                        .setVehicleInfoId(vo.getId())
                                        .setVehicleInfoName(vo.getVehicleModelInfo());
                                return vehicleResponse;
                            }).collect(Collectors.toList()));
                    // 为每个请求的车辆添加同步的数据（如果存在）
                    for (Long vehicleId : request.getBrandBaseInfoId()) {
                        // 检查是否有同步的功能域分数数据（type=1）
                        VehicleDomainScoreEntity syncScore = vehicleDomainScoreDao.selectOne(
                                Wrappers.<VehicleDomainScoreEntity>lambdaQuery()
                                        .eq(VehicleDomainScoreEntity::getVehicleId, vehicleId)
                                        .eq(VehicleDomainScoreEntity::getDomainId,
                                                Long.parseLong(it.getFunctionDomainId()))
                                        .eq(VehicleDomainScoreEntity::getType, (short) 1));

                        if (syncScore != null) {
                            // 检查是否已经存在该车辆的分数（避免重复添加）
                            boolean alreadyExists = vehicleScoreResponses.stream()
                                    .anyMatch(v -> v.getVehicleInfoId().equals(vehicleId.toString()));

                            if (!alreadyExists) {
                                DomainTreeQueryScoreResponse.VehicleScoreResponse syncVehicleResponse = new DomainTreeQueryScoreResponse.VehicleScoreResponse(
                                        null, null, null);
                                syncVehicleResponse.setVehicleInfoScore(syncScore.getScore().doubleValue())
                                        .setVehicleInfoId(vehicleId.toString())
                                        .setVehicleInfoName(
                                                getVehicleNameById(vehicleId.toString(), request.getLanguage()));

                                vehicleScoreResponses.add(syncVehicleResponse);
                                log.debug("为车辆{}添加同步的功能域分数: domainId={}, score={}",
                                        vehicleId, it.getFunctionDomainId(), syncScore.getScore());
                            }
                        }
                    }
                    response.setVehicleScore(vehicleScoreResponses);
                    return response;
                }).toList());

        queryScoreResponses.sort(new Comparator<DomainTreeQueryScoreResponse>() {
            @Override
            public int compare(DomainTreeQueryScoreResponse o1, DomainTreeQueryScoreResponse o2) {
                return Long.compare(Long.parseLong(o2.getFunctionDomainId()), Long.parseLong(o1.getFunctionDomainId())); // 降序排序
            }
        });

        // 定义排序顺序
        Map<String, Integer> orderMap = new HashMap<>();
        if (request.getLanguage().equals("en")) {
            orderMap.put("Vehicle Control", 1);
            orderMap.put("Travel", 2);
            orderMap.put("Vehicle Knowledge", 3);
            orderMap.put("Casual Chat", 4);
            orderMap.put("Entertainment", 5);
            orderMap.put("Creation", 6);
        } else {
            orderMap.put("车控域", 1);
            orderMap.put("出行域", 2);
            orderMap.put("车书域", 3);
            orderMap.put("闲聊域", 4);
            orderMap.put("娱乐域", 5);
            orderMap.put("创作域", 6);
        }

        // 使用Comparator进行排序
        queryScoreResponses.sort(Comparator.comparingInt(response -> orderMap.get(response.getFunctionDomainName())));

        return Result.ok(queryScoreResponses);
    }

    /**
     * 根据车辆品牌id 获取每个指标的分数
     * 
     * @param request
     * @return
     */
    @Override
    public List<DomainTreeQueryIndexScoreResponse> queryDomainIndexScore(DomainTreeQueryScoreRequest request,
            String language) {
        // 查询每个指标 和 平均分
        List<DomainIndexAvgScoreVo> domainAvgScore = domainIndexDao.findDomainAvgScore(request.getFunctionalDomain(),
                language);
        if (CollectionUtils.isEmpty(domainAvgScore)) {
            // return Result.error("查询数据为空");
        }
        List<DomainTreeQueryIndexScoreResponse> responses = domainAvgScore.stream().map(it -> {
            BigDecimal two = new BigDecimal(
                    Objects.nonNull(it.getDoMainIndexAvgScore()) ? it.getDoMainIndexAvgScore() : 0);
            double three = two.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            DomainTreeQueryIndexScoreResponse response = new DomainTreeQueryIndexScoreResponse();
            response.setDomainIndexId(it.getDoMainIndexId())
                    .setIndexAvgScore(three)
                    .setDomainIndexName(it.getDoMainIndexName())
                    .setIndexDetail(it.getIndexDetail());
            // 检查是否只包含 0
            if (request.getBrandBaseInfoId().stream().allMatch(id -> id == 0L)) {
                // 如果只包含 0，设置为空列表
                request.setBrandBaseInfoId(new ArrayList<>());
            }
            List<VehicleInfoDomainIndexScoreVo> domainVehicleInfoId = domainIndexDao
                    .findDomainVehicleInfoId(request.getBrandBaseInfoId(), it.getDoMainIndexId(), language);
            List<DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse> list = domainVehicleInfoId.stream()
                    .map(vo -> {
                        BigDecimal decimal = new BigDecimal(vo.getAvgScore());
                        double avgCore = decimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

                        DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse vehicleResponse = new DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse();
                        vehicleResponse.setVehicleInfoScore(avgCore)
                                .setVehicleInfoId(vo.getId())
                                .setVehicleInfoName(vo.getVehicleModelInfo())
                                .setVehicleSystemVersion(vo.getVehicleSystemVersion())
                                .setTestDate(vo.getTestDate());
                        return vehicleResponse;
                    }).collect(Collectors.toList());
            response.setVehicleIndexScore(list);
            return response;
        }).toList();
        // 批量查询同步的三级指标分数数据（type=2），优化性能
        if (!CollectionUtils.isEmpty(request.getBrandBaseInfoId())) {
            // 1. 批量查询所有请求车辆的同步数据
            List<VehicleDomainScoreEntity> allSyncScores = vehicleDomainScoreDao.selectList(
                    Wrappers.<VehicleDomainScoreEntity>lambdaQuery()
                            .in(VehicleDomainScoreEntity::getVehicleId, request.getBrandBaseInfoId())
                            .eq(VehicleDomainScoreEntity::getType, (short) 2));

            if (!CollectionUtils.isEmpty(allSyncScores)) {
                // 2. 获取所有相关的domainIndexId，用于批量查询功能域信息
                Set<Long> domainIndexIds = allSyncScores.stream()
                        .map(VehicleDomainScoreEntity::getDomainId)
                        .collect(Collectors.toSet());

                // 3. 批量查询domainIndex信息，建立domainId -> functionalDomain的映射
                Map<Long, Long> domainIndexToFunctionalDomainMap;
                if (!CollectionUtils.isEmpty(domainIndexIds)) {
                    List<DomainIndexEntity> domainIndices = domainIndexDao.selectBatchIds(domainIndexIds);
                    domainIndexToFunctionalDomainMap = domainIndices.stream()
                            .collect(
                                    Collectors.toMap(DomainIndexEntity::getId, DomainIndexEntity::getFunctionalDomain));
                } else {
                    domainIndexToFunctionalDomainMap = new HashMap<>();
                }

                // 4. 过滤出属于请求功能域的同步数据
                Long requestedFunctionalDomain = request.getFunctionalDomain();
                List<VehicleDomainScoreEntity> filteredSyncScores = allSyncScores.stream()
                        .filter(syncScore -> {
                            Long functionalDomain = domainIndexToFunctionalDomainMap.get(syncScore.getDomainId());
                            return functionalDomain != null && functionalDomain.equals(requestedFunctionalDomain);
                        })
                        .collect(Collectors.toList());

                // 5. 将同步数据按domainId分组，便于快速查找
                Map<Long, List<VehicleDomainScoreEntity>> syncScoresByDomainId = filteredSyncScores.stream()
                        .collect(Collectors.groupingBy(VehicleDomainScoreEntity::getDomainId));

                // 6. 批量获取车辆信息，建立vehicleId -> 车辆信息的映射
                Map<String, VehicleInfo> vehicleInfoMap = getVehicleInfoBatch(request.getBrandBaseInfoId(), language);

                // 7. 为每个指标响应对象添加对应的同步数据
                for (DomainTreeQueryIndexScoreResponse response : responses) {
                    Long domainIndexId = Long.parseLong(response.getDomainIndexId());
                    List<VehicleDomainScoreEntity> domainSyncScores = syncScoresByDomainId.get(domainIndexId);

                    if (!CollectionUtils.isEmpty(domainSyncScores)) {
                        List<DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse> vehicleScores = response
                                .getVehicleIndexScore();
                        if (vehicleScores == null) {
                            vehicleScores = new ArrayList<>();
                            response.setVehicleIndexScore(vehicleScores);
                        }

                        // 获取已存在的车辆ID集合，用于去重
                        Set<String> existingVehicleIds = vehicleScores.stream()
                                .map(DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse::getVehicleInfoId)
                                .collect(Collectors.toSet());

                        for (VehicleDomainScoreEntity syncScore : domainSyncScores) {
                            String vehicleIdStr = syncScore.getVehicleId().toString();

                            // 检查是否已经存在，避免重复添加
                            if (!existingVehicleIds.contains(vehicleIdStr)) {
                                DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse syncVehicleScore = new DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse();

                                syncVehicleScore.setVehicleInfoScore(syncScore.getScore().doubleValue());
                                syncVehicleScore.setVehicleInfoId(vehicleIdStr);

                                // 从预加载的车辆信息中获取数据
                                VehicleInfo vehicleInfo = vehicleInfoMap.get(vehicleIdStr);
                                if (vehicleInfo != null) {
                                    syncVehicleScore.setVehicleInfoName(vehicleInfo.getName());
                                    syncVehicleScore.setVehicleSystemVersion(vehicleInfo.getSystemVersion());
                                    syncVehicleScore.setTestDate(vehicleInfo.getTestDate());
                                } else {
                                    // 降级处理
                                    syncVehicleScore.setVehicleInfoName(getVehicleNameById(vehicleIdStr, language));
                                    syncVehicleScore.setVehicleSystemVersion(getVehicleSystemVersion(vehicleIdStr));
                                    syncVehicleScore.setTestDate(getVehicleTestDate(vehicleIdStr));
                                }

                                vehicleScores.add(syncVehicleScore);
                                existingVehicleIds.add(vehicleIdStr); // 更新已存在集合
                            }
                        }

                        log.debug("为指标{}添加了{}条同步数据", domainIndexId, domainSyncScores.size());
                    }
                }

                log.info("批量添加同步数据完成，共处理{}条同步记录", filteredSyncScores.size());
            }
        }

        return responses;
    }

    public void addVehicleScoreToDomainList(Long functionalDomain,
            List<DomainTreeQueryIndexScoreResponse> domainList, String language) {

        // 1. 创建功能域ID到指标分数的映射
        Map<String, Double> domainIndexScores = createDomainIndexScoreMap(functionalDomain);

        // 2. 准备固定值的车辆信息
        DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse vehicleScore = new DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse();
        if (!language.equals("en")) {
            vehicleScore.setVehicleInfoName("日产-N7");
            vehicleScore.setVehicleInfoId("1937700291038908417");
            vehicleScore.setVehicleSystemVersion("NISSAN OS1.0.0");
        } else {
            vehicleScore.setVehicleInfoName("NISSAN-N7");
            vehicleScore.setVehicleInfoId("1937700291038908417");
            vehicleScore.setVehicleSystemVersion("NISSAN OS1.0.0");
        }
        Date testDate = null;
        // 设置固定日期 (2025-06-17)
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            testDate = sdf.parse("2025-06-17 00:00:00");
            vehicleScore.setTestDate(testDate);
        } catch (ParseException e) {
            // 使用当前日期作为后备
            vehicleScore.setTestDate(new Date());
        }

        // 3. 遍历所有域指标
        for (DomainTreeQueryIndexScoreResponse domain : domainList) {
            // 获取当前指标的车辆分数列表
            List<DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse> vehicleScoreList = domain
                    .getVehicleIndexScore();

            if (Long.parseLong(domain.getDomainIndexId()) == 451751102942019630L) {
                domain.setIndexAvgScore(3.2);
            }
            if (Long.parseLong(domain.getDomainIndexId()) == 451751102942019666L) {
                domain.setIndexAvgScore(3.2857);
            }

            // 处理列表不可变的情况
            if (vehicleScoreList == null) {
                // 如果列表为空，创建新的可变列表
                vehicleScoreList = new ArrayList<>();
                domain.setVehicleIndexScore(vehicleScoreList);
            } else {
                // 尝试添加一个临时元素来检测是否可变
                try {
                    vehicleScoreList.add(null);
                    vehicleScoreList.remove(vehicleScoreList.size() - 1);
                } catch (UnsupportedOperationException e) {
                    // 如果是不可变列表，创建新的可变列表并复制元素
                    List<DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse> mutableList = new ArrayList<>(
                            vehicleScoreList);
                    domain.setVehicleIndexScore(mutableList);
                    vehicleScoreList = mutableList;
                }
            }

            // 获取当前指标的ID
            String indexId = domain.getDomainIndexId();

            // 检查该指标是否属于当前功能域
            if (domainIndexScores.containsKey(indexId)) {
                // 创建新的车辆评分对象
                DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse newVehicleScore = new DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse();
                if (!language.equals("en")) {
                    newVehicleScore.setVehicleInfoName("日产-N7");
                    newVehicleScore.setVehicleInfoId("1937700291038908417");
                    newVehicleScore.setVehicleSystemVersion("NISSAN OS1.0.0");
                } else {
                    newVehicleScore.setVehicleInfoName("NISSAN-N7");
                    newVehicleScore.setVehicleInfoId("1937700291038908417");
                    newVehicleScore.setVehicleSystemVersion("NISSAN OS1.0.0");
                }
                newVehicleScore.setTestDate(testDate);
                newVehicleScore.setVehicleInfoScore(domainIndexScores.get(indexId));

                // 添加到列表
                vehicleScoreList.add(newVehicleScore);
            }
        }
    }

    public void addVehicleScoreToDomainList2(Long functionalDomain,
            List<DomainTreeQueryIndexScoreResponse> domainList, String language) {

        // 1. 创建功能域ID到指标分数的映射
        Map<String, Double> domainIndexScores = createDomainIndexScoreMap2(functionalDomain);

        // 2. 准备固定值的车辆信息
        DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse vehicleScore = new DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse();
        if (!language.equals("en")) {
            vehicleScore.setVehicleInfoName("零跑-B10");
            vehicleScore.setVehicleInfoId("448078678031597645");
            vehicleScore.setVehicleSystemVersion("leap OS3.00.10");
        } else {
            vehicleScore.setVehicleInfoName("Leapmotor-B10");
            vehicleScore.setVehicleInfoId("448078678031597645");
            vehicleScore.setVehicleSystemVersion("leap OS3.00.10");
        }
        Date testDate = null;
        // 设置固定日期 (2025-06-17)
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            testDate = sdf.parse("2025-06-17 00:00:00");
            vehicleScore.setTestDate(testDate);
        } catch (ParseException e) {
            // 使用当前日期作为后备
            vehicleScore.setTestDate(new Date());
        }

        // 3. 遍历所有域指标
        for (DomainTreeQueryIndexScoreResponse domain : domainList) {
            // 获取当前指标的车辆分数列表
            List<DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse> vehicleScoreList = domain
                    .getVehicleIndexScore();

            if (Long.parseLong(domain.getDomainIndexId()) == 451751102942019630L) {
                domain.setIndexAvgScore(3.2);
            }
            if (Long.parseLong(domain.getDomainIndexId()) == 451751102942019666L) {
                domain.setIndexAvgScore(3.2857);
            }

            // 处理列表不可变的情况
            if (vehicleScoreList == null) {
                // 如果列表为空，创建新的可变列表
                vehicleScoreList = new ArrayList<>();
                domain.setVehicleIndexScore(vehicleScoreList);
            } else {
                // 尝试添加一个临时元素来检测是否可变
                try {
                    vehicleScoreList.add(null);
                    vehicleScoreList.remove(vehicleScoreList.size() - 1);
                } catch (UnsupportedOperationException e) {
                    // 如果是不可变列表，创建新的可变列表并复制元素
                    List<DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse> mutableList = new ArrayList<>(
                            vehicleScoreList);
                    domain.setVehicleIndexScore(mutableList);
                    vehicleScoreList = mutableList;
                }
            }

            // 获取当前指标的ID
            String indexId = domain.getDomainIndexId();

            // 检查该指标是否属于当前功能域
            if (domainIndexScores.containsKey(indexId)) {
                // 创建新的车辆评分对象
                DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse newVehicleScore = new DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse();
                if (!language.equals("en")) {
                    newVehicleScore.setVehicleInfoName("零跑-B10");
                    newVehicleScore.setVehicleInfoId("448078678031597645");
                    newVehicleScore.setVehicleSystemVersion("leap OS3.00.10");
                } else {
                    newVehicleScore.setVehicleInfoName("Leapmotor-B10");
                    newVehicleScore.setVehicleInfoId("448078678031597645");
                    newVehicleScore.setVehicleSystemVersion("leap OS3.00.10");
                }
                newVehicleScore.setTestDate(testDate);
                newVehicleScore.setVehicleInfoScore(domainIndexScores.get(indexId));

                // 添加到列表
                vehicleScoreList.add(newVehicleScore);
            }
        }
    }

    public void filterResponses(Long targetVehicleId, Long removeIndexId, List<Long> idList,
            List<DomainTreeQueryIndexScoreResponse> responses) {

        // 检查目标ID是否存在于Long列表中
        if (idList.contains(targetVehicleId)) {
            Iterator<DomainTreeQueryIndexScoreResponse> iterator = responses.iterator();
            while (iterator.hasNext()) {
                DomainTreeQueryIndexScoreResponse response = iterator.next();
                // 比较字符串类型的ID（注意处理null）
                if (response.getDomainIndexId() != null &&
                        response.getDomainIndexId().equals(String.valueOf(removeIndexId))) {
                    iterator.remove(); // 安全删除当前元素
                }
            }
        }
    }

    // 创建功能域ID到指标分数的映射
    private Map<String, Double> createDomainIndexScoreMap(Long functionalDomain) {
        Map<String, Double> scoreMap = new HashMap<>();

        // 根据功能域ID添加对应的指标分数
        if (functionalDomain == 450679990120349703L) { // 车控域
            scoreMap.put("451751102942019625", 3.125); // 复杂指令识别
            scoreMap.put("451751102942019627", 1.0667); // 模态丰富性
            scoreMap.put("451751102942019626", 1.8); // 模糊意图识别
            scoreMap.put("451751102942019628", 2.7547); // 直接指令识别
            scoreMap.put("451751102942019630", 3.2); // 上下文记忆

        } else if (functionalDomain == 450679990120349705L) { // 出行域
            scoreMap.put("452214388338720768", 3.0); // 上下文记忆
            scoreMap.put("451751102942019607", 2.5); // 出行生态信息
            scoreMap.put("451751102942019612", 3.6471); // 回答质量
            scoreMap.put("451751102942019609", 2.7368); // 复杂指令识别
            scoreMap.put("451751102942019611", 3.0); // 模糊意图识别
            scoreMap.put("451751102942019608", 3.6364); // 生活生态信息
            scoreMap.put("451751102942019610", 3.5714); // 直接指令识别

        } else if (functionalDomain == 450679990120349704L) { // 车书域
            scoreMap.put("451751102942019603", 4.35); // 交通知识
            scoreMap.put("451751102942019606", 3.7667); // 回答质量
            scoreMap.put("451751102942019602", 3.9655); // 汽车知识
            scoreMap.put("451751102942019604", 3.5862); // 用车技巧
            scoreMap.put("451751102942019605", 3.15); // 直接指令识别
            scoreMap.put("451751102942019666", 3.2857);// 复杂指令识别

        } else if (functionalDomain == 450679990120349702L) { // 娱乐域
            scoreMap.put("451751102942019624", 2.8571); // 上下文记忆
            scoreMap.put("451751102942019621", 3.5588); // 复杂指令识别
            scoreMap.put("451751102942019623", 3.8182); // 模糊意图识别
            scoreMap.put("451751102942019622", 4.0); // 直接指令识别

        } else if (functionalDomain == 450679990120349701L) { // 闲聊域
            scoreMap.put("451751102942019620", 4.6667); // 上下文记忆
            scoreMap.put("451751102942019618", 3.8); // 回答质量
            scoreMap.put("451751102942019615", 4.0556); // 复杂指令识别
            scoreMap.put("451751102942019619", 2.1667); // 情感响应能力
            scoreMap.put("451751102942019616", 4.1538); // 直接指令识别
            scoreMap.put("451751102942019614", 1.6); // 直接指令识别

        } else if (functionalDomain == 450679990120349706L) { // 创作域
            scoreMap.put("452214396081405952", 4.1429); // 回答质量
            scoreMap.put("451751102942019633", 3.4167); // 图像生成质量
            scoreMap.put("451751102942019629", 4.25); // 复杂指令识别
            scoreMap.put("451751102942019632", 2.5625); // 情感响应能力
            scoreMap.put("451751102942019631", 3.9091); // 模糊意图识别
            scoreMap.put("451751102942019634", 4.0833); // 直接指令识别
        }

        return scoreMap;
    }

    private Map<String, Double> createDomainIndexScoreMap2(Long functionalDomain) {
        Map<String, Double> scoreMap = new HashMap<>();

        // 根据功能域ID添加对应的指标分数
        if (functionalDomain == 450679990120349703L) { // 车控域
            scoreMap.put("451751102942019625", 3.0250); // 复杂指令识别
            scoreMap.put("451751102942019627", 1.4000); // 模态丰富性
            scoreMap.put("451751102942019626", 2.5862); // 模糊意图识别
            scoreMap.put("451751102942019628", 3.0667); // 直接指令识别
            scoreMap.put("451751102942019630", 2.0000); // 上下文记忆

        } else if (functionalDomain == 450679990120349705L) { // 出行域
            scoreMap.put("452214388338720768", 2.5000); // 上下文记忆
            scoreMap.put("451751102942019607", 2.3143); // 出行生态信息
            scoreMap.put("451751102942019612", 3.8889); // 回答质量
            scoreMap.put("451751102942019609", 1.6842); // 复杂指令识别
            scoreMap.put("451751102942019611", 2.2813); // 模糊意图识别
            scoreMap.put("451751102942019608", 2.9091); // 生活生态信息
            scoreMap.put("451751102942019610", 3.7857); // 直接指令识别

        } else if (functionalDomain == 450679990120349704L) { // 车书域
            scoreMap.put("451751102942019603", 4.0500); // 交通知识
            scoreMap.put("451751102942019606", 3.7407); // 回答质量
            scoreMap.put("451751102942019602", 4.1724); // 汽车知识
            scoreMap.put("451751102942019604", 4.2500); // 用车技巧
            scoreMap.put("451751102942019605", 4.0000); // 直接指令识别
            scoreMap.put("451751102942019666", 3.1905); // 复杂指令识别

        } else if (functionalDomain == 450679990120349702L) { // 娱乐域
            scoreMap.put("451751102942019624", 1.1429); // 上下文记忆
            scoreMap.put("451751102942019621", 2.9706); // 复杂指令识别
            scoreMap.put("451751102942019623", 3.0294); // 模糊意图识别
            scoreMap.put("451751102942019622", 3.1250); // 直接指令识别

        } else if (functionalDomain == 450679990120349701L) { // 闲聊域
            scoreMap.put("451751102942019620", 3.0000); // 上下文记忆
            scoreMap.put("451751102942019618", 4.2667); // 回答质量
            scoreMap.put("451751102942019615", 4.7222); // 复杂指令识别
            scoreMap.put("451751102942019619", 2.8889); // 情感响应能力
            scoreMap.put("451751102942019616", 4.1538); // 直接指令识别

            // 新增在线搜索指标（ID需要确认）
            // scoreMap.put("在线搜索ID", 3.6000); // 在线搜索

        } else if (functionalDomain == 450679990120349706L) { // 创作域
            scoreMap.put("452214396081405952", 4.7143); // 回答质量
            scoreMap.put("451751102942019633", 3.0000); // 图像生成质量
            scoreMap.put("451751102942019629", 3.5500); // 复杂指令识别
            scoreMap.put("451751102942019632", 3.8667); // 情感响应能力
            scoreMap.put("451751102942019631", 4.0000); // 模糊意图识别
            scoreMap.put("451751102942019634", 3.5000); // 直接指令识别
        }

        return scoreMap;
    }

    /**
     * 查询总分排行
     * 
     * @param request
     * @return
     */
    @Override
    public Result<DomainTreeScoreResponse> countScoreSort(DomainTreeQueryScoreRequest request) {
        // 查询功能域所有域的 均分和 id名称
        List<FunctionalDomainAvgScoreVo> functionalDomainAvgScoreVos = functionalDomainDao.queryDomainAveScore("功能域表现",
                request.getLanguage());
        if (CollectionUtils.isEmpty(functionalDomainAvgScoreVos)) {
            log.info("查询出域对应的平均值的数据为空");
            return Result.error("查询数据为空");
        }
        // 功能域表现 各车
        List<DomainTreeCountScoreSortResponse> sortResponseList = functionalDomainAvgScoreVos.stream().map(it -> {
            BigDecimal two = new BigDecimal(it.getIndustryAvgScore());
            double three = two.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

            DomainTreeCountScoreSortResponse response = new DomainTreeCountScoreSortResponse();
            response.setFunctionDomainId(it.getFunctionDomainId())
                    .setFunctionalDomainName(it.getFunctionalDomainName())
                    .setIndustryAvgScore(three);
            // 检查是否只包含 0
            if (request.getBrandBaseInfoId().stream().allMatch(id -> id == 0L)) {
                // 如果只包含 0，设置为空列表
                request.setBrandBaseInfoId(new ArrayList<>());
            }
            // 查询车辆的信息
            List<VehicleInfoContScoreVo> vehicleInfoContScoreVos = baseInfoDao.queryVehicleInfoAndCountScore(
                    request.getBrandBaseInfoId(),
                    it.getFunctionDomainId(), "功能域表现", request.getLanguage());
            List<DomainTreeCountScoreSortResponse.VehicleIndexScoreResponse> vehicleScoreResponses = vehicleInfoContScoreVos
                    .stream().map(vo -> {
                        BigDecimal decimal = new BigDecimal(vo.getAvgScore());
                        double score = decimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                        DomainTreeCountScoreSortResponse.VehicleIndexScoreResponse vehicleResponse = new DomainTreeCountScoreSortResponse.VehicleIndexScoreResponse();
                        vehicleResponse.setVehicleInfoScore(score)
                                .setVehicleInfoId(vo.getId())
                                .setVehicleInfoName(vo.getVehicleModelInfo())
                                .setVehicleSystemVersion(vo.getVehicleSystemVersion())
                                .setTestDate(vo.getTestDate());
                        return vehicleResponse;
                    }).toList();
            response.setVehicleIndexScore(vehicleScoreResponses);
            return response;
        }).collect(Collectors.toList());

        // 查询出对应domain tag下的均分
        DomainTagAvgScoreVo tagAvgScoreVo = functionalDomainDao.queryDomainTagAvg("基础能力");
        tagAvgScoreVo.setDomainTagId("111");
        BigDecimal baseAvg = new BigDecimal(tagAvgScoreVo.getAvgScore());
        double baseAvgDb = baseAvg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        DomainTreeCountScoreSortResponse response = new DomainTreeCountScoreSortResponse();
        response.setFunctionDomainId(tagAvgScoreVo.getDomainTagId())
                .setFunctionalDomainName(tagAvgScoreVo.getDomainTagName())
                .setIndustryAvgScore(baseAvgDb);

        // 检查是否只包含 0
        if (request.getBrandBaseInfoId().stream().allMatch(id -> id == 0L)) {
            // 如果只包含 0，设置为空列表
            request.setBrandBaseInfoId(new ArrayList<>());
        }

        // 基础
        List<VehicleInfoContScoreVo> vehicleInfoContScoreVos = baseInfoDao
                .queryVehicleInfoAndCountScoreByTag(request.getBrandBaseInfoId(), "基础能力");
        List<DomainTreeCountScoreSortResponse.VehicleIndexScoreResponse> list = vehicleInfoContScoreVos.stream()
                .map(it -> {
                    BigDecimal decimal = new BigDecimal(it.getAvgScore());
                    double score = decimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                    DomainTreeCountScoreSortResponse.VehicleIndexScoreResponse vehicleResponse = new DomainTreeCountScoreSortResponse.VehicleIndexScoreResponse();
                    vehicleResponse.setVehicleInfoScore(score)
                            .setVehicleInfoId(it.getId())
                            .setVehicleInfoName(it.getVehicleModelInfo())
                            .setVehicleSystemVersion(it.getVehicleSystemVersion())
                            .setTestDate(it.getTestDate());
                    return vehicleResponse;
                }).toList();
        response.setVehicleIndexScore(list);
        sortResponseList.addFirst(response);

        DomainTreeScoreResponse convert = domainTreeCountScoreSortResponseConvertDomainTreeScoreResponse(
                sortResponseList);

        // System.out.println(JsonUtils.objToJson(convert));

        return Result.ok(convert);
    }

    /**
     * 基础能力 指标排行
     * 
     * @param request
     * @return
     */
    @Override
    public DomainTreeScoreResponse baseDomainIndexScoreSort(DomainTreeQueryScoreRequest request, String language) {
        // 根据域 tag查询下的所有indexid
        List<DomainIndexAvgScoreVo> avgScoreVoList = domainIndexDao.findDomainIndexAvgScoreByDomainTag("基础能力",
                language);
        if (CollectionUtils.isEmpty(avgScoreVoList)) {
            log.info("查询出域对应的平均值的数据为空");
        }
        List<DomainTreeQueryIndexScoreResponse> responses = avgScoreVoList.stream().map(it -> {
            BigDecimal baseDecimal = new BigDecimal(it.getDoMainIndexAvgScore());
            double baseAvg = baseDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

            DomainTreeQueryIndexScoreResponse response = new DomainTreeQueryIndexScoreResponse();
            response.setDomainIndexName(it.getDoMainIndexName())
                    .setDomainIndexId(it.getDoMainIndexId())
                    .setIndexAvgScore(baseAvg);

            // 检查是否只包含 0
            if (request.getBrandBaseInfoId().stream().allMatch(id -> id == 0L)) {
                // 如果只包含 0，设置为空列表
                request.setBrandBaseInfoId(new ArrayList<>());
            }

            // 查询每辆汽车的平均分
            List<VehicleInfoContScoreVo> vehicleInfoContScoreVos = baseInfoDao.queryVehicleInfoAndCountScoreByIndex(
                    request.getBrandBaseInfoId(), it.getDoMainIndexId(), language);
            List<DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse> scoreResponseList = vehicleInfoContScoreVos
                    .stream().map(vo -> {
                        BigDecimal decimal = new BigDecimal(vo.getAvgScore());
                        double score = decimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                        DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse vehicleResponse = new DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse();
                        vehicleResponse.setVehicleInfoScore(score)
                                .setVehicleInfoId(vo.getId())
                                .setVehicleInfoName(vo.getVehicleModelInfo())
                                .setVehicleSystemVersion(vo.getVehicleSystemVersion())
                                .setTestDate(vo.getTestDate());
                        return vehicleResponse;
                    }).toList();
            response.setVehicleIndexScore(scoreResponseList);
            return response;
        }).toList();
        DomainTreeScoreResponse convert = domainTreeQueryIndexScoreResponseConvertDomainTreeScoreResponse(responses,
                language);
        return convert;
    }

    @Override
    public List<FunctionalDomainResponse> queryDomainTree(String language) {
        return functionalDomainDao.queryDomainTree(language);
    }

    @Override
    public DomainTreeScoreResponse queryDomainIndexScoreRank(DomainTreeQueryScoreRequest request, String language) {
        List<DomainTreeQueryIndexScoreResponse> listResult = queryDomainIndexScore(request, language);
        return domainTreeQueryIndexScoreResponseConvertDomainTreeScoreResponse(listResult, language);
    }

    @Override
    public List<GeneralAbilityVo> queryGeneralAbilityVos(List<String> vehicleId, String language) {
        return functionalDomainDao.queryGeneralAbilityVos(vehicleId, language);
    }

    @Override
    public List<ActionAbilityVo> queryActionAbilityVos(List<String> vehicleId, String language) {
        return functionalDomainDao.queryActionAbilityVos(vehicleId, language);
    }

    @Override
    public List<GeneralAbilityVo> queryGeneralAbilityAvgVos(List<String> vehicleId, String language) {
        List<GeneralAbilityVo> generalAbilityVos = functionalDomainDao.queryGeneralAbilityAvgVos(vehicleId, language);
        for (GeneralAbilityVo s : generalAbilityVos) {
            if (language.equals("zh_home_top")) {
                s.setVehicleName("行业均值");
            } else {
                s.setVehicleName("Industry average");
            }
            s.setVehicleId("111");
        }
        return generalAbilityVos;
    }

    @Override
    public List<ActionAbilityVo> queryActionAbilityAVGVos(List<String> vehicleId, String language) {
        List<ActionAbilityVo> actionAbilityVos = functionalDomainDao.queryActionAbilityAvgVos(vehicleId, language);
        for (ActionAbilityVo s : actionAbilityVos) {
            if (language.equals("zh_home_top")) {
                s.setVehicleName("行业均值");
            } else {
                s.setVehicleName("Industry average");
            }
            s.setVehicleId("111");
        }
        return actionAbilityVos;
    }

    public static DomainTreeScoreResponse domainTreeCountScoreSortResponseConvertDomainTreeScoreResponse(
            List<DomainTreeCountScoreSortResponse> dataList) {
        DomainTreeScoreResponse response = new DomainTreeScoreResponse();

        // 1. 构建表头
        List<DomainTreeScoreResponse.HeaderResponse> headerList = new ArrayList<>();
        headerList.add(new DomainTreeScoreResponse.HeaderResponse("车型", "vehicleInfoName"));
        headerList.add(new DomainTreeScoreResponse.HeaderResponse("系统版本", "vehicleSystemVersion"));
        headerList.add(new DomainTreeScoreResponse.HeaderResponse("测试时间", "testDate"));

        // 动态添加功能域表头
        dataList.forEach(data -> {
            headerList.add(new DomainTreeScoreResponse.HeaderResponse(
                    data.getFunctionalDomainName(),
                    data.getFunctionDomainId()));
        });

        response.setHeaderList(headerList);

        // 2. 构建表数据
        List<Map<String, Object>> tableList = new ArrayList<>();

        // 用于存储每辆车的分数数据
        Map<String, Map<String, Object>> vehicleDataMap = new HashMap<>();

        // 遍历每辆车的数据
        for (DomainTreeCountScoreSortResponse data : dataList) {
            String functionDomainId = data.getFunctionDomainId();

            for (DomainTreeCountScoreSortResponse.VehicleIndexScoreResponse vehicle : data.getVehicleIndexScore()) {
                String vehicleKey = vehicle.getVehicleInfoId() + "-" + vehicle.getVehicleSystemVersion();

                // 如果车辆数据不存在，初始化
                if (!vehicleDataMap.containsKey(vehicleKey)) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("vehicleInfoName", vehicle.getVehicleInfoName());
                    row.put("vehicleSystemVersion", vehicle.getVehicleSystemVersion());
                    row.put("testDate", vehicle.getTestDate());
                    row.put("vehicleInfoId", vehicle.getVehicleInfoId());
                    vehicleDataMap.put(vehicleKey, row);
                }

                // 添加当前功能域的分数
                vehicleDataMap.get(vehicleKey).put(functionDomainId, vehicle.getVehicleInfoScore());
            }
        }

        // 将车辆数据添加到 tableList
        tableList.addAll(vehicleDataMap.values());

        // 添加行业均值行
        Map<String, Object> industryAvgRow = new HashMap<>();
        industryAvgRow.put("vehicleInfoName", "行业 均值");
        industryAvgRow.put("vehicleSystemVersion", "");
        industryAvgRow.put("testDate", "-");
        industryAvgRow.put("vehicleInfoId", null);

        for (DomainTreeCountScoreSortResponse data : dataList) {
            industryAvgRow.put(data.getFunctionDomainId(), data.getIndustryAvgScore());
        }

        tableList.add(industryAvgRow);

        response.setTableList(tableList);

        return response;
    }

    public static DomainTreeScoreResponse domainTreeQueryIndexScoreResponseConvertDomainTreeScoreResponse(
            List<DomainTreeQueryIndexScoreResponse> dataList, String language) {
        DomainTreeScoreResponse response = new DomainTreeScoreResponse();

        // 1. 构建表头
        List<DomainTreeScoreResponse.HeaderResponse> headerList = new ArrayList<>();
        if (language.equals("zh_home_top")) {
            headerList.add(new DomainTreeScoreResponse.HeaderResponse("车型", "vehicleInfoName"));
            headerList.add(new DomainTreeScoreResponse.HeaderResponse("系统版本", "vehicleSystemVersion"));
            headerList.add(new DomainTreeScoreResponse.HeaderResponse("测试时间", "testDate"));
        } else if (language.equals("en")) {
            headerList.add(new DomainTreeScoreResponse.HeaderResponse("Vehicle type", "vehicleInfoName"));
            headerList.add(new DomainTreeScoreResponse.HeaderResponse("System version", "vehicleSystemVersion"));
            headerList.add(new DomainTreeScoreResponse.HeaderResponse("Test date", "testDate"));
        }
        // 动态添加功能域表头
        dataList.forEach(data -> {
            headerList.add(new DomainTreeScoreResponse.HeaderResponse(
                    data.getDomainIndexName(),
                    data.getDomainIndexId()));
        });

        response.setHeaderList(headerList);

        // 2. 构建表数据
        List<Map<String, Object>> tableList = new ArrayList<>();

        // 用于存储每辆车的分数数据
        Map<String, Map<String, Object>> vehicleDataMap = new HashMap<>();

        // 遍历每辆车的数据
        for (DomainTreeQueryIndexScoreResponse data : dataList) {
            String functionDomainId = data.getDomainIndexId();

            for (DomainTreeQueryIndexScoreResponse.VehicleIndexScoreResponse vehicle : data.getVehicleIndexScore()) {
                String vehicleKey = vehicle.getVehicleInfoId() + "-" + vehicle.getVehicleSystemVersion();

                // 如果车辆数据不存在，初始化
                if (!vehicleDataMap.containsKey(vehicleKey)) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("vehicleInfoName", vehicle.getVehicleInfoName());
                    row.put("vehicleSystemVersion", vehicle.getVehicleSystemVersion());
                    row.put("testDate", vehicle.getTestDate());
                    row.put("vehicleInfoId", vehicle.getVehicleInfoId());
                    vehicleDataMap.put(vehicleKey, row);
                }

                // 添加当前功能域的分数
                vehicleDataMap.get(vehicleKey).put(functionDomainId, vehicle.getVehicleInfoScore());
            }
        }

        // 将车辆数据添加到 tableList
        tableList.addAll(vehicleDataMap.values());

        // 添加行业均值行
        Map<String, Object> industryAvgRow = new HashMap<>();
        if (language.equals("zh_home_top")) {
            industryAvgRow.put("vehicleInfoName", "行业 均值");
            industryAvgRow.put("vehicleSystemVersion", "");
        } else if (language.equals("en")) {
            industryAvgRow.put("vehicleInfoName", "Industry Average");
            industryAvgRow.put("vehicleSystemVersion", "");
        }
        industryAvgRow.put("testDate", "-");
        industryAvgRow.put("vehicleInfoId", null);

        for (DomainTreeQueryIndexScoreResponse data : dataList) {
            industryAvgRow.put(data.getDomainIndexId(), data.getIndexAvgScore());
        }

        tableList.add(industryAvgRow);

        response.setTableList(tableList);

        return response;
    }

    /**
     * 车辆信息内部类，用于批量查询优化
     */
    private static class VehicleInfo {
        private String name;
        private String systemVersion;
        private Date testDate;

        public VehicleInfo(String name, String systemVersion, Date testDate) {
            this.name = name;
            this.systemVersion = systemVersion;
            this.testDate = testDate;
        }

        public String getName() {
            return name;
        }

        public String getSystemVersion() {
            return systemVersion;
        }

        public Date getTestDate() {
            return testDate;
        }
    }

    /**
     * 批量获取车辆信息，提高查询性能
     */
    private Map<String, VehicleInfo> getVehicleInfoBatch(List<Long> vehicleIds, String language) {
        Map<String, VehicleInfo> result = new HashMap<>();

        if (CollectionUtils.isEmpty(vehicleIds)) {
            return result;
        }

        try {
            // 批量查询车辆基本信息
            List<BaseInfoEntity> vehicles = baseInfoDao.selectBatchIds(vehicleIds);
            if (CollectionUtils.isEmpty(vehicles)) {
                return result;
            }

            // 获取所有品牌ID
            Set<Long> brandIds = vehicles.stream()
                    .map(BaseInfoEntity::getBrandId)
                    .collect(Collectors.toSet());

            // 批量查询品牌信息
            Map<Long, BrandEntity> brandMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(brandIds)) {
                List<BrandEntity> brands = brandDao.selectBatchIds(brandIds);
                brandMap = brands.stream()
                        .collect(Collectors.toMap(BrandEntity::getId, brand -> brand));
            }

            // 构建车辆信息映射
            for (BaseInfoEntity vehicle : vehicles) {
                String vehicleIdStr = vehicle.getId().toString();
                BrandEntity brand = brandMap.get(vehicle.getBrandId());

                String vehicleName;
                if (language.equals("zh_home_top")) {
                    vehicleName = (brand != null ? brand.getBrand() + "-" : "") + vehicle.getVehicleModel();
                } else {
                    vehicleName = (brand != null ? brand.getBrandEn() + "-" : "") + vehicle.getVehicleModel();
                }

                VehicleInfo vehicleInfo = new VehicleInfo(
                        vehicleName,
                        vehicle.getVehicleSystemVersion(),
                        vehicle.getTestDate());

                result.put(vehicleIdStr, vehicleInfo);
            }

            log.debug("批量获取车辆信息完成，共{}条记录", result.size());

        } catch (Exception e) {
            log.error("批量获取车辆信息失败", e);
            // 返回空的映射，让后续逻辑使用降级方案
        }

        return result;
    }

    /**
     * 根据车辆ID获取车辆名称
     */
    private String getVehicleNameById(String vehicleId, String language) {
        try {
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

    /**
     * 根据车辆ID获取系统版本
     */
    private String getVehicleSystemVersion(String vehicleId) {
        try {
            BaseInfoEntity vehicle = baseInfoDao.selectById(Long.parseLong(vehicleId));
            return vehicle != null ? vehicle.getVehicleSystemVersion() : "";
        } catch (Exception e) {
            log.error("获取车辆系统版本失败，vehicleId: {}", vehicleId, e);
            return "";
        }
    }

    /**
     * 根据车辆ID获取测试日期
     */
    private Date getVehicleTestDate(String vehicleId) {
        try {
            BaseInfoEntity vehicle = baseInfoDao.selectById(Long.parseLong(vehicleId));
            return vehicle != null ? vehicle.getTestDate() : new Date();
        } catch (Exception e) {
            log.error("获取车辆测试日期失败，vehicleId: {}", vehicleId, e);
            return new Date();
        }
    }

}