/*
 * @作者: 魏来
 * @日期: 2021年8月23日  下午4:02:39
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.mybatis.save;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.leesky.ezframework.mybatis.enums.RelationType;
import com.leesky.ezframework.mybatis.mapper.IleeskyMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SaveHandler<T> {

	private final One2oneHandler<T> o2oHandler;

	private final One2manyHandler<T> o2mHandler;

	private final Many2oneHandler<T> m2oHandler;

	private final Many2manyHandler<T> m2mHandler;

	/**
	 * @author: weilai
	 * @Data: 2020-8-1910:44:25
	 * @Desc: 遍历实体类的中的 one2one、many2many、many2one、one2many
	 * @Desc: 关联关系 分为主表和从表，含义参见各自类说明
	 * @Desc: 先存储从表后存储主表，因为存储完毕后实体类才有主键，才能把得到到主键 赋值给 主表中对应字段（非主键关联谁先谁后存储，无所谓）
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	public void relationship(List<T> entity, IleeskyMapper ibaseMapper, Map<String, String[]> entityMap) {
		T t = entity.get(0);

		try {
			String entityName = t.getClass().getName() + ".";

			String[] o2o = entityMap.get(entityName + RelationType.ONETOONE.name());
			String[] m2o = entityMap.get(entityName + RelationType.MANYTOONE.name());
			String[] o2m = entityMap.get(entityName + RelationType.ONETOMANY.name());
			String[] m2m = entityMap.get(entityName + RelationType.MANYTOMANY.name());

			if (ArrayUtils.isNotEmpty(o2o))
				o2oHandler.handler(o2o, t, ibaseMapper);

			if (ArrayUtils.isNotEmpty(m2m))
				m2mHandler.handler(m2m, t, ibaseMapper);

			if (ArrayUtils.isNotEmpty(o2m))
				o2mHandler.handler(o2m, t, ibaseMapper);

			if (ArrayUtils.isNotEmpty(m2o))
				m2oHandler.handler(m2o, t, ibaseMapper);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
