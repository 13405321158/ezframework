package com.leesky.ezframework.query;

import lombok.Data;

import java.util.List;

/**
 * 一对多关系 通用dto
 *
 * @author： 魏来
 * @date： 2021/12/10 下午3:20
 */
@Data
public class CommonDTO {

    private String pid;//父亲id

    private List<String> cid;//子id
}
