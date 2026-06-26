package com.xailab.vehicle.framework.common.utils;

import java.util.UUID;

/**
 * @ClassName: RandomSignUtils
 * @Description: 随机标志工具类
 * @author: liulin
 * @date: 2023/4/20 16:32
 */
public class RandomTraceUtils {


    /**
     * 获取traceId
     * @return
     */
    public static String getTraceId(){
        String versionId = String.valueOf(System.currentTimeMillis());
        String traceId = versionId + UUID.randomUUID().toString().trim().replaceAll("-", "").substring(0, 19);
        return traceId.substring(7, 28);
    }

    /**
     * 获取traceId
     * @return
     */
    public static String getTraceId(String prefix){
        String versionId = String.valueOf(System.currentTimeMillis());
        String traceId = versionId + UUID.randomUUID().toString().trim().replaceAll("-", "").substring(0, 19);
        return prefix+traceId.substring(7, 28);
    }


    /**
     * 获取spanId
     * @return
     */
    public static String getSpanId(){
        String spanId= UUID.randomUUID().toString().trim().replaceAll("-", "").substring(0, 10);
        return spanId;
    }

}
