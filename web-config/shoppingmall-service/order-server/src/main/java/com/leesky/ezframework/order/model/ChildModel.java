package com.leesky.ezframework.order.model;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.InverseJoinColumn;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.ManyToMany;
import com.leesky.ezframework.mybatis.annotation.ManyToOne;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import com.leesky.ezframework.order.mapper.ImanMapper;
import com.leesky.ezframework.order.mapper.IstudentCourseMapper;
import com.leesky.ezframework.order.mapper.IstudentTeacherMapper;
import com.leesky.ezframework.order.mapper.IwomanMapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("cbm_child")
public class ChildModel extends BaseUuidModel {

	private static final long serialVersionUID = 4806381129926616239L;

	private String name;

	private String laoMaId;

	private String laoHanId;

    
	@ManyToOne
	@TableField(exist = false)
	@JoinColumn(name = "lao_han_id")
    @EntityMapper(targetMapper = ImanMapper.class)
	private ManModel laoHan;

	@ManyToOne
	@TableField(exist = false)
	@JoinColumn(name = "lao_ma_id")
    @EntityMapper(targetMapper = IwomanMapper.class)
	private WomanModel laoMa;


	@ManyToMany
	@TableField(exist = false)
	@JoinColumn(referencedColumnName = "student_id")
	@InverseJoinColumn(referencedColumnName = "course_id")
	@EntityMapper(targetMapper = IstudentCourseMapper.class,entityClass =StudentCourseModel.class )
	private List<CourseModel> cours;

	@ManyToMany
	@TableField(exist = false)
	@JoinColumn(referencedColumnName = "student_id")
	@InverseJoinColumn(referencedColumnName = "teacher_id")
	@EntityMapper(targetMapper = IstudentTeacherMapper.class, entityClass = StudentTeacherModel.class)
	private Set<TeacherModel> teacher;

	public ChildModel() {
		this.name = "Child_"+RandomStringUtils.randomAlphabetic(4);
	}

	public ChildModel(ManModel laoHan) {
		this.laoHan = laoHan;
	}

	public ChildModel(String name) {
		this.name = name;
	}
	public ChildModel(String name,String id) {
		this.id= id;
		this.name = name;
	}
}
