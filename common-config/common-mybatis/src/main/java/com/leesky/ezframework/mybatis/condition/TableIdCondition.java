package com.leesky.ezframework.mybatis.condition;

import com.baomidou.mybatisplus.annotation.TableId;
import com.google.common.collect.Lists;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Data
public class TableIdCondition {
	private TableId tableId;
	private Field fieldOfTableId;

	public TableIdCondition(Class<?> entityClass) {
		List<Field> fields = getAllField(entityClass);
		for (Field field : fields) {
			if (field.isAnnotationPresent(TableId.class)) {
				tableId = field.getDeclaredAnnotation(TableId.class);
				fieldOfTableId = field;
				break;
			}

		}

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
