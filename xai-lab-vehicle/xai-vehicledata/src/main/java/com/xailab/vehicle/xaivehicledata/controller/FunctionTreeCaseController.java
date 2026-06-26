package com.xailab.vehicle.xaivehicledata.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xailab.vehicle.feign.vo.FunctionTreeCaseFeignVo;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.R;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.service.FunctionTreeCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xailab.vehicle.xaivehicledata.entity.FunctionTreeCaseEntity;



/**
 * 功能树用例表
 *
 * @author caomei
 * @email d2460687074@gmail.com
 * @date 2025-06-08 01:22:23
 */
@RestController
@RequestMapping("ware/functiontreecase")
public class FunctionTreeCaseController {
    @Autowired
    private FunctionTreeCaseService functionTreeCaseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = functionTreeCaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		FunctionTreeCaseEntity functionTreeCase = functionTreeCaseService.getById(id);

        return R.ok().put("functionTreeCase", functionTreeCase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody FunctionTreeCaseEntity functionTreeCase){
		functionTreeCaseService.save(functionTreeCase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody FunctionTreeCaseEntity functionTreeCase){
		functionTreeCaseService.updateById(functionTreeCase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		functionTreeCaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 基于threeId查询列表
     */
    @PostMapping("/queryList")
    public List<FunctionTreeCaseEntity> queryList(@RequestBody String threeId){

        List<FunctionTreeCaseEntity> list =
                functionTreeCaseService.list(
                        new QueryWrapper<FunctionTreeCaseEntity>()
                                .eq("three_tag_id", threeId));

        return list;
    }

    /**
     * 批量保存
     */
    @PostMapping("/saveBatch")
    public Boolean saveBatch(@RequestBody List<FunctionTreeCaseFeignVo> list){

        List<FunctionTreeCaseEntity> functionTreeCaseEntityList = new ArrayList<>();

        for (FunctionTreeCaseFeignVo functionTreeCaseFeignVo : list) {

            FunctionTreeCaseEntity functionTreeCaseEntity = new FunctionTreeCaseEntity();

            functionTreeCaseEntity.setCaseContent(functionTreeCaseFeignVo.getCaseContent());

            functionTreeCaseEntity.setId(functionTreeCaseFeignVo.getId());

            functionTreeCaseEntity.setThreeTagId(functionTreeCaseFeignVo.getThreeTagId());

            functionTreeCaseEntity.setUpdateTime(functionTreeCaseFeignVo.getUpdateTime());

            functionTreeCaseEntity.setCreateTime(functionTreeCaseFeignVo.getCreateTime());

            functionTreeCaseEntityList.add(functionTreeCaseEntity);
        }

        return functionTreeCaseService.saveBatch(functionTreeCaseEntityList);

    }

}
