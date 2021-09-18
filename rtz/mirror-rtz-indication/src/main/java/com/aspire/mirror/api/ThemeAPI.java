package com.aspire.mirror.api;

import com.aspire.common.EnvConfigProperties;
import com.aspire.common.HttpUtil;
import com.aspire.mirror.util.SpringUtil;

public class ThemeAPI {

    private static EnvConfigProperties configProperties;

    /**
     * 根据主题ID 获取主题详细信息
     * @param themeId
     * @return
     */
    public static String getThemeInfo(String themeId) {
        String requestUrl = getConfigProperties().getInterFace().getThemeInfo();
        requestUrl = requestUrl.replace("{theme_id}", themeId);
        return HttpUtil.getMethod(requestUrl, null);
    }

    public static EnvConfigProperties getConfigProperties() {
        if (configProperties == null) {
            configProperties = SpringUtil.getBean(EnvConfigProperties.class);
        }
        return configProperties;
    }
}
