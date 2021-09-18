package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.process;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.aspire.mirror.composite.service.cmdb.process.payload.ProcessParams;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.process.ProcessClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.process.conf.ImportTemplate;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ProcessThread
 * Author:   zhu.juwang
 * Date:     2019/6/21 16:02
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@Slf4j
public abstract class ProcessThread implements Runnable {
    protected String processId;
    protected Sheet sheet;
    protected Sheet comboSheet; // 下拉框数据源sheet页
    protected String importType;
    protected ProcessClient processClient;
    protected ImportTemplate importTemplate;
    protected ProcessParams processParams;

    @Override
    public void run() {
        try {
            //解析数据成List<Map>
            this.init();
            int totalRows = sheet.getPhysicalNumberOfRows();
            Row firstRow = sheet.getRow(getRealHeaderIndex());
            List<Map<String, String>> dataList = new LinkedList<>();
            this.valid();
            for (int r = getDataIndex() ; r < totalRows; r++) {
                Map<String, String> keyValueMap = new LinkedHashMap<>();
                for (int c = 0; c < sheet.getRow(0).getPhysicalNumberOfCells(); c++) {
                    Cell titleCell = firstRow.getCell(c);
                    if (sheet.getRow(r) == null) {
                        continue;
                    }
                    if (titleCell == null) {
                        continue;
                    }
                    //获取每一列的值与数据
                    String cellText = getHeaderCellText(titleCell);
                    if (cellText == null) {
                        continue;
                    }
                   String cellValue = getDataCellText(sheet.getRow(r).getCell(c));
                    this.setDataMap(keyValueMap, cellText, cellValue);
                }
                if (keyValueMap.size() > 0) {
                    dataList.add(keyValueMap);
                }
            }
            Map<String, Object> importData = new HashMap<>();
            if (dataList.size() > 0) {
                // 每种导入类型 可以重写此方法, 设置自己需要设置的值
                setSpecialData(importData);
                setComboData(importData);
                importData.put("processId", processId);
                importData.put("dataList", dataList);
                importData.put("processParams", JSONObject.fromObject(processParams));
                importData.put("headerList", new LinkedList<>(dataList.get(0).keySet()));
                try {
                    processClient.importProcess(importType, importData);
                    log.info("is success");
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 设置数据Map的值
     * @param keyValueMap
     * @param key
     * @param value
     */
    protected abstract void setDataMap(Map<String, String> keyValueMap, String key, String value) ;

    protected abstract void setSpecialData(Map<String, Object> importData);

    protected void setComboData(Map<String, Object> importData) {
        Map<String, List<Map<String, Object>>> comboDataMap = new LinkedHashMap<>();
        if (comboSheet != null) {
            int totalRows = comboSheet.getPhysicalNumberOfRows();
            Row firstRow = comboSheet.getRow(0);
            if (firstRow != null) {
                for (int c = 0; c < firstRow.getPhysicalNumberOfCells(); c++) {
                    List<Map<String, Object>> dataList = new LinkedList<>();
                    Cell titleCell = firstRow.getCell(c);
                    if (titleCell == null) {
                        continue;
                    }
                    //获取每一列的值与数据
                    String cellText = getHeaderCellText(titleCell);
                    if (cellText == null) {
                        continue;
                    }
                    for (int r = 1; r <= totalRows; r ++) {
                        if (comboSheet.getRow(r) == null) {
                            continue;
                        }
                        String cellValue = getDataCellText(comboSheet.getRow(r).getCell(c));
                        if (StringUtils.isEmpty(cellValue)) {
                            continue;
                        }
                        Map<String, Object> keyValueMap = new LinkedHashMap<>();
                        keyValueMap.put("value", cellValue);
                        dataList.add(keyValueMap);
                    }
                    if (dataList.size() > 0) {
                        comboDataMap.put(cellText, dataList);
                    }
                }
            }
        }
        importData.put("comboDataMap", comboDataMap);
    }

    protected String getHeaderCellText(Cell titleCell) {
        String cellText = "";
        if(titleCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            cellText = (int)titleCell.getNumericCellValue() + "";
        } else {
            cellText = titleCell.getStringCellValue().trim();
        }
        if (cellText.equals("失败原因")) {
            cellText = null;
        }
        return  cellText;
    }

    protected String getDataCellText(Cell dataCell) {
        String cellValue = "";
        if (dataCell != null) {
            // 判断数据的类型
            switch (dataCell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: // 数字
                    short format = dataCell.getCellStyle().getDataFormat();
                    if (DateUtil.isCellDateFormatted(dataCell)) {
                        SimpleDateFormat sdf = null;
                        // System.out.println("cell.getCellStyle().getDataFormat()="+cell.getCellStyle().getDataFormat());
                        if (format == 20 || format == 32) {
                            sdf = new SimpleDateFormat("HH:mm");
                        } else if (format == 14 || format == 31 || format == 57 || format == 58) {
                            // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                            sdf = new SimpleDateFormat("yyyy-MM-dd");
                            double value = dataCell.getNumericCellValue();
                            Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                        cellValue = sdf.format(date);
                        } else {// 日期
                            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        }
                        try {
                            cellValue = sdf.format(dataCell.getDateCellValue());// 日期
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        BigDecimal bd = new BigDecimal(dataCell.getNumericCellValue());
                        cellValue = bd.toPlainString();// 数值 这种用BigDecimal包装再获取plainString，可以防止获取到科学计数值
                    }
                    break;

                // case Cell.CELL_TYPE_NUMERIC: // 数字
                // if (HSSFDateUtil.isCellDateFormatted(dataCell)) {// 处理日期格式、时间格式
                // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");;
                // Date date = dataCell.getDateCellValue();
                // cellValue = sdf.format(date);
                // } else if (dataCell.getCellStyle().getDataFormat() == 0) {//处理数值格式 General格式
                //// dataCell.setCellType(Cell.CELL_TYPE_STRING);
                //// cellValue = String.valueOf(dataCell.getRichStringCellValue().toString());
                //// if (cellValue != null && cellValue.endsWith(".0")) {
                //// cellValue = cellValue.substring(0, cellValue.length() - 2);
                //// }
                // DecimalFormat df = new DecimalFormat("#.##########");
                // Double value = dataCell.getNumericCellValue();
                // cellValue = df.format(value);
                // } else {
                // if("#,##0.00".equals(dataCell.getCellStyle().getDataFormatString())) { // 货币格式‘#,##0.00’会在数字后添加随机的浮点数
                // cellValue = String.valueOf(dataCell.getNumericCellValue());
                // } else {
                //// dataCell.setCellType(Cell.CELL_TYPE_STRING);
                //// cellValue = String.valueOf(dataCell.getRichStringCellValue().toString());
                // DecimalFormat df = new DecimalFormat("#.##########");
                // Double value = dataCell.getNumericCellValue();
                // cellValue = df.format(value);
                // }
                // }
                // break;
                case Cell.CELL_TYPE_STRING: // 字符串
                    cellValue = String.valueOf(dataCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN: // Boolean
                    cellValue = String.valueOf(dataCell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA: // 公式
                    cellValue = String.valueOf(dataCell.getCellFormula());
                    break;
                default:
                    break;
            }
        }
        return cellValue;
    }

    protected abstract void init();

    protected abstract void valid();

    protected void validHeader() {
        List<String> headers = getTemplateHeader();
        List<String> sheetHeader = new LinkedList<>();
        if (headers.size() > 0) {
            Integer headerStart = getRealHeaderIndex();
            for (int i = 0; i < sheet.getRow(headerStart).getPhysicalNumberOfCells(); i++) {
                Cell eCell = sheet.getRow(headerStart).getCell(i);
                sheetHeader.add(eCell.getStringCellValue().trim());
            }
            for (String header : headers) {
                if (!sheetHeader.contains(header)) {
                    throw new RuntimeException("缺少列[" + header + "], 请检测并重新上传.");
                }
            }
        }
    }

    protected List<String> getTemplateHeader() {
        return new ArrayList<>();
    }

    protected Integer getRealHeaderIndex() {
        return 0;
    }

    protected Integer getDataIndex() {
        if (null == processParams.getDataStart()) {
            processParams.setDataStart(1);
        }
        return processParams.getDataStart();
    }
}
