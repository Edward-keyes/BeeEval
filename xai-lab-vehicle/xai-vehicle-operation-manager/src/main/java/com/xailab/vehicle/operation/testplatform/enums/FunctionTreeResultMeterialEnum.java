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
public enum FunctionTreeResultMeterialEnum {
    /**
     * * 测试用例评级
     * 	 * 未测试/不具备/未验证/Avg/Good/Poor
     * 	 * na/avg/good/poor
     */
    NO_TEST("nt",-1,"未测试"),
    NA("na",0,"未验证"),
    Poor("poor",1,"Poor"),
    Avg("avg",2,"Avg"),
    Good("good",3,"Good"),
    NOT_AVAILABLE("nta",4,"不具备"),

    ;
    private final String value;
    private final Integer code;
    private final String describe;


    public static FunctionTreeResultMeterialEnum paseEnumDefaultValue(String value){
        for (FunctionTreeResultMeterialEnum resultMeterialEnum : values()) {
            if (resultMeterialEnum.getValue().equals(value)){
                return resultMeterialEnum;
            }
        }
        return FunctionTreeResultMeterialEnum.NA;
    }

    public static FunctionTreeResultMeterialEnum paseEnum(String value){
        for (FunctionTreeResultMeterialEnum resultMeterialEnum : values()) {
            if (resultMeterialEnum.getValue().equals(value)){
                return resultMeterialEnum;
            }
        }
        return null;
    }

}
