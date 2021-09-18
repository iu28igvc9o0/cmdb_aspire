package com.aspire.ums.cmdb.v2.process.export.thread;

import com.aspire.ums.cmdb.cmic.util.EventThreadUtils;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.common.throwable.CmdbRuntimeException;
import com.aspire.ums.cmdb.config.RequestAuthContext;
import com.aspire.ums.cmdb.deviceStatistic.util.ExportExcelUtil;
import com.aspire.ums.cmdb.deviceStatistic.util.POIModuleUtils;
import com.aspire.ums.cmdb.ftp.service.FtpService;
import com.aspire.ums.cmdb.instance.payload.ImportProcess;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 说明:
 * 工程: BPM
 * 作者: zhujuwang
 * 时间: 2020/11/30 10:23
 */
@Slf4j
@Data
@NoArgsConstructor
public class ExportProcessThread implements Runnable {
    private Map<String, Object> params;
    private String processId;
    private List<String> headers;
    private List<String> keys;
    private Map<String, List<Map<String, Object>>> comboDataMap;
    private ICmdbInstanceService instanceService;
    private IRedisService redisService;
    private POIModuleUtils poiModuleUtils;
    private FtpService ftpService;
    private Integer totalCount;
    // 用来存放需要下载的excel数据源
    private Map<Integer, List<Map<String, Object>>> workbookMap = Maps.newHashMap();
    // 资源权限
    private RequestAuthContext requestAuthContext;

