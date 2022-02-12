package com.leesky.ezframework.backend.action;

import com.google.common.collect.Lists;
import com.leesky.ezframework.backend.dto.MenuDTO;
import com.leesky.ezframework.backend.dto.MenuMetaDTO;
import com.leesky.ezframework.json.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2022/1/5 下午12:36
 */
@RestController
@RequestMapping("/resource")
public class MenuAction {


    /**
     * 登陆用户获取菜单
     *
     * @author： 魏来
     * @date: 2022/1/5 下午12:37
     */
    @GetMapping(value = "/r01")
    public Result<List<MenuDTO>> r01() {
        List<MenuDTO> menu = Lists.newArrayList();

        MenuMetaDTO meta = new MenuMetaDTO("Setting", "平台管理");
        MenuDTO dto = new MenuDTO("system", "/system", "/system/user/index", meta);


        MenuMetaDTO meta01 = new MenuMetaDTO("User", "用户管理");
        MenuDTO dto01 = new MenuDTO("user", "/system/user/index",  meta01);


        MenuMetaDTO meta02 = new MenuMetaDTO("Setting", "角色管理");
        MenuDTO dto02 = new MenuDTO("dict", "/system/dict/index",  meta02);


        dto.setChildren(Lists.newArrayList(dto01, dto02));
        menu.add(dto);
        return Result.success(menu,false);
    }


}
