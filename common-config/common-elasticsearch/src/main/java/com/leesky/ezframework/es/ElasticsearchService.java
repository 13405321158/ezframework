/*
 * @作者: 魏来
 * @日期: 2021/5/31  上午9:26
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @Desc: 操作elasticservice的工具类（增、删、改、查），支持 es6和es7版本
 */
package com.leesky.ezframework.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.leesky.ezframework.query.ParamModel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    private final RestHighLevelClient highLevelClient;


    /**
     * 1、增加索引
     *
     * @author: 魏来
     * @date: 2022/3/15 下午7:09
     */
    public void addIndex(String indexName) throws IOException {
        Assert.isTrue(StringUtils.isNotBlank(indexName), "参数【indexName】不能是空值");

        GetIndexRequest re = new GetIndexRequest(indexName);
        boolean exists = this.highLevelClient.indices().exists(re, RequestOptions.DEFAULT);
        if (!exists) {
            CreateIndexRequest request = new CreateIndexRequest(indexName);

            this.highLevelClient.indices().create(request, RequestOptions.DEFAULT);

        }

    }
    /**
     * 2、删除索引(该索引内的文档一并被删除)： 使用 this.xxxRepo.deleteAll();只是删除索引内的文档，索引并没有删除
     *
     * @author: 魏来
     * @date: 2021/6/2   下午12:38
     */
    public void delIndex(String indexName) throws IOException {
        Assert.isTrue(StringUtils.isNotBlank(indexName), "参数【indexName】不能是空值");

        GetIndexRequest re = new GetIndexRequest(indexName);
        boolean exists = this.highLevelClient.indices().exists(re, RequestOptions.DEFAULT);
        if (exists) {
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            this.highLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        }

    }


    /**
     * 3、查询全部，不分页,不带查询参数
     *
     * @author: 魏来
     * @date: 2021/5/31 下午1:13
     **/
    public QueryResult findAll(String indexName) throws IOException {
        Assert.isTrue(StringUtils.isNotBlank(indexName), "参数【indexName】不能是空值");
        return query(indexName, null, null, null, null);
    }

    /**
     * 3、分页查询, 不带排序字段
     *
     * @author: 魏来
     * @date: 2021/5/31 下午1:40
     **/
    public QueryResult page(String indexName, ParamModel param) throws IOException {
        return query(indexName, param, null, null, null);
    }

    /**
     * 4、分页查询, 带排序字段
     * sortFieldsToAsc 参数是个map，其中value =true代表ASC排序，value=false代表desc排序
     *
     * @author: 魏来
     * @date: 2021/5/31 下午1:40
     **/
    public QueryResult page(ParamModel param, Map<String, Boolean> sortFieldsToAsc) throws IOException {
        Assert.isTrue(StringUtils.isNotBlank(param.getSelect()), "ParamModel.select属性代表索引名称，不能是空值");

        return query(param.getSelect(), param, sortFieldsToAsc, null, null);
    }

    /**
     * 5、分页查询, 查询部分字段
     * 包括字段：includeFields
     * 排除字段：excludeFields
     *
     * @author: 魏来
     * @date: 2021/5/31 下午1:40
     **/
    public QueryResult page(ParamModel param, List<String> includeFields, List<String> excludeFields) throws IOException {
        if (CollectionUtils.isEmpty(includeFields))
            includeFields = Lists.newArrayList();

        if (CollectionUtils.isEmpty(excludeFields)) {
            excludeFields = Lists.newArrayList();
        }

        String[] include = includeFields.toArray(new String[0]);
        String[] exclude = excludeFields.toArray(new String[0]);
        return query(param.getSelect(), param, null, include, exclude);

    }

    /**
     * 6、分页查询, 带排序字段 和 查询部分字段
     * 包括字段：includeFields
     * 排除字段：excludeFields
     *
     * @author: 魏来
     * @date: 2021/5/31 下午1:40
     **/
    public QueryResult page(String indexName, ParamModel param, Map<String, Boolean> sortFieldsToAsc,
                                                             List<String> includeFields, List<String> excludeFields) throws IOException {
        if (CollectionUtils.isEmpty(includeFields))
            includeFields = Lists.newArrayList();

        if (CollectionUtils.isEmpty(excludeFields))
            excludeFields = Lists.newArrayList();


        String[] include = includeFields.toArray(new String[0]);
        String[] exclude = excludeFields.toArray(new String[0]);
        return query(indexName, param, sortFieldsToAsc, include, exclude);

    }

    /**
     * 7、修改文档: queryStr是更新条件，如果是null 则更新全部文档
     *
     * @author: 魏来
     * @date: 2021/6/1 下午1:42
     **/
    public void updateByModel(Object javaBean, ParamModel param) throws IOException {
        Assert.isTrue(StringUtils.isNotBlank(param.getSelect()), "ParamModel.select属性代表索引名称，不能是空值");

        List<String> script = Lists.newArrayList();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest(param.getSelect()).setBatchSize(100).setAbortOnVersionConflict(false);
        Map<String, Object> params = JSONObject.parseObject(JSON.toJSONString(javaBean),
                new TypeReference<HashMap<String, Object>>() {
                });

        for (Map.Entry<String, Object> map : params.entrySet()) {
            script.add("ctx._source." + map.getKey() + "=params." + map.getKey());//"params."  只能这样写
        }
        updateByQueryRequest.setScript(new Script(ScriptType.INLINE, "painless", StringUtils.join(script, ";"), params));
        BoolQueryBuilder query = buildQueryParam(param, sourceBuilder);

        updateByQueryRequest.setQuery(query);

        this.highLevelClient.updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);


    }

    /**
     * 8、根据条件删除记录: queryStr =null 则代表删除全部文档
     *
     * @author: 魏来
     * @date: 2021/6/2   下午12:12
     */
    public BulkByScrollResponse delete(ParamModel param) throws IOException {
        Assert.isTrue(StringUtils.isNotBlank(param.getSelect()), "ParamModel.select属性代表索引名称，不能是空值");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(param.getSelect()).setBatchSize(100).setAbortOnVersionConflict(false);
        BoolQueryBuilder query = buildQueryParam(param, sourceBuilder);
        deleteByQueryRequest.setQuery(query);
        return this.highLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);

    }



    /**
     * 通用 查询方法
     *
     * @author: 魏来
     * @date: 2021/5/31 下午4:49
     **/
    private QueryResult query(String indexName, ParamModel param, Map<String, Boolean> sortFieldsToAsc,
                                                               String[] includeFields, String[] excludeFields) throws IOException {


        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.timeout(new TimeValue(50, TimeUnit.SECONDS));

        // 1、设置分页
        buildPage(param, sourceBuilder);

        // 2、构造查询条件
        BoolQueryBuilder query = buildQueryParam(param, sourceBuilder);

        // 3、设置排序字段
        buildSort(sortFieldsToAsc, sourceBuilder);

        // 4 、查询需要的列，或排除的列，即 select * 中的 *
        buildSelect(includeFields, excludeFields, sourceBuilder);

        // 5、开始查询
        CountRequest countRequest = new CountRequest(indexName).query(query);
        CountResponse countResponse = highLevelClient.count(countRequest, RequestOptions.DEFAULT);

        SearchRequest searchRequest = new SearchRequest(indexName).source(sourceBuilder);
        searchRequest.setPreFilterShardSize(128);
        SearchResponse searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        // 6.1、查询失败 返回空白List
        if (searchResponse.status() != RestStatus.OK) {
            return new QueryResult(0L, Lists.newArrayList());
        }
        // 6.2、正常返回获取查询结果
        List<Map<String, Object>> result = Arrays.stream(searchResponse.getHits().getHits())
                .map(SearchHit::getSourceAsMap).collect(Collectors.toList());
        return new QueryResult(countResponse.getCount(), result);


    }

    /**
     * 设置分页信息
     *
     * @author: 魏来
     * @date: 2021/5/31 下午3:44
     **/
    private void buildPage(ParamModel param, SearchSourceBuilder sb) {
        if (ObjectUtils.isNotEmpty(param)) {
            sb.from((param.getPage()) * param.getLimit());
            sb.size(param.getLimit());
        }
    }

    /**
     * 设置排序: 字段是实体类属性，不是数据表的字段名称
     *
     * @author: 魏来
     * @date: 2021/5/31 下午2:45
     **/
    private void buildSort(Map<String, Boolean> sortFieldsToAsc, SearchSourceBuilder sb) {

        if (MapUtils.isEmpty(sortFieldsToAsc)) {
            sb.sort(new ScoreSortBuilder().order(SortOrder.DESC));
        } else {
            sortFieldsToAsc.forEach((k, v) -> {
                k = Hump2underline.build(k);
                sb.sort(new FieldSortBuilder(k).order(v ? SortOrder.ASC : SortOrder.DESC));
            });
        }
    }

    /**
     * 构建查询条件
     *
     * @author: 魏来
     * @date: 2021/5/31 下午1:54
     **/
    private BoolQueryBuilder buildQueryParam(ParamModel param, SearchSourceBuilder sb) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (ObjectUtils.isNotEmpty(param) && StringUtils.isNotBlank(param.getQueryStr())) {

            Map<String, String> params = JSONObject.parseObject(param.getQueryStr(),
                    new TypeReference<HashMap<String, String>>() {
                    });

            for (Map.Entry<String, String> map : params.entrySet()) {

                if (StringUtils.isNotBlank(map.getValue())) {
                    String[] array = StringUtils.split(map.getKey().replace("Q_", ""), "_");
                    getOpera(array, map.getValue(), boolQueryBuilder);
                }
            }
            sb.query(boolQueryBuilder);
        }

        return boolQueryBuilder;
    }

    /**
     * 设置 查询那些属性,类似 select * 中的 *
     *
     * @author: 魏来
     * @date: 2021/5/31 下午2:58
     **/
    private void buildSelect(String[] includeFields, String[] excludeFields, SearchSourceBuilder sb) {
        sb.fetchSource(includeFields, excludeFields);
    }

    /**
     * 构建查询对象
     *
     * @author: 魏来
     * @date: 2021/5/31 下午7:08
     **/
    private void getOpera(String[] array, String val, BoolQueryBuilder boolQueryBuilder) {
        String column;

        if (array.length > 1) {
            column = Hump2underline.build(array[0]);
            String opera = array[1];

            switch (opera) {
                //wildcardQuery 模糊查询单个汉字可以，但是多个汉字在一起查询就不行了
                //matchQuery用于文本类型模糊检索，会对查询的关键字进行分词 , 然后再进行匹配）
                case "LK":// 模糊 like查询
                    boolQueryBuilder.must(QueryBuilders.matchQuery(column, val));
                    break;

                case "IN":// = 精确查询
                case "EQ":// = 精确查询
                    String[] a = StringUtils.split(val, ",");
                    boolQueryBuilder.should(QueryBuilders.termsQuery(column + ".keyword", a));
                    break;

                case "GE":// >= 范围查询
                    boolQueryBuilder.must(QueryBuilders.rangeQuery(column).gte(val));
                    break;

                case "GT":// > 范围查询
                    boolQueryBuilder.must(QueryBuilders.rangeQuery(column).gt(val));
                    break;

                case "LE":// <= 范围查询
                    boolQueryBuilder.must(QueryBuilders.rangeQuery(column).lte(val));
                    break;

                case "LT":// < 范围查询
                    boolQueryBuilder.must(QueryBuilders.rangeQuery(column).lt(val));
                    break;


                default:
                    break;
            }
        }
    }

}
