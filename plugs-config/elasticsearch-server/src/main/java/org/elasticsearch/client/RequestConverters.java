//package org.elasticsearch.client;
//
//import com.google.common.collect.Maps;
//import org.apache.http.HttpEntity;
//import org.apache.http.entity.ContentType;
//import org.apache.http.nio.entity.NByteArrayEntity;
//import org.apache.lucene.util.BytesRef;
//import org.elasticsearch.action.DocWriteRequest;
//import org.elasticsearch.action.DocWriteRequest.OpType;
//import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest.Level;
//import org.elasticsearch.action.admin.cluster.storedscripts.DeleteStoredScriptRequest;
//import org.elasticsearch.action.admin.cluster.storedscripts.GetStoredScriptRequest;
//import org.elasticsearch.action.admin.cluster.storedscripts.PutStoredScriptRequest;
//import org.elasticsearch.action.bulk.BulkRequest;
//import org.elasticsearch.action.delete.DeleteRequest;
//import org.elasticsearch.action.explain.ExplainRequest;
//import org.elasticsearch.action.fieldcaps.FieldCapabilitiesRequest;
//import org.elasticsearch.action.get.GetRequest;
//import org.elasticsearch.action.get.MultiGetRequest;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.search.ClearScrollRequest;
//import org.elasticsearch.action.search.MultiSearchRequest;
//import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.action.search.SearchScrollRequest;
//import org.elasticsearch.action.support.ActiveShardCount;
//import org.elasticsearch.action.support.IndicesOptions;
//import org.elasticsearch.action.update.UpdateRequest;
//import org.elasticsearch.client.core.CountRequest;
//import org.elasticsearch.client.core.MultiTermVectorsRequest;
//import org.elasticsearch.client.core.TermVectorsRequest;
//import org.elasticsearch.client.indices.AnalyzeRequest;
//import org.elasticsearch.client.security.RefreshPolicy;
//import org.elasticsearch.cluster.health.ClusterHealthStatus;
//import org.elasticsearch.common.Priority;
//import org.elasticsearch.common.Strings;
//import org.elasticsearch.common.bytes.BytesReference;
//import org.elasticsearch.common.xcontent.*;
//import org.elasticsearch.core.Nullable;
//import org.elasticsearch.core.SuppressForbidden;
//import org.elasticsearch.core.TimeValue;
//import org.elasticsearch.index.VersionType;
//import org.elasticsearch.index.rankeval.RankEvalRequest;
//import org.elasticsearch.index.reindex.AbstractBulkByScrollRequest;
//import org.elasticsearch.index.reindex.DeleteByQueryRequest;
//import org.elasticsearch.index.reindex.ReindexRequest;
//import org.elasticsearch.index.reindex.UpdateByQueryRequest;
//import org.elasticsearch.script.mustache.MultiSearchTemplateRequest;
//import org.elasticsearch.script.mustache.SearchTemplateRequest;
//import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
//import org.elasticsearch.tasks.TaskId;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.nio.charset.Charset;
//import java.util.*;
//
///**
// * @author: weilai
// * @date: 2021/5/31 下午12:29
// * @desc: 当前springcloud 集成的 RestHighLevelClient 版本是 7，而安装的es服务是6.8
// * @desc: 当查询一个index的全部内容时提示ccs_minimize_roundtrips 参数无法识别
// **/
//@SuppressWarnings({ "deprecation", "rawtypes" })
//final class RequestConverters {
//	static final XContentType REQUEST_BODY_CONTENT_TYPE;
//
//	private RequestConverters() {
//	}
//
//	static Request delete(DeleteRequest deleteRequest) {
//		String endpoint = endpoint(deleteRequest.index(), deleteRequest.type(), deleteRequest.id());
//		Request request = new Request("DELETE", endpoint);
//		RequestConverters.Params parameters = new RequestConverters.Params();
//		parameters.withRouting(deleteRequest.routing());
//		parameters.withTimeout(deleteRequest.timeout());
//		parameters.withVersion(deleteRequest.version());
//		parameters.withVersionType(deleteRequest.versionType());
//		parameters.withIfSeqNo(deleteRequest.ifSeqNo());
//		parameters.withIfPrimaryTerm(deleteRequest.ifPrimaryTerm());
//		parameters.withRefreshPolicy(deleteRequest.getRefreshPolicy());
//		parameters.withWaitForActiveShards(deleteRequest.waitForActiveShards());
//		request.addParameters(parameters.asMap());
//		return request;
//	}
//
//	static Request info() {
//		return new Request("GET", "/");
//	}
//
//	static Request bulk(BulkRequest bulkRequest) throws IOException {
//		Request request = new Request("POST", "/_bulk");
//		RequestConverters.Params parameters = new RequestConverters.Params();
//		parameters.withTimeout(bulkRequest.timeout());
//		parameters.withRefreshPolicy(bulkRequest.getRefreshPolicy());
//		parameters.withPipeline(bulkRequest.pipeline());
//		parameters.withRouting(bulkRequest.routing());
//		XContentType bulkContentType = null;
//
//		for (int i = 0; i < bulkRequest.numberOfActions(); ++i) {
//
//			DocWriteRequest<?> action = (DocWriteRequest) bulkRequest.requests().get(i);
//			OpType opType = action.opType();
//			if (opType != OpType.INDEX && opType != OpType.CREATE) {
//				if (opType == OpType.UPDATE) {
//					UpdateRequest updateRequest = (UpdateRequest) action;
//					if (updateRequest.doc() != null) {
//						bulkContentType = enforceSameContentType(updateRequest.doc(), bulkContentType);
//					}
//
//					if (updateRequest.upsertRequest() != null) {
//						bulkContentType = enforceSameContentType(updateRequest.upsertRequest(), bulkContentType);
//					}
//				}
//			} else {
//				bulkContentType = enforceSameContentType((IndexRequest) action, bulkContentType);
//			}
//		}
//
//		if (bulkContentType == null) {
//			bulkContentType = XContentType.JSON;
//		}
//
//		byte separator = bulkContentType.xContent().streamSeparator();
//		ContentType requestContentType = createContentType(bulkContentType);
//		ByteArrayOutputStream content = new ByteArrayOutputStream();
//		Iterator var27 = bulkRequest.requests().iterator();
//
//		while (var27.hasNext()) {
//			DocWriteRequest<?> action = (DocWriteRequest) var27.next();
//			OpType opType = action.opType();
//			XContentBuilder metadata = XContentBuilder.builder(bulkContentType.xContent());
//
//			try {
//				metadata.startObject();
//				metadata.startObject(opType.getLowercase());
//				if (Strings.hasLength(action.index())) {
//					metadata.field("_index", action.index());
//				}
//
//				if (Strings.hasLength(action.type()) && !"_doc".equals(action.type())) {
//					metadata.field("_type", action.type());
//				}
//
//				if (Strings.hasLength(action.id())) {
//					metadata.field("_id", action.id());
//				}
//
//				if (Strings.hasLength(action.routing())) {
//					metadata.field("routing", action.routing());
//				}
//
//				if (action.version() != -3L) {
//					metadata.field("version", action.version());
//				}
//
//				VersionType versionType = action.versionType();
//				if (versionType != VersionType.INTERNAL) {
//					if (versionType == VersionType.EXTERNAL) {
//						metadata.field("version_type", "external");
//					} else if (versionType == VersionType.EXTERNAL_GTE) {
//						metadata.field("version_type", "external_gte");
//					}
//				}
//
//				if (action.ifSeqNo() != -2L) {
//					metadata.field("if_seq_no", action.ifSeqNo());
//					metadata.field("if_primary_term", action.ifPrimaryTerm());
//				}
//
//				if (opType != OpType.INDEX && opType != OpType.CREATE) {
//					if (opType == OpType.UPDATE) {
//						UpdateRequest updateRequest = (UpdateRequest) action;
//						if (updateRequest.retryOnConflict() > 0) {
//							metadata.field("retry_on_conflict", updateRequest.retryOnConflict());
//						}
//
//						if (updateRequest.fetchSource() != null) {
//							metadata.field("_source", updateRequest.fetchSource());
//						}
//					}
//				} else {
//					IndexRequest indexRequest = (IndexRequest) action;
//					if (Strings.hasLength(indexRequest.getPipeline())) {
//						metadata.field("pipeline", indexRequest.getPipeline());
//					}
//				}
//
//				metadata.endObject();
//				metadata.endObject();
//				BytesRef metadataSource = BytesReference.bytes(metadata).toBytesRef();
//				content.write(metadataSource.bytes, metadataSource.offset, metadataSource.length);
//				content.write(separator);
//			} catch (Throwable var23) {
//				if (metadata != null) {
//					try {
//						metadata.close();
//					} catch (Throwable var18) {
//						var23.addSuppressed(var18);
//					}
//				}
//
//				throw var23;
//			}
//
//			if (metadata != null) {
//				metadata.close();
//			}
//
//			BytesRef source = null;
//			if (opType != OpType.INDEX && opType != OpType.CREATE) {
//				if (opType == OpType.UPDATE) {
//					source = XContentHelper.toXContent((UpdateRequest) action, bulkContentType, false).toBytesRef();
//				}
//			} else {
//				IndexRequest indexRequest = (IndexRequest) action;
//				BytesReference indexSource = indexRequest.source();
//				XContentType indexXContentType = indexRequest.getContentType();
//				XContentParser parser = XContentHelper.createParser(NamedXContentRegistry.EMPTY,
//						DeprecationHandler.THROW_UNSUPPORTED_OPERATION, indexSource, indexXContentType);
//
//				try {
//					XContentBuilder builder = XContentBuilder.builder(bulkContentType.xContent());
//
//					try {
//						builder.copyCurrentStructure(parser);
//						source = BytesReference.bytes(builder).toBytesRef();
//					} catch (Throwable var21) {
//						if (builder != null) {
//							try {
//								builder.close();
//							} catch (Throwable var20) {
//								var21.addSuppressed(var20);
//							}
//						}
//
//						throw var21;
//					}
//
//					if (builder != null) {
//						builder.close();
//					}
//				} catch (Throwable var22) {
//					if (parser != null) {
//						try {
//							parser.close();
//						} catch (Throwable var19) {
//							var22.addSuppressed(var19);
//						}
//					}
//
//					throw var22;
//				}
//
//				if (parser != null) {
//					parser.close();
//				}
//			}
//
//			if (source != null) {
//				content.write(source.bytes, source.offset, source.length);
//				content.write(separator);
//			}
//		}
//
//		request.addParameters(parameters.asMap());
//		request.setEntity(new NByteArrayEntity(content.toByteArray(), 0, content.size(), requestContentType));
//		return request;
//	}
//
//	static Request exists(GetRequest getRequest) {
//		return getStyleRequest("HEAD", getRequest);
//	}
//
//	static Request get(GetRequest getRequest) {
//		return getStyleRequest("GET", getRequest);
//	}
//
//	private static Request getStyleRequest(String method, GetRequest getRequest) {
//		Request request = new Request(method, endpoint(getRequest.index(), getRequest.type(), getRequest.id()));
//		RequestConverters.Params parameters = new RequestConverters.Params();
//		parameters.withPreference(getRequest.preference());
//		parameters.withRouting(getRequest.routing());
//		parameters.withRefresh(getRequest.refresh());
//		parameters.withRealtime(getRequest.realtime());
//		parameters.withStoredFields(getRequest.storedFields());
//		parameters.withVersion(getRequest.version());
//		parameters.withVersionType(getRequest.versionType());
//		parameters.withFetchSourceContext(getRequest.fetchSourceContext());
//		request.addParameters(parameters.asMap());
//		return request;
//	}
//
//	static Request sourceExists(GetRequest getRequest) {
//		String optionalType = getRequest.type();
//		String endpoint;
//		if (optionalType.equals("_doc")) {
//			endpoint = endpoint(getRequest.index(), "_source", getRequest.id());
//		} else {
//			endpoint = endpoint(getRequest.index(), optionalType, getRequest.id(), "_source");
//		}
//
//		Request request = new Request("HEAD", endpoint);
//		RequestConverters.Params parameters = new RequestConverters.Params();
//		parameters.withPreference(getRequest.preference());
//		parameters.withRouting(getRequest.routing());
//		parameters.withRefresh(getRequest.refresh());
//		parameters.withRealtime(getRequest.realtime());
//		request.addParameters(parameters.asMap());
//		return request;
//	}
//
//	static Request multiGet(MultiGetRequest multiGetRequest) throws IOException {
//		Request request = new Request("POST", "/_mget");
//		RequestConverters.Params parameters = new RequestConverters.Params();
//		parameters.withPreference(multiGetRequest.preference());
//		parameters.withRealtime(multiGetRequest.realtime());
//		parameters.withRefresh(multiGetRequest.refresh());
//		request.addParameters(parameters.asMap());
//		request.setEntity(createEntity(multiGetRequest, REQUEST_BODY_CONTENT_TYPE));
//		return request;
//	}
//
//	static Request index(IndexRequest indexRequest) {
//		String method = Strings.hasLength(indexRequest.id()) ? "PUT" : "POST";
//		String endpoint;
//		if (indexRequest.opType() == OpType.CREATE) {
//			endpoint = indexRequest.type().equals("_doc") ? endpoint(indexRequest.index(), "_create", indexRequest.id())
//					: endpoint(indexRequest.index(), indexRequest.type(), indexRequest.id(), "_create");
//		} else {
//			endpoint = endpoint(indexRequest.index(), indexRequest.type(), indexRequest.id());
//		}
//
//		Request request = new Request(method, endpoint);
//		RequestConverters.Params parameters = new RequestConverters.Params();
//		parameters.withRouting(indexRequest.routing());
//		parameters.withTimeout(indexRequest.timeout());
//		parameters.withVersion(indexRequest.version());
//		parameters.withVersionType(indexRequest.versionType());
//		parameters.withIfSeqNo(indexRequest.ifSeqNo());
//		parameters.withIfPrimaryTerm(indexRequest.ifPrimaryTerm());
//		parameters.withPipeline(indexRequest.getPipeline());
//		parameters.withRefreshPolicy(indexRequest.getRefreshPolicy());
//		parameters.withWaitForActiveShards(indexRequest.waitForActiveShards());
//		BytesRef source = indexRequest.source().toBytesRef();
//		ContentType contentType = createContentType(indexRequest.getContentType());
//		request.addParameters(parameters.asMap());
//		request.setEntity(new NByteArrayEntity(source.bytes, source.offset, source.length, contentType));
//		return request;
//	}
//
//	static Request ping() {
//		return new Request("HEAD", "/");
//	}
//
//	static Request update(UpdateRequest updateRequest) throws IOException {
//		String endpoint = updateRequest.type().equals("_doc")
//				? endpoint(updateRequest.index(), "_update", updateRequest.id())
//				: endpoint(updateRequest.index(), updateRequest.type(), updateRequest.id(), "_update");
//		Request request = new Request("POST", endpoint);
//		RequestConverters.Params parameters = new RequestConverters.Params();
//		parameters.withRouting(updateRequest.routing());
//		parameters.withTimeout(updateRequest.timeout());
//		parameters.withRefreshPolicy(updateRequest.getRefreshPolicy());
//		parameters.withWaitForActiveShards(updateRequest.waitForActiveShards());
//		parameters.withDocAsUpsert(updateRequest.docAsUpsert());
//		parameters.withFetchSourceContext(updateRequest.fetchSource());
//		parameters.withRetryOnConflict(updateRequest.retryOnConflict());
//		parameters.withVersion(updateRequest.version());
//		parameters.withVersionType(updateRequest.versionType());
//		XContentType xContentType = null;
//		if (updateRequest.doc() != null) {
//			xContentType = updateRequest.doc().getContentType();
//		}
//
//		if (updateRequest.upsertRequest() != null) {
//			XContentType upsertContentType = updateRequest.upsertRequest().getContentType();
//			if (xContentType != null && xContentType != upsertContentType) {
//				throw new IllegalStateException("Update request cannot have different content types for doc ["
//						+ xContentType + "] and upsert [" + upsertContentType + "] documents");
//			}
//
//			xContentType = upsertContentType;
//		}
//
//		if (xContentType == null) {
//			xContentType = Requests.INDEX_CONTENT_TYPE;
//		}
//
//		request.addParameters(parameters.asMap());
//		request.setEntity(createEntity(updateRequest, xContentType));
//		return request;
//	}
//
//	static Request search(SearchRequest searchRequest, String searchEndpoint) throws IOException {
//		Request request = new Request("POST", endpoint(searchRequest.indices(), searchRequest.types(), searchEndpoint));
//		RequestConverters.Params params = new RequestConverters.Params();
//		addSearchRequestParams(params, searchRequest);
//		if (searchRequest.source() != null) {
//			request.setEntity(createEntity(searchRequest.source(), REQUEST_BODY_CONTENT_TYPE));
//		}
//
//		request.addParameters(params.asMap());
//		return request;
//	}
//
//	private static void addSearchRequestParams(RequestConverters.Params params, SearchRequest searchRequest) {
//		params.putParam("typed_keys", "true");
//		params.withRouting(searchRequest.routing());
//		params.withPreference(searchRequest.preference());
//		params.withIndicesOptions(searchRequest.indicesOptions());
//		params.putParam("search_type", searchRequest.searchType().name().toLowerCase(Locale.ROOT));
//		// params.putParam("ccs_minimize_roundtrips",
//		// Boolean.toString(searchRequest.isCcsMinimizeRoundtrips()));
//		params.putParam("pre_filter_shard_size", Integer.toString(searchRequest.getPreFilterShardSize()));
//		params.putParam("max_concurrent_shard_requests",
//				Integer.toString(searchRequest.getMaxConcurrentShardRequests()));
//		if (searchRequest.requestCache() != null) {
//			params.putParam("request_cache", Boolean.toString(searchRequest.requestCache()));
//		}
//
//		if (searchRequest.allowPartialSearchResults() != null) {
//			params.putParam("allow_partial_search_results",
//					Boolean.toString(searchRequest.allowPartialSearchResults()));
//		}
//
//		params.putParam("batched_reduce_size", Integer.toString(searchRequest.getBatchedReduceSize()));
//		if (searchRequest.scroll() != null) {
//			params.putParam("scroll", searchRequest.scroll().keepAlive());
//		}
//
//	}
//
//	static Request searchScroll(SearchScrollRequest searchScrollRequest) throws IOException {
//		Request request = new Request("POST", "/_search/scroll");
//		request.setEntity(createEntity(searchScrollRequest, REQUEST_BODY_CONTENT_TYPE));
//		return request;
//	}
//
//	static Request clearScroll(ClearScrollRequest clearScrollRequest) throws IOException {
//		Request request = new Request("DELETE", "/_search/scroll");
//		request.setEntity(createEntity(clearScrollRequest, REQUEST_BODY_CONTENT_TYPE));
//		return request;
//	}
//
//	static Request multiSearch(MultiSearchRequest multiSearchRequest) throws IOException {
//		Request request = new Request("POST", "/_msearch");
//		RequestConverters.Params params = new RequestConverters.Params();
//		params.putParam("typed_keys", "true");
//		if (multiSearchRequest.maxConcurrentSearchRequests() != 0) {
//			params.putParam("max_concurrent_searches",
//					Integer.toString(multiSearchRequest.maxConcurrentSearchRequests()));
//		}
//
//		XContent xContent = REQUEST_BODY_CONTENT_TYPE.xContent();
//		byte[] source = MultiSearchRequest.writeMultiLineFormat(multiSearchRequest, xContent);
//		request.addParameters(params.asMap());
//		request.setEntity(new NByteArrayEntity(source, createContentType(xContent.type())));
//		return request;
//	}
//
//	static Request searchTemplate(SearchTemplateRequest searchTemplateRequest) throws IOException {
//		Request request;
//		if (searchTemplateRequest.isSimulate()) {
//			request = new Request("GET", "_render/template");
//		} else {
//			SearchRequest searchRequest = searchTemplateRequest.getRequest();
//			String endpoint = endpoint(searchRequest.indices(), searchRequest.types(), "_search/template");
//			request = new Request("GET", endpoint);
//			RequestConverters.Params params = new RequestConverters.Params();
//			addSearchRequestParams(params, searchRequest);
//			request.addParameters(params.asMap());
//		}
//
//		request.setEntity(createEntity(searchTemplateRequest, REQUEST_BODY_CONTENT_TYPE));
//		return request;
//	}
//
//	static Request multiSearchTemplate(MultiSearchTemplateRequest multiSearchTemplateRequest) throws IOException {
//		Request request = new Request("POST", "/_msearch/template");
//		RequestConverters.Params params = new RequestConverters.Params();
//		params.putParam("typed_keys", "true");
//		if (multiSearchTemplateRequest.maxConcurrentSearchRequests() != 0) {
//			params.putParam("max_concurrent_searches",
//					Integer.toString(multiSearchTemplateRequest.maxConcurrentSearchRequests()));
//		}
//
//		request.addParameters(params.asMap());
//		XContent xContent = REQUEST_BODY_CONTENT_TYPE.xContent();
//		byte[] source = MultiSearchTemplateRequest.writeMultiLineFormat(multiSearchTemplateRequest, xContent);
//		request.setEntity(new NByteArrayEntity(source, createContentType(xContent.type())));
//		return request;
//	}
//
//	static Request count(CountRequest countRequest) throws IOException {
//		Request request = new Request("POST", endpoint(countRequest.indices(), countRequest.types(), "_count"));
//		RequestConverters.Params params = new RequestConverters.Params();
//		params.withRouting(countRequest.routing());
//		params.withPreference(countRequest.preference());
//		params.withIndicesOptions(countRequest.indicesOptions());
//		if (countRequest.terminateAfter() != 0) {
//			params.withTerminateAfter(countRequest.terminateAfter());
//		}
//
//		if (countRequest.minScore() != null) {
//			params.putParam("min_score", String.valueOf(countRequest.minScore()));
//		}
//
//		request.addParameters(params.asMap());
//		request.setEntity(createEntity(countRequest, REQUEST_BODY_CONTENT_TYPE));
//		return request;
//	}
//
//	static Request explain(ExplainRequest explainRequest) throws IOException {
//		String endpoint = explainRequest.type().equals("_doc")
//				? endpoint(explainRequest.index(), "_explain", explainRequest.id())
//				: endpoint(explainRequest.index(), explainRequest.type(), explainRequest.id(), "_explain");
//		Request request = new Request("GET", endpoint);
//		RequestConverters.Params params = new RequestConverters.Params();
//		params.withStoredFields(explainRequest.storedFields());
//		params.withFetchSourceContext(explainRequest.fetchSourceContext());
//		params.withRouting(explainRequest.routing());
//		params.withPreference(explainRequest.preference());
//		request.addParameters(params.asMap());
//		request.setEntity(createEntity(explainRequest, REQUEST_BODY_CONTENT_TYPE));
//		return request;
//	}
//
//	static Request fieldCaps(FieldCapabilitiesRequest fieldCapabilitiesRequest) {
//		Request request = new Request("GET", endpoint(fieldCapabilitiesRequest.indices(), "_field_caps"));
//		RequestConverters.Params params = new RequestConverters.Params();
//		params.withFields(fieldCapabilitiesRequest.fields());
//		params.withIndicesOptions(fieldCapabilitiesRequest.indicesOptions());
//		request.addParameters(params.asMap());
//		return request;
//	}
//
//	static Request rankEval(RankEvalRequest rankEvalRequest) throws IOException {
//		Request request = new Request("GET", endpoint(rankEvalRequest.indices(), Strings.EMPTY_ARRAY, "_rank_eval"));
//		RequestConverters.Params params = new RequestConverters.Params();
//		params.withIndicesOptions(rankEvalRequest.indicesOptions());
//		params.putParam("search_type", rankEvalRequest.searchType().name().toLowerCase(Locale.ROOT));
//		request.addParameters(params.asMap());
//		request.setEntity(createEntity(rankEvalRequest.getRankEvalSpec(), REQUEST_BODY_CONTENT_TYPE));
//		return request;
//	}
//
//	static Request reindex(ReindexRequest reindexRequest) throws IOException {
//		return prepareReindexRequest(reindexRequest, true);
//	}
//
//	static Request submitReindex(ReindexRequest reindexRequest) throws IOException {
//		return prepareReindexRequest(reindexRequest, false);
//	}
//
//	static Request submitDeleteByQuery(DeleteByQueryRequest deleteByQueryRequest) throws IOException {
//		return prepareDeleteByQueryRequest(deleteByQueryRequest, false);
//	}
//
//	private static Request prepareReindexRequest(ReindexRequest reindexRequest, boolean waitForCompletion)
//			throws IOException {
//		String endpoint = (new RequestConverters.EndpointBuilder()).addPathPart("_reindex").build();
//		Request request = new Request("POST", endpoint);
//		RequestConverters.Params params = (new RequestConverters.Params()).withWaitForCompletion(waitForCompletion)
//				.withRefresh(reindexRequest.isRefresh()).withTimeout(reindexRequest.getTimeout())
//				.withWaitForActiveShards(reindexRequest.getWaitForActiveShards())
//				.withRequestsPerSecond(reindexRequest.getRequestsPerSecond()).withSlices(reindexRequest.getSlices());
//		if (reindexRequest.getScrollTime() != null) {
//			params.putParam("scroll", reindexRequest.getScrollTime());
//		}
//
//		request.addParameters(params.asMap());
//		request.setEntity(createEntity(reindexRequest, REQUEST_BODY_CONTENT_TYPE));
//		return request;
//	}
//
//	private static Request prepareDeleteByQueryRequest(DeleteByQueryRequest deleteByQueryRequest,
//			boolean waitForCompletion) throws IOException {
//		String endpoint = endpoint(deleteByQueryRequest.indices(), deleteByQueryRequest.getDocTypes(),
//				"_delete_by_query");
//		Request request = new Request("POST", endpoint);
//		RequestConverters.Params params = (new RequestConverters.Params())
//				.withRouting(deleteByQueryRequest.getRouting()).withRefresh(deleteByQueryRequest.isRefresh())
//				.withTimeout(deleteByQueryRequest.getTimeout())
//				.withWaitForActiveShards(deleteByQueryRequest.getWaitForActiveShards())
//				.withRequestsPerSecond(deleteByQueryRequest.getRequestsPerSecond())
//				.withIndicesOptions(deleteByQueryRequest.indicesOptions()).withWaitForCompletion(waitForCompletion)
//				.withSlices(deleteByQueryRequest.getSlices());
//		if (!deleteByQueryRequest.isAbortOnVersionConflict()) {
//			params.putParam("conflicts", "proceed");
//		}
//
//		if (deleteByQueryRequest.getBatchSize() != 1000) {
//			params.putParam("scroll_size", Integer.toString(deleteByQueryRequest.getBatchSize()));
//		}
//
//		if (deleteByQueryRequest.getScrollTime() != AbstractBulkByScrollRequest.DEFAULT_SCROLL_TIMEOUT) {
//			params.putParam("scroll", deleteByQueryRequest.getScrollTime());
//		}
//
//		if (deleteByQueryRequest.getMaxDocs() > 0) {
//			params.putParam("max_docs", Integer.toString(deleteByQueryRequest.getMaxDocs()));
//		}
//
//		request.addParameters(params.asMap());
//		request.setEntity(createEntity(deleteByQueryRequest, REQUEST_BODY_CONTENT_TYPE));
//		return request;
//	}
//
//	static Request updateByQuery(UpdateByQueryRequest updateByQueryRequest) throws IOException {
//		String endpoint = endpoint(updateByQueryRequest.indices(), updateByQueryRequest.getDocTypes(),
//				"_update_by_query");
//		Request request = new Request("POST", endpoint);
//		RequestConverters.Params params = (new RequestConverters.Params())
//				.withRouting(updateByQueryRequest.getRouting()).withPipeline(updateByQueryRequest.getPipeline())
//				.withRefresh(updateByQueryRequest.isRefresh()).withTimeout(updateByQueryRequest.getTimeout())
//				.withWaitForActiveShards(updateByQueryRequest.getWaitForActiveShards())
//				.withRequestsPerSecond(updateByQueryRequest.getRequestsPerSecond())
//				.withIndicesOptions(updateByQueryRequest.indicesOptions()).withSlices(updateByQueryRequest.getSlices());
//		if (!updateByQueryRequest.isAbortOnVersionConflict()) {
//			params.putParam("conflicts", "proceed");
//		}
//
//		if (updateByQueryRequest.getBatchSize() != 1000) {
//			params.putParam("scroll_size", Integer.toString(updateByQueryRequest.getBatchSize()));
//		}
//
//		if (updateByQueryRequest.getScrollTime() != AbstractBulkByScrollRequest.DEFAULT_SCROLL_TIMEOUT) {
//			params.putParam("scroll", updateByQueryRequest.getScrollTime());
//		}
//
//		if (updateByQueryRequest.getMaxDocs() > 0) {
//			params.putParam("max_docs", Integer.toString(updateByQueryRequest.getMaxDocs()));
//		}
//
//		request.addParameters(params.asMap());
//		request.setEntity(createEntity(updateByQueryRequest, REQUEST_BODY_CONTENT_TYPE));
//		return request;
//	}
//
//	static Request deleteByQuery(DeleteByQueryRequest deleteByQueryRequest) throws IOException {
//		return prepareDeleteByQueryRequest(deleteByQueryRequest, true);
//	}
//
//	static Request rethrottleReindex(RethrottleRequest rethrottleRequest) {
//		return rethrottle(rethrottleRequest, "_reindex");
//	}
//
//	static Request rethrottleUpdateByQuery(RethrottleRequest rethrottleRequest) {
//		return rethrottle(rethrottleRequest, "_update_by_query");
//	}
//
//	static Request rethrottleDeleteByQuery(RethrottleRequest rethrottleRequest) {
//		return rethrottle(rethrottleRequest, "_delete_by_query");
//	}
//
//	private static Request rethrottle(RethrottleRequest rethrottleRequest, String firstPathPart) {
//		String endpoint = (new RequestConverters.EndpointBuilder()).addPathPart(firstPathPart)
//				.addPathPart(rethrottleRequest.getTaskId().toString()).addPathPart("_rethrottle").build();
//		Request request = new Request("POST", endpoint);
//		RequestConverters.Params params = (new RequestConverters.Params())
//				.withRequestsPerSecond(rethrottleRequest.getRequestsPerSecond());
//		params.putParam("group_by", "none");
//		request.addParameters(params.asMap());
//		return request;
//	}
//
//	static Request putScript(PutStoredScriptRequest putStoredScriptRequest) throws IOException {
//		String endpoint = (new RequestConverters.EndpointBuilder()).addPathPartAsIs("_scripts")
//				.addPathPart(putStoredScriptRequest.id()).build();
//		Request request = new Request("POST", endpoint);
//		RequestConverters.Params params = new RequestConverters.Params();
//		params.withTimeout(putStoredScriptRequest.timeout());
//		params.withMasterTimeout(putStoredScriptRequest.masterNodeTimeout());
//		if (Strings.hasText(putStoredScriptRequest.context())) {
//			params.putParam("context", putStoredScriptRequest.context());
//		}
//
//		request.addParameters(params.asMap());
//		request.setEntity(createEntity(putStoredScriptRequest, REQUEST_BODY_CONTENT_TYPE));
//		return request;
//	}
//
//	static Request analyze(AnalyzeRequest request) throws IOException {
//		RequestConverters.EndpointBuilder builder = new RequestConverters.EndpointBuilder();
//		String index = request.index();
//		if (index != null) {
//			builder.addPathPart(index);
//		}
//
//		builder.addPathPartAsIs("_analyze");
//		Request req = new Request("GET", builder.build());
//		req.setEntity(createEntity(request, REQUEST_BODY_CONTENT_TYPE));
//		return req;
//	}
//
//	static Request termVectors(TermVectorsRequest tvrequest) throws IOException {
//		String endpoint;
//		if (tvrequest.getType() != null) {
//			endpoint = (new RequestConverters.EndpointBuilder())
//					.addPathPart(tvrequest.getIndex(), tvrequest.getType(), tvrequest.getId())
//					.addPathPartAsIs("_termvectors").build();
//		} else {
//			endpoint = (new RequestConverters.EndpointBuilder()).addPathPart(tvrequest.getIndex())
//					.addPathPartAsIs("_termvectors").addPathPart(tvrequest.getId()).build();
//		}
//
//		Request request = new Request("GET", endpoint);
//		RequestConverters.Params params = new RequestConverters.Params();
//		params.withRouting(tvrequest.getRouting());
//		params.withPreference(tvrequest.getPreference());
//		params.withFields(tvrequest.getFields());
//		params.withRealtime(tvrequest.getRealtime());
//		request.addParameters(params.asMap());
//		request.setEntity(createEntity(tvrequest, REQUEST_BODY_CONTENT_TYPE));
//		return request;
//	}
//
//	static Request mtermVectors(MultiTermVectorsRequest mtvrequest) throws IOException {
//		String endpoint = "_mtermvectors";
//		Request request = new Request("GET", endpoint);
//		request.setEntity(createEntity(mtvrequest, REQUEST_BODY_CONTENT_TYPE));
//		return request;
//	}
//
//	static Request getScript(GetStoredScriptRequest getStoredScriptRequest) {
//		String endpoint = (new RequestConverters.EndpointBuilder()).addPathPartAsIs("_scripts")
//				.addPathPart(getStoredScriptRequest.id()).build();
//		Request request = new Request("GET", endpoint);
//		RequestConverters.Params params = new RequestConverters.Params();
//		params.withMasterTimeout(getStoredScriptRequest.masterNodeTimeout());
//		request.addParameters(params.asMap());
//		return request;
//	}
//
//	static Request deleteScript(DeleteStoredScriptRequest deleteStoredScriptRequest) {
//		String endpoint = (new RequestConverters.EndpointBuilder()).addPathPartAsIs("_scripts")
//				.addPathPart(deleteStoredScriptRequest.id()).build();
//		Request request = new Request("DELETE", endpoint);
//		RequestConverters.Params params = new RequestConverters.Params();
//		params.withTimeout(deleteStoredScriptRequest.timeout());
//		params.withMasterTimeout(deleteStoredScriptRequest.masterNodeTimeout());
//		request.addParameters(params.asMap());
//		return request;
//	}
//
//	static HttpEntity createEntity(ToXContent toXContent, XContentType xContentType) throws IOException {
//		return createEntity(toXContent, xContentType, ToXContent.EMPTY_PARAMS);
//	}
//
//	static HttpEntity createEntity(ToXContent toXContent, XContentType xContentType,
//			org.elasticsearch.common.xcontent.ToXContent.Params toXContentParams) throws IOException {
//		BytesRef source = XContentHelper.toXContent(toXContent, xContentType, toXContentParams, false).toBytesRef();
//		return new NByteArrayEntity(source.bytes, source.offset, source.length, createContentType(xContentType));
//	}
//
//	static String endpoint(String index, String type, String id) {
//		return (new RequestConverters.EndpointBuilder()).addPathPart(index, type, id).build();
//	}
//
//	static String endpoint(String index, String type, String id, String endpoint) {
//		return (new RequestConverters.EndpointBuilder()).addPathPart(index, type, id).addPathPartAsIs(endpoint).build();
//	}
//
//	static String endpoint(String[] indices) {
//		return (new RequestConverters.EndpointBuilder()).addCommaSeparatedPathParts(indices).build();
//	}
//
//	static String endpoint(String[] indices, String endpoint) {
//		return (new RequestConverters.EndpointBuilder()).addCommaSeparatedPathParts(indices).addPathPartAsIs(endpoint)
//				.build();
//	}
//
//	static String endpoint(String[] indices, String[] types, String endpoint) {
//		return (new RequestConverters.EndpointBuilder()).addCommaSeparatedPathParts(indices)
//				.addCommaSeparatedPathParts(types).addPathPartAsIs(endpoint).build();
//	}
//
//	static String endpoint(String[] indices, String endpoint, String[] suffixes) {
//		return (new RequestConverters.EndpointBuilder()).addCommaSeparatedPathParts(indices).addPathPartAsIs(endpoint)
//				.addCommaSeparatedPathParts(suffixes).build();
//	}
//
//	static String endpoint(String[] indices, String endpoint, String type) {
//		return (new RequestConverters.EndpointBuilder()).addCommaSeparatedPathParts(indices).addPathPartAsIs(endpoint)
//				.addPathPart(type).build();
//	}
//
//	@SuppressForbidden(reason = "Only allowed place to convert a XContentType to a ContentType")
//	public static ContentType createContentType(XContentType xContentType) {
//		return ContentType.create(xContentType.mediaTypeWithoutParameters(), (Charset) null);
//	}
//
//	static XContentType enforceSameContentType(IndexRequest indexRequest, @Nullable XContentType xContentType) {
//		XContentType requestContentType = indexRequest.getContentType();
//		if (requestContentType != XContentType.JSON && requestContentType != XContentType.SMILE) {
//			throw new IllegalArgumentException("Unsupported content-type found for request with content-type ["
//					+ requestContentType + "], only JSON and SMILE are supported");
//		} else if (xContentType == null) {
//			return requestContentType;
//		} else if (requestContentType != xContentType) {
//			throw new IllegalArgumentException("Mismatching content-type found for request with content-type ["
//					+ requestContentType + "], previous requests have content-type [" + xContentType + "]");
//		} else {
//			return xContentType;
//		}
//	}
//
//	static {
//		REQUEST_BODY_CONTENT_TYPE = XContentType.JSON;
//	}
//
//	static class Params {
//		private final Map<String, String> parameters = Maps.newHashMap();
//
//		Params() {
//		}
//
//		RequestConverters.Params putParam(String name, String value) {
//			if (Strings.hasLength(value)) {
//				this.parameters.put(name, value);
//			}
//
//			return this;
//		}
//
//		RequestConverters.Params putParam(String key, TimeValue value) {
//			return value != null ? this.putParam(key, value.getStringRep()) : this;
//		}
//
//		Map<String, String> asMap() {
//			return this.parameters;
//		}
//
//		RequestConverters.Params withDocAsUpsert(boolean docAsUpsert) {
//			return docAsUpsert ? this.putParam("doc_as_upsert", Boolean.TRUE.toString()) : this;
//		}
//
//		RequestConverters.Params withFetchSourceContext(FetchSourceContext fetchSourceContext) {
//			if (fetchSourceContext != null) {
//				if (!fetchSourceContext.fetchSource()) {
//					this.putParam("_source", Boolean.FALSE.toString());
//				}
//
//				if (fetchSourceContext.includes() != null && fetchSourceContext.includes().length > 0) {
//					this.putParam("_source_includes", String.join(",", fetchSourceContext.includes()));
//				}
//
//				if (fetchSourceContext.excludes() != null && fetchSourceContext.excludes().length > 0) {
//					this.putParam("_source_excludes", String.join(",", fetchSourceContext.excludes()));
//				}
//			}
//
//			return this;
//		}
//
//		RequestConverters.Params withFields(String[] fields) {
//			return fields != null && fields.length > 0 ? this.putParam("fields", String.join(",", fields)) : this;
//		}
//
//		RequestConverters.Params withMasterTimeout(TimeValue masterTimeout) {
//			return this.putParam("master_timeout", masterTimeout);
//		}
//
//		RequestConverters.Params withPipeline(String pipeline) {
//			return this.putParam("pipeline", pipeline);
//		}
//
//		RequestConverters.Params withPreference(String preference) {
//			return this.putParam("preference", preference);
//		}
//
//		RequestConverters.Params withRealtime(boolean realtime) {
//			return !realtime ? this.putParam("realtime", Boolean.FALSE.toString()) : this;
//		}
//
//		RequestConverters.Params withRefresh(boolean refresh) {
//			return refresh ? this.withRefreshPolicy(RefreshPolicy.IMMEDIATE) : this;
//		}
//
//		/**
//		 * @deprecated
//		 */
//		@Deprecated
//		RequestConverters.Params withRefreshPolicy(
//				org.elasticsearch.action.support.WriteRequest.RefreshPolicy refreshPolicy) {
//			return refreshPolicy != org.elasticsearch.action.support.WriteRequest.RefreshPolicy.NONE
//					? this.putParam("refresh", refreshPolicy.getValue())
//					: this;
//		}
//
//		RequestConverters.Params withRefreshPolicy(RefreshPolicy refreshPolicy) {
//			return refreshPolicy != RefreshPolicy.NONE ? this.putParam("refresh", refreshPolicy.getValue()) : this;
//		}
//
//		RequestConverters.Params withRequestsPerSecond(float requestsPerSecond) {
//			return Float.isFinite(requestsPerSecond)
//					? this.putParam("requests_per_second", Float.toString(requestsPerSecond))
//					: this.putParam("requests_per_second", "-1");
//		}
//
//		RequestConverters.Params withRetryOnConflict(int retryOnConflict) {
//			return retryOnConflict > 0 ? this.putParam("retry_on_conflict", String.valueOf(retryOnConflict)) : this;
//		}
//
//		RequestConverters.Params withRouting(String routing) {
//			return this.putParam("routing", routing);
//		}
//
//		RequestConverters.Params withSlices(int slices) {
//			return slices == 0 ? this.putParam("slices", "auto") : this.putParam("slices", String.valueOf(slices));
//		}
//
//		RequestConverters.Params withStoredFields(String[] storedFields) {
//			return storedFields != null && storedFields.length > 0
//					? this.putParam("stored_fields", String.join(",", storedFields))
//					: this;
//		}
//
//		RequestConverters.Params withTerminateAfter(int terminateAfter) {
//			return this.putParam("terminate_after", String.valueOf(terminateAfter));
//		}
//
//		RequestConverters.Params withTimeout(TimeValue timeout) {
//			return this.putParam("timeout", timeout);
//		}
//
//		RequestConverters.Params withVersion(long version) {
//			return version != -3L ? this.putParam("version", Long.toString(version)) : this;
//		}
//
//		RequestConverters.Params withVersionType(VersionType versionType) {
//			return versionType != VersionType.INTERNAL
//					? this.putParam("version_type", versionType.name().toLowerCase(Locale.ROOT))
//					: this;
//		}
//
//		RequestConverters.Params withIfSeqNo(long ifSeqNo) {
//			return ifSeqNo != -2L ? this.putParam("if_seq_no", Long.toString(ifSeqNo)) : this;
//		}
//
//		RequestConverters.Params withIfPrimaryTerm(long ifPrimaryTerm) {
//			return ifPrimaryTerm != 0L ? this.putParam("if_primary_term", Long.toString(ifPrimaryTerm)) : this;
//		}
//
//		RequestConverters.Params withWaitForActiveShards(ActiveShardCount activeShardCount) {
//			return this.withWaitForActiveShards(activeShardCount, ActiveShardCount.DEFAULT);
//		}
//
//		RequestConverters.Params withWaitForActiveShards(ActiveShardCount activeShardCount,
//				ActiveShardCount defaultActiveShardCount) {
//			return activeShardCount != null && activeShardCount != defaultActiveShardCount
//					? this.putParam("wait_for_active_shards", activeShardCount.toString().toLowerCase(Locale.ROOT))
//					: this;
//		}
//
//		RequestConverters.Params withIndicesOptions(IndicesOptions indicesOptions) {
//			if (indicesOptions != null) {
//				this.withIgnoreUnavailable(indicesOptions.ignoreUnavailable());
//				this.putParam("allow_no_indices", Boolean.toString(indicesOptions.allowNoIndices()));
//				String expandWildcards;
//				if (!indicesOptions.expandWildcardsOpen() && !indicesOptions.expandWildcardsClosed()) {
//					expandWildcards = "none";
//				} else {
//					StringJoiner joiner = new StringJoiner(",");
//					if (indicesOptions.expandWildcardsOpen()) {
//						joiner.add("open");
//					}
//
//					if (indicesOptions.expandWildcardsClosed()) {
//						joiner.add("closed");
//					}
//
//					expandWildcards = joiner.toString();
//				}
//
//				this.putParam("expand_wildcards", expandWildcards);
//				this.putParam("ignore_throttled", Boolean.toString(indicesOptions.ignoreThrottled()));
//			}
//
//			return this;
//		}
//
//		RequestConverters.Params withIgnoreUnavailable(boolean ignoreUnavailable) {
//			this.putParam("ignore_unavailable", Boolean.toString(ignoreUnavailable));
//			return this;
//		}
//
//		RequestConverters.Params withHuman(boolean human) {
//			if (human) {
//				this.putParam("human", Boolean.toString(human));
//			}
//
//			return this;
//		}
//
//		RequestConverters.Params withLocal(boolean local) {
//			if (local) {
//				this.putParam("local", Boolean.toString(local));
//			}
//
//			return this;
//		}
//
//		RequestConverters.Params withIncludeDefaults(boolean includeDefaults) {
//			return includeDefaults ? this.putParam("include_defaults", Boolean.TRUE.toString()) : this;
//		}
//
//		RequestConverters.Params withPreserveExisting(boolean preserveExisting) {
//			return preserveExisting ? this.putParam("preserve_existing", Boolean.TRUE.toString()) : this;
//		}
//
//		RequestConverters.Params withDetailed(boolean detailed) {
//			return detailed ? this.putParam("detailed", Boolean.TRUE.toString()) : this;
//		}
//
//		RequestConverters.Params withWaitForCompletion(Boolean waitForCompletion) {
//			return this.putParam("wait_for_completion", waitForCompletion.toString());
//		}
//
//		RequestConverters.Params withNodes(String[] nodes) {
//			return this.withNodes(Arrays.asList(nodes));
//		}
//
//		RequestConverters.Params withNodes(List<String> nodes) {
//			return nodes != null && nodes.size() > 0 ? this.putParam("nodes", String.join(",", nodes)) : this;
//		}
//
//		RequestConverters.Params withActions(String[] actions) {
//			return this.withActions(Arrays.asList(actions));
//		}
//
//		RequestConverters.Params withActions(List<String> actions) {
//			return actions != null && actions.size() > 0 ? this.putParam("actions", String.join(",", actions)) : this;
//		}
//
//		RequestConverters.Params withTaskId(TaskId taskId) {
//			return taskId != null && taskId.isSet() ? this.putParam("task_id", taskId.toString()) : this;
//		}
//
//		RequestConverters.Params withParentTaskId(TaskId parentTaskId) {
//			return parentTaskId != null && parentTaskId.isSet()
//					? this.putParam("parent_task_id", parentTaskId.toString())
//					: this;
//		}
//
//		RequestConverters.Params withTaskId(org.elasticsearch.client.tasks.TaskId taskId) {
//			return taskId != null && taskId.isSet() ? this.putParam("task_id", taskId.toString()) : this;
//		}
//
//		RequestConverters.Params withParentTaskId(org.elasticsearch.client.tasks.TaskId parentTaskId) {
//			return parentTaskId != null && parentTaskId.isSet()
//					? this.putParam("parent_task_id", parentTaskId.toString())
//					: this;
//		}
//
//		RequestConverters.Params withWaitForStatus(ClusterHealthStatus status) {
//			return status != null ? this.putParam("wait_for_status", status.name().toLowerCase(Locale.ROOT)) : this;
//		}
//
//		RequestConverters.Params withWaitForNoRelocatingShards(boolean waitNoRelocatingShards) {
//			return waitNoRelocatingShards ? this.putParam("wait_for_no_relocating_shards", Boolean.TRUE.toString())
//					: this;
//		}
//
//		RequestConverters.Params withWaitForNoInitializingShards(boolean waitNoInitShards) {
//			return waitNoInitShards ? this.putParam("wait_for_no_initializing_shards", Boolean.TRUE.toString()) : this;
//		}
//
//		RequestConverters.Params withWaitForNodes(String waitForNodes) {
//			return this.putParam("wait_for_nodes", waitForNodes);
//		}
//
//		RequestConverters.Params withLevel(Level level) {
//			return this.putParam("level", level.name().toLowerCase(Locale.ROOT));
//		}
//
//		RequestConverters.Params withWaitForEvents(Priority waitForEvents) {
//			return waitForEvents != null
//					? this.putParam("wait_for_events", waitForEvents.name().toLowerCase(Locale.ROOT))
//					: this;
//		}
//	}
//
//	static class EndpointBuilder {
//		private final StringJoiner joiner = new StringJoiner("/", "/", "");
//
//		EndpointBuilder() {
//		}
//
//		RequestConverters.EndpointBuilder addPathPart(String... parts) {
//			String[] var2 = parts;
//			int var3 = parts.length;
//
//			for (int var4 = 0; var4 < var3; ++var4) {
//				String part = var2[var4];
//				if (Strings.hasLength(part)) {
//					this.joiner.add(encodePart(part));
//				}
//			}
//
//			return this;
//		}
//
//		RequestConverters.EndpointBuilder addCommaSeparatedPathParts(String[] parts) {
//			this.addPathPart(String.join(",", parts));
//			return this;
//		}
//
//		RequestConverters.EndpointBuilder addCommaSeparatedPathParts(List<String> parts) {
//			this.addPathPart(String.join(",", parts));
//			return this;
//		}
//
//		RequestConverters.EndpointBuilder addPathPartAsIs(String... parts) {
//			String[] var2 = parts;
//			int var3 = parts.length;
//
//			for (int var4 = 0; var4 < var3; ++var4) {
//				String part = var2[var4];
//				if (Strings.hasLength(part)) {
//					this.joiner.add(part);
//				}
//			}
//
//			return this;
//		}
//
//		String build() {
//			return this.joiner.toString();
//		}
//
//		private static String encodePart(String pathPart) {
//			try {
//				URI uri = new URI((String) null, "", "/" + pathPart, (String) null, (String) null);
//				return uri.getRawPath().substring(1).replaceAll("/", "%2F");
//			} catch (URISyntaxException var2) {
//				throw new IllegalArgumentException("Path part [" + pathPart + "] couldn't be encoded", var2);
//			}
//		}
//	}
//}
