/*
 * @作者: 魏来
 * @日期: 2021/9/17  上午10:33
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.mybatis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <li>描述:
 */
@Data
@TableName("test_weilai")
public class TestModel {

    private String id;

    private String name;
}
