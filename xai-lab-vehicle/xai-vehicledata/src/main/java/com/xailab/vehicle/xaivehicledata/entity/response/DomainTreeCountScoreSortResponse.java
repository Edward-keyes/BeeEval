package com.xailab.vehicle.xaivehicledata.entity.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
public class DomainTreeCountScoreSortResponse implements Serializable {

    private String functionDomainId;
    /**
     * 功能域名称
     */
    private String functionalDomainName;
    /**
     * 指标行业均分
     */
    private Double industryAvgScore;

    /**
     * 车分数
     */
    private List<VehicleIndexScoreResponse> vehicleIndexScore;


    /**
     * 车分数返回值
     */
    @Data
    @Accessors(chain = true)
    public static class VehicleIndexScoreResponse implements Serializable {
        /**
         * 车辆系统版本
         */
        private String vehicleSystemVersion;
        /**
         * 测试时间
         */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date testDate;
        /**
         * 车辆名称
         */
        private String vehicleInfoName;
        /**
         * 车辆ID
         */
        private String vehicleInfoId;
        /**
         * 车辆分数
         */
        private Double vehicleInfoScore;
    }
}
