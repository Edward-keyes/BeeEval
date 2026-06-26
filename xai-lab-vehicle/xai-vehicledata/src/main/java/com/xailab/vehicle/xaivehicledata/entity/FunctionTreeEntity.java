package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * 
 * @email d2460687074@gmail.com
 * @date 2025-01-15 10:30:59
 */
@Data
@TableName("vehicle_function_tree")
public class FunctionTreeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 车辆id
	 */
	private Long vehicleId;
	/**
	 * 功能树三级标签id
	 */
	private Long functionThreeTagId;
	/**
	 * 功能清单 (1:效果最佳,2:有,3:不及预期,4:无)
	 */
	private Integer functionList;
	/**
	 * 功能标注 (0:问题,1:亮点)
	 */
	private Integer labelExplain;
	/**
	 * 功能说明
	 */
	private String functionLabel;
	/**
	 * 视频编号
	 */
	private String videoNumber;
	/**
	 * 图片编号 多个
	 */
	private String pictureNumber;
	/**
	 * 视频字幕（拼接规则 vehicleId+视频编号+en）
	 * 此处只判断该视频是否有无字幕
	 */
	private Integer videoStr;
	/**
	 * 测试时间
	 */
	private Date testDate;
	/**
	 * 系统版本
	 */
	private String vehicleSystemVersion;

}
