package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@TableName("company")
public class Company {
	@TableId(value = "company_id")
	private Long id;
	private String name;
	
	//一对多
	@TableField(exist = false)
	@OneToMany
//	@Lazy()
	@JoinColumn(name="company_id",referencedColumnName = "company_id")
	private List<Man> employees;
}
