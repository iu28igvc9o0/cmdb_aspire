package com.aspire.ums.cmdb.deviceStatistic.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
public class POIModuleUtils {

    private static final String SSFNAME_SUFFIX = "_range_name";

    /**
     * 导出模型配置模板
     */
    public void generateExcel(SXSSFWorkbook wb, String sheetName, List<String> headerList, List<String> keyList, List<Map<String, Object>> dataList, Map<String, List<Map<String, Object>>> comboDataMap) {
        try {
            // 创建2个sheet页 第一个sheet存值 第二个存下拉框列表
            SXSSFSheet sheet1 = wb.createSheet(sheetName);
            SXSSFSheet sheet2 = wb.createSheet("sheet2");
            sheet2.protectSheet("aspire");
            // 生成数据行
            sheet1.createRow(0);
            sheet2.createRow(0);
            // 生成数据列
            this.createSheetCell(wb, sheet1, sheet2, headerList, keyList, dataList, comboDataMap);
        } catch (Exception e) {
            log.error("Excel[" + sheetName + "]数据下载失败！", e);
        }
    }

    /**
     * 获取样式
     * @param wb wb
     * @return
     */
    private CellStyle getStyle(SXSSFWorkbook wb) {
        // 生成一个样式
        CellStyle style = wb.createCellStyle();
        // 设置样式
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置单无格的边框为粗体
        style.setBorderBottom(BorderStyle.THIN);
        // 设置单元格的边框颜色．
        style.setBottomBorderColor(IndexedColors.BLACK.index);
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.index);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.index);
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.index);
        // 指定单元格居中对齐
        style.setAlignment(HorizontalAlignment.CENTER);
        // 指定单元格垂直居中对齐
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 指定单元格自动换行
        style.setWrapText(true);
        return style;
    }

    /**
     * 获取字体样式
     * @param wb wb
     * @return
     */
    private Font getFont(SXSSFWorkbook wb) {
        // 生成一个字体
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontName("宋体");
        font.setFontHeight((short) 230);
        return font;
    }

    private void createSheetCell(SXSSFWorkbook wb, SXSSFSheet sheet1, SXSSFSheet sheet2, List<String> headerList, List<String> keyList,
                                 List<Map<String, Object>> dataList, Map<String, List<Map<String, Object>>> comboDataMap) {

        CellStyle style = getStyle(wb);
        Font font = getFont(wb);
        long startDate = System.currentTimeMillis();
        // 获取sheet2的最大行数
        int maxRowCount = 1;
        if (comboDataMap != null && comboDataMap.keySet().size() > 0) {
            for (Map.Entry<String, List<Map<String, Object>>> entry : comboDataMap.entrySet()) {
                List<Map<String, Object>> comboDataList = entry.getValue();
                if (comboDataList != null && comboDataList.size() > maxRowCount) {
                    maxRowCount = comboDataList.size();
                }
            }
        }
        // 先生成sheet2的数据
        for (int i = 0; i < maxRowCount; i ++) {
            SXSSFRow row = sheet2.getRow(i);
            if (row == null) {
                row = sheet2.createRow(i);
            }
            for (int c = 0; c < headerList.size() ; c++) {
                String cellTitle = headerList.get(c);
                // 表示第一行
                if (i == 0) {
                    if (cellTitle.contains("[必填]")) {
                        font.setColor(IndexedColors.RED.index);
                    } else {
                        font.setColor(IndexedColors.BLACK.index);
                    }
                    style.setFont(font);
                    SXSSFCell sheet2RowCell1 = row.createCell(c);
                    sheet2RowCell1.setCellStyle(style);
                    sheet2RowCell1.setCellValue(cellTitle);
                    sheet2.setColumnWidth(i, 5000);
                } else {
                    SXSSFCell rowCell = row.createCell(c);
                    List<Map<String, Object>> comboDataList = null;
                    if (comboDataMap.containsKey(cellTitle) && comboDataMap.get(cellTitle) != null) {
                        comboDataList = comboDataMap.get(cellTitle);
                    }
                    if (comboDataList != null && comboDataList.size() > 0 && comboDataList.size() > i - 1) {
                        rowCell.setCellValue(comboDataList.get(i - 1).get("value").toString());
                        // 添加下拉框数据源 []需要名称管理器不能识别 需要替换
                        Name hssfName = wb.getName(cellTitle.replace("[必填]", "") + SSFNAME_SUFFIX);
                        if (hssfName == null) {
                            hssfName = wb.createName();
                            String cellAddress = rowCell.getAddress().toString();
                            int cellAtRowIndex = rowCell.getAddress().getRow();
                            cellAddress = cellAddress.replace(String.valueOf(cellAtRowIndex + 1), "");
                            hssfName.setRefersToFormula(sheet2.getSheetName() + "!$" + cellAddress + "$2:$" + cellAddress + "$" + (comboDataList.size() + 1));
                            hssfName.setNameName(cellTitle.replace("[必填]", "") + SSFNAME_SUFFIX);
                        }
                    }
                }
            }
        }
        log.info("==========> 1. 生成sheet2耗时 {}", (System.currentTimeMillis() - startDate));
        startDate = System.currentTimeMillis();
        // 生成sheet1的数据
        DataValidationHelper dvHelper = sheet1.getDataValidationHelper();
        // 生成sheet1表头
        for (int i = 0; i < headerList.size(); i ++) {
            String cellTitle = headerList.get(i);
            if (cellTitle.contains("[必填]")) {
                font.setColor(IndexedColors.RED.index);
            } else {
                font.setColor(IndexedColors.BLACK.index);
            }
            style.setFont(font);
            // 第一个sheet
            SXSSFRow sheet1Row = sheet1.getRow(0);
            SXSSFCell sheet1RowCell = sheet1Row.createCell(i);
            sheet1RowCell.setCellStyle(style);
            sheet1RowCell.setCellValue(cellTitle);
            sheet1.setColumnWidth(i, 5000);
        }
        // 生成sheet1数据
        if (dataList != null && dataList.size() > 0) {
            // 按照1000条为一个线程 多线程写入sheet
            int totalPage = dataList.size() % 1000 == 0 ? (dataList.size() / 1000) : (dataList.size() / 1000 + 1);
            for (int p = 0; p < totalPage; p ++) {
                int startIndex = p * 1000 + 1;
                int endIndex = (p + 1) * 1000;
                log.info("Start write index {}, {}", startIndex, endIndex);
                writeExcelPage(wb, sheet1, dvHelper, headerList, keyList, dataList, startIndex, endIndex);
            }
        }
        log.info("==========> 2. 生成sheet1耗时 {}", (System.currentTimeMillis() - startDate));
    }

    private synchronized SXSSFRow getRow(SXSSFSheet sheet, int number) {
        SXSSFRow row = sheet.getRow(number);
        if (row == null) {
            row = sheet.createRow(number);
        }
        return row;
    }

    private void writeExcelPage(SXSSFWorkbook wb,
                                SXSSFSheet sheet1,
                                DataValidationHelper dvHelper,
                                List<String> headerList,
                                List<String> keyList,
                                List<Map<String, Object>> dataList,
                                int startIndex,
                                int endIndex) {
        try {
            for (int r = startIndex - 1; r < dataList.size() && r < endIndex; r ++) {
                SXSSFRow row = getRow(sheet1, r + 1);
                for (int c = 0; c < headerList.size() ; c++) {
                    String dataKey = keyList.get(c);
                    SXSSFCell cell1 = row.createCell(c);
                    String cellValue = dataList.get(r).get(dataKey) == null ? "" : dataList.get(r).get(dataKey).toString();
                    cell1.setCellValue(cellValue);
                    // 创建名称管理器
                    Name name = wb.getName(headerList.get(c).replace("[必填]", "") + SSFNAME_SUFFIX);
                    if (name != null) {
                        XSSFDataValidationConstraint dvConstraint1 = new XSSFDataValidationConstraint(DataValidationConstraint.ValidationType.LIST,
                                name.getRefersToFormula());
                        CellRangeAddressList rangeList = new CellRangeAddressList(1, dataList.size() + 1, c, c);
                        XSSFDataValidation ctDataValidation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint1, rangeList);
                        ctDataValidation.createErrorBox("数据错误", "数据不在可选范围,请选择正确数据.");
                        ctDataValidation.setShowErrorBox(true);
                        ctDataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
                        sheet1.addValidationData(ctDataValidation);
                    }
                }
            }
        } catch (Exception e) {
            log.error("生成Excel数据失败, 原因: {}", e.getLocalizedMessage(), e);
        }
    }

}
