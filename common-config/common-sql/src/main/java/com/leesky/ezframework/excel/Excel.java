package com.leesky.ezframework.excel;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author:weilai
 * @Data:2020年2月21日下午3:07:23
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc: 标注在实体类中
 */
@Component
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
public @interface Excel {

	boolean isMergedRegion() default false;// 是否合并单元格

	int firstCol() default 0;// 合并起始列index

	int lastCol() default 0;// 合并结束列index

	int value() default 0;// 在单元格中的位置

	boolean isTotal() default false;// 当前字段 是否求和

	String writeTitle() default "";// 当前字段值写入到表头，例如表头中某列标题是 月份，这个月份需要跟进内容添加上数字，如：5月份

	int row() default 0;// 把writeTitle 写入当前定义的行

	int column() default 0;// 把writeTitle 写入当前定义的列

	boolean fixed() default false;// 此属性为true时，根据指定的row和column定位单元格位置赋值

	DataType dataType() default DataType.String;// 数据类型，主要处理数字类型，否则默认写入的是字符类型，会导致公式失效

	String dateFormat() default "yyyy-MM-dd"; //日期格式类型 或者 yyyy-MM-dd HH:mm:ss


	enum DataType {
		String,Number
	}
}
