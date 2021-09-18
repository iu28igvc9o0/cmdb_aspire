package com.aspire.mirror.template.server.config.redisson;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper=false)
public class SingleServerConfig extends AbstractRedissonConfig {
	private Integer				threads;
	private Integer				nettyThreads;
	private String				codec;
	private String				transportMode;

	// single server specified configuration
	private Map<String, Object>	singleServerConfig	= new HashMap<>();
}
