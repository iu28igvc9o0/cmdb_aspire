package com.aspire.mirror.theme.server.config.redisson;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper=false)
public class MasterSlaveServersConfig extends AbstractRedissonConfig {
	private Integer				threads;
	private Integer				nettyThreads;
	private String				codec;
	private String				transportMode;

	// masterSlaveServers specified configuration
	private Map<String, Object>	masterSlaveServersConfig	= new HashMap<>();
}
