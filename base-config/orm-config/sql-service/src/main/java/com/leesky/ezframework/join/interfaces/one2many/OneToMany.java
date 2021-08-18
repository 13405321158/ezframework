/**
 * 
 * @author:weilai
 * @Data:2020-8-1910:09:25
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:
 *        <li>
 */

package com.leesky.ezframework.join.interfaces.one2many;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
public @interface OneToMany {

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-2014:37:52
	 * @Desc:
	 *        <li>many一方实体类表名称
	 */
	String tableName();// 获取数据的目标表名称

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-2014:27:49
	 * @Desc:
	 *        <li>many方数据表存储的one关联字段名称：one一方发起查询时依据此字段在many数据表中查询
	 */
	String joinColumn();

	/**
	 * 
	 * @author:weilai
	 * @Data:2020年11月20日 下午4:49:21
	 * @Desc:
	 *        <li>one方类的bean属性，依据此属性的值 去many方数据表查询
	 */
	String oneClassProperty();

	/**
	 * 
	 * 
	 * @author:weilai
	 * @Data:2020-8-2014:38:22
	 * @Desc:
	 *        <li>要查询那些字段，默认是全部
	 */
	String selectColumn() default "*";// 默认是select * (全部字段，这里可以定义select 的字段)。


}
