package com.aspire.cdn.esdatawrap.biz.report.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aspire.cdn.esdatawrap.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayway.jsonpath.DocumentContext;

import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 *
 * 项目名称: cdn-b2b-integrate 
 * <p/>
 * 
 * 类名: CustomOpsReportResponse
 * <p/>
 *
 * 类功能描述: 自定义报表查询响应对象
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年9月10日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@NoArgsConstructor
public class CustomOpsReportResponse {
	private String					reportKey;
	private final List<RespRowItem>	dataList	= new ArrayList<RespRowItem>();
	
	public static CustomOpsReportResponse resolveQueryResult(CustomOpsReportParam queryParam, DocumentContext respCtx) {
		CustomOpsReportResponse resp = new CustomOpsReportResponse();
		resp.setReportKey(queryParam.getReportKey());
		Integer bucketSize = respCtx.read("$.aggregations.termsDistrict.buckets.length()");
		if (bucketSize.intValue() == 0) {
			return resp;
		}
		for (int i = 0; i < bucketSize; i++) {
			final RespRowItem item = new RespRowItem();
			resp.addRespRowItem(item);
			String district = respCtx.read("$.aggregations.termsDistrict.buckets.[" + i + "].key");
			item.addAttr("district", district);
			if (CustomOpsReportParam.REPORT_KEY_BANDWIDTH.equals(queryParam.getReportKey())) {
				Double bandWidth = respCtx.read("$.aggregations.termsDistrict.buckets.[" + i + "].bandwidth.value");
				item.addAttr("bandWidth", trimSubnum(bandWidth, 2));
			} 
			else if (CustomOpsReportParam.REPORT_KEY_SERVICE_SUCC_PERCENT.equals(queryParam.getReportKey())) {
				Double okPercent = respCtx.read("$.aggregations.termsDistrict.buckets.[" + i + "].okPercent.value");
				item.addAttr("okPercent", trimSubnum(okPercent * 100, 2) + "%");
			}
			else if (CustomOpsReportParam.REPORT_KEY_FILE_HIT_SUC_PERCENT.equals(queryParam.getReportKey())) {
				Double fileHitPercent = respCtx.read("$.aggregations.termsDistrict.buckets.[" + i + "].fileHitPercent.value");
				item.addAttr("fileHitPercent", trimSubnum(fileHitPercent * 100, 2) + "%");
			}
			else if (CustomOpsReportParam.REPORT_KEY_REQUEST_COUNT.equals(queryParam.getReportKey())) {
				Long totalCount = respCtx.read("$.aggregations.termsDistrict.buckets.[" + i + "].totalCount.value", Number.class).longValue();
				item.addAttr("totalCount", totalCount);
			}
			else if (CustomOpsReportParam.REPORT_KEY_RETURN_STATUS_SUC_PERCENT.equals(queryParam.getReportKey())) {
				Double totalCount = respCtx.read("$.aggregations.termsDistrict.buckets.[" + i + "].doc_count", Number.class).doubleValue();
				Double trueCount = 0d;
				Integer innerBucketSize = respCtx.read("$.aggregations.termsDistrict.buckets.[" + i + "].statusGroup.buckets.length()");
				for (int k = 0; k < innerBucketSize; k++) {
					String returnStatus = respCtx.read("$.aggregations.termsDistrict.buckets.[" + i + "].statusGroup.buckets[" + k + "].key_as_string");
					if ("true".equals(returnStatus)) {
						trueCount = respCtx.read("$.aggregations.termsDistrict.buckets.[" + i + "].statusGroup.buckets[" + k + "].doc_count", Number.class).doubleValue();
						break;
					}
				}
				double returnSucPercent = totalCount == 0d ? 0d : trueCount/totalCount;
				item.addAttr("returnSucPercent", trimSubnum(returnSucPercent * 100, 2) + "%");
			}
			else if (CustomOpsReportParam.REPORT_KEY_INIT_BIT_DELAY_TIME.equals(queryParam.getReportKey())) {
				Double initDelayValue = respCtx.read("$.aggregations.termsDistrict.buckets.[" + i + "].initDelayValue.value");
				item.addAttr("initDelayValue", trimSubnum(initDelayValue, 2));
			}
			else if (CustomOpsReportParam.REPORT_KEY_DOWNLOAD_RATE.equals(queryParam.getReportKey())) {
				Double downloadRate = respCtx.read("$.aggregations.termsDistrict.buckets.[" + i + "].downloadRate.value");
				item.addAttr("downloadRate", trimSubnum(downloadRate, 2));
			}
			else if (CustomOpsReportParam.REPORT_KEY_HTTP_STATUS_WEIGTH.equals(queryParam.getReportKey())) {
				Double total200 = JsonUtil.readWithDefault(respCtx, "$.aggregations.termsDistrict.buckets.[" + i + "].total200.value", 0d);
				Double total206 = JsonUtil.readWithDefault(respCtx, "$.aggregations.termsDistrict.buckets.[" + i + "].total206.value", 0d);
				Double total301 = JsonUtil.readWithDefault(respCtx, "$.aggregations.termsDistrict.buckets.[" + i + "].total301.value", 0d);
				Double total302 = JsonUtil.readWithDefault(respCtx, "$.aggregations.termsDistrict.buckets.[" + i + "].total302.value", 0d);
				Double total4xx = JsonUtil.readWithDefault(respCtx, "$.aggregations.termsDistrict.buckets.[" + i + "].total4xx.value", 0d);
				Double total5xx = JsonUtil.readWithDefault(respCtx, "$.aggregations.termsDistrict.buckets.[" + i + "].total5xx.value", 0d);
				Double totalReq = JsonUtil.readWithDefault(respCtx, "$.aggregations.termsDistrict.buckets.[" + i + "].total_req.value", 0d);
				item.addAttr("percent_2xx", trimSubnum((total200 + total206)*100/totalReq, 2) + "%");
				item.addAttr("percent_3xx", trimSubnum((total301 + total302)*100/totalReq, 2) + "%");
				item.addAttr("percent_4xx", trimSubnum(total4xx*100/totalReq, 2) + "%");
				item.addAttr("percent_5xx", trimSubnum(total5xx*100/totalReq, 2) + "%");
			}
		}
		return resp;
	}
	
	/** 
	 * 功能描述: 精确到小数位  
	 * <p>
	 * @param source
	 * @return
	 */
	public static Double trimSubnum(Double source, int subNum) {
		if (source == null) {
			return 0d;
		}
		Double result = source.doubleValue() * Math.pow(10, subNum);
		Long powResult = result.longValue();
		return powResult / Math.pow(10, subNum);
	}
	
	public void addRespRowItem(RespRowItem item) {
		this.dataList.add(item);
	}
	
	public static class RespRowItem {
		@JsonIgnore
		private Map<String, Object> attrMap = new HashMap<>();
		
		@JsonAnySetter
		public void addAttr(String key, Object val) {
			attrMap.put(key, val);
		}
		
		@JsonAnyGetter
		public Map<String, Object> getAttrMap() {
			return attrMap;
		}
	}
}
