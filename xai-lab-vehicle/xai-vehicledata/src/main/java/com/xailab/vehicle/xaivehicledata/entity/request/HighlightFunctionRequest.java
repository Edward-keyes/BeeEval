package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

import java.util.List;

public class HighlightFunctionRequest {

    /**
     * 一级标签名称
     */
    private String oneTagName; // 不需要 final

    /**
     * iconUrl
     */
    private String iconUrl;

    /**
     * 二级标签list
     */
    private List<TwoTagHighlightRequest> twoTagHighlightRequests; // 不需要 final

    public HighlightFunctionRequest(String oneTagName,String iconUrl, List<TwoTagHighlightRequest> twoTagHighlightRequests) {
        this.oneTagName = oneTagName;
        this.iconUrl = iconUrl;
        this.twoTagHighlightRequests = twoTagHighlightRequests;
    }

    // 手动实现 getter 和 setter
    public String getOneTagName() {
        return oneTagName;
    }

    public void setOneTagName(String oneTagName) {
        this.oneTagName = oneTagName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public List<TwoTagHighlightRequest> getTwoTagHighlightRequests() {
        return twoTagHighlightRequests;
    }

    public void setTwoTagHighlightRequests(List<TwoTagHighlightRequest> twoTagHighlightRequests) {
        this.twoTagHighlightRequests = twoTagHighlightRequests;
    }
}
