package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.OneToMany;
import com.leesky.ezframework.mybatis.annotation.OneToOne;
import com.leesky.ezframework.mybatis.enums.FetchType;
import com.leesky.ezframework.order.mapper.ChildMapper;
import com.leesky.ezframework.order.mapper.ManMapper;
import lombok.Data;

import java.util.List;

@Data
@TableName("woman")
public class Woman {
	@TableId(value = "woman_id")
	private Long id;

	private String name;

	@TableField("lao_gong_id")
	private Long laoGongId;

	@TableField(exist = false)
	@OneToOne
	@JoinColumn(name = "lao_gong_id", referencedColumnName = "man_id")
	@EntityMapper(targetMapper = ManMapper.class)
	private Man laoGong;

	@TableField(exist = false)
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "woman_id", referencedColumnName = "lao_ma_id")
	@EntityMapper(targetMapper = ChildMapper.class)
	private List<Child> waWa;
}
