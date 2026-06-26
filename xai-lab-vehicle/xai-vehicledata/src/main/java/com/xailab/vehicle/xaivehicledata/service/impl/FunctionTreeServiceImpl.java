package com.xailab.vehicle.xaivehicledata.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xailab.vehicle.feign.pojo.request.FunctionTreeDataSyncCaseInfoDto;
import com.xailab.vehicle.feign.pojo.request.FunctionTreeDataSyncInfoRequest;
import com.xailab.vehicle.feign.pojo.request.FunctionTreeDataSyncRequest;
import com.xailab.vehicle.feign.pojo.response.FunctionTreeDataSyncCaseInfoJournal;
import com.xailab.vehicle.feign.pojo.response.FunctionTreeDataSyncInfoJournal;
import com.xailab.vehicle.feign.pojo.response.FunctionTreeTaskSyncJournalResponse;
import com.xailab.vehicle.xaicommon.utils.*;
import com.xailab.vehicle.xaivehicledata.config.ALiYunOssConfig;
import com.xailab.vehicle.xaivehicledata.dao.*;
import com.xailab.vehicle.xaivehicledata.entity.*;
import com.xailab.vehicle.xaivehicledata.entity.constant.VehicleConstant;
import com.xailab.vehicle.xaivehicledata.entity.request.FunctionCaseData;
import com.xailab.vehicle.xaivehicledata.entity.request.FunctionTreeVideoNewRequest;
import com.xailab.vehicle.xaivehicledata.entity.request.PerceptionAbilityRequest;
import com.xailab.vehicle.xaivehicledata.entity.request.QueryFunctionTreeVideoRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.*;
import com.xailab.vehicle.xaivehicledata.entity.vo.*;
import com.xailab.vehicle.xaivehicledata.entity.vo.VehicleFunctionGradeVo;
import com.xailab.vehicle.xaivehicledata.service.*;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;


@Service("functionTreeService")
@RequiredArgsConstructor
@Slf4j
public class FunctionTreeServiceImpl extends ServiceImpl<FunctionTreeDao, FunctionTreeEntity> implements FunctionTreeService {

    @Autowired
    private FunctionTreeDao functionTreeDao;

    private final ALiYunOssConfig ossConfig;

    @Resource
    private VehicleUserService vehicleUserService;

    @Resource
    private VehicleTryUserService vehicleTryUserService;

    @Resource
    private ALiYunOSSService aLiYunOSSService;

    @Resource
    private FunctionThreeTagDao functionThreeTagDao;

    @Resource
    private BaseInfoService baseInfoService;

    /**
     * 车辆基础信息
     */
    @Resource
    private BaseInfoDao baseInfoDao;

    @Resource
    private FunctionTreeCaseDao functionTreeCaseDao;

    @Resource
    private FunctionCaseVehicleDao functionCaseVehicleDao;

    @Resource
    private FunctionCaseMaterialDao functionCaseMaterialDao;

