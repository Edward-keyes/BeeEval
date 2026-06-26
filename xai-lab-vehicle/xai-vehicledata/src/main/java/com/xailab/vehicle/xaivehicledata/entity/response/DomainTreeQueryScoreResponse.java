package com.xailab.vehicle.xaivehicledata.entity.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: DomainTreeQueryScoreRequest
 * @Description:
 *
 * @date: 2025/3/1 21:55
 */
@Data
@Accessors(chain = true)
public class DomainTreeQueryScoreResponse implements Serializable {
    /**
     * 功能域
     */
    private String functionDomainId;
    /**
     * 功能域名称
     */
    private String functionDomainName;
    /**
     * 行业均分
     */
    private Double industryAvgScore;

    /**
     * 车分数
     */
    private List<VehicleScoreResponse> vehicleScore;


    /**
     * 车分数返回值
     */
    @Data
    @Accessors(chain = true)
    public static class VehicleScoreResponse implements Serializable {
        private String vehicleInfoName;
        private String vehicleInfoId;
        private Double vehicleInfoScore;

        // 构造方法
        public VehicleScoreResponse(String vehicleInfoName, String vehicleInfoId, Double vehicleInfoScore) {
            this.vehicleInfoName = vehicleInfoName;
            this.vehicleInfoId = vehicleInfoId;
            this.vehicleInfoScore = vehicleInfoScore;
        }
    }
}
