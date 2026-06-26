package com.xailab.vehicle.operation.testplatform.vo;

import com.xailab.vehicle.framework.common.utils.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
* 功能树一级标签类型
*
* @author mu 
* @since  2025-05-31
*/
@Data
@Schema(description = "功能树一级标签类型")
public class FunctionTreeFirstTypeVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "Primary Key")
	private Long id;

	@Schema(description = "标签")
	private String label;

	@Schema(description = "标签名称")
	private String labelName;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createTime;

	@Schema(description = "修改时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date updateTime;

	@Schema(description = "是否删除 0/1 否/是")
	private Integer deleted;


}