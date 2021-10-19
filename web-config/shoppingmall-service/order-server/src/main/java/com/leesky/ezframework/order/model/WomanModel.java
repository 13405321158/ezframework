package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.annotation.EntityMapper;
import com.leesky.ezframework.mybatis.annotation.JoinColumn;
import com.leesky.ezframework.mybatis.annotation.OneToMany;
import com.leesky.ezframework.mybatis.annotation.OneToOne;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import com.leesky.ezframework.order.mapper.IchildMapper;
import com.leesky.ezframework.order.mapper.IidCardMapper;
import com.leesky.ezframework.order.mapper.ImanMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

@Getter
@Setter
@TableName("cbm_woman")
public class WomanModel extends BaseUuidModel {

    private static final long serialVersionUID = 4535079787922191738L;

    private String name;

    private String laogongId;

    private String cardId;

    @OneToOne
    @TableField(exist = false)
    @JoinColumn(name = "laogong_id")
    @EntityMapper(targetMapper = ImanMapper.class)
    private ManModel laoGong;

    @OneToOne
    @TableField(exist = false)
    @JoinColumn(name = "card_id")
    @EntityMapper(targetMapper = IidCardMapper.class)
    private IdCardModel idCard;


    @OneToMany
    @TableField(exist = false)
    @JoinColumn(referencedColumnName = "lao_ma_id")
    @EntityMapper(targetMapper = IchildMapper.class)
    private List<ChildModel> waWa;

    public WomanModel() {
        this.name = RandomStringUtils.randomAlphanumeric(4);
    }

    public WomanModel(String name) {
        this.name = name;
    }
}
