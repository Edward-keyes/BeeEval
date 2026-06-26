package com.xailab.vehicle.operation.testplatform.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FunctionTreeDataStateEnum {

    /**
     * 数据状态 0/1/2 缺数据/可同步/待审核
     */
    LACK_OF_DATA(0,"缺数据"),
    SYNCHRONIZATION(1,"可同步"),
    PENDING_REVIEW(2,"待审核"),
    ONLINE(3,"已上线"),
    //被驳回
    REJECTED(4,"被驳回"),

    ;
    private final Integer value;
    private final String describe;

    /**
     * 判断
     * @param value
     * @return
     */
    public static FunctionTreeDataStateEnum paseEnum(Integer value){
        for (FunctionTreeDataStateEnum stateEnum : values()) {
            if (stateEnum.equals(value)){
                return stateEnum;
            }
        }
        return null;
    }

    /**
     * eqals
     * @param value
     * @return
     */
    public boolean equals(Integer value){
        return this.value.equals(value);
    }
}
