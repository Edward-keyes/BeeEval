package com.xailab.vehicle.xaivehicledata.controller;

import java.util.*;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xailab.vehicle.feign.vo.FunctionDomainVideoVo;
import com.xailab.vehicle.feign.vo.FunctionalVideoVoF;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.dao.VehicleDomainScoreDao;
import com.xailab.vehicle.xaivehicledata.entity.*;
import com.xailab.vehicle.xaivehicledata.entity.constant.VehicleConstant;
import com.xailab.vehicle.xaivehicledata.entity.request.DomainTreeQueryScoreRequest;
import com.xailab.vehicle.xaivehicledata.entity.request.LanguageRequest;
import com.xailab.vehicle.xaivehicledata.entity.request.OneIDRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.*;
import com.xailab.vehicle.xaivehicledata.entity.vo.VehicleInfoVo;
import com.xailab.vehicle.xaivehicledata.service.BaseInfoService;
import com.xailab.vehicle.xaivehicledata.service.VehicleTryUserService;
import com.xailab.vehicle.xaivehicledata.service.VehicleUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

import com.xailab.vehicle.xaivehicledata.service.DomainTreeService;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.R;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 *
 *
 * @email d2460687074@gmail.com
 * @date 2025-02-26 02:07:44
 */
// @SaCheckLogin
@RestController
@RequestMapping("vehicle/domaintree")
@Slf4j
public class DomainTreeController {
    @Autowired
    private DomainTreeService domainTreeService;

    @Autowired
    private VehicleUserService vehicleUserService;

    @Resource
    private VehicleTryUserService vehicleTryUserService;

    @Resource
    private VehicleDomainScoreDao vehicleDomainScoreDao;

    @Resource
    private BaseInfoService baseInfoService;

    /**
     * 列表
     */

    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = domainTreeService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        DomainTreeEntity domainTree = domainTreeService.getById(id);

