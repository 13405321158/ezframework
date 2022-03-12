/*
 * @作者: 魏来
 * @日期: 2022/3/12 下午12:10
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.backend.action;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leesky.ezframework.backend.dto.OauthClientDetailsDTO;
import com.leesky.ezframework.backend.model.OauthClientDetailsModel;
import com.leesky.ezframework.backend.service.IoauthClientService;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.mybatis.query.QueryFilter;
import com.leesky.ezframework.query.CommonDTO;
import com.leesky.ezframework.query.ParamModel;
import com.leesky.ezframework.utils.MD5Util;
import com.leesky.ezframework.utils.Po2DtoUtil;
import com.leesky.ezframework.utils.ValidatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <li>client控制器</li>
 *
 * @author: 魏来
 * @date: 2022/3/12 下午12:10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientAction {

    private final IoauthClientService service;
    private final PasswordEncoder passwordEncoder;

    @Value("${access.token.validity:360}") // 默认值过期时间60*60s 一小时
    private int access;
    @Value("${access.refresh.validity:360}")
    private int refresh;

    /**
     * cleint分页列表
     *
     * @author： 魏来
     * @date: 2022/3/12  下午12:14
     */
    @PostMapping(value = "/r01")
    public Result<List<OauthClientDetailsDTO>> page(@RequestBody ParamModel param) {
        QueryFilter<OauthClientDetailsModel> filter = new QueryFilter<>(param);

        Page<OauthClientDetailsModel> data = this.service.page(filter);

        List<OauthClientDetailsDTO> dto = Po2DtoUtil.convertor(data.getRecords(), OauthClientDetailsDTO.class);
        return Result.success(dto, data.getTotal(), false);
    }

    /**
     * 编辑客户端密码: 前端使用md5加密 client_secret
     *
     * @author： 魏来
     * @date: 2022/3/12  下午12:18
     */
    @PostMapping(value = "/u01")
    public Result<?> editSecret(@RequestBody CommonDTO dto) {
        UpdateWrapper<OauthClientDetailsModel> filter = new UpdateWrapper<>();

        String raw = MD5Util.encrypt(dto.getPid() + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

        filter.set("client_secret", this.passwordEncoder.encode(raw)).eq("client_id", dto.getPid());
        this.service.update(filter);

        return Result.success();
    }


    /**
     * 新增客户端
     *
     * @author： 魏来
     * @date: 2022/3/12  下午12:22
     */
    @PostMapping(value = "/c01")
    public Result<?> addClient(@RequestBody OauthClientDetailsDTO dto) throws Exception {
        ValidatorUtils.all(dto);

        OauthClientDetailsModel model = Po2DtoUtil.convertor(dto, OauthClientDetailsModel.class);

        model.build(dto.getAuthorizedGrantTypes(), this.access, this.refresh, this.passwordEncoder.encode(model.getClientSecret()));
        this.service.insert(model, false);

        return Result.success();
    }
}
