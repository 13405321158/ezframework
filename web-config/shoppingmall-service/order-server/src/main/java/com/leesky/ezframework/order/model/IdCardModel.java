/*
 * @作者: 魏来
 * @日期: 2021/10/13  下午3:04
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.order.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leesky.ezframework.mybatis.model.BaseUuidModel;
import lombok.Getter;
import lombok.Setter;

/**
 * <li>描述:
 */
@Setter
@Getter
@TableName("cbm_idcard")
public class IdCardModel extends BaseUuidModel {

    private static final long serialVersionUID = 9193338380326378106L;

    private String cardNo;

    private String cardAdds;

    public IdCardModel() {
    }

    public IdCardModel(String cardNo, String cardAdds) {
        this.cardNo = cardNo;
        this.cardAdds = cardAdds;
    }
}
