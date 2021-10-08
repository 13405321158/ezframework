package com.leesky.ezframework.order.model;

import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.InverseJoinColumn;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.JoinTable;
import com.leesky.ezframework.mybatis.annotation.Lazy;
import com.leesky.ezframework.mybatis.annotation.ManyToMany;
import com.leesky.ezframework.mybatis.annotation.ManyToOne;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import com.leesky.ezframework.order.mapper.IstudentCourseMapper;
import com.leesky.ezframework.order.mapper.IstudentTeacherMapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@AutoLazy(true)
@TableName("cbm_child")
public class Child extends BaseUuidModel {

	private static final long serialVersionUID = 4806381129926616239L;

	private String name;

	private String laoHanId;

	private String laoMaId;

	@TableField(exist = false)
	@ManyToOne
	@JoinColumn(name = "lao_han_id", referencedColumnName = "man_id")
	private Man laoHan;

	@TableField(exist = false)
	@ManyToOne
	@JoinColumn(name = "lao_ma_id", referencedColumnName = "woman_id")
	private Woman laoMa;

	@Lazy(false)
	@ManyToMany
	@TableField(exist = false)
	@JoinTable(targetMapper = IstudentCourseMapper.class)
	@JoinColumn(name = "child_id", referencedColumnName = "student_id")
	@InverseJoinColumn(name = "course_id", referencedColumnName = "course_id")
	private List<Course> courses;

	@ManyToMany
	@TableField(exist = false)
	@JoinTable(targetMapper = IstudentTeacherMapper.class)
	@JoinColumn(name = "child_id", referencedColumnName = "student_id")
	@InverseJoinColumn(name = "teacher_id", referencedColumnName = "teacher_id")
	private Set<Teacher> teachers;

	public Child() {
	}

	public Child(String name) {
		this.name = name;
	}
}
