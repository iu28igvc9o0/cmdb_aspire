package com.aspire.mirror.alert.server.controller.cmdbInstance;

import com.aspire.mirror.alert.api.service.cmdbInstance.IAlertCmdbInstanceService;
import com.aspire.mirror.alert.server.biz.model.AlertFieldBiz;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.alert.server.dao.cmdbInstance.CmdbInstanceMapper;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@Slf4j
public class AlertCmdbInstanceController implements IAlertCmdbInstanceService {

    @Autowired
    private CmdbInstanceMapper cmdbInstanceMapper;
    @Autowired
    private AlertFieldBiz alertFieldBiz;
    @Override
    public Map<String, Object> detailById(@PathVariable("id") String id) {
        List<Map<String, Object>> list = cmdbInstanceMapper.detailsById(id);
        if (list.isEmpty()) {
            return Maps.newHashMap();
        }
        Map<String, Object> map = list.get(0);
        List<AlertFieldVo> cmdbModelFieldList = alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_DEVICE_INSTANCE);

        Map<String, Object> resultMap = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            if (!key.toLowerCase(Locale.getDefault()).endsWith("_name")) {
                if (!resultMap.containsKey(key)) {
                    resultMap.put(key, entry.getValue());
                }
                continue;
            }
            String preKey = key.substring(0, key.length() - 5);
            if (!map.containsKey(preKey)) {
                if (!resultMap.containsKey(key)) {
                    resultMap.put(key, entry.getValue());
                }
                continue;
            }
            if (CollectionUtils.isEmpty(cmdbModelFieldList)) {
                continue;
            }

            for (AlertFieldVo alertFieldVo : cmdbModelFieldList) {
                String fieldCode = alertFieldVo.getFieldCode();
                String ciCode = alertFieldVo.getCiCode();
                if (!key.equalsIgnoreCase(fieldCode)) {
                    continue;
                }

                if (ciCode.toLowerCase(Locale.getDefault()).endsWith("_name")) {
                    resultMap.put(preKey, entry.getValue());
                }
            }

        }

        return resultMap;
    }
}
