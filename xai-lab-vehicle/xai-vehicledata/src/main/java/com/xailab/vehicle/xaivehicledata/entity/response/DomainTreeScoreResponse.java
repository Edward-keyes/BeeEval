package com.xailab.vehicle.xaivehicledata.entity.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DomainTreeScoreResponse {

    /**
     * 表头
     */
    private List<HeaderResponse> headerList;

    /**
     * 表数据
     */
    private List<Map<String,Object>> tableList;

    @Data
    public static class HeaderResponse{

        /**
         * 表头名称
         */
        private String headerName;

        /**
         * 表头ID
         */
        private String headerId;

        public HeaderResponse(String functionalDomainName, String functionDomainId) {
            this.headerName = functionalDomainName;
            this.headerId = functionDomainId;
        }
    }

}
