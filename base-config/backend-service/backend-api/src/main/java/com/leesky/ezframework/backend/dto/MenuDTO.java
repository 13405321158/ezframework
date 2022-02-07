package com.leesky.ezframework.backend.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 菜单
 *
 * @author： 魏来
 * @date： 2022/1/6 下午1:20
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuDTO {

    private String name;

    private String path;

    private String redirect;

    private MenuMetaDTO meta;

    private List<MenuDTO> children;

    public MenuDTO(String name, String path, String redirect, MenuMetaDTO meta) {
        this.path = path;
        this.name = name;
        this.redirect = redirect;

        this.meta = meta;
    }

    public MenuDTO(String name, String path, MenuMetaDTO meta) {
        this.path = path;
        this.name = name;

        this.meta = meta;
    }
}
