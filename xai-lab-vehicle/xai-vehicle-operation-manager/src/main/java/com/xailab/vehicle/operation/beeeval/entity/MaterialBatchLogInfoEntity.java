package com.xailab.vehicle.operation.beeeval.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author caomei
 * @email d2460687074@gmail.com
 * @date 2025-07-10 17:14:20
 */
@Data
@TableName("material_batch_log_info")
public class MaterialBatchLogInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 批次号
	 */
	private Integer batchNumber;
	/**
	 * 素材名称
	 */
	private String materialName;
	/**
	 * 素材类型(1:图片素材,2:视频素材)
	 */
	private Integer materialType;
	/**
	 * 状态(1:上传成功,-1:上传失败,0:上传中,2:等待上传)
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createDate;

}
