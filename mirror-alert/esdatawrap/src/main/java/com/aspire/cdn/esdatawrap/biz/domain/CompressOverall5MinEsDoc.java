package com.aspire.cdn.esdatawrap.biz.domain;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jayway.jsonpath.DocumentContext;

import lombok.Data;

@Data
public class CompressOverall5MinEsDoc implements IEsDocMarshall {
	
	@JsonProperty("1xx_count")
	private Long	count1xx;
	
	@JsonProperty("200_count")
	private Long	count2xx;
	
	@JsonProperty("206_count")
	private Long	count206;
	
	@JsonProperty("301_count")
	private Long	count301;
	
	@JsonProperty("302_count")
	private Long	count302;
	
	@JsonProperty("403_count")
	private Long	count403;
	
	@JsonProperty("404_count")
	private Long	count404;
	
	@JsonProperty("4xx_count")
	private Long	count4xx;
	
	@JsonProperty("502_count")
	private Long	count502;
	
	@JsonProperty("503_count")
	private Long	count503;
	
	@JsonProperty("5xx_count")
	private Long	count5xx;
	
	@JsonProperty("cp_name")
	private String	cpName;
	
	@JsonProperty("fbt_sum")
	private Double	fbtSum;
	
	@JsonProperty("file_hit_count")
	private Long	fileHitCount;
	
	@JsonProperty("over_all_file_size")
	private Double	overAllFileSize;
	
	@JsonProperty("province_name")
	private String	provinceName;
	
	@JsonProperty("req_count")
	private Long	reqCount;
	
	@JsonProperty("req_domain")
	private String	reqDomain;
	
	@JsonProperty("send_time")
	private Double	sendTime;
	
	@JsonProperty("server_all_size")
	private Long	serverAllSize;
	
	@JsonProperty("service_type")
	private String	serviceType;
	
	@JsonProperty("timestamp")
	private Long	timestamp;
	
	
	public static CompressOverall5MinEsDoc popupFromBucket(String provinceName, Long marktime, DocumentContext bucketCtx) {
		CompressOverall5MinEsDoc model = new CompressOverall5MinEsDoc();
		String reqDomain = bucketCtx.read("$.key.req_domain_group");
		String serviceType = bucketCtx.read("$.key.service_type_group");
		String cpName = bucketCtx.read("$.unique_cp_name.buckets[0].key");
		model.setProvinceName(provinceName);
		model.setReqDomain(reqDomain);
		model.setServiceType(serviceType);
		model.setCpName(cpName);
		model.setTimestamp(marktime);
		
		Map<String, Field> map = getDirectReadFieldList();
		for (Entry<String, Field> entry : map.entrySet()) {
			Object attrWrap = bucketCtx.read("$['" + entry.getKey() + "']");
			if (attrWrap == null) {
				continue;
			}
			Object attrVal = bucketCtx.read("$['" + entry.getKey() + "']['value']");
			if (attrVal == null) {
				continue;
			}
			
			Field field = entry.getValue();
			field.setAccessible(true); 
			try {
				if (field.getType() == Long.class) {
					field.set(model, Number.class.cast(attrVal).longValue());
				} 
				else if (field.getType() == Double.class) {
					field.set(model, Number.class.cast(attrVal).doubleValue());
				} 
				else if (field.getType() == String.class) {
					field.set(model, String.valueOf(attrVal));
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return model;
	}
	
	private static Map<String, Field> getDirectReadFieldList() {
		Map<String, Field> directFieldList = new HashMap<>();
        Field[] fields = CompressOverall5MinEsDoc.class.getDeclaredFields();
        for (Field field : fields) {
            JsonProperty anno = AnnotationUtils.findAnnotation(field, JsonProperty.class);
            String attrName = field.getName();
            if (anno != null && StringUtils.isNotBlank(anno.value())) {
            	attrName = anno.value();
            }
            if ("timestamp".equals(attrName) || "cp_name".equals(attrName)
            		|| "province_name".equals(attrName) || "req_domain".endsWith(attrName) || "server_type".equals(attrName)) {
            	continue;
            }
            directFieldList.put(attrName, field);
        }
        return directFieldList;
    }
}
