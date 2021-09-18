package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.mainten;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.mainten.IMaintenHardWareUseService;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.maintenance.payload.HardwareUse;
import com.aspire.ums.cmdb.maintenance.payload.HardwareUseRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten.MaintenHardWareUseClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 类名称: MaintenHardWareUseController
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/8/4 13:44
 * 版本: v1.0
 */
@RestController
@Slf4j
public class MaintenHardWareUseController implements IMaintenHardWareUseService {

    @Autowired
    private MaintenHardWareUseClient hardwareUseClient;

    @Override
    public Map<String, Object> addHardwareUse(@RequestBody HardwareUse hardwareUse) {
        if (null != hardwareUse && StringUtils.isEmpty(hardwareUse.getCreater())) {
            RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
            String username = authCtx.getUser().getUsername();
            hardwareUse.setCreater(username);
        }
        Map<String, Object> rs = hardwareUseClient.addHardwareUse(hardwareUse);
        return rs;
    }

    @Override
    public Map<String, Object> batchAddHardwareUse(@RequestBody List<HardwareUse> list) {
        for (HardwareUse item : list) {
            if (null != item && StringUtils.isEmpty(item.getCreater())) {
                RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
                String username = authCtx.getUser().getUsername();
                item.setCreater(username);
            }
        }
        Map<String, Object> rs = hardwareUseClient.batchAddHardwareUse(list);
        return rs;
    }

    @Override
    public Result<HardwareUse> selectHardwareUseByPage(@RequestBody HardwareUseRequest request) {
        Result<HardwareUse> rs = hardwareUseClient.selectHardwareUseByPage(request);
        return rs;
    }

    @Override
    public Map<String, Object> updateHardwareUse(@RequestBody HardwareUse hardwareUse) {
        return hardwareUseClient.updateHardwareUse(hardwareUse);
    }

    @Override
    public Map<String, Object> batchUpdateHardwareUse(@RequestBody HardwareUse hardwareUseList) {
        return hardwareUseClient.batchUpdateHardwareUse(hardwareUseList);
    }

    @Override
    public Map<String, Object> deleteHardwareUse(@RequestParam("id") String id) {
        return hardwareUseClient.deleteHardwareUse(id);
    }

    @Override
    public List<Map<String, String>> getHardwareTableList() {
        return hardwareUseClient.getHardwareTableList();
    }

    @Override
    public Map<String, String> export(HttpServletResponse response, @RequestBody Map<String, Object> sendRequest) {
        Map<String, String> returnMap = new HashMap<>();
        try{
            Map<String, Object> reqParam = new HashMap<String, Object>();
            reqParam.put("system_name", sendRequest.get("system_name").toString());
            reqParam.put("device_name", sendRequest.get("device_name").toString());
            reqParam.put("warranty_date_before", sendRequest.get("warranty_date_before").toString());
            reqParam.put("warranty_date_after", sendRequest.get("warranty_date_after").toString());
            reqParam.put("device_serial_number", sendRequest.get("device_serial_number").toString());
            reqParam.put("assets_number", sendRequest.get("assets_number").toString());
            reqParam.put("serverProduce", sendRequest.get("serverProduce").toString());
            String type = String.valueOf(sendRequest.get("type"));
            reqParam.put("type", type);

            JSONObject returnJson = hardwareUseClient.export(reqParam);
            String[] headerList = {"资源池","一级部门","二级部门","业务系统","设备分类","设备类型",
                    "设备型号","设备名称","设备序列号","资产编号","服务厂家","服务人员","服务级别",
                    "开始时间","结束时间","处理时长","实际人天","移动审批人","运维审批人","创建人"};
            String[] keyList = {"resource_pool","department1","department2","system_name","device_classify","device_type",
                    "device_model","device_name","device_serial_number","assets_number","produce_name","server_person","server_level",
                    "start_date","end_date","process_time","actual_man_day","mobile_approver","operate_approver","creater"};

            List<Map<String, Object>> dataList = new LinkedList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            if (StringUtils.isNotEmpty(type) && "template".equals(type.toLowerCase(Locale.ENGLISH))) {
                headerList = new String[] {"设备序列号[必填]","出保时间[必填]","服务人员[必填]","服务级别[必填]",
                        "开始时间[必填]","结束时间[必填]","实际人天[必填]","移动审批人","运维审批人","创建人"};
                keyList = new String[] {"device_serial_number","warranty_date","server_person","server_level",
                        "start_date","end_date","actual_man_day","mobile_approver","operate_approver","creater"};
            } else {
                if (returnJson.containsKey("dataList") && returnJson.get("dataList") != null) {
                    List<Map<String, Object>> rsList = (List<Map<String, Object>>) returnJson.get("dataList");
                    for (Map<String, Object> map  : rsList) {

                        map.put("warranty_date", sdf.format(map.get("warranty_date")));

                        dataList.add(map);
                    }
                }
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            String fileName = dateFormat.format(new Date()) + "硬件维保使用列表.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            if (sendRequest != null && sendRequest.size() > 0) {
                // 取得输出流
                OutputStream os = response.getOutputStream();
                ExportExcelUtil eeu = new ExportExcelUtil();
                Workbook book = new SXSSFWorkbook(128);
                eeu.exportExcel(book, 0, "硬件维保使用列表", headerList, dataList, keyList);
                book.write(os);
                os.flush();
                os.close();
            }
            returnMap.put("code", "success");
            response.setStatus(HttpStatus.NO_CONTENT.value());
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
            returnMap.put("code", "error");
            returnMap.put("message", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return returnMap;
    }
}
