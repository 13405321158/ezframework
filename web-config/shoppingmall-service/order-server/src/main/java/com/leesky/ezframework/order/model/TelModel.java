package com.leesky.ezframework.order.model;

import org.apache.commons.lang3.RandomStringUtils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.ManyToOne;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import com.leesky.ezframework.order.mapper.ImanMapper;

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
    @EntityMapper(targetMapper = ImanMapper.class)
    private ManModel laoHan;

    public TelModel() {
        this.tel = "13" + RandomStringUtils.randomNumeric(9);
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
