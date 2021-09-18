package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

import com.aspire.mirror.common.constant.SystemConstant;
import com.aspire.mirror.composite.service.cmdb.IConfigDictService;
import com.aspire.mirror.composite.service.inspection.payload.ConfigDict;
import com.aspire.mirror.composite.service.inspection.payload.Dict;
import com.aspire.mirror.composite.service.inspection.payload.Result;
import com.migu.tsg.microservice.atomicservice.composite.biz.ConfigDictService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.ConfigDictClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.migu.tsg.microservice.atomicservice.composite.controller.cmdb
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/4/15 14:19
 * 版本: v1.0
 */
@RestController
@Slf4j
public class ConfigDictController implements IConfigDictService {
    
    @Autowired
    private ConfigDictClient configDictClient;
    @Autowired
    private ConfigDictService dictServiceClient;

    @Value("${systemType:simple}")
    private String systemType;
    
    @Override
    public Result<Dict> getAllConfigDictData(@RequestParam(value = "pageNum",required = false) int pageNum,
                                             @RequestParam(value = "startPageNum",required = false) int startPageNum,
                                             @RequestParam(value = "pageSize",required = false) int pageSize,
                                             @RequestParam(value = "pcode",required = false) String pcode,
                                             @RequestParam(value = "dictCode",required = false) String dictCode,
                                             @RequestParam(value = "dictNote",required = false) String dictNote,
                                             @RequestParam(value = "colName",required = false) String colName) {
        return configDictClient.getAllConfigDictData(pageNum,startPageNum,pageSize,pcode,dictCode,dictNote,colName);
    }
    
    @Override
    public String insertCfgDict(@RequestBody Dict dict) {
        return configDictClient.insertCfgDict(dict);
    }
    
    @Override
    public String deleteCfgDictById(@RequestParam String dictId) {
        return configDictClient.deleteCfgDictById(dictId);
    }
    
    @Override
    public String updateCfgDict(@RequestBody Dict dict) {
        return configDictClient.updateCfgDict(dict);
    }
    
    @Override
    public Dict getById(@RequestParam String dictId) {
        return configDictClient.getById(dictId);
    }

    @Override
    public List<Dict> getByIds(@RequestParam("ids") String dictIds) {
        return configDictClient.getByIds(dictIds);
    }


    @Override
    public List<Dict> getAll(@RequestParam(value = "dictId",required = false)String dictId) {
        return configDictClient.getAll(dictId);
    }
    
    @Override
    public void exportDict(@RequestParam(value = "pcode",required = false) String pcode,
                           @RequestParam(value = "dictCode",required = false) String dictCode,
                           @RequestParam(value = "dictNote",required = false) String dictNote,
                           @RequestParam(value = "colName",required = false) String colName) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        try {
            List<Map<String, Object>> configDictAll = configDictClient.exportDict(pcode,dictCode,dictNote,colName);
            log.info("[cdmb ConfigDict Data] >>> {}", configDictAll );
            String[] headerList = {"父标签","标签名","数据值","类型","描述"};
            String[] keyList = {"pname","dictCode","dictNote","colName","description"};
            String title = "字典表数据";
            String fileName = title+".xlsx";
            OutputStream os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf( URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            //excel constuct
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, title, headerList, configDictAll, keyList);
            book.write(os);
        } catch (Exception e) {
            log.error("[export ConfigDictData is error] >>> " + e);
        }
    }
    
    @Override
    public List<ConfigDict> getDicts() {
        return configDictClient.getDicts();
    }
    
    @Override
    public List<ConfigDict> getDictsByType(String type,String pid,String pValue,String pType) {
        if (SystemConstant.BIZ_SYSTEM_BDC.equals(this.systemType)) {
            return dictServiceClient.selectDictsByType(type, pid, pValue, pType);
        }
        List<ConfigDict> dictsByType = configDictClient.getDictsByType( type, pid, pValue, pType );
        log.info("count >>> " + dictsByType.size());
        return dictsByType;
    }

    @Override
    public List<ConfigDict> getTree(@PathVariable("col_name") String colName) {
        return configDictClient.getTree(colName);
    }
	
	@Override
    public List<Map> getDistinctDictType() {
        return configDictClient.getDistinctDictType();
    }
}
