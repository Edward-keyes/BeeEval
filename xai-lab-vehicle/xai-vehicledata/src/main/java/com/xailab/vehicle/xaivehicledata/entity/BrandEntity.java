package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("vehicle_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 车辆品牌
	 */
	private String brand;
	/**
	 * 车辆品牌en
	 */
	private String brandEn;
	/**
	 * 车标路径
	 */
	private String brandPicturePath;

}
