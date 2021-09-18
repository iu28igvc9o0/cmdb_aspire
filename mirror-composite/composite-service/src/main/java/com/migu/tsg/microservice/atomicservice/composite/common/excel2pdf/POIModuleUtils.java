package com.migu.tsg.microservice.atomicservice.composite.common.excel2pdf;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class POIModuleUtils {

    /**
     * 导出模型配置模板
     */
    public void generateExcel(XSSFWorkbook wb, String sheetName, List<String> headerList, List<Map<String, Object>> dataList, Map<String, List<Map<String, Object>>> comboDataMap) {
        try {
            // 创建2个sheet页 第一个sheet存值 第二个存下拉框列表
            XSSFSheet sheet1 = wb.createSheet(sheetName);
            XSSFSheet sheet2 = wb.createSheet("sheet2");
            sheet2.protectSheet("aspire");
            // 生成数据行
            sheet1.createRow(0);
            sheet2.createRow(0);
            // 生成数据列
            this.createSheetCell(wb, sheet1, sheet2, headerList, dataList, comboDataMap);
        } catch (Exception e) {
            log.error("Excel[" + sheetName + "]数据下载失败！", e);
        }
    }

    private void createSheetCell(XSSFWorkbook wb, XSSFSheet sheet1, XSSFSheet sheet2, List<String> headerList, List<Map<String, Object>> dataList,
            Map<String, List<Map<String, Object>>> comboDataMap) {
        // 生成一个样式
        XSSFCellStyle style = wb.createCellStyle();
        // 设置样式
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN); // 设置单无格的边框为粗体
        style.setBottomBorderColor(IndexedColors.BLACK.index); // 设置单元格的边框颜色．
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.index);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.index);
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.index);
        style.setAlignment(HorizontalAlignment.CENTER); // 指定单元格居中对齐
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 指定单元格垂直居中对齐
        style.setWrapText(true);// 指定单元格自动换行

        // 生成一个字体
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setFontName("宋体");
        font.setFontHeight((short) 230);

        for (int i = 0; i < headerList.size(); i ++) {
            String cellTitle = headerList.get(i);
            if (cellTitle.contains("[必填]")) {
                font.setColor(IndexedColors.RED.index);
            } else {
                font.setColor(IndexedColors.BLACK.index);
            }
            style.setFont(font);
            // 第一个sheet
            XSSFRow sheet1Row = sheet1.getRow(0);
            XSSFCell sheet1RowCell = sheet1Row.createCell(i);
            sheet1RowCell.setCellStyle(style);
            sheet1RowCell.setCellValue(cellTitle);
            sheet1.setColumnWidth(i, "88888888888888888888888888".getBytes().length * 256);
            // 生个sheet1的数据
            if (dataList != null && dataList.size() > 0) {
                for (int k = 1; k <= dataList.size(); k ++) {
                    XSSFRow sheet1RowK = sheet1.getRow(k);
                    if (sheet1RowK == null) {
                        sheet1RowK = sheet1.createRow(k);
                    }
                    // 生成数据
                    XSSFCell cell1 = sheet1RowK.getCell(i);
                    if (cell1 == null) {
                        cell1 = sheet1RowK.createCell(i);
                    }
                    // 生成数据
                    String cellValue = dataList.get(k-1).get(cellTitle) == null ? "" : dataList.get(k-1).get(cellTitle).toString();
                    cell1.setCellValue(cellValue);
                }
            }
            // 生成第二个
            // 第二个sheet
            XSSFRow sheet2Row = sheet2.getRow(0);
            XSSFCell sheet2RowCell1 = sheet2Row.createCell(i);
            sheet2RowCell1.setCellStyle(style);
            sheet2RowCell1.setCellValue(cellTitle);
            sheet2.setColumnWidth(i, "88888888888888888888888888".getBytes().length * 256);

            List<Map<String, Object>> comboDataList = null;
            if (comboDataMap.containsKey(cellTitle) && comboDataMap.get(cellTitle) != null) {
                comboDataList = comboDataMap.get(cellTitle);
            }
            if (comboDataList != null && comboDataList.size() > 0) {
                // sheet2 cell 处理
                for (int j = 1; j <= comboDataList.size(); j ++) {
                    XSSFRow sheet2RowJ = sheet2.getRow(j);
                    if (sheet2RowJ == null) {
                        sheet2RowJ = sheet2.createRow(j);
                    }
                    XSSFCell cell1 = sheet2RowJ.getCell(i);
                    if (cell1 == null) {
                        cell1 = sheet2RowJ.createCell(i);
                    }
                    cell1.setCellValue(comboDataList.get(j - 1).get("value").toString());
                }
                // 添加下拉框数据源 []需要名称管理器不能识别 需要替换
                XSSFName hssfName = wb.getName(cellTitle.replace("[必填]", "") + "_range_name");
                if (hssfName == null) {
                    hssfName = wb.createName();
                    String cellAddress = sheet2Row.getCell(i).getAddress().toString();
                    int cellRow = sheet2Row.getCell(i).getAddress().getRow();
                    cellAddress = cellAddress.replace(String.valueOf(cellRow + 1), "");
                    hssfName.setRefersToFormula(sheet2.getSheetName() + "!$" + cellAddress + "$2:$" + cellAddress + "$" + (comboDataList.size() + 1));
                    hssfName.setNameName(cellTitle.replace("[必填]", "") + "_range_name");
                }
                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet1);
                XSSFDataValidationConstraint dvConstraint1 = new XSSFDataValidationConstraint(DataValidationConstraint.ValidationType.LIST,
                        hssfName.getRefersToFormula());
                CellRangeAddressList rangeList = new CellRangeAddressList(1, 65535, i, i);
                XSSFDataValidation ctDataValidation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint1, rangeList);
                ctDataValidation.createErrorBox("数据错误", "数据不在可选范围,请选择正确数据.");
                ctDataValidation.setShowErrorBox(true);
                ctDataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
                sheet1.addValidationData(ctDataValidation);
            }
        }
    }

}
