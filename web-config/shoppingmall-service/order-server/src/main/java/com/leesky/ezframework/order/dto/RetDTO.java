/*
 * @作者: 魏来
 * @日期: 2021/10/26  上午8:43
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.order.dto;

import lombok.Data;

import java.util.List;

/**
 * <li>描述:
 */
@Data
public class RetDTO {

    private String id;
    private String name;

    private String laopoId;

    private String companyId;


    private String laopname;

    private WomanDTO laoPo;

    private CompanyDTO company;

    private IdCardDTO idCard;

    private List<ChildDTO> childs;

}