    @Override
    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final int pageSize = 1000;
                    // 分隔的文件数量 每5000条数据存放到一个excel中
                    final int totalFileSize = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize + 1);
                    final int step = Math.round(totalCount / totalFileSize / 2);
                    long startDate = new Date().getTime();
                    CountDownLatch countDownLatch = new CountDownLatch(totalFileSize);
                    for (int i = 1; i <= totalFileSize; i ++ ) {
                        final int pageNum = i;
                        EventThreadUtils.NORMAL_POOL.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    log.info("正在导出数据[{}, {}]...", pageNum, pageSize);
                                    long s = new Date().getTime();
                                    if (pageNum == 0) {
                                        log.info("is error");
                                    }
                                    // 设置请求上下文信息
                                    RequestAuthContext.setRequestAuthContext(requestAuthContext);
                                    // 获取当前记录数的起始位置
                                    Map<String, Object> queryParams = Maps.newHashMap();
                                    queryParams.putAll(params);
                                    queryParams.put("currentPage", pageNum);
                                    queryParams.put("pageSize", pageSize);
                                    List<Map<String, Object>> dataList = instanceService.getInstanceListData(queryParams, null);
                                    workbookMap.put(pageNum, dataList);
                                    syncProcessInfo(processId, step, null);
                                    log.info("查询耗时{}, {}, {}", pageNum, pageSize, (new Date().getTime() - s));
                                } catch (Exception ex) {
                                    log.info("导出错误{}", pageNum, ex);
                                } finally {
                                    countDownLatch.countDown();
                                }
                            }
                        });
                    }
                    countDownLatch.await();
                    log.info("开始合并....");
                    List<Map<String, Object>> dataList = workbookMap.get(1);

                    ExportExcelUtil eeu = new ExportExcelUtil();
                    SXSSFWorkbook workbook = new SXSSFWorkbook(pageSize);
                    eeu.exportExcel(workbook, 0, "资源列表", headers.toArray(new String[headers.size()]),
                            dataList, keys.toArray(new String[keys.size()]));
                    // 第一个workbook作为基础
                    SXSSFSheet targetSheet = workbook.getSheetAt(0);
                    CellStyle cellStyle = getCellStyle(workbook);
                    for (int key : workbookMap.keySet()) {
                        if (key == 1) {
                            continue;
                        }
                        dataList = workbookMap.get(key);
                        log.info("正在合并第{}个excel..., 共{}条.", key, dataList.size());
                        copySheet(cellStyle, dataList, targetSheet, keys.toArray(new String[keys.size()]));
                        syncProcessInfo(processId, step, null);
                    }
                    Map<String, String>  returnMap = toHandleFTPExport("配置列表", workbook);
                    syncProcessInfo(processId, step, returnMap.get("path"));
                    log.info("导入耗时:" + (new Date().getTime() - startDate));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Sheet复制
     *
     * @param dataList
     * @param targetSheet
     */
    private static void copySheet(CellStyle cellStyle, List<Map<String, Object>> dataList, SXSSFSheet targetSheet, String[] listKey) {
        int index = targetSheet.getLastRowNum() + 1;
        for (Map<String, Object> map : dataList) {
            SXSSFRow row = targetSheet.createRow( index);
            int cellIndex = 0;
            for (String key : listKey) {
                Cell cell = row.createCell(cellIndex);
                cell.setCellStyle(cellStyle);
                if (null != map && null != map.get(key)) {
                    String va = map.get(key).toString();
                    if (!StringUtils.isEmpty(va) && va.matches("^[-+]?(([0-9]+)([.]([0-9]+))?)$")) {
                        //是数字当作double处理
                        if (va.length() > 11) {
                            cell.setCellValue(va);
                        } else {
                            cell.setCellValue(Double.parseDouble(va));
                        }
                    } else {
                        cell.setCellValue(map.get(key).toString());
                    }
                } else {
                    cell.setCellValue("");
                }
                cellIndex++;
            }
            index++;
        }
    }

    private CellStyle getCellStyle(SXSSFWorkbook workbook) {
        CellStyle style1 = workbook.createCellStyle();
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单无格的边框为粗体
        style1.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style1.setLeftBorderColor(HSSFColor.BLACK.index);
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style1.setRightBorderColor(HSSFColor.BLACK.index);
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style1.setTopBorderColor(HSSFColor.BLACK.index);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
        style1.setWrapText(false);// 指定单元格自动换行
        return style1;
    }

    private Map<String, String> toHandleFTPExport(String fileName, SXSSFWorkbook book) {
        ByteArrayOutputStream ops = null;
        ByteArrayInputStream in = null;
        try {
            ops = new ByteArrayOutputStream();
            book.write(ops);
            byte[] b = ops.toByteArray();
            in = new ByteArrayInputStream(b);
            Map<String, String> returnMap = ftpService.uploadtoFTP(processId, fileName, in);
            ops.flush();
            if (returnMap.get("code").equals("error")) {
                throw new CmdbRuntimeException(returnMap.get("message"));
            }
            return returnMap;
        } catch (Exception e) {
            throw new CmdbRuntimeException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(book);
            IOUtils.closeQuietly(ops);
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 同步更新进程信息
     * @param processId
     */
    public synchronized void syncProcessInfo(String processId, Integer successNum, String filePath) {
        Object object = redisService.get(String.format(Constants.REDIS_PROCESS_DETAIL, processId));
        ImportProcess importProcess;
        if (object == null) {
            throw new CmdbRuntimeException("导出进程已被移除, processId=" + processId);
        }
        importProcess = new ObjectMapper().convertValue(object, new TypeReference<ImportProcess>(){});
        importProcess.setSuccessCount(importProcess.getSuccessCount() + successNum);
        // 计算导入速度
        double speed = (new Date().getTime() - importProcess.getStartTime().getTime()) / 1000 * 1.0;
        // 计算导入剩余时间
        Integer leaveTime = (int) Math.ceil((speed / importProcess.getProcessCount() * 1.0)
                * (importProcess.getTotalRecord() - importProcess.getProcessCount()));
        importProcess.setLeaveTime(Math.abs(leaveTime));
        importProcess.setProcessCount(importProcess.getProcessCount() + successNum);
        // 导入结束
        if (StringUtils.isNotEmpty(filePath)) {
            importProcess.setEndTime(new Date());
            importProcess.setExportFilePath(filePath);
        }
        redisService.set(String.format(Constants.REDIS_PROCESS_DETAIL, processId), importProcess);
    }

}
