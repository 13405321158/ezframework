/*
 * @作者: 魏来
 * @日期: 2021年8月25日  上午10:42:43
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.mybatis.utils;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class JoinUtil {

    /**
     * @作者: 魏来
     * @日期: 2021年8月24日 上午8:27:22
     * @描述: 根据model实体名称 构造对应到ixxxMapper名称
     */
    public static String buildMapperBeanName(String entityName) {
        String modelName = StringUtils.substringAfterLast(entityName, ".").replace("Model", "");
        return "i" + StringUtils.uncapitalize(modelName) + "Mapper";
    }

    /**
     * <li>:根据mapper名字 返回 model名字
     *
     * @作者: 魏来
     * @日期: 2021/10/8  上午10:42
     **/
    public static String buildModelBeanName(String name) {
        String modelName = StringUtils.replace(name, "Mapper", "");
        modelName = StringUtils.substring(modelName, 1);
        return StringUtils.capitalize(modelName) + "Model";
    }

    /**
     * @author: weilai
     * @Data:2021年1月30日下午3:15:20
     * @Desc:获取类所有属性，包括父类，爷爷等类
     */
    public static List<Field> getAllField(Object model) {
        Class<?> clazz = model.getClass();
        List<Field> fields = Lists.newArrayList();
        while (clazz != null) {
            Field[] fs = clazz.getDeclaredFields();
            fields.addAll(Arrays.asList(fs));

            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    /**
     * 在实体类中 查找 fieldName的字段
     *
     * @author： 魏来
     * @date: 2021/12/16 下午6:16
     */
    public static Field getField(Object model, String fieldName) {
        List<Field> fs = getAllField(model);
        for (Field f : fs) {
            if (f.getName().equals(fieldName))
                return f;
        }
        return null;
    }

    /**
     * 返回实体类的 数据表名称
     *
     * @author： 魏来
     * @date: 2021/12/17 上午11:23
     */
    public static String getTableName(Class<?> obj) {
        TableName tb = obj.getAnnotation(TableName.class);
        Assert.isTrue(ObjectUtils.isNotEmpty(tb), "在" + obj.getName() + "上未发现TableName注解");
        return tb.value();
    }

    /**
     * 返回实体类中的主键 name
     *
     * @author： 魏来
     * @date: 2021/12/17 下午12:54
     */
    public static String getTableKeyName(Class<?> clazz) {
        List<Field> fields = getAllField(clazz);
        for (Field f : fields) {
            TableId td = f.getAnnotation(TableId.class);
            if (ObjectUtils.isNotEmpty(td))
                return td.value();
        }

        return null;
    }


    /**
     * @author: weilai
     * @Data:2021年1月30日下午3:15:20
     * @Desc:获取类所有属性，包括父类，爷爷等类
     */
    public static List<Field> getAllField(Class<?> clazz) {

        List<Field> fields = Lists.newArrayList();
        while (clazz != null) {
            Field[] fs = clazz.getDeclaredFields();
            fields.addAll(Arrays.asList(fs));

            clazz = clazz.getSuperclass();
        }
        return fields;
    }
}
