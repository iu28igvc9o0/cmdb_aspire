package com.aspire.ums.cmdb.bpmx.sendOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.ipCollect.entity.BpmAutoStartTaskOrderParam;
import com.aspire.ums.cmdb.ipCollect.entity.ExcelTemplateParam;
import com.aspire.ums.cmdb.ipCollect.mapper.CmdbIpCollectMapper;
import com.aspire.ums.cmdb.util.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动化采集异常设备-异常派通用任务工单到自动化平台组
 *
 * @author jiangxuwen
 * @date 2021/1/4 14:09
 */
@Component
@Slf4j
public class CmdbAutoStatusMonitorSendOrderTask extends AbstractBpmAutoOrderTask {

    @Autowired
    private CmdbIpCollectMapper ipCollectMapper;

    @Autowired
    private ConfigDictService configDictService;

    @Override
    public String getTaskName() {
        return "[每日巡检][自动化采集异常设备]";
    }

    @Override
    public ExcelTemplateParam buildExcelTemplateParam() {
        String[] headerArray = { "设备IP", "主备", "所属资源池", "所属独立业务", "所属独立业务子模块", "检测时间", "检测状态" };
        String[] keyArray = { "device_ip", "hostBackupName", "idcTypeName", "businessLevel1Name", "businessLevel2Name",
                "monitor_time", "auto_status" };
        String sheetName = "自动化采集异常设备列表";
        String filePath = "/opt/aspire/attachment/bmpx/";
        return new ExcelTemplateParam(keyArray, headerArray, sheetName, filePath);
    }

    @Override
    public List<Map<String, Object>> getPendingSendOrderList() {
        return ipCollectMapper.queryHostServerSendOrderList();
    }

    @Override
    public void updateOrderSendStatus(List<Map<String, Object>> resultList, Map<String, Object> resultMap) {
        String resultCode = (String) resultMap.get("code");
        if ("0".equals(resultCode)) {
            String orderNo = (String) resultMap.get("globalFlowNo");
            Map<String, Object> objectMap = Maps.newHashMap();
            objectMap.put("orderNo", orderNo);
            List<List<Map<String, Object>>> partition = Lists.partition(resultList, 200);
            for (List<Map<String, Object>> subList : partition) {
                objectMap.remove("idList");
                List<String> idList = Lists.newArrayList();
                for (Map<String, Object> map : subList) {
                    String id = map.get("id").toString();
                    idList.add(id);
                }
                objectMap.put("idList", idList);
                ipCollectMapper.updateHostServerOrderStatus(objectMap);
            }
        }
    }

    @Override
    public BpmAutoStartTaskOrderParam buildBpmAutoTaskParam() {
        // 自动化（agent/账号）状态巡检,派发通用任务单给主机组
        String date = DateUtils.format(new Date(), DateUtils.DATE_PLAIN_FORMAT);
        ConfigDict sqr = configDictService.getDictByColNameAndDictCode("auto_sqrId", "bpm派单-申请人ID");
        ConfigDict business1 = configDictService.getDictByColNameAndDictCode("auto_business1", "bpm派单-自动化独立业务线");
        ConfigDict business2 = configDictService.getDictByColNameAndDictCode("auto_business2", "bpm派单-自动化独立业务子模块");
        ConfigDict ydjkrRole = configDictService.getDictByColNameAndDictCode("auto_ydjkrRole", "bpm派单-自动化移动接口人角色ID");
        ConfigDict role = configDictService.getDictByColNameAndDictCode("auto_roleId", "bpm派单-自动化角色ID");
        String sqrId = sqr != null ? sqr.getValue() : getBpmConfiguration().getOsa2BpmAccount();// 网管
        String businessLevel1Id = business1 != null ? business1.getValue() : "1363fcd2e8f14f35cb2478ce73105f63";// 39_运维管理系统
        String businessLevel2Id = business2 != null ? business2.getValue() : "3b7e1b3da76146febdd28f2c27512169";// 02_自动化运维平台
        String ydjkrRoleID = ydjkrRole != null ? ydjkrRole.getValue() : "8503721131320320";
        String roleID = role != null ? role.getValue() : "10000040208038";
        String xqms = "请对[自动化采集异常设备]进行巡检，包括MM独立系统、用管中心、南基资源池、萝岗资源池、北方资源池等，并附上巡检报告";
        String sfxysjlsp = "2";
        String sfxybmldsp = "2";
        String gdbt = "[每日巡检][自动化采集异常设备]工单-" + date;
        return new BpmAutoStartTaskOrderParam(sqrId, gdbt, businessLevel1Id, businessLevel2Id, ydjkrRoleID, roleID, xqms, sfxysjlsp,
                sfxybmldsp);
    }
}
