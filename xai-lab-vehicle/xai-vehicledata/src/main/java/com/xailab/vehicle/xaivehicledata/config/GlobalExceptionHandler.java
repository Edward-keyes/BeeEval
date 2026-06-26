package com.xailab.vehicle.xaivehicledata.config;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SaTokenException.class)
    public SaResult handleSaTokenException(SaTokenException e) {

        return SaResult.error(e.getMessage()).setCode(e.getCode());
    }
}