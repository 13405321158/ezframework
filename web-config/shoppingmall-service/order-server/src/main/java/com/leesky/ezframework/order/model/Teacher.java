package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.InverseJoinColumn;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.JoinTable;
import com.leesky.ezframework.mybatis.annotation.ManyToMany;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import com.leesky.ezframework.order.mapper.IstudentCourseMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@TableName("cbm_teacher")
public class Teacher extends BaseUuidModel {

    private static final long serialVersionUID = -7564115763006797934L;

    private String name;

    @ManyToMany
    @TableField(exist = false)
    @JoinTable(targetMapper = IstudentCourseMapper.class)
    @JoinColumn(name = "id", referencedColumnName = "teacher_id")
    @InverseJoinColumn(name = "id", referencedColumnName = "student_id")
    private Set<Child> students;

    public Teacher() {
    }

    public Teacher(String name) {
        this.name = name;
    }

    public Teacher(String name, String id) {
        this.id = id;
        this.name = name;
    }
}
