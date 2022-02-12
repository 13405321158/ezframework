/**
 * 通用dto
 *
 * @author： 魏来
 * @date： 2021/12/10 下午3:20
 */
package com.leesky.ezframework.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true) // 忽略前台传递过来不认识的字段,避免springmvc报错
public class CommonDTO {

    private String pid;//父亲id

    private List<String> cid;//子id

    private  Object ext;

    private Map<String,Object> obj;
}
