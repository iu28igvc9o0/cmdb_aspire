package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.mainten;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.mainten.ICmdbInventoryInfoStatistAPI;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.payload.CmdbInventoryInfoStatistRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten.CmdbInventoryInfoStatistClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CmdbInventoryInfoStatistController
 * @Description TODO
 * @Author luowenbo
 * @Date 2020/2/17 14:05
 * @Version 1.0
 */
@RestController
@Slf4j
public class CmdbInventoryInfoStatistController implements ICmdbInventoryInfoStatistAPI {

    @Autowired
    private CmdbInventoryInfoStatistClient cmdbInventoryInfoStatistClient;

    @Override
    public List<Map<String, Object>> firstLayer() {
        return cmdbInventoryInfoStatistClient.firstLayer();
    }

    @Override
    public JSONObject exportFirstLayer(HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map<String, Object>> list = cmdbInventoryInfoStatistClient.firstLayer();
            // 处理维保数量数据的格式
            String fileName = "firstLayer.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            String[] header = new String[] {"资源池","维保项目关联成功设备数量","维保项目未关联成功设备数量","资产管理未关联设备数量"};
            String[] keys = new String[] {"idcType","successCount","failCount","inventory"};
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, "firstLayer", header, list, keys);
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
    public Result<Map<String, Object>> secondLayer(@RequestBody CmdbInventoryInfoStatistRequest req) {
        return cmdbInventoryInfoStatistClient.secondLayer(req);
    }

    @Override
    public JSONObject exportSecondLayer(@RequestBody CmdbInventoryInfoStatistRequest req,HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map<String, Object>> list = cmdbInventoryInfoStatistClient.secondLayer(req).getData();
            // 处理维保数量数据的格式
            String fileName = "secondLayer.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            String[] header = new String[] {"维保项目名称","维保项目关联成功设备数量","维保项目未关联成功设备数量","资产管理未关联设备数量"};
            String[] keys = new String[] {"projectName","successCount","failCount","inventory"};
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, "secondLayer", header, list, keys);
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
    public Result<Map<String, Object>> thirdLayer(@RequestBody CmdbInventoryInfoStatistRequest req) {
        return cmdbInventoryInfoStatistClient.thirdLayer(req);
    }

    @Override
    public JSONObject exportThirdLayer(@RequestBody CmdbInventoryInfoStatistRequest req,HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map<String, Object>> list = cmdbInventoryInfoStatistClient.thirdLayer(req).getData();
            // 处理维保数量数据的格式
            String fileName = "thirdLayer.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            String[] header = null;
            String[] keys = null;
            // 维保未关联数据处理
            if("maintenance".equals(req.getSearchType())) {
                header = new String[] {"设备序列号","设备名称","设备ip","资源池","一级部门","二级部门","业务系统"};
                keys = new String[] {"device_sn","device_name","ip","idcType","department1","department2","bizSystem"};
            } else {
                // 资产未关联数据处理
                header = new String[] {"管理IP","设备名称","创建时间","最后更新时间","设备类型","二级部门",
                        "维护部门","管理网是否监控","设备序列号","POD名称","设备分类","业务系统","项目名称",
                        "维护人员","制造厂商","设备状态","设备型号","资源池名称","机柜名称","一级部门","维保厂家"};
                keys = new String[] {"ip","device_name","insert_time","update_time","device_type","department2",
                        "dept_operation","is_ansible","device_sn","pod_name","device_class","bizSystem","project_name",
                        "ops","device_mfrs","device_status","device_model","idcType","idc_cabinet","department1","mainten_factory"};
            }
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, "thirdLayer", header, list, keys);
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
}
