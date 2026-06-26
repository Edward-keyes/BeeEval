package com.xailab.vehicle.xaimessage.service;

public interface SendMailService {

    Boolean newSendCode(String email, String password);

    Boolean newSendEnCode(String email, String password);

    Boolean newSendJpCode(String email, String password);

    Boolean sendAccountAudit(String reason,String email,Integer aon);

    Boolean sendZhengShi(String email, String password);

    Boolean sendZhengShiTest(String email, String password);
}
