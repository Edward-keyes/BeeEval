package com.xailab.vehicle.operation.testplatform.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: TestPlatformTestCaseTreeVo
 * @Description: 三级树
 * @author: liulin
 * @date: 2025/4/18 0:02
 */
@Data
public class TestPlatformTestCaseTreeVo implements Serializable {
    /**
     * 当前名称
     */
    private String name;

    /**
     * 当前级别的孩子
     */
    private List<TestPlatformTestCaseTreeVo> child;

}
