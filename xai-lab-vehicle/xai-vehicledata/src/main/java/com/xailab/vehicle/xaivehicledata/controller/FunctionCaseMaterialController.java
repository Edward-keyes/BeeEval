package com.xailab.vehicle.xaivehicledata.controller;

import java.util.Arrays;
import java.util.Map;

import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.R;
import com.xailab.vehicle.xaivehicledata.service.FunctionCaseMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xailab.vehicle.xaivehicledata.entity.FunctionCaseMaterialEntity;


/**
 * 功能用例素材表
 *
 * @author caomei
 * @email d2460687074@gmail.com
 * @date 2025-06-08 01:22:23
 */
@RestController
@RequestMapping("ware/functioncasematerial")
public class FunctionCaseMaterialController {
    @Autowired
    private FunctionCaseMaterialService functionCaseMaterialService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = functionCaseMaterialService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		FunctionCaseMaterialEntity functionCaseMaterial = functionCaseMaterialService.getById(id);

        return R.ok().put("functionCaseMaterial", functionCaseMaterial);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody FunctionCaseMaterialEntity functionCaseMaterial){
		functionCaseMaterialService.save(functionCaseMaterial);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody FunctionCaseMaterialEntity functionCaseMaterial){
		functionCaseMaterialService.updateById(functionCaseMaterial);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		functionCaseMaterialService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
