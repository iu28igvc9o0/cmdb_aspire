package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.mainten;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.mainten.ICmdbComponentInfoAPI;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.payload.CmdbComponentInfo;
import com.aspire.ums.cmdb.maintenance.payload.CmdbComponentInfoQueryRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten.MaintenComponentInfoClient;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FileName: MaintenComponentInfoController
 * Author:   luowenbo
 * Date:     2020.2.7 15:58
 * Description: 维保部件信息控制层类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class MaintenComponentInfoController implements ICmdbComponentInfoAPI {

    @Autowired
    private MaintenComponentInfoClient maintenComponentInfoClient;

    @Override
    public JSONObject save(@RequestBody CmdbComponentInfo obj) {
        return maintenComponentInfoClient.save(obj);
    }

    @Override
    public JSONObject delete(@RequestBody CmdbComponentInfo obj) {
        return maintenComponentInfoClient.delete(obj);
    }

    @Override
    public JSONObject update(@RequestBody CmdbComponentInfo obj) {
        return maintenComponentInfoClient.update(obj);
    }

    @Override
    public Result<CmdbComponentInfo> list(@RequestBody CmdbComponentInfoQueryRequest request) {
        return maintenComponentInfoClient.list(request);
    }

    @Override
    public JSONObject exportProject(@RequestBody CmdbComponentInfoQueryRequest request,HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<CmdbComponentInfo> tmpList = maintenComponentInfoClient.list(request).getData();
            List<Map<String, Object>> list = new ArrayList<>();
            for(CmdbComponentInfo item : tmpList) {
                Map<String, Object> mp = JSON.parseObject(JSON.toJSONString(item),Map.class);
                list.add(mp);
            }
            String fileName = "维保部件清单.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            String[] header = new String[] {"规格型号","配置描述","部件序列号","部件名称","单价","数量","不含税单价",
                    "不含税价格","增值税率或征收率","增值税税额","含税价格"};
            String[] keys = new String[] {"specificModel","configDesc","serialNumber","componentName","unit","quantity","unitNotTax",
                    "moneyNotTax","addTax","addAmount","moneyWithTax"};
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, "维保部件清单", header, list, keys);
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
