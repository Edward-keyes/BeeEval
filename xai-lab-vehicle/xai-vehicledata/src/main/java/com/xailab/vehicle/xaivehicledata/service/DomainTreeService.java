package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.feign.vo.FunctionDomainVideoVo;
import com.xailab.vehicle.feign.vo.FunctionalVideoVoF;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.R;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.DomainTreeEntity;
import com.xailab.vehicle.xaivehicledata.entity.FunctionalDomainEntity;
import com.xailab.vehicle.xaivehicledata.entity.request.DomainTreeQueryScoreRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.*;
import com.xailab.vehicle.xaivehicledata.entity.vo.ActionAbilityVo;
import com.xailab.vehicle.xaivehicledata.entity.vo.GeneralAbilityVo;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Map;

/**
 * 
 *
 *
 * @email d2460687074@gmail.com
 * @date 2025-02-26 02:07:44
 */
public interface DomainTreeService extends IService<DomainTreeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    FunctionalVideoListResponse getFunctionalVideoList(String vehicleId, String language, Integer page);

    FunctionalVideoNewListResponse getFunctionalVideoNewList(String vehicleId, String language, Integer page);

    /**
     * 数据批量导入
     * @param multipartFile
     * @return
     */
    Result<Void> batchDataImport(MultipartFile multipartFile);

    /**
     * 根据车品牌id 获取每个域下的平均值和个体值
     * @return
     */
    Result<List<DomainTreeQueryScoreResponse>> queryScoreByBrandInfo(DomainTreeQueryScoreRequest request);

    /**
     * 根据车辆品牌id 获取每个指标的分数
     * @param request
     * @return
     */
    List<DomainTreeQueryIndexScoreResponse> queryDomainIndexScore(DomainTreeQueryScoreRequest request,String language);

    /**
     * 查询总分排行
     * @param request
     * @return
     */
    Result<DomainTreeScoreResponse> countScoreSort(DomainTreeQueryScoreRequest request);


    /**
     * 基础能力 指标排行
     * @param request
     * @return
     */
    DomainTreeScoreResponse baseDomainIndexScoreSort(DomainTreeQueryScoreRequest request,String language);

    List<FunctionalDomainResponse> queryDomainTree(String language);

    DomainTreeScoreResponse queryDomainIndexScoreRank(DomainTreeQueryScoreRequest request,String language);

    List<GeneralAbilityVo> queryGeneralAbilityVos(List<String> vehicleId, String language);

    List<ActionAbilityVo> queryActionAbilityVos(List<String> vehicleId, String language);

    List<GeneralAbilityVo> queryGeneralAbilityAvgVos(List<String> vehicleId, String language);

    List<ActionAbilityVo> queryActionAbilityAVGVos(List<String> vehicleId, String language);

    FunctionalVideoListResponse getFunctionalVideoEditList(String vehicleId,String language);

    FunctionDomainVideoVo getVideoUrlDetail(FunctionalVideoVoF functionalVideoVo);

    Boolean updateFunctionDomainVideoInfo(FunctionDomainVideoVo functionDomainVideoVo);
}

