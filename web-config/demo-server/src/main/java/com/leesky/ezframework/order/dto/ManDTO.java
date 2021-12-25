package com.leesky.ezframework.order.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2021/12/22 上午9:57
 */
@Data
public class ManDTO {


    private String name;

    private String laopoId;

    private String cardId;

    private String companyId;

    private Set<TelDTO> tels;

    private List<ChildDTO> childs;

    private WomanDTO laoPo;

    private IdCardDTO idCard;

    private CompanyDTO company;
}
