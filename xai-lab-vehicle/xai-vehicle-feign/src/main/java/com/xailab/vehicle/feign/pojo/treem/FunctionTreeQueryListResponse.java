package com.xailab.vehicle.feign.pojo.treem;

import com.xailab.vehicle.feign.pojo.response.FunctionTreeCaseInfoResponse;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: FunctionTreeListResponse
 * @Description:
 * @author: liulin
 * @date: 2025/6/8 1:24
 */
@Data
public class FunctionTreeQueryListResponse implements Serializable {

    /**
     * 标签编号
     */
    private String tagNumber;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 标签名称英文
     */
    private String tagNameEn;

    /**
     * 说明
     */
    private String description;

    /**
     * 说明英文
     */
    private String descriptionEn;


    /**
     * 功能评价
     * 功能清单(1:效果最佳,2:有,3:不及预期,4:无)
     */
    private Integer functionList;


    /**
     * 功能树用例
     */
    private List<FunctionTreeCaseResponse> functionTreeCase;

}
