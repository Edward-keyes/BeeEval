package com.xailab.vehicle.operation.testplatform.pojo.response;

import com.xailab.vehicle.operation.testplatform.enums.FunctionTreeTestCaseRateStateEnum;
import com.xailab.vehicle.operation.testplatform.enums.FunctionTreeResultMeterialEnum;
import com.xailab.vehicle.operation.testplatform.vo.TestCaseMaterialVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: TestStateInfoResponse
 * @Description:
 * @author: liulin
 * @date: 2025/5/30 9:21
 */
@Data
public class TestStateInfoResponse implements Serializable {
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

    /**
     * 测试用例素材列表
     */
    private List<TestCaseMaterialVo> materialVos;

}
