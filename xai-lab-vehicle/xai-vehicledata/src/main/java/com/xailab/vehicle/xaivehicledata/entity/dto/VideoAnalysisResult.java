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
public class VideoAnalysisResult {

    private Long videoId;

    private Double overallScore;

    private Map<String, DimensionScore> dimensions;

    private String summary;

    private List<String> improvementSuggestions;

    private List<String> keyObservations;

    private String transcript;

    private String visualDescription;

    private Integer processingTimeMs;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DimensionScore {
        private Double score;
        private String comment;
    }
}
