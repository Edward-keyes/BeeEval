/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.xailab.vehicle.feign.common;

import lombok.Data;
import org.apache.http.HttpStatus;

import java.io.Serializable;

/**
 * 返回数据
 *
 * 
 */
@Data
public class Result<T> implements Serializable {

	/**
	 * 状态码 0为成功
	 */
	private int code;
	private String msg;
	private T data;

	private static final long serialVersionUID = 1L;

	public Result() {
		this.code = 0;
		this.msg = "success";
	}
	
	public static Result error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}
	
	public static Result error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}
	
	public static Result error(int code, String msg) {
		Result r = new Result();
		r.setCode(code);
		r.setMsg(msg);
		return r;
	}

	public static Result ok(String msg) {
		Result r = new Result();
		r.setMsg(msg);
		return r;
	}
	public static <T> Result<T> ok(T datag) {
		Result<T> r = new Result<T>();
		r.setData(datag);
		return r;
	}
	
	public static Result ok() {
		return new Result();
	}

	public static Result ok(Integer code,String msg) {
		Result r = new Result();
		r.setCode(code);
		r.setMsg(msg);
		return r;
	}
	public boolean isOk(){
		return code == 0;
	}

}
