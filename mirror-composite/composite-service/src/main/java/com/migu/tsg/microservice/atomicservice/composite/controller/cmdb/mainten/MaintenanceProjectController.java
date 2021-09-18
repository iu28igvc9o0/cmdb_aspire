package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.mainten;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.mainten.ICompMaintenanceProjectAPI;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.payload.*;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten.MaintenanceProjectClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: MaintenanceProjectController
 * Author:   zhu.juwang
 * Date:     2019/8/5 10:13
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class MaintenanceProjectController implements ICompMaintenanceProjectAPI {

    @Autowired
    private MaintenanceProjectClient projectClient;

    @Override
    public Result<Map<String, Object>> getSimpleList(@RequestBody CmdbMaintenanceProjectQuery entity) {
        return projectClient.getSimpleList(entity);
    }

    @Override
    public JSONObject insertMaintenanceProject(@RequestBody CmdbMaintenanceProject maintenanceProject) {
        return projectClient.insertMaintenanceProject(maintenanceProject);
    }

    @Override
    public Result<CmdbMaintenanceProject> getMaintenanceProjectList(@RequestBody CmdbMaintenanceProjectQuery query) {
        return projectClient.getMaintenanceProjectList(query);
    }

    @Override
    public Result<Map<String,Object>> getMaintenanceProjectListNotMoney(@RequestBody CmdbMaintenanceProjectQuery query) {
        return projectClient.getMaintenanceProjectListNotMoney(query);
    }

    @Override
    public CmdbMaintenanceProject getMaintenanceProjectInfo(@RequestParam("projectId") String projectId) {
        return projectClient.getMaintenanceProjectInfo(projectId);
    }

    @Override
    public CmdbMaintenanceProject getMaintenanceProjectInfoByName(@RequestParam("projectName") String projectName) {
        return projectClient.getMaintenanceProjectInfoByName(projectName);
    }

    @Override
    public JSONObject bindProjectInstance(@RequestBody List<CmdbMaintenanceProjectInstance> projectInstanceList,
                                          @RequestParam("project_id") String projectId) {
        JSONObject result =  projectClient.bindProjectInstance(projectInstanceList, projectId);
        return result;
    }

    @Override
    public JSONObject removeBindInstance(@RequestParam("project_instance_id") String projectInstanceId,
                                         @RequestParam("project_id") String projectId) {
        JSONObject result =  projectClient.removeBindInstance(projectInstanceId, projectId);
        return result;
    }

    @Override
    public Result<CmdbMaintenanceProjectBindInstance> listBindInstance(@RequestBody CmdbMaintenanceProjectBindInstanceQuery query) {
        return projectClient.listBindInstance(query);
    }

    @Override
    public JSONObject deleteMaintenanceProject(@RequestParam("project_id") String projectId) {
        return projectClient.deleteMaintenanceProject(projectId);
    }

    @Override
    public JSONObject exportProject(@RequestBody CmdbMaintenanceProjectQuery query, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map<String, Object>> list = projectClient.exportProjectList(query);
            // 处理维保数量数据的格式
            for(Map<String, Object> item : list) {
                String serviceType = item.get("serviceTypes") == null ? null : item.get("serviceTypes").toString();
                String serviceNum = item.get("serviceNums") == null ? null : item.get("serviceNums").toString();
                if(serviceType != null && serviceNum != null) {
                    String[] types = serviceType.split(",");
                    String[] nums = serviceNum.split(",");
                    StringBuilder rsStr = new StringBuilder( types[0] + "(" + nums[0] + ")");
                    for(int i=1;i<types.length;i++) {
                        rsStr.append(";" + types[i] + "(" + nums[i] + ")");
                    }
                    item.put("service_num",rsStr.toString());
                } else {
                    item.put("service_num","");
                }
            }
            String fileName = "维保项目列表.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            String[] header = new String[] {"项目名称","合同编号","合同供应商","合同联系人","合同联系人电话","合同联系人邮箱","维保类型",
                    "服务形式","服务数量","开始时间","结束时间","服务供应商","服务联系人","服务联系人电话","服务联系人邮箱","设备区域","维保对象类型",
                    "采购类型","金额(万)","设备类型","税前金额","税率","税后金额","单价","总价","结算方式","折扣后金额","折扣率"};
            String[] keys = new String[] {"project_name","project_no","contract_produce","contract_contact","contract_contact_phone",
                    "contract_contact_email","maintenance_type","service_type","service_num","service_start_time",
                    "service_end_time","produce","produce_concat_name","produce_concat_phone","produce_concat_email",
                    "device_area","maintenance_project_type","procure_type","money","device_type","pre_tax","tax_rate","after_tax",
                    "unit_price","total_price","pay_method","discount_amount","discount_rate"};
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, "维保项目列表", header, list, keys);
            book.write(os);
            os.flush();
            os.close();
            jsonObject.put("flag", "success");
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return jsonObject;
    }

    @Override
    public CmdbMaintenanceProject getValidProjectByDeviceSn(@RequestParam("device_sn") String deviceSn) {
        return projectClient.getValidProjectByDeviceSn(deviceSn);
    }

    @Override
    public JSONObject addServiceNum(@RequestBody List<CmdbMaintenanceServiceNum> request) {
        return projectClient.addServiceNum(request);
    }

    @Override
    public List<Map<String, Object>> getMaintenObjByTimeAndSn(@RequestBody Map<String,String> mpValue) {
        return projectClient.getMaintenObjByTimeAndSn(mpValue);
    }
}
