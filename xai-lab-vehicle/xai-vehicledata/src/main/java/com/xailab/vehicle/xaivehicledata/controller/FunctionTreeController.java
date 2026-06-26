package com.xailab.vehicle.xaivehicledata.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xailab.vehicle.feign.pojo.request.FunctionTreeDataSyncRequest;
import com.xailab.vehicle.feign.pojo.response.FunctionTreeTaskSyncJournalResponse;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.BaseInfoEntity;
import com.xailab.vehicle.xaivehicledata.entity.ThreeTagList;
import com.xailab.vehicle.xaivehicledata.entity.VehicleUserEntity;
import com.xailab.vehicle.xaivehicledata.entity.constant.VehicleConstant;
import com.xailab.vehicle.xaivehicledata.entity.request.*;
import com.xailab.vehicle.xaivehicledata.entity.response.*;
import com.xailab.vehicle.xaivehicledata.service.BaseInfoService;
import com.xailab.vehicle.xaivehicledata.service.VehicleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xailab.vehicle.xaivehicledata.entity.FunctionTreeEntity;
import com.xailab.vehicle.xaivehicledata.service.FunctionTreeService;
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
@RequestMapping("ware/functiontree")
public class FunctionTreeController {
    @Autowired
    private FunctionTreeService functionTreeService;

    @Autowired
    private BaseInfoService vehicleBaseInfoService;

    @Autowired
    private VehicleUserService vehicleUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:functiontree:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = functionTreeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:functiontree:info")
    public R info(@PathVariable("id") Long id){
		FunctionTreeEntity functionTree = functionTreeService.getById(id);

        return R.ok().put("functionTree", functionTree);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:functiontree:save")
    public R save(@RequestBody FunctionTreeEntity functionTree){
		functionTreeService.save(functionTree);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:functiontree:update")
    public R update(@RequestBody FunctionTreeEntity functionTree){
		functionTreeService.updateById(functionTree);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:functiontree:delete")
    public R delete(@RequestBody Long[] ids){
		functionTreeService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 基于功能树查询渗透率
     */
    @SaCheckLogin
    @PostMapping("/queryPenetrationRate")
    public Result<PenetrationRateResponse> queryPenetrationRate(@RequestBody IdRequest idRequest){

        long l = 0L;

        Object loginId = StpUtil.getTokenInfo().getLoginId();
        VehicleUserEntity user = vehicleUserService.getById(loginId+"");
        Boolean isTry = user.getStatus() == 1;

        if (isTry) {
            l= 2L;
        }else{
            l=vehicleBaseInfoService.list(new QueryWrapper<BaseInfoEntity>().eq("status",1)).size();
        }

        List<ThreeTagList> threeTagList = functionTreeService.queryPenetrationRateByFunctionTreeId(idRequest.getIds(),l,isTry,idRequest.getLanguage(),Long.parseLong(loginId+""));

        PenetrationRateResponse response = new PenetrationRateResponse();
        response.setPenetrationRateList(threeTagList);
        //TODO: 这里需要根据实际情况返回相应的车辆数量

        response.setVehicleCount(Math.toIntExact(l));

        return Result.ok(response);
    }

    /**
     * 基于车辆ID查询一级标签占比
     */
    @SaCheckLogin
    @PostMapping("/queryFirstLevelTagRatio")
    public Result<List<FirstLevelTagRatioResponse>> queryFirstLevelTagRatio(@RequestBody IdRequest idRequest){

        List<FirstLevelTagRatioResponse> firstLevelTagRatioList = functionTreeService.queryFirstLevelTagRatioByVehicleIds(idRequest.getIds(),idRequest.getLanguage());

        return Result.ok(firstLevelTagRatioList);
    }

    /**
     * 基于功能树与车辆ID查询车辆功能评级
     */
    @SaCheckLogin
    @PostMapping("/queryVehicleFunctionGrade")
    public Result<List<VehicleFunctionGradeResponse>> queryVehicleFunctionGrade(@RequestBody OneTagIdsRequest idRequest){

        return Result.ok(functionTreeService.queryVehicleFunctionGradeByFunctionTreeIdsAndVehicleIds(idRequest.getOneTagIds(),idRequest.getVehicleIds(),idRequest.getLanguage()));
    }

    /**
     * 根据三级场景id与车辆id查询当前车辆视频or图片
     */
    @PostMapping("/queryVideoOrPictureByThreeTagIdAndVehicleId")
    public Result<FileUrlResponse> queryVideoOrPictureByThreeTagIdAndVehicleId(@RequestBody ThreeTagIdsRequest idRequest){

        FileUrlResponse fileUrlResponse = functionTreeService.queryVideoOrPictureByThreeTagIdAndVehicleId(idRequest.getThreeTagIds(),idRequest.getVehicleIds(),idRequest.getLanguage());

        return Result.ok(fileUrlResponse);
    }

    /**
     * 根据车辆id查询功能丰富度各个场景的占比
     */
    @SaCheckLogin
    @PostMapping("/queryFunctionRichnessRatioByVehicleId")
    public Result<List<FunctionRichnessRatioRequest>> queryFunctionRichnessRatioByVehicleId(@RequestBody OneIDRequest idRequest){
        return Result.ok(functionTreeService.queryFunctionRichnessRatioByVehicleId(idRequest.getId(),idRequest.getLanguage()));
    }

    /**
     * 感知能力查询
     */
    @SaCheckLogin
    @PostMapping("/queryPerceptionAbilityByVehicleId")
    public Result<List<PerceptionAbilityRequest>> queryPerceptionAbilityByVehicleId(@RequestBody IdRequest idRequest){
        return Result.ok(functionTreeService.queryPerceptionAbilityByVehicleId(idRequest.getIds(),idRequest.getLanguage()));
    }


    /**
     * pcafe功能树数据同步至beeeval
     * @param request
     * @return
     */
    @PostMapping("/pcafeDataSync")
    public Result<FunctionTreeTaskSyncJournalResponse> pcafeDataSync(@RequestBody FunctionTreeDataSyncRequest request){
        return functionTreeService.pcafeDataSync(request);
    }


    /**
     * 功能树同任务回滚
     * @param request
     * @return
     */
    @PostMapping("/taskSyncFallback")
    public Result<Void> taskSyncFallback(@RequestBody FunctionTreeTaskSyncJournalResponse request){
        return functionTreeService.taskSyncFallback(request);
    }
}
