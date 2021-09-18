package com.aspire.mirror.indexproxy.config.redisson;

import com.aspire.mirror.indexproxy.util.JsonUtil;

public abstract class AbstractRedissonConfig implements IRedisonConfigContentAware {
	@Override
	public String formatConfig2JsonContent() {
		return JsonUtil.toJacksonJson(this);
	}
}
