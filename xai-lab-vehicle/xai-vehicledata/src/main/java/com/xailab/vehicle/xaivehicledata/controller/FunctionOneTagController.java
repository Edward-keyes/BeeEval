package com.xailab.vehicle.xaivehicledata.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.BaseInfoEntity;
import com.xailab.vehicle.xaivehicledata.entity.request.*;
import com.xailab.vehicle.xaivehicledata.entity.response.FunctionTreeOneAndTwoTagResponse;
import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionOneTagVo;
import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionThreeTagVo;
import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionalDomainVo;
import com.xailab.vehicle.xaivehicledata.service.BaseInfoService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xailab.vehicle.xaivehicledata.entity.FunctionOneTagEntity;
import com.xailab.vehicle.xaivehicledata.service.FunctionOneTagService;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.R;



/**
 * 
 *
 *
 * @email d2460687074@gmail.com
 * @date 2025-01-15 10:30:59
 */
@RestController
@RequestMapping("ware/functiononetag")
public class FunctionOneTagController {

    @Resource
    private BaseInfoService baseInfoService;

    @Autowired
    private FunctionOneTagService functionOneTagService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:functiononetag:list")
    public Result<PageUtils> list(@RequestParam Map<String, Object> params){
        PageUtils page = functionOneTagService.queryPage(params);

        return Result.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:functiononetag:info")
    public R info(@PathVariable("id") Long id){
		FunctionOneTagEntity functionOneTag = functionOneTagService.getById(id);

        return R.ok().put("functionOneTag", functionOneTag);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:functiononetag:save")
    public R save(@RequestBody FunctionOneTagEntity functionOneTag){
		functionOneTagService.save(functionOneTag);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:functiononetag:update")
    public R update(@RequestBody FunctionOneTagEntity functionOneTag){
		functionOneTagService.updateById(functionOneTag);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:functiononetag:delete")
    public R delete(@RequestBody Long[] ids){
		functionOneTagService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     *
     * 获取一级三级标签
     * @return
     */
    @PostMapping("/queryAllOneTag")
    public Result<List<FunctionDomainResultVo>> queryAllOneTag(@RequestBody LanguageRequest request) {
        List<FunctionDomainResultVo> functionalDomain = functionOneTagService.queryAllOneTagCountThreeTag(request.getLanguage());
        return Result.ok(functionalDomain);
    }

    /**
     * 获取所有亮点功能推荐
     * @return
     */
    @PostMapping("/queryAllHighlightFunction")
    public Result<List<HighlightFunctionRequest>> queryAllHighlightFunction(@RequestBody LanguageRequest languageRequest) {
        List<HighlightFunctionRequest> functionalDomain = functionOneTagService.queryAllHighlightFunction(languageRequest.getLanguage());



        return Result.ok(functionalDomain);
    }

    /**
     * 查询所有一二三级功能树
     * @return
     */
    @SaCheckLogin
    @PostMapping("/queryAllFunctionTagTree")
    public Result<List<FunctionOneTagVo>> queryAllFunctionTagTree(@RequestBody LanguageRequest languageRequest) {
        List<FunctionOneTagVo> functionTagTree = functionOneTagService.queryAllFunctionTagTree(languageRequest.getLanguage());
        return Result.ok(functionTagTree);
    }

    /**
     * 根据三级id与车辆id查询此车的视频与介绍
     * @param functionTreeVideoRequest
     * @return
     */
    @SaCheckLogin
    @PostMapping("/queryVideoByThreeTagIdAndVehicleId")
    public Result queryAllFunctionThreeTagTree(@RequestBody QueryFunctionTreeVideoRequest functionTreeVideoRequest) {

        BaseInfoEntity one = baseInfoService.getOne(Wrappers.<BaseInfoEntity>lambdaQuery().eq(BaseInfoEntity::getId, functionTreeVideoRequest.getVehicleId()));

        if (one.getVehicleType()!=1) {

            FunctionTreeVideoNewRequest functionTreeVideoNewRequest = functionOneTagService.queryVideoByThreeTagIdAndVehicleIdNew(functionTreeVideoRequest);

            return Result.ok(functionTreeVideoNewRequest);
        }else {

            FunctionTreeVideoRequest functionTreeVideoRequests = functionOneTagService.queryVideoByThreeTagIdAndVehicleId(functionTreeVideoRequest);

            return Result.ok(functionTreeVideoRequests);
        }
    }

    /**
     * 根据三级id查询其他车是否有此视频
     */
    @SaCheckLogin
    @PostMapping("/queryOtherVideoByThreeTagId")
    public Result<List<FunctionTreeCompareRequest>> queryOtherVideoByThreeTagId(@RequestBody QueryFunctionTreeVideoRequest threeIdRequest) {
        List<FunctionTreeCompareRequest> functionTreeVideoRequests = functionOneTagService.queryOtherVideoByThreeTagId(threeIdRequest.getThreeTagId(), threeIdRequest.getVehicleId(),threeIdRequest.getLanguage());

        return Result.ok(functionTreeVideoRequests);
    }

    /**
     * (运) 获取一二级所有标签
     */
    @PostMapping("/getAllOneAndTwoTag")
    public List<FunctionTreeOneAndTwoTagResponse> getAllOneAndTwoTag() {

        return functionOneTagService.getAllOneAndTwoTag();
    }

}
