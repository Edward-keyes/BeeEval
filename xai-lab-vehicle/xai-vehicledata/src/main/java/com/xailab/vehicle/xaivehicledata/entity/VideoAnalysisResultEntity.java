package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("video_analysis_result")
public class VideoAnalysisResultEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long videoId;

    private Long vehicleId;

    private Long domainId;

    private Long indexId;

    private String transcript;

    private Double transcriptConfidence;

    private String screenshotUrl;

    private String visualDescription;

    private Double evaluationScore;

    private String evaluationDimensions;

    private String evaluationSummary;

    private String improvementSuggestions;

    private String analysisModel;

    private Integer processingTimeMs;

    private String status;

    private String errorMessage;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
