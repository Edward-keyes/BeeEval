package com.xailab.vehicle.xaivehicledata.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptResult {

    private String text;

    private Double confidence;

    private List<Segment> segments;

    private String language;

    private Integer duration;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Segment {
        private Integer startTime;
        private Integer endTime;
        private String text;
        private Double confidence;
    }
}
