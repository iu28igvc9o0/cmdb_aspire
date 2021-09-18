package com.aspire.ums.cmdb.deviceStatistic.util;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportExcelUtil {

    private static final long serialVersionUID = 2165773254718823136L;
    public void exportExcel(Workbook workbook, int sheetNum, String sheetTitle, String[] headers, List<Map<String, Object>> result, String[] listKey) throws Exception {
        // 生成一个表格
        Sheet sheet = workbook.createSheet();
        workbook.setSheetName(sheetNum, sheetTitle);
        // 生成一个样式  
        CellStyle style = workbook.createCellStyle();
        // 设置样式
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单无格的边框为粗体 
        style.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色． 
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setRightBorderColor(HSSFColor.BLACK.index);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setTopBorderColor(HSSFColor.BLACK.index);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐 
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐 
        style.setWrapText(true);// 指定单元格自动换行 
        // 生成一个字体
        Font font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 230);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 指定当单元格内容显示不下时自动换行
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
        // 产生表格标题行
        Row row = sheet.createRow(0);
        String[] testva = new String[headers.length];
        for (int i = 0; i < headers.length; i++) {
            testva[i] = "88888888";
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text.toString());
        }
        row.setHeightInPoints(30);
        // 遍历集合数据，产生数据行
        if (result.size() != 0) {
            int index = 1;
            for (Map map : result) {
                row = sheet.createRow(index);
                int cellIndex = 0;
                for (String key : listKey) {
                    if (null == testva[cellIndex]) {
                        testva[cellIndex] = "0";
                    }
                    if (null != map && null != map.get(key)) {
                        Cell cell = row.createCell(cellIndex);
                        String va = map.get(key).toString();
                        if (va.getBytes().length > testva[cellIndex].getBytes().length) {
                            testva[cellIndex] = va;
                        }
                        if (!StringUtils.isEmpty(va) && isNum(va)) {
                            //是数字当作double处理
                            if (va.length() > 11) {
                                cell.setCellValue(va);
                                cell.setCellStyle(style1);
                            } else {
                                cell.setCellValue(Double.parseDouble(va));
                                cell.setCellStyle(style1);
                            }
                        } else {
                            cell.setCellValue(map.get(key).toString());
                            cell.setCellStyle(style1);

                        }
                    } else {
                        Cell cell = row.createCell(cellIndex);
                        cell.setCellValue("");
                        cell.setCellStyle(style1);
                    }
                    cellIndex++;
                }
                index++;
            }
        } else {
            // 如果内容为空，则提示无符合条件记录
            row = sheet.createRow(1);
            Cell cell = row.createCell(2);
        }
        for (int i = 0; i < testva.length; i++) {
            String t = testva[i] + "88";
            //如果数据长度大于列宽允许设置的最大值,将列宽设置为默认值
            if (t.getBytes().length * 256 > 256 * 15) {
                sheet.setColumnWidth(i, "888888888888888".getBytes().length * 256);
            } else {
                sheet.setColumnWidth(i, t.getBytes().length * 256);
            }
        }
    }

    public boolean isNum(String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?)$");
    }

    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
//            field.setAccessible(true);
            ReflectionUtils.makeAccessible(field);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }

    /**
     * 生成多表头 多sheet页 合并行的表格
     */
    public static void exportMergedExcel(Workbook workbook, int sheetNum, String sheetTitle, String[] headers, List<Map<String, Object>> result, String[] listKey) {
//        // 创建sheet页
//        Sheet sheet = workbook.createSheet();
//        workbook.setSheetName(sheetNum, sheetTitle);
//
//        // 第一行
//        Row r0 = sheet.createRow(0);
//        r0.setHeight((short) 800);
//
//        //
//
//        Cell c00 = r0.createCell(0);
//        c00.setCellValue(dto.getBbmc());
//        c00.setCellStyle(headerStyle);
//        //合并单元格
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
    }
}
