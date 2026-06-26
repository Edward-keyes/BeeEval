package com.xailab.vehicle.feign.pojo.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: FunctionTreeDataSyncCaseInfoDto
 * @Description:
 * @author: liulin
 * @date: 2025/6/13 0:13
 */
@Data
public class FunctionTreeDataSyncCaseInfoDto implements Serializable {

    /**
     * 对应用例id
     */
    private Integer id;

    /**
     * pcafe测试状态id
     */
    private Integer testStateId;

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
    private String functionEvaluation;

    /**
     * 文件素材
     */
    private List<CaseFileMaterial> caseFileMaterial;


    /**
     * 测试文件素材用例评级 不同步字段
     * na 缺数据
     * poor 没有素材
     * good 正常上传
     * show 首页展示
     * @see  FunctionTreeTestCaseRateStateEnum
     */
    private String testCaseRate;

    /**
     * 同步选项数据
     * 通过-拼接
     */
    private String syncOption;


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

    }

}
