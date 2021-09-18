package com.aspire.ums.cmdb.v3.screen.web;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.v3.screen.ICmdbScreenProblemInfoAPI;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenProblemInfo;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenProblemInfoRequest;
import com.aspire.ums.cmdb.v3.screen.service.ICmdbScreenProblemInfoService;
import com.aspire.ums.cmdb.v3.screen.util.CmdbSmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @projectName: CmdbScreenProblemInfoController
 * @description: 类
 * @author: luowenbo
 * @create: 2020-07-03 13:37
 **/
@RestController
public class CmdbScreenProblemInfoController implements ICmdbScreenProblemInfoAPI {

    @Autowired
    private ICmdbScreenProblemInfoService cmdbScreenProblemInfoService;
    @Autowired
    private CmdbSmsUtil cmdbSmsUtil;

    @Override
    public Map<String, Object> update(@RequestBody CmdbScreenProblemInfo req) {
        return cmdbScreenProblemInfoService.update(req);
    }

    @Override
    public Map<String,Object> save(@RequestBody CmdbScreenProblemInfo req) {
        Map<String,Object> rs = cmdbScreenProblemInfoService.insert(req);
        // 发送短信
        if("true".equals(rs.get("flag").toString())) {
            cmdbSmsUtil.sendSms(req);
        }
        return rs;
    }

    @Override
    public Result<CmdbScreenProblemInfo> list(@RequestBody CmdbScreenProblemInfoRequest req) {
        return cmdbScreenProblemInfoService.listByPage(req);
    }

    @Override
    public CmdbScreenProblemInfo listDetail(@PathVariable("id") String id) {
        return cmdbScreenProblemInfoService.get(id);
    }
}
