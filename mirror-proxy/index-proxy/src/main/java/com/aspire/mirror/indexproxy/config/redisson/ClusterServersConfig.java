package com.aspire.mirror.indexproxy.config.redisson;

import java.util.HashMap;
import java.util.Map;

import com.aspire.mirror.indexproxy.util.JsonUtil;

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
		Object rawConfig = clusterServersConfig.get("nodeAddresses");
		if (rawConfig != null && Map.class.isInstance(rawConfig)) {
			clusterServersConfig.put("nodeAddresses", Map.class.cast(rawConfig).values());
		}
		return JsonUtil.toJacksonJson(this);
	}
}
