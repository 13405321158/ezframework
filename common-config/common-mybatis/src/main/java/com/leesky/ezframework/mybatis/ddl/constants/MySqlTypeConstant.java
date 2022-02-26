package com.leesky.ezframework.mybatis.ddl.constants;

import com.leesky.ezframework.mybatis.ddl.annotation.LengthCount;

public class MySqlTypeConstant {

	@LengthCount
	public static final  String INT = "int";
	
	@LengthCount
	public static final  String VARCHAR = "varchar";
	
	@LengthCount(Length=0)
	public static final  String TEXT = "text";

	@LengthCount(Length=0)
	public static final  String MEDIUMTEXT = "mediumtext";

	@LengthCount(Length=0)
	public static final  String LONGTEXT = "longtext";

	@LengthCount(Length=0)
	public static final  String DATETIME = "datetime";
	
	@LengthCount(Length=2)
	public static final  String DECIMAL = "decimal";
	
	@LengthCount(Length=2)
	public static final  String DOUBLE = "double";
	
	@LengthCount
	public static final  String CHAR = "char";
	
	@LengthCount
	public static final  String BIGINT = "bigint";
	
	@LengthCount(Length=1)
	public static final  String BIT = "bit";
	
	@LengthCount(Length=0)
	public static final  String TIMESTAMP = "timestamp";
	
	@LengthCount(Length=0)
	public static final  String DATE = "date";
	
	@LengthCount(Length=0)
	public static final  String TIME = "time";
	
	@LengthCount(Length=0)
	public static final  String FLOAT = "float";

	@LengthCount(Length=0)
	public static final String LONGBLOB="longblob";
}
