/*
 * @作者: 魏来
 * @日期: 2022/2/25 下午2:38
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.service.impl.buy;

import com.google.common.collect.Lists;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.backend.mapper.IoauthClientMapper;
import com.leesky.ezframework.backend.mapper.buy.IbuyerBaseMapper;
import com.leesky.ezframework.backend.model.OauthClientDetailsModel;
import com.leesky.ezframework.backend.model.buyer.BuyerBaseModel;
import com.leesky.ezframework.backend.service.buy.IbuyerBaseService;
import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import com.leesky.ezframework.utils.MD5Util;
import com.leesky.ezframework.utils.Po2DtoUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <li></li>
 *
 * @author: 魏来
 * @date: 2022/2/25 下午2:38
 */
@Service
@RequiredArgsConstructor
public class BuyerBaseServiceImpl extends LeeskyServiceImpl<IbuyerBaseMapper, BuyerBaseModel> implements IbuyerBaseService {

    @Value("${access.token.validity:360}") // 默认值过期时间60*60s 一小时
    private int access;

    @Value("${access.refresh.validity:360}")
    private int refresh;

    private final PasswordEncoder passwordEncoder;

    private final IoauthClientMapper clientMapper;


    @Override
    @Transactional
    public void addUser(UserBaseDTO dto) throws Exception {
        String p = StringUtils.isNotBlank(dto.getPassword()) ? dto.getPassword() : MD5Util.encrypt("Pwd" + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now()));
        String pwd = this.passwordEncoder.encode(p);

        BuyerBaseModel model = Po2DtoUtil.convertor(dto, BuyerBaseModel.class);
        model.setPassword(pwd);

        this.insert(model, true);


        List<OauthClientDetailsModel> list = Lists.newArrayList();
        list.add(new OauthClientDetailsModel(dto.getUsername(), pwd, "password,refresh_token,captcha", access, refresh));
        list.add(new OauthClientDetailsModel(dto.getMobile(), this.passwordEncoder.encode(MD5Util.encrypt(dto.getMobile() + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE))), "sms_code", access, refresh));

        this.clientMapper.insertBatch(list);
    }
}
