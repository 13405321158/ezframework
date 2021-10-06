package com.leesky.ezframework.order.model;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.InverseJoinColumn;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.JoinTable;
import com.leesky.ezframework.mybatis.annotation.ManyToMany;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import com.leesky.ezframework.order.mapper.StudentCourseMapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("cbm_course")
public class Course extends BaseUuidModel{

	
    private static final long serialVersionUID = -2246038785611164245L;

	private String name;

    @TableField(exist = false)
    @ManyToMany
    @JoinTable(targetMapper = StudentCourseMapper.class)
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    @InverseJoinColumn(name = "child_id", referencedColumnName = "student_id")
    private List<Child> students;
}
