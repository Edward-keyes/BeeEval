package com.xailab.operation.api.feign.pojo;

import lombok.Data;

@Data
public class OssUploadRequest {

    private String file_id;

    private String file_type;

    private String uploaded_file;

    private String file_url;

    private String local_file_path;
}
