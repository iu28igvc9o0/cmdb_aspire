package com.aspire.mirror.ops.config.redisson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aspire.mirror.ops.util.JsonUtil;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ClusterServersConfig extends AbstractRedissonConfig {
	private Integer				threads;
	private Integer				nettyThreads;
	private String				codec;
	private String				transportMode;

	// cluster servers specified configuration
	private Map<String, Object>	clusterServersConfig	= new HashMap<>();

	public final String formatConfig2JsonContent() {
		Map<String, String> nodeMap = (Map<String, String>) clusterServersConfig.get("nodeAddresses");
		List<String> nodeList = Lists.newArrayList();
		for (String nodeItem : nodeMap.values()) {
			nodeList.add(nodeItem);
		}
		clusterServersConfig.put("nodeAddresses", nodeList);
		return JsonUtil.toJacksonJson(this);
	}
}
