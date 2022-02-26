/*
 * @作者: 魏来
 * @日期: 2022/2/18 下午12:24
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.tools.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.ObjectListing;
import com.google.common.collect.Lists;
import com.leesky.ezframework.query.ParamModel;
import com.leesky.ezframework.tools.service.IaliService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Service
@RefreshScope
public class AliServiceImpl implements IaliService {

    @Value("${ali.endpoint}")
    private String endpoint;

    @Value("${ali.bucketName}")
    private String bucketName;

    @Value("${ali.accessKeyId}")
    private String accessKeyId;

    @Value("${ali.accessKeySecret}")
    private String accessKeySecret;

    private final String rootDir = "whr/";


    @Override
    public ObjectListing listObje(ParamModel model) {

        ListObjectsRequest req = new ListObjectsRequest(bucketName);
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        req.withPrefix(this.rootDir + model.getOrder());//查询目录
        req.withMaxKeys(model.getLimit());//显示数量，有效值：1～1000

        ObjectListing objectListing = ossClient.listObjects(req);

        ossClient.shutdown();

        return objectListing;
    }

    @Override
    public void delObje(String key) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        ossClient.deleteObject(bucketName, key);

        ossClient.shutdown();
    }

    @Override
    public List<String> upload(MultipartFile[] file, String dir) throws IOException {
        String fileName;
        InputStream inputStream;

        List<String> list = Lists.newArrayList();
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        for (MultipartFile uploadFile : file) {
            inputStream = uploadFile.getInputStream();
            fileName = uploadFile.getOriginalFilename();

            // 生成新的文件名
            String ext = StringUtils.substringAfter(fileName, ".");// 文件后缀
            fileName = this.rootDir + dir + "/" + System.currentTimeMillis() + "." + ext;// 新的文件名

            ossClient.putObject(bucketName, fileName, inputStream);
            list.add("https://" + bucketName + "." + endpoint + File.separator + fileName);

        }
        ossClient.shutdown();

        return list;
    }
}
