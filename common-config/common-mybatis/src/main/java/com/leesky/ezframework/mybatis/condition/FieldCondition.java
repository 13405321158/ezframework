package com.leesky.ezframework.mybatis.condition;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.google.common.collect.Sets;
import com.leesky.ezframework.mybatis.annotation.*;
import com.leesky.ezframework.mybatis.enums.FetchType;
import com.leesky.ezframework.mybatis.enums.FieldCollectionType;
import com.leesky.ezframework.mybatis.enums.RelationType;
import com.leesky.ezframework.mybatis.exception.OneToManyException;
import com.leesky.ezframework.mybatis.exception.OneToOneException;
import com.leesky.ezframework.mybatis.exception.RelationException;
import lombok.Data;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.ObjectFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
public class FieldCondition<T> {
    private final ObjectFactory<SqlSession> factory;


    private T entity;
    private Field field;
    private Boolean fetchEager;

    private String name;
    private Boolean isCollection;
    private FieldCollectionType fieldCollectionType;
    private RelationType relationType;
    private Class<?> fieldClass;
    private Boolean isLazy;

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
    private FetchType fetchType;
    private Lazy lazy;
    private JoinColumn joinColumn;
    private JoinColumns joinColumns;
    private JoinTable joinTable;
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
        this.fetchEager = fetchEager;

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

        if (field.getType() == List.class) {
            this.fieldCollectionType = FieldCollectionType.LIST;
        } else if (field.getType() == Set.class) {
            this.fieldCollectionType = FieldCollectionType.SET;
        } else {
            this.fieldCollectionType = FieldCollectionType.NONE;
        }

        this.tableField = field.getAnnotation(TableField.class);
        this.oneToMany = field.getAnnotation(OneToMany.class);
        this.oneToOne = field.getAnnotation(OneToOne.class);
        this.manyToOne = field.getAnnotation(ManyToOne.class);
        this.manyToMany = field.getAnnotation(ManyToMany.class);
        if (oneToMany != null) {
            this.relationType = RelationType.ONETOMANY;
            this.fetchType = oneToMany.fetch();
        } else if (oneToOne != null) {
            this.relationType = RelationType.ONETOONE;
            this.fetchType = oneToOne.fetch();
        } else if (manyToOne != null) {
            this.relationType = RelationType.MANYTOONE;
            this.fetchType = manyToOne.fetch();
        } else if (manyToMany != null) {
            this.relationType = RelationType.MANYTOMANY;
            this.fetchType = manyToMany.fetch();
        }

        this.lazy = field.getAnnotation(Lazy.class);
        this.joinColumn = field.getAnnotation(JoinColumn.class);
        this.joinColumns = field.getAnnotation(JoinColumns.class);
        this.joinTable = field.getAnnotation(JoinTable.class);
        this.inverseJoinColumn = field.getAnnotation(InverseJoinColumn.class);
        this.entityMapper = field.getAnnotation(EntityMapper.class);

        if (fetchEager) {
            isLazy = false;
        } else {
            if (lazy != null) {
                isLazy = lazy.value();

            } else {
                isLazy = fetchType == FetchType.LAZY;

                if (this.oneToOne != null || this.manyToOne != null)
                    this.isLazy = true;
            }
        }

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
            String entityName = this.getFieldClass().getSimpleName();
            Collection<Class<?>> mappers = this.factory.getObject().getConfiguration().getMapperRegistry().getMappers();
            for (Class<?> mapperClz : mappers) {
                String mapperClassName = mapperClz.getSimpleName();
                if (mapperClassName.equalsIgnoreCase(entityName + "Mapper")) {
                    mapperClass = mapperClz;
                    break;
                }
            }

            if (mapperClass == null) {
                throw new RelationException(
                        "[Class: FieldCondition=>FieldCondition(T entity, Field field, boolean fetchEager, ObjectFactory<SqlSession> factory)],RelationException By: load Class(Mapper Interface):"
                                + this.getFieldClass().getSimpleName() + "Mapper");
            }
        }

        this.joinTableMapperClass = null;
        String[] joinMapperNames = new String[]{
                entity.getClass().getSimpleName() + this.getFieldClass().getSimpleName() + "Mapper",
                this.getFieldClass().getSimpleName() + entity.getClass().getSimpleName() + "Mapper"};

        if (joinTable != null) {
            if (joinTable.targetMapper() != null && joinTable.targetMapper() != void.class) {
                joinTableMapperClass = joinTable.targetMapper();
            } else {
                Collection<Class<?>> mappers = this.factory.getObject().getConfiguration().getMapperRegistry()
                        .getMappers();
                boolean isMapperFound = false;
                for (String joinMapperName : joinMapperNames) {
                    if (isMapperFound) {
                        break;
                    }

                    for (Class<?> mapperClz : mappers) {
                        if (mapperClz.getSimpleName().equalsIgnoreCase(joinMapperName)) {
                            isMapperFound = true;
                            joinTableMapperClass = mapperClz;
                            break;
                        }
                    }
                }

                if (!isMapperFound) {
                    throw new RelationException(
                            "[Class: FieldCondition=>FieldCondition(T entity, Field field, boolean fetchEager, ObjectFactory<SqlSession> factory)],RelationException By: load Class(Mapper Interface):"
                                    + entity.getClass().getSimpleName() + this.getFieldClass().getSimpleName()
                                    + "Mapper" + " Or " + this.getFieldClass().getSimpleName()
                                    + entity.getClass().getSimpleName() + "Mapper");
                }

            }
        }

    }


    public <E> void setFieldValueByList(List<E> list) {
        if (list != null) {
            field.setAccessible(true);
            try {
                if (this.getFieldCollectionType() == FieldCollectionType.SET) {
                    // list to set
                    Set<E> set = Sets.newHashSet(list);

                    field.set(entity, set);

                } else {
                    field.set(entity, list);
                }

            } catch (Exception e) {
                throw new OneToManyException(String.format(this.ex, entity, field.getName()));
            }
        }
    }

    public <E> void setFieldValueBySet(Set<E> set) {
        if (set != null) {
            field.setAccessible(true);
            try {
                field.set(entity, set);
            } catch (Exception e) {
                throw new OneToManyException(String.format(this.ex, entity, field.getName()));
            }
        }
    }

    public <E> void setFieldValueByObject(E e) {
        field.setAccessible(true);
        try {
            field.set(entity, e);
        } catch (Exception ex) {
            throw new OneToOneException(String.format(this.ex, entity, field.getName()));
        }
    }
}
