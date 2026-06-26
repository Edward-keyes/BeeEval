package com.xailab.vehicle.xaivehicledata.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.request.IdRequest;
import com.xailab.vehicle.xaivehicledata.entity.request.LanguageRequest;
import com.xailab.vehicle.xaivehicledata.entity.request.OneIDRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.BigModelCapabilityAssessmentBase;
import com.xailab.vehicle.xaivehicledata.entity.response.DomainIndexDetail;
import com.xailab.vehicle.xaivehicledata.entity.response.FunctionalDomainRepresentation;
import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionalDomainVehicleVo;
import com.xailab.vehicle.xaivehicledata.entity.vo.IndexDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xailab.vehicle.xaivehicledata.entity.DomainIndexEntity;
import com.xailab.vehicle.xaivehicledata.service.DomainIndexService;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.R;



/**
 * 
 *
 * 
 * @email d2460687074@gmail.com
 * @date 2025-02-26 02:07:44
 */
@RestController
@RequestMapping("vehicle/domainindex")
public class DomainIndexController {
    @Autowired
    private DomainIndexService domainIndexService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = domainIndexService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		DomainIndexEntity domainIndex = domainIndexService.getById(id);

        return R.ok().put("domainIndex", domainIndex);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody DomainIndexEntity domainIndex){
		domainIndexService.save(domainIndex);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody DomainIndexEntity domainIndex){
		domainIndexService.updateById(domainIndex);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		domainIndexService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 大模型能力测评-基础能力
     */
    @SaCheckLogin
    @PostMapping("/bigModelCapabilityAssessmentBase")
    public Result<List<BigModelCapabilityAssessmentBase>> largeModelCapabilityAssessment(@RequestBody IdRequest oneIDRequest) {

        List<BigModelCapabilityAssessmentBase> bigModelCapabilityAssessmentBase = domainIndexService.largeModelCapabilityAssessment(oneIDRequest.getIds(),oneIDRequest.getLanguage());

        return Result.ok(bigModelCapabilityAssessmentBase);
    }

    /**
     * 大模型能力评测-指标详情介绍
     */
    @SaCheckLogin
    @PostMapping("/bigModelIndexDetail")
    public Result<DomainIndexDetail> bigModelIndexDetail(@RequestBody LanguageRequest languageRequest) {

        DomainIndexDetail bigModelCapabilityAssessmentBase = domainIndexService.queryDomainIndexDetail(languageRequest.getLanguage());

        return Result.ok(bigModelCapabilityAssessmentBase);

    }


    /**
     * 大模型能力测评-功能域表现
     */
    @PostMapping("/bigModelCapabilityAssessmentFunction")
    public Result<List<FunctionalDomainVehicleVo>> largeModelCapabilityAssessmentFunction(@RequestBody IdRequest oneIDRequest) {

        List<FunctionalDomainVehicleVo> functionalDomainRepresentations = domainIndexService.largeModelCapabilityAssessmentFunction(oneIDRequest.getIds());

        return Result.ok(functionalDomainRepresentations);
    }
}
