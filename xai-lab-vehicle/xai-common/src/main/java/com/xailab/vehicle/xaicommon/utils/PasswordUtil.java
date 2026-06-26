package com.xailab.vehicle.xaicommon.utils;

import org.springframework.util.DigestUtils;

public class PasswordUtil {

    /**
     * 校验
     * @param enterPass
     * @param dbPassword
     * @param encrypt
     * @return
     */
    public static boolean matchingPassword(String enterPass,String dbPassword,String encrypt){
        String encPass = encryptPassword(enterPass,encrypt);
        return encPass.equals(dbPassword);
    }

    /**
     * 加密
     * @param password
     * @param encrypt
     * @return
     */
    public static String encryptPassword(String password, String encrypt){
        return DigestUtils.md5DigestAsHex((password+encrypt).getBytes());
    }

}
