package com.leesky.ezframework.backend.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
public class MenuDTO implements Serializable {

    private static final long serialVersionUID = -301839688881914976L;

    private String id;
    private String name;
    private String path;
    private int parentId;
    private String icon;
    private String type;
    private int sort;
    private MetaBean meta;
    private String component;
    private boolean hidden;
    private String redirect;
    private boolean alwaysShow;
    private String typeName;
    private String createTime;
    private List<MenuDTO> children;

    @Data
    public static class MetaBean implements Serializable {

        private static final long serialVersionUID = -8106972197895351290L;
        private String title;
        private String icon;
        private boolean breadcrumb;
    }


}
