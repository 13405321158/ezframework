/*
 * @作者: 魏来
 * @日期: 2021-08-21 15:48:39
 * @组织: 森麒麟轮胎股份有限公司
 * @部门: 国内市场替换部IT组
 * @Desc:
 */
package com.leesky.ezframework.backend.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Lists;
import com.leesky.ezframework.backend.dto.UserBaseDTO;
import com.leesky.ezframework.backend.mapper.IoauthClientMapper;
import com.leesky.ezframework.backend.mapper.sys.IuserBaseExt01Mapper;
import com.leesky.ezframework.backend.mapper.sys.IuserBaseExt02Mapper;
import com.leesky.ezframework.backend.mapper.sys.IuserBaseMapper;
import com.leesky.ezframework.backend.model.OauthClientDetailsModel;
import com.leesky.ezframework.backend.model.sys.UserBaseExt01Model;
import com.leesky.ezframework.backend.model.sys.UserBaseExt02Model;
import com.leesky.ezframework.backend.model.sys.UserBaseModel;
import com.leesky.ezframework.backend.service.sys.IuserBaseService;
import com.leesky.ezframework.mybatis.service.impl.LeeskyServiceImpl;
import com.leesky.ezframework.utils.MD5Util;
import com.leesky.ezframework.utils.Po2DtoUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserBaseServiceImpl extends LeeskyServiceImpl<IuserBaseMapper, UserBaseModel> implements IuserBaseService {

    @Value("${access.token.validity:360}") // 默认值过期时间60*60s 一小时
    private int accessTokenValiditySeconds;

    @Value("${access.refresh.validity:360}")
    private int refreshTokenValiditySeconds;

    private final PasswordEncoder passwordEncoder;

    private final IuserBaseMapper repo;
    private final IoauthClientMapper clientMapper;
    private final IuserBaseExt01Mapper ext01Mapper;
    private final IuserBaseExt02Mapper ext02Mapper;

    /**
     * <li>新增账户，同时新增对应client</li>
     * <li>控制器中已经验证了 数据表中无重重复账户信息
     *
     * @author： 魏来
     * @date: 2022/1/28  14:52
     */
    @Override
    @Transactional
    public void addUser(UserBaseDTO dto) throws Exception {

        String p = StringUtils.isNotBlank(dto.getPassword()) ? dto.getPassword() : MD5Util.encrypt("Pwd" + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now()));
        String pwd = this.passwordEncoder.encode(p);

        UserBaseModel model = Po2DtoUtil.convertor(dto, UserBaseModel.class);
        model.setPassword(pwd);


        this.insert(model, true);

        List<OauthClientDetailsModel> list = Lists.newArrayList();
        list.add(new OauthClientDetailsModel(dto.getMobile(), this.passwordEncoder.encode(dto.getMobile()), accessTokenValiditySeconds, refreshTokenValiditySeconds));
        list.add(new OauthClientDetailsModel(dto.getUsername(), this.passwordEncoder.encode(dto.getUsername()), accessTokenValiditySeconds, refreshTokenValiditySeconds));
        this.clientMapper.insertBatch(list);

    }

    /**
     * 编辑用户
     *
     * @author： 魏来
     * @date: 2022/1/29  16:17
     */
    @Override
    @Transactional
    public void editUser(UserBaseModel model) {

        UpdateWrapper<UserBaseModel> filter = new UpdateWrapper<>();
        filter.eq("id", model.getId());
        this.repo.update(model, filter);

        UpdateWrapper<UserBaseExt01Model> filter01 = new UpdateWrapper<>();
        filter01.eq("id", model.getExt01Id());
        this.ext01Mapper.update(model.getExt01(), filter01);


        UpdateWrapper<UserBaseExt02Model> filter02 = new UpdateWrapper<>();
        filter02.eq("id", model.getExt02Id());
        this.ext02Mapper.update(model.getExt02(), filter02);


        UpdateWrapper<OauthClientDetailsModel> filter04 = new UpdateWrapper<>();
        filter04.eq("client_id", model.getUsername()).set("client_secret", model.getPassword());
        this.clientMapper.update(new OauthClientDetailsModel(), filter04);
    }

    /**
     * <li>修改密码</li>
     *
     * @author: 魏来
     * @date: 2022/2/10 上午10:40
     */
    @Override
    @Transactional
    public void editPwd(String uid, String username, String pwd) {
        pwd = this.passwordEncoder.encode(pwd);
        //1、更新client
        UpdateWrapper<OauthClientDetailsModel> filter01 = new UpdateWrapper<>();
        filter01.eq("client_id", username).set("client_secret", pwd);
        this.clientMapper.update(new OauthClientDetailsModel(), filter01);

        //2、更新用户密码
        UpdateWrapper<UserBaseModel> filter = new UpdateWrapper<>();
        filter.eq("id", uid).set("password", pwd).set("modify_date", LocalDateTime.now());
        this.repo.update(new UserBaseModel(), filter);
    }

    /**
     * 删除用户：cbm_mag_user、cbm_mag_user_ext01、cbm_mag_user_ext02、oauth_client_details
     *
     * @author： 魏来
     * @date: 2022/1/26  13:06
     */
    @Override
    @Transactional
    public void delUser(List<UserBaseDTO> list) {
        List<String> ids = list.stream().map(UserBaseDTO::getId).collect(Collectors.toList());
        this.repo.deleteBatchIds(ids);

        QueryWrapper<UserBaseExt01Model> e1 = new QueryWrapper<>();
        e1.in("user_id", list);
        this.ext01Mapper.delete(e1);

        QueryWrapper<UserBaseExt02Model> e2 = new QueryWrapper<>();
        e1.in("user_id", list);
        this.ext02Mapper.delete(e2);


        List<String> clients = list.stream().map(UserBaseDTO::getUsername).collect(Collectors.toList());
        this.clientMapper.deleteBatchIds(clients);
    }

}
