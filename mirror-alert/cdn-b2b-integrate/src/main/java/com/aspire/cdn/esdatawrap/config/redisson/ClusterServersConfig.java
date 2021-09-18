package com.aspire.cdn.esdatawrap.config.redisson;

import java.util.HashMap;
import java.util.Map;

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
}
