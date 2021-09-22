package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 *
 */
@Data

/*
 * @AutoLazy(true)  可开启自动延迟加载(默认为false)，对于多个延迟的属性，会触发多次连接（不是一个事务完成）。
         当@AutoLazy(false)时，如需要，可手动方式调用 initilizeXXX方法来加载@lazy(true)的属性（多个延迟的属性，可以只触发一次连接，在同个事务内未完成）。参见示例中的：t_man_service_001_initialize()
 */
@TableName("man")
public class Man {


    @TableId(value = "man_id")
    private Long id;

    private String name;

    private Long laoPoId;

    @TableField("company_id")
    private Long companyId;


    @OneToMany
    @TableField(exist = false)
    @JoinColumn(name = "man_id", referencedColumnName = "man_id")
    private Set<Tel> tels;


    @OneToMany
    @TableField(exist = false)
    @JoinColumn(name = "man_id", referencedColumnName = "lao_han_id")
    private List<Child> waWa;


    @OneToOne
    @TableField(exist = false)
    @JoinColumn(name = "lao_po_id", referencedColumnName = "woman_id")
    private Woman laoPo;


    @ManyToOne
    @TableField(exist = false)
    @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    private Company company;


    public Man() {
    }

    public Man(Long id) {
        this.id = id;
    }

    public Man(String name) {
        this.name = name;
    }

    public Man(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toLazyString() {
        return "Man [id=" + id + ", name=" + name + ", laoPoId=" + laoPoId + ", laoPo=" + laoPo + ", companyId="
                + companyId + ", company=" + company + "]";
    }
}
