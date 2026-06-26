package com.xailab.vehicle.operation.testplatform.pojo.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: SyncTaskTreeResponse
 * @Description: 同步任务树返回参数
 * @author: liulin
 * @date: 2025/6/9 20:23
 */
@Data
public class SyncTaskTreeResponse implements Serializable {

    /**
     * 具有映射关系的tag
     */
    private List<FunctionTreeSyncAllTreeResponse> correspondTree;


    /**
     * 没有映射关系的tag 测试
     */
    private List<FunctionTreeSyncAllTreeResponse> noCorrespondPcafeTree;
    /**
     * 没有映射关系的tag beeeval
     */
    private List<FunctionTreeSyncAllTreeResponse> noCorrespondBeeevalTree;

}
