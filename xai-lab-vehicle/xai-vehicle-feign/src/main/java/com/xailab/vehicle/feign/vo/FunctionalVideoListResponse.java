package com.xailab.vehicle.feign.vo;

import lombok.Data;

import java.util.List;

@Data
public class FunctionalVideoListResponse {
    /**
     * 高分表现功能域视频list
     */
    private List<FunctionalVideoVoF> highScoreFunctionalVideoList;

    /**
     * 高频问题功能域视频list
     */
    private List<FunctionalVideoVoF> highFrequencyFunctionalVideoList;
}
