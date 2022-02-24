/**
 * <li>阿里云 OSS service接口</li>
 *
 * @author: 魏来
 * @date: 2022/2/18 下午12:23
 */
package com.leesky.ezframework.tools.service;

import com.aliyun.oss.model.ObjectListing;
import com.leesky.ezframework.query.ParamModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface IaliService {

    /**
     * <li>获取阿里OSS中 上传的文件列表</li>
     *
     * @author: 魏来
     * @date: 2022/2/18 下午12:32
     */
    ObjectListing listObje(ParamModel model);

    /**
     * <li>删除指定目标</li>
     * <ali oss 数据列表返回格式如下：
     * {
     * "bucketName": "sentury-oss",
     * "key": "whr/cms/0029e017-79c9-403d-9db2-704d826db5e6.png",
     * "size": 281211,
     * "lastModified": "2020-08-21T15:01:06.000+08:00",
     * "storageClass": "Standard",
     * "owner": {
     * "displayName": "1331183221740434",
     * "id": "1331183221740434"
     * },
     * "type": "Normal",
     * "etag": "847593973A066FAB38884E38B840A219"
     * },
     *
     * @author: 魏来
     * @date: 2022/2/18 下午12:45
     */
    void delObje(String key);

    /**
     * <li>上传对象到oss； 森麒麟OSS 根目录是 /whr</li>
     * <li>网站上传到/whr/cms目录下</li>
     * <li>知识库(麒麟通，云店)上传到/whr/kbs目录下</li>
     *
     * @author: 魏来
     * @date: 2022/2/18 下午12:58
     */
    List<String> upload(MultipartFile[] file, String dir) throws IOException;
}
