package com.xailab.operation.api.feign.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: OSSFileDeleteRequest
 * @Description:
 * @author: liulin
 * @date: 2025/6/10 10:46
 */
@Data
public class OSSFileDeleteRequest implements Serializable {

    /**
     * 文件id 文件名
     */
    private List<String> file_ids;

}
