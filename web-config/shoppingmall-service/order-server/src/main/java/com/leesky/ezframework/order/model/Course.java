package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.*;
import com.leesky.ezframework.order.mapper.StudentCourseMapper;
import lombok.Data;

import java.util.List;

@Data
@TableName("course")
public class Course {
	@TableId(value = "course_id")
	private Long id;
	private String name;

	@TableField(exist = false)
	@ManyToMany
	@JoinTable(targetMapper = StudentCourseMapper.class)
	@JoinColumn(name = "course_id", referencedColumnName = "course_id")
	@InverseJoinColumn(name = "child_id", referencedColumnName = "student_id")
	@Lazy(false)
	private List<Child> students;
}
