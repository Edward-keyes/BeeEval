package com.xailab.vehicle.xaivehicledata.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xailab.vehicle.xaivehicledata.entity.DomainTagEntity;
import com.xailab.vehicle.xaivehicledata.service.DomainTagService;
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
@RequestMapping("vehicle/domaintag")
public class DomainTagController {
    @Autowired
    private DomainTagService domainTagService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = domainTagService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		DomainTagEntity domainTag = domainTagService.getById(id);

        return R.ok().put("domainTag", domainTag);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody DomainTagEntity domainTag){
		domainTagService.save(domainTag);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody DomainTagEntity domainTag){
		domainTagService.updateById(domainTag);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		domainTagService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
