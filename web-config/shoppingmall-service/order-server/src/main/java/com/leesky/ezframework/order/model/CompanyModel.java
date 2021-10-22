package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.OneToMany;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import com.leesky.ezframework.order.mapper.ImanMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@TableName("cbm_company")
public class CompanyModel extends BaseUuidModel {

	private static final long serialVersionUID = -4065233907401013994L;

	private String name;

	private String companyAddr;

	@OneToMany
	@TableField(exist = false)
	@JoinColumn(referencedColumnName = "company_id")
	@EntityMapper(targetMapper = ImanMapper.class,entityClass = ManModel.class)
	private List<ManModel> employees;

	public CompanyModel() {
	}

	public CompanyModel(String name) {
		this.name = name;
	}

	public CompanyModel(String name, String id) {
		this.id = id;
		this.name = name;
	}
}
