package com.aspire.cdn.esdatawrap.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.aspire.cdn.esdatawrap.biz.domain.DomainMapCpname;
import com.aspire.cdn.esdatawrap.biz.domain.DomainMapCpname.DomainMapCpnameQueryModel;
import com.aspire.cdn.esdatawrap.common.GeneralResponse;
import com.aspire.cdn.esdatawrap.common.PageListQueryResult;
import com.aspire.cdn.esdatawrap.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jayway.jsonpath.DocumentContext;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: esdatawrap6 
 * <p/>
 * 
 * 类名: DomainMapCpNameHolder
 * <p/>
 *
 * 类功能描述: 域名与cp_name映射关系持有器
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年5月21日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
@Component
public final class DomainMapCpNameHolder {
//	private static final String					DOMAIN_MAP_CPNAME_FILE	= "reqDomain_map_cpName.json";
	public static final String					DOMAIN_MAP_CPNAME_INDEX	= "reqdomain_map_cpname";
	public static final String					KEY_EQUAL_WITH			= "equalwith";
	public static final String					KEY_END_WITH			= "endwith";
	private final ReentrantLock					lock					= new ReentrantLock();
	private final List<DomainMapCpname>			domainCpNameCache		= new ArrayList<>();
	@Autowired
	private RestHighLevelClient					restClient;
	@Autowired
	private LabelContentHolder 					labelHolder;
	
	@PostConstruct
	private void init() {
		try {
			initLoadData();
			log.info("Success load reqdomain-map-cpname with size {}.", domainCpNameCache.size());
		} catch (Exception e) {
			throw new RuntimeException("Error when load reqdomain-map-cpname from es.", e);
		}
	}
	
	private void initLoadData() throws Exception {
		Request request = new Request("Post", DOMAIN_MAP_CPNAME_INDEX + "/doc/_search?scroll=1m");
		String json = labelHolder.getContentByLabelKey(LabelContentHolder.KEY_SCROLL_LOAD_IDX_DATA);
		request.setJsonEntity(json);
		Response response = restClient.getLowLevelClient().performRequest(request);
		DocumentContext respCtx = JsonUtil.buildDefaultJsonPathContext(EntityUtils.toString(response.getEntity()));
		
		Map<String, Object> params = new HashMap<>();
		params.put("timeout", "1m");
		params.put("scrollId", respCtx.read("$._scroll_id"));
		
		boolean continueFlag = cacheScrollDataList(respCtx);
		while (continueFlag) {
			request = new Request("Post", "/_search/scroll");
			json = labelHolder.getContentByLabelKey(LabelContentHolder.KEY_SCROLL_FETCH_DATA, params);
			request.setJsonEntity(json);
			response = restClient.getLowLevelClient().performRequest(request);
			respCtx = JsonUtil.buildDefaultJsonPathContext(EntityUtils.toString(response.getEntity()));
			continueFlag = cacheScrollDataList(respCtx);
		}
	}
	
	private boolean cacheScrollDataList(DocumentContext respCtx) {
		List<Object> hitsList = respCtx.read("$.hits.hits");
		if (CollectionUtils.isEmpty(hitsList)) {
			return false;
		}
		for (int i = 0; i < hitsList.size(); i++) {
			Map<?, ?> record = respCtx.read("$.hits.hits[" + i + "]['_source']");
			DomainMapCpname mapData = JsonUtil.jacksonConvert(record, DomainMapCpname.class);
			domainCpNameCache.add(mapData);
		}
		return true;
	}
	
	private Map<String, Map<String, String>> group2MapByMatchWay() {
		List<DomainMapCpname> copyList = new ArrayList<>(domainCpNameCache);
		Map<String, Map<String, String>> group = new HashMap<>();
		Map<String, String> equalGroup = new HashMap<String, String>();
		Map<String, String> suffixGroup = new HashMap<String, String>();
		for (DomainMapCpname mapData : copyList) {
			String reqDomain = mapData.getReqDomain();
			if (reqDomain.startsWith("*")) {
				suffixGroup.put(reqDomain.substring(1), mapData.getCpname());
				continue;
			}
			equalGroup.put(reqDomain, mapData.getCpname());
		}
		group.put(KEY_EQUAL_WITH, equalGroup);
		group.put(KEY_END_WITH, suffixGroup);
		return group;
	}
	
	public Map<String, Map<String, String>> getDomainCpNameMatch() {
		return group2MapByMatchWay();
	}
	
