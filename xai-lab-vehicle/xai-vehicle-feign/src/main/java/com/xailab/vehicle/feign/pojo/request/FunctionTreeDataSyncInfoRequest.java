package com.xailab.vehicle.feign.pojo.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: FunctionTreeDataSyncInfoRequest
 * @Description:
 * @author: liulin
 * @date: 2025/6/13 0:05
 */
@Data
public class FunctionTreeDataSyncInfoRequest implements Serializable {

    /**
     * pcafe 对应functionTag
     */
    private String functionTag;


    /**
     * beeeval 对应tagNubmer;
     */
    private String tagNumber;

    /**
     * 功能评价状态 na/modest/avg/good
     * 对应   1:效果最佳,2:有,3:不及预期,4:无
     */
    private Integer functionEvaluate;

    /**
     * 同步选项数据
     * 通过-拼接
     */
    private String syncOption;


    /**
     * 功能树用例数据
     */
    private List<FunctionTreeDataSyncCaseInfoDto> functionCaseData;




}
