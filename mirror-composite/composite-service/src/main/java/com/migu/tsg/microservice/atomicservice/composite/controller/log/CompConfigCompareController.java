package com.migu.tsg.microservice.atomicservice.composite.controller.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.log.api.dto.ConfigCompareLogsResp;
import com.aspire.mirror.log.api.dto.ConfigCompareReq;
import com.aspire.mirror.log.api.dto.ConfigCompareResp;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.log.IConfigCompareServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.common.StringUtils;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LastLogCodeEnum;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.composite.exception.BaseException;
import com.migu.tsg.microservice.atomicservice.composite.exception.ResultErrorEnum;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.ICompConfigCompareService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resources;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author baiwenping
 * @Title: CompConfigCompareController
 * @Package com.migu.tsg.microservice.atomicservice.composite.controller.log
 * @Description: TODO
 * @date 2020/12/24 11:10
 */
@Slf4j
@RestController
public class CompConfigCompareController implements ICompConfigCompareService {
    @Autowired
    private IConfigCompareServiceClient configCompareServiceClient;

    private static final String COMPARE_TEMPLATE_PATH = "/download/compare_template.xlsx";
    /**
     * 获取比对列表
     *
     * @param masterIp
     * @param backupIp
     * @param compareTimeFrom
     * @param compareTimeTo
     * @param compareType
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResponse<ConfigCompareResp> getCompareList(@RequestParam(name = "masterIp", required = false) String masterIp,
                                                          @RequestParam(name = "backupIp", required = false) String backupIp,
                                                          @RequestParam(name = "compareTimeFrom", required = false) String compareTimeFrom,
                                                          @RequestParam(name = "compareTimeTo", required = false) String compareTimeTo,
                                                          @RequestParam(name = "compareType", required = false) String compareType,
                                                          @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                                          @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize) {
        return configCompareServiceClient.getCompareList(masterIp, backupIp, compareTimeFrom, compareTimeTo, compareType, pageNum, pageSize);
    }

    /**
     * 导出比对清单
     *
     * @param ids
     * @return
     */
    @Override
    public void exportCompareList(@RequestBody List<Integer> ids) {
        List<ConfigCompareResp> list = configCompareServiceClient.exportCompareList(ids);
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        String[] headerList = {"资源池","主设备IP","备设备IP","厂商","新增未同步","修改未同步","删除未同步","最新比对时间"};
        String[] keyList = {"idcType","masterIp","backupIp","brand","addCount","modifyCount","delCount","compareTime"};
        String title = "主备配置清单";
        String fileName = title+".xlsx";

        try {
            List<Map<String, Object>> dataLists = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (ConfigCompareResp configCompareResp : list) {
                Map<String, Object>  map= ExportExcelUtil.objectToMap(configCompareResp);
                if (configCompareResp.getCompareTime() != null) {
                    map.put("compareTime",  sdf.format(configCompareResp.getCompareTime()));
                }
                dataLists.add(map);
            }
            OutputStream os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            //excel constuct
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, title, headerList, dataLists, keyList);
            book.write(os);
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
        }
    }

    /**
     *
     */
    public void downloadTemplate() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        String fileName = "compare_template.xlsx";
        InputStream input = null;
        OutputStream out = null;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            //excel constuct
//            input = this.getClass().getResourceAsStream(COMPARE_TEMPLATE_PATH);
            input = new ClassPathResource(COMPARE_TEMPLATE_PATH).getInputStream();
//            input = new FileInputStream(filePath1);
            out = response.getOutputStream();
            byte[] b = new byte[2048];
            int len;
            while ((len = input.read(b)) != -1) {
                out.write(b, 0, len);
            }
            //修正 Excel在“xxx.xlsx”中发现不可读取的内容。是否恢复此工作薄的内容？如果信任此工作簿的来源，请点击"是"
