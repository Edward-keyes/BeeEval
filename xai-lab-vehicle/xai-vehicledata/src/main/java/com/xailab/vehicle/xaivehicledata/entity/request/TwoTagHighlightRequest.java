package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

import java.util.List;

public class TwoTagHighlightRequest {

    /**
     * 二级标签名称
     */
    private String twoTagName;

    /**
     * 三级标签列表
     */
    private List<ThreeTagHighlightRequest> threeTagHighlightRequests;

    public TwoTagHighlightRequest(String twoTagName, List<ThreeTagHighlightRequest> threeTagHighlightRequests) {
        this.twoTagName = twoTagName;
        this.threeTagHighlightRequests = threeTagHighlightRequests;
    }

    public String getTwoTagName() {
        return twoTagName;
    }

    public List<ThreeTagHighlightRequest> getThreeTagHighlightRequests() {
        return threeTagHighlightRequests;
    }

    @Override
    public String toString() {
        return "TwoTagHighlightRequest{" +
                "twoTagName='" + twoTagName + '\'' +
                ", threeTagHighlightRequests=" + threeTagHighlightRequests +
                '}';
    }
}