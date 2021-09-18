package com.aspire.mirror.inspection.server.config.redisson;

import com.aspire.mirror.inspection.server.util.JsonUtil;

public abstract class AbstractRedissonConfig implements IRedisonConfigContentAware {
	@Override
	public String formatConfig2JsonContent() {
		return JsonUtil.toJacksonJson(this);
	}

}
