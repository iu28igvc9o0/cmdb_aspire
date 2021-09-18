package com.aspire.mirror.ops.config.redisson;

import com.aspire.mirror.ops.util.JsonUtil;

public abstract class AbstractRedissonConfig implements IRedisonConfigContentAware {
	@Override
	public String formatConfig2JsonContent() {
		return JsonUtil.toJacksonJson(this);
	}
}
