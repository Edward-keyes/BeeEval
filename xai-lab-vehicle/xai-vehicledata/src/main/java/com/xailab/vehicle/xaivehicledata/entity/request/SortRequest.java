package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

@Data
public class SortRequest {

    private String onlyTag;

    private Integer newSortValue;

    private Integer oldSortValue;

}
