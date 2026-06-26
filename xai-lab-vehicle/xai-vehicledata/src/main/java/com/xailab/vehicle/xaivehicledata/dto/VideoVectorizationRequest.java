package com.xailab.vehicle.xaivehicledata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 视频向量化处理请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoVectorizationRequest {

    private List<Long> videoIds;

    private Boolean forceUpdate;

    private Boolean includeTranscript;

    private Boolean includeKeyframes;

    public static VideoVectorizationRequest all() {
        return VideoVectorizationRequest.builder()
                .videoIds(null)
                .forceUpdate(false)
                .includeTranscript(true)
                .includeKeyframes(true)
                .build();
    }

    public static VideoVectorizationRequest of(List<Long> videoIds) {
        return VideoVectorizationRequest.builder()
                .videoIds(videoIds)
                .forceUpdate(false)
                .includeTranscript(true)
                .includeKeyframes(true)
                .build();
    }
}
