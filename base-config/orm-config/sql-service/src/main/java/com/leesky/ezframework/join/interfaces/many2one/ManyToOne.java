/**
 * 
 * @author:weilai
 * @Data:2020-8-1910:08:18
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:
 *        <li>
 */

package com.leesky.ezframework.join.interfaces.many2one;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
public @interface ManyToOne {

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-2014:37:52
	 * @Desc:
	 *        <li>one方实体类表名称
	 */
	String tableName();// 获取数据的目标表名称:ONE方数据表名称

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-2014:27:49
	 * @Desc:
	 *        <li>one方数据表主键：many一方发起查询时 依据此字段在one数据表中查询
	 *        <li>PS:
	 *        <li>1、在当前实体类中拥有一个 one方的字段A(一般是one方的主键值)
	 *        <li>2、关联查询时使用A的值到ONE方实体类查询，而不是使用本类的主键值(ID)关联查询
	 *        <li>3、这一点和OneToOne不同，务必注意
	 *        <li>exapmle: select * from cbm_teacher【one方】 where id【joinColumn】 ='45613e6e-7b36-4e7d-8f35-f228d381a987【many方存储的one方记录值，即joinField的值】'
	 *        <li>使用方法如下 2步：
	 *        <li>
	 *        <li>  1、 private String transferId;//one方在many方数据表中字段,驼峰规则会在many方数据表生成transfer_id,这个字段就是many方的主键
	 *        <li>
	 *        <li>  2、
	 *        <li>   @TableField(exist = false) //必须添加
	 *        <li>   @ManyToOne(joinColumn = "id 对应Many方的主键字段名称,", joinField = "transferId 对应 1、上面定义的字段", tableName ="so_transfer 对应One方法数据表名称") 
	 *        <li>   private SoTransferModel transfer;
	 */
	String joinColumn(); 

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-2015:05:36
	 * @Desc:
	 *        <li>one 一方 在 many 一方实体类中对应javaBean属性
	 */
	String joinField();//

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-2014:38:22
	 * @Desc:
	 *        <li>要查询那些字段，默认是全部
	 */
	String selectColumn() default "*";// 默认是select * (全部字段，这里可以定义select 的字段[PS： 这里设置的字段 去ONE方查询，不会出现在Many方的sql语句中])。

}
