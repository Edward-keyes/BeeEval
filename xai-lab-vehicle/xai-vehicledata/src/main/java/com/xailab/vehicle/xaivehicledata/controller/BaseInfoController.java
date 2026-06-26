package com.xailab.vehicle.xaivehicledata.controller;

import java.util.*;

import cn.dev33.satoken.stp.StpUtil;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.VehicleTryUserEntity;
import com.xailab.vehicle.xaivehicledata.entity.VehicleUserEntity;
import com.xailab.vehicle.xaivehicledata.entity.constant.VehicleConstant;
import com.xailab.vehicle.xaivehicledata.entity.request.AddVehicleInfoRequest;
import com.xailab.vehicle.xaivehicledata.entity.request.LanguageRequest;
import com.xailab.vehicle.xaivehicledata.entity.request.OneIDRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.VehicleInfoOpResponse;
import com.xailab.vehicle.xaivehicledata.entity.vo.VehicleDetailInfoVo;
import com.xailab.vehicle.xaivehicledata.entity.vo.VehicleInfoVo;
import com.xailab.vehicle.xaivehicledata.service.VehicleTryUserService;
import com.xailab.vehicle.xaivehicledata.service.VehicleUserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.xailab.vehicle.xaivehicledata.entity.BaseInfoEntity;
import com.xailab.vehicle.xaivehicledata.service.BaseInfoService;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.R;
import org.springframework.web.multipart.MultipartFile;


/**
 * 
 *
 * 
 * @email d2460687074@gmail.com
 * @date 2025-01-15 10:30:59
 */
@RestController
@RequestMapping("ware/baseinfo")
public class BaseInfoController {
    @Autowired
    private BaseInfoService baseInfoService;

    @Resource
    private VehicleUserService vehicleUserService;

