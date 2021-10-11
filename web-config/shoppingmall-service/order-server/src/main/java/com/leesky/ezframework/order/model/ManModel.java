package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.*;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import com.leesky.ezframework.order.mapper.IchildMapper;
import com.leesky.ezframework.order.mapper.IcompanyMapper;
import com.leesky.ezframework.order.mapper.ItelMapper;
import com.leesky.ezframework.order.mapper.IwomanMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;


@Getter
@Setter
/*
 * @AutoLazy(true) 可开启自动延迟加载(默认为false)，对于多个延迟的属性，会触发多次连接（不是一个事务完成）。
 * 当@AutoLazy(false)时，如需要，可手动方式调用
 * initilizeXXX方法来加载@lazy(true)的属性（多个延迟的属性，可以只触发一次连接，在同个事务内未完成）。参见示例中的：
 * t_man_service_001_initialize()
 */
@TableName("cbm_man")
public class ManModel extends BaseUuidModel {

    private static final long serialVersionUID = 592676841068359256L;

    private String name;

    private String laoPoId;

    private String companyId;

    @OneToMany
    @TableField(exist = false)
    @JoinColumn(referencedColumnName = "man_id")
    @EntityMapper(targetMapper = ItelMapper.class)
    private Set<TelModel> telModels;

    @OneToMany
    @TableField(exist = false)
    @JoinColumn(referencedColumnName = "lao_han_id")
	@EntityMapper(targetMapper = IchildMapper.class)
    private List<ChildModel> waWa;

    @OneToOne
    @TableField(exist = false)
    @JoinColumn(name = "lao_po_id")
    @EntityMapper(targetMapper = IwomanMapper.class)
    private WomanModel laoPo;

    @ManyToOne
    @TableField(exist = false)
    @JoinColumn(name = "company_id")
    @EntityMapper(targetMapper = IcompanyMapper.class)
    private CompanyModel companyModel;

    public ManModel() {
    }


    public ManModel(WomanModel laoPo) {

		this.laoPo = laoPo;
		this.name="刘德华";
	}

    public ManModel(CompanyModel companyModel,String name) {
        this.name = name;
        this.companyModel = companyModel;
    }
}
