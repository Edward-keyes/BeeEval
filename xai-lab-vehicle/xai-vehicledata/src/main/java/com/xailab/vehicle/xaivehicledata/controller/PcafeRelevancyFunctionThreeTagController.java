package com.xailab.vehicle.xaivehicledata.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xailab.vehicle.feign.vo.PcafeRelevancyFunctionThreeTagVo;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.R;
import com.xailab.vehicle.xaivehicledata.entity.FunctionThreeTagEntity;
import com.xailab.vehicle.xaivehicledata.service.FunctionThreeTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.xailab.vehicle.xaivehicledata.entity.PcafeRelevancyFunctionThreeTagEntity;
import com.xailab.vehicle.xaivehicledata.service.PcafeRelevancyFunctionThreeTagService;



/**
 * 
 *
 * @author caomei
 * @email d2460687074@gmail.com
 * @date 2025-05-26 09:43:10
 */
@RestController
@RequestMapping("xaivehicledata/pcaferelevancyfunctionthreetag")
public class PcafeRelevancyFunctionThreeTagController {
    @Autowired
    private PcafeRelevancyFunctionThreeTagService pcafeRelevancyFunctionThreeTagService;

    @Autowired
    private FunctionThreeTagService functionThreeTagService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pcafeRelevancyFunctionThreeTagService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PcafeRelevancyFunctionThreeTagEntity pcafeRelevancyFunctionThreeTag = pcafeRelevancyFunctionThreeTagService.getById(id);

        return R.ok().put("pcafeRelevancyFunctionThreeTag", pcafeRelevancyFunctionThreeTag);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PcafeRelevancyFunctionThreeTagEntity pcafeRelevancyFunctionThreeTag){
		pcafeRelevancyFunctionThreeTagService.save(pcafeRelevancyFunctionThreeTag);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PcafeRelevancyFunctionThreeTagEntity pcafeRelevancyFunctionThreeTag){
		pcafeRelevancyFunctionThreeTagService.updateById(pcafeRelevancyFunctionThreeTag);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		pcafeRelevancyFunctionThreeTagService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * （运）查询所有关联三级标签与功能ID
     * @return
     */
    @PostMapping("/queryList")
    List<PcafeRelevancyFunctionThreeTagVo> queryList(){
        List<PcafeRelevancyFunctionThreeTagEntity> list = pcafeRelevancyFunctionThreeTagService.list(Wrappers.<PcafeRelevancyFunctionThreeTagEntity>lambdaQuery()
                .orderByAsc(PcafeRelevancyFunctionThreeTagEntity::getBeeevalThreeNumber)
        );
        if (CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        List<FunctionThreeTagEntity> tagEntities = functionThreeTagService.list();
        Map<String, String> tagMap = tagEntities.stream().collect(Collectors.toMap(FunctionThreeTagEntity::getTagNumber,
                FunctionThreeTagEntity::getTagName));
        List<PcafeRelevancyFunctionThreeTagVo> vos = list.stream().map(it -> {
            PcafeRelevancyFunctionThreeTagVo vo = new PcafeRelevancyFunctionThreeTagVo();
            vo.setId(it.getId());
            vo.setBeeevalThreeNumber(it.getBeeevalThreeNumber());
            vo.setPecafeThreeNumber(it.getPecafeThreeNumber());
            vo.setBeeevalThreeName(tagMap.get(it.getBeeevalThreeNumber()));
            return vo;
        }).toList();
        return vos;
    }

}
