package com.xailab.vehicle.xaivehicledata.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.xailab.vehicle.feign.vo.DomainRelevancyVo;
import com.xailab.vehicle.feign.vo.FunctionDomainIndexVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xailab.vehicle.xaivehicledata.entity.FunctionalDomainEntity;
import com.xailab.vehicle.xaivehicledata.service.FunctionalDomainService;
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
@RequestMapping("vehicle/functionaldomain")
public class FunctionalDomainController {
    @Autowired
    private FunctionalDomainService functionalDomainService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = functionalDomainService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		FunctionalDomainEntity functionalDomain = functionalDomainService.getById(id);

        return R.ok().put("functionalDomain", functionalDomain);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody FunctionalDomainEntity functionalDomain){
		functionalDomainService.save(functionalDomain);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody FunctionalDomainEntity functionalDomain){
		functionalDomainService.updateById(functionalDomain);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		functionalDomainService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * （运）同步指标用例数据查询
     * @return
     */
    @PostMapping("/queryRelevancy")
    public List<DomainRelevancyVo> queryRelevancy(){
        List<DomainRelevancyVo> domainRelevancyVos = functionalDomainService.queryRelevancy();
        return domainRelevancyVos;
    }

    /**
     * 同步功能域指标数据查询
     * @return
     */
    @PostMapping("/queryRelevancyIndex")
    public List<FunctionDomainIndexVo> queryRelevancyIndex(){

        return functionalDomainService.queryRelevancyIndex();
    }
}
