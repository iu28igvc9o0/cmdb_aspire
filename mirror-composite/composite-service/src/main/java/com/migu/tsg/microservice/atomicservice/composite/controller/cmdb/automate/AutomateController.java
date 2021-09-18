package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.automate;

import com.aspire.mirror.composite.service.cmdb.automate.IAutomateAPI;
import com.aspire.mirror.composite.service.inspection.payload.ConfigDict;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.common.ResultVo;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.ConfigDictClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.automate.AutomateClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.instance.InstanceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author fanwenhui
 * @date 2020-09-15 11:30
 * @description
 */
@Slf4j
@RestController
public class AutomateController implements IAutomateAPI {

    @Autowired
    private AutomateClient client;
    @Autowired
    private InstanceClient instanceClient;
    @Autowired
    private ConfigDictClient configDictClient;
    // 主机配置文件根路径
    private static final String AUTOMATE_CONF_ROOT = "/tmp/hostconf/data/";
    // 文件分隔符
    public static final String AUTOMATE_CONF_SEPARATOR = File.separator;

    @Override
    public ResultVo create(String param) {
        return client.create(param);
    }

    @Override
    public ResultVo getAutomateHostDetail(@RequestBody Map<String,String> param) {
        return client.getAutomateHostDetail(param);
    }

    @Override
    public List<Map<String, Object>> getAutomateColumns() {
        return client.getAutomateColumns();
    }

    @Override
    public Map<String, List<String>> buildExportHeaderList() {
        return client.buildExportHeaderList();
    }

    public Map<String, String> exportInstance(HttpServletResponse response,
                                              @RequestBody Map<String, Object> params,
                                              @RequestParam(value = "moduleType", required = false) String moduleType) {
        Map<String, String> returnMap = new HashMap<>();
        String moduleName = "主机配置信息";
        try {
            Map<String,List<String>> headerMap = buildExportHeaderList();
            params.put("currentPage", null);
            params.put("pageSize", null);
            // 新增导出数量阈值限制 fanwenhui 20200724
            Integer integer = instanceClient.listV3Count(params, moduleType);
            boolean checkFlag = checkExportLimit(integer);
            if (!checkFlag) {
                returnMap.put("code", "error");
                returnMap.put("message", "导出数量过大，请重新选择");
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                return returnMap;
            }
            Result<Map<String, Object>> pageResult = instanceClient.getInstanceListV3(params, moduleType);
            List<Map<String, Object>> dataList = pageResult.getData();
            log.info("Export instance data size -> {}", dataList.size());
            if (headerMap != null && !headerMap.isEmpty()) {
                final List<String> header = headerMap.get("header");
                final List<String> keys = headerMap.get("keys");
                ExportExcelUtil eeu = new ExportExcelUtil();
                Workbook book = new SXSSFWorkbook(128);
                eeu.exportExcel(book, 0, moduleName, header.toArray(new String[header.size()]),
                        dataList, keys.toArray(new String[keys.size()]));
                OutputStream os = null;
                try {
                    response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(moduleName + ".xlsx", "UTF-8"))));
                    response.setHeader("Connection", "close");
                    response.setHeader("Content-Type", "application/vnd.ms-excel");
                    os = response.getOutputStream();// 取得输出流
                    book.write(os);
                    os.flush();
                    os.close();
                    returnMap.put("code", "success");
                    response.setStatus(HttpStatus.NO_CONTENT.value());
                    return returnMap;
                } catch (Exception e) {
                    log.error("导出Excel数据失败!", e);
                    returnMap.put("code", "error");
                    returnMap.put("message", e.getMessage());
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
            }
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
            returnMap.put("code", "error");
            returnMap.put("message", e.getMessage());
        }
        return returnMap;
    }

    @Override
    public List<Map<String, String>> findAutomateConfList(@RequestBody Map<String, Object> param) {
        return client.findAutomateConfList(param);
    }

    @Override
    public void automateDownload(HttpServletRequest request, HttpServletResponse response,@RequestParam String fileId) throws Exception{
        // 通过附件ID获取文件路径 \data\hostconf\data\20201113\10.3.16.25\ld.so.conf.d.tar
//        String filePath = AUTOMATE_CONF_ROOT + AUTOMATE_CONF_SEPARATOR + "20201113" + AUTOMATE_CONF_SEPARATOR + "10.2.0.3" + AUTOMATE_CONF_SEPARATOR + "ifcfg-eth5";
        String filePath = "";
        Map<String, Object> param = new HashMap<>();
        param.put("fileId",fileId);
        List<Map<String, String>> automateConfList = client.findAutomateConfList(param);
        if (null != automateConfList && !automateConfList.isEmpty()) {
            Map<String, String> automateConf = automateConfList.get(0);
            filePath = AUTOMATE_CONF_ROOT + AUTOMATE_CONF_SEPARATOR + automateConf.get("filePath") + AUTOMATE_CONF_SEPARATOR + automateConf.get("fileIp") + AUTOMATE_CONF_SEPARATOR + automateConf.get("fileName");
        }
        // 清空response
        response.reset();
        try (InputStream fis = new BufferedInputStream(new FileInputStream(filePath));
             OutputStream toClient = new BufferedOutputStream(response.getOutputStream())){
            File file = new File(filePath);
            if (!file.exists()) {
                log.info("文件查找不到:{}",fileId);
                return;
            }
            String fileName = file.getName();
            // 以流的形式下载文件。
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);

            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            response.setContentType("application/octet-stream");
            response.setHeader("Access-Control-Allow-Origin", "*");
            toClient.write(buffer);
            toClient.flush();
        }
    }

    /**
     * 校验导出数量是否超过限制
     * @param total 查询到的导出数量
     * @return true - 允许导出
     */
    private boolean checkExportLimit(int total) {
        boolean checkFlag = true;
        List<ConfigDict> exportCountLimit = configDictClient.getDictsByType("exportCountLimit", null, null, null);
        if (!exportCountLimit.isEmpty()) {
            ConfigDict configDict = exportCountLimit.get(0);
            String value = configDict.getValue();
            int exportLimit = 5000;
            if (org.apache.commons.lang.StringUtils.isNotEmpty(value)) {
                exportLimit = Integer.parseInt(value);
            }
            if (total > exportLimit) {
                checkFlag = false;
            }
        }
        return checkFlag;
    }
}
