package com.xailab.vehicle.xaivehicledata.entity.response;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;

    private Integer status;

    private String team;

}
