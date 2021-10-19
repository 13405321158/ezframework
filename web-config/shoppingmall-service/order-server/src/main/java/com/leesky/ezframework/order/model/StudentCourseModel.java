package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("cbm_l_student_course")
public class StudentCourseModel {

	@TableId
	private String id;

	private String studentId;


	private String courseId;
}
