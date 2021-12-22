/*
 * @作者: 魏来
 * @日期: 2021/5/28  下午4:49
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @Desc:
 */
package com.leesky.ezframework.es.action;

import java.math.BigDecimal;
import java.util.Optional;

import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.leesky.ezframework.es.config.ElasticsearchService;
import com.leesky.ezframework.es.config.QueryResult;
import com.leesky.ezframework.es.model.backend.Demo01Model;
import com.leesky.ezframework.es.repo.backend.Idem01Repo;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.query.ParamModel;


@RestController
@RequestMapping("/book")
@SuppressWarnings({"rawtypes"})
public class DemoAction {

    private final Idem01Repo bookRepo;

    private final ElasticsearchService service;


    @Autowired
    public DemoAction(Idem01Repo bookRepo, ElasticsearchService service) {
        this.service = service;
        this.bookRepo = bookRepo;
    }


    @PostMapping("/c01")
    public Result<Demo01Model> add(@RequestBody Demo01Model book) {

        Demo01Model b = this.bookRepo.save(book);

        return Result.success(b);

    }


    @PostMapping("/u01")
    public Result<Demo01Model> xxx(@RequestBody ParamModel param) {

        Demo01Model book = new Demo01Model("西游记", new BigDecimal("3.48"), Lists.newArrayList("名著,小说,最爱,战争"));

        this.service.updateByModel("book", book, param);

        return Result.success();
    }


    @GetMapping("/r01")
    public Result<Object> list() {


        QueryResult result = this.service.findAll("book");


        return Result.success(result.getContent(), result.getTotal());
    }


    @GetMapping("/r02")
    public Result list(@RequestBody ParamModel param) {

        //demo-1  查询结果不含有 tag字段
        // QueryResult result = this.service.page("book", param, Lists.newArrayList(), Lists.newArrayList("tag"));

        //demo-2 查询结果只包含title字段
        //QueryResult result = this.service.page("book", param, Lists.newArrayList("title"), Lists.newArrayList());

        //demo-3 查询结果按照 price字段排序 true=ASC排序，false=desc排序
        QueryResult result = this.service.page("book", param, ImmutableMap.of("price", false));


        return Result.success(result.getContent(), result.getTotal());
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
    public Result delDoc(@RequestBody ParamModel param) {

        BulkByScrollResponse result = this.service.delete("book", param);

        return Result.success(result.getDeleted() + "个文档被删除");

    }
}
