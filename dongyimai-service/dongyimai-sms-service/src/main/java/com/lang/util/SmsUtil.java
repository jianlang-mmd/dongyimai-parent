package com.lang.util;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SmsUtil {
    //每个人的appcode不一样
    @Value("${sms.appcode}")
    private String appcode;

    @Value("${sms.tpl_id}")
    private String tpl_id;

    String host = "http://dingxin.market.alicloudapi.com";
    String path = "/dx/sendSms";
    String method = "POST";

    public HttpResponse sendSms(String mobile, String code) {
        Map<String, String> heards = new HashMap<>();
        heards.put("Authorization", "APPCODE" + appcode);
        Map<String, String> querys = new HashMap<>();
        querys.put("mobile", mobile);
        querys.put("param", "code:" + code);
        querys.put("tpl_id", tpl_id);// 短信验证码的模板(默认模板)

        Map<String, String> bodys = new HashMap<>();

        try {
            return HttpUtils.doPost(host, path, method, heards, querys, bodys);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
