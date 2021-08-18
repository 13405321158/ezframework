package com.leesky.ezframework.ddl.command;

import lombok.Data;

import java.util.List;

/**
 * 用于存放创建表的字段信息
 *
 * @author sunchenbin, Spet
 * @version 2019/07/06
 */
@Data
public class CreateTableParam implements Cloneable {

	/**
	 * 字段名
	 */
	private String fieldName;

	/**
	 * 字段类型
	 */
	private String fieldType;

	/**
	 * 类型长度
	 */
	private int fieldLength;

	/**
	 * 类型小数长度
	 */
	private int fieldDecimalLength;

	/**
	 * 字段是否非空
	 */
	private boolean fieldIsNull = true;

	/**
	 * 字段是否是主键
	 */
	private boolean fieldIsKey = false;

	/**
	 * 主键是否自增
	 */
	private boolean fieldIsAutoIncrement;

//附加参数
	private boolean addPrama;
	
	private String  comment;//注释
	
	private boolean isKey;

	/**
	 * 该类型需要几个长度（例如，需要小数位数的，那么总长度和小数长度就是2个长度）一版只有0、1、2三个可选值，自动从配置的类型中获取的
	 */
	private int fileTypeLength;

	/**
	 * 值是否唯一
	 */
	private boolean fieldIsUnique;

	/**
	 * 索引名称
	 */
	private String filedIndexName;

	/**
	 * 所以字段列表
	 */
	private List<String> filedIndexValue;

	/**
	 * 唯一约束名称
	 */
	private String filedUniqueName;

	/**
	 * 唯一约束列表
	 */
	private List<String> filedUniqueValue;

	@Override
	public CreateTableParam clone() {
		CreateTableParam createTableParam = null;
		try {
			createTableParam = (CreateTableParam) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return createTableParam;
	}
}
