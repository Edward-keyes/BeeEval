package com.xailab.vehicle.feign.pojo.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: FunctionTreeDataSyncCaseInfoJournal
 * @Description: 测试用例详情
 * @author: liulin
 * @date: 2025/7/6 16:53
 */
@Data
public class FunctionTreeDataSyncCaseInfoJournal implements Serializable {

    /**
     * 对应用例id
     */
    private Integer id;

    /**
     * pcafe测试状态id
     */
    private Integer testStateId;

    /**
     * 是否首页展示 修改之前的值
     */
    private Boolean isShowOriginal;

    /**
     * 是否首页展示
     */
    private Boolean isShow;


    /**
     * 功能评价（测试用例测试结果素材状态）
     *  0: na/1:modest/2:avg/3:good
     * value：na/avg/good/poor
     *  @see  FunctionTreeResultMeterialEnum
     */
    private String functionEvaluationOriginal;

    /**
     * 功能评价（测试用例测试结果素材状态）
     *  0: na/1:modest/2:avg/3:good
     * value：na/avg/good/poor
     *  @see  FunctionTreeResultMeterialEnum
     */
    private String functionEvaluation;


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
     * 文件素材
     */
    private List<FunctionTreeDataSyncCaseInfoJournal.CaseFileMaterial> caseFileMaterial;


    /**
     * 文件素材
     */
    @Data
    public static class CaseFileMaterial implements Serializable {
        /**
         * 文件素材url
         */
        private String materialUrl;
        /**
         * 文件素材类型
         *  0:image/1:video
         */
        private String materialType;

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

    }
}