//            response.setHeader("Content-Length", String.valueOf(input.getChannel().size()));
            input.close();
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 导出比对明细
     *
     * @param ids
     * @return
     */
    @Override
    public void exportCompareDetailList(@RequestBody List<Integer> ids) {
        List<ConfigCompareResp> list = configCompareServiceClient.exportCompareList(ids);
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        String[] headerList = {"资源池","主设备IP","备设备IP","厂商","比对类型","主设备名称","主设备内容","备设备名称","备设备内容","异常点","最新比对时间"};
        String[] keyList = {"idcType","masterIp","backupIp","brand","compareType","master_name","master_content","backup_name","backup_content","compare_result","compareTime"};
        String title = "比对明细列表";
        String fileName = title+".xlsx";

        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (ConfigCompareResp configCompareResp : list) {
                Map<String, Object>  compareMap= ExportExcelUtil.objectToMap(configCompareResp);
                if (configCompareResp.getCompareTime() != null) {
                    compareMap.put("compareTime",  sdf.format(configCompareResp.getCompareTime()));
                }

                // 新增未同步
                String addDatas = configCompareResp.getAddDatas();
                Integer addCount = configCompareResp.getAddCount();
                if (!StringUtils.isEmpty(addDatas) && addCount != null && addCount > 0) {
                    try {
                        JSONArray array = JSON.parseArray(addDatas);
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            Map<String, Object>  map = Maps.newHashMap();
                            map.putAll(jsonObject);
                            String backupContent = jsonObject.getString("backup_content");
                            if (!StringUtils.isEmpty(backupContent) && backupContent.indexOf(",") > -1) {
                                jsonObject.put("backup_content",backupContent.replaceAll(",","\n"));
                            }
                            String masterContent = jsonObject.getString("master_content");
                            if (!StringUtils.isEmpty(masterContent) && masterContent.indexOf(",") > -1) {
                                jsonObject.put("master_content",masterContent.replaceAll(",","\n"));
                            }
                            map.putAll(compareMap);
                            map.put("compareType", "新增未同步");
                            dataLists.add(map);
                        }
                    } catch (Exception e) {

                    }
                }

                // 修改未同步
                String modifyDatas = configCompareResp.getModifyDatas();
                Integer modifyCount = configCompareResp.getModifyCount();
                if (!StringUtils.isEmpty(modifyDatas) && modifyCount != null && modifyCount > 0) {
                    try {
                        JSONArray array = JSON.parseArray(modifyDatas);
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            String backupContent = jsonObject.getString("backup_content");
                            if (!StringUtils.isEmpty(backupContent) && backupContent.indexOf(",") > -1) {
                                jsonObject.put("backup_content",backupContent.replaceAll(",","\n"));
                            }
                            String masterContent = jsonObject.getString("master_content");
                            if (!StringUtils.isEmpty(masterContent) && masterContent.indexOf(",") > -1) {
                                jsonObject.put("master_content",masterContent.replaceAll(",","\n"));
                            }
                            Map<String, Object>  map = Maps.newHashMap();
                            map.putAll(jsonObject);
                            map.putAll(compareMap);
                            map.put("compareType", "修改未同步");
                            dataLists.add(map);
                        }
                    } catch (Exception e) {

                    }
                }

                // 删除未同步
                String delDatas = configCompareResp.getDelDatas();
                Integer delCount = configCompareResp.getDelCount();
                if (!StringUtils.isEmpty(delDatas) && delCount != null && delCount > 0) {
                    try {
                        JSONArray array = JSON.parseArray(delDatas);
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            String backupContent = jsonObject.getString("backup_content");
                            if (!StringUtils.isEmpty(backupContent) && backupContent.indexOf(",") > -1) {
                                jsonObject.put("backup_content",backupContent.replaceAll(",","\n"));
                            }
                            String masterContent = jsonObject.getString("master_content");
                            if (!StringUtils.isEmpty(masterContent) && masterContent.indexOf(",") > -1) {
                                jsonObject.put("master_content",masterContent.replaceAll(",","\n"));
                            }
                            Map<String, Object>  map = Maps.newHashMap();
                            map.putAll(jsonObject);
                            map.putAll(compareMap);
                            map.put("compareType", "删除未同步");
                            dataLists.add(map);
                        }
                    } catch (Exception e) {

                    }
                }

            }
            OutputStream os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            //excel constuct
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, title, headerList, dataLists, keyList);
            book.write(os);
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
        }
    }

    /**
     * 比对
     *
     * @param id
     * @param masterIndex
     * @param backupIndex
     * @return
     */
    @Override
    public Map<String, Object> compare(@PathVariable("id") Integer id,
                                     @RequestParam(name = "masterIndex") String masterIndex,
                                     @RequestParam(name = "backupIndex") String backupIndex) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        return configCompareServiceClient.compare(id, authCtx.getUser().getUsername(), masterIndex, backupIndex);
    }

    /**
     * 新增
     *
     * @param configCompare
     * @return
     */
    @Override
    public void insert(@RequestBody ConfigCompareReq configCompare) {
        if (configCompare == null || StringUtils.isEmpty(configCompare.getMasterIp()) || StringUtils.isEmpty(configCompare.getBackupIp())) {
            throw new BaseException(LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BAD_REQUEST);
        }
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        configCompare.setCreateUser(authCtx.getUser().getUsername());
        configCompareServiceClient.insert(configCompare);
    }

    /**
     * 导入比对列表
     *
     * @param file
     * @return
     */
    @Override
    public Map<String, Object> importFile(@RequestPart("file") MultipartFile file) {
        Map<String, Object> returnMap = new HashMap<>();
        if (null == file) {
            returnMap.put("flag", "false");
            returnMap.put("message", "文件不能为空！");
            return returnMap;
        }
        InputStream is = null;
        Workbook wb;
        try {
            is = file.getInputStream();
            wb = WorkbookFactory.create(is);
            Sheet sheet = wb.getSheetAt(0);
            int totalRows = sheet.getPhysicalNumberOfRows();
            if (totalRows <= 1) {
                returnMap.put("flag", "false");
                returnMap.put("message", "Excel格式不正确, 请先下载Excel模板后, 填写数据再上传!");
                return returnMap;
            }
            List<ConfigCompareReq> list = Lists.newArrayList();
            RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
            for (int r = 1 ; r < totalRows; r++) {
                Row row = sheet.getRow(r);
                // 资源池
                Cell cell0 = row.getCell(0);
                // 主设备IP
                Cell cell1 = row.getCell(1);
                // 备设备IP
                Cell cell2 = row.getCell(2);
                // 厂商
                Cell cell3 = row.getCell(3);
                if (cell1 == null || cell2 == null
                        || StringUtils.isEmpty(cell1.getStringCellValue())
                        || StringUtils.isEmpty(cell2.getStringCellValue())) {
                    continue;
                }
                ConfigCompareReq compareReq = new ConfigCompareReq();
                if (cell0 != null) {
                    compareReq.setIdcType(cell0.getStringCellValue());
                }
                compareReq.setMasterIp(cell1.getStringCellValue());
                compareReq.setBackupIp(cell2.getStringCellValue());
                if (cell3 != null) {
                    compareReq.setBrand(cell3.getStringCellValue());
                }
                compareReq.setCreateUser(authCtx.getUser().getUsername());
                list.add(compareReq);
            }
            if (list.size() > 0) {
                configCompareServiceClient.importFile(list);
            } else {
                returnMap.put("flag", "false");
                returnMap.put("message", "合法数据为空！");
            }
        } catch (Exception e) {
            returnMap.put("flag", "false");
            returnMap.put("message", "解析Excel文件失败:" + e.getMessage());
            returnMap.put("error", e.getMessage());
            return returnMap;
        } finally {
            IOUtils.closeQuietly(is);
        }

        return returnMap;
    }

    /**
     * 获取比对记录
     *
     * @param compareId
     * @return
     */
    @Override
    public List<ConfigCompareLogsResp> getLogs(@PathVariable("compare_id") Integer compareId) {
        return configCompareServiceClient.getLogs(compareId);
    }

    /**
     * 导出比对记录
     *
     * @param compareId
     * @return
     */
    @Override
    public void exportLogs(@PathVariable("compare_id") Integer compareId) {
        List<ConfigCompareLogsResp> list = configCompareServiceClient.getLogs(compareId);
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        String[] headerList = {"主配置原始文件","被配置原始文件","新增未同步","修改未同步","删除未同步","最新比对时间"};
        String[] keyList = {"masterConfigFile","backupConfigFile","addResult","modifyResult","delResult","compareTime"};
        String title = "比对记录";
        String fileName = title+".xlsx";

        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (ConfigCompareLogsResp configCompareLogsResp : list) {
                String addResult = configCompareLogsResp.getAddResult();
                if (!StringUtils.isNotEmpty(addResult) && addResult.indexOf(",") > -1) {
                    configCompareLogsResp.setAddResult(addResult.replaceAll(",", "\n"));
                }
                String delResult = configCompareLogsResp.getDelResult();
                if (!StringUtils.isNotEmpty(delResult) && delResult.indexOf(",") > -1) {
                    configCompareLogsResp.setDelResult(delResult.replaceAll(",", "\n"));
                }
                String masterConfigFile = configCompareLogsResp.getMasterConfigFile();
                if (!StringUtils.isEmpty(masterConfigFile)) {
                    configCompareLogsResp.setMasterConfigFile(masterConfigFile.substring(masterConfigFile.lastIndexOf("/")));
                }
                String backupConfigFile = configCompareLogsResp.getBackupConfigFile();
                if (!StringUtils.isEmpty(backupConfigFile)) {
                    configCompareLogsResp.setBackupConfigFile(backupConfigFile.substring(backupConfigFile.lastIndexOf("/")));
                }
                Map<String, Object>  map= ExportExcelUtil.objectToMap(configCompareLogsResp);
                map.put("compareTime",  sdf.format(configCompareLogsResp.getCompareTime()));
                dataLists.add(map);
            }
            OutputStream os = response.getOutputStream();// 取得输出流
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            //excel constuct
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, title, headerList, dataLists, keyList);
            book.write(os);
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
        }
    }

    /**
     *
     * @param compareId
     * @return
     */
    public Map<String, Object> getIndex(@PathVariable("compare_id") Integer compareId) {
        return configCompareServiceClient.getIndex(compareId);
    }
}
