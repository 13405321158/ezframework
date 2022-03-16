/*
 * @作者: 魏来
 * @日期: 2021/5/28  下午4:49
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @Desc:
 */
package com.leesky.ezframework.es.action;

import com.google.common.collect.ImmutableMap;
import com.leesky.ezframework.es.config.ElasticsearchService;
import com.leesky.ezframework.es.config.QueryResult;
import com.leesky.ezframework.es.model.Demo01Model;
import com.leesky.ezframework.es.repo.Idem01Repo;
import com.leesky.ezframework.json.Result;
import com.leesky.ezframework.query.ParamModel;
import com.leesky.ezframework.utils.I18nUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class DemoAction {

    private final I18nUtil i18n;

    private final Idem01Repo bookRepo;

    private final ElasticsearchService service;

    /**
     * 1、新增索引(存储数据前必须先增加索引，然后在增加文档)
     *
     * @author： 魏来
     * @date: 2022/3/15  下午7:09
     */
    @PostMapping(value = "/c01")
    public Result<?> addIndex(@RequestBody ParamModel param) throws IOException {
        Assert.isTrue(StringUtils.isNotBlank(param.getSelect()), i18n.getMsg("doc.indexName.null"));
        this.service.addIndex(param.getSelect());

        return Result.success();
    }

    /**
     * 删除索引
     *
     * @author: 魏来
     * @date: 2022/3/16 上午11:11
     */
    @GetMapping("/d01")
    public Result<?> deleteIndex(@RequestBody ParamModel param) {
        Assert.isTrue(StringUtils.isNotBlank(param.getSelect()), i18n.getMsg("doc.indexName.null"));

        this.service.delIndex(param.getSelect());
        return Result.success("成功删除索引：" + param.getSelect());
    }

    /**
     * 2、新增文档
     *
     * @author: 魏来
     * @date: 2022/3/16 上午10:04
     */
    @PostMapping("/c02")
    public Result<Demo01Model> addDoc(@RequestBody Demo01Model book) {

        Demo01Model data = this.bookRepo.save(book);

        return Result.success(data, false);

    }

    /**
     * 根据id查找
     *
     * @author: 魏来
     * @date: 2022/3/16 下午2:15
     */
    @GetMapping("/id/{id}")
    public Demo01Model findOne(@PathVariable String id) {
        Optional<Demo01Model> data = bookRepo.findById(id);

        return data.orElse(null);
    }

    /**
     * 不带条件查询全部文档(参数select=索引名称)
     *
     * @author: 魏来
     * @date: 2022/3/16 上午10:08
     */
    @GetMapping("/r01")
    public Result<Object> findAll(@RequestBody ParamModel param) throws IOException {
        Assert.isTrue(StringUtils.isNotBlank(param.getSelect()), i18n.getMsg("doc.indexName.null"));

        QueryResult result = this.service.findAll(param.getSelect());

        return Result.success(result.getContent(), result.getTotal(), false);
    }

    /**
     * 分页查询
     * 参数说明：param.select = 索引(必填)； param.limit= 每页数量； param.page=当前页；queryStr=查询条件
     * 其中查询条件构造格式如： "queryStr":"{'Q_title_LK':'大记','Q_name_EQ':'wei'}"
     *
     * @author: 魏来
     * @date: 2022/3/16 上午10:09
     */
    @GetMapping("/r02")
    public Result<List<Map<String, Object>>> page(@RequestBody ParamModel param) throws IOException {
        Assert.isTrue(StringUtils.isNotBlank(param.getSelect()), i18n.getMsg("doc.indexName.null"));
        //demo-1  查询结果不含有 tag字段
        // QueryResult result = this.service.page("book", param, Lists.newArrayList(), Lists.newArrayList("tag"));

        //demo-2 查询结果只包含title字段
        //QueryResult result = this.service.page(param, Lists.newArrayList("title"), Lists.newArrayList());

        //demo-3 查询结果按照 price字段排序 true=ASC排序，false=desc排序
        QueryResult result = this.service.page(param, ImmutableMap.of("price", false));

        return Result.success(result.getContent(), result.getTotal(), false);
    }

    /**
     * 修改文档: queryStr是更新条件，如果是null 则更新全部文档
     *
     * @author: 魏来
     * @date: 2022/3/16 上午10:05
     */
    @PostMapping("/u01")
    public Result<Demo01Model> editDoc(@RequestBody ParamModel param) throws IOException {
        Assert.isTrue(StringUtils.isNotBlank(param.getSelect()), i18n.getMsg("doc.indexName.null"));

        Demo01Model book = new Demo01Model();
        book.setPrice(new BigDecimal("3.48"));//更新内容

        this.service.updateByModel(book, param);

        return Result.success();
    }

    /**
     * 根据条件删除记录: queryStr =null 则代表删除全部文档
     *
     * @author: 魏来
     * @date: 2022/3/16 下午2:28
     */
    @PostMapping("/d02")
    public Result<?> delDoc(@RequestBody ParamModel param) throws IOException {

        BulkByScrollResponse result = this.service.delete(param);

        return Result.success(result.getDeleted() + "个文档被删除", false);

    }
}
