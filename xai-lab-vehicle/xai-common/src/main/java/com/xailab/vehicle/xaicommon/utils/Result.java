/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.xailab.vehicle.xaicommon.utils;

import lombok.Data;
import org.apache.http.HttpStatus;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * 
 */
@Data
public class  Result<T> implements Serializable {

	private int code;
	private String msg;
	private T data;

	private static final long serialVersionUID = 1L;

	public Result() {
		this.code = 0;
		this.msg = "success";
	}
	
	public static <T> Result<T> error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}
	
	public static <T> Result<T> error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}
	
	public static <T> Result<T> error(int code, String msg) {
		Result<T> r = new Result<>();
		r.setCode(code);
		r.setMsg(msg);
		return r;
	}

	public static <T> Result<T> ok(String msg) {
		Result<T> r = new Result<>();
		r.setMsg(msg);
		return r;
	}
	public static <T> Result<T> ok(T datag) {
		Result<T> r = new Result<T>();
		r.setData(datag);
		return r;
	}
	
	public static <T> Result<T> ok() {
		return new Result();
	}

	public static <T> Result<T> ok(Integer code,String msg) {
		Result<T> r = new Result<>();
		r.setCode(code);
		r.setMsg(msg);
		return r;
	}

	public boolean isOk(){
		return code == 0;
	}

}
