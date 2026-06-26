package com.xailab.vehicle.xaivehicledata.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xailab.vehicle.xaivehicledata.entity.FunctionTwoTagEntity;
import com.xailab.vehicle.xaivehicledata.service.FunctionTwoTagService;
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
@RequestMapping("ware/functiontwotag")
public class FunctionTwoTagController {
    @Autowired
    private FunctionTwoTagService functionTwoTagService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:functiontwotag:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = functionTwoTagService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:functiontwotag:info")
    public R info(@PathVariable("id") Long id){
		FunctionTwoTagEntity functionTwoTag = functionTwoTagService.getById(id);

        return R.ok().put("functionTwoTag", functionTwoTag);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:functiontwotag:save")
    public R save(@RequestBody FunctionTwoTagEntity functionTwoTag){
		functionTwoTagService.save(functionTwoTag);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:functiontwotag:update")
    public R update(@RequestBody FunctionTwoTagEntity functionTwoTag){
		functionTwoTagService.updateById(functionTwoTag);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:functiontwotag:delete")
    public R delete(@RequestBody Long[] ids){
		functionTwoTagService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
