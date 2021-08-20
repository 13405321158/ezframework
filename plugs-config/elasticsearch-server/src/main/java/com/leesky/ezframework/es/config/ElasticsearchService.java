/*
 * @作者: 魏来
 * @日期: 2021/5/31  上午9:26
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @Desc: 操作elasticservice的工具类（增、删、改、查），支持 es6和es7版本
 */
package com.leesky.ezframework.es.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.leesky.ezframework.query.ParamModel;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ElasticsearchService {

    private final RestHighLevelClient highLevelClient;

    @Autowired
    public ElasticsearchService(RestHighLevelClient highLevelClient) {
        this.highLevelClient = highLevelClient;
    }

    /**
     * @author: weilai
     * @date: 2021/5/31 下午1:13
     * @desc: <li>查询全部，不分页,不带查询参数</li>
     **/
    public QueryResult findAll(String indexName) {
        return query(indexName, null, null, null, null);
    }

    /**
     * @author: weilai
     * @date: 2021/5/31 下午1:40
     * @desc: 分页查询, 不带排序字段
     **/
    public QueryResult page(String indexName, ParamModel parm) {
        return query(indexName, parm, null, null, null);
    }

    /**
     * @author: weilai
     * @date: 2021/5/31 下午1:40
     * @desc: 分页查询, 带排序字段
     **/
    public QueryResult page(String indexName, ParamModel param, Map<String, Boolean> sortFieldsToAsc) {
        return query(indexName, param, sortFieldsToAsc, null, null);
    }

    /**
     * @param: 包括字段：includeFields
     * @param: 排除字段：excludeFields
     * @author: weilai
     * @date: 2021/5/31 下午1:40
     * @desc: 分页查询, 查询部分字段
     **/
    public QueryResult page(String indexName, ParamModel param, List<String> includeFields, List<String> excludeFields) {
        if (null == includeFields) {
            includeFields = Lists.newArrayList();
        }
        if (null == excludeFields) {
            excludeFields = Lists.newArrayList();
        }

        String[] include = includeFields.toArray(new String[includeFields.size()]);
        String[] exclude = excludeFields.toArray(new String[excludeFields.size()]);
        return query(indexName, param, null, include, exclude);

    }

    /**
     * @param: 包括字段：includeFields
     * @param: 排除字段：excludeFields
     * @author: weilai
     * @date: 2021/5/31 下午1:40
     * @desc: 分页查询, 带排序字段 和 查询部分字段
     **/
    public QueryResult page(String indexName, ParamModel parm, Map<String, Boolean> sortFieldsToAsc,
                            List<String> includeFields, List<String> excludeFields) {
        if (null == includeFields) {
            includeFields = Lists.newArrayList();
        }
        if (null == excludeFields) {
            excludeFields = Lists.newArrayList();
        }

        String[] include = includeFields.toArray(new String[includeFields.size()]);
        String[] exclude = excludeFields.toArray(new String[excludeFields.size()]);
        return query(indexName, parm, sortFieldsToAsc, include, exclude);

    }

    /**
     * @author: weilai
     * @date: 2021/6/1 下午1:42
     * @desc: 更新
     **/
    public void updateByModel(String indexName, Object javaBean, ParamModel param) {

        try {
            List<String> script = Lists.newArrayList();
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest(indexName).setBatchSize(100).setAbortOnVersionConflict(false);
            Map<String, Object> params = JSONObject.parseObject(JSON.toJSONString(javaBean),
                    new TypeReference<HashMap<String, Object>>() {
                    });

            for (Map.Entry<String, Object> map : params.entrySet()) {
                script.add("ctx._source." + map.getKey() + "=params." + map.getKey());//"params."  只能这样写
            }
            updateByQueryRequest.setScript(new Script(ScriptType.INLINE, "painless", StringUtils.join(script, ";"), params));
            BoolQueryBuilder query = buildQueryParm(param, sourceBuilder);

            updateByQueryRequest.setQuery(query);

            this.highLevelClient.updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @author: weilai
     * @date: 2021/6/2   下午12:12
     * @desc: <li> 删除indexName内的指定内容
     */
    public BulkByScrollResponse delete(String indexName, ParamModel param) {
        BulkByScrollResponse rp = null;
        try {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(indexName).setBatchSize(100).setAbortOnVersionConflict(false);
            BoolQueryBuilder query = buildQueryParm(param, sourceBuilder);
            deleteByQueryRequest.setQuery(query);
            rp = this.highLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rp;
    }

    /**
     * @ver: 1.0.0
     * @author: weilai
     * @date: 2021/6/2   下午12:38
     * @desc: <li> 删除索引(该索引内的文档一并被删除)： 使用 this.xxxRepo.deleteAll();只是删除索引内的文档，索引并没有删除
     */
    public void delIndex(String indexName) {

        try {
            GetIndexRequest re = new GetIndexRequest(indexName);
            boolean exists = this.highLevelClient.indices().exists(re, RequestOptions.DEFAULT);
            if (exists) {
                DeleteIndexRequest request = new DeleteIndexRequest(indexName);
                this.highLevelClient.indices().delete(request, RequestOptions.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @author: weilai
     * @date: 2021/5/31 下午4:49
     * @desc: 主要查询方法
     **/
    private QueryResult query(String indexName, ParamModel param, Map<String, Boolean> sortFieldsToAsc,
                              String[] includeFields, String[] excludeFields) {

        try {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.timeout(new TimeValue(50, TimeUnit.SECONDS));

            // 1、设置分页
            buildPage(param, sourceBuilder);

            // 2、构造查询条件
            BoolQueryBuilder query = buildQueryParm(param, sourceBuilder);

            // 3、设置排序字段
            buildSort(sortFieldsToAsc, sourceBuilder);

            // 4 、查询需要的列，或排除的列，即 select * 中的 *
            buildSelect(includeFields, excludeFields, sourceBuilder);

            // 5、开始查询
            CountRequest countRequest = new CountRequest(indexName).query(query);
            CountResponse countResponse = highLevelClient.count(countRequest, RequestOptions.DEFAULT);

            SearchRequest searchRequest = new SearchRequest(indexName).source(sourceBuilder);
            SearchResponse searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            // 6.1、查询失败 返回空白List
            if (searchResponse.status() != RestStatus.OK) {
                return new QueryResult(0L, Lists.newArrayList());
            }
            // 6.2、正常返回获取查询结果
            List<Map<String, Object>> result = Arrays.stream(searchResponse.getHits().getHits())
                    .map(SearchHit::getSourceAsMap).collect(Collectors.toList());
            return new QueryResult(countResponse.getCount(), result);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new QueryResult(0L, Lists.newArrayList());
    }

    /**
     * @author: weilai
     * @date: 2021/5/31 下午3:44
     * @desc: 设置分页信息
     **/
    private void buildPage(ParamModel parm, SearchSourceBuilder sb) {
        if (null != parm && parm.getPage() != null && null != parm.getLimit()) {
            sb.from((parm.getPage() - 1) * parm.getLimit());
            sb.size(parm.getLimit());
        }
    }

    /**
     * ang
     *
     * @author: weilai
     * @date: 2021/5/31 下午2:45
     * @desc: 设置排序: 字段是实体类属性，不是数据表的字段名称
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
     * @author: weilai
     * @date: 2021/5/31 下午1:54
     * @desc: 构建查询条件
     **/
    private BoolQueryBuilder buildQueryParm(ParamModel parm, SearchSourceBuilder sb) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (null != parm && StringUtils.isNotBlank(parm.getQueryStr())) {

            Map<String, String> params = JSONObject.parseObject(parm.getQueryStr(),
                    new TypeReference<HashMap<String, String>>() {
                    });

            for (Map.Entry<String, String> map : params.entrySet()) {

                if (StringUtils.isNotBlank(map.getValue())) {
                    String[] arry = StringUtils.split(map.getKey().replace("Q_", ""), "_");
                    getOpera(arry, map.getValue(), boolQueryBuilder);
                }
            }
            sb.query(boolQueryBuilder);
        }

        return boolQueryBuilder;
    }

    /**
     * @author: weilai
     * @date: 2021/5/31 下午2:58
     * @desc: 设置 查询那些属性,类似 select * 中的 *
     **/
    private void buildSelect(String[] includeFields, String[] excludeFields, SearchSourceBuilder sb) {
        sb.fetchSource(includeFields, excludeFields);
    }

    /**
     * @author: weilai
     * @date: 2021/5/31 下午7:08
     * @desc: 构建查询对象
     **/
    private void getOpera(String[] array, String val, BoolQueryBuilder boolQueryBuilder) {
        String column;

        if (array.length > 1) {
            column = Hump2underline.build(array[0]);
            String opera = array[1];

            switch (opera) {

                case "LK":// 模糊 like查询
                    boolQueryBuilder.must(QueryBuilders.wildcardQuery(column, val));
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
