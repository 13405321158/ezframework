package com.leesky.ezframework.order.model;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.AutoLazy;
import com.leesky.ezframework.mybatis.annotation.InverseJoinColumn;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.JoinTable;
import com.leesky.ezframework.mybatis.annotation.Lazy;
import com.leesky.ezframework.mybatis.annotation.ManyToMany;
import com.leesky.ezframework.mybatis.annotation.ManyToOne;

import lombok.Data;

@Data
@TableName("cbm_child2")
@AutoLazy
public class Child2 {
	@TableId("child_id")
	private Long id;

	private String name;

	private Long laoHanId;
	
	@TableField(exist = false)
	@ManyToOne
	@JoinColumn(name = "lao_han_id", referencedColumnName = "man_id")
	//@EntityMapper
	@Lazy
	private ManModel laoHan;
	
	
	@TableField(exist = false)
	@ManyToMany
	@JoinTable
	@JoinColumn(name = "child_id", referencedColumnName = "student_id")
	@InverseJoinColumn(name = "course_id", referencedColumnName = "course_id")
	@Lazy(false)
	private List<CourseModel> cours;
	
	


	

}