	/** 
	 * 功能描述: 查询  域名-cp_name映射关系  列表  
	 * <p>
	 * @param queryParam
	 * @return
	 */
	public PageListQueryResult<DomainMapCpname> queryDomainMapCpnameList(DomainMapCpnameQueryModel queryParam) {
		List<DomainMapCpname> copyList = new ArrayList<>(domainCpNameCache);
		List<DomainMapCpname> filterList = new ArrayList<>();
		
		String reqDomainLike = queryParam.getReqDomainLike();
		String cpNameLike = queryParam.getCpnameLike();
		for (DomainMapCpname item : copyList) {
			if (StringUtils.isNotBlank(reqDomainLike) && item.getReqDomain().indexOf(reqDomainLike) < 0) {
				continue;
			}
			if (StringUtils.isNotBlank(cpNameLike) && item.getCpname().indexOf(cpNameLike) < 0) {
				continue;
			}
			filterList.add(item);
		}
		return resolvePageResult(queryParam, filterList);
	}
	
	@SuppressWarnings("unchecked")
	private PageListQueryResult<DomainMapCpname> resolvePageResult(
								DomainMapCpnameQueryModel queryParam, List<DomainMapCpname> filterList) {
		
		Integer startIdx = queryParam.getStartIdx();
		if (startIdx == null) {
			return new PageListQueryResult<DomainMapCpname>(filterList.size(), filterList);
		}
		if (startIdx >= filterList.size()) {
			return new PageListQueryResult<DomainMapCpname>(filterList.size(), new ArrayList<>());
		}
		
		int orderType = queryParam.getOrderTypeVal();
		String orderCol = queryParam.getOrderColumn();
		if (StringUtils.isNotBlank(orderCol)) {
			Field sortFeild = getAttrNameMatchField(DomainMapCpname.class, orderCol);
			sortFeild.setAccessible(true);
			Collections.sort(filterList, (o1, o2) -> {
				try {
					Object val1 = sortFeild.get(o1);
					Object val2 = sortFeild.get(o2);
					if (val1 == null) {
						return -1 * orderType;
					}
					if (val2 == null) {
						return 1 * orderType;
					}
					return Comparable.class.cast(val1).compareTo(Comparable.class.cast(val2)) * orderType;
				} catch (Exception e) {
					log.error(null, e);
					throw new RuntimeException(null, e);
				}
			});
		}
		
		int endIdx = startIdx + queryParam.getPageSize();
		if (endIdx >= filterList.size()) {
			endIdx = filterList.size();
		}
		return new PageListQueryResult<DomainMapCpname>(filterList.size(), filterList.subList(startIdx, endIdx));
	}
	
	private Field getAttrNameMatchField(Class<?> targetClazz, String attrName) {
    	Field[] fields = targetClazz.getDeclaredFields();
		for (Field field : fields) {
			JsonProperty anno = AnnotationUtils.findAnnotation(field, JsonProperty.class);
			if (anno == null || StringUtils.isBlank(anno.value())) {
				continue;
			}
			if (attrName.equals(anno.value())) {
				return field;
			}
		}

		for (Field field : fields) {
			if (field.getName().equals(attrName)) {
				return field;
			}
		}
		return null;
    }
	
	
	/** 
	 * 功能描述: 保存域名-cp_name映射关系  
	 * <p>
	 * @param mapData
	 * @return
	 */
	public GeneralResponse saveDomainMapCpname(DomainMapCpname mapData) {
		mapData.setUpdateTime(System.currentTimeMillis());
		Request request = new Request("Post", "/" + DOMAIN_MAP_CPNAME_INDEX + "/doc/" + mapData.getId() + "/_update");
		request.setJsonEntity(JsonUtil.toJacksonJson(mapData.buildUpsertRequestData()));
		Response response;
		try {
			lock.lock();
			response = restClient.getLowLevelClient().performRequest(request);
			String respJson = EntityUtils.toString(response.getEntity());
			if (log.isDebugEnabled()) {
				log.debug("SaveDomainMapCpname response: {}", respJson);
			}
			domainCpNameCache.remove(mapData);
			domainCpNameCache.add(mapData);
			return new GeneralResponse(true);
		} catch (Exception e) {
			log.error(null, e);
			return new GeneralResponse(false, e.getMessage());
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}
	
	/** 
	 * 功能描述: 根据id删除域名-cp_name映射关系    
	 * <p>
	 * @param id
	 * @return
	 */
	public GeneralResponse removeDomainMapCpname(String id) {
		try {
			lock.lock();
			DeleteRequest deleteReq = new DeleteRequest().index(DOMAIN_MAP_CPNAME_INDEX).type("doc").id(id);
			DeleteResponse deleteResponse = restClient.delete(deleteReq, RequestOptions.DEFAULT);
			Result result = deleteResponse.getResult();
			if (result == Result.DELETED) {
				DomainMapCpname removeData = new DomainMapCpname();
				removeData.setId(id);
				domainCpNameCache.remove(removeData);
			}
			return new GeneralResponse(true);
		} catch (Exception e) {
			log.error(null, e);
			return new GeneralResponse(false, e.getMessage());
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}
}
