package com.leesky.ezframework.mybatis.result;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Maps;
import com.leesky.ezframework.mybatis.condition.FieldCondition;
import com.leesky.ezframework.mybatis.enums.FieldCollectionType;
import lombok.Data;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.LazyLoader;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

@Data
public class OneToManyResult<T, E> {


	private List<T> list;
	private Field[] fields;
	private Collection<E> CollectionAll;
	private boolean lazy;
	private String fieldCode;
	private String refColumn;
	private BaseMapper<E> mapperE;
	private Map<String, BaseMapper<E>> mapperMap;
	private FieldCollectionType fieldCollectionType;
	private List<Serializable> columnPropertyValueList;

	private Map<String, String> columnPropertyMap;
	private Map<String, String> refColumnPropertyMap;

	private FieldCondition<T> fc;
	private Map<String, Boolean> isExeSqlMap;
	private Map<String, Collection<E>> collectionMap;

	public OneToManyResult(Field[] fields) {
		isExeSqlMap = Maps.newHashMap();
		collectionMap = Maps.newHashMap();
		for (Field f : fields) {
			isExeSqlMap.put(f.getName(), false);
			collectionMap.put(f.getName(), null);
		}
	}

	public void handle(Field field) {
		List<E> listAll = null;
		if (!lazy) {
			if (fieldCollectionType == FieldCollectionType.SET) {
				Set<E> setAll = (Set<E>) CollectionAll;
				if (setAll != null) {
					listAll = new ArrayList<E>();
					for (E e : setAll) {
						listAll.add(e);
					}
				}
			} else {
				listAll = (List<E>) CollectionAll;
			}
		}

		if (listAll != null && listAll.size() > 0) {
			for (int j = 0; j < list.size(); j++) {
				T entity = list.get(j);
				String columnProperty = columnPropertyMap.get(fieldCode);
				String refColumnProperty = refColumnPropertyMap.get(fieldCode);

				Collection<E> listForThisEntity = new ArrayList<E>();
				if (fieldCollectionType == FieldCollectionType.SET) {
					listForThisEntity = new HashSet<E>();
				}

				for (int k = 0; k < listAll.size(); k++) {
					E entityE = listAll.get(k);
					Field entityField ;
					Field entity2Field ;
					try {
						entityField = entity.getClass().getDeclaredField(columnProperty);
						entityField.setAccessible(true);
						Object columnValue = entityField.get(entity);

						entity2Field = entityE.getClass().getDeclaredField(refColumnProperty);
						entity2Field.setAccessible(true);
						Object refCoumnValue = entity2Field.get(entityE);

						if (columnValue!=null && refCoumnValue!=null && columnValue.toString().equals(refCoumnValue.toString())) {
							listForThisEntity.add(entityE);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}

				try {
					if (listForThisEntity != null && listForThisEntity.size() > 0) {
						field.set(entity, listForThisEntity);
					}
				} catch (

						Exception e) {
					e.printStackTrace();
				}
			} // end loop-entity
		} // end if

	}

	public void handleLazy(Field field) {
		final BaseMapper<E> mapper = (BaseMapper<E>) this.mapperE;

		if (fieldCollectionType == FieldCollectionType.SET) {
			for (int i = 0; i < this.list.size(); i++) {
				T entity = list.get(i);

				@SuppressWarnings("unchecked")
				Set<E> setForThisEntityProxy = (Set<E>) Enhancer.create(Set.class, new LazyLoader() {
					@Override
					public Set<E> loadObject() throws Exception {
						if (isExeSqlMap.get(field.getName()) == false) {
							if (columnPropertyValueList.size() == 1) {
								collectionMap.put(field.getName(), mapper.selectList(
										new QueryWrapper<E>().eq(refColumn, columnPropertyValueList.get(0))));
							} else {
								collectionMap.put(field.getName(), mapper
										.selectList(new QueryWrapper<E>().in(refColumn, columnPropertyValueList)));
							}
							isExeSqlMap.put(field.getName(), true);
						}

						List<E> listAll = (List<E>) collectionMap.get(field.getName());

						String columnProperty = columnPropertyMap.get(fieldCode);
						String refColumnProperty = refColumnPropertyMap.get(fieldCode);

						Collection<E> listForThisEntity = new ArrayList<E>();
						if (fieldCollectionType == FieldCollectionType.SET) {
							listForThisEntity = new HashSet<E>();
						}

						for (int k = 0; k < listAll.size(); k++) {
							E entityE = listAll.get(k);
							Field entityField = null;
							Field entity2Field = null;
							try {
								entityField = entity.getClass().getDeclaredField(columnProperty);
								entityField.setAccessible(true);
								Object columnValue = entityField.get(entity);

								entity2Field = entityE.getClass().getDeclaredField(refColumnProperty);
								entity2Field.setAccessible(true);
								Object refCoumnValue = entity2Field.get(entityE);

								if (columnValue != null && refCoumnValue!=null && columnValue.toString().equals(refCoumnValue.toString())) {
									listForThisEntity.add(entityE);
								}
							} catch (Exception e1) {
								e1.printStackTrace();
							}

						}

						return (Set<E>) listForThisEntity;
					}

				});

				// 设置代理
				try {
					field.set(entity, setForThisEntityProxy);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			for (int i = 0; i < this.list.size(); i++) {
				T entity = list.get(i);

				@SuppressWarnings("unchecked")
				List<E> listForThisEntityProxy = (List<E>) Enhancer.create(List.class, new LazyLoader() {
					@Override
					public List<E> loadObject() throws Exception {
						if (isExeSqlMap.get(field.getName()) == false) {
							if (columnPropertyValueList.size() == 1) {
								collectionMap.put(field.getName(), mapper.selectList(
										new QueryWrapper<E>().eq(refColumn, columnPropertyValueList.get(0))));
							} else {
								collectionMap.put(field.getName(), mapper
										.selectList(new QueryWrapper<E>().in(refColumn, columnPropertyValueList)));
							}
							isExeSqlMap.put(field.getName(), true);
						}

						List<E> listAll = (List<E>) collectionMap.get(field.getName());

						String columnProperty = columnPropertyMap.get(fieldCode);
						String refColumnProperty = refColumnPropertyMap.get(fieldCode);

						Collection<E> listForThisEntity = new ArrayList<E>();
						if (fieldCollectionType == FieldCollectionType.SET) {
							listForThisEntity = new HashSet<E>();
						}

						for (int k = 0; k < listAll.size(); k++) {
							E entityE = listAll.get(k);
							Field entityField = null;
							Field entity2Field = null;
							try {
								entityField = entity.getClass().getDeclaredField(columnProperty);
								entityField.setAccessible(true);
								Object columnValue = entityField.get(entity);

								entity2Field = entityE.getClass().getDeclaredField(refColumnProperty);
								entity2Field.setAccessible(true);
								Object refCoumnValue = entity2Field.get(entityE);

								if (columnValue != null && refCoumnValue!=null && columnValue.toString().equals(refCoumnValue.toString())) {
									listForThisEntity.add(entityE);
								}
							} catch (Exception e1) {
								e1.printStackTrace();
							}

						}

						return (List<E>) listForThisEntity;
					}

				});

				// 设置代理
				try {
					field.set(entity, listForThisEntityProxy);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static <E> List<E> getListResult(Field field) {
		return null;
	}



}
