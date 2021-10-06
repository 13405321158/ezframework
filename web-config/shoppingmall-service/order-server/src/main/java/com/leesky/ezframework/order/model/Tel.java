package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.ManyToOne;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("cbm_tel")
public class Tel extends BaseUuidModel {

	private static final long serialVersionUID = 1260334746552255040L;

	private String tel;

	private String manId;

	@ManyToOne
	@JoinColumn
	@TableField(exist = false)
	private Man laoHan;

}
