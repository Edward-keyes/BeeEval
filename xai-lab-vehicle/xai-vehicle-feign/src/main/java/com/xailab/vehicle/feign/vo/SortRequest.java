package com.xailab.vehicle.feign.vo;

import lombok.Data;

@Data
public class SortRequest {

    private String onlyTag;

    private Integer newSortValue;

    private Integer oldSortValue;

}
