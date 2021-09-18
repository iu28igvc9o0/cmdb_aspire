package com.aspire.ums.cmdb.v3.screen.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.util.HttpUtil;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenAnswerInfo;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenProblemInfo;
import com.aspire.ums.cmdb.v3.screen.service.ICmdbScreenProblemInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @projectName: CmdbSmsUtil
 * @description: 类
 * @author: luowenbo
 * @create: 2020-07-07 14:33
 **/
@Component
@Slf4j
public class CmdbSmsUtil {

    @Autowired
    private ICmdbConfigService configService;
    @Autowired
    private ICmdbScreenProblemInfoService cmdbScreenProblemInfoService;

    public void sendSms(CmdbScreenProblemInfo problem) {
        Map<String,Object> param = smsUrlPackageParams(problem);
        smsLayer(param,"department_screen_sms_template");
    }

    public void sendSms(CmdbScreenAnswerInfo answer,boolean isAdmin) {
        Map<String,Object> param = smsUrlPackageParams(answer,isAdmin);
        // 管理员进行处理，普通租户进行追加
        String templateName = isAdmin?"department_screen_sms_inform_template":"department_screen_sms_template";
        smsLayer(param,templateName);
    }

    public void smsLayer(Map<String,Object> paramMp,String templateName){
        // 从CMDB配置中心，获取URL
        CmdbConfig cmdbConfig = new CmdbConfig();
        cmdbConfig.setConfigCode("cmdb_sms_url");
        CmdbConfig smsConfig = configService.get(cmdbConfig);
        JSONObject header = new JSONObject();
        JSONObject params = JSON.parseObject(JSONObject.toJSONString(paramMp));
        // 调用短信发送接口
        HttpUtil.postMethod(smsConfig.getConfigValue() + templateName,header,params);
    }

    public Map<String,Object> smsUrlPackageParams(CmdbScreenProblemInfo problem){
        Map<String,Object> smsNotifyParams = new HashMap<>();
        List<String> mobile = new ArrayList<>();
        Map<String,String> params = new HashMap<>();
        // 获取通知人信息，向配置好的人员发送短信
        JSONObject accepterJson = getCmdbConfig("cmdb_sms_accepter");
        // 填充请求体参数 封装第一步
        mobile.add(accepterJson.getString("mobile"));
        params.put("department1",problem.getDepartment1());
        params.put("department2",problem.getDepartment2());
        params.put("username",problem.getUserName());
        // 封装第二步
        smsNotifyParams.put("mobiles",mobile);
        smsNotifyParams.put("params",params);
        // 封装第三步
        return smsNotifyParams;
    }

    public Map<String,Object> smsUrlPackageParams(CmdbScreenAnswerInfo answerInfo,boolean isAdmin){
        Map<String,Object> smsNotifyParams = new HashMap<>();
        List<String> mobile = new ArrayList<>();
        Map<String,String> params = new HashMap<>();
        if(isAdmin) {
            // 管理员进行处理，向提问者发出短信
            CmdbScreenProblemInfo questioner = cmdbScreenProblemInfoService.get(answerInfo.getProblemId());
            JSONObject accepterJson = getCmdbConfig("cmdb_sms_accepter");
            // 填充请求体参数 封装第一步
            mobile.add(questioner.getMobile());
            params.put("department1",accepterJson.getString("department1"));
            params.put("department2",accepterJson.getString("department2"));
            params.put("username",accepterJson.getString("username"));
        } else {
            // 普通用户追加，向配置的接收者发送短信
            // 填充请求体参数 封装第一步
            CmdbScreenProblemInfo questioner = cmdbScreenProblemInfoService.get(answerInfo.getProblemId());
            JSONObject accepterJson = getCmdbConfig("cmdb_sms_accepter");
            mobile.add(accepterJson.getString("mobile"));
            params.put("department1",questioner.getDepartment1());
            params.put("department2",questioner.getDepartment2());
            params.put("username",questioner.getUserName());
        }
        // 封装第二步
        smsNotifyParams.put("mobiles",mobile);
        smsNotifyParams.put("params",params);
        return smsNotifyParams;
    }

    public JSONObject getCmdbConfig(String code) {
        CmdbConfig cmdbConfig = new CmdbConfig();
        cmdbConfig.setConfigCode(code);
        CmdbConfig accepter = configService.get(cmdbConfig);
        JSONObject accepterJson = JSON.parseObject(accepter.getConfigValue());
        return accepterJson;
    }
}
