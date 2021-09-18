package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.Lazy;
import com.leesky.ezframework.mybatis.annotation.ManyToOne;
import lombok.Data;

@Data
@TableName("tel")
public class Tel {
	private Long id;
	private String tel;
	
	@TableField("man_id")
	private Long manId;

	@TableField(exist = false)
	@ManyToOne
	@JoinColumn(name = "man_id", referencedColumnName = "man_id")
	@Lazy
	private Man laoHan;
	
}
