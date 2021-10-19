package com.leesky.ezframework.order.model;

import java.util.Set;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.InverseJoinColumn;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.ManyToMany;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import com.leesky.ezframework.order.mapper.IstudentTeacherMapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("cbm_teacher")
public class TeacherModel extends BaseUuidModel {

    private static final long serialVersionUID = -7564115763006797934L;

    private String name;

    @ManyToMany
    @TableField(exist = false)

    @JoinColumn(referencedColumnName = "teacher_id")
    @InverseJoinColumn(referencedColumnName = "student_id")
	@EntityMapper(targetMapper = IstudentTeacherMapper.class, entityClass = StudentTeacherModel.class)
    private Set<ChildModel> students;

    public TeacherModel() {
    }

    public TeacherModel(String name) {
        this.name = name;
    }

    public TeacherModel(String name, String id) {
        this.id = id;
        this.name = name;
    }
}
