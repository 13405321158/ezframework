/**
 * @author: weilai
 * @Data:上午11:16:56,2019年12月19日
 * @Org:Sentury Co., ltd.
 * @Deparment:Domestic Sales, Tech Center
 * @Desc: <li>美邦软通 http://www.5c.com.cn/
 * <li>https://v2.5c.com.cn/ user:qilin ,pwd:asdf1234q
 */
package com.leesky.ezframework.tools.utils;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


@Component
public class MbltUtil {

    private static final String username = "qilin";

    private static final String pwd = "bd9ce951e90745d9650b24eb052879da";// 32位md5

    private static final String apiKey = "23f902e8cc8f6dc685946a69b5aada25";

    private static final String url = "http://115.28.23.78/api/send/index.php";

    /**
     * @ver: 1.0.0
     * @author: weilai
     * @data: 下午12:42:32,2019年12月19日
     * @desc: <li>success:msgid 提交成功。
     * <li>
     * <li>error:msgid 提交失败
     * <li>error:Missing username 用户名为空
     * <li>error:Missing password 密码为空
     * <li>error:Missing apikey APIKEY为空
     * <li>error:Missing recipient 手机号码为空
     * <li>error:Missing message content 短信内容为空
     * <li>error:Account is blocked 帐号被禁用
     * <li>error:Unrecognized encoding 编码未能识别
     * <li>error:APIKEY or password_md5 error APIKEY或密码错误
     * <li>error:Unauthorized IP address 未授权 IP 地址
     * <li>error:Account balance is insufficient 余额不足
     */
    public void send(String mobile, String sms) throws IOException {
        String encode = "UTF-8";

        StringBuilder buffer = new StringBuilder();
        String contentUrlEncode = URLEncoder.encode(sms, encode);

        buffer.append(url);
        buffer.append("?username=").append(username);
        buffer.append("&password_md5=").append(pwd);
        buffer.append("&mobile=").append(mobile);
        buffer.append("&apikey=").append(apiKey);
        buffer.append("&content=").append(contentUrlEncode);
        buffer.append("&encode=").append(encode);
        URL url = new URL(buffer.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

        reader.readLine();

    }


}
