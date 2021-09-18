package com.aspire.mirror.alert.server.util;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 告警业务工具类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.util
 * 类名称:    ExcelReaderUtil.java
 * 类描述:    Excel解析工具类
 * 创建人:    LiangJun
 * 创建时间:  2019/7/10 14:55
 * 版本:      v1.0
 */
public class ExcelReaderUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExcelReaderUtil.class);


    public static List<ExcelSheetPO> readExcel (File file, Integer rowCount, Integer columnCount)
            throws FileNotFoundException, IOException {
        String fileName = file.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        Workbook wb = null;
        if (".xls".equals(ext)) {
            wb = new HSSFWorkbook(new FileInputStream(file));
        } else if (".xlsx".equals(ext)) {
            wb = new XSSFWorkbook(new FileInputStream(file));
        } else {
            throw new IllegalArgumentException("Invalid excel version");
        }
        List<ExcelSheetPO> sheetPOs = new ArrayList<>();
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            List<List<Object>> dataList = new ArrayList<>();
            ExcelSheetPO sheetPO = new ExcelSheetPO();
            sheetPO.setSheetName(sheet.getSheetName());
            sheetPO.setDataList(dataList);
            int readRowCount = 0;
            if (rowCount == null || rowCount > sheet.getPhysicalNumberOfRows()) {
                readRowCount = sheet.getPhysicalNumberOfRows();
            } else {
                readRowCount = rowCount;
            }
            // 解析sheet 的行
            for (int j = sheet.getFirstRowNum(); j < readRowCount; j++) {
                Row row = sheet.getRow(j);
                if (row == null) {
                    continue;
                }
                if (row.getFirstCellNum() < 0) {
                    continue;
                }
                int readColumnCount = 0;
                if (columnCount == null || columnCount > row.getLastCellNum()) {
                    readColumnCount = (int) row.getLastCellNum();
                } else {
                    readColumnCount = columnCount;
                }
                List<Object> rowValue = new LinkedList<>();
                // 解析sheet 的列
                for (int k = 0; k < readColumnCount; k++) {
                    Cell cell = row.getCell(k);
                    rowValue.add(getCellValue(wb, cell));
                }
                dataList.add(rowValue);
            }
            sheetPOs.add(sheetPO);
        }
        return sheetPOs;
    }

    private static Object getCellValue (Workbook wb, Cell cell) {
        Object columnValue = null;
        if (cell != null) {
            DecimalFormat df = new DecimalFormat("0");// 格式化 number
            // String
            // 字符
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
            DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    columnValue = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                        columnValue = df.format(cell.getNumericCellValue());
                    } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                        columnValue = nf.format(cell.getNumericCellValue());
                    } else {
                        columnValue = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    columnValue = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK:
                    columnValue = "";
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    // 格式单元格
                    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
                    evaluator.evaluateFormulaCell(cell);
                    CellValue cellValue = evaluator.evaluate(cell);
                    columnValue = cellValue.getNumberValue();
                    break;
                default:
                    columnValue = cell.toString();
            }
        }
        return columnValue;
    }

    /*
    *  接收MultipartFile对象
    * */
    public static List<ExcelSheetPO> readExcel (MultipartFile file, Integer rowCount, Integer columnCount)
            throws IOException {
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        Workbook wb = null;
        if (".xls".equals(ext)) {
            wb = new HSSFWorkbook(file.getInputStream());
        } else if (".xlsx".equals(ext)) {
            wb = new XSSFWorkbook(file.getInputStream());
        } else {
            throw new IllegalArgumentException("Invalid excel version");
        }
        List<ExcelSheetPO> sheetPOs = new ArrayList<>();
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            List<List<Object>> dataList = new ArrayList<>();
            ExcelSheetPO sheetPO = new ExcelSheetPO();
            sheetPO.setSheetName(sheet.getSheetName());
            sheetPO.setDataList(dataList);
            int readRowCount = 0;
            if (rowCount == null || rowCount > sheet.getPhysicalNumberOfRows()) {
                readRowCount = sheet.getPhysicalNumberOfRows();
            } else {
                readRowCount = rowCount;
            }
            // 解析sheet 的行
            for (int j = sheet.getFirstRowNum(); j < readRowCount; j++) {
                Row row = sheet.getRow(j);
                if (row == null) {
                    continue;
                }
                if (row.getFirstCellNum() < 0) {
                    continue;
                }
                int readColumnCount = 0;
                if (columnCount == null || columnCount > row.getLastCellNum()) {
                    readColumnCount = (int) row.getLastCellNum();
                } else {
                    readColumnCount = columnCount;
                }
                if(j == 0) {
                    String[] headers = new String[columnCount];
                    for (int k = 0; k < readColumnCount; k++) {
                        Cell cell = row.getCell(k);
                        headers[k] = getCellValue(wb, cell).toString();
                    }
                    sheetPO.setHeaders(headers);
                } else {
                    List<Object> rowValue = new LinkedList<>();
                    // 解析sheet 的列
                    for (int k = 0; k < readColumnCount; k++) {
                        Cell cell = row.getCell(k);
                        rowValue.add(getCellValue(wb, cell));
                    }
                    dataList.add(rowValue);
                }
            }
            sheetPOs.add(sheetPO);
        }
        return sheetPOs;
    }
}
