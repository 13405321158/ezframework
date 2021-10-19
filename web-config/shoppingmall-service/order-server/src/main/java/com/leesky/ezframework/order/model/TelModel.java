package com.leesky.ezframework.order.model;

import org.apache.commons.lang3.RandomStringUtils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.ManyToOne;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("cbm_tel")
public class TelModel extends BaseUuidModel {

    private static final long serialVersionUID = 1260334746552255040L;

    private String tel;

	private String laoHanId;
	
    @ManyToOne
    @TableField(exist = false)
    @JoinColumn(name = "lao_han_id")
    private ManModel laoHan;

    public TelModel() {
        this.tel = "134" + RandomStringUtils.randomAlphanumeric(7);
    }

    public TelModel(ManModel laoHan) {
        this.laoHan = laoHan;
    }

    public TelModel(String tel) {
        this.tel = tel;
    }

    public TelModel(String tel, String id) {
        this.id = id;
        this.tel = tel;
    }
}
