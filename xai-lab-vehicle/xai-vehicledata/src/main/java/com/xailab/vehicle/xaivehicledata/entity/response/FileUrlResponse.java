package com.xailab.vehicle.xaivehicledata.entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class FileUrlResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String videoUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> pictureUrl;

    private String type;

    private String videoId;

    private String description;

    private String[] functionLabel;

    private String srtUrl;

}
