package com.xailab.vehicle.xaivehicledata.controller;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.xailab.vehicle.feign.pojo.response.FunctionTreeListResponse;
import com.xailab.vehicle.feign.vo.FunctionTreeSynchronizationRequest;
import com.xailab.vehicle.feign.vo.FunctionTreeSynchronizationVoRequest;
import com.xailab.vehicle.xaivehicledata.entity.PcafeRelevancyFunctionThreeTagEntity;
import com.xailab.vehicle.xaivehicledata.entity.request.SortRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.FunctionTreeOpResponse;
import com.xailab.vehicle.xaivehicledata.entity.response.SynchronizationThreeTagResponse;
import com.xailab.vehicle.xaivehicledata.service.PcafeRelevancyFunctionThreeTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xailab.vehicle.xaivehicledata.entity.FunctionThreeTagEntity;
import com.xailab.vehicle.xaivehicledata.service.FunctionThreeTagService;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.R;



/**
 * @email d2460687074@gmail.com
 * @date 2025-01-15 10:30:59
 */
@RestController
@Slf4j
@RequestMapping("ware/functionthreetag")
public class FunctionThreeTagController {
    @Autowired
    private FunctionThreeTagService functionThreeTagService;

    @Autowired
    private PcafeRelevancyFunctionThreeTagService pcafeRelevancyFunctionThreeTagService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:functionthreetag:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = functionThreeTagService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:functionthreetag:info")
    public R info(@PathVariable("id") Long id){
		FunctionThreeTagEntity functionThreeTag = functionThreeTagService.getById(id);

        return R.ok().put("functionThreeTag", functionThreeTag);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:functionthreetag:save")
    public R save(@RequestBody FunctionThreeTagEntity functionThreeTag){
		functionThreeTagService.save(functionThreeTag);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:functionthreetag:update")
    public R update(@RequestBody FunctionThreeTagEntity functionThreeTag){
		functionThreeTagService.updateById(functionThreeTag);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:functionthreetag:delete")
    public R delete(@RequestBody Long[] ids){
		functionThreeTagService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * (运)获取一至三级所有标签信息
     */
    @PostMapping("/getFunctionTagList")
    public List<FunctionTreeOpResponse> getFunctionTagList(){

        return functionThreeTagService.getFunctionTagList();
    }

    /**
     * (运)同二级标签下，排序三级标签
     */
    @PostMapping("/sortThreeTag")
    public Integer sortThreeTag(@RequestBody SortRequest sortRequest) {

        return functionThreeTagService.sortThreeTag(sortRequest);
    }

    /**
     * (运)获取三级标签，对接同步用
     */
    @PostMapping("/getThreeTagList")
    public List<SynchronizationThreeTagResponse> getThreeTagListSynchronization() {

        return functionThreeTagService.getThreeTagListSynchronization();
    }

    /**
     * (运)基于三级标签编号 保存三级标签关联关系
     */
    @PostMapping("/saveList")
    public Boolean saveList(@RequestBody List<PcafeRelevancyFunctionThreeTagEntity> pcafeRelevancyFunctionThreeTagEntities){

        List<PcafeRelevancyFunctionThreeTagEntity> list = pcafeRelevancyFunctionThreeTagService.list();

        if (list.size() > 0) {

            Map<String, String> stringLongMap = extractPropertiesWithMerge(list);

            List<PcafeRelevancyFunctionThreeTagEntity> collect = new ArrayList<>();

            for (PcafeRelevancyFunctionThreeTagEntity entity : pcafeRelevancyFunctionThreeTagEntities) {

                String l = stringLongMap.get(entity.getPecafeThreeNumber());

                if (Objects.isNull(l)) {
                    collect.add(entity);
                }
            }
            return pcafeRelevancyFunctionThreeTagService.saveBatch(deduplicate(collect));
        }else {
            return pcafeRelevancyFunctionThreeTagService.saveBatch(deduplicate(pcafeRelevancyFunctionThreeTagEntities));
        }


    }

    public List<PcafeRelevancyFunctionThreeTagEntity> deduplicate(List<PcafeRelevancyFunctionThreeTagEntity> list) {
        return new ArrayList<>(list.stream()
                .collect(Collectors.toMap(
                        entity -> new AbstractMap.SimpleEntry<>(
                                entity.getBeeevalThreeNumber(),
                                entity.getPecafeThreeNumber()
                        ),
                        Function.identity(),
                        (existing, replacement) -> existing,  // 保留首次出现的元素
                        LinkedHashMap::new  // 保持原始顺序
                ))
                .values());
    }

    public Map<String, String> extractPropertiesWithMerge(
            List<PcafeRelevancyFunctionThreeTagEntity> entities) {

        return entities.stream()
                .collect(Collectors.toMap(
                        PcafeRelevancyFunctionThreeTagEntity::getPecafeThreeNumber,
                        PcafeRelevancyFunctionThreeTagEntity::getBeeevalThreeNumber,
                        (existing, replacement) -> {
                            // 添加日志记录重复键处理
                            log.warn("Duplicate key detected: {} with value {}. Keeping existing value {}",
                                    existing, replacement, existing);
//                            return existing; // 保留现有值
                             return replacement; // 使用新值
                        }
                ));
    }

    /**
     * 获取三级标签树
     */
    @PostMapping("/treeList")
    public  List<FunctionTreeListResponse> findFunctionTreeList() {

        return functionThreeTagService.findFunctionTreeList();
    }

    /**
     * （运）将三级标签与三级标签用例 数据同步至Beeeval
     */
    @PostMapping("/syncToBeeeval")
    public Boolean syncToBeeeval(FunctionTreeSynchronizationVoRequest request){

        return functionThreeTagService.syncToBeeeval(request);
    }
}
