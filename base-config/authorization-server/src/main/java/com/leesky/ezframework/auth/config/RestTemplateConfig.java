/*
 * @作者: 魏来
 * @日期: 2022/3/15 上午10:34
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.auth.config;


import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    //最大连接数
    private Integer maxTotal = 100;
    //创建连接的最长时间
    private Integer connectTimeout = 1000;
    //数据传输的最长时间
    private Integer socketTimeout = 10000;
    //并发数
    private Integer defaultMaxPerRoute = 20;
    //从连接池中获取到连接的最长时间
    private Integer connectionRequestTimeout = 500;
    //提交请求前测试连接是否可用
    private boolean staleConnectionCheckEnabled = true;
    //可用空闲连接过期时间,重用空闲连接时会先检查是否空闲时间超过这个时间，如果超过，释放socket重新建立
    private Integer validateAfterInactivity = 3000000;


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(httpRequestFactory());
    }

    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public HttpClient httpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(maxTotal); // 最大连接数
        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);    //单个路由最大连接数
        connectionManager.setValidateAfterInactivity(validateAfterInactivity); // 最大空间时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)        //服务器返回数据(response)的时间，超过抛出read timeout
                .setConnectTimeout(connectTimeout)      //连接上服务器(握手成功)的时间，超出抛出connect timeout
                .setStaleConnectionCheckEnabled(staleConnectionCheckEnabled) // 提交前检测是否可用
                .setConnectionRequestTimeout(connectionRequestTimeout)//从连接池中获取连接的超时时间，超时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
                .build();
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();
    }

}
