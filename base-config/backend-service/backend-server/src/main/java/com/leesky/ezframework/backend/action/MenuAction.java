package com.leesky.ezframework.backend.action;

import com.alibaba.fastjson.JSON;
import com.leesky.ezframework.backend.dto.MenuDTO;
import com.leesky.ezframework.json.Result;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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

        StringBuffer menuJson = readJsonFile("menu.json");

        List<MenuDTO> data = JSON.parseArray(menuJson.toString(), MenuDTO.class);

        return Result.success(data, false);
    }



    @SneakyThrows
    private StringBuffer readJsonFile(String fileName) {
        StringBuffer buf = new StringBuffer();
        String line;

        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        assert in != null;
        InputStreamReader stream = new InputStreamReader(in);
        BufferedReader read = new BufferedReader(stream);
        while ((line = read.readLine()) != null)
            buf.append(line);

        read.close();
        stream.close();
        in.close();

        return buf;
    }

}
