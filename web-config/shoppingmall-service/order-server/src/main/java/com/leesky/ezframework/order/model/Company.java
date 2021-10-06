package com.leesky.ezframework.order.model;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.OneToMany;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import com.leesky.ezframework.order.mapper.ImanMapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("cbm_company")
public class Company extends BaseUuidModel {

    private static final long serialVersionUID = -4065233907401013994L;

	private String name;

    //一对多
    @OneToMany
    @TableField(exist = false)
    @JoinColumn(referencedColumnName = "company_id")
    @EntityMapper(targetMapper = ImanMapper.class)
    private List<Man> employees;
}
