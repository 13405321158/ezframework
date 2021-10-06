package com.leesky.ezframework.order.model;

import java.util.Set;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.InverseJoinColumn;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.JoinTable;
import com.leesky.ezframework.mybatis.annotation.ManyToMany;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import com.leesky.ezframework.order.mapper.StudentCourseMapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("cbm_teacher")
public class Teacher extends BaseUuidModel {

	private static final long serialVersionUID = -7564115763006797934L;

	private String name;

	@ManyToMany
	@TableField(exist = false)
	@JoinTable(targetMapper = StudentCourseMapper.class)
	@JoinColumn(name = "id", referencedColumnName = "teacher_id")
	@InverseJoinColumn(name = "id", referencedColumnName = "student_id")
	private Set<Child> students;
}
