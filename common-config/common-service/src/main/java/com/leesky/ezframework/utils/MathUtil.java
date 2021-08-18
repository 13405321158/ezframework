package com.leesky.ezframework.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Objects;

/**
 * 由于Java的简单类型不能够精确的对浮点数进行运算， 这个工具类提供精确的浮点数运算，包括加减乘除和四舍五入。
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0 2018年3月23日 上午10:26:41
 */
public final class MathUtil {
	/**
	 * 默认除法运算精度
	 */
	private static final int DEF_DIV_SCALE = 2;

	/**
	 * 这个类不能实例化
	 */
	private MathUtil() {
	}

	/**
	 * 提供精确的加法运算。
	 *
	 * @param v1 被加数
	 * @param v2 加数
	 * @return 两个参数的和
	 */
	public static Double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的加法运算。
	 *
	 * @param v1 被加数
	 * @param v2 加数
	 * @return 两个参数的和
	 */
	public static BigDecimal add(BigDecimal v1, double v2) {
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return v1.add(b2).setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * 提供精确的减法运算。
	 *
	 * @param v1 被减数
	 * @param v2 减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 *
	 * @param v1 被减数
	 * @param v2 减数
	 * @return 两个参数的差
	 */
	public static BigDecimal sub(BigDecimal v1, double v2) {
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return v1.subtract(b2).setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * @author: zhangxin
	 * @date: 2020/12/7 15:50
	 * @desc: <li>  两个参数相乘返回四舍五入的两位小数
	 */
	public static BigDecimal multiply(BigDecimal v1, int v2, int decimal) {
		BigDecimal b2 = new BigDecimal(v2);
		return v1.multiply(b2).setScale(decimal, RoundingMode.HALF_UP);
	}

	/**
	 * @author: zhangxin
	 * @date: 2020/12/7 15:50
	 * @desc: <li>  两个参数相除返回bigdecimal
	 */
	public static BigDecimal divide(BigDecimal v1, double v2, int decimal) {

		return v1.divide(new BigDecimal(v2), decimal, RoundingMode.HALF_UP);
	}

	/**
	 * @author: zhangxin
	 * @date: 2020/12/7 15:50
	 * @desc: <li>  两个参数相除返回bigdecimal
	 */
	public static BigDecimal divideDown(BigDecimal v1, double v2, int decimal) {

		return v1.divide(new BigDecimal(v2), decimal,  RoundingMode.DOWN);
	}

	/**
	 * 提供精确的乘法运算。
	 *
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @return 两个参数的积
	 */
	public static Double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时， 精确到小数点以后10位，以后的数字四舍五入。
	 *
	 * @param v1 被除数
	 * @param v2 除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。 当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
	 *
	 * @param v1    被除数
	 * @param v2    除数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 *
	 * @param v     需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 金额转分
	 *
	 * @param money
	 * @return
	 */
	public static String toFen(Double money) {
		BigDecimal fen = BigDecimal.valueOf(money).multiply(new BigDecimal(100));
		NumberFormat nFormat = NumberFormat.getNumberInstance();
		nFormat.setMaximumFractionDigits(0);
		nFormat.setGroupingUsed(false);
		return nFormat.format(fen);
	}

	/**
	 * 
	 * 
	 * @Author:weilai
	 * @Data:2020年6月23日上午9:27:28
	 * @Desc:
	 *        <li>int a = bigdemical.compareTo(bigdemical2)
	 *        <li>a = -1,表示bigdemical小于bigdemical2；
	 *        <li>a = 0,表示bigdemical等于bigdemical2；
	 *        <li>a = 1,表示bigdemical大于bigdemical2；
	 *
	 */
	public static Boolean aGTb(Double a, Double b) {
		BigDecimal a1 = new BigDecimal(a);
		BigDecimal b1 = new BigDecimal(b);

		return a1.compareTo(b1) >= 0 ? true : false;
	}

	public static Boolean aGTb(BigDecimal a, Double b) {
		BigDecimal b1 = new BigDecimal(b);
		return a.compareTo(b1) >= 0 ? true : false;
	}

	/**
	 * @author: zhangxin
	 * @date: 2020/12/8 14:33
	 * @desc: <li>  将Object 转换为 BigDecimal
	 */
	public static BigDecimal getBigDecimal(Object value) {
		BigDecimal ret = null;
		if (value != null) {
			if (value instanceof BigDecimal) {
				ret = (BigDecimal) value;
			} else if (value instanceof String) {
				ret = new BigDecimal((String) value);
			} else if (value instanceof BigInteger) {
				ret = new BigDecimal((BigInteger) value);
			} else if (value instanceof Number) {
				ret = new BigDecimal(((Number) value).doubleValue());
			} else {
				throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal.");
			}
		}
		return ret;
	}


	/**
	 * 转百分比，百分比小数点最多2位，四舍五入
	 * 例如：
	 * “0.01” -> "1%"
	 * “0.0123” -> "1.23%"
	 * “0.01234” -> "1.23%"
	 * “0.01235” -> "1.24%"
	 * “1.0123” -> "101.12%"
	 * @param bigDecimal
	 * @return
	 */
	public static String percentFormat(BigDecimal bigDecimal){
		if(Objects.isNull(bigDecimal)){
			return null;
		}

		NumberFormat percent = NumberFormat.getPercentInstance();  //建立百分比格式化引用
		percent.setMaximumFractionDigits(2); //百分比小数点最多2位,四舍五入
		return  percent.format(bigDecimal);
	}
}