    @Resource
    private VehicleTryUserService vehicleTryUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:baseinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = baseInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:baseinfo:info")
    public R info(@PathVariable("id") Long id){
		BaseInfoEntity baseInfo = baseInfoService.getById(id);

        return R.ok().put("baseInfo", baseInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:baseinfo:save")
    public R save(@RequestBody BaseInfoEntity baseInfo){
		baseInfoService.save(baseInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:baseinfo:update")
    public R update(@RequestBody BaseInfoEntity baseInfo){
		baseInfoService.updateById(baseInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:baseinfo:delete")
    public R delete(@RequestBody Long[] ids){
		baseInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 所有车辆查询
     */
    @RequestMapping("/allVehicle")
    public Result<List<VehicleInfoVo>> queryAllVehicle(@RequestBody LanguageRequest languageRequest) {
        List<VehicleInfoVo> list=baseInfoService.queryAllVehicle(languageRequest.getLanguage());
        VehicleConstant constant=new VehicleConstant();
        Object loginId = StpUtil.getTokenInfo().getLoginId();

        if (Objects.nonNull(loginId)) {
            VehicleUserEntity user = vehicleUserService.getById(loginId+"");

            if (user.getStatus() == 1){

                Map<String,Long> tryMap = new HashMap<>();
                List<VehicleTryUserEntity> vehicleTryUserEntityList = vehicleTryUserService.getTryUserListByUserId(Long.parseLong(loginId + ""));
                if (!vehicleTryUserEntityList.isEmpty()) {
                    vehicleTryUserEntityList.stream().forEach(vehicleTryUserEntity -> {
                        tryMap.put(vehicleTryUserEntity.getVehicleId()+"",vehicleTryUserEntity.getUserId());
                    });
                    for (VehicleInfoVo vehicleDetailInfoVo : list) {
                        Long l = tryMap.get(vehicleDetailInfoVo.getId());
                        if (l!=null) {
                            vehicleDetailInfoVo.setStatus(3);
                        }
                    }
                }else {
                    // 遍历列表，检查id是否为"438735847190167560"或"448078678031597629"
                    for (VehicleInfoVo vehicleDetailInfoVo : list) {
                        if (constant.getVehicle1Id().equals(vehicleDetailInfoVo.getId()) || constant.getVehicle2Id().equals(vehicleDetailInfoVo.getId()) || constant.getVehicle3Id().equals(vehicleDetailInfoVo.getId())) {
                            // 如果id为"448078678031597629"或"438735847190167560"，将status设置为3
                            vehicleDetailInfoVo.setStatus(3);
                        }
                    }
                }
            }
        }

        // 自定义排序逻辑
        Collections.sort(list, new Comparator<VehicleInfoVo>() {
            @Override
            public int compare(VehicleInfoVo o1, VehicleInfoVo o2) {
                // 先按status排序
                if (o1.getStatus() != o2.getStatus()) {
                    if (o1.getStatus() == 3) return -1; // status为3的排在前面
                    if (o2.getStatus() == 3) return 1;
                    if (o1.getStatus() == 1) return -1; // status为1的排在status为2的前面
                    return 1; // status为2的排在最后
                }
                // 如果status相同，按id升序排序
                return o1.getId().compareTo(o2.getId());
            }
        });

        return Result.ok(list);
    }

    /**
     * 所有车辆详细信息查询
     */
    @RequestMapping("/allVehicleInfo")
    public Result<List<VehicleDetailInfoVo>> allVehicleInfo(@RequestBody LanguageRequest language) {

        System.out.println("language: " + language.getLanguage());
        List<VehicleDetailInfoVo> list=baseInfoService.queryAllVehicleDetailInfo(language.getLanguage());
        VehicleConstant constant=new VehicleConstant();
        Object loginId = StpUtil.getTokenInfo().getLoginId();

        if (Objects.nonNull(loginId)) {
            VehicleUserEntity user = vehicleUserService.getById(loginId+"");

            if (user.getStatus() == 1){
                Map<String,Long> tryMap = new HashMap<>();
                List<VehicleTryUserEntity> vehicleTryUserEntityList = vehicleTryUserService.getTryUserListByUserId(Long.parseLong(loginId + ""));
                if (!vehicleTryUserEntityList.isEmpty()) {
                    vehicleTryUserEntityList.stream().forEach(vehicleTryUserEntity -> {
                        tryMap.put(vehicleTryUserEntity.getVehicleId()+"",vehicleTryUserEntity.getUserId());
                    });
                    for (VehicleDetailInfoVo vehicleDetailInfoVo : list) {
                        Long l = tryMap.get(vehicleDetailInfoVo.getId());
                        if (l!=null) {
                            vehicleDetailInfoVo.setStatus(3);
                        }
                    }
                }else {
                // 遍历列表，检查id是否为"438735847190167560"或"448078678031597629"
                for (VehicleDetailInfoVo vehicleDetailInfoVo : list) {
                    if (constant.getVehicle3Id().equals(vehicleDetailInfoVo.getId()) || constant.getVehicle1Id().equals(vehicleDetailInfoVo.getId()) || constant.getVehicle2Id().equals(vehicleDetailInfoVo.getId())) {
                        // 如果id为"448078678031597629"或"438735847190167560"，将status设置为3
                        vehicleDetailInfoVo.setStatus(3);
                    }
                }
}
            }
        }

        // 自定义排序逻辑
        Collections.sort(list, new Comparator<VehicleDetailInfoVo>() {
            @Override
            public int compare(VehicleDetailInfoVo o1, VehicleDetailInfoVo o2) {
                // 先按status排序
                if (o1.getStatus() != o2.getStatus()) {
                    if (o1.getStatus() == 3) return -1; // status为3的排在前面
                    if (o2.getStatus() == 3) return 1;
                    if (o1.getStatus() == 1) return -1; // status为1的排在status为2的前面
                    return 1; // status为2的排在最后
                }
                // 如果status相同，按id升序排序
                return o1.getId().compareTo(o2.getId());
            }
        });

        // 定义优先级列表
        Set<String> priorityIds = new HashSet<>(Arrays.asList("1955540431761186818","1953353442240634881", "448078678031597645"));

        // 自定义排序规则
        list.sort((v1, v2) -> {
            boolean v1Priority = priorityIds.contains(v1.getId());
            boolean v2Priority = priorityIds.contains(v2.getId());

            if (v1Priority && !v2Priority) {
                return -1; // v1 优先级高，排在前面
            } else if (!v1Priority && v2Priority) {
                return 1; // v2 优先级高，排在前面
            } else {
                return 0; // 优先级相同，保持原顺序
            }
        });

        return Result.ok(list);
    }

    /**
     * 根据车辆id查询车辆详细信息
     */
    @PostMapping("/vehicleInfoByVehicleId")
    public Result<VehicleDetailInfoVo> vehicleInfoByVehicleId(@RequestBody OneIDRequest oneIDRequest) {
        VehicleDetailInfoVo list=baseInfoService.queryVehicleInfoByVehicleId(oneIDRequest.getId(),oneIDRequest.getLanguage());
        return Result.ok(list);
    }
}
