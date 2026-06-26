package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Problem implements Serializable {

    private Integer score;

    private String question;

}