package com.leesky.ezframework.order.model;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.ManyToOne;
import com.leesky.ezframework.mybatis.annotation.OneToMany;
import com.leesky.ezframework.mybatis.annotation.OneToOne;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import com.leesky.ezframework.order.mapper.IchildMapper;
import com.leesky.ezframework.order.mapper.IcompanyMapper;
import com.leesky.ezframework.order.mapper.IidCardMapper;
import com.leesky.ezframework.order.mapper.ItelMapper;
import com.leesky.ezframework.order.mapper.IwomanMapper;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
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
    @EntityMapper(targetMapper = ItelMapper.class)
    private Set<TelModel> tels;

    @OneToMany
    @TableField(exist = false)
    @JoinColumn(referencedColumnName = "lao_han_id")
    @EntityMapper(targetMapper = IchildMapper.class)
    private List<ChildModel> childs;

    @OneToOne
    @TableField(exist = false)
    @JoinColumn(name = "laopo_id")
    @EntityMapper(targetMapper = IwomanMapper.class)
    private WomanModel laoPo;

    @OneToOne
    @TableField(exist = false)
    @JoinColumn(name = "card_id")
    @EntityMapper(targetMapper = IidCardMapper.class)
    private IdCardModel idCard;

    @ManyToOne
    @TableField(exist = false)
    @JoinColumn(name = "company_id")
    @EntityMapper(targetMapper = IcompanyMapper.class)
    private CompanyModel companyModel;

    public ManModel() {
        this.name = "man_" + RandomStringUtils.randomAlphabetic(4);
    }


    public ManModel(WomanModel laoPo) {

        this.laoPo = laoPo;
        this.name = RandomStringUtils.randomAlphabetic(4);
    }

    public ManModel(CompanyModel companyModel, String name) {
        this.name = name;
        this.companyModel = companyModel;
    }
}
