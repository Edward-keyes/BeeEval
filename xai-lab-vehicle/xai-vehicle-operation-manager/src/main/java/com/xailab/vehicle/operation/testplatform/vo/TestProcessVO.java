package com.xailab.vehicle.operation.testplatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import com.xailab.vehicle.framework.common.utils.DateUtils;

/**
* 流程数据表
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Data
@Schema(description = "流程数据表")
public class TestProcessVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "流程id")
	private Integer id;

	@Schema(description = "一级")
	private String levelOneName;

	@Schema(description = "二级")
	private String levelTwoName;

	@Schema(description = "三级")
	private String levelThreeName;

	@Schema(description = "步骤顺序")
	private Integer step;

	@Schema(description = "流程题目")
	private String processTitle;

	@Schema(description = "流程描述")
	private String description;

	@Schema(description = "提示")
	private String tip;

	@Schema(description = "问题1")
	private String quesOne;

	@Schema(description = "问题2")
	private String quesTwo;

	@Schema(description = "问题3")
	private String quesThree;

	@Schema(description = "选项标题")
	private String optionsTitle;

	@Schema(description = "选项类型")
	private String optionsType;


}