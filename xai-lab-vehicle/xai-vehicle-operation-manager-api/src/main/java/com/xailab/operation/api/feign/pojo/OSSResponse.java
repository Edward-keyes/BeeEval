package com.xailab.operation.api.feign.pojo;

import lombok.Data;

@Data
public class OSSResponse {

    private String success;

    private String reason;

    private String playlist_url;

    private String url;

}
