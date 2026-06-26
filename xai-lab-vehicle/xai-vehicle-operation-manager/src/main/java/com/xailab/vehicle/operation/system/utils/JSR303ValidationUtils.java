package com.xailab.vehicle.operation.system.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;


/**
 * @ClassName: JSR303ValidationUtils
 * @Description: jsr303参数校验工具类
 * @author: liulin
 * @date: 2023/2/14 10:41
 */
@Slf4j
public class JSR303ValidationUtils {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * jsr303 参数手动校验
     * @param object 校验参数
     * @Exceptions OperationException 校验失败
     */
    public static String manualValidator(Object object) {
        //参数校验
        Set<ConstraintViolation<Object>> validateSet = validator.validate(object);
        if (!CollectionUtils.isEmpty(validateSet)) {
            String message = validateSet.stream().findFirst().get().getMessage();
            log.info("参数校验不成功:{}", message);
            return message;
        }
        return null;
    }


}
