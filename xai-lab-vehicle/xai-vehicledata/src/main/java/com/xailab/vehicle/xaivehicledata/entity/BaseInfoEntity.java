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
@TableName("vehicle_base_info")
public class BaseInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 品牌id
	 */
	private Long brandId;
	/**
	 * 车型
	 */
	private String vehicleModel;
	/**
	 * 汽车版本
	 */
	private String vehicleVersion;
	/**
	 * 汽车版本
	 */
	private String vehicleVersionEn;
	/**
	 * 车辆图片
	 */
	private String vehiclePicture;
	/**
	 * 上市时间
	 */
	private Date timeToMarket;
	/**
	 * 测试时间
	 */
	private Date testDate;
	/**
	 * 系统版本
	 */
	private String vehicleSystemVersion;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 修改时间
	 */
	private Date updateDate;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 是否删除
	 */
	private Integer isDelete;
	/**
	 * 能源类型
	 */
	private String energyType;
	/**
	 * 能源类型
	 */
	private String energyTypeEn;
	/**
	 * 公里数
	 */
	private String enduranceMileage;
	/**
     * 模型名称
     */
    private String modelName;
	/**
	 * 芯片信息
	 */
	private String chipInformation;
	/**
	 * 芯片信息En
	 */
	private String chipInformationEn;
	/**
	 * 车辆数据类型 1为旧数据格式车型
	 */
	private Integer vehicleType;
}
