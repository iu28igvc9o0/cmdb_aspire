package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.instance;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.instance.ICmdbInstanceAgentCheckAPI;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbInstanceAgentCheck;
import com.aspire.ums.cmdb.instance.payload.CmdbInstanceAgentCheckQuery;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.instance.CmdbInstanceAgentCheckClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CmdbInstanceAgentCheckController
 * @Description
 * @Author luowenbo
 * @Date 2020/5/26 17:50
 * @Version 1.0
 */
@RestController
public class CmdbInstanceAgentCheckController implements ICmdbInstanceAgentCheckAPI {

    @Autowired
    private CmdbInstanceAgentCheckClient agentCheckClient;

    @Override
    public Result<CmdbInstanceAgentCheck> list(@RequestBody CmdbInstanceAgentCheckQuery query) {
        return agentCheckClient.list(query);
    }

    @Override
    public JSONObject batchDelete(@RequestBody CmdbInstanceAgentCheckQuery query) {
        return agentCheckClient.batchDelete(query);
    }

    @Override
    public JSONObject export(@RequestBody CmdbInstanceAgentCheckQuery query, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<CmdbInstanceAgentCheck> tmpList = agentCheckClient.list(query).getData();
            List<Map<String, Object>> list = new ArrayList<>();
            for(CmdbInstanceAgentCheck item : tmpList) {
                String scanTime = item.getScanTime();
                String cmdbUpdateTime = item.getCmdbUpdateTime();
                item.setScanTime(scanTime.substring(0,scanTime.lastIndexOf(".")));
                item.setCmdbUpdateTime(cmdbUpdateTime.substring(0,cmdbUpdateTime.lastIndexOf(".")));
                Map<String, Object> mp = JSON.parseObject(JSON.toJSONString(item),Map.class);
                list.add(mp);
            }
            String fileName = "已安装Agent设备检查.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            String[] header = new String[] {"IP","资源池","设备名称","设备类型","同步状态","CMDB更新时间","扫描检查时间"};
            String[] keys = new String[] {"ip","idcType","deviceName","deviceType","syncStatus","cmdbUpdateTime","scanTime"};
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, "安装Agent的设备检查", header, list, keys);
            book.write(os);
            os.flush();
            os.close();
            jsonObject.put("flag", "success");
        } catch (Exception e) {
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return jsonObject;
    }
}
