package com.xailab.vehicle.xaivehicledata.entity.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: DomainTreeQueryIndexScoreResponse
 * @Description:
 * 
 * @date: 2025/3/1 21:55
 */
@Data
@Accessors(chain = true)
public class DomainTreeQueryIndexScoreResponse implements Serializable {
    /**
     * 指标 id
     */
    private String domainIndexId;
    /**
     * 指标名称
     */
    private String domainIndexName;
    /**
     * 指标行业均分
     */
    private Double indexAvgScore;
    /**
     * 指标详情
     */
    private String indexDetail;

    /**
     * 车分数
     */
    private List<VehicleIndexScoreResponse> vehicleIndexScore = new ArrayList<>();;


    /**
     * 车分数返回值
     */
    @Data
    @Accessors(chain = true)
    public static class VehicleIndexScoreResponse implements Serializable {
        private String vehicleInfoName;
        private String vehicleInfoId;
        private Double vehicleInfoScore;
        /**
         * 车版本
         */
        private String vehicleSystemVersion;
        /**
         * 测试时间
         */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date testDate;
    }
}
