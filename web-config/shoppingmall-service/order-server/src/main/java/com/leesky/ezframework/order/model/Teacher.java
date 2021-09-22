package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.*;
import com.leesky.ezframework.order.mapper.StudentCourseMapper;
import lombok.Data;

import java.util.Set;

@Data
@TableName("teacher")
public class Teacher {
	@TableId(value = "teacher_id")
	private Long id;
	private String name;

	@TableField(exist = false)
	@ManyToMany
	@JoinTable(targetMapper = StudentCourseMapper.class)
	@JoinColumn(name = "teacher_id", referencedColumnName = "teacher_id")
	@InverseJoinColumn(name = "child_id", referencedColumnName = "student_id")
	private Set<Child> students;
}
