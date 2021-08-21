/*
 * @author:weilai
 * @Data:2020-8-1816:20:16
 * @Org:Sentury Co.,ltd.
 * @Department:Domestic Sales,Tech Center
 */
package com.leesky.ezframework.ddl.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



// 该注解用于方法声明
@Target(ElementType.FIELD)
// VM将在运行期也保留注释，因此可以通过反射机制读取注解的信息
@Retention(RetentionPolicy.RUNTIME)
// 将此注解包含在javadoc中
@Documented
// 允许子类继承父类中的注解
@Inherited
public @interface LengthCount{

	/**
	 * 默认是1，0表示不需要设置，1表示需要设置一个，2表示需要设置两个
	 * 
	 * @return 默认是1，0表示不需要设置，1表示需要设置一个，2表示需要设置两个
	 */
	
	public int Length() default 1;
	
}
