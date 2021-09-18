package com.aspire.ums.cmdb.v3.screen.web;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.v3.screen.ICmdbScreenAnswerInfoAPI;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenAnswerInfo;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenProblemInfo;
import com.aspire.ums.cmdb.v3.screen.service.ICmdbScreenAnswerInfoService;
import com.aspire.ums.cmdb.v3.screen.service.ICmdbScreenProblemInfoService;
import com.aspire.ums.cmdb.v3.screen.util.CmdbSmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @projectName: CmdbScreenAnswerInfoController
 * @description: 类
 * @author: luowenbo
 * @create: 2020-07-03 13:36
 **/
@RestController
public class CmdbScreenAnswerInfoController implements ICmdbScreenAnswerInfoAPI {

    @Autowired
    private ICmdbScreenAnswerInfoService cmdbScreenAnswerInfoService;
    @Autowired
    private ICmdbScreenProblemInfoService cmdbScreenProblemInfoService;
    @Autowired
    private CmdbSmsUtil cmdbSmsUtil;

    @Override
    public Map<String, Object> save(@RequestBody CmdbScreenAnswerInfo req,@RequestParam("isAdmin") Boolean isAdmin) {
        req.setIsAdmin(isAdmin.toString());
        Map<String, Object> rs = cmdbScreenAnswerInfoService.insert(req);
        // 发送短信
        if("true".equals(rs.get("flag").toString())) {
            cmdbSmsUtil.sendSms(req,isAdmin);
        }
        CmdbScreenProblemInfo problemInfo = new CmdbScreenProblemInfo();
        problemInfo.setId(req.getProblemId());
        // 管理员作处理，普通租户作追问处理
        problemInfo.setStatus(isAdmin?"已处理":"追问");
        cmdbScreenProblemInfoService.update(problemInfo);
        return rs;
    }

}