        return R.ok().put("domainTree", domainTree);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody DomainTreeEntity domainTree) {
        domainTreeService.save(domainTree);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody DomainTreeEntity domainTree) {
        domainTreeService.updateById(domainTree);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        domainTreeService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 功能表现视频list
     */
    @SaCheckLogin
    @PostMapping("/functionalVideoList")
    public Result functionalVideoList(@RequestBody OneIDRequest oneIDRequest) {

        BaseInfoEntity one = baseInfoService
                .getOne(Wrappers.<BaseInfoEntity>lambdaQuery().eq(BaseInfoEntity::getId, oneIDRequest.getId()));

        // TODO 给视频加上对应id
        if (one.getVehicleType() == 1) {
            FunctionalVideoListResponse page = domainTreeService.getFunctionalVideoList(oneIDRequest.getId(),
                    oneIDRequest.getLanguage(), oneIDRequest.getPage());
            return Result.ok(page);
        } else {
            FunctionalVideoNewListResponse page1 = domainTreeService.getFunctionalVideoNewList(oneIDRequest.getId(),
                    oneIDRequest.getLanguage(), oneIDRequest.getPage());
            return Result.ok(page1);
        }
    }

    @PostMapping("batchData")
    public Result<Void> batchDataImport(@RequestPart("file") MultipartFile multipartFile) {
        return domainTreeService.batchDataImport(multipartFile);
    }

    /**
     * 根据车品牌id 获取每个域下的平均值和个体值
     *
     * @return
     */
    @SaCheckLogin
    @PostMapping("queryScoreByBrandInfo")
    public Result<List<DomainTreeQueryScoreResponse>> queryScoreByBrandInfo(
            @RequestBody DomainTreeQueryScoreRequest request) {
        return domainTreeService.queryScoreByBrandInfo(request);
    }

    /**
     * 查询功能域id和名称
     */
    @PostMapping("queryDomainTree")
    public Result<List<FunctionalDomainResponse>> queryDomainTree(@RequestBody LanguageRequest languageRequest) {
        return Result.ok(domainTreeService.queryDomainTree(languageRequest.getLanguage()));
    }

    /**
     * 查询出每个指标的分数，根据功能域id和车辆id
     *
     * @return
     */
    @SaCheckLogin
    @PostMapping("queryIndexScore")
    public Result<List<DomainTreeQueryIndexScoreResponse>> queryDomainIndexScore(
            @RequestBody DomainTreeQueryScoreRequest request) {
        return Result.ok(domainTreeService.queryDomainIndexScore(request, request.getLanguage()));
    }

    /**
     * 排行榜结构 查询出每个指标的分数，根据功能域id和车辆id
     *
     * @return
     */
    @PostMapping("queryIndexScoreRank")
    public Result<DomainTreeScoreResponse> queryDomainIndexScoreRank(@RequestBody DomainTreeQueryScoreRequest request) {

        DomainTreeScoreResponse domainTreeScoreResponse = domainTreeService.queryDomainIndexScoreRank(request,
                request.getLanguage());

        List<Map<String, Object>> tableList = domainTreeScoreResponse.getTableList();

        // 为请求的车辆添加同步的三级指标分数数据（type=2）
        List<Long> vehicleIdsToQuery = request.getBrandBaseInfoId();
        boolean queryAllVehicles = (vehicleIdsToQuery == null || vehicleIdsToQuery.isEmpty());

        if (queryAllVehicles) {
            // 如果没有指定车辆ID，查询所有有同步数据的车辆ID
            vehicleIdsToQuery = vehicleDomainScoreDao.selectList(
                    Wrappers.<VehicleDomainScoreEntity>lambdaQuery()
                            .eq(VehicleDomainScoreEntity::getType, (short) 2)
                            .select(VehicleDomainScoreEntity::getVehicleId)
                            .groupBy(VehicleDomainScoreEntity::getVehicleId))
                    .stream()
                    .map(VehicleDomainScoreEntity::getVehicleId)
                    .distinct()
                    .collect(Collectors.toList());

            log.debug("查询所有车辆模式，找到{}个有同步数据的车辆", vehicleIdsToQuery.size());
        }

        if (!CollectionUtils.isEmpty(vehicleIdsToQuery)) {
            // 批量查询指定车辆的三级指标分数
            List<VehicleDomainScoreEntity> allSyncScores = vehicleDomainScoreDao.selectList(
                    Wrappers.<VehicleDomainScoreEntity>lambdaQuery()
                            .in(VehicleDomainScoreEntity::getVehicleId, vehicleIdsToQuery)
                            .eq(VehicleDomainScoreEntity::getType, (short) 2));

            if (!CollectionUtils.isEmpty(allSyncScores)) {
                // 按车辆ID分组，构建每个车辆的三级指标数据
                Map<Long, List<VehicleDomainScoreEntity>> scoresByVehicle = allSyncScores.stream()
                        .collect(Collectors.groupingBy(VehicleDomainScoreEntity::getVehicleId));

                // 获取车辆信息映射
                Map<String, VehicleInfo> vehicleInfoMap = getVehicleInfoBatch(vehicleIdsToQuery,
                        request.getLanguage());

                for (Long vehicleId : vehicleIdsToQuery) {
                    List<VehicleDomainScoreEntity> vehicleScores = scoresByVehicle.get(vehicleId);

                    if (!CollectionUtils.isEmpty(vehicleScores)) {
                        // 检查是否已经存在该车辆的数据，避免重复添加
                        boolean alreadyExists = tableList.stream()
                                .anyMatch(row -> vehicleId.toString().equals(String.valueOf(row.get("vehicleInfoId"))));

                        if (!alreadyExists) {
                            Map<String, Object> vehicleData = new HashMap<>();

                            // 设置三级指标分数
                            for (VehicleDomainScoreEntity syncScore : vehicleScores) {
                                Long domainIndexId = syncScore.getDomainId();
                                Double scoreValue = syncScore.getScore().doubleValue();

                                // 保留一位小数
                                double roundedValue = Math.round(scoreValue * 10.0) / 10.0;
                                vehicleData.put(domainIndexId.toString(), roundedValue);
                            }

                            // 设置车辆基本信息
                            vehicleData.put("vehicleInfoId", vehicleId.toString());
                            vehicleData.put("status", 1);

                            // 从车辆信息映射中获取详细信息
                            VehicleInfo vehicleInfo = vehicleInfoMap.get(vehicleId.toString());
                            if (vehicleInfo != null) {
                                vehicleData.put("vehicleInfoName", vehicleInfo.getName());
                                vehicleData.put("vehicleSystemVersion", vehicleInfo.getSystemVersion());
                                vehicleData.put("testDate",
                                        vehicleInfo.getTestDate() != null
                                                ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                                        .format(vehicleInfo.getTestDate())
                                                : "2025-06-17 00:00:00");
                            } else {
                                // 降级处理
                                vehicleData.put("vehicleInfoName", "未知车辆-" + vehicleId);
                                vehicleData.put("vehicleSystemVersion", "");
                                vehicleData.put("testDate", "2025-06-17 00:00:00");
                            }

                            tableList.add(vehicleData);

                            log.debug("为车辆{}添加同步的三级指标数据，共{}个指标", vehicleId, vehicleScores.size());
                        }
                    }
                }

                log.info("批量添加同步三级指标数据完成，共处理{}条记录", allSyncScores.size());
            }
        }

        VehicleConstant constant = new VehicleConstant();
        Object loginId = StpUtil.getTokenInfo().getLoginId();

        if (Objects.nonNull(loginId)) {
            VehicleUserEntity user = vehicleUserService.getById(loginId + "");

            if (user.getStatus() == 1) {

                for (Map<String, Object> map : tableList) {
                    // 检查 vehicleInfoId 的值
                    Object vehicleInfoId = map.get("vehicleInfoId");
                    Object vehicleInfoName = map.get("vehicleInfoName");
                    Map<String, Long> tryMap = new HashMap<>();
                    List<VehicleTryUserEntity> vehicleTryUserEntityList = vehicleTryUserService
                            .getTryUserListByUserId(Long.parseLong(loginId + ""));
                    if (!vehicleTryUserEntityList.isEmpty()) {
                        vehicleTryUserEntityList.stream().forEach(vehicleTryUserEntity -> {
                            tryMap.put(vehicleTryUserEntity.getVehicleId() + "", vehicleTryUserEntity.getUserId());
                        });
                        Long l = tryMap.get(vehicleInfoId);
                        if (l != null) {
                            map.put("status", 3);
                        } else {
                            // 否则，添加 status = 1
                            map.put("status", 1);
                            if (!"行业 均值".equals(vehicleInfoName)) {
                                // 将所有可以转换为 long 类型的键对应的值设置为 0
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    try {
                                        Long.parseLong(entry.getKey());
                                        entry.setValue(0L); // 设置值为 0
                                    } catch (NumberFormatException e) {
                                        // 如果键不能转换为 long 类型，忽略
                                    }
                                }
                            }
                        }

                    } else {
                        // 判断 vehicleInfoId 是否为 1 或 2
                        if (constant.getVehicle3Id().equals(vehicleInfoId)
                                || constant.getVehicle1Id().equals(vehicleInfoId)
                                || constant.getVehicle2Id().equals(vehicleInfoId)) {
                            // 如果是 1 或 2，添加 status = 3
                            map.put("status", 3);
                        } else {
                            // 否则，添加 status = 1
                            map.put("status", 1);
                            if (!"行业 均值".equals(vehicleInfoName)) {
                                // 将所有可以转换为 long 类型的键对应的值设置为 0
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    try {
                                        Long.parseLong(entry.getKey());
                                        entry.setValue(0L); // 设置值为 0
                                    } catch (NumberFormatException e) {
                                        // 如果键不能转换为 long 类型，忽略
                                    }
                                }
                            }
                        }
                    }
                }
            } else {

                for (Map<String, Object> map : tableList) {

                    map.put("status", 1);

                }

            }
        }

        // 排序逻辑
        tableList.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                // 检查 vehicleInfoName 是否为 "行业均值"
                // 检查 vehicleInfoName 是否为 "行业均值"
                Boolean isIndustryAverage1 = null;
                Boolean isIndustryAverage2 = null;

                if (request.getLanguage().equals("zh_home_top")) {
                    isIndustryAverage1 = "行业 均值".equals(o1.get("vehicleInfoName"));
                    isIndustryAverage2 = "行业 均值".equals(o2.get("vehicleInfoName"));
                } else if (request.getLanguage().equals("en")) {
                    isIndustryAverage1 = "Industry Average".equals(o1.get("vehicleInfoName"));
                    isIndustryAverage2 = "Industry Average".equals(o2.get("vehicleInfoName"));
                }

                // 如果两者都是 "行业均值"，保持原顺序
                if (isIndustryAverage1 && isIndustryAverage2) {
                    return 0;
                }
                // 如果 o1 是 "行业均值"，排在最后
                if (isIndustryAverage1) {
                    return 1;
                }
                // 如果 o2 是 "行业均值"，排在最后
                if (isIndustryAverage2) {
                    return -1;
                }

                // 按 status 排序
                Integer status1 = (Integer) o1.get("status");
                Integer status2 = (Integer) o2.get("status");

                if (!status1.equals(status2)) {
                    if (status1 == 3)
                        return -1; // status = 3 排在前面
                    if (status2 == 3)
                        return 1;
                    if (status1 == 1)
                        return -1; // status = 1 排在 status = 2 前面
                    return 1;
                }

                // 如果 status 相同，按 vehicleInfoId 升序排序
                Long vehicleInfoId1 = Long.valueOf(o1.get("vehicleInfoId") + "");
                Long vehicleInfoId2 = Long.valueOf(o2.get("vehicleInfoId") + "");
                return vehicleInfoId1.compareTo(vehicleInfoId2);
            }
        });

        domainTreeScoreResponse.setTableList(tableList);

        return Result.ok(domainTreeScoreResponse);
    }

    /**
     * 总分排行
     *
     * @return
     */
    @PostMapping("countScoreSort")
    public Result<DomainTreeScoreResponse> countScoreSort(@RequestBody DomainTreeQueryScoreRequest request) {
        return domainTreeService.countScoreSort(request);
    }

    /**
     * 基础能力 指标排行
     *
     * @param request
     * @return
     */
    @PostMapping("baseDomainIndexScoreSort")
    public Result<DomainTreeScoreResponse> baseDomainIndexScoreSort(@RequestBody DomainTreeQueryScoreRequest request) {

        DomainTreeScoreResponse domainTreeScoreResponse = domainTreeService.baseDomainIndexScoreSort(request,
                request.getLanguage());

        List<Map<String, Object>> tableList = domainTreeScoreResponse.getTableList();

        // 为请求的车辆添加同步的基础能力数据（type=3）
        List<Long> vehicleIdsToQuery = request.getBrandBaseInfoId();
        boolean queryAllVehicles = (vehicleIdsToQuery == null || vehicleIdsToQuery.isEmpty());

        if (queryAllVehicles) {
            // 如果没有指定车辆ID，查询所有有同步数据的车辆ID
            vehicleIdsToQuery = vehicleDomainScoreDao.selectList(
                    Wrappers.<VehicleDomainScoreEntity>lambdaQuery()
                            .eq(VehicleDomainScoreEntity::getType, (short) 3)
                            .select(VehicleDomainScoreEntity::getVehicleId)
                            .groupBy(VehicleDomainScoreEntity::getVehicleId))
                    .stream()
                    .map(VehicleDomainScoreEntity::getVehicleId)
                    .distinct()
                    .collect(Collectors.toList());

            log.debug("查询所有车辆模式，找到{}个有同步数据的车辆", vehicleIdsToQuery.size());
        }

        if (!CollectionUtils.isEmpty(vehicleIdsToQuery)) {
            // 批量查询指定车辆的基础能力分数
            List<VehicleDomainScoreEntity> allSyncScores = vehicleDomainScoreDao.selectList(
                    Wrappers.<VehicleDomainScoreEntity>lambdaQuery()
                            .in(VehicleDomainScoreEntity::getVehicleId, vehicleIdsToQuery)
                            .eq(VehicleDomainScoreEntity::getType, (short) 3));

            if (!CollectionUtils.isEmpty(allSyncScores)) {
                // 按车辆ID分组，构建每个车辆的基础能力数据
                Map<Long, List<VehicleDomainScoreEntity>> scoresByVehicle = allSyncScores.stream()
                        .collect(Collectors.groupingBy(VehicleDomainScoreEntity::getVehicleId));

                // 获取车辆信息映射
                Map<String, VehicleInfo> vehicleInfoMap = getVehicleInfoBatch(vehicleIdsToQuery, request.getLanguage());

                for (Long vehicleId : vehicleIdsToQuery) {
                    List<VehicleDomainScoreEntity> vehicleScores = scoresByVehicle.get(vehicleId);

                    if (!CollectionUtils.isEmpty(vehicleScores)) {
                        // 检查是否已经存在该车辆的数据，避免重复添加
                        boolean alreadyExists = tableList.stream()
                                .anyMatch(row -> vehicleId.toString().equals(String.valueOf(row.get("vehicleInfoId"))));

                        if (!alreadyExists) {
                            Map<String, Object> vehicleData = new HashMap<>();

                            // 设置基础能力指标分数
                            for (VehicleDomainScoreEntity score : vehicleScores) {
                                Long domainIndexId = score.getDomainId();
                                Double scoreValue = null;
                                if (Objects.nonNull(score.getScore())) {
                                    scoreValue = score.getScore().doubleValue();

                                    // 根据指标ID设置对应的值，处理百分比和时间单位
                                    if (domainIndexId.equals(451751102942019595L) || // 拒识准确率
                                            domainIndexId.equals(451751102942019596L) || // 免唤醒准确率
                                            domainIndexId.equals(451751102942019597L)) { // 任务完成率
                                        // 这些是百分比，需要特殊处理（如果已经是小数形式）

                                        vehicleData.put(domainIndexId.toString(), scoreValue / 100);
                                    } else if (
//                                            domainIndexId.equals(451751102942019599L) || // 首字响应时长
                                            domainIndexId.equals(451751102942019601L)) { // 图像生成速度
                                        vehicleData.put(domainIndexId.toString(), scoreValue*1000);
                                    } else {
                                        // 其他指标直接设置
                                        vehicleData.put(domainIndexId.toString(), scoreValue);
                                    }
                                }
                            }

                            // 设置车辆基本信息
                            vehicleData.put("vehicleInfoId", vehicleId.toString());
                            vehicleData.put("status", 1);

                            // 从车辆信息映射中获取详细信息
                            VehicleInfo vehicleInfo = vehicleInfoMap.get(vehicleId.toString());
                            if (vehicleInfo != null) {
                                vehicleData.put("vehicleInfoName", vehicleInfo.getName());
                                vehicleData.put("vehicleSystemVersion", vehicleInfo.getSystemVersion());
                                vehicleData.put("testDate",
                                        vehicleInfo.getTestDate() != null
                                                ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                                        .format(vehicleInfo.getTestDate())
                                                : "2025-06-17 00:00:00");
                            } else {
                                // 降级处理
                                vehicleData.put("vehicleInfoName", "未知车辆-" + vehicleId);
                                vehicleData.put("vehicleSystemVersion", "");
                                vehicleData.put("testDate", "2025-06-17 00:00:00");
                            }

                            tableList.add(vehicleData);

                            log.debug("为车辆{}添加同步的基础能力数据，共{}个指标", vehicleId, vehicleScores.size());
                        }
                    }
                }

                log.info("批量添加同步基础能力数据完成，共处理{}条记录", allSyncScores.size());
            }
        }

        // 对旧数据进行单位转换（首字响应时长和图像生成速度从毫秒转换为秒）
        for (Map<String, Object> row : tableList) {
            // 检查是否是旧数据（不是我们刚添加的同步数据）
            Object vehicleInfoId = row.get("vehicleInfoId");
            Object vehicleInfoName = row.get("vehicleInfoName");

            // 处理条件：vehicleInfoId不为null且不以"未知车辆-"开头，或者是行业均值数据
            boolean isOldData = (vehicleInfoId != null && !String.valueOf(vehicleInfoId).startsWith("未知车辆-")) ||
                    (vehicleInfoName != null && "行业 均值".equals(String.valueOf(vehicleInfoName)));

            /**
            if (isOldData) {
                // 对特定的时间指标进行单位转换
                convertTimeUnitsForOldData(row);
            }
             */
        }

        VehicleConstant constant = new VehicleConstant();
        Object loginId = StpUtil.getTokenInfo().getLoginId();

        if (Objects.nonNull(loginId)) {
            VehicleUserEntity user = vehicleUserService.getById(loginId + "");

            if (user.getStatus() == 1) {

                for (Map<String, Object> map : tableList) {
                    // 检查 vehicleInfoId 的值
                    Object vehicleInfoId = map.get("vehicleInfoId");
                    Object vehicleInfoName = map.get("vehicleInfoName");
                    Map<String, Long> tryMap = new HashMap<>();
                    List<VehicleTryUserEntity> vehicleTryUserEntityList = vehicleTryUserService
                            .getTryUserListByUserId(Long.parseLong(loginId + ""));
                    if (!vehicleTryUserEntityList.isEmpty()) {
                        vehicleTryUserEntityList.stream().forEach(vehicleTryUserEntity -> {
                            tryMap.put(vehicleTryUserEntity.getVehicleId() + "", vehicleTryUserEntity.getUserId());
                        });
                        Long l = tryMap.get(vehicleInfoId);
                        if (l != null) {
                            map.put("status", 3);
                        } else {
                            // 否则，添加 status = 1
                            map.put("status", 1);
                            if (!"行业 均值".equals(vehicleInfoName)) {
                                // 将所有可以转换为 long 类型的键对应的值设置为 0
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    try {
                                        Long.parseLong(entry.getKey());
                                        entry.setValue(0L); // 设置值为 0
                                    } catch (NumberFormatException e) {
                                        // 如果键不能转换为 long 类型，忽略
                                    }
                                }
                            }
                        }

                    } else {
                        // 判断 vehicleInfoId 是否为 1 或 2
                        if (constant.getVehicle1Id().equals(vehicleInfoId)
                                || constant.getVehicle2Id().equals(vehicleInfoId)
                                || constant.getVehicle3Id().equals(vehicleInfoId)) {
                            // 如果是 1 或 2，添加 status = 3
                            map.put("status", 3);
                        } else {
                            // 否则，添加 status = 1
                            map.put("status", 1);
                            if (!"行业 均值".equals(vehicleInfoName)) {
                                // 将所有可以转换为 long 类型的键对应的值设置为 0
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    try {
                                        Long.parseLong(entry.getKey());
                                        entry.setValue(0L); // 设置值为 0
                                    } catch (NumberFormatException e) {
                                        // 如果键不能转换为 long 类型，忽略
                                    }
                                }
                            }
                        }
                    }
                }
            } else {

                for (Map<String, Object> map : tableList) {

                    map.put("status", 1);

                }

            }
        }

        // 排序逻辑
        tableList.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {

                // 检查 vehicleInfoName 是否为 "行业均值"
                Boolean isIndustryAverage1 = null;
                Boolean isIndustryAverage2 = null;

                if (request.getLanguage().equals("zh_home_top")) {
                    isIndustryAverage1 = "行业 均值".equals(o1.get("vehicleInfoName"));
                    isIndustryAverage2 = "行业 均值".equals(o2.get("vehicleInfoName"));
                } else if (request.getLanguage().equals("en")) {
                    isIndustryAverage1 = "Industry Average".equals(o1.get("vehicleInfoName"));
                    isIndustryAverage2 = "Industry Average".equals(o2.get("vehicleInfoName"));
                }

                // 如果两者都是 "行业均值"，保持原顺序
                if (isIndustryAverage1 && isIndustryAverage2) {
                    return 0;
                }
                // 如果 o1 是 "行业均值"，排在最后
                if (isIndustryAverage1) {
                    return 1;
                }
                // 如果 o2 是 "行业均值"，排在最后
                if (isIndustryAverage2) {
                    return -1;
                }

                // 按 status 排序
                Integer status1 = (Integer) o1.get("status");
                Integer status2 = (Integer) o2.get("status");

                if (status1 != null && status2 != null) {

                    if (!status1.equals(status2)) {
                        if (status1 == 3)
                            return -1; // status = 3 排在前面
                        if (status2 == 3)
                            return 1;
                        if (status1 == 1)
                            return -1; // status = 1 排在 status = 2 前面
                        return 1;
                    }
                }

                // 如果 status 相同，按 vehicleInfoId 升序排序
                Long vehicleInfoId1 = Long.valueOf(o1.get("vehicleInfoId") + "");
                Long vehicleInfoId2 = Long.valueOf(o2.get("vehicleInfoId") + "");
                return vehicleInfoId1.compareTo(vehicleInfoId2);
            }
        });

        domainTreeScoreResponse.setTableList(tableList);

        return Result.ok(domainTreeScoreResponse);
    }

    @PostMapping("/editDomainVideo")
    public FunctionalVideoListResponse editDomainVideo(@RequestBody OneIDRequest vo) {

        FunctionalVideoListResponse zhHomeTop = domainTreeService.getFunctionalVideoEditList(vo.getId(), "zh_home_top");

        return zhHomeTop;
    }

    @PostMapping("/queryVideoByFunctionalVideoVo")
    public FunctionDomainVideoVo queryVideoByFunctionalVideoVo(@RequestBody FunctionalVideoVoF functionalVideoVo) {

        return domainTreeService.getVideoUrlDetail(functionalVideoVo);
    }

    @PostMapping("/updateFunctionDomainVideoInfo")
    Boolean updateFunctionDomainVideoInfo(@RequestBody FunctionDomainVideoVo functionDomainVideoVo) {

        return domainTreeService.updateFunctionDomainVideoInfo(functionDomainVideoVo);
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
            // 查询所有车辆信息，然后在内存中过滤
            List<VehicleInfoVo> allVehicles = baseInfoService.queryAllVehicle(language);
            if (CollectionUtils.isEmpty(allVehicles)) {
                return result;
            }

            // 将vehicleIds转换为字符串集合，用于过滤
            Set<String> vehicleIdStrings = vehicleIds.stream()
                    .map(String::valueOf)
                    .collect(java.util.stream.Collectors.toSet());

            // 过滤出请求的车辆信息
            List<VehicleInfoVo> vehicles = allVehicles.stream()
                    .filter(vehicle -> vehicleIdStrings.contains(vehicle.getId()))
                    .collect(java.util.stream.Collectors.toList());

            // 构建车辆信息映射
            for (VehicleInfoVo vehicle : vehicles) {
                String vehicleIdStr = vehicle.getId();

                VehicleInfo vehicleInfo = new VehicleInfo(
                        vehicle.getBrandModel(),
                        "", // 暂时为空，基础能力接口可能不需要系统版本
                        null // 暂时为null
                );

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
     * 对旧数据的特定时间指标进行单位转换（毫秒转秒），并保留两位小数
     */
    private void convertTimeUnitsForOldData(Map<String, Object> row) {
        // 首字响应时长（451751102942019599）
        Object responseTime = row.get("451751102942019599");
        if (responseTime instanceof Number) {
            double value = ((Number) responseTime).doubleValue();
            double convertedValue;
            if (value >= 1000) { // 如果是毫秒，转换为秒
                convertedValue = value / 1000;
            } else {
                convertedValue = value;
            }
            // 保留两位小数
            double roundedValue = Math.round(convertedValue * 100.0) / 100.0;
            row.put("451751102942019599", roundedValue);
        }

        // 图像生成速度（451751102942019601）
        Object imageGenSpeed = row.get("451751102942019601");
        if (imageGenSpeed instanceof Number) {
            double value = ((Number) imageGenSpeed).doubleValue();
            double convertedValue;
            if (value >= 1000) { // 如果是毫秒，转换为秒
                convertedValue = value / 1000;
            } else {
                convertedValue = value;
            }
            // 保留两位小数
            double roundedValue = Math.round(convertedValue * 100.0) / 100.0;
            row.put("451751102942019601", roundedValue);
        }
    }

}
