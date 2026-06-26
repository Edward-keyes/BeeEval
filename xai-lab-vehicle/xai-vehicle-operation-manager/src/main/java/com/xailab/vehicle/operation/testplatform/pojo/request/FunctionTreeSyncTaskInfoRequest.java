package com.xailab.vehicle.operation.testplatform.pojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
* 功能树同步任务详情表
*
* @author mm 
* @since 1.0.0 2025-06-02
*/
@Data
@Schema(description = "功能树同步任务详情表")
public class FunctionTreeSyncTaskInfoRequest implements Serializable {

	@Schema(description = "功能id")
	private String functionTag;

	@Schema(description = "目标功能id（当同步任务规则为自定义关联时需指定）")
	private String targetFunctionTag;

	@Schema(description = "选择的测试用例ids")
	private List<String> testCaseId;

//	@Schema(description = "数据类型：0-功能树三级数据，1-测试用例数据")
//	private Integer dataType;

}