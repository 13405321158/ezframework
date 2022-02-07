package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.*;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import com.leesky.ezframework.order.mapper.*;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.Set;


@Getter
@Setter
@ApiModel("老公表")
@TableName("cbm_man")
public class ManModel extends BaseUuidModel {

    private static final long serialVersionUID = 592676841068359256L;

    private String name;

    private String laopoId;

    private String cardId;

    private String companyId;

    @OneToMany
    @TableField(exist = false)
    @JoinColumn(referencedColumnName = "lao_han_id")
    @EntityMapper(targetMapper = ItelMapper.class, entityClass = TelModel.class)
    private Set<TelModel> tels;

    @OneToMany
    @TableField(exist = false)
    @JoinColumn(referencedColumnName = "lao_han_id")
    @EntityMapper(targetMapper = IchildMapper.class, entityClass = ChildModel.class)
    private List<ChildModel> childs;

    @OneToOne
    @TableField(exist = false)
    @JoinColumn(name = "laopo_id")
    @EntityMapper(targetMapper = IwomanMapper.class, entityClass = WomanModel.class)
    private WomanModel laoPo;

    @OneToOne
    @TableField(exist = false)
    @JoinColumn(name = "card_id")
    @EntityMapper(targetMapper = IidCardMapper.class, entityClass = IdCardModel.class)
    private IdCardModel idCard;

    @ManyToOne
    @TableField(exist = false)
    @JoinColumn(name = "company_id")
    @EntityMapper(targetMapper = IcompanyMapper.class, entityClass = CompanyModel.class)
    private CompanyModel company;

    public ManModel() {
        this.name = "man_" + RandomStringUtils.randomAlphabetic(4);
    }


    public ManModel(WomanModel laoPo) {

        this.laoPo = laoPo;
        this.name = RandomStringUtils.randomAlphabetic(4);
    }

    public ManModel(CompanyModel company, String name) {
        this.name = name;
        this.company = company;
    }
}
