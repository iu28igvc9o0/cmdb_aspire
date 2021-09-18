package com.aspire.ums.cmdb.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-06-22 17:27
 * @description 资产模板导出工具类
 */
@Slf4j
public class AssetExcelExportUtils {

    private static String EXCEL_HIDE_SHEET_NAME = "excelhidesheetname";
    private static String EXCEL_HIDE_SHEET_NAME2 = "excelhidesheetname2";
    private static final int XLS_MAX_ROW = 65535; // 0开始

    private HashMap map = new HashMap();
    // 设置下拉列表的内容
    private static String[] headerLists;
    private static List<List<Object>> result;
    private static List<String> hideString;
    private static int tempSize;
    private static int tempSize1;
    private static String[] tempRow = null;

    /**
     * 资产导出
     * @param wb excel模板
     * @param results 模板其他参数取值数组
     * @param headerList excel模板头部字段
     * @param title 默认为 资产信息表模板
     */
    public static void cmdbUtilss(XSSFWorkbook wb, List<List<Object>> results, String[] headerList, String title) {
        result = results;
        headerLists = headerList;
        // 创建标题栏
        createExcelMo(wb, title);
        // 重要方法！！！创建隐藏的Sheet页
        creatExcelHidePageForCmdb(wb);
        // 重要方法！！！ 数据验证 下拉,级联
        setDataValidationForCmdb(wb);

    }

