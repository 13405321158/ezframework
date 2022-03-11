package com.leesky.ezframework.mybatis.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseDTO implements Serializable {

    private static final long serialVersionUID = -6053115515437350525L;

    protected String id;

    protected LocalDateTime createDate;

    protected LocalDateTime modifyDate;


    /**
     * <li>新增记录时 这两个字段就能获取初始值了，不用手工赋值</li>
     *
     * @author: 魏来
     * @date: 2022/3/4 下午12:39
     */
    public BaseDTO() {
        if (StringUtils.isBlank(this.id))
            this.modifyDate = this.createDate = LocalDateTime.now();

    }


}
