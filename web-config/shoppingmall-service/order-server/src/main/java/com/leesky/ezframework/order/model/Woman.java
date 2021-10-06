package com.leesky.ezframework.order.model;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.OneToMany;
import com.leesky.ezframework.mybatis.annotation.OneToOne;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import com.leesky.ezframework.order.mapper.ChildMapper;
import com.leesky.ezframework.order.mapper.ManMapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("cbm_woman")
public class Woman extends BaseUuidModel {

	private static final long serialVersionUID = 4535079787922191738L;

	private String name;

	private String laoGongId;

	@OneToOne
	@TableField(exist = false)
	@JoinColumn(name = "lao_gong_id")
	@EntityMapper(targetMapper = ManMapper.class)
	private Man laoGong;

	@OneToMany
	@TableField(exist = false)
	@JoinColumn(referencedColumnName = "lao_ma_id")
	@EntityMapper(targetMapper = ChildMapper.class)
	private List<Child> waWa;

	public Woman() {
		
	}

	public Woman(String name) {
		this.name = "赵雅芝";
	}
}
