package com.leesky.ezframework.mybatis.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.leesky.ezframework.mybatis.annotation.*;
import com.leesky.ezframework.mybatis.save.SaveHandler;
import com.leesky.ezframework.mybatis.utils.MapperUtils;
import com.leesky.ezframework.utils.Hump2underline;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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

import java.io.File;
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
	
	private List<String> list03 = Lists.newArrayList();
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
					if (ObjectUtils.isNotEmpty(annotation)) {
						autoMapperBean(clazz.getName());
						//creatMapperXml(clazz);
					}
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

	public <T> void insert(T t, IeeskyMapper ibaseMapper, SaveHandler<T> saveHandler) {

		if (ObjectUtils.isNotEmpty(t))
			saveHandler.relationship(Lists.newArrayList(t), ibaseMapper, entityMap);

	}

	/**
	 * <li>批量插入实体</li>
	 *
	 * @作者: 魏来
	 * @日期: 2021年9月26日 上午11:15:49
	 */
	public <T> void insert(List<T> t, IeeskyMapper ibaseMapper, SaveHandler<T> saveHandler) {

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

	public void creatMapperXml(Class<?> clz) {

		Field[] fs = clz.getDeclaredFields();

		String modelPath = clz.getName();
		String tableName = clz.getAnnotation(TableName.class).value();
		String m = "I" + StringUtils.uncapitalize(clz.getSimpleName().replace("Model", "Mapper"));
		String mapperPath = StringUtils.replace(modelPath, "model." + clz.getSimpleName(), "mapper." + m);

		try {

			List<String> c = Lists.newArrayList(MapperUtils.c1);
			List<String> e = Lists.newArrayList(MapperUtils.c1);

			c.add(MapperUtils.b + mapperPath + MapperUtils.e2);
			e.add(MapperUtils.b + mapperPath + MapperUtils.e2);

			c.add(MapperUtils.r1 + modelPath + MapperUtils.e1 + MapperUtils.r11);
			boolean flag = getRelate(fs, c, clz.getName());

			c.add(MapperUtils.r2);

			c.add(MapperUtils.sql1 + tableName + MapperUtils.sql2);

			c.addAll(list03);
			
			c.add(MapperUtils.c2);
			e.add(MapperUtils.c3);


			if (flag)
				FileUtils.writeLines(new File("src/main/resources/mybatis/" + m + ".xml"), c);
			else
				FileUtils.writeLines(new File("src/main/resources/mybatis/" + m + ".xml"), e);
		} catch (IOException e) {

		}

	}

	public boolean getRelate(Field[] fs, List<String> c, String clzName) {
		list03.clear();
		boolean flag = false;
		List<String> list01 = Lists.newArrayList();
		List<String> list02 = Lists.newArrayList();


		for (Field f : fs) {
			try {
				OneToOne o2o = f.getAnnotation(OneToOne.class);
				OneToMany o2m = f.getAnnotation(OneToMany.class);
				ManyToOne m2o = f.getAnnotation(ManyToOne.class);
				ManyToMany m2m = f.getAnnotation(ManyToMany.class);

				if (ObjectUtils.isNotEmpty(o2o) || ObjectUtils.isNotEmpty(m2o)) {
					flag = true;
					list01.add(MapperUtils.association1 + f.getName() + MapperUtils.e1 + MapperUtils.association2

							+ f.getAnnotation(JoinColumn.class).name() + MapperUtils.e1 + MapperUtils.association3

							+ f.getAnnotation(EntityMapper.class).targetMapper().getName() + MapperUtils.association4);
				}
				if (ObjectUtils.isNotEmpty(o2m) || ObjectUtils.isNotEmpty(m2m)) {
					flag = true;
					list02.add(MapperUtils.collection1 + f.getName() + MapperUtils.e1 + MapperUtils.collection2

							+ f.getAnnotation(JoinColumn.class).name() + MapperUtils.e1 + MapperUtils.collection3

							+ f.getAnnotation(EntityMapper.class).entityClass().getName() + MapperUtils.e1

							+ MapperUtils.collection4 + f.getAnnotation(EntityMapper.class).targetMapper().getName()

							+ ".selectBy" + Hump2underline.lineToHump(f.getAnnotation(JoinColumn.class).referencedColumnName()) + MapperUtils.e1

							+ MapperUtils.collection5

					);
				}
				
				if( ObjectUtils.isNotEmpty(m2o)) {
					list03.add(MapperUtils.select1 + "selectBy" + Hump2underline.lineToHump(f.getAnnotation(JoinColumn.class).name()) + MapperUtils.select2);
					list03.add(MapperUtils.select3 + f.getAnnotation(JoinColumn.class).name() + MapperUtils.select4);
				}
			} catch (Exception e) {
				System.err.println("------" + clzName + "中的字段：" + f.getName() + " 缺少相关注解");
			}
		}

		if (CollectionUtils.isNotEmpty(list01))
			c.addAll(list01);

		if (CollectionUtils.isNotEmpty(list02))
			c.addAll(list02);

		return flag;
	}
}
