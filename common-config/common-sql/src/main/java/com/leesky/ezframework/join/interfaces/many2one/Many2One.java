/*
 * @author:weilai
 * @Data:2020-8-1910:08:18
 * @Org:Sentury Co., ltd.
 * @Department:Domestic Sales, Tech Center
 * @Desc: <li>
 */

package com.leesky.ezframework.join.interfaces.many2one;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface Many2One {

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  上午10:38
     * @描述: one方实体类表名称
     * @描述: 注意，这里是many方，要去one方查询，所以指定的是one方表名称
     **/
    String oneTableName();

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  上午10:39
     * @描述: many一方发起查询时依据此字段在one数据表中查询
     * @描述: 关联查询一般是主键，但也可以不是主键，所以用此属性指定
     **/
    String joinColumn();

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  上午10:41
     * @描述: one 一方 在 many 一方实体类中对应javaBean属性
     * @描述: 此属性必须带有 @TableField(exist = false)，即此字段不出现在数据表中
     **/
    String joinField();

}
