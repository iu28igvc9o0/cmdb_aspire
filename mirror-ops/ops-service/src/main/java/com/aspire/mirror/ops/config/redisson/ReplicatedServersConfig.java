package com.aspire.mirror.ops.config.redisson;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ReplicatedServersConfig extends AbstractRedissonConfig {
	private Integer				threads;
	private Integer				nettyThreads;
	private String				codec;
	private String				transportMode;
	
	// replicatedServers specified configuration
	private Map<String, Object> replicatedServersConfig = new HashMap<>();
}
