/*
 *
 * @author:weilai
 * @Data:2020-8-1910:09:25
 * @Org:Sentury Co.,ltd.
 * @Department:Domestic Sales,Tech Center
 * @Desc:
 */

package com.leesky.ezframework.join.interfaces.one2many;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface One2Many {

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  上午10:46
     * @描述: many一方实体类表名称
     * @描述: 注意，这里是one方，要去many方查询，所以指定的是many方表名称
     **/
    String manyTableName();

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  上午10:46
     * @描述: 此字段在many方数据表中，存储的是one方的映射
     * @描述: one一方发起查询时依据此字段在many数据表中查询
     * @描述: 关联查询一般是主键，但也可以不是主键，所以用此属性指定
     **/
    String joinColumn();

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  上午10:46
     * @描述: many方类的bean属性,通过此属性和one方建立映射关系。
     **/
    String joinField();


}