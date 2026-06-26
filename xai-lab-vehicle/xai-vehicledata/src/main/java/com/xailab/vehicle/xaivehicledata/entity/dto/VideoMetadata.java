package com.xailab.vehicle.xaivehicledata.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoMetadata {

    private Long duration;

    private Integer width;

    private Integer height;

    private String format;

    private Double frameRate;

    private String codec;

    private Long bitrate;

    private Boolean hasAudio;

    private Boolean hasSubtitle;
}
