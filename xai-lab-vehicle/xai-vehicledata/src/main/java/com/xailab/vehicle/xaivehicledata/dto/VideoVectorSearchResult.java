package com.xailab.vehicle.xaivehicledata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 视频向量搜索结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoVectorSearchResult {

    private Long videoId;

    private Long vehicleId;

    private Long functionDomainId;

    private Long functionDomainIndexId;

    private String summaryText;

    private String transcriptText;

    private String analysisText;

    private String keyframeText;

    private Integer videoType;

    private String taskType;

    private BigDecimal overallScore;

    private Double similarity;

    private Double textRank;

    private Double combinedScore;

    public String getVideoTypeName() {
        if (videoType == null) {
            return "未知";
        }
        return videoType == 1 ? "优秀案例" : "问题案例";
    }
}
