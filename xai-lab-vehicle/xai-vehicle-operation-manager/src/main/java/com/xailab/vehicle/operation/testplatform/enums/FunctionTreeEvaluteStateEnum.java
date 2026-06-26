package com.xailab.vehicle.operation.testplatform.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FunctionTreeEvaluteStateEnum {

    /**
     * 功能评价状态 na/modest/avg/good
     * 1:效果最佳,2:有,3:不及预期,4:无
     */
    UN("un",-1,"未验证"),
    NA("na",4,"n.a"),
    MODEST("modest",3,"Modest"),
    Avg("avg",2,"Avg"),
    Good("good",1,"Good"),

    ;
    private final String value;
    private final Integer code;
    private final String describe;

    /**
     * 判断
     * @param value
     * @return
     */
    public static FunctionTreeEvaluteStateEnum paseEnum(String value){
        for (FunctionTreeEvaluteStateEnum evaluteStateEnum : values()) {
            if (evaluteStateEnum.equals(value)){
                return evaluteStateEnum;
            }
        }
        return null;
    }

    /**
     * eqals
     * @param value
     * @return
     */
    public boolean equals(String value){
        return this.value.equals(value);
    }
}