    private static final SnowflakeIdGenerator snowflakeIdGenerator
            = new SnowflakeIdGenerator(0, 0);

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FunctionTreeEntity> page = this.page(
                new Query<FunctionTreeEntity>().getPage(params),
                new QueryWrapper<FunctionTreeEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ThreeTagList> queryPenetrationRateByFunctionTreeId(List<String> ids, Long allVehicleCount,Boolean isTry,String language,Long loginId) {
        List<PenetrationRateVO> penetrationRateVos = new ArrayList<>();
        VehicleConstant constant=new VehicleConstant();

        if (isTry) {
            List<String> s = new ArrayList<>();

            List<VehicleTryUserEntity> vehicleTryUserEntityList = vehicleTryUserService.getTryUserListByUserId(loginId);
            if (!vehicleTryUserEntityList.isEmpty()){
                for (VehicleTryUserEntity vehicleTryUserEntity : vehicleTryUserEntityList) {
                    s.add(vehicleTryUserEntity.getVehicleId()+"");
                }
            }else {
                s.add(constant.getVehicle1Id());
                s.add(constant.getVehicle2Id());
                s.add(constant.getVehicle3Id());
            }
            penetrationRateVos = functionTreeDao.queryPenetrationRateByFunctionTreeId(ids,s,language);
        }else {
            penetrationRateVos = functionTreeDao.queryPenetrationRateByFunctionTreeId(ids,new ArrayList<String>(),language);
        }
        List<ThreeTagList> threeTagLists = penetrationRateVos.stream().map(penetrationRateVo -> {
            ThreeTagList threeTagList = new ThreeTagList();
            threeTagList.setDomain(penetrationRateVo.getThreeTagName());
            threeTagList.setMainDomain(penetrationRateVo.getOneTagName());
            threeTagList.setCount(penetrationRateVo.getCount());
            double v = ((double) penetrationRateVo.getCount()) / (double) (allVehicleCount+1) * 100;
            //保留两位小数
            String formattedV = String.format("%.2f", v);
            //转为double类型
            double doubleV = Double.parseDouble(formattedV);
            threeTagList.setPenetration(doubleV);

            return threeTagList;
        }).toList();

        return threeTagLists;
    }

    @Override
    public List<FirstLevelTagRatioResponse> queryFirstLevelTagRatioByVehicleIds(List<String> ids,String language) {

        VehicleConstant constant=new VehicleConstant();
        Object loginId = StpUtil.getTokenInfo().getLoginId();
        VehicleUserEntity user = vehicleUserService.getById(loginId+"");

        List<String> vehicleId = new ArrayList<>();

        if (user.getStatus() == 1){

            List<VehicleTryUserEntity> vehicleTryUserEntityList = vehicleTryUserService.getTryUserListByUserId(Long.parseLong(loginId+""));
            if (!vehicleTryUserEntityList.isEmpty()){
                for (VehicleTryUserEntity vehicleTryUserEntity : vehicleTryUserEntityList) {
                    vehicleId.add(vehicleTryUserEntity.getVehicleId()+"");
                }
            }else {
                vehicleId.add(constant.getVehicle2Id());
                vehicleId.add(constant.getVehicle1Id());
                vehicleId.add(constant.getVehicle3Id());
            }
        }

        List<FirstLevelTagRatioVo> firstLevelTagRatioVos = functionTreeDao.queryFirstLevelTagRatioByVehicleIds(ids,vehicleId,language);

        return convertToResponse(firstLevelTagRatioVos);
    }

    @Override
    public List<VehicleFunctionGradeResponse> queryVehicleFunctionGradeByFunctionTreeIdsAndVehicleIds(List<String> oneTagIds, List<String> vehicleIds,String language) {
        List<String> filteredList = new ArrayList<>();
        List<String> filteredList2 = new ArrayList<>();
        if (vehicleIds.size() > 0) {
            filteredList = vehicleIds.stream()
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }
        if (oneTagIds.size() > 0) {
            filteredList2 = oneTagIds.stream()
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }
        List<VehicleFunctionGradeVo> vehicleFunctionGradeVos = functionTreeDao.queryVehicleFunctionGradeByFunctionTreeIdsAndVehicleIds(filteredList2, filteredList,language);

        return convert(vehicleFunctionGradeVos);
    }

    @Override
    public FileUrlResponse queryVideoOrPictureByThreeTagIdAndVehicleId(String threeTagIds, String vehicleIds,String language) {
        BaseInfoEntity one = baseInfoService.getOne(Wrappers.<BaseInfoEntity>lambdaQuery().eq(BaseInfoEntity::getId, vehicleIds));

        FileUrlVo fileUrlVo = functionTreeDao.queryVideoOrPictureByThreeTagIdAndVehicleId(threeTagIds, vehicleIds, language);

        FileUrlResponse response = new FileUrlResponse();
        response.setDescription(fileUrlVo.getDescription());
        if (Objects.nonNull(fileUrlVo.getFunctionLabel())) {
            response.setFunctionLabel(fileUrlVo.getFunctionLabel().split("-"));
        }
        Okhttp3Utils okhttp3Utils = new Okhttp3Utils();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        if (one.getVehicleType()==1) {
            if (!ObjectUtils.isEmpty(fileUrlVo.getVideoNumber()) && StringUtils.hasLength(fileUrlVo.getVideoNumber())) {
                response.setVideoId(vehicleIds + "-" + fileUrlVo.getVideoNumber());
                try {
                    String data = okhttp3Utils.getData(ossConfig.getGetPlayList() + "?file_id=" + vehicleIds + "-" + fileUrlVo.getVideoNumber(), headers);
                    OSSResponse ossResponse = JsonUtils.jsonToObj(data, OSSResponse.class);
                    response.setVideoUrl(ossResponse.getPlaylist_url());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (Objects.nonNull(fileUrlVo.getSrtUrl()) && Integer.parseInt(fileUrlVo.getSrtUrl()) == 1 && "en".equals(language)) {
                    response.setSrtUrl(aLiYunOSSService.queryStr(vehicleIds + "-" + fileUrlVo.getVideoNumber() + "-en.srt"));
                }
                response.setType("video");
            } else if (StringUtils.hasLength(fileUrlVo.getPictureNumber())) {
                List<String> pictureUrls = new ArrayList<>();

                try {
                    String[] split = fileUrlVo.getPictureNumber().split("/");
                    for (String pictureNumber : split) {
                        String data = okhttp3Utils.getData(ossConfig.getGetImageUrl() + "?file_id=" + vehicleIds + "-" + pictureNumber, headers);
                        OSSResponse ossResponse = JsonUtils.jsonToObj(data, OSSResponse.class);
                        pictureUrls.add(ossResponse.getUrl());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                response.setType("picture");
                response.setPictureUrl(pictureUrls);
            }

            return response;
        }else {
            List<FunctionTreeVideoNewVo> functionTreeVideoVo = functionThreeTagDao.queryCaseVideo(threeTagIds,vehicleIds);

            FunctionTreeVideoNewRequest convert = convert1(functionTreeVideoVo);
            List<FunctionCaseData> caseDataList = convert.getCaseDataList();
            for (FunctionCaseData data:caseDataList){
                response.setVideoId("功能树三级指标"+threeTagIds);
                response.setVideoUrl(data.getVideoNumber());
                response.setType("video");
            }
            return response;
        }
    }

    public FunctionTreeVideoNewRequest convert1(List<FunctionTreeVideoNewVo> voList) {

        Okhttp3Utils okhttp3Utils = new Okhttp3Utils();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        FunctionTreeVideoNewRequest request = new FunctionTreeVideoNewRequest();

        // 1. 处理 description（取第一个非空值）
        String description = voList.stream()
                .map(FunctionTreeVideoNewVo::getDescription)
                .filter(d -> d != null && !d.isEmpty())
                .findFirst()
                .orElse(null);
        request.setDescription(description);

        // 2. 处理 functionLabel（拆分为数组并去重）
        String functionLabel = voList.stream()
                .map(FunctionTreeVideoNewVo::getFunctionLabel)
                .filter(d -> d != null && !d.isEmpty())
                .findFirst()
                .orElse(null);

        // 3. 处理 caseDataList（每个Vo转换为FunctionCaseData）
        List<FunctionCaseData> caseDataList = new ArrayList<>();
        for (FunctionTreeVideoNewVo vo : voList) {
            FunctionCaseData caseData = new FunctionCaseData();
            caseData.setCaseContent(vo.getCaseContent());
            if (Objects.nonNull(vo.getFunctionLabel())) {
                caseData.setFunctionLabel(vo.getFunctionLabel().split("-"));
            }
            if (Objects.nonNull(vo.getMaterialUrl())){
                try {
                    String data=null;
                    if (vo.getType()==0) {
                        // 0是图片
                        data= okhttp3Utils.getData(ossConfig.getGetImageUrl() + "?file_id=" + vo.getMaterialUrl(), headers);
                        OSSResponse ossResponse = JsonUtils.jsonToObj(data, OSSResponse.class);
                        caseData.setVideoNumber(ossResponse.getUrl());
                    }else if (vo.getType()==1){
                        // 1是视频
                        data = okhttp3Utils.getData(ossConfig.getGetPlayList() + "?file_id=" + vo.getMaterialUrl(), headers);
                        OSSResponse ossResponse = JsonUtils.jsonToObj(data, OSSResponse.class);
                        caseData.setVideoNumber(ossResponse.getPlaylist_url());
                    }

                    caseData.setMaterialType(vo.getType());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            caseData.setVideoStr(null); // 无数据来源，设为null
            if (Objects.nonNull(caseData.getVideoNumber())) {
                caseDataList.add(caseData);
            }
        }
        request.setCaseDataList(caseDataList);

        return request;
    }

    @Override
    public List<FunctionRichnessRatioRequest> queryFunctionRichnessRatioByVehicleId(String id,String language) {

        List<FunctionRichnessRatioRequest> functionRichnessRatioRequests = new LinkedList<>();
        List<FunctionRichnessRatioVo> functionRichnessRatioVos = functionTreeDao.queryFunctionRichnessRatioByVehicleId(id,language);
// 按照 oneId 从大到小排序
        Collections.sort(functionRichnessRatioVos, new Comparator<FunctionRichnessRatioVo>() {
            @Override
            public int compare(FunctionRichnessRatioVo o1, FunctionRichnessRatioVo o2) {
                return Long.compare(o2.getOneId(), o1.getOneId()); // 降序排序
            }
        });
        for (FunctionRichnessRatioVo functionRichnessRatioVo : functionRichnessRatioVos) {
            FunctionRichnessRatioRequest request = new FunctionRichnessRatioRequest();
            request.setFunctionName(functionRichnessRatioVo.getFunctionName());
            request.setDenominator(functionRichnessRatioVo.getThreeCount() + "");
            request.setMolecule(functionRichnessRatioVo.getCount() + "");

            double ratio = functionRichnessRatioVo.getCount() / functionRichnessRatioVo.getThreeCount() * 100;
            String formattedRatio = String.format("%.0f", ratio);
            request.setRatio(String.valueOf(Double.parseDouble(formattedRatio)));
            functionRichnessRatioRequests.add(request);
        }

        return functionRichnessRatioRequests;
    }

    @Override
    public List<PerceptionAbilityRequest> queryPerceptionAbilityByVehicleId(List<String> id,String language) {

        List<PerceptionAbilityVo> perceptionAbilityVoList = functionTreeDao.queryPerceptionAbilityByVehicleId(id,language);

        return perceptionAbilityConverter(perceptionAbilityVoList);
    }


    /**
     * 数据同步
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<FunctionTreeTaskSyncJournalResponse> pcafeDataSync(FunctionTreeDataSyncRequest request) {
        log.info("pcafe数据同步 请求参数为：{}",JsonUtils.objToJson(request));
        String vehicleId = request.getVehicleId();
        //查询车辆信息 是否为空
        BaseInfoEntity baseInfoEntity = baseInfoDao.selectById(vehicleId);
        if (Objects.isNull(baseInfoEntity)) {
            return Result.error("车辆信息不存在");
        }
        //创建同步日志
        FunctionTreeTaskSyncJournalResponse response = new FunctionTreeTaskSyncJournalResponse();
        BeanUtils.copyProperties(request, response);
        response.setSyncInfo(new ArrayList<>());

        List<FunctionTreeDataSyncInfoRequest> syncInfo = request.getSyncInfo();
        //同步
        for (FunctionTreeDataSyncInfoRequest info : syncInfo) {
            FunctionTreeDataSyncInfoJournal syncInfoJournal = new FunctionTreeDataSyncInfoJournal();
            BeanUtils.copyProperties(info, syncInfoJournal);
            syncInfoJournal.setFunctionCaseData(new ArrayList<>());
            Result<FunctionTreeEntity> voidResult = syncInfoCore(info, baseInfoEntity, syncInfoJournal);
            if (!voidResult.isOk()) {
                syncInfoJournal.setCode(voidResult.getCode());
                syncInfoJournal.setMessage(voidResult.getMsg());
                log.info("功能数数据同步失败：tagNumber：{},message:{}",info.getTagNumber(),voidResult.getMsg());
                continue;
            }
            FunctionTreeEntity treeEntity = voidResult.getData();
            //同步测试用例信息
            List<FunctionTreeDataSyncCaseInfoDto> functionCaseData = info.getFunctionCaseData();
            for (FunctionTreeDataSyncCaseInfoDto functionCaseDataDto : functionCaseData) {
                FunctionTreeDataSyncCaseInfoJournal syncCaseInfoJournal = new FunctionTreeDataSyncCaseInfoJournal();
                BeanUtils.copyProperties(functionCaseDataDto, syncCaseInfoJournal);
                syncInfoJournal.getFunctionCaseData().add(syncCaseInfoJournal);

                Result<FunctionCaseVehicleEntity> resultCase = functionTreeCaseSync(functionCaseDataDto, info, treeEntity,syncCaseInfoJournal);
                if (!resultCase.isOk()){
                    syncCaseInfoJournal.setCode(resultCase.getCode());
                    syncCaseInfoJournal.setMessage(resultCase.getMsg());
                    log.info("功能树用例数据同步失败：tagNumber：{},用例id：{},message:{}",info.getTagNumber(),functionCaseDataDto.getId(),voidResult.getMsg());
                    continue;
                }
                FunctionCaseVehicleEntity vehicleEntity = resultCase.getData();
                //同步素材
                List<FunctionTreeDataSyncCaseInfoDto.CaseFileMaterial> caseFileMaterial = functionCaseDataDto.getCaseFileMaterial();
                //同步素材
                List<FunctionTreeDataSyncCaseInfoJournal.CaseFileMaterial> caseFileMaterials = functionTreeCaseMaterialSync(caseFileMaterial, vehicleEntity);
                //日志记录
                syncCaseInfoJournal.setCaseFileMaterial(caseFileMaterials);

            }
            //添加日志
            response.getSyncInfo().add(syncInfoJournal);
        }
        return Result.ok(response);
    }


    /**
     * 数据同步回滚
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> taskSyncFallback(FunctionTreeTaskSyncJournalResponse request) {
        log.info("pcafe数据同步回滚 请求参数为：{}",JsonUtils.objToJson(request));
        String vehicleId = request.getVehicleId();
        //查询车辆信息 是否为空
        BaseInfoEntity baseInfoEntity = baseInfoDao.selectById(vehicleId);
        if (Objects.isNull(baseInfoEntity)) {
            return Result.error("数据回滚车辆信息不存在");
        }
        List<FunctionTreeDataSyncInfoJournal> syncInfo = request.getSyncInfo();
        //同步
        for (FunctionTreeDataSyncInfoJournal info : syncInfo) {
            Result<FunctionTreeEntity> voidResult = syncRollBackInfoCore(info, baseInfoEntity);
            if (!voidResult.isOk()) {
                log.info("功能数数据同步回滚失败：tagNumber：{},message:{}", info.getTagNumber(), voidResult.getMsg());
                continue;
            }
            FunctionTreeEntity treeEntity = voidResult.getData();
            //同步测试用例信息
            List<FunctionTreeDataSyncCaseInfoJournal> functionCaseData = info.getFunctionCaseData();
            for (FunctionTreeDataSyncCaseInfoJournal functionCaseDataDto : functionCaseData) {
                Result<FunctionCaseVehicleEntity> resultCase = functionTreeCaseSyncRollback(functionCaseDataDto, info, treeEntity);
                if (!resultCase.isOk()) {
                    log.info("数据回退用例数据同步失败：tagNumber：{},用例id：{},message:{}", info.getTagNumber(), functionCaseDataDto.getId(), voidResult.getMsg());
                    continue;
                }
                FunctionCaseVehicleEntity vehicleEntity = resultCase.getData();
                //同步素材
                List<FunctionTreeDataSyncCaseInfoJournal.CaseFileMaterial> caseFileMaterial = functionCaseDataDto.getCaseFileMaterial();
                //同步素材
                functionTreeCaseMaterialSyncRollBack(caseFileMaterial, vehicleEntity);
            }
        }
        return Result.ok();
    }

    /**
     * 同步详情操作
     * @param info
     * @param baseInfoEntity
     */
    private Result<FunctionTreeEntity> syncRollBackInfoCore(FunctionTreeDataSyncInfoJournal info, BaseInfoEntity baseInfoEntity) {
        if (!info.getCode().equals(200)){
            log.info("功能树数据同步日志为失败：tagNumber：{},message:{}",info.getTagNumber(),info.getMessage());
            return Result.error(info.getCode(),info.getMessage());
        }
        //查询tagnumber id
        FunctionThreeTagEntity threeTagEntity = functionThreeTagDao.selectOne(Wrappers.<FunctionThreeTagEntity>lambdaQuery()
                .eq(FunctionThreeTagEntity::getTagNumber, info.getTagNumber())
        );
        if (Objects.isNull(threeTagEntity)) {
            log.info("功能树数据回滚 功能树信息不存在：{}", info.getTagNumber());
            return Result.error("功能树信息不存在");
        }
        //查询当前功能树信息是否存在
        FunctionTreeEntity treeEntity = baseMapper.selectOne(Wrappers.<FunctionTreeEntity>lambdaQuery()
                .eq(FunctionTreeEntity::getVehicleId, baseInfoEntity.getId())
                .eq(FunctionTreeEntity::getFunctionThreeTagId, threeTagEntity.getId())
        );
        if (Objects.isNull(treeEntity)){
            log.info("beeeval功能树对应车辆信息为空:{}", info.getTagNumber());
            return Result.error("beeeval功能树对应车辆信息为空");
        }
        //判断当前数据是否是新增，如果是新增则删除，否则则更
        if (info.getOperationType().equals("add")) {
            log.info("功能树数据回滚 功能树信息不存在：{}", info.getTagNumber());
            baseMapper.delete(Wrappers.<FunctionTreeEntity>lambdaQuery()
                    .eq(FunctionTreeEntity::getVehicleId, baseInfoEntity.getId())
                    .eq(FunctionTreeEntity::getFunctionThreeTagId, threeTagEntity.getId())
            );
            return Result.ok(treeEntity);
        }
        treeEntity.setFunctionList(info.getFunctionEvaluateOriginal());
        treeEntity.setFunctionLabel(StringUtils.hasLength(info.getSyncOptionOriginal())?info.getSyncOptionOriginal():"");
        baseMapper.insertOrUpdate(treeEntity);
        return Result.ok(treeEntity);
    }


    /**
     * 同步测试用例信息
     * @param functionCaseDataDto
     * @return
     */
    private Result<FunctionCaseVehicleEntity> functionTreeCaseSyncRollback(FunctionTreeDataSyncCaseInfoJournal functionCaseDataDto,
                                                                           FunctionTreeDataSyncInfoJournal info,
                                                                   FunctionTreeEntity functionTreeEntity) {
        if (!functionCaseDataDto.getCode().equals(200)){
            log.info("功能树测试用例同步日志为失败：caseId：{},message:{}",functionCaseDataDto.getId(),functionCaseDataDto.getMessage());
            return Result.error(functionCaseDataDto.getCode(),functionCaseDataDto.getMessage());
        }
        //查询当前测试用例
        FunctionTreeCaseEntity testCaseEntity = functionTreeCaseDao.selectOne(Wrappers.<FunctionTreeCaseEntity>lambdaQuery()
                .eq(FunctionTreeCaseEntity::getId, functionCaseDataDto.getId())
                .eq(FunctionTreeCaseEntity::getThreeTagId, info.getTagNumber())
        );
        if (Objects.isNull(testCaseEntity)) {
            log.info("回退beeeval测试用例不存在：{},tagNumber:{}", functionCaseDataDto.getId(),info.getTagNumber());
            return Result.error("beeeval测试用例不存在");
        }

        //查询当前存储信息的测试用例
        FunctionCaseVehicleEntity functionCaseVehicleEntity = functionCaseVehicleDao.selectOne(Wrappers.<FunctionCaseVehicleEntity>lambdaQuery()
                .eq(FunctionCaseVehicleEntity::getCaseId, testCaseEntity.getId())
                .eq(FunctionCaseVehicleEntity::getVehicleId, functionTreeEntity.getVehicleId())
        );
        if (Objects.isNull(functionCaseVehicleEntity)) {
           log.info("数据回退，测试用例对应车状态为空：caseId:{},vehicleId：{}", testCaseEntity.getId(), functionTreeEntity.getVehicleId());
           return Result.error("测试用例对应车状态为空");
        }
        //判断是否新增，如果新增操作就进行删除
        if (functionCaseDataDto.getOperationType().equals("add")) {
            functionCaseVehicleDao.delete(Wrappers.<FunctionCaseVehicleEntity>lambdaQuery()
                    .eq(FunctionCaseVehicleEntity::getCaseId, testCaseEntity.getId())
                    .eq(FunctionCaseVehicleEntity::getVehicleId, functionTreeEntity.getVehicleId())
            );
            return Result.ok(functionCaseVehicleEntity);
        }
        functionCaseVehicleEntity.setIsShow(functionCaseDataDto.getIsShowOriginal()?1:0);
        functionCaseVehicleEntity.setFunctionEvaluation(functionCaseDataDto.getFunctionEvaluationOriginal());
        //设置回滚的测试用例选项数据
        functionCaseVehicleEntity.setCaseOptions(StringUtils.hasLength(functionCaseDataDto.getSyncOptionOriginal())?functionCaseDataDto.getSyncOptionOriginal():"");
        functionCaseVehicleDao.insertOrUpdate(functionCaseVehicleEntity);
        return Result.ok(functionCaseVehicleEntity);
    }


    /**
     * 同步素材
     *
     * @param fileMaterial
     */
    private void functionTreeCaseMaterialSyncRollBack(List<FunctionTreeDataSyncCaseInfoJournal.CaseFileMaterial> fileMaterial,FunctionCaseVehicleEntity vehicleEntity) {
        List<String> urlList = fileMaterial.stream().filter(it -> it.getCode().equals(200) && it.getOperationType().equals("add"))
                .map(FunctionTreeDataSyncCaseInfoJournal.CaseFileMaterial::getMaterialUrl).toList();
        if (CollectionUtils.isEmpty(urlList)){
            log.info("数据回滚,对应新增素材为空caseId：{}", vehicleEntity.getCaseId());
            return;
        }
        int delete = functionCaseMaterialDao.delete(Wrappers.<FunctionCaseMaterialEntity>lambdaQuery()
                .eq(FunctionCaseMaterialEntity::getFunctionTreeCaseId, vehicleEntity.getCaseId())
                .eq(FunctionCaseMaterialEntity::getVehicleId, vehicleEntity.getVehicleId())
                .in(FunctionCaseMaterialEntity::getMaterialUrl, urlList)
        );
        log.info("数据回滚,删除素材数量为：{}",delete);
    }


    /**
     * 同步素材
     *
     * @param fileMaterial
     * @param vehicleEntity
     */
    private List<FunctionTreeDataSyncCaseInfoJournal.CaseFileMaterial> functionTreeCaseMaterialSync(List<FunctionTreeDataSyncCaseInfoDto.CaseFileMaterial> fileMaterial,
                                                                                                    FunctionCaseVehicleEntity vehicleEntity) {
        //查询出所有的已经存在的素材名称
        List<String> urlList = functionCaseMaterialDao.selectUrlByCaseId(vehicleEntity.getCaseId(), vehicleEntity.getVehicleId());
        log.info("查询出已经存在的素材名称为：{},veheicleId：{}，caseId:{}", urlList, vehicleEntity.getVehicleId(), vehicleEntity.getCaseId());
        List<FunctionCaseMaterialEntity> list = fileMaterial.stream().filter(it -> !urlList.contains(it.getMaterialUrl())).map(it -> {
            FunctionCaseMaterialEntity materialEntity = new FunctionCaseMaterialEntity();
            materialEntity.setFunctionTreeCaseId(vehicleEntity.getCaseId());
            materialEntity.setMaterialUrl(it.getMaterialUrl());
            materialEntity.setVehicleId(vehicleEntity.getVehicleId());
            materialEntity.setMaterialType(it.getMaterialType());
            return materialEntity;
        }).toList();
        functionCaseMaterialDao.insert(list);
        //记录日志
        List<FunctionTreeDataSyncCaseInfoJournal.CaseFileMaterial> journalList = fileMaterial.stream().map(it -> {
            FunctionTreeDataSyncCaseInfoJournal.CaseFileMaterial material = new FunctionTreeDataSyncCaseInfoJournal.CaseFileMaterial();
            material.setMaterialUrl(it.getMaterialUrl());
            material.setMaterialType(it.getMaterialType());
            if (urlList.contains(it.getMaterialUrl())) {
                material.setCode(500);
                material.setMessage("素材已存在");
                material.setOperationType("update");
            } else {
                material.setCode(200);
                material.setOperationType("add");
            }
            return material;
        }).toList();
        return journalList;
    }

    /**
     * 同步详情操作
     * @param info
     * @param baseInfoEntity
     */
    private Result<FunctionTreeEntity> syncInfoCore(FunctionTreeDataSyncInfoRequest info, BaseInfoEntity baseInfoEntity,FunctionTreeDataSyncInfoJournal infoJournal) {
        //查询tagnumber id
        FunctionThreeTagEntity threeTagEntity = functionThreeTagDao.selectOne(Wrappers.<FunctionThreeTagEntity>lambdaQuery()
                .eq(FunctionThreeTagEntity::getTagNumber, info.getTagNumber())
        );
        if (Objects.isNull(threeTagEntity)) {
            //TODO 是否添加同步日志
            log.info("功能树信息不存在：{}", info.getTagNumber());
            return Result.error("功能树信息不存在");
        }
        //查询当前功能树信息是否存在
        FunctionTreeEntity treeEntity = baseMapper.selectOne(Wrappers.<FunctionTreeEntity>lambdaQuery()
                .eq(FunctionTreeEntity::getVehicleId, baseInfoEntity.getId())
                .eq(FunctionTreeEntity::getFunctionThreeTagId, threeTagEntity.getId())
        );
        if (Objects.isNull(treeEntity)){
            treeEntity = new FunctionTreeEntity();
            treeEntity.setId(snowflakeIdGenerator.nextId());
            treeEntity.setVehicleId(baseInfoEntity.getId());
            treeEntity.setFunctionThreeTagId(threeTagEntity.getId());
            infoJournal.setOperationType("add");
        }else {
            infoJournal.setOperationType("update");
            infoJournal.setFunctionEvaluateOriginal(treeEntity.getFunctionList());
            infoJournal.setSyncOptionOriginal(treeEntity.getFunctionLabel());
        }
        treeEntity.setFunctionList(info.getFunctionEvaluate());
//        if (StringUtils.hasLength(info.getSyncOption())){
//            treeEntity.setFunctionLabel(info.getSyncOption());
//        }
        baseMapper.insertOrUpdate(treeEntity);
        //设置成功
        infoJournal.setCode(200);
        return Result.ok(treeEntity);
    }

    /**
     * 同步测试用例信息
     * @param functionCaseDataDto
     * @return
     */
    private Result<FunctionCaseVehicleEntity> functionTreeCaseSync(FunctionTreeDataSyncCaseInfoDto functionCaseDataDto,
                                                                   FunctionTreeDataSyncInfoRequest info,
                                                                   FunctionTreeEntity functionTreeEntity,
                                                                   FunctionTreeDataSyncCaseInfoJournal syncCaseInfoJournal) {
        //查询当前测试用例
        FunctionTreeCaseEntity testCaseEntity = functionTreeCaseDao.selectOne(Wrappers.<FunctionTreeCaseEntity>lambdaQuery()
                .eq(FunctionTreeCaseEntity::getId, functionCaseDataDto.getId())
                .eq(FunctionTreeCaseEntity::getThreeTagId, info.getTagNumber())
        );
        if (Objects.isNull(testCaseEntity)) {
            log.info("beeeval测试用例不存在：{},tagNumber:{}", functionCaseDataDto.getId(),info.getTagNumber());
            return Result.error("beeeval测试用例不存在");
        }
        //查询当前存储信息的测试用例
        FunctionCaseVehicleEntity functionCaseVehicleEntity = functionCaseVehicleDao.selectOne(Wrappers.<FunctionCaseVehicleEntity>lambdaQuery()
                .eq(FunctionCaseVehicleEntity::getCaseId, testCaseEntity.getId())
                .eq(FunctionCaseVehicleEntity::getVehicleId, functionTreeEntity.getVehicleId())
        );
        if (Objects.isNull(functionCaseVehicleEntity)) {
            functionCaseVehicleEntity = new FunctionCaseVehicleEntity();
            functionCaseVehicleEntity.setCaseId(testCaseEntity.getId());
            functionCaseVehicleEntity.setVehicleId(functionTreeEntity.getVehicleId());
            syncCaseInfoJournal.setOperationType("add");
        }else {
            syncCaseInfoJournal.setOperationType("update");
            syncCaseInfoJournal.setFunctionEvaluationOriginal(functionCaseVehicleEntity.getFunctionEvaluation());
            syncCaseInfoJournal.setIsShowOriginal(functionCaseVehicleEntity.getIsShow().equals(1));
            //记录同步的测试用例对于的选项信息
            syncCaseInfoJournal.setSyncOptionOriginal(functionCaseVehicleEntity.getCaseOptions());
        }
        functionCaseVehicleEntity.setIsShow(functionCaseDataDto.getIsShow()?1:0);
        functionCaseVehicleEntity.setFunctionEvaluation(functionCaseDataDto.getFunctionEvaluation());
        //同步测试用例的选项信息
        functionCaseVehicleEntity.setCaseOptions(functionCaseDataDto.getSyncOption());
        functionCaseVehicleDao.insertOrUpdate(functionCaseVehicleEntity);
        syncCaseInfoJournal.setCode(200);
        return Result.ok(functionCaseVehicleEntity);
    }



    public static List<PerceptionAbilityRequest> perceptionAbilityConverter(List<PerceptionAbilityVo> voList) {
        // 按 perceptionAbilityName 分组
        Map<String, List<PerceptionAbilityVo>> groupedByPerceptionAbilityName = voList.stream()
                .collect(Collectors.groupingBy(PerceptionAbilityVo::getPerceptionAbilityName));

        // 构造 PerceptionAbilityRequest 列表
        List<PerceptionAbilityRequest> requestList = new ArrayList<>();
        for (Map.Entry<String, List<PerceptionAbilityVo>> entry : groupedByPerceptionAbilityName.entrySet()) {
            String perceptionAbilityName = entry.getKey();
            List<PerceptionAbilityVo> vos = entry.getValue();

            // 构造 PerceptionAbilityRequest 对象
            PerceptionAbilityRequest request = new PerceptionAbilityRequest();
            request.setPerceptionAbilityName(perceptionAbilityName);

            // 构造 PerceptionAbilitySmall 列表
            List<PerceptionAbilityRequest.PerceptionAbilitySmall> smallList = vos.stream()
                    .collect(Collectors.groupingBy(PerceptionAbilityVo::getThreeTagName))
                    .entrySet().stream()
                    .map(entrySmall -> {
                        PerceptionAbilityRequest.PerceptionAbilitySmall small = new PerceptionAbilityRequest.PerceptionAbilitySmall();
                        small.setThreeTagName(entrySmall.getKey());

                        // 构造 Vehicle 列表
                        List<PerceptionAbilityRequest.Vehicle> vehicleList = entrySmall.getValue().stream()
                                .map(vo -> new PerceptionAbilityRequest.Vehicle()
                                        .setVehicleId(vo.getVehicleId())
                                        .setVehicleName(vo.getVehicleName())
                                        .setIsHave(vo.getIsHave()))
                                .collect(Collectors.toList());

                        small.setVehicleList(vehicleList);
                        return small;
                    })
                    .collect(Collectors.toList());

            request.setPerceptionAbilitySmallList(smallList);
            requestList.add(request);
        }

        return requestList;
    }

    public static List<VehicleFunctionGradeResponse> convert(List<VehicleFunctionGradeVo> voList) {
        // 按照一级标签分组
        Map<String, List<VehicleFunctionGradeVo>> oneTagMap = voList.stream()
                .collect(Collectors.groupingBy(VehicleFunctionGradeVo::getOneTagId));

        return oneTagMap.entrySet().stream()
                .map(entry -> {
                    // 一级标签处理
                    VehicleFunctionGradeResponse response = new VehicleFunctionGradeResponse();
                    response.setOneTagId(entry.getKey());
                    response.setOneTagName(entry.getValue().get(0).getOneTagName());

                    // 二级标签处理
                    Map<String, List<VehicleFunctionGradeVo>> twoTagMap = entry.getValue().stream()
                            .collect(Collectors.groupingBy(VehicleFunctionGradeVo::getTwoTagId));

                    List<VehicleFunctionGradeResponse.VehicleFunctionTwoGradeResponse> twoGradeResponses =
                            twoTagMap.entrySet().stream()
                                    .map(twoEntry -> {
                                        VehicleFunctionGradeResponse.VehicleFunctionTwoGradeResponse twoGrade =
                                                new VehicleFunctionGradeResponse.VehicleFunctionTwoGradeResponse();
                                        twoGrade.setTwoTagId(twoEntry.getKey());
                                        twoGrade.setTwoTagName(twoEntry.getValue().get(0).getTwoTagName());

                                        // 三级标签处理
                                        Map<String, List<VehicleFunctionGradeVo>> threeTagMap = twoEntry.getValue().stream()
                                                .collect(Collectors.groupingBy(VehicleFunctionGradeVo::getThreeTagId));

                                        List<ThreeFunctionGradeVo> threeGradeVos = threeTagMap.entrySet().stream()
                                                .map(threeEntry -> {
                                                    ThreeFunctionGradeVo threeGrade = new ThreeFunctionGradeVo();
                                                    threeGrade.setThreeTagId(threeEntry.getKey());
                                                    threeGrade.setThreeTagName(threeEntry.getValue().get(0).getThreeTagName());

                                                    // 车辆功能列表处理
                                                    List<VehicleFunctionGradeVoResponse> vehicleFunctions = threeEntry.getValue().stream()
                                                            .map(vo -> {
                                                                VehicleFunctionGradeVoResponse function = new VehicleFunctionGradeVoResponse();
                                                                function.setVehicleId(vo.getVehicleId());
                                                                function.setVehicleName(vo.getVehicleName());
                                                                function.setFunctionList(vo.getFunctionList());
                                                                return function;
                                                            })
                                                            .collect(Collectors.toList());

                                                    threeGrade.setVehiclefunctionGrade(vehicleFunctions);
                                                    return threeGrade;
                                                })
                                                .collect(Collectors.toList());

                                        twoGrade.setThreeFunctionGradeVos(threeGradeVos);
                                        return twoGrade;
                                    })
                                    .collect(Collectors.toList());

                    response.setVehicleFunctionTwoGradeResponses(twoGradeResponses);
                    return response;
                })
                .collect(Collectors.toList());
    }

    public static List<FirstLevelTagRatioResponse> convertToResponse(List<FirstLevelTagRatioVo> inputList) {
        // 按照oneTagName和oneId分组
        Map<String, List<FirstLevelTagRatioVo>> groupedData = inputList.stream()
                .collect(Collectors.groupingBy(vo -> vo.getOneTagName() + "-" + vo.getOneId()));

        // 构建最终的响应列表
        List<FirstLevelTagRatioResponse> responseList = new ArrayList<>();

        for (Map.Entry<String, List<FirstLevelTagRatioVo>> entry : groupedData.entrySet()) {
            // 获取分组后的数据
            List<FirstLevelTagRatioVo> group = entry.getValue();
            // 提取oneTagName和oneId
            String oneTagName = group.get(0).getOneTagName();
            String oneId = group.get(0).getOneId();

            // 构建VehicleTagRatioResponse列表
            List<VehicleTagRatioResponse> vehicleTagRatios = group.stream()
                    .map(vo -> {
                        VehicleTagRatioResponse vehicleResponse = new VehicleTagRatioResponse();
                        vehicleResponse.setBrandModel(vo.getBrandModel());
                        vehicleResponse.setVehicleId(vo.getVehicleId());
                        // 计算tagRatio
                        if (vo.getThreeCount() != null && vo.getThreeCount() != 0) {
                            double ratio = vo.getCount() / (double) vo.getThreeCount() * 100;
                            String formattedRatio = String.format("%.2f", ratio);
                            vehicleResponse.setTagRatio(Double.parseDouble(formattedRatio));
                        } else {
                            vehicleResponse.setTagRatio(0.0); // 防止除以零
                        }
                        return vehicleResponse;
                    })
                    .collect(Collectors.toList());

            // 构建FirstLevelTagRatioResponse
            FirstLevelTagRatioResponse response = new FirstLevelTagRatioResponse();
            response.setOneTagName(oneTagName);
            response.setOneId(oneId);
            vehicleTagRatios.removeIf(user -> user.getBrandModel() == null);
            // 按照 oneId 从大到小排序
            vehicleTagRatios.sort(new Comparator<VehicleTagRatioResponse>() {
                @Override
                public int compare(VehicleTagRatioResponse o1, VehicleTagRatioResponse o2) {
                    return Long.compare(Long.parseLong(o2.getVehicleId()), Long.parseLong(o1.getVehicleId())); // 降序排序
                }
            });
            response.setVehicleTagRatio(vehicleTagRatios);

            // 添加到最终响应列表
            responseList.add(response);

        }



        return responseList;
    }

}