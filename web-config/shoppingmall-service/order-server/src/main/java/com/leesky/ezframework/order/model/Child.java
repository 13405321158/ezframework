package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.*;
import com.leesky.ezframework.order.mapper.StudentCourseMapper;
import com.leesky.ezframework.order.mapper.StudentTeacherMapper;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
//@AutoLazy(true)
@TableName("cbm_child")
public class Child {

	@TableId("child_id")
	private Long id;

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
	@JoinTable(targetMapper = StudentCourseMapper.class)
	@JoinColumn(name = "child_id", referencedColumnName = "student_id")
	@InverseJoinColumn(name = "course_id", referencedColumnName = "course_id")
	private List<Course> courses;

	@ManyToMany
	@TableField(exist = false)
	@JoinTable(targetMapper = StudentTeacherMapper.class)
	@JoinColumn(name = "child_id", referencedColumnName = "student_id")
	@InverseJoinColumn(name = "teacher_id", referencedColumnName = "teacher_id")
	private Set<Teacher> teachers;
}
