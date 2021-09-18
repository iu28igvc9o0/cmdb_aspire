package com.aspire.mirror.theme.server.config.redisson;


import com.aspire.mirror.theme.server.util.JsonUtil;

public abstract class AbstractRedissonConfig implements IRedisonConfigContentAware {
	@Override
	public String formatConfig2JsonContent() {
		return JsonUtil.toJacksonJson(this);
	}
}
