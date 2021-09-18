package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.instance;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.instance.ICmdbAssignAPI;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbAssign;
import com.aspire.ums.cmdb.instance.payload.CmdbAssignQuery;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.instance.CmdbAssignClient;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbAssignController
 * Author:   hangfang
 * Date:     2019/11/14
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbAssignController implements ICmdbAssignAPI {

    @Autowired
    private CmdbAssignClient assignClient;

    @Override
    public Result<CmdbAssign> list(@RequestBody CmdbAssignQuery query) {
        return assignClient.list(query);
    }

    @Override
    public JSONObject export(@RequestBody CmdbAssignQuery query, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            Result<CmdbAssign> result = assignClient.list(query);
            List<CmdbAssign> assignList = result.getData();
            for (CmdbAssign assign : assignList) {
                Map<String, Object>  map = ExportExcelUtil.objectToMap(assign);
                list.add(map);
            }
            String fileName = "资源分配分析报表列表.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            String[] header = new String[] {"资源池名称","类型","一级租户","二级租户","业务系统","租户资源需求","已建设量",
                    "已分配量","预分配量","未建设量"};
            String[] keys = new String[] {"resourcePool","type","department1","department2","bizSystem",
                    "departNeedCount","builtCount","assignedCount","preAssignCount",
                    "unBuiltCount"};
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, "资源分配分析报表列表", header, list, keys);
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
    public Map<String, Object> delete(@RequestParam("id") String id) {
        return assignClient.delete(id);
    }
}
