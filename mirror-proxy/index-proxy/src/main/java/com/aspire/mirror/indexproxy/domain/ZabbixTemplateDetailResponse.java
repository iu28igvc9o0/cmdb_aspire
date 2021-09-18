package com.aspire.mirror.indexproxy.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
public class ZabbixTemplateDetailResponse {
	
	@JsonProperty("templateid")
	private Integer templateId;
	
	@JsonProperty("name")
	private String templateName;

	@JsonProperty("items")
	private List<MonitorItemDetailResponse> items;

	@JsonProperty("discoveries")
	private List<MonitorItemDetailResponse> discoveries;

	@JsonProperty("hosts")
	private List<MonitorHost> hosts;

	@JsonProperty("proxy_identity")
	private String proxyIdentity;

	public MonitorTemplateRecord parseTemplateData() {
		MonitorTemplateRecord monitorTemplateVo = new MonitorTemplateRecord();
		monitorTemplateVo.setProxyIdentity(this.getProxyIdentity());
		monitorTemplateVo.setName(this.getTemplateName());
		monitorTemplateVo.setZabbixTemplateId(this.getTemplateId().toString());
		monitorTemplateVo.setDescription("");
		// 监控
		monitorTemplateVo.setFunType("1");
		// 自监控系统
		monitorTemplateVo.setMonType("3");
		// 正式
		monitorTemplateVo.setStatus("1");
		monitorTemplateVo.setSysType("AIOPS");
		// 主机
		monitorTemplateVo.setType("3");

		if (!CollectionUtils.isEmpty(this.getItems())) {
			List<MonitorItemRecord> itemList = Lists.newArrayList();
			for (MonitorItemDetailResponse item : this.items) {
				MonitorItemRecord monitorItemRecord = new MonitorItemRecord();
				monitorItemRecord.setDataType(item.getDataType());
				monitorItemRecord.setDelay(item.getDelay());
				monitorItemRecord.setZabbixItemId(item.getItemId());
				monitorItemRecord.setKey(item.getItemKey());
				monitorItemRecord.setName(item.getItemName());
				monitorItemRecord.setSysType("AIOPS");
				monitorItemRecord.setType("INDEX");
				monitorItemRecord.setUnits(item.getUnits());
				monitorItemRecord.setValueType(item.getValueType());
				// 默认启用
				monitorItemRecord.setStatus("ON");
				itemList.add(monitorItemRecord);
			}
			monitorTemplateVo.setItems(itemList);
		}
		monitorTemplateVo.setHosts(this.getHosts());
		return monitorTemplateVo;
	}
}
