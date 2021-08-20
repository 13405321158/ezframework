/*
 * @作者: 魏来
 * @日期: 2021/5/28  下午4:49
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @Desc:
 */
package com.leesky.ezframework.es.action;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.leesky.ezframework.es.config.ElasticsearchService;
import com.leesky.ezframework.es.config.QueryResult;
import com.leesky.ezframework.es.model.backend.Demo01Model;
import com.leesky.ezframework.es.repo.backend.Idem01Repo;
import com.leesky.ezframework.json.AjaxJson;
import com.leesky.ezframework.query.ParamModel;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/book")
public class DemoAction {

    private final Idem01Repo bookRepo;

    private final ElasticsearchService service;


    @Autowired
    public DemoAction(Idem01Repo bookRepo, ElasticsearchService service) {
        this.service = service;
        this.bookRepo = bookRepo;
    }


    @PostMapping("/c01")
    public AjaxJson add(@RequestBody Demo01Model book) {

        AjaxJson json = new AjaxJson();
        try {
            Demo01Model b = this.bookRepo.save(book);
            json.setData(b);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            json.setSuccess(false, e.getMessage());
        }
        return json;

    }


    @PostMapping("/u01")
    public AjaxJson xxx(@RequestBody ParamModel param) {

        AjaxJson json = new AjaxJson();
        try {
            Demo01Model book = new Demo01Model("西游记", new BigDecimal("3.48"), Lists.newArrayList("名著,小说,最爱,战争"));

            this.service.updateByModel("book", book, param);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            json.setSuccess(false, e.getMessage());
        }
        return json;

    }


    @GetMapping("/r01")
    public AjaxJson list() {
        AjaxJson json = new AjaxJson();
        try {

            QueryResult result = this.service.findAll("book");

            json.setData(result.getContent());
            json.setCount(result.getTotal());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            json.setSuccess(false, e.getMessage());
        }
        return json;
    }


    @GetMapping("/r02")
    public AjaxJson list(@RequestBody ParamModel param) {
        AjaxJson json = new AjaxJson();
        try {
            //demo-1  查询结果不含有 tag字段
            // QueryResult result = this.service.page("book", param, Lists.newArrayList(), Lists.newArrayList("tag"));

            //demo-2 查询结果只包含title字段
            //QueryResult result = this.service.page("book", param, Lists.newArrayList("title"), Lists.newArrayList());

            //demo-3 查询结果按照 price字段排序 true=ASC排序，false=desc排序
            QueryResult result = this.service.page("book", param, ImmutableMap.of("price", false));


            json.setData(result.getContent());
            json.setCount(result.getTotal());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            json.setSuccess(false, e.getMessage());
        }
        return json;
    }


    @GetMapping("/{id}")
    public Demo01Model getById(@PathVariable String id) {
        Optional<Demo01Model> dd = bookRepo.findById(id);

        return dd.orElse(null);
    }


    @GetMapping("/d01")
    public void deleteIndex(@RequestBody ParamModel param) {
        try {
            this.service.delIndex("book");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @PostMapping("/d02")
    public AjaxJson delDoc(@RequestBody ParamModel param) {

        AjaxJson json = new AjaxJson();
        try {
            BulkByScrollResponse result = this.service.delete("book", param);
            json.setData(result.getDeleted() + "个文档被删除");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            json.setSuccess(false, e.getMessage());
        }
        return json;

    }
}
