package com.xailab.vehicle.feign.pojo.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: FunctionTreeDataSyncInfoJournal
 * @Description:
 * @author: liulin
 * @date: 2025/7/6 16:49
 */
@Data
public class FunctionTreeDataSyncInfoJournal implements Serializable {
    /**
     * pcafe 对应functionTag
     */
    private String functionTag;


    /**
     * beeeval 对应tagNubmer;
     */
    private String tagNumber;


    /**
     * 功能评价状态 na/modest/avg/good 修改之前的值
     * 对应   1:效果最佳,2:有,3:不及预期,4:无
     */
    private Integer functionEvaluateOriginal;

    /**
     * 功能评价状态 na/modest/avg/good
     * 对应   1:效果最佳,2:有,3:不及预期,4:无
     */
    private Integer functionEvaluate;

    /**
     * 同步状态码 200为成功
     */
    private Integer code;

    /**
     * 同步状态码信息
     */
    private String message;

    /**
     * 同步操作类型
     * Add / Update
     */
    private String operationType;

    /**
     * 同步选项数据 拼接之前的值
     * 通过-拼接
     */
    private String syncOptionOriginal;

    /**
     * 同步选项数据
     * 通过-拼接
     */
    private String syncOption;



    /**
     * 功能树用例数据
     */
    private List<FunctionTreeDataSyncCaseInfoJournal> functionCaseData;

}
