package com.aspire.cdn.esdatawrap.config.redisson;

import com.aspire.cdn.esdatawrap.util.JsonUtil;

public abstract class AbstractRedissonConfig implements IRedisonConfigContentAware {
	@Override
	public final String formatConfig2JsonContent() {
		return JsonUtil.toJacksonJson(this);
	}
}
