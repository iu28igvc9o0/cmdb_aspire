package com.aspire.mirror.template.server.config.redisson;


import com.aspire.mirror.template.server.util.JsonUtil;

public abstract class AbstractRedissonConfig implements IRedisonConfigContentAware {
	@Override
	public String formatConfig2JsonContent() {
		return JsonUtil.toJacksonJson(this);
	}
}
