package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("student_teacher")
public class StudentTeacher {
	private Long id;

	@TableField("student_id")
	private Long studentId;

	@TableField("teacher_id")
	private Long teacherId;
}
