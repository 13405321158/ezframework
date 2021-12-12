package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("cbm_l_student_course")
public class StudentCourseModel {

	@TableId(value = "id", type = IdType.ASSIGN_UUID)
	private String id;

	private String studentId;


	private String courseId;
}
