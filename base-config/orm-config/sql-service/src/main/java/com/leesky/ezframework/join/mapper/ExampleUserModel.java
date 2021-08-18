package com.leesky.ezframework.join.mapper;

import java.io.Serializable;
//怕忘记如何使用，存下这个例子，仅供参考
//@Getter
//@Setter
//@TableName("act_id_user")
public class ExampleUserModel implements Serializable {

	private static final long serialVersionUID = 8316389130702210399L;
//
//	@TableId(value = "ID_", type = IdType.ASSIGN_UUID)
//	private String id;
//
//	@TableField(value = "REV_")
//	private Integer rev;
//
//	@TableField("FIRST_") // 登录名称	
//	private String firstName;
//
//	@TableField(value = "LAST_")
//	private String lastName;
//
//	@TableField(value = "DISPLAY_NAME_") // 中文名称
//	private String dispalyName;
//
//	@TableField(value = "EMAIL_") // 归属公司
//	private String email;
//
//	@TableField(value = "PWD_")
//	private String pwd;
//
//	@TableField(value = "PICTURE_ID_")
//	private String pictureId;
//
//	@TableField(value = "TENANT_ID_")
//	private String tenantId;
//
//	@TableField(value = "CREATE_TIME_")
//	private String createTime;
//
//	@TableField(value = "TEACHER_ID_")
//	private String teacherId;

	// 用户具有的组
//	@TableField(exist = false)
//	@ManyToMany(
//
//			joinColumns = @TargetTable(tableName = "act_id_group", onColumn = "ID_"),
//
//			inverseJoinColumns = @ShipTable(tableName = "act_id_membership", onColumn = "GROUP_ID_", whereColumn = "USER_ID_")
//
//	)
//	private Set<FlowableGroupModel> groupSet = Sets.newHashSet();

	// 用户对应课程
//	@TableField(exist = false)
//	@OneToOne(tableName = "cbm_course", joinColumn = "user_id", selectColumn = "name")
//	private FlowableCourseModel course;

//	// 用户对应老师
//	@TableField(exist = false)
//	@ManyToOne(tableName = "cbm_teacher", joinField = "teacherId", joinColumn = "id")
//	private FlowableTeacherModel teacher;
	
	
	// 老师对应学生
//	@TableField(exist = false)
//	@OneToMany(tableName = "cbm_teacher",joinColumn = "user_id")
//	private List<FlowableTeacherModel> teachers;
	
	



}
