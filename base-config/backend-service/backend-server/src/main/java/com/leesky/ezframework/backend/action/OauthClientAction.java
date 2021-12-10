package com.leesky.ezframework.backend.action;

import com.leesky.ezframework.backend.dto.OauthClientDetailsDTO;
import com.leesky.ezframework.backend.model.OauthClientDetailsModel;
import com.leesky.ezframework.backend.service.IoauthCientService;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.utils.Po2DtoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * desc：oauth Client 控制器
 *
 * @author： 魏来
 * @date： 2021/12/1 下午5:02
 */

@Slf4j
@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class OauthClientAction {

    private final IoauthCientService service;

    /**
     * 查询client
     *
     * @author： 魏来
     * @date: 2021/12/1 下午6:14
     */
    @GetMapping("/r01/public")
    public Result<OauthClientDetailsDTO> getOAuth2ClientById(@RequestParam String clientId) {

        OauthClientDetailsModel client = this.service.findOne(clientId);
        Assert.isTrue(client != null, "OAuth2 客户端不存在");
        OauthClientDetailsDTO dto = Po2DtoUtil.convertor(client, OauthClientDetailsDTO.class);

        return Result.success(dto);
    }
}
