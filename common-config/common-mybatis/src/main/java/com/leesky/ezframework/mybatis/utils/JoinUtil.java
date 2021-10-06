/*
 * @作者: 魏来
 * @日期: 2021年8月25日  上午10:42:43
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.mybatis.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

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


//    /**
//     * @author: weilai
//     * @Data:2021年1月30日下午3:15:20
//     * @Desc:获取类所有属性，包括父类，爷爷等类
//     */
//    public static List<PropertyDescriptor> getFields(Object model) {
//        List<PropertyDescriptor> descriptors = Lists.newArrayList();
//        try {
//            BeanInfo bi = Introspector.getBeanInfo(model.getClass());
//
//            descriptors = Arrays.stream(bi.getPropertyDescriptors()).filter(p -> !StringUtils.equals("class", p.getName())).collect(Collectors.toList());
//
//        } catch (IntrospectionException e) {
//            e.printStackTrace();
//        }
////		for (PropertyDescriptor p : descriptors) {
////			Method readMethod = p.getReadMethod();
////			 try {
////				Object o = readMethod.invoke("entity");
////				 System.out.println(o);
////			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
////				e.printStackTrace();
////			}
////		}
//        return descriptors;
//    }



//    /**
//     * @作者: 魏来
//     * @日期: 2021年8月24日 上午8:27:22
//     * @描述: 根据model实体名称 构造对应的service名称
//     */
//    public static String buildServiceBeanNaem(Field f) {
//        String modelName = StringUtils.substringAfterLast(f.getGenericType().getTypeName(), ".").replace("Model", "").replace(">", "");
//        return StringUtils.uncapitalize(modelName) + "ServiceImpl";
//    }
//
//    /**
//     * @作者: 魏来
//     * @日期: 2021年8月24日 上午10:27:17
//     * @描述: 获取记录id
//     */
//    public static Object getId(Object obj) {
//        Object key = "";
//        if (obj instanceof BaseUuidModel) {
//            BaseUuidModel model = ((BaseUuidModel) obj);
//            key = model.getId();
//        } else if (obj instanceof BaseAutoModel) {
//            BaseAutoModel model = ((BaseAutoModel) obj);
//            key = String.valueOf(model.getId());
//        }
//
//        return key;
//    }
//
//    /**
//     * @作者: 魏来
//     * @日期: 2021年8月25日 下午12:15:06
//     * @描述: bean 属性的 get方法
//     */
//    public static String getMethod(String filed) {
//        String beanPropertyName = Hump2underline.lineToHump(filed);
//        return "get" + StringUtils.capitalize(beanPropertyName);
//    }
//
//    /**
//     * @作者: 魏来
//     * @日期: 2021年8月25日 下午12:15:11
//     * @描述: bean 属性的 set方法
//     */
//    public static String setMethod(String filed) {
//        String beanPropertyName = Hump2underline.lineToHump(filed);
//        return "set" + StringUtils.capitalize(beanPropertyName);
//    }
//
//    /**
//     * @作者: 魏来
//     * @日期: 2021年8月24日 下午12:26:39
//     * @描述: 向字段赋值: entity.setFiled(v)
//     */
//    public static void setValue(Object entity, String filed, Object v) {
//        try {
//            BeanUtils.setProperty(entity, filed, v);
//            BeanUtils.setProperty(entity, Hump2underline.lineToHump(filed), v);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @作者: 魏来
//     * @日期: 2021年8月25日 下午12:16:05
//     * @描述: 返回 对象model的 filed 字段值
//     */
//    public static Object getValue(Object model, String filed) {
//        Object ret = null;
//        try {
//            ret = BeanUtils.getProperty(model, filed);
//        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//        return ret;
//    }
}
