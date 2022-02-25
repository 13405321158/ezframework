/*
 * @作者: 魏来
 * @日期: 2022/2/18 下午1:35
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.tools.model;

import com.leesky.ezframework.nosql.model.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * <li>上传到OSS文件记录</li>
 *
 * @author: 魏来
 * @date: 2022/2/18 下午1:35
 */
@Getter
@Setter
@NoArgsConstructor
@Document(collection = "cbm_mag_uploadlog")
public class UploadLogModel extends BaseModel {

    @Field("url")
    private String url;

    @Field("oss_type")
    private String ossType;

    @Field("upload_name")
    private String uploadName;

    public UploadLogModel(String url, String ossType, String uploadName) {
        this.url = url;
        this.ossType = ossType;
        this.uploadName = uploadName;
        this.createDate = this.modifyDate = LocalDateTime.now();
    }
}
