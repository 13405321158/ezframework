/*
 * @author:weilai
 * @Data:2020年4月3日下午6:22:39
 * @Org:Sentury Co., ltd.
 * @Deparment:Domestic Sales, Tech Center
 * @Desc:<li>描述此类的作用
 */
package com.leesky.ezframework.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan("com.leesky.ezframework.*.mapper")
public class MybatisPlusConfig {

    /**
     * @作者: 魏来
     * @日期: 2021/8/21  上午10:36
     * @描述:  v3.4.3.1 配置
     **/
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));//分页插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());//乐观锁插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());//防止全表更新与删除
        return interceptor;
    }


}
