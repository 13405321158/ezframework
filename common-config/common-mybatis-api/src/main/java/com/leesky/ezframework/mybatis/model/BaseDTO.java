package com.leesky.ezframework.mybatis.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Data
public class BaseDTO {

    protected String id;

    protected LocalDateTime createDate;

    protected LocalDateTime modifyDate;

    protected LocalDateTime modifyDate02;

    public LocalDateTime getCreateDate() {
        if (StringUtils.isBlank(this.id))
            return LocalDateTime.now();
        else
            return createDate;
    }

    public LocalDateTime getModifyDate() {
        return LocalDateTime.now();
    }

    public LocalDateTime getModifyDate02() {
        return modifyDate;
    }
}
