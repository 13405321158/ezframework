/*
 * @作者: 魏来
 * @日期: 2021年10月19日  下午4:00:52
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:  
 */
package com.leesky.ezframework.mybatis.utils;

/**
 * 类功能说明：
 * <li></li>
 */
public class MapperUtils {
	public static String e1="\"";
	public static String e2="\">";
	public static String b="<mapper namespace=\"";
	
	public static String r1="    <resultMap type=\"";
	public static String r11=" id=\"xmlResult\">";
	public static String r2="    </resultMap>";
	
	//一对一
	public static String association1="        <association property=\"";
	public static String association2=" column=\"";
	public static String association3="  select=\"";
	public static String association4=".selectById\" fetchType=\"lazy\"/>";
	
	//一对多
	public static String collection1="        <collection property=\"";
	public static String collection2=" column=\"";
	public static String collection3=" ofType=\"";
	public static String collection4=" select=\"";
	public static String collection5=" fetchType=\"lazy\"/>";
	
	public static String sql1="    <sql id=\"tableName\">";
	public static String sql2="</sql>";
	
	public static String c1="<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n"
			+ "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">";
	
	
	
	
	public static String c2="\r\n"
			+ "    <select id=\"selectById\" parameterType=\"String\" resultMap=\"xmlResult\">\r\n"
			+ "        select * from\r\n"
			+ "        <include refid=\"tableName\"/>\r\n"
			+ "        where id = #{id}\r\n"
			+ "    </select>\r\n"
			+ "    <select id=\"selectList\" parameterType=\"com.leesky.ezframework.mybatis.query.QueryFilter\" resultMap=\"xmlResult\">\r\n"
			+ "        <choose>\r\n"
			+ "            <when test=\"ew.param!=null\">\r\n"
			+ "                select ${ew.sqlSelect} from\r\n"
			+ "                <include refid=\"tableName\"/>\r\n"
			+ "                where\r\n"
			+ "                <foreach collection=\"ew.op\" item=\"value\" separator=\" and \">${value}</foreach>\r\n"
			+ "            </when>\r\n"
			+ "            <otherwise>\r\n"
			+ "                select * from\r\n"
			+ "                <include refid=\"tableName\"/>\r\n"
			+ "            </otherwise>\r\n"
			+ "        </choose>\r\n"
			+ "    </select>\r\n"
			+ "    <select id=\"selectBatchIds\" parameterType=\"java.util.Collection\" resultMap=\"xmlResult\">\r\n"
			+ "        <if test=\"coll!=null\">\r\n"
			+ "            select * from\r\n"
			+ "            <include refid=\"tableName\"/>\r\n"
			+ "            where id in (\r\n"
			+ "            <foreach collection=\"coll\" item=\"value\" separator=\",\">\r\n"
			+ "                #{value}\r\n"
			+ "            </foreach>\r\n"
			+ "            )\r\n"
			+ "        </if>\r\n"
			+ "    </select>\r\n"
			+ "    <select id=\"selectPage\" resultMap=\"xmlResult\">\r\n"
			+ "        select ${ew.sqlSelect} from\r\n"
			+ "        <include refid=\"tableName\"/>\r\n"
			+ "        where\r\n"
			+ "        <foreach collection=\"ew.op\" item=\"value\" separator=\" and \">${value}</foreach>\r\n"
			+ "    </select>\r\n"
			+ "\r\n"
			+ "</mapper>";
	
	
	public static String c3="</mapper>";
	
	
	public static String select1="    <select id=\"";
	public static String select2="\" parameterType=\"String\" resultMap=\"xmlResult\">";
	public static String select3="        select * from <include refid=\"tableName\"/> where ";
	public static String select4=" = #{id}\n"
			+ "    </select>";
}
