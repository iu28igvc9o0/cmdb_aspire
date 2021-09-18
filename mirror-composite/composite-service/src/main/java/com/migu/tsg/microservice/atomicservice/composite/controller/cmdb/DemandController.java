package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.IDemandService;
import com.aspire.mirror.composite.service.inspection.payload.InsertDemandEntity;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.DemandClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ResourceAuthHelper;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 项目名称:
 * 包: com.migu.tsg.microservice.atomicservice.composite.controller.cmdb
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/5/13 14:35
 * 版本: v1.0
 */
@RestController
@Slf4j
public class DemandController extends CommonResourceController implements IDemandService {

    @Autowired
    private DemandClient demandClient;

    @Autowired
    protected ResourceAuthHelper resAuthHelper;

    @Override
    @ResAction(action = "view", resType = "cmdb-resource")
    public JSONObject list(@RequestBody Map<String, Object> queryMap) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        // TODO
//        GeneralAuthConstraintsAware authParam = new GeneralAuthConstraintsAware();
//        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
//            Map<String, Set<String>> totalConstraints = resAuthHelper.verifyActionAndGetResource(
//                    authCtx.getUser(), authCtx.getResAction(), authCtx.getFlattenConstraints());
//            if (!super.applyGeneralAuthConstraints(totalConstraints, authParam)) {
//                return new JSONObject();
//            }
//        }
//        queryMap.put("authBizSystemIdList", authParam.getAuthBizSystemIdList());
        JSONObject obj = demandClient.list(queryMap);
        return obj;
    }

    @Override
    @ResAction(action = "view", resType = "cmdb-resource")
    public JSONObject get(@RequestParam("demandId") String demandId) {
        return demandClient.get(demandId);
    }

    @Override
    @ResAction(action = "view", resType = "cmdb-resource")
    public List<Map<String, Object>> getTableHeader() {
        return demandClient.getTableHeader();
    }

    @Override
    @ResAction(action = "view", resType = "cmdb-resource")
    public List<Map<String, Object>> getDemandTypeList() {
        return demandClient.getDemandTypeList();
    }

    @Override
    @ResAction(action = "create", resType = "cmdb-resource")
    public String save(@RequestBody InsertDemandEntity insertDemandEntity) {
        return demandClient.save(insertDemandEntity);
    }

    @Override
    @ResAction(action = "upate", resType = "cmdb-resource")
    public String update(@RequestBody InsertDemandEntity insertDemandEntity) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        RequestAuthContext.RequestHeadUser user = authCtx.getUser();
        if (null != user) insertDemandEntity.setUserName(user.getUsername());
        return demandClient.update(insertDemandEntity);
    }

    @Override
    @ResAction(action = "view", resType = "cmdb-resource")
    public Map<String, String> exportDemand(HttpServletResponse response, @RequestBody Map<String, Object>
            sendRequest) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            Map<String, String[]> header = getDemandHeader();
            Map<String, Object> reqParam = new HashMap<String, Object>();
            if (sendRequest.get("department") != null) {
                reqParam.put("department", sendRequest.get("department").toString());
            }
            if (sendRequest.get("bizSystem") != null) {
                reqParam.put("bizSystem", sendRequest.get("bizSystem").toString());
            }
            JSONObject returnJson = demandClient.exportDemand(reqParam);
            List<Map<String, Object>> dataList = new LinkedList<>();
            if (returnJson.containsKey("dataList") && returnJson.get("dataList") != null) {
                dataList = (List<Map<String, Object>>) returnJson.get("dataList");
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            String fileName = dateFormat.format(new Date()) + "配置异常列表.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            if (sendRequest != null && sendRequest.size() > 0) {
                OutputStream os = response.getOutputStream();// 取得输出流
                ExportExcelUtil eeu = new ExportExcelUtil();
                Workbook book = new SXSSFWorkbook(128);
                eeu.exportExcel(book, 0, "配置异常列表", header.get("header"), dataList, header.get("key"));
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

    private Map<String, String[]> getDemandHeader() {
        List<Map<String, Object>> headerList = demandClient.getTableHeader();
        List<String> header = new LinkedList<>();
        List<String> key = new LinkedList<>();
        if (headerList != null && headerList.size() > 0) {
            for (Map<String, Object> headerMap : headerList) {
                if (headerMap.get("data") != null) {
                    List<Map<String, String>> resourceList = (List<Map<String, String>>) headerMap.get("data");
                    if (resourceList != null) {
                        for (Map<String, String> source : resourceList) {
                            if (source.get("label").toLowerCase(Locale.ENGLISH).equals("id")) {
                                continue;
                            }
                            String title = source.get("label");
                            switch (source.get("label").toLowerCase(Locale.ENGLISH)) {
                                case "部门":
                                case "需求接口人":
                                case "需求接口人电话":
                                case "应用系统":
                                case "资源需求提出时间":
                                    title = title + "[必填]";
                                    break;
                            }
                            header.add(title);
                            key.add(source.get("key"));
                        }
                    }
                }
            }
        }
        Map<String, String[]> returnMap = new HashMap<>();
        returnMap.put("header", header.toArray(new String[header.size()]));
        returnMap.put("key", key.toArray(new String[key.size()]));
        return returnMap;
    }

    @Override
    public Map<String, String> importDemandTemplate(HttpServletResponse response) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            Map<String, String[]> header = getDemandHeader();
            List<Map<String, Object>> dataList = new LinkedList<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            String fileName = dateFormat.format(new Date()) + "配置异常列表.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, "配置异常列表", header.get("header"), dataList, header.get("key"));
            book.write(os);
            os.flush();
            os.close();
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
