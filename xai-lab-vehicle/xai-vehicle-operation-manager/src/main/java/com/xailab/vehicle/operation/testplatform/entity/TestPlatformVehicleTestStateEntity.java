package com.xailab.vehicle.operation.testplatform.entity;

import com.xailab.vehicle.operation.testplatform.enums.FunctionTreeTestCaseRateStateEnum;
import com.xailab.vehicle.operation.testplatform.enums.FunctionTreeResultMeterialEnum;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

/**
 * 测试记录用例表
 *
 * @author mumu 
 * @since 1.0.0 2025-04-16
 */

@Data
@TableName("test_state")
public class TestPlatformVehicleTestStateEntity {
	@TableId
	private Integer id;

	/**
	* 测试状态 未测试：0；已测试：1
	*/
	private Integer testStatus;

	/**
	* 测试记录id
	*/
	private Integer recordId;

	/**
	* 测试用例id
	*/
	private Integer testcaseId;

	/**
	* 任务是否成功 初始：-1； 失败：0； 成功：1
	*/
	private Integer isSuccessful;

	/**
	* 打分
	*/
	private Integer score;

	private Integer hasBug;

	/**
	* 创建时间
	*/
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	* 音频路径
	*/
	private String audioPath;

	/**
	* 错误类型
	*/
	private Integer errorType;

	/**
	* 错误详情
	*/
	private String errorDetail;

	/**
	* case类型
	*/
	private Integer caseType;

	/**
	 * 视频名称
	 */
	private String videoName;

	/**
	 * 测试文件素材用例评级
	 * na 缺数据
	 * poor 没有素材
	 * good 正常上传
	 * show 首页展示
	 * @see  FunctionTreeTestCaseRateStateEnum
	 */
	private String testCaseRate;

	/**
	 * 测试用例测试结果素材状态
	 * describe：未验证/Avg/Good/Poor
	 * value：na/avg/good/poor
	 *  @see  FunctionTreeResultMeterialEnum
	 */
	private String materialState;

}