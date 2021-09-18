package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.*;
import lombok.Data;

import java.util.List;

@Data
@TableName("child2")
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
	private Man laoHan;
	
	
	@TableField(exist = false)
	@ManyToMany
	@JoinTable
	@JoinColumn(name = "child_id", referencedColumnName = "student_id")
	@InverseJoinColumn(name = "course_id", referencedColumnName = "course_id")
	@Lazy(false)
	private List<Course> courses;
	
	


	

}
