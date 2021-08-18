/**
 * 
 * @author:weilai
 * @Data:2020-8-1816:20:16
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:
 *        <li>
 */

package com.leesky.ezframework.join.interfaces.many2may;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
public @interface ManyToMany {

	SourceTable joinColumns();

	ShipTable inverseJoinColumns();

	String foreignKey() default "";// 如果是非主键关联，请用此字段 指定
	
	String selectColumn() default "*";// 默认是select * (全部字段，这里可以定义select 的字段)。


	// SELECT a.*【#selectColumn位置】 FROM act_id_group a【#joinColumns.tableName位置】
	// LEFT JOIN act_id_membership【#inverseJoinColumns.tableName位置】 b on
	// a.ID_【joinColumns.columnName位置】
	// = b.GROUP_ID_【inverseJoinColumns.onColumn位置】 where
	// b.USER_ID_【inverseJoinColumns.whereColumn位置】
	// ='1f5826c7-0e81-457b-90f1-27e1cf3e762a【foreignKey位置】'
}
