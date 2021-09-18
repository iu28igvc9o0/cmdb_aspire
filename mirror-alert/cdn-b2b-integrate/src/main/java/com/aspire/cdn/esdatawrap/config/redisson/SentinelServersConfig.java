package com.aspire.cdn.esdatawrap.config.redisson;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SentinelServersConfig extends AbstractRedissonConfig {
	private Integer				threads;
	private Integer				nettyThreads;
	private String				codec;
	private String				transportMode;

	// sentinelServers specified configuration
	private Map<String, Object>	sentinelServersConfig	= new HashMap<>();
}
