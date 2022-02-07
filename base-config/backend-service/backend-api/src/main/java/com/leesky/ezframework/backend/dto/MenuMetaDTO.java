package com.leesky.ezframework.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2022/1/6 下午3:26
 */

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuMetaDTO {

    private String icon;//按钮
    private String title;//中文名称
    private Integer rank;//顺序
    private Boolean showLink;//=true 前台才显示
    private Boolean keepAlive;
    private Integer dynamicLevel;
    private String refreshRedirect;
    private List<String> authority;


    public MenuMetaDTO(String icon, String title) {
        this.showLink = true;
        this.icon = icon;
        this.title = title;
    }
}
