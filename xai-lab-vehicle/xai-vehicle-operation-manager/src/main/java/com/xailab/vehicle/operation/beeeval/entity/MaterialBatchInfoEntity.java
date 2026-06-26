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
 * @date 2025-07-10 21:29:52
 */
@Data
@TableName("material_batch_info")
public class MaterialBatchInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 批上传任务名
	 */
	private String batchName;
	/**
	 * 批上传任务创建人id
	 */
	private Integer createUserId;
	/**
	 * 任务id
	 */
	private Integer recordId;
	/**
	 * 素材分类(1:功能树,2:测评)
	 */
	private Integer materialClassify;
	/**
	 * 执行时间
	 */
	private Date executionTime;
	/**
	 * 状态(0:等待同步,1:同步中,2:同步结束,-1:关闭同步)
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createDate;

}
