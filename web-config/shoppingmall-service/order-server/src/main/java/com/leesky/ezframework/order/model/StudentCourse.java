package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("student_course")
public class StudentCourse {
	private Long id;

	@TableField("student_id")
	private Long studentId;

	@TableField("course_id")
	private Long courseId;
}