    public static void createExcelMo(XSSFWorkbook wb, String title) {
        Sheet sheet = wb.createSheet(title);
        // 生成一个样式
        XSSFCellStyle style = wb.createCellStyle();

        // 设置样式
        // style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(CellStyle.BORDER_THIN); // 设置单无格的边框为粗体
        style.setBottomBorderColor(IndexedColors.BLACK.index); // 设置单元格的边框颜色．
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.index);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.index);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.index);
        style.setAlignment(CellStyle.ALIGN_CENTER); // 指定单元格居中对齐
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
        style.setWrapText(true);// 指定单元格自动换行

        // 生成一个字体

        XSSFFont font = wb.createFont();
        // font.setColor(HSSFColor.BLACK.index);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 230);

        // 把字体应用到当前的样式

        style.setFont(font);

        // 指定当单元格内容显示不下时自动换行

        XSSFCellStyle style1 = wb.createCellStyle();
        style1.setBorderBottom(CellStyle.BORDER_THIN); // 设置单无格的边框为粗体
        style1.setBottomBorderColor(IndexedColors.BLACK.index); // 设置单元格的边框颜色．
        style1.setBorderLeft(CellStyle.BORDER_THIN);
        style1.setLeftBorderColor(IndexedColors.BLACK.index);
        style1.setBorderRight(CellStyle.BORDER_THIN);
        style1.setRightBorderColor(IndexedColors.BLACK.index);
        style1.setBorderTop(CellStyle.BORDER_THIN);
        style1.setTopBorderColor(IndexedColors.BLACK.index);
        // style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
        style1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
        style1.setWrapText(true);// 指定单元格自动换行

        // 产生表格标题行

        Row row = sheet.createRow(0);
        String[] testva = new String[headerLists.length];
        for (int i = 0; i < headerLists.length; i++) {
            testva[i] = "888888888888888888";// 表格宽度
            Cell cell = row.createCell(i);

            cell.setCellStyle(style);

            XSSFRichTextString text = new XSSFRichTextString(headerLists[i]);

            cell.setCellValue(text.toString());
        }
        for (int i = 0; i < testva.length; i++) {// 改变列的宽度
            String t = testva[i] + "88888888";
            // 如果数据长度大于列宽允许设置的最大值,将列宽设置为默认值
            if (t.getBytes().length * 256 > 62580) {
                sheet.setColumnWidth(i, "888888888".getBytes().length * 256);
            } else {
                sheet.setColumnWidth(i, t.getBytes().length * 256);
            }
        }
    }

    public static void creatExcelHidePageForCmdb(XSSFWorkbook workbook) {
        Sheet hideInfoSheet = workbook.createSheet(EXCEL_HIDE_SHEET_NAME);// 设置隐藏 页的标题
        // 在隐藏页设置选择信息
        // 有些i需要我们手动控制
        int L = 0;
        ArrayList<String> arrayList = new ArrayList<>();// 把所有标题
        for (int i = 0; i < result.size(); i++) {
            log.debug("i==={}", i);
            // if (i == 0 || i == 18 || i == 19 || ((i >= 22 && i <= 69) && (i != 43 && i != 45 && i != 46))) { // 此判断用于 无下拉框的列
            if (i == 0
                    || i == 18
                    || i == 19
                    || ((i >= 22 && i <= 86) && (i != 23 && i != 40 && i != 42 && i != 43 && i != 66 && i != 67 && i != 69
                    && i != 72 && i != 75 && i != 82 && i != 90 && i != 103))) {
                Row row = hideInfoSheet.createRow(L); // 创建一行
                String[] rowS = (String[]) result.get(i).get(0);// 把预先数据转成字符串数组
                creatRow(row, rowS);// 把字符串数组，遍历，设置列的内容
                String rowString = (String) result.get(i).get(1);// 获取标题
                arrayList.add(rowString);
                hideString = arrayList;
                L++;
            } else if (i == 1) {// 联动的开头者 indirect()
                Row row = hideInfoSheet.createRow(L);
                tempRow = (String[]) result.get(i).get(0);
                creatRow(row, tempRow);
                String rowString = (String) result.get(i).get(1);
                arrayList.add(rowString);
                hideString = arrayList;
                creatExcelNameList(workbook, rowString, L + 1, tempRow.length, false);
                L++;
            } else if (i == 2) {// 联动关联者

                int k = 0;
                List<Map<String, Object>> business = (List<Map<String, Object>>) result.get(i).get(0);
                for (Map<String, Object> map : business) {
                    ArrayList<String> business2List = (ArrayList<String>) map.get("BUSINESS_LEVEL2");
                    String[] business2 = (String[]) business2List.toArray(new String[0]);
                    Row row = hideInfoSheet.createRow(L);
                    creatRow(row, business2);
                    for (int j = k; j < tempRow.length; j++) {
                        String rowName = tempRow[j];
                        if (rowName.matches("[0-9]+.*")) {
                            rowName = "temp" + rowName;
                        }
                        arrayList.add(rowName);
                        hideString = arrayList;
                        creatExcelNameList(workbook, rowName, L + 1, business2.length, true);
                        L++;
                        k++;
                        break;
                    }

                }
                // 把临时数据置空
                tempRow = null;
            } else if (i >= 3 && i < 6) {
                tempSize = L;
                Row row = hideInfoSheet.createRow(L);
                String[] rowS = (String[]) result.get(i).get(0);
                creatRow(row, rowS);
                String rowString = (String) result.get(i).get(1);
                arrayList.add(rowString);
                hideString = arrayList;
                creatExcelNameList(workbook, rowString, L + 1, rowS.length, false);
                L++;
            } else if (i == 6) {
                Row row = hideInfoSheet.createRow(L);
                tempRow = (String[]) result.get(i).get(0);
                creatRow(row, tempRow);
                String rowString = (String) result.get(i).get(1);
                arrayList.add(rowString);
                hideString = arrayList;
                creatExcelNameList(workbook, rowString, L + 1, tempRow.length, false);
                L++;

            } else if (i == 7) {
                int k = 0;
                List<Map<String, Object>> device = (List<Map<String, Object>>) result.get(i).get(0);
                for (Map<String, Object> map : device) {
                    List<String> deviceLists = (ArrayList<String>) map.get("DEVICE_TYPE");
                    String[] devices = (String[]) deviceLists.toArray(new String[0]);
                    Row row = hideInfoSheet.createRow(L);
                    creatRow(row, devices);
                    for (int j = k; j < tempRow.length; j++) {
                        String rowName = tempRow[j];
                        arrayList.add(rowName);
                        hideString = arrayList;
                        creatExcelNameList(workbook, rowName, L + 1, devices.length, true);
                        L++;
                        k++;
                        break;
                    }

                }
                // 把临时数据置空
                tempRow = null;
            } else if (i == 8) {
                List<Map<String, Object>> model = (List<Map<String, Object>>) result.get(i).get(0);
                for (Map<String, Object> map : model) {
                    List<String> deviceLists = (ArrayList<String>) map.get("DEVICE_TYPE");
                    List<String> modelLists = (ArrayList<String>) map.get("DEVICE_MODEL");
                    String[] models = (String[]) modelLists.toArray(new String[0]);
                    String[] devices = (String[]) deviceLists.toArray(new String[0]);
                    for (int m = 0; m < devices.length; m++) {
                        Row row = hideInfoSheet.createRow(L);
                        creatRow(row, models);
                        String rowName = devices[m];
                        // 存在重复名称的时候，动态为其起新名字
                        if (arrayList.contains(rowName)) {
                            rowName = rowName + "repeat" + L;
                        }

                        arrayList.add(rowName);
                        hideString = arrayList;
                        creatExcelNameList(workbook, rowName, L + 1, models.length, true); // 一个名称对应一个下拉框数据
                        L++;
                    }
                }

                // } else if ((i >= 9 && i <= 17) || (i >= 20 && i <= 21) || i == 43 || i == 45 || i == 46) {
            } else if ((i >= 9 && i <= 17) || (i >= 20 && i <= 21) || i == 23 || i == 40 || i == 42 || i == 43 || i == 66
                    || i == 67 || i == 69 || i == 72 || i == 75 || i == 82 || i == 90 || i == 103) {
                tempSize1 = L;
                Row row = hideInfoSheet.createRow(L);
                String[] rowS = (String[]) result.get(i).get(0);
                creatRow(row, rowS);
                String rowString = (String) result.get(i).get(1);
                arrayList.add(rowString);
                hideString = arrayList;
                creatExcelNameList(workbook, rowString, L + 1, rowS.length, false);
                L++;
            }

        }

        // 设置隐藏页标志
        workbook.setSheetHidden(workbook.getSheetIndex(EXCEL_HIDE_SHEET_NAME), true); // 可以设置为false,可以查看隐藏sheet的内容排列
    }

    public static void setDataValidationForCmdb(XSSFWorkbook wb) {
        int sheetIndex = wb.getNumberOfSheets();
        if (sheetIndex > 0) {
            for (int i = 0; i < sheetIndex; i++) {
                Sheet sheet = wb.getSheetAt(i);
                if (!EXCEL_HIDE_SHEET_NAME.equals(sheet.getSheetName())) {

                    DataValidation business1Validation = getDataValidationCustomFormula(sheet, hideString.get(1), 2, 2);
                    sheet.addValidationData(business1Validation);

                    DataValidation business2Validation = getDataValidationCustomFormula(sheet, "INDIRECT($B2)", 2, 3);
                    sheet.addValidationData(business2Validation);

                    DataValidation idcValidation = getDataValidationCustomFormula(sheet, hideString.get(tempSize - 2), 2, 4);
                    sheet.addValidationData(idcValidation);

                    DataValidation idcLabelValidation = getDataValidationCustomFormula(sheet, hideString.get(tempSize - 1), 2, 5);
                    sheet.addValidationData(idcLabelValidation);

                    // 包含取消提示框校验
                    DataValidation idcLocationValidation = getDataValidationCustomFormula(sheet, hideString.get(tempSize), 2, 6);
                    sheet.addValidationData(idcLocationValidation);

                    DataValidation deviceClassValidation = getDataValidationCustomFormula(sheet, hideString.get(tempSize + 1), 2, 7);
                    sheet.addValidationData(deviceClassValidation);

                    DataValidation deviceTypeValidation = getDataValidationCustomFormula(sheet, "INDIRECT($G2)", 2, 8);
                    sheet.addValidationData(deviceTypeValidation);

                    DataValidation deviceModelValidation = getDataValidationCustomFormula(sheet, "INDIRECT($H2)", 2, 9);
                    sheet.addValidationData(deviceModelValidation);

                    DataValidation deviceOsTypeValidation = getDataValidationCustomFormula(sheet, "device_os_type", 2, 10);
                    sheet.addValidationData(deviceOsTypeValidation);

                    DataValidation deviceNetworkLayerValidation = getDataValidationCustomFormula(sheet,
                            "DEVICE_NETWORK_LAYER_NAME", 2, 11);
                    // DataValidation deviceNetworkLayerValidation = getDataValidationByFormula("INDIRECT($G1)", 11);
                    sheet.addValidationData(deviceNetworkLayerValidation);

                    DataValidation applicationMuduleValidation = getDataValidationCustomFormula(sheet, "APPLICATIONMODULE", 2, 12);
                    sheet.addValidationData(applicationMuduleValidation);

                    DataValidation deviceStatusValidation = getDataValidationCustomFormula(sheet, "deviceStatus", 2, 13);
                    sheet.addValidationData(deviceStatusValidation);

                    DataValidation mgrAnsibleFlagValidation = getDataValidationCustomFormula(sheet, "managed_by_ansible", 2, 14);
                    sheet.addValidationData(mgrAnsibleFlagValidation);

                    DataValidation mgrPoolFlagValidation = getDataValidationCustomFormula(sheet, "mgd_by_pool", 2, 15);
                    sheet.addValidationData(mgrPoolFlagValidation);

                    DataValidation configZabbixFlagValidation = getDataValidationCustomFormula(sheet, "zabbix_agent_monitor_flag",
                            2, 16);
                    sheet.addValidationData(configZabbixFlagValidation);

                    DataValidation autoInstallAgentFlagValidation = getDataValidationCustomFormula(sheet, "amp_agent_monitor_flag",
                            2, 17);
                    sheet.addValidationData(autoInstallAgentFlagValidation);

                    DataValidation filebeatAgentFlagValidation = getDataValidationCustomFormula(sheet,
                            "filebeat_agent_monitor_flag", 2, 18);
                    sheet.addValidationData(filebeatAgentFlagValidation);

                    DataValidation hostBackupValidation = getDataValidationCustomFormula(sheet, "host_backup", 2, 21);
                    sheet.addValidationData(hostBackupValidation);

                    DataValidation resoucePlanValidation = getDataValidationCustomFormula(sheet, "resource_plan", 2, 22);
                    sheet.addValidationData(resoucePlanValidation);

                    DataValidation projectBelongToValidation = getDataValidationCustomFormula(sheet, "cmdbProjectBelongTo", 2, 24);
                    sheet.addValidationData(projectBelongToValidation);

                    /*
                     * 减去 分布式存储类型、高端块存储(GB)(必填)、分布式存储(GB),大概在20-25 DataValidation maintencePurchaseFlagValidation =
                     * getDataValidationByFormula("maintence_purchase_flag", 44);
                     * sheet.addValidationData(maintencePurchaseFlagValidation); DataValidation venderMaintenceFlagValidation =
                     * getDataValidationByFormula("vender_maintence_flag", 46);
                     * sheet.addValidationData(venderMaintenceFlagValidation); DataValidation maintencePurchaseDescValidation =
                     * getDataValidationByFormula("maintence_purchase_desc", 47);
                     * sheet.addValidationData(maintencePurchaseDescValidation);
                     */

                    DataValidation maintencePurchaseFlagValidation = getDataValidationCustomFormula(sheet,
                            "maintence_purchase_flag", 2, 41);
                    sheet.addValidationData(maintencePurchaseFlagValidation);

                    DataValidation venderMaintenceFlagValidation = getDataValidationCustomFormula(sheet, "vender_maintence_flag",
                            2, 43);
                    sheet.addValidationData(venderMaintenceFlagValidation);

                    DataValidation maintencePurchaseDescValidation = getDataValidationCustomFormula(sheet,
                            "maintence_purchase_desc", 2, 44);
                    sheet.addValidationData(maintencePurchaseDescValidation);

                    DataValidation equipmentRiskLevelValidation = getDataValidationCustomFormula(sheet, "EQUIPMENT_RISK_LEVEL", 2,
                            67);
                    sheet.addValidationData(equipmentRiskLevelValidation);

                    DataValidation deviceFactoryValidation = getDataValidationCustomFormula(sheet, "deviceFactoryName", 2, 68);
                    sheet.addValidationData(deviceFactoryValidation);

                    DataValidation driverTypeValidation = getDataValidationCustomFormula(sheet, "driver_type", 2, 70);
                    sheet.addValidationData(driverTypeValidation);

                    DataValidation ipTypeValidation = getDataValidationCustomFormula(sheet, "ipType", 2, 73);
                    sheet.addValidationData(ipTypeValidation);

                    DataValidation resCommunicationLoginTypeValidation = getDataValidationCustomFormula(sheet,
                            "resCommunicationLoginType", 2, 91);
                    sheet.addValidationData(resCommunicationLoginTypeValidation);

                    DataValidation isSetMaxConnValidation = getDataValidationCustomFormula(sheet, "isSetMaxConn", 2, 104);
                    sheet.addValidationData(isSetMaxConnValidation);
                }
            }
        }
    }

    /**
     * 创建一列数据
     *
     * @param currentRow
     * @param textList
     */
    private static void creatRow(Row currentRow, String[] textList) {
        if (textList != null && textList.length > 0) {
            int i = 0;
            for (String cellValue : textList) {
                Cell userNameLableCell = currentRow.createCell(i++);
                userNameLableCell.setCellValue(cellValue);
            }
        }
    }

    /**
     * 创建一个名称
     *
     * @param workbook
     */
    private static void creatExcelNameList(Workbook workbook, String nameCode, int order, int size, boolean cascadeFlag) {
        Name name;
        log.debug("nameCode==" + nameCode);
        name = workbook.createName();
        name.setNameName(nameCode);
        name.setRefersToFormula(EXCEL_HIDE_SHEET_NAME + "!" + creatExcelNameList(order, size, cascadeFlag));
    }

    public static XSSFDataValidation getDataValidationCustomFormula(Sheet sheet, String formulaString, int naturalRowIndex,
                                                                    int naturalColumnIndex) {
        // 四个参数分别是：起始行、终止行、起始列、终止列
        int firstRow = naturalRowIndex - 1;
        int lastRow = naturalRowIndex - 1;
        int firstCol = naturalColumnIndex - 1;
        int lastCol = naturalColumnIndex - 1;
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper
                .createFormulaListConstraint(formulaString);
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, XLS_MAX_ROW, firstCol, lastCol);
        // XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, regions);
        // 数据有效性对象
        // 绑定
        XSSFDataValidation data_validation_list = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, regions);
        data_validation_list.setEmptyCellAllowed(false);
        if (data_validation_list instanceof XSSFDataValidation) {
            data_validation_list.setSuppressDropDownArrow(true);
            data_validation_list.setShowErrorBox(true);
        } else {
            data_validation_list.setSuppressDropDownArrow(false);
        }
        // 设置输入信息提示信息
        data_validation_list.createPromptBox("下拉选择提示", "请使用下拉方式选择合适的值！");
        // 设置输入错误提示信息
        // data_validation_list.createErrorBox("选择错误提示", "你输入的值未在备选列表中，请下拉选择合适的值！");
        return data_validation_list;
    }

    /**
     * 名称数据行列计算表达式
     */
    private static String creatExcelNameList(int order, int size, boolean cascadeFlag) {
        char start = 'A';
        if (cascadeFlag) {
            if (size <= 26) {// 曾经是《=25
                char end = (char) (start + size - 1);
                if (end == '@') {
                    end = 'A';
                }
                return "$" + start + "$" + order + ":$" + end + "$" + order;
            } else {
                char endPrefix = 'A';
                char endSuffix = 'A';
                if ((size - 25) / 26 == 0 || size == 51) {// 26-51之间，包括边界（仅两次字母表计算）
                    if ((size - 25) % 26 == 0) {// 边界值
                        endSuffix = (char) ('A' + 25);
                    } else {
                        endSuffix = (char) ('A' + (size - 25) % 26 - 2);
                    }
                } else {// 51以上
                    if ((size - 25) % 26 == 0 || (size - 25) % 26 == 1) {
                        endSuffix = (char) ('A' + 25);
                        endPrefix = (char) (endPrefix + (size - 25) / 26 - 1);
                    } else {

                        endSuffix = (char) ('A' + (size - 25) % 26 - 2);

                        endPrefix = (char) (endPrefix + (size - 25) / 26);
                    }
                }
                String s = "$" + start + "$" + order + ":$" + endPrefix + endSuffix + "$" + order;
                return s;
            }
        } else {
            if (size <= 26) {
                char end = (char) (start + size - 1);
                return "$" + start + "$" + order + ":$" + end + "$" + order;
            } else {
                char endPrefix = 'A';
                char endSuffix = 'A';
                if (size % 26 == 0) {
                    endSuffix = (char) ('A' + 25);
                    if (size > 52 && size / 26 > 0) {
                        endPrefix = (char) (endPrefix + size / 26 - 2);
                    }
                } else {
                    endSuffix = (char) ('A' + size % 26 - 1);
                    if (size > 52 && size / 26 > 0) {
                        endPrefix = (char) (endPrefix + size / 26 - 1);
                    }
                }
                return "$" + start + "$" + order + ":$" + endPrefix + endSuffix + "$" + order;
            }
        }
        /*
         * char start = 'A'; if (cascadeFlag) { if (size <= 25) { char end = (char) (start + size - 1); String s ="$" + start + "$"
         * + order + ":$" + end + "$" + order; return s; } else { char endPrefix = 'A'; char endSuffix = 'A'; if ((size - 25) / 26
         * == 0 || size == 51) {//26-51之间，包括边界（仅两次字母表计算） if ((size - 25) % 26 == 0) {//边界值 endSuffix = (char) ('A' + 25); } else {
         * endSuffix = (char) ('A' + (size - 25) % 26 - 1); } } else {//51以上 if ((size - 25) % 26 == 0) { endSuffix = (char) ('A' +
         * 25); endPrefix = (char) (endPrefix + (size - 25) / 26 - 1); } else { endSuffix = (char) ('A' + (size - 25) % 26 - 1);
         * endPrefix = (char) (endPrefix + (size - 25) / 26); } } String s ="$" + start + "$" + order + ":$" + endPrefix + endSuffix
         * + "$" + order; return s;
         *
         * } } else { if (size <= 26) { char end = (char) (start + size - 1); String s ="$" + start + "$" + order + ":$" + end + "$"
         * + order; return s; } else { char endPrefix = 'A'; char endSuffix = 'A'; if (size % 26 == 0) { endSuffix = (char) ('A' +
         * 25); if (size > 52 && size / 26 > 0) { endPrefix = (char) (endPrefix + size / 26 - 2); } } else { endSuffix = (char) ('A'
         * + size % 26 - 1); if (size > 52 && size / 26 > 0) { endPrefix = (char) (endPrefix + size / 26 - 1); } } String s = "$" +
         * start + "$" + order + ":$" + endPrefix + endSuffix + "$" + order; return s; } }
         */
    }
}
