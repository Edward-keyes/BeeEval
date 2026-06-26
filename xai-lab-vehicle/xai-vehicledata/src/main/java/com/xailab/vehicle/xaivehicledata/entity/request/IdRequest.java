package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

import java.util.List;

@Data
public class IdRequest {

    /**
     * id列表
     */
    private List<String> ids;

    private String language;

}
