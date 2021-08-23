///*
// * @author: weilai
// * @Data:2021年1月8日上午10:58:16
// * @Org:Sentury Co., ltd.
// * @Department:Domestic Sales, Tech Center
// * @Desc: <li>
// */
//package com.leesky.ezframework.join.query;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.TypeReference;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import com.google.common.collect.Sets;
//import com.leesky.ezframework.join.interfaces.ManyToOne;
//import com.leesky.ezframework.join.interfaces.OneToMany;
//import com.leesky.ezframework.join.mapper.LeeskyMapper;
//import com.leesky.ezframework.query.QueryFilter;
//import com.leesky.ezframework.utils.Hump2underline;
//import org.apache.commons.lang3.ObjectUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.reflect.FieldUtils;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Assert;
//import org.springframework.util.ReflectionUtils;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Component
//@SuppressWarnings("unchecked")
//public class JoinQuery<T, M> {
//
//    private LeeskyMapper<T> baseMapper = null;
//
//    public List<T> findAll(QueryFilter<T> filter, Class<T> entity, M repo) {
//
//        Page<T> data = listAll(filter, null, entity, repo);
//
//        List<T> result = lineToHump(data.getRecords(), entity);
//
//        return result;
//
//    }
//
//    public Page<T> pageQuery(QueryFilter<T> filter, Page<T> page, Class<T> entity, M repo) {
//        Page<T> data = listAll(filter, page, entity, repo);
//        List<T> result = lineToHump(data.getRecords(), entity);
//        page.setRecords(result);
//        page.setTotal(data.getTotal());
//
//        return page;
//
//    }
//
//    private Page<T> listAll(QueryFilter<T> filter, Page<T> page, Class<T> entity, M repo) {
//
//        StringBuffer buf = new StringBuffer();
//        this.baseMapper = (LeeskyMapper<T>) repo;
//        // 1、获取查询条件
//        String select = filter.getParam().getExt();// 1.1 获取select 条件
//        String queryStr = filter.getParam().getQueryStr();// 1.2 获取where条件
//
//        select = StringUtils.isNotBlank(select) ? select : "a.*";
//        verifySelect(select);// select 要求: 主表字段需要含有"a." ，子表字段含有"xxx." xxx = 子表在A表集合名称，如‘items’
//
//        // 2、主表名称
//        String mainTableName = entity.getAnnotation(TableName.class).value();
//        buf.append(select).append(" from ").append(mainTableName).append(" a ");
//
//        // 3、主表中是否含有多个子表集合，遍历集合
//        Set<Field> one2manySet = findFieldsByAnnotation(entity, OneToMany.class);
//        Set<Field> many2oneSet = findFieldsByAnnotation(entity, ManyToOne.class);
//
//
//        Map<String, String> params = JSON.parseObject(queryStr, new TypeReference<>() {
//        });
//        // 4、如果含有多个子表，本次参与查询筛选方法
//        List<String> result = selectChild(params);
///*
//		for (Field f : one2manySet) {
//			String fname = f.getName();// 用为：sql语句中子表别名
//
//			if (result.contains(fname)) {
//				OneToMany zj = f.getAnnotation(OneToMany.class);
//				String relationMainTable = Hump2underline.build(zj.joinField());// 主表：关联字段
//				String relationChildTable = zj.joinColumn();// 子表：关联字段
//				buf.append("LEFT JOIN ").append(zj.tableName()).append(" ").append(fname).append(" ON a.").append(relationMainTable);
//				buf.append(" = ").append(fname).append(".").append(relationChildTable).append(" ");
//			}
//
//		}
//
//		for (Field f : many2oneSet) {
//			String fname = f.getName();// 用为：sql语句中子表别名
//
//			ManyToOne zj = f.getAnnotation(ManyToOne.class);
//			String relationMainTable = Hump2underline.build(zj.joinColumn());// 主表：关联字段
//			String relationChildTable = zj.joinColumn();// 子表：关联字段
//			buf.append("LEFT JOIN ").append(zj.tableName()).append(" ").append(fname).append(" ON a.").append(relationMainTable);
//			buf.append(" = ").append(fname).append(".").append(relationChildTable).append(" ");
//		}*/
//
//        /* 查询条件构造 */
//        String quaryCondition = StringUtils.join(analyzing(params), " and ");
//        if (StringUtils.isNotBlank(quaryCondition))
//            buf.append(" where ").append(quaryCondition);
//
//
//        if (ObjectUtils.isNotEmpty(page)) {
//            Long total = this.baseMapper.count(buf.toString().replace(select, "").replace("a.*", ""));
//            page.setTotal(total);
//
//            buf.append(" LIMIT ").append((page.getCurrent() - 1) * page.getSize()).append(",").append(page.getSize());
//            page.setRecords(this.baseMapper.findAll(buf.toString()));
//        } else {
//            page = new Page<>();
//            page.setRecords(this.baseMapper.findAll(buf.toString()));
//        }
//        return page;
//    }
//
//    /**
//     * @ver: 1.0.0
//     * @author: weilai
//     * @date: 上午9:50:55,2020年1月31日
//     * @desc: <li>判断并分割：前端传递的quaryString字符串内容
//     */
//    private List<String> analyzing(Map<String, String> params) {
//        List<String> condition = Lists.newArrayList();
//
//        for (Map.Entry<String, String> map : params.entrySet()) {
//            if (StringUtils.isNotBlank(String.valueOf(map.getValue()))) {
//                String str = map.getKey().replace("Q_", "");
//                String[] arry = StringUtils.split(str, "_");
//                buildConditon(arry, map.getValue(), condition);
//            }
//
//        }
//
//        return condition;
//    }
//
//    /**
//     * @ver: 1.0.0
//     * @author: weilai
//     * @date: 上午9:49:05,2020年1月31日
//     * @desc: <li>转换为mybatisPlus之QueryWrapper需求：构建where 条件
//     */
//    private List<String> buildConditon(String[] arry, Object value, List<String> condition) {
//
//        if (arry.length == 2) {
//            String pre = null;
//            String column = null;
//
//            if (StringUtils.contains(arry[0], ".")) {
//                String[] child = StringUtils.split(arry[0], ".");
//                pre = child[0] + ".";
//                column = Hump2underline.build(child[1]);
//            } else {
//                pre = "a.";
//                column = Hump2underline.build(arry[0]);
//            }
//
//            String oper = arry[1];
//            switch (oper) {
//                case "EQ":
//                    condition.add(StringUtils.join(pre, column, " = '", value, "'"));
//                    break;
//                case "NE":
//                    condition.add(StringUtils.join(pre, column, " != '", value, "'"));
//                    break;
//                case "GE":
//                    condition.add(StringUtils.join(pre, column, " >= ", value));
//                    break;
//                case "GT":
//                    condition.add(StringUtils.join(pre, column, " > ", value));
//                    break;
//                case "LE":
//                    condition.add(StringUtils.join(pre, column, " <= ", value));
//                    break;
//                case "LT":
//                    condition.add(StringUtils.join(pre, column, " < ", value));
//                case "BW":
//                    String[] v = StringUtils.split(value.toString(), "~");
//                    condition.add(StringUtils.join(pre, column, " BETWEEN '", v[0], "' AND '", v[1], "'"));
//                    break;
//                case "BW01":// 适合yyyy-MM-dd HH:mm:ss 这样的格式
//                    String[] v1 = StringUtils.split(value.toString(), "~");
//                    condition.add(StringUtils.join(pre, column, " BETWEEN '", v1[0], "' AND '", v1[1], " 23:59:59'"));
//                    break;
//                case "LK":
//                    condition.add(StringUtils.join(pre, column, " like '%", value, "%'"));
//                    break;
//                case "LKRIGHT":
//                    condition.add(StringUtils.join(pre, column, " like '", value, "%'"));
//                    break;
//                case "LKLeft":
//                    condition.add(StringUtils.join(pre, column, " like '%", value, "'"));
//                    break;
//                case "IN":
//                    if (value instanceof String) {
//                        String valueStr = value.toString();
//                        if (StringUtils.isNotBlank(valueStr)) {
//                            String collect = Arrays.asList(valueStr.split(",")).stream().map(s -> "'" + s + "'").collect(Collectors.joining(", "));
//                            condition.add(StringUtils.join(pre, column, " in (", collect, ")"));
//                        }
//                    } else {
//                        condition.add(StringUtils.join(pre, column, " in (", value, ")"));
//                    }
//                    break;
//            }
//
//        }
//
//        return condition;
//    }
//
//    /**
//     * @author: weilai
//     * @Data:2021年1月8日上午11:20:17
//     * @Desc: <li>查找实体类clazz 中带有某个注解[ann]的字段集合
//     * <li>包括其父类上的
//     */
//    private Set<Field> findFieldsByAnnotation(Class<?> clazz, Class<? extends Annotation> ann) {
//        Set<Field> set = Sets.newHashSet();
//        Class<?> c = clazz;
//        while (c != null) {
//            for (Field field : c.getDeclaredFields()) {
//                if (field.isAnnotationPresent(ann))
//                    set.add(field);
//
//            }
//            c = c.getSuperclass();
//        }
//        return set;
//    }
//
//    private void verifySelect(String s) {
//
//        String[] arry = StringUtils.split(s, ",");
//        for (String str : arry) {
//            String[] size = StringUtils.split(str, ".");
//            Assert.isTrue(size.length == 2, "格式错误,【点号分隔符‘.’】有且只能有一个：" + str);
//        }
//    }
//
//    /**
//     * @author: weilai
//     * @Data:2021年1月11日下午8:31:32
//     * @Desc: <li>分析 ParmModel.quaryString 格式，获取“.”后面的字符串：即子表在sql中的别名
//     * <li>ps: 正常运行建立在 格式正确的基础上，这里没有做容错判断
//     */
//    private List<String> selectChild(Map<String, String> params) {
//        List<String> list = Lists.newArrayList();
//        for (Map.Entry<String, String> map : params.entrySet()) {
//            if (StringUtils.isNotBlank(String.valueOf(map.getValue()))) {
//                String str = map.getKey().replace("Q_", "");
//                String[] arry = StringUtils.split(str, "_");
//                if (StringUtils.contains(arry[0], "."))
//                    list.add(StringUtils.substringBefore(arry[0], "."));
//            }
//        }
//        return list;
//    }
//
//    /**
//     * @author: weilai
//     * @Data: 2021年1月30日上午10:59:28
//     * @Desc: <li>把map的key转换为 驼峰命名格式
//     */
//    private List<T> lineToHump(List<T> list, Class<T> entity) {
//        List<T> result = Lists.newArrayList();
//        try {
//            for (T t : list) {
//                T model = entity.getDeclaredConstructor().newInstance();
//
//                map2BeanUtil(model, (HashMap<String, Object>) t);
//
//                result.add(model);
//            }
//        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
//
//    /**
//     * @author:weilai
//     * @Data:2020-8-1914:32:15
//     * @Desc: <li>因为查询数据库返回的结果是Map类型, 此Map的键值是数据表字段名称，
//     * <li>而实体bean中字段可能 用TableField修饰后和数据表字段 有可能，有可能不对应，所以没法
//     * <li>使用BeanUtils.populate(bean, Map),只能自己写方法
//     */
//    private Object map2BeanUtil(T obj, HashMap<String, Object> map) {
//        Object value = null;
//        Map<String, Method> methodMap = Maps.newHashMap();
//        Method[] methods = obj.getClass().getMethods();
//
//        for (Method m : methods)
//            methodMap.put(m.getName(), m);
//
//        Field[] fields = FieldUtils.getAllFields(obj.getClass());
//
//        for (Field fd : fields) {// obj是javaBean,遍历其属性
//            TableId td = fd.getAnnotation(TableId.class);
//            TableField tf = fd.getAnnotation(TableField.class);
//
//            if (ObjectUtils.isNotEmpty(tf))
//                value = map.get(tf.value());// 有TableField修饰
//            else if (ObjectUtils.isNotEmpty(td))
//                value = map.get(td.value());// 有TableId修饰
//            else
//                value = map.get(Hump2underline.build(fd.getName()));// 没有TableField修饰，则采用驼峰命名
//
//            Method method = methodMap.get("set" + StringUtils.capitalize(fd.getName()));
//
//            if (ObjectUtils.isNotEmpty(value) && ObjectUtils.isNotEmpty(method)) {
//                try {
//                    ReflectionUtils.invokeMethod(method, obj, value);// XXXSet<对象赋值>
//                } catch (Exception e) {
//                    System.err.println(method + "," + value.getClass());
//                }
//            }
//        }
//
//        return obj;
//    }
//}
