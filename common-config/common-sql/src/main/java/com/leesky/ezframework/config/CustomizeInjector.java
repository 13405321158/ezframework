/*
 * @作者: 魏来
 * @日期: 2021年9月1日  下午3:37:46
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.config;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;

public class CustomizeInjector extends DefaultSqlInjector {

	@Override
	public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {

		List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);

		methodList.add(new InsertBatchMethod());

		methodList.add(new UpdateBatchMethod());

		methodList.add(new QueryMethod());


		return methodList;
	}
}
