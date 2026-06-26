package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 视频向量存储实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("video_vector_store")
public class VideoVectorStoreEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("video_id")
    private Long videoId;

    @TableField("vehicle_id")
    private Long vehicleId;

    @TableField("function_domain_id")
    private Long functionDomainId;

    @TableField("function_domain_index_id")
    private Long functionDomainIndexId;

    @TableField("summary_text")
    private String summaryText;

    @TableField("transcript_text")
    private String transcriptText;

    @TableField("analysis_text")
    private String analysisText;

    @TableField("keyframe_text")
    private String keyframeText;

    @TableField("summary_embedding")
    private String summaryEmbedding;

    @TableField("transcript_embedding")
    private String transcriptEmbedding;

    @TableField("analysis_embedding")
    private String analysisEmbedding;

    @TableField("keyframe_embedding")
    private String keyframeEmbedding;

    @TableField("video_type")
    private Integer videoType;

    @TableField("task_type")
    private String taskType;

    @TableField("overall_score")
    private BigDecimal overallScore;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    public static String vectorToString(float[] vector) {
        if (vector == null || vector.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < vector.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(vector[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    public static float[] stringToVector(String vectorStr) {
        if (vectorStr == null || vectorStr.isEmpty()) {
            return null;
        }
        String content = vectorStr.substring(1, vectorStr.length() - 1);
        String[] parts = content.split(",");
        float[] vector = new float[parts.length];
        for (int i = 0; i < parts.length; i++) {
            vector[i] = Float.parseFloat(parts[i].trim());
        }
        return vector;
    }
}
