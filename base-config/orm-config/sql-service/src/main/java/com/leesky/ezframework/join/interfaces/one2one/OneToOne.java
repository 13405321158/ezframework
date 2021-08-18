/**
 * 
 * @author:weilai
 * @Data:2020-8-1910:08:48
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:
 *        <li>
 */

package com.leesky.ezframework.join.interfaces.one2one;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
public @interface OneToOne {

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-2014:34:16
	 * @Desc:
	 *        <li>要关联的(注解所字段属性)类实体类表名称
	 *        <li>非当前注解所在实体的表名称
	 */
	String tableName();// 获取数据的目标表名称

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-2014:31:10
	 * @Desc:
	 *        <li>对方实体类中拥有本类的一个字段(一般是本类的主键值)
	 *        <li>去对方数据表查询时根据 本类的 ID值
	 *        <li>如果OneToOne是双向的，则在本类也含有对方实体类的一个字段(一般是对方主键值)
	 */
	String joinColumn() default "";

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-2014:35:01
	 * @Desc:
	 *        <li>查询那些字段，默认是全部字段
	 */
	String selectColumn() default "*";// 默认是select * (全部字段，这里可以定义select 的字段)。



}
