package com.leesky.ezframework.mybatis.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.leesky.ezframework.mybatis.annotation.ManyToMany;
import com.leesky.ezframework.mybatis.annotation.ManyToOne;
import com.leesky.ezframework.mybatis.annotation.OneToMany;
import com.leesky.ezframework.mybatis.annotation.OneToOne;
import com.leesky.ezframework.mybatis.save.SaveHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AutoMapper for one2one/one2many/many2many
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class AutoMapper {
	@Autowired
	ObjectFactory<SqlSession> factory;

	protected Map<String, String[]> entityMap = Maps.newHashMap();

	/**
	 * <li>平台中的model包命名规范必须是com.leesky.ezframework.**.model</li>
	 * <li>平台中的model类 必须包含注解：TableName，否则无法自动映射 o2o,o2m,m2o,m2m关系</li>
	 * <li>jar中的 model也能加载</li>
	 *
	 * @作者: 魏来
	 * @日期: 2021/9/17 上午10:44
	 **/
	public AutoMapper(String entityPackages) {
		ResourcePatternResolver rr = new PathMatchingResourcePatternResolver();
		try {
			String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(entityPackages) + "/**/*.class";
			Resource[] resources = rr.getResources(pattern);
			MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(rr);
			for (Resource r : resources) {
				if (r.isReadable()) {
					MetadataReader reader = readerFactory.getMetadataReader(r);
					String className = reader.getClassMetadata().getClassName();
					Class<?> clazz = Class.forName(className);
					TableName annotation = clazz.getAnnotation(TableName.class);
					if (ObjectUtils.isNotEmpty(annotation))
						autoMapperBean(clazz.getName());
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			log.error("读取class失败", e);
		}

	}

	/**
	 * <li>插入一个实体</li>
	 *
	 * @作者: 魏来
	 * @日期: 2021年9月26日 上午11:15:34
	 */

	public <T> void insert(T t, IeeskyMapper ibaseMapper, SaveHandler<T> saveHandler) throws Exception {

		if (ObjectUtils.isNotEmpty(t))
			saveHandler.relationship(Lists.newArrayList(t), ibaseMapper, entityMap);

	}

	/**
	 * <li>批量插入实体</li>
	 *
	 * @作者: 魏来
	 * @日期: 2021年9月26日 上午11:15:49
	 */
	public <T> void insert(List<T> t, IeeskyMapper ibaseMapper, SaveHandler<T> saveHandler) throws Exception {

		if (CollectionUtils.isNotEmpty(t))
			saveHandler.relationship(t, ibaseMapper, entityMap);
	}

	/**
	 * @作者: 魏来
	 * @日期: 2021年10月19日 下午2:54:38
	 */
	private void autoMapperBean(String className) {

		ArrayList<String> o2oFields = Lists.newArrayList();
		ArrayList<String> o2mFields = Lists.newArrayList();
		ArrayList<String> m2oFields = Lists.newArrayList();
		ArrayList<String> m2mFields = Lists.newArrayList();

		try {
			Class<?> clazz = Class.forName(className);

			Field[] fields = clazz.getDeclaredFields();
			if (ArrayUtils.isNotEmpty(fields)) {
				for (Field field : fields) {
					OneToOne o2o = field.getAnnotation(OneToOne.class);
					if (ObjectUtils.isNotEmpty(o2o))
						o2oFields.add(field.getName());

					OneToMany o2m = field.getAnnotation(OneToMany.class);
					if (ObjectUtils.isNotEmpty(o2m))
						o2mFields.add(field.getName());

					ManyToOne m2o = field.getAnnotation(ManyToOne.class);
					if (ObjectUtils.isNotEmpty(m2o))
						m2oFields.add(field.getName());

					ManyToMany m2m = field.getAnnotation(ManyToMany.class);
					if (ObjectUtils.isNotEmpty(m2m))
						m2mFields.add(field.getName());
				}

				if (o2oFields.size() > 0)
					entityMap.put(clazz.getName() + ".ONETOONE", o2oFields.toArray(new String[] {}));

				if (o2mFields.size() > 0)
					entityMap.put(clazz.getName() + ".ONETOMANY", o2mFields.toArray(new String[] {}));

				if (m2oFields.size() > 0)
					entityMap.put(clazz.getName() + ".MANYTOONE", m2oFields.toArray(new String[] {}));

				if (m2mFields.size() > 0)
					entityMap.put(clazz.getName() + ".MANYTOMANY", m2mFields.toArray(new String[] {}));

			}

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Error in scan entity bean");
		}
	}

}
