package com.leesky.ezframework.mybatis.condition;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.ObjectFactory;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.InverseJoinColumn;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.ManyToMany;
import com.leesky.ezframework.mybatis.annotation.ManyToOne;
import com.leesky.ezframework.mybatis.annotation.OneToMany;
import com.leesky.ezframework.mybatis.annotation.OneToOne;

import lombok.Data;

@Data
public class FieldCondition<T> {
	private final ObjectFactory<SqlSession> factory;

	private T entity;
	private Field field;

	private String name;
	private Boolean isCollection;

	private Class<?> fieldClass;

	private TableId tableId;
	private Field fieldOfTableId;
	private TableField tableField;

	private TableId refTableId;
	private Field fieldOfRefTableId;
	private TableField refTableField;

	private TableId inverseTableId;
	private Field fieldOfInverseTableId;
	private TableField inverseTableField;

	private OneToMany oneToMany;
	private OneToOne oneToOne;
	private ManyToOne manyToOne;
	private ManyToMany manyToMany;

	private JoinColumn joinColumn;

	private InverseJoinColumn inverseJoinColumn;
	private EntityMapper entityMapper;
	private Class<?> mapperClass;
	private Class<?> joinTableMapperClass;

	private final String ex = "%s call setter %s is not correct!";

	public FieldCondition(T entity, Field field, boolean fetchEager, ObjectFactory<SqlSession> factory) {
		this.factory = factory;

		this.entity = entity;
		this.field = field;
		this.field.setAccessible(true);

		this.name = field.getName();
		this.isCollection = field.getType() == List.class || field.getType() == Set.class;
		this.fieldClass = field.getType();
		if (isCollection) {
			Type genericType = field.getGenericType();
			if (genericType instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) genericType;
				this.fieldClass = (Class<?>) pt.getActualTypeArguments()[0];
			}
		}

		this.tableField = field.getAnnotation(TableField.class);
		this.oneToMany = field.getAnnotation(OneToMany.class);
		this.oneToOne = field.getAnnotation(OneToOne.class);
		this.manyToOne = field.getAnnotation(ManyToOne.class);
		this.manyToMany = field.getAnnotation(ManyToMany.class);

		this.joinColumn = field.getAnnotation(JoinColumn.class);

		this.inverseJoinColumn = field.getAnnotation(InverseJoinColumn.class);
		this.entityMapper = field.getAnnotation(EntityMapper.class);

		TableIdCondition tidCondition = new TableIdCondition(entity.getClass());
		this.tableId = tidCondition.getTableId();
		this.fieldOfTableId = tidCondition.getFieldOfTableId();

		if (inverseJoinColumn != null) {
			TableIdCondition tidConditionInverse = new TableIdCondition(fieldClass);
			this.inverseTableId = tidConditionInverse.getTableId();
			this.fieldOfInverseTableId = tidConditionInverse.getFieldOfTableId();
		}

		if (!isCollection) {
			TableIdCondition tidConditionRef = new TableIdCondition(fieldClass);
			this.refTableId = tidConditionRef.getTableId();
			this.fieldOfRefTableId = tidConditionRef.getFieldOfTableId();
		}

		this.mapperClass = null;
		if (entityMapper != null && entityMapper.targetMapper() != void.class) {
			mapperClass = entityMapper.targetMapper();
		} else {
			String entityName = this.getFieldClass().getSimpleName().replace("Model", "");
			Collection<Class<?>> mappers = this.factory.getObject().getConfiguration().getMapperRegistry().getMappers();
			for (Class<?> mapperClz : mappers) {
				String mapperClassName = mapperClz.getSimpleName();
				if (mapperClassName.equalsIgnoreCase("i" + StringUtils.uncapitalize(entityName) + "Mapper")) {
					mapperClass = mapperClz;
					break;
				}
			}

			if (mapperClass == null) {
				throw new RuntimeException(
						"[Class: FieldCondition=>FieldCondition(T entity, Field field, boolean fetchEager, ObjectFactory<SqlSession> factory)],RelationException By: load Class(Mapper Interface):"
								+ this.getFieldClass().getSimpleName() + "Mapper");
			}
		}

	}

}
