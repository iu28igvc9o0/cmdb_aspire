package com.aspire.mirror.api;

import com.aspire.common.EnvConfigProperties;
import com.aspire.common.HttpUtil;
import com.aspire.mirror.util.SpringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ItemAPI {

    private static EnvConfigProperties configProperties;

    /**
     * 根据主题ID 获取主题详细信息
     * @param themeCode
     * @return
     */
    public static JSONArray getItemList(String themeCode) {
        JSONObject params = new JSONObject();
        params.accumulate("theme_code", themeCode);
        params.accumulate("page_size", "10000");
        Object result = HttpUtil.postMethod(getConfigProperties().getInterFace().getIndicationList(), null, params);
        if (result != null) {
            return JSONObject.fromObject(result).getJSONArray("result");
        }
        return new JSONArray();
    }

    public static EnvConfigProperties getConfigProperties() {
        if (configProperties == null) {
            configProperties = SpringUtil.getBean(EnvConfigProperties.class);
        }
        return configProperties;
    }
}
