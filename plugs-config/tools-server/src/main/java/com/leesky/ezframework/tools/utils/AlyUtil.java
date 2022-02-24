package com.leesky.ezframework.tools.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AlyUtil {

    @Value("${ali.accessKeyId}")
    private String accessKeyId;
    @Value("${ali.accessKeySecret}")
    private String accessKeySecret;

    private  final DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);

    public  void send(String mobile, String code, SignEnum sign) {
        try {
            IAcsClient client = new DefaultAcsClient(profile);
            CommonRequest request = new CommonRequest();
            request.setSysMethod(MethodType.POST);
            request.setSysDomain("dysmsapi.aliyuncs.com");
            request.setSysVersion("2017-05-25");
            request.setSysAction("SendSms");
            request.putQueryParameter("RegionId", "cn-hangzhou");
            request.putQueryParameter("PhoneNumbers", mobile);
            request.putQueryParameter("SignName", sign.getSign());
            request.putQueryParameter("TemplateCode", sign.getTemplate());
            request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");

            client.getCommonResponse(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        send("13405321158", "4521", SignEnum.qlb);
//    }
}
