package com.xailab.vehicle.operation.testplatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import com.xailab.vehicle.framework.common.utils.DateUtils;

/**
* 功能评价选项表
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Data
@Schema(description = "功能评价选项表")
public class TestQuesOptionsVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	@Schema(description = "问题id")
	private Integer quesId;

	@Schema(description = "选项")
	private String options;

	@Schema(description = "排序")
	private Integer sort;


}