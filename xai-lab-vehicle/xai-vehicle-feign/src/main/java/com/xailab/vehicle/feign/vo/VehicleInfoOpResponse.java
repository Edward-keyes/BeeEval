package com.xailab.vehicle.feign.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class VehicleInfoOpResponse {
    private String id;

    private String brand;

    private String brandEn;

    private String vehicleModel;

    private String vehicleVersion;

    private String vehicleVersionEn;

    private String vehiclePicture;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date timeToMarket;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date testDate;

    private String vehicleSystemVersion;

    private String modelName;

    private String energyType;

    private String energyTypeEn;

    private String enduranceMileage;

    private String chipInformation;

    private String chipInformationEn;

    private Integer status;
}
