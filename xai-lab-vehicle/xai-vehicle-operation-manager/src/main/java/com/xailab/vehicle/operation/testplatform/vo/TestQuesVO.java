package com.xailab.vehicle.operation.testplatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import com.xailab.vehicle.framework.common.utils.DateUtils;
import java.util.Date;

/**
* 功能评价数据表
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Data
@Schema(description = "功能评价数据表")
public class TestQuesVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	@Schema(description = "三级名称")
	private String testName;

	@Schema(description = "选项类型 0单选 1多选")
	private Integer optionsType;

	@Schema(description = "类型")
	private String categoryType;

	@Schema(description = "问题/维度")
	private String questionDimension;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createTime;


}