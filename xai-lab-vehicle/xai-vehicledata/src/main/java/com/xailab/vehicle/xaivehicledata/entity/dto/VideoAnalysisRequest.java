package com.xailab.vehicle.xaivehicledata.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoAnalysisRequest {

    private Long videoId;

    private String transcript;

    private List<String> framePaths;

    private String vehicleName;

    private String domainName;

    private String indexName;

    private String videoType;

    private String taskType;

    private String description;
}
