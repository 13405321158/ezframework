package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("cbm_l_student_teacher")
public class StudentTeacherModel {
	

	private String studentId;


	private String teacherId;
}
