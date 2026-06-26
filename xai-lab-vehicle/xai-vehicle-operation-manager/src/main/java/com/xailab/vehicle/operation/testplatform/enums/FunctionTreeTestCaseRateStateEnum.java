package com.xailab.vehicle.operation.testplatform.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: FunctionTreeCaseRateEnum
 * @Description:
 * @author: liulin
 * @date: 2025/5/3 2:45
 */
@AllArgsConstructor
@Getter
public enum FunctionTreeTestCaseRateStateEnum {
    /**
     * * 测试用例评级
     * 	 * 未验证/Avg/Good/Poor
     * 	 * na/avg/good/poor
     */
    NA("na","缺数据"),
    Poor("poor","没有素材"),
    Good("good","正常上传"),
    Show("show","首页展示"),

    ;
    private final String value;
    private final String describe;

    public static FunctionTreeTestCaseRateStateEnum paseEnum(String value){
        for (FunctionTreeTestCaseRateStateEnum functionTreeTestCaseRateStateEnum : values()) {
            if (functionTreeTestCaseRateStateEnum.getValue().equals(value)){
                return functionTreeTestCaseRateStateEnum;
            }
        }
        return null;
    }

    public boolean equals(String value){
        return this.value.equals(value);
    }

}
