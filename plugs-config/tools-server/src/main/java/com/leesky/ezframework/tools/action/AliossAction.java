/**
 * @author:weilai
 * @Data:2020年10月16日下午7:47:08
 * @Org:Sentury Co., ltd.
 * @Deparment:Domestic Sales, Tech Center
 * @Desc: <li>阿里云OSS存储对象
 */
package com.leesky.ezframework.tools.action;

import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.google.common.collect.Lists;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.query.ParamModel;
import com.leesky.ezframework.tools.model.uploadModel;
import com.leesky.ezframework.tools.service.IaliService;
import com.leesky.ezframework.tools.service.IuploadService;
import com.leesky.ezframework.tools.utils.UserContext;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/oss/ali")
public class AliossAction {


    private final IaliService service;
    private final UserContext userContext;
    private final IuploadService uploadService;

    /**
     * <li>上传 图片至阿里云（支持多图）,使用默认目录
     *
     * @author: 魏来
     * @date: 2022/2/18 下午1:11
     */
    @SneakyThrows
    @RequestMapping("/upload")
    public Result<List<String>> list(@RequestParam("files") MultipartFile[] file, @RequestParam(value = "dir") String dir) {

        List<String> list = this.service.upload(file, dir);

        List<uploadModel> uploadLog = Lists.newArrayList();
        list.forEach(e -> uploadLog.add(new uploadModel(e, "ali", this.userContext.getUserName())));
        this.uploadService.saveBatch(uploadLog);

        return Result.success(list, false);
    }


    /**
     * <li>parm.getExt = label,parm.limit =每次加载数量：1～1000是有效值
     *
     * @author: 魏来
     * @date: 2022/2/18 下午1:12
     */
    @RequestMapping("/list")
    public Result<List<OSSObjectSummary>> list(@RequestBody ParamModel model) {

        Assert.isTrue(StringUtils.isNotBlank(model.getOrder()), "目录参数[ext]不允许空");

        ObjectListing objectListing = this.service.listObje(model);

        List<OSSObjectSummary> data = objectListing.getObjectSummaries();
        int size = objectListing.getObjectSummaries().size();

        return Result.success(data, Long.valueOf(size), false);
    }

    /**
     * 删除指定文件
     *
     * @author: 魏来
     * @date: 2022/2/18 下午1:12
     */
    @RequestMapping("/del")
    public Result del(String key) {
        this.service.delObje(key);
        return Result.success();
    }

}
