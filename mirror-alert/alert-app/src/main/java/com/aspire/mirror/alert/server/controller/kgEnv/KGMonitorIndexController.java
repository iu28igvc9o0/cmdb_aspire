package com.aspire.mirror.alert.server.controller.kgEnv;

import com.aspire.mirror.alert.api.service.kgEnv.KGMonitorIndexService;
import com.aspire.mirror.alert.server.biz.kgEnv.KGMonitorIndexBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class KGMonitorIndexController implements KGMonitorIndexService {

    @Autowired
    private KGMonitorIndexBiz kgMonitorIndexBiz;

    @Override
    public List<Map<String, Object>> getAlertView(@RequestBody Map<String,Object> param) {
        return kgMonitorIndexBiz.getAlertView(param);
    }
}
