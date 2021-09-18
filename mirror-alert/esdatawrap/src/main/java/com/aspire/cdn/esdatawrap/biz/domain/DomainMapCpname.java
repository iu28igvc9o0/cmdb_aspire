package com.aspire.cdn.esdatawrap.biz.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aspire.cdn.esdatawrap.common.AbsPageQueryParamsAware;
import com.aspire.cdn.esdatawrap.util.JsonUtil;
import com.aspire.cdn.esdatawrap.util.Md5Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@JsonInclude(Include.NON_NULL)
public class DomainMapCpname implements IEsDocMarshall {
	private static final DateTimeFormatter	TIME_FORMAT		= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@JsonIgnore
	private String	id;

	@JsonProperty("req_domain")
	private String	reqDomain;

	@JsonProperty("cp_name")
	private String	cpname;
	
	@JsonProperty("update_time")
	private Long updateTime;
	
	
	/** 
	 * 功能描述: 由于域名的唯一性, 直接以域名MD5编码作为id  
	 * <p>
	 * @return
	 */
	@JsonProperty("id")
	public String getId() {
		if (id != null) {
			return id;
		}
		return (id = Md5Util.getMD5String(reqDomain));
	}
	
	@JsonProperty("format_update_time")
	public String getFormatUpdateTime() {
		if (updateTime == null) {
			return null;
		}
		LocalDateTime localTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(updateTime), ZoneId.systemDefault());
		return TIME_FORMAT.format(localTime);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!this.getClass().isInstance(obj)) {
			return false;
		}
		DomainMapCpname other = DomainMapCpname.class.cast(obj);
		return this.getId().equals(other.getId());
	}
	
	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}
	
	public Map<String, Object> buildUpsertRequestData() {
		Map<String, Object> upsertData = new HashMap<>();
		upsertData.put("doc", this);
		upsertData.put("doc_as_upsert", true);
		return upsertData;
	}
	
//		{ "index" : { "_index" : "test", "_type" : "_doc", "_id" : "1" } }
//		{ "field1" : "value1", "field2" : "value2" }
	@JsonIgnore
	public List<String> buildBulkIndexLines() {
		
		Map<String, Object> indexAttrs = new HashMap<String, Object>();
		indexAttrs.put("_index", "reqdomain_map_cpname");
		indexAttrs.put("_type", "doc");
		indexAttrs.put("_id", this.getId());
		
		List<String> lines = new ArrayList<>();
		lines.add(JsonUtil.toJacksonJson(Collections.singletonMap("index", indexAttrs)));
		lines.add(JsonUtil.toJacksonJson(this));
		return lines;
	}
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public static class DomainMapCpnameQueryModel extends AbsPageQueryParamsAware {
		
		@JsonProperty("req_domain_like")
		private String reqDomainLike;
		
		@JsonProperty("cpname_like")
		private String cpnameLike;
		
		@JsonProperty("order_column")
		private String orderColumn;
		
		@JsonProperty("order")
		private String order = "desc";		// asc | desc
		
		
		public int getOrderTypeVal() {
			if ("asc".equalsIgnoreCase(order)) {
				return 1;
			}
			return -1;
		}
	}
}
