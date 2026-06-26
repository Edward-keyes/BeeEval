package com.xailab.vehicle.operation.system.utils;

import okhttp3.*;
import okhttp3.Request.Builder;
import org.apache.commons.collections4.MapUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * okhttp工具类
 * 
 */
public class Okhttp3Utils {
    // 自定义 OkHttpClient 实例，并设置超时时间
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(1200, TimeUnit.SECONDS) // 设置读取超时为60秒
            .connectTimeout(1200, TimeUnit.SECONDS) // 设置连接超时为30秒
            .writeTimeout(1200, TimeUnit.SECONDS) // 设置写入超时为30秒
            .build();

    /**
     * 执行GET请求
     *
     * @param url       请求URL
     * @param headerMap 请求头参数
     * @return 响应数据
     * @throws IOException IO异常
     */
    public static String getData(String url, Map<String, String> headerMap) throws IOException {
        Builder builder = new Builder().url(url);
        addHeaders(builder, headerMap);
        Call call = okHttpClient.newCall(builder.build());
        try (Response response = call.execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                System.out.println("GET Request failed. Code: " + response.code());
            }
        }
        return null;
    }

    /**
     * 执行POST请求
     *
     * @param url       请求URL
     * @param json      请求体数据(JSON格式)
     * @param headerMap 请求头参数
     * @return 响应数据
     * @throws IOException IO异常
     */
    public static String postData(String url, String json, Map<String, String> headerMap) throws IOException {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Builder builder = new Builder().url(url);
        addHeaders(builder, headerMap);
        Request request = builder.post(requestBody).build();
        try (ResponseBody body = okHttpClient.newCall(request).execute().body()) {
            return resolver(body);
        }
    }

    /**
     * 执行POST请求
     *
     * @param url       请求URL
     * @param json      请求体数据(JSON格式)
     * @param headerMap 请求头参数
     * @return 响应数据
     * @throws IOException IO异常
     */
    public static String deleteData(String url, String json, Map<String, String> headerMap) throws IOException {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Builder builder = new Builder().url(url);
        addHeaders(builder, headerMap);
        Request request = builder.delete(requestBody).build();
        try (ResponseBody body = okHttpClient.newCall(request).execute().body()) {
            return resolver(body);
        }
    }

    /**
     * 添加请求头参数
     *
     * @param builder   请求构建器
     * @param headerMap 请求头参数
     */
    private static void addHeaders(Builder builder, Map<String, String> headerMap) {
        if (MapUtils.isNotEmpty(headerMap)) {
            headerMap.forEach(builder::addHeader);
        }
    }

    /**
     * 处理响应体数据
     *
     * @param body 响应体
     * @return 响应数据字符串
     * @throws IOException IO异常
     */
    private static String resolver(ResponseBody body) throws IOException {
        return body.string();
    }

    /**
     * 上传文件
     *
     * @param url       请求 URL
     * @param headerMap 请求头参数
     * @return 响应数据
     * @throws IOException IO 异常
     */
    public static String uploadFile(String url,MultipartBody.Builder multipartBuilder,Map<String, String> headerMap) throws IOException {
        // 创建 MultipartBody.Builder
        // 构建请求
        Builder builder = new Builder().url(url);
        addHeaders(builder, headerMap);
        Request request = builder.post(multipartBuilder.build()).build();

        // 执行请求
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                System.out.println("POST Request failed. Code: " + response.code());
            }
        }
        return null;
    }
}