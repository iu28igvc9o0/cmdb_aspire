package com.aspire.mirror.indexadapt.config.redisson;

import com.aspire.mirror.indexadapt.util.JsonUtil;

public abstract class AbstractRedissonConfig implements IRedisonConfigContentAware {
	@Override
	public String formatConfig2JsonContent() {
		return JsonUtil.toJacksonJson(this);
	}
}
