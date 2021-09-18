package com.aspire.ums.cmdb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aspire.ums.cmdb.cmic.payload.ExcelData;
import com.google.common.collect.Lists;

@Slf4j
public class POICascadeUtils2 {

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

    /**
     * 合并单元格,需指定开始行/结束行+开始列/结束列.
     *
     * @param workbook
     *            工作薄
     * @param sheet
     *            工作sheet
     * @param mergeRow
     *            合并的行
     * @param mergeCellColumn
     *            合并的列
     * @param startRow
     *            合并开始行
     * @param endRow
     *            合并结束行
     * @param startCol
     *            合并开始列
     * @param endCol
     *            合并结束列
     * @param mergeCellValue
     *            合并Value
     * @return
     */
    public void createCmdbProjectMergeRow(XSSFWorkbook workbook, Sheet sheet, Row mergeRow, int mergeCellColumn, int startRow,
            int endRow, int startCol, int endCol, String mergeCellValue) {
        // 合并单元格操作
        sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startCol, endCol));
        createMergeCell(workbook, mergeRow, mergeCellColumn, mergeCellValue);
    }

    private void createMergeCell(XSSFWorkbook workbook, Row row, int columnNum, String cellValue) {
        Cell cell = row.createCell(columnNum);
        cell.setCellStyle(createMergeCellStyle(workbook));
        cell.setCellValue(cellValue);
    }

    public static CellStyle createMergeCellStyle(XSSFWorkbook workbook) {
        // 设置样式
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
        titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        titleStyle.setBorderBottom(CellStyle.BORDER_THIN); // 设置单无格的边框为粗体
        titleStyle.setBottomBorderColor(IndexedColors.BLACK.index); // 设置单元格的边框颜色．
        titleStyle.setBorderLeft(CellStyle.BORDER_THIN);
        titleStyle.setLeftBorderColor(IndexedColors.BLACK.index);
        titleStyle.setBorderRight(CellStyle.BORDER_THIN);
        titleStyle.setRightBorderColor(IndexedColors.BLACK.index);
        titleStyle.setBorderTop(CellStyle.BORDER_THIN);
        titleStyle.setTopBorderColor(IndexedColors.BLACK.index);
        titleStyle.setAlignment(CellStyle.ALIGN_LEFT);
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        titleStyle.setWrapText(true);// 指定单元格自动换行
        // 设置标题单元格类型

        // 设置字体
        XSSFFont titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 12);
        titleFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        titleFont.setFontName("SimSun");
        titleStyle.setFont(titleFont);
        return titleStyle;
    }

    public static void setCellStyle(Sheet sheet, int rowIndex, int columnIndex, int toRowIndex, int toColumnIndex,
            String cellValue, CellStyle cellStyle) {
        for (int i = rowIndex; i <= toRowIndex; i++) {
            for (int j = columnIndex; j <= toColumnIndex; j++) {
                Row row = sheet.getRow(i);
                Cell cell = null;
                if (null != row) {
                    cell = row.getCell(j);
                    if (null == cell) {
                        cell = row.createCell(j);
                        cell.setCellStyle(cellStyle);
                        XSSFRichTextString textString = new XSSFRichTextString(cellValue);
                        cell.setCellValue(textString.toString());
                    }
                }
            }
        }
    }

    /**
     * 工程交维硬件设备清单表模板-合并单元格.
     *
     * @param
     * @return
     */
    public void createCmdbProjectMergeHeader(XSSFWorkbook wb, String sheetName) {
        Sheet sheet = wb.createSheet(sheetName);
        CellStyle cellStyle = createMergeCellStyle(wb);
        /** ---------------------------第一行 start----------------------- */

        Row mergeRow1 = sheet.createRow(0);
        mergeRow1.setHeightInPoints(200);

        StringBuilder col1 = new StringBuilder();
        col1.append("管理属性字段填写要求：").append("\r\n").append("1、管理IP：必填。填写安装时配置的管理网络接入IP地址。").append("\r\n")
                .append("2、主备关系：必填；例子：主、备1、备2、备…").append("\r\n").append("3、设备状态：必填，字典：上电、下电").append("\r\n")
                .append("4、是否资源池管理：必填，是或否。").append("\r\n").append("5、Console IP：可选。带外管理IP").append("\r\n")
                .append("6、资源计划性：可选。填计划内或计划外").append("\r\n").append("7、管理地址：分布式存储类型设备专用。").append("\r\n")
                .append("8、管理网段：可选。网络设备专用。").append("\r\n").append("9、管理web登录地址：可选。如设备自带管理界面，则需要填写相关的登录数据。").append("\r\n");
        
        StringBuilder col2 = new StringBuilder();
        col2.append("设备位置信息：").append("\r\n").append("1、所属位置：必填，所需位置信息l列表；").append("\r\n").append("2、所在机房：物理设备必填。列表选择。")
                .append("\r\n").append("3、机架号：物理设备必填。填写所在的机柜编号").append("\r\n").append("4、U位：物理设备必填。物理设备高度，占机架多少个U位。")
                .append("\r\n").append("5、机架位置：可选。填写设备安装的U位。").append("\r\n").append("6、资产标签号：硬件设备必填，若未转资则填写未转资").append("\r\n")
                .append("7、项目归属：必填。");
        
        StringBuilder col3 = new StringBuilder();
        col3.append("设备基本信息填写要求：").append("\r\n").append("1、设备分类/设备类型/设备型号：必填。可选内容见列表。").append("\r\n").append("2、设备序列号：物理设备必填。")
                .append("\r\n").append("3、设备规格：可选。建议填写CPU，内存，硬盘等设备关键信息。").append("\r\n").append("4、操作系统：可选。可选内容见列表。")
                .append("\r\n").append("5、主机名：可选。填写操作系统中定义的主机名称。").append("\r\n")
                .append("6、物理机CPU核心数量/物理机内存总量/物理机磁盘总量：物理服务器类记录必填，其余类型设备可选。字段将用于报表统计，请填准确数字。").append("\r\n")
                .append("7、外挂存储/外挂存储厂家/外挂存储类型/外挂存储挂载目录：如主机挂载来外部存储则必填。").append("\r\n").append("8、刀箱管理IP：可选。填写所在刀箱的管理IP。")
                .append("\r\n").append("9、刀箱号：可选。填写刀片所在的上级设备逻辑名").append("\r\n").append("10、槽位号：可选。填写所在刀箱位置。").append("\r\n")
                .append("11、存储角色/存储业务归属：存储类设备必填。内容见列表。").append("\r\n").append("12、物理机MAC1/物理机MAC2：可选。物理机网卡mac地址");

        StringBuilder col4 = new StringBuilder();
        col4.append("设备连接属性填写要求：").append("\r\n").append("与现行交维表格《07项目名称-机房地址-设备信息表.xlsx》中的《物理机与网络端口对应表》中对应字段一致。");

        StringBuilder col5 = new StringBuilder();
        col5.append("网络属性字段填写要求：").append("\r\n").append("1、设备逻辑名：必填。按格式:机架号-机房名称-网络层次-项目名称-网络设备类型+3位阿拉伯数字-厂家型号编写。").append("\r\n")
                .append("2、设备网络层次：网络设备必填。内容见列表。").append("\r\n").append("3、网络区域：必填。内容见列表。").append("\r\n").append("4、业务IP：可选。")
                .append("\r\n").append("5、其它IP：可选。填写设备添加的其它IP地址，多个IP地址用半角逗号隔开。").append("\r\n").append("6、公网IP：可选。填写设备被指派的公网IP地址。")
                .append("\r\n").append("7、所在集群名称/所在集群IP：可选。如设备属于某个集群，填写集群相关信息。");

        StringBuilder col6 = new StringBuilder();
        col6.append("虚拟机属性字段填写要求：").append("\r\n").append("1、宿主机IP：设备类型为虚拟机必填。").append("\r\n")
                .append("2、宿主机名称：设备类型为虚拟机必填。填写宿主机设备的设备逻辑名。").append("\r\n").append("3、宿主机CPU：设备类型为虚拟机必填。填写宿主机的CPU核心数量。")
                .append("\r\n").append("4、宿主机内存：设备类型为虚拟机必填。填写宿主机的安装内存总数。").append("\r\n")
                .append("5、宿主机MAC-1/宿主机MAC-2：可选。填写宿主机网卡mac地址。").append("\r\n").append("6、宿主机操作系统：设备类型为虚拟机必填。");

        StringBuilder col7 = new StringBuilder();
        col7.append("业务属性字段填写要求：").append("\r\n").append("1、业务线：可选。可选内容见列表。").append("\r\n").append("2、独立业务：必填。按设备分配情况填写。内容见列表")
                .append("\r\n").append("3、独立业务子模块：必填。按设备分配情况填写。内容见列表").append("\r\n").append("4、应用类型：服务器类和存储类设备必填。内容见列表")
                .append("\r\n").append("5、应用配置：服务器类和存储类设备必填。内容见列表").append("\r\n").append("6、资源申请人：可选。");

        StringBuilder col8 = new StringBuilder();
        col8.append("成本属性字段填写要求：").append("\r\n").append("1、转资成本。可选。").append("\r\n").append("2、按比例分摊日期。可选。").append("\r\n")
                .append("3、使用年限。可选。").append("\r\n").append("4、设备价格：可选。");

        StringBuilder col9 = new StringBuilder();
        col9.append("维保属性字段填写要求：").append("\r\n").append("1、是否购买维保：可选。").append("\r\n").append("2、出保时间：硬件设备必填。").append("\r\n")
                .append("3、设备厂家：下拉选择，硬件设备必填。").append("\r\n").append("4、维保厂家：可选。").append("\r\n")
                .append("5、维保厂家联系方式：填写维保厂家联系人和电话号码，可选。").append("\r\n").append("6、供应商联系方式：供应商联系人和电话号码，可选。").append("\r\n")
                .append("7、是否购买原厂维保：可选。").append("\r\n").append("8、实际购买维保类型：可选。").append("\r\n").append("9、本期维保起始时间：可选。")
                .append("\r\n").append("10、本期维保结束时间：可选。").append("\r\n").append("11、维保管理人：可选。填名称+电话号码");

        StringBuilder col10 = new StringBuilder();
        col10.append("监控属性字段填写要求：").append("\r\n")
                .append("1、filebeat：必填。服务器类（含虚拟机）确定需要安装agent的，填“是”则需要进行验收前验证。其它设备不需要或者不能安装agent的，填“否”将不进行验收前验证操作。。").append("\r\n")
                .append("2、自动化：必填。服务器类（含虚拟机）、网络设备确定需要自动化纳管的，填“是”则需要进行验收前验证。其它设备不需要或者不能自动化纳管的，填“否”将不进行验收前验证操作。").append("\r\n")
                .append("3、日志采集目录：如果filebeat字段为是，则必填。目前仅支持填写一个目录，请填默认目录；").append("\r\n").append("4、自动化Proxy IP：如果网络设备需自动化纳管，则必填；");

        StringBuilder col11 = new StringBuilder();
        col11.append("其它属性字段填写要求：").append("\r\n").append("1、备注：可选。");

        int startRow = 0;
        int[] mgrCellRange = { 0, 8 };// 管理属性合并列
        int[] locationCellRange = { 9, 15 };// 设备位置合并列
        int[] baseInfoCellRange = { 16, 40 };// 设备基本信息合并列
        int[] linkInfoCellRange = { 41, 56 };// 设备连接属性合并列
        int[] networkCellRange = { 57, 65 };// 网络属性合并列
        int[] vmCellRange = { 66, 72 };// 虚拟机属性合并列
        int[] businessCellRange = { 73, 78 };// 业务属性合并列
        int[] costCellRange = { 79, 82 };// 成本属性合并列
        int[] maintenceCellRange = { 83, 93 };// 维保属性合并列
        int[] monitorCellRange = { 94, 98 };// 监控属性合并列
        int[] remarkCellRange = { 99 };// 其他属性合并列
        setCellStyle(sheet, startRow, mgrCellRange[0], startRow, mgrCellRange[1], col1.toString(), cellStyle);
        setCellStyle(sheet, startRow, locationCellRange[0], startRow, locationCellRange[1], col2.toString(), cellStyle);
        setCellStyle(sheet, startRow, baseInfoCellRange[0], startRow, baseInfoCellRange[1], col3.toString(), cellStyle);
        setCellStyle(sheet, startRow, linkInfoCellRange[0], startRow, linkInfoCellRange[1], col4.toString(), cellStyle);
        setCellStyle(sheet, startRow, networkCellRange[0], startRow, networkCellRange[1], col5.toString(), cellStyle);
        setCellStyle(sheet, startRow, vmCellRange[0], startRow, vmCellRange[1], col6.toString(), cellStyle);
        setCellStyle(sheet, startRow, businessCellRange[0], startRow, businessCellRange[1], col7.toString(), cellStyle);
        setCellStyle(sheet, startRow, costCellRange[0], startRow, costCellRange[1], col8.toString(), cellStyle);
        setCellStyle(sheet, startRow, maintenceCellRange[0], startRow, maintenceCellRange[1], col9.toString(), cellStyle);
        setCellStyle(sheet, startRow, monitorCellRange[0], startRow, monitorCellRange[1], col10.toString(), cellStyle);
        setCellStyle(sheet, startRow, remarkCellRange[0], startRow, remarkCellRange[0], col11.toString(), cellStyle);
        startRow++;
        /** ---------------------------第一行 end----------------------- */

        /** ---------------------------第二行 start----------------------- */
        Row mergeRow2 = sheet.createRow(startRow);

        setCellStyle(sheet, startRow, mgrCellRange[0], startRow, mgrCellRange[1], "管理属性", cellStyle);
        setCellStyle(sheet, startRow, locationCellRange[0], startRow, locationCellRange[1], "资产属性", cellStyle);
        setCellStyle(sheet, startRow, baseInfoCellRange[0], startRow, baseInfoCellRange[1], "设备属性", cellStyle);
        setCellStyle(sheet, startRow, linkInfoCellRange[0], startRow, linkInfoCellRange[1], "端口属性", cellStyle);
        setCellStyle(sheet, startRow, networkCellRange[0], startRow, networkCellRange[1], "网络属性", cellStyle);
        setCellStyle(sheet, startRow, vmCellRange[0], startRow, vmCellRange[1], "虚拟机属性", cellStyle);
        setCellStyle(sheet, startRow, businessCellRange[0], startRow, businessCellRange[1], "业务属性", cellStyle);
        setCellStyle(sheet, startRow, costCellRange[0], startRow, costCellRange[1], "成本属性", cellStyle);
        setCellStyle(sheet, startRow, maintenceCellRange[0], startRow, maintenceCellRange[1], "维保属性", cellStyle);
        setCellStyle(sheet, startRow, monitorCellRange[0], startRow, monitorCellRange[1], "监控属性", cellStyle);
        setCellStyle(sheet, startRow, remarkCellRange[0], startRow, remarkCellRange[0], "其他属性", cellStyle);

        startRow = 0;
        CellRangeAddress region1 = new CellRangeAddress(startRow, startRow, mgrCellRange[0], mgrCellRange[1]);
        CellRangeAddress region2 = new CellRangeAddress(startRow, startRow, locationCellRange[0], locationCellRange[1]);
        CellRangeAddress region3 = new CellRangeAddress(startRow, startRow, baseInfoCellRange[0], baseInfoCellRange[1]);
        CellRangeAddress region4 = new CellRangeAddress(startRow, startRow, linkInfoCellRange[0], linkInfoCellRange[1]);
        CellRangeAddress region5 = new CellRangeAddress(startRow, startRow, networkCellRange[0], networkCellRange[1]);
        CellRangeAddress region6 = new CellRangeAddress(startRow, startRow, vmCellRange[0], vmCellRange[1]);
        CellRangeAddress region7 = new CellRangeAddress(startRow, startRow, businessCellRange[0], businessCellRange[1]);
        CellRangeAddress region8 = new CellRangeAddress(startRow, startRow, costCellRange[0], costCellRange[1]);
        CellRangeAddress region9 = new CellRangeAddress(startRow, startRow, maintenceCellRange[0], maintenceCellRange[1]);
        CellRangeAddress region10 = new CellRangeAddress(startRow, startRow, monitorCellRange[0], monitorCellRange[1]);
        CellRangeAddress region11 = new CellRangeAddress(startRow, startRow, remarkCellRange[0], remarkCellRange[0]);
        startRow++;

        CellRangeAddress region2_1 = new CellRangeAddress(startRow, startRow, mgrCellRange[0], mgrCellRange[1]);
        CellRangeAddress region2_2 = new CellRangeAddress(startRow, startRow, locationCellRange[0], locationCellRange[1]);
        CellRangeAddress region2_3 = new CellRangeAddress(startRow, startRow, baseInfoCellRange[0], baseInfoCellRange[1]);
        CellRangeAddress region2_4 = new CellRangeAddress(startRow, startRow, linkInfoCellRange[0], linkInfoCellRange[1]);
        CellRangeAddress region2_5 = new CellRangeAddress(startRow, startRow, networkCellRange[0], networkCellRange[1]);
        CellRangeAddress region2_6 = new CellRangeAddress(startRow, startRow, vmCellRange[0], vmCellRange[1]);
        CellRangeAddress region2_7 = new CellRangeAddress(startRow, startRow, businessCellRange[0], businessCellRange[1]);
        CellRangeAddress region2_8 = new CellRangeAddress(startRow, startRow, costCellRange[0], costCellRange[1]);
        CellRangeAddress region2_9 = new CellRangeAddress(startRow, startRow, maintenceCellRange[0], maintenceCellRange[1]);
        CellRangeAddress region2_10 = new CellRangeAddress(startRow, startRow, monitorCellRange[0], monitorCellRange[1]);
        CellRangeAddress region2_11 = new CellRangeAddress(startRow, startRow, remarkCellRange[0], remarkCellRange[0]);

        sheet.addMergedRegion(region1);
        sheet.addMergedRegion(region2);
        sheet.addMergedRegion(region3);
        sheet.addMergedRegion(region4);
        sheet.addMergedRegion(region5);
        sheet.addMergedRegion(region6);
        sheet.addMergedRegion(region7);
        sheet.addMergedRegion(region8);
        sheet.addMergedRegion(region9);
        sheet.addMergedRegion(region10);
        sheet.addMergedRegion(region11);

        sheet.addMergedRegion(region2_1);
        sheet.addMergedRegion(region2_2);
        sheet.addMergedRegion(region2_3);
        sheet.addMergedRegion(region2_4);
        sheet.addMergedRegion(region2_5);
        sheet.addMergedRegion(region2_6);
        sheet.addMergedRegion(region2_7);
        sheet.addMergedRegion(region2_8);
        sheet.addMergedRegion(region2_9);
        sheet.addMergedRegion(region2_10);
        sheet.addMergedRegion(region2_11);
        // sheet.addMergedRegion(region2_10);

        sheet.createFreezePane(0, 3, 0, 3);
        /** ---------------------------第二行 end----------------------- */

        createCmdbProjectHeader(wb, sheet, 2);
    }

    /**
     * 工程交维--生成标题栏
     *
     * @param
     * @return
     */
    public static void createCmdbProjectHeader(XSSFWorkbook wb, Sheet sheet, int rowNum) {

        // 把字体应用到当前的样式
        // style.setFont(font);

        // 产生表格标题行

        Row row = sheet.createRow(rowNum);
        String[] testva = new String[headerLists.length];
        // 设置不同颜色(必填为红色，有前置条件必填字段标橙色)
        int[] red = { 0, 1, 2, 3, 9, 14, 15, 16, 17, 18, 20, 57, 59, 74, 75, 94, 95, 97 };
        int[] orange = { 10, 11, 12, 19, 23, 24, 25, 37, 38, 66, 67, 68, 69, 72, 76, 77, 84, 85, 96, 98 };
        for (int i = 0; i < headerLists.length; i++) {

            // 生成一个样式
            XSSFCellStyle style = wb.createCellStyle();
            
            // 设置样式
            // style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
            style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
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
            testva[i] = "888888888888888888";// 表格宽度
            Cell cell = row.createCell(i);
            
            // 生成一个字体

            XSSFFont font = wb.createFont();
            // font.setColor(HSSFColor.BLACK.index);
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
            font.setFontName("宋体");
            font.setFontHeight((short) 230);

            boolean redFlag = false;
            boolean orangeFlag = false;
            for (int c : red) {
                if (c == i) {
                    redFlag = true;
                    break;
                }
            }
            for (int c : orange) {
                if (c == i) {
                    orangeFlag = true;
                    break;
                }
            }
            if (redFlag) {
                font.setColor(IndexedColors.RED.index);
            } else if (orangeFlag) {
                font.setColor(IndexedColors.ORANGE.index);
            } else {
                font.setColor(IndexedColors.BLACK.index);
            }
            style.setFont(font);
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

    public void generateExcel(XSSFWorkbook wb, List<ExcelData> dataList, String title) {
        List<String> headerList = dataList.stream().map(e -> e.getFieldName()).collect(Collectors.toList());
        headerLists = headerList.toArray(new String[headerList.size()]);
        createExcelMo(wb, title);
        initDropDownList(wb, dataList);
        setDataValidation(wb, dataList);
    }

    public static void initDropDownList(Workbook workbook, List<ExcelData> dataList) {
        Sheet hideInfoSheet = workbook.createSheet(EXCEL_HIDE_SHEET_NAME);// 隐藏一些信息
        // 在隐藏页设置选择信息
        // i需要我们手动控制
        int L = 0;
        ArrayList<String> arrayList = new ArrayList<>();// 把所有标题
        List<String> namesList = Lists.newArrayList();// Excel名称管理器 不能重复
        for (int i = 0; i < dataList.size(); i++) {
            ExcelData excelData = dataList.get(i);
            String controlCode = excelData.getFieldType();
            String fieldCode = excelData.getFieldCode();
            String fieldName = excelData.getFieldName();
            List<Object> valueList = excelData.getFieldValueList();
            if ("listSel".equals(controlCode)) {
                // 下拉
                Row row = hideInfoSheet.createRow(i);
                List<String> stringList = valueList.stream().map(e -> e.toString()).collect(Collectors.toList());
                creatRow(row, stringList.toArray(new String[stringList.size()]));
                creatExcelNameList(workbook, fieldCode, namesList, i + 1, valueList.size(), false);
            } else if ("cascader".equals(controlCode)) {
                // 级联下拉
                Row row = hideInfoSheet.createRow(i);
                List<String> stringList = valueList.stream().map(e -> e.toString()).collect(Collectors.toList());
                creatRow(row, stringList.toArray(new String[stringList.size()]));
                creatExcelNameList(workbook, fieldCode, namesList, i + 1, valueList.size(), true);
            } else {
                // 无下拉框的列
                Row row = hideInfoSheet.createRow(i);
                List<String> stringList = valueList.stream().map(e -> e.toString()).collect(Collectors.toList());
                creatRow(row, stringList.toArray(new String[stringList.size()]));
            }
        }

        // 设置隐藏页标志
        workbook.setSheetHidden(workbook.getSheetIndex(EXCEL_HIDE_SHEET_NAME), false);
    }

    /**
     * 生成验证--工程交维.
     *
     * @param
     * @return
     */
    public static void setDataValidation(Workbook wb, List<ExcelData> dataList) {
        int sheetIndex = wb.getNumberOfSheets();
        if (sheetIndex > 0) {
            for (int i = 0; i < sheetIndex; i++) {
                Sheet sheet = wb.getSheetAt(i);
                if (!EXCEL_HIDE_SHEET_NAME.equals(sheet.getSheetName())) {
                    int startRow = 2;
                    for (int j = 0; j < dataList.size(); j++) {
                        ExcelData excelData = dataList.get(i);
                        String controlCode = excelData.getFieldType();
                        String fieldCode = excelData.getFieldCode();
                        String fieldName = excelData.getFieldName();
                        List<Object> valueList = excelData.getFieldValueList();
                        if ("listSel".equals(controlCode)) {
                            // 下拉
                            DataValidation validation = getDataValidationCustomFormula(sheet, fieldCode, startRow,
                                    excelData.getColIndex());
                            sheet.addValidationData(validation);
                        } else if ("cascader".equals(controlCode)) {
                            // 级联下拉
                            ExcelData parent = excelData.getParent();
                            String letter = numberToLetter(parent.getColIndex()) + startRow;
                            DataValidation validation = getDataValidationCustomFormula(sheet, "INDIRECT($" + letter + ")",
                                    startRow, excelData.getColIndex());
                            sheet.addValidationData(validation);
                        }
                    }
                }
            }
        }
    }

    /**
     * 将以字母表示的Excel列数转换成数字表示
     *
     * @author jiangxuwen
     * @param letter
     *            以字母表示的列数，不能为空且只允许包含字母字符
     * @return 返回转换的数字，转换失败返回-1
     */
    public static int letterToNumber(String letter) {
        // 检查字符串是否为空
        if (letter == null || letter.isEmpty()) {
            return -1;
        }
        String upperLetter = letter.toUpperCase(); // 转为大写字符串
        if (!upperLetter.matches("[A-Z]+")) { // 检查是否符合，不能包含非字母字符
            return -1;
        }
        long num = 0; // 存放结果数值
        long base = 1;
        // 从字符串尾部开始向头部转换
        for (int i = upperLetter.length() - 1; i >= 0; i--) {
            char ch = upperLetter.charAt(i);
            num += (ch - 'A' + 1) * base;
            base *= 26;
            if (num > Integer.MAX_VALUE) { // 防止内存溢出
                return -1;
            }
        }
        return (int) num;
    }

    public static void main(String[] args) {
        System.out.println(letterToNumber("A"));
        System.out.println(letterToNumber("b"));
        System.out.println(letterToNumber("Z"));
        System.out.println(letterToNumber("aB"));
        System.out.println(letterToNumber("Q"));
        System.out.println(letterToNumber("R"));
        System.out.println(letterToNumber("BW"));

        System.out.println(numberToLetter(1));
        System.out.println(numberToLetter(2));
        System.out.println(numberToLetter(17));
        System.out.println(numberToLetter(26));
        System.out.println(numberToLetter(28));
        System.out.println(numberToLetter(731));
    }

    /**
     * 将数字转换成以字母表示的Excel列数
     *
     * @author jiangxuwen
     * @param num
     *            表示列数的数字
     * @return 返回转换的字母字符串，转换失败返回null
     */
    public static String numberToLetter(int num) {
        if (num <= 0) { // 检测列数是否正确
            return null;
        }
        StringBuffer letter = new StringBuffer();
        do {
            --num;
            int mod = num % 26; // 取余
            letter.append((char) (mod + 'A')); // 组装字符串
            num = (num - mod) / 26; // 计算剩下值
        } while (num > 0);
        return letter.reverse().toString(); // 返回反转后的字符串
    }

    public void utilss(XSSFWorkbook wb, List<List<Object>> results, String[] headerList, String title) {
        result = results;
        headerLists = headerList;
        // 创建标题栏
        createExcelMo(wb, title);
        // 创建隐藏的Sheet页
        creatExcelHidePage(wb);
        // 数据验证
        setDataValidation(wb);
    }

    // result:{BUSINESS_LEVEL1=测试中心, BUSINESS_LEVEL2=[云测试平台, 众测平台]}
    // headerList:第一行标题
    // title:资产信息表模板
    public void cmdbUtilss(XSSFWorkbook wb, List<List<Object>> results, String[] headerList, String title) {
        result = results;
        headerLists = headerList;
        // 创建标题栏
        createExcelMo(wb, title);
        // 重要方法！！！创建隐藏的Sheet页
        creatExcelHidePageForCmdb(wb);
        // 重要方法！！！ 数据验证 下拉,级联
        setDataValidationForCmdb(wb);

    }

    /**
     * 工程交维模板(硬件明细表).
     *
     * @param
     * @return
     */
    public void cmdbProjectUtilss(XSSFWorkbook wb, List<List<Object>> results, String[] headerList, String title) {
        result = results;
        headerLists = headerList;
        // 创建标题栏
        // createExcelMo(wb, title);
        createCmdbProjectMergeHeader(wb, title);
        // 重要方法！！！创建隐藏的Sheet页
        creatExcelHidePageForCmdbProject(wb);
        // 重要方法！！！ 数据验证 下拉,级联
        setDataValidationForCmdbProject(wb);

    }

    /**
     * 工程交维模板(IP地址规划表).
     *
     * @param
     * @return
     */
    public void cmdbIPVlanUtilss(XSSFWorkbook wb, List<List<Object>> results1, String[] headerList1, String title1,
            List<List<Object>> results2, String[] headerList2, String title2) {
        result = results1;
        headerLists = headerList1;
        // 创建标题栏
        createExcelMo(wb, title1);
        // 重要方法！！！创建隐藏的Sheet页
        creatExcelHidePageForIP(wb);
        // 重要方法！！！ 数据验证 下拉,级联
        // setDataValidationForIPVLAN(wb);

        result = results2;
        headerLists = headerList2;
        // 创建标题栏
        createExcelMo(wb, title2);
        // 重要方法！！！创建隐藏的Sheet页
        creatExcelHidePageForIPVLAN(wb);
        // 重要方法！！！ 数据验证 下拉,级联
        setDataValidationForIPVLAN(wb);
    }

    // title:第三方维保硬件维保服务需求调查表模板
    public void maintenanceUtilss(Workbook wb, List<List<Object>> results, String[] headerList, String title, String[] keyList,
            List<Map> dataList) {
        result = results;
        headerLists = headerList;
        // 创建标题栏
        createExcelMaintenance(wb, title, keyList, dataList);
        // 重要方法！！！创建隐藏的Sheet页
        creatExcelHidePageForMaintenance(wb);
        // 重要方法！！！ 数据验证 下拉,级联
        setDataValidationForMaintenance(wb);

    }

    // title:cmdb存储信息模板
    public void cmdbStorageUtilss(Workbook wb, List<List<Object>> results, String[] headerList, String title, String[] keyList,
            List<Map> dataList) {
        result = results;
        headerLists = headerList;
        // 创建标题栏
        createExcelMaintenance(wb, title, keyList, dataList);
        // 重要方法！！！创建隐藏的Sheet页
        creatExcelHidePageForCmdbStorage(wb);
        // 重要方法！！！ 数据验证 下拉,级联
        setDataValidationForCmdbStorage(wb);

    }

    public static void createExcelMaintenance(Workbook wb, String title, String[] keyList, List<Map> dataList) {
        Sheet sheet = wb.createSheet(title);
        // 生成一个样式
        CellStyle style = wb.createCellStyle();

        // 设置样式
        // style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
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

        Font font = wb.createFont();
        // font.setColor(HSSFColor.BLACK.index);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 230);

        // 把字体应用到当前的样式

        style.setFont(font);

        // 指定当单元格内容显示不下时自动换行

        CellStyle style1 = wb.createCellStyle();
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单无格的边框为粗体
        style1.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style1.setLeftBorderColor(HSSFColor.BLACK.index);
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style1.setRightBorderColor(HSSFColor.BLACK.index);
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style1.setTopBorderColor(HSSFColor.BLACK.index);
        // style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
        style1.setWrapText(true);// 指定单元格自动换行

        // 产生表格标题行

        Row row = sheet.createRow(0);
        String[] testva = new String[headerLists.length];
        for (int i = 0; i < headerLists.length; i++) {
            testva[i] = "888888888888888888";// 表格宽度
            Cell cell = row.createCell(i);

            cell.setCellStyle(style);

            HSSFRichTextString text = new HSSFRichTextString(headerLists[i]);

            cell.setCellValue(text.toString());
        }
        // 遍历集合数据，产生数据行

        if (result.size() != 0) {
            int index = 1;
            for (Map map : dataList) {
                row = sheet.createRow(index);
                int cellIndex = 0;
                for (String key : keyList) {
                    if (null == testva[cellIndex]) {
                        testva[cellIndex] = "0";
                    }

                    if (null != map && null != map.get(key)) {
                        Cell cell = row.createCell(cellIndex);
                        String va = map.get(key).toString();
                        if (va.getBytes().length >= testva[cellIndex].getBytes().length) {
                            if (cellIndex != 0 && va.getBytes().length > testva[cellIndex - 1].getBytes().length) {
                                testva[cellIndex] = va;
                            }
                        }
                        if (!org.apache.commons.lang3.StringUtils.isEmpty(va) && isNum(va)) {
                            // 是数字当作double处理
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
            cell.setCellValue("无符合条件记录....");
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

    public void creatExcelHidePageForMaintenance(Workbook workbook) {
        Sheet hideInfoSheet = workbook.createSheet(EXCEL_HIDE_SHEET_NAME);// 设置隐藏 页的标题
        // 在隐藏页设置选择信息
        // 有些i需要我们手动控制
        int L = 0;
        ArrayList<String> arrayList = new ArrayList<>();// 把所有标题
        for (int i = 0; i < result.size(); i++) {

            if (i == 0 || ((i >= 8 && i <= 20) && (i != 10 && i != 12 && i != 19 && i != 13))) { // 此判断用于 无下拉框的列
                Row row = hideInfoSheet.createRow(L); // 创建一行
                String[] rowS = (String[]) result.get(i).get(0);// 把预先数据转成字符串数组
                creatRow(row, rowS);// 把字符串数组，遍历，设置列的内容
                String rowString = (String) result.get(i).get(1);// 获取标题
                arrayList.add(rowString);
                hideString = arrayList;
                L++;
            } else if (i == 3) {// 联动的开头者 indirect()
                Row row = hideInfoSheet.createRow(L);
                tempRow = (String[]) result.get(i).get(0);
                creatRow(row, tempRow);
                String rowString = (String) result.get(i).get(1);
                arrayList.add(rowString);
                hideString = arrayList;
                creatExcelNameList(workbook, rowString, L + 1, tempRow.length, false);
                L++;
            } else if (i == 4) {// 联动关联者

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
            } else if (i == 1) {
                tempSize = L;
                Row row = hideInfoSheet.createRow(L);
                String[] rowS = (String[]) result.get(i).get(0);
                creatRow(row, rowS);
                String rowString = (String) result.get(i).get(1);
                arrayList.add(rowString);
                hideString = arrayList;
                creatExcelNameList(workbook, rowString, L + 1, rowS.length, false);
                L++;
            } else if (i == 5) {
                Row row = hideInfoSheet.createRow(L);
                tempRow = (String[]) result.get(i).get(0);
                creatRow(row, tempRow);
                String rowString = (String) result.get(i).get(1);
                arrayList.add(rowString);
                hideString = arrayList;
                creatExcelNameList(workbook, rowString, L + 1, tempRow.length, false);
                L++;

            } else if (i == 6) {
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
            } else if (i == 7) {
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

            } else if (i == 3 || (i >= 10 || i <= 13) || i == 19) {
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

    public void creatExcelHidePageForCmdbStorage(Workbook workbook) {
        Sheet hideInfoSheet = workbook.createSheet(EXCEL_HIDE_SHEET_NAME);// 设置隐藏 页的标题
        // 在隐藏页设置选择信息

        // 有些i需要我们手动控制
        // 用于记录行

        int L = 0;
        ArrayList<String> arrayList = new ArrayList<>();// 把所有标题
        for (int i = 0; i < result.size(); i++) {

            if (i == 0 || i == 7 || i == 8) { // 此判断用于 无下拉框的列
                Row row = hideInfoSheet.createRow(L); // 创建一行
                String[] rowS = (String[]) result.get(i).get(0);// 把预先数据转成字符串数组
                creatRow(row, rowS);// 把字符串数组，遍历，设置列的内容
                String rowString = (String) result.get(i).get(1);// 获取标题
                arrayList.add(rowString);
                hideString = arrayList;
                L++;
            } else if (i == 5) {// 联动的开头者 indirect()
                Row row = hideInfoSheet.createRow(L);
                tempRow = (String[]) result.get(i).get(0);
                creatRow(row, tempRow);
                String rowString = (String) result.get(i).get(1);
                arrayList.add(rowString);
                hideString = arrayList;
                creatExcelNameList(workbook, rowString, L + 1, tempRow.length, false);
                L++;
            } else if (i == 6) {// 联动关联者

                int k = 0;
                List<Map<String, Object>> business = (List<Map<String, Object>>) result.get(i).get(0);
                for (Map<String, Object> map : business) {
                    ArrayList<String> business2List = (ArrayList<String>) map.get("storageType2");
                    String[] business2 = (String[]) business2List.toArray(new String[0]);
                    Row row = hideInfoSheet.createRow(L);
                    creatRow(row, business2);
                    for (int j = k; j < tempRow.length; j++) {
                        String rowName = tempRow[j];
                        if (rowName.matches("[0-9]+.*")) {
                            rowName = "temp" + rowName;
                        }
                        arrayList.add("");
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
            } else if (i == 3) {// 联动的开头者 indirect()
                Row row = hideInfoSheet.createRow(L);
                tempRow = (String[]) result.get(i).get(0);
                creatRow(row, tempRow);
                String rowString = (String) result.get(i).get(1);
                arrayList.add(rowString);
                hideString = arrayList;
                creatExcelNameList(workbook, rowString, L + 1, tempRow.length, false);
                L++;
            } else if (i == 4) {// 联动关联者

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
            } else if (i == 1 || i == 2) {
                tempSize = L;
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

    public void setDataValidationForMaintenance(Workbook wb) {
        int sheetIndex = wb.getNumberOfSheets();
        if (sheetIndex > 0) {
            for (int i = 0; i < sheetIndex; i++) {
                Sheet sheet = wb.getSheetAt(i);
                if (!EXCEL_HIDE_SHEET_NAME.equals(sheet.getSheetName())) {

                    // 省份选项添加验证数据
                    for (int a = 2; a < 5002; a++) {

                        DataValidation business1Validation = getDataValidationByFormula(hideString.get(3), a, 4);
                        sheet.addValidationData(business1Validation);
                        // "INDIRECT($D$" + a + ")" 返回一级下拉框的excel的单元格内容 例如 D2
                        DataValidation business2Validation = getDataValidationByFormula("INDIRECT($D$" + a + ")", a, 5);
                        sheet.addValidationData(business2Validation);

                        DataValidation idcValidation = getDataValidationByFormula(hideString.get(tempSize), a, 2);
                        sheet.addValidationData(idcValidation);

                        DataValidation deviceClassValidation = getDataValidationByFormula(hideString.get(tempSize + 72), a, 6);
                        sheet.addValidationData(deviceClassValidation);
                        DataValidation deviceTypeValidation = getDataValidationByFormula("INDIRECT($F$" + a + ")", a, 7);
                        sheet.addValidationData(deviceTypeValidation);
                        DataValidation deviceModelValidation = getDataValidationByFormula("INDIRECT($G$" + a + ")", a, 8);
                        sheet.addValidationData(deviceModelValidation);

                        /*
                         * DataValidation hostBackupValidation = getDataValidationByFormula("host_backup", a, 3);
                         * sheet.addValidationData(hostBackupValidation);
                         */
                        DataValidation maintencePurchaseFlagValidation = getDataValidationByFormula("maintence_purchase_flag", a,
                                11);
                        sheet.addValidationData(maintencePurchaseFlagValidation);
                        DataValidation venderMaintenceFlagValidation = getDataValidationByFormula("vender_maintence_flag", a, 13);
                        sheet.addValidationData(venderMaintenceFlagValidation);
                        DataValidation maintencePurchaseDescValidation = getDataValidationByFormula("maintence_purchase_desc", a,
                                14);
                        sheet.addValidationData(maintencePurchaseDescValidation);

                        DataValidation maintencePurchaseTypeValidation = getDataValidationByFormula("maintence_purchase_type", a,
                                20);
                        sheet.addValidationData(maintencePurchaseTypeValidation);

                    }
                }
            }
        }

    }

    /**
     * 设置模板文件的横向表头单元格的样式
     *
     * @param wb
     * @return
     */
    private static CellStyle getTitleStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        // 对齐方式设置
        style.setAlignment(CellStyle.ALIGN_CENTER);
        // 边框颜色和宽度设置
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        // 设置背景颜色
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        // 粗体字设置
        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        return style;
    }

    public void setDataValidationForCmdbStorage(Workbook wb) {
        int sheetIndex = wb.getNumberOfSheets();
        if (sheetIndex > 0) {
            for (int i = 0; i < sheetIndex; i++) {
                Sheet sheet = wb.getSheetAt(i);
                if (!EXCEL_HIDE_SHEET_NAME.equals(sheet.getSheetName())) {

                    // 省份选项添加验证数据
                    for (int a = 2; a < 3002; a++) {
                        DataValidation idcValidation = getDataValidationByFormula(hideString.get(tempSize - 1), 2);
                        sheet.addValidationData(idcValidation);
                        DataValidation hostBackupValidation = getDataValidationByFormula("hostBackup", 3);
                        sheet.addValidationData(hostBackupValidation);

                        DataValidation business1Validation = getDataValidationByFormula(hideString.get(3), a, 4);
                        sheet.addValidationData(business1Validation);
                        // "INDIRECT($D$" + a + ")" 返回一级下拉框的excel的单元格内容 例如 D2
                        DataValidation business2Validation = getDataValidationByFormula("INDIRECT($D$" + a + ")", a, 5);
                        sheet.addValidationData(business2Validation);

                        DataValidation storageTypeValidation = getDataValidationByFormula("storageType", a, 6);
                        sheet.addValidationData(storageTypeValidation);
                        // "INDIRECT($D$" + a + ")" 返回一级下拉框的excel的单元格内容 例如 D2 ("INDIRECT($B1)", 3);
                        DataValidation factoryNameValidation = getDataValidationByFormula("INDIRECT($F$" + a + ")", a, 7);
                        sheet.addValidationData(factoryNameValidation);

                        /*
                         * DataValidation storageTypeValidation = getDataValidationByFormula("storageType", 4);
                         * sheet.addValidationData(storageTypeValidation); DataValidation factoryNameValidation =
                         * getDataValidationByFormula("factoryName", 5); sheet.addValidationData(factoryNameValidation);
                         */

                    }
                }
            }
        }
    }

    /**
     * 设置模板文件的横向表头单元格的样式
     *
     * @param wb
     * @return
     */
    public static void creatExcelHidePage(Workbook workbook) {
        Sheet hideInfoSheet = workbook.createSheet(EXCEL_HIDE_SHEET_NAME);// 隐藏一些信息
        // 在隐藏页设置选择信息

        // i需要我们手动控制
        int L = 0;
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            if (i != 0 && i != 1 && i != 5) { // 此判断用于排除联动，但不排除有下拉数据和无数据
                Row row = hideInfoSheet.createRow(L);
                String[] rowS = (String[]) result.get(i).get(0);
                creatRow(row, rowS);
                String rowString = (String) result.get(i).get(1);
                arrayList.add(rowString);
                hideString = arrayList;
                L++;
            } else {
                Row row = hideInfoSheet.createRow(L);
                String[] rowS = (String[]) result.get(i).get(0);
                creatRow(row, rowS);
                String rowString = (String) result.get(i).get(1);
                arrayList.add(rowString);
                hideString = arrayList;
                creatExcelNameList(workbook, rowString, L + 1, rowS.length, true);
                L++;
            }

        }

        // 设置隐藏页标志
        workbook.setSheetHidden(workbook.getSheetIndex(EXCEL_HIDE_SHEET_NAME), false);
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

    /**
     * 工程交维-设置隐藏sheet
     *
     * @param
     * @return
     */
    public static void creatExcelHidePageForCmdbProject(Workbook workbook) {
        Sheet hideInfoSheet = workbook.createSheet(EXCEL_HIDE_SHEET_NAME);// 设置隐藏 页的标题
        // 在隐藏页设置选择信息
        // 有些i需要我们手动控制
        int L = 0;
        ArrayList<String> arrayList = new ArrayList<>();// 把所有标题
        List<String> namesList = Lists.newArrayList();// Excel名称管理器 不能重复
        for (int i = 0; i < result.size(); i++) {
            log.debug("i=={}", i);
            if (i == 0 || (i <= 8 && i >= 4) || (i >= 11 && i <= 14) || i == 19 || i == 20 || (i >= 22 && i <= 26)
                    || (i >= 29 && i <= 30) || (i >= 33 && i <= 36) || (i >= 39 && i <= 57) || (i >= 60 && i <= 72)
                    || (i >= 78 && i <= 82) || i == 84 || (i >= 86 && i <= 88) || (i >= 91 && i <= 93) || i == 96
                    || (i >= 98 && i <= 99)) { // 此判断用于
                // 无下拉框的列
                Row row = hideInfoSheet.createRow(L); // 创建一行
                String[] rowS = (String[]) result.get(i).get(0);// 把预先数据转成字符串数组
                creatRow(row, rowS);// 把字符串数组，遍历，设置列的内容
                String rowString = (String) result.get(i).get(1);// 获取标题
                log.debug("rowString==非下拉==" + rowString);
                arrayList.add(rowString);
                hideString = arrayList;
                L++;
            } else if (i == 73) { // 业务线
                Row row = hideInfoSheet.createRow(L);
                tempRow = (String[]) result.get(i).get(0);
                creatRow(row, tempRow);
                String rowString = (String) result.get(i).get(1);
                log.debug("rowString==业务线==" + rowString);
                arrayList.add(rowString);
                hideString = arrayList;
                creatExcelNameList(workbook, rowString, namesList, L + 1, tempRow.length, false);
                L++;
            } else if (i == 74) { // 一级业务线
                Row row = hideInfoSheet.createRow(L);
                tempRow = (String[]) result.get(i).get(0);
                creatRow(row, tempRow);
                String rowString = (String) result.get(i).get(1);
                log.debug("rowString==一级业务线==" + rowString);
                arrayList.add(rowString);
                hideString = arrayList;
                creatExcelNameList(workbook, rowString, namesList, L + 1, tempRow.length, false);
                L++;
            } else if (i == 75) {// 二级业务线

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
                        creatExcelNameList(workbook, rowName, namesList, L + 1, business2.length, true);
                        L++;
                        k++;
                        break;
                    }

                }
                // 把临时数据置空
                tempRow = null;
            } else if (i == 16) {// 设备分类
                Row row = hideInfoSheet.createRow(L);
                tempRow = (String[]) result.get(i).get(0);
                creatRow(row, tempRow);
                String rowString = (String) result.get(i).get(1);
                log.debug("rowString==设备分类==" + rowString);
                arrayList.add(rowString);
                hideString = arrayList;
                creatExcelNameList(workbook, rowString, namesList, L + 1, tempRow.length, false);
                L++;

            } else if (i == 17) {// 设备类型
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
                        creatExcelNameList(workbook, rowName, namesList, L + 1, devices.length, true);
                        L++;
                        k++;
                        break;
                    }

                }
                // 把临时数据置空
                tempRow = null;
            } else if (i == 18) {// 设备型号
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
                        creatExcelNameList(workbook, rowName, namesList, L + 1, models.length, true); // 一个名称对应一个下拉框数据
                        L++;
                    }
                }

            } else {
                tempSize1 = L;
                Row row = hideInfoSheet.createRow(L);
                String[] rowS = (String[]) result.get(i).get(0);
                creatRow(row, rowS);
                String rowString = (String) result.get(i).get(1);
                log.debug("rowString==其他下拉==" + rowString);
                arrayList.add(rowString);
                hideString = arrayList;
                creatExcelNameList(workbook, rowString, namesList, L + 1, rowS.length, false);
                L++;
            }

        }

        // 设置隐藏页标志
        workbook.setSheetHidden(workbook.getSheetIndex(EXCEL_HIDE_SHEET_NAME), true); // 可以设置为false,可以查看隐藏sheet的内容排列
    }

    /**
     * IPVLAN-设置隐藏sheet
     *
     * @param
     * @return
     */
    public static void creatExcelHidePageForIP(XSSFWorkbook workbook) {
        Sheet hideInfoSheet = workbook.createSheet(EXCEL_HIDE_SHEET_NAME);// 设置隐藏 页的标题
        // 在隐藏页设置选择信息
        // 有些i需要我们手动控制
        int L = 0;
        ArrayList<String> arrayList = new ArrayList<>();// 把所有标题
        List<String> namesList = Lists.newArrayList();// Excel名称管理器 不能重复
        for (int i = 0; i < result.size(); i++) {
            log.debug("i=={}", i);
            if (i > 1) { // 此判断用于
                // 无下拉框的列
                Row row = hideInfoSheet.createRow(L); // 创建一行
                String[] rowS = (String[]) result.get(i).get(0);// 把预先数据转成字符串数组
                creatRow(row, rowS);// 把字符串数组，遍历，设置列的内容
                String rowString = (String) result.get(i).get(1);// 获取标题
                log.debug("rowString==非下拉==" + rowString);
                arrayList.add(rowString);
                hideString = arrayList;
                L++;
            } else {
                tempSize1 = L;
                Row row = hideInfoSheet.createRow(L);
                String[] rowS = (String[]) result.get(i).get(0);
                creatRow(row, rowS);
                String rowString = (String) result.get(i).get(1);
                log.debug("rowString==其他下拉==" + rowString);
                arrayList.add(rowString);
                hideString = arrayList;
                creatExcelNameList(workbook, rowString, namesList, L + 1, rowS.length, false);
                L++;
            }

        }

        // 设置隐藏页标志
        workbook.setSheetHidden(workbook.getSheetIndex(EXCEL_HIDE_SHEET_NAME), true); // 可以设置为false,可以查看隐藏sheet的内容排列
    }

    /**
     * IPVLAN-设置隐藏sheet
     *
     * @param
     * @return
     */
    public static void creatExcelHidePageForIPVLAN(XSSFWorkbook workbook) {
        Sheet hideInfoSheet = workbook.createSheet(EXCEL_HIDE_SHEET_NAME2);// 设置隐藏 页的标题
        // 在隐藏页设置选择信息
        // 有些i需要我们手动控制
        int L = 0;
        ArrayList<String> arrayList = new ArrayList<>();// 把所有标题
        List<String> namesList = Lists.newArrayList();// Excel名称管理器 不能重复
        for (int i = 0; i < result.size(); i++) {
            log.debug("i=={}", i);
            if (i > 3) { // 此判断用于
                // 无下拉框的列
                Row row = hideInfoSheet.createRow(L); // 创建一行
                String[] rowS = (String[]) result.get(i).get(0);// 把预先数据转成字符串数组
                creatRow(row, rowS);// 把字符串数组，遍历，设置列的内容
                String rowString = (String) result.get(i).get(1);// 获取标题
                log.debug("rowString==非下拉==" + rowString);
                arrayList.add(rowString);
                hideString = arrayList;
                L++;
            } else if (i == 1) { // 一级业务线
                Row row = hideInfoSheet.createRow(L);
                tempRow = (String[]) result.get(i).get(0);
                creatRow(row, tempRow);
                String rowString = (String) result.get(i).get(1);
                log.debug("rowString==一级业务线==" + rowString);
                arrayList.add(rowString);
                hideString = arrayList;
                creatExcelNameList2(workbook, rowString, namesList, L + 1, tempRow.length, false);
                L++;
            } else if (i == 2) {// 二级业务线

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
                        creatExcelNameList2(workbook, rowName, namesList, L + 1, business2.length, true);
                        L++;
                        k++;
                        break;
                    }

                }
                // 把临时数据置空
                tempRow = null;
            } else {
                tempSize1 = L;
                Row row = hideInfoSheet.createRow(L);
                String[] rowS = (String[]) result.get(i).get(0);
                creatRow(row, rowS);
                String rowString = (String) result.get(i).get(1);
                log.debug("rowString==其他下拉==" + rowString);
                arrayList.add(rowString);
                hideString = arrayList;
                creatExcelNameList2(workbook, rowString, namesList, L + 1, rowS.length, false);
                L++;
            }

        }

        // 设置隐藏页标志
        workbook.setSheetHidden(workbook.getSheetIndex(EXCEL_HIDE_SHEET_NAME2), true); // 可以设置为false,可以查看隐藏sheet的内容排列
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

    /**
     * 创建一个名称 去重.
     *
     * @param workbook
     */
    private static void creatExcelNameList(Workbook workbook, String nameCode, List<String> namesList, int order, int size,
            boolean cascadeFlag) {
        Name name;
        if (namesList.contains(nameCode)) {
            nameCode = nameCode + "r";
        }
        namesList.add(nameCode);
        log.debug("nameCode==" + nameCode);
        name = workbook.createName();
        name.setNameName(nameCode);
        name.setRefersToFormula(EXCEL_HIDE_SHEET_NAME + "!" + creatExcelNameList(order, size, cascadeFlag));
    }

    /**
     * 创建一个名称 去重.
     *
     * @param workbook
     */
    private static void creatExcelNameList2(Workbook workbook, String nameCode, List<String> namesList, int order, int size,
            boolean cascadeFlag) {
        Name name;
        if (namesList.contains(nameCode)) {
            nameCode = nameCode + "r";
        }
        namesList.add(nameCode);
        log.debug("nameCode==" + nameCode);
        name = workbook.createName();
        name.setNameName(nameCode);
        name.setRefersToFormula(EXCEL_HIDE_SHEET_NAME2 + "!" + creatExcelNameList(order, size, cascadeFlag));
    }

    /**
     * 名称数据行列计算表达式
     *
     * @param workbook
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
     * 添加数据验证选项
     *
     * @param sheet
     */
    public static void setDataValidation(Workbook wb) {
        int sheetIndex = wb.getNumberOfSheets();
        if (sheetIndex > 0) {
            for (int i = 0; i < sheetIndex; i++) {
                Sheet sheet = wb.getSheetAt(i);
                if (!EXCEL_HIDE_SHEET_NAME.equals(sheet.getSheetName())) {

                    // 添加验证数据
                    for (int a = 2; a < 3002; a++) {

                        DataValidation data_validation_list1 = getDataValidationByFormula(hideString.get(0), a, 1);
                        sheet.addValidationData(data_validation_list1);
                        DataValidation data_validation_list2 = getDataValidationByFormula(hideString.get(1), a, 2);
                        sheet.addValidationData(data_validation_list2);
                        DataValidation data_validation_list3 = getDataValidationByFormula(hideString.get(5), a, 6);
                        sheet.addValidationData(data_validation_list3);
                    }
                }
            }
        }
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
     * 生成验证--工程交维.
     *
     * @param
     * @return
     */
    public static void setDataValidationForCmdbProject(Workbook wb) {
        int sheetIndex = wb.getNumberOfSheets();
        if (sheetIndex > 0) {
            for (int i = 0; i < sheetIndex; i++) {
                Sheet sheet = wb.getSheetAt(i);
                if (!EXCEL_HIDE_SHEET_NAME.equals(sheet.getSheetName())) {
                    int startRow = 4;
                    // 主备关系
                    // DataValidation hostBackupValidation = getDataValidationByFormula("host_backup", startRow, 2);
                    DataValidation hostBackupValidation = getDataValidationCustomFormula(sheet, "host_backup", startRow, 2);
                    sheet.addValidationData(hostBackupValidation);

                    // 设备状态
                    DataValidation deviceStatusValidation = getDataValidationCustomFormula(sheet, "deviceStatus", startRow, 3);
                    sheet.addValidationData(deviceStatusValidation);

                    // 是否资源池管理
                    DataValidation mgrByPoolValidation = getDataValidationCustomFormula(sheet, "mgr_by_pool", startRow, 4);
                    sheet.addValidationData(mgrByPoolValidation);

                    // 所属位置
                    DataValidation idcValidation = getDataValidationCustomFormula(sheet, "IDC_LABEL", startRow, 10);
                    sheet.addValidationData(idcValidation);

                    // 所在机房
                    DataValidation idcLocationValidation = getDataValidationCustomFormula(sheet, "IDC_LOCATION", startRow, 11);
                    sheet.addValidationData(idcLocationValidation);

                    // 项目归属
                    DataValidation projectBelongToValidation = getDataValidationCustomFormula(sheet, "cmdbProjectBelongTo",
                            startRow, 16);
                    sheet.addValidationData(projectBelongToValidation);

                    // 设备分类
                    DataValidation deviceClassValidation = getDataValidationCustomFormula(sheet, "deviceClassLists", startRow, 17);
                    sheet.addValidationData(deviceClassValidation);

                    // 设备类型
                    DataValidation deviceTypeValidation = getDataValidationCustomFormula(sheet, "INDIRECT($Q4)", startRow, 18);
                    sheet.addValidationData(deviceTypeValidation);

                    // 设备型号
                    DataValidation deviceModelValidation = getDataValidationCustomFormula(sheet, "INDIRECT($R4)", startRow, 19);
                    sheet.addValidationData(deviceModelValidation);

                    // 操作系统
                    DataValidation deviceOsTypeValidation = getDataValidationCustomFormula(sheet, "device_os_type", startRow, 22);
                    sheet.addValidationData(deviceOsTypeValidation);

                    // 外挂存储类型1
                    DataValidation deviceMountTypeValidation = getDataValidationCustomFormula(sheet, "deviceMountType", startRow,
                            29);
                    sheet.addValidationData(deviceMountTypeValidation);
                    // 厂家1
                    DataValidation deviceMountFactoryListsValidation = getDataValidationCustomFormula(sheet,
                            "extStorageFactoryList", startRow, 28);
                    sheet.addValidationData(deviceMountFactoryListsValidation);

                    // 外挂存储类型2
                    DataValidation deviceMountTypeValidation2 = getDataValidationCustomFormula(sheet, "deviceMountType2", startRow,
                            33);
                    sheet.addValidationData(deviceMountTypeValidation2);

                    // 厂家2
                    DataValidation deviceMountFactoryLists2Validation = getDataValidationCustomFormula(sheet,
                            "extStorageFactoryList2", startRow, 32);
                    sheet.addValidationData(deviceMountFactoryLists2Validation);

                    // 设备网络层次
                    DataValidation deviceNetworkLayerValidation = getDataValidationCustomFormula(sheet,
                            "DEVICE_NETWORK_LAYER_NAME", startRow, 59);
                    sheet.addValidationData(deviceNetworkLayerValidation);
                    // 网络区域
                    DataValidation deviceNetworkAreaValidation = getDataValidationCustomFormula(sheet, "deviceNetworkArea",
                            startRow, 60);
                    sheet.addValidationData(deviceNetworkAreaValidation);

                    DataValidation businessLineValidation = getDataValidationCustomFormula(sheet, "fBusiness", startRow, 74);
                    sheet.addValidationData(businessLineValidation);

                    DataValidation business1Validation = getDataValidationCustomFormula(sheet, "business1", startRow, 75);
                    sheet.addValidationData(business1Validation);

                    DataValidation business2Validation = getDataValidationCustomFormula(sheet, "INDIRECT($BW4)", startRow, 76);
                    sheet.addValidationData(business2Validation);
                    // 应用类型
                    DataValidation deviceApplicationValidation = getDataValidationCustomFormula(sheet, "deviceApplication",
                            startRow, 77);
                    sheet.addValidationData(deviceApplicationValidation);
                    // 应用配置
                    DataValidation deviceApplicationConfigValidation = getDataValidationCustomFormula(sheet,
                            "deviceApplicationConfig", startRow, 78);
                    sheet.addValidationData(deviceApplicationConfigValidation);
                    // 存储角色
                    DataValidation storageRoleValidation = getDataValidationCustomFormula(sheet, "storageRole", startRow, 38);
                    sheet.addValidationData(storageRoleValidation);
                    // 存储业务归属
                    DataValidation disStorageTypeValidation = getDataValidationCustomFormula(sheet, "disStorageType", startRow, 39);
                    sheet.addValidationData(disStorageTypeValidation);

                    // 是否购买维保
                    DataValidation hasBuyMainTence = getDataValidationCustomFormula(sheet, "hasBuyMainTence", startRow, 84);
                    sheet.addValidationData(hasBuyMainTence);

                    // 设备厂家
                    DataValidation deviceFactoryName = getDataValidationCustomFormula(sheet, "deviceFactoryName", startRow, 86);
                    sheet.addValidationData(deviceFactoryName);

                    // 是否购买原厂维保
                    DataValidation hasBuyOriginalMainTence = getDataValidationCustomFormula(sheet, "hasBuyOriginalMainTence",
                            startRow, 90);
                    sheet.addValidationData(hasBuyOriginalMainTence);

                    // 实际购买维保类型
                    DataValidation mainTenceType = getDataValidationCustomFormula(sheet, "mainTenceType", startRow, 91);
                    sheet.addValidationData(mainTenceType);

                    DataValidation configZabbixFlagValidation = getDataValidationCustomFormula(sheet, "zabbix_agent_monitor_flag",
                            startRow, 95);
                    sheet.addValidationData(configZabbixFlagValidation);

                    DataValidation autoInstallAgentFlagValidation = getDataValidationCustomFormula(sheet, "amp_agent_monitor_flag",
                            startRow, 96);
                    sheet.addValidationData(autoInstallAgentFlagValidation);

                    DataValidation filebeatAgentFlagValidation = getDataValidationCustomFormula(sheet,
                            "filebeat_agent_monitor_flag", startRow, 98);
                    sheet.addValidationData(filebeatAgentFlagValidation);
                }
            }
        }
    }

    /**
     * 生成验证--IP地址规划表-IPsheet.
     *
     * @param
     * @return
     */
    public static void setDataValidationForIPVLAN(XSSFWorkbook wb) {
        int sheetIndex = wb.getNumberOfSheets();
        Sheet sheet1 = wb.getSheet("IP地址规划表");
        int startRow = 2;
        // IDC
        DataValidation idcValidation = getDataValidationCustomFormula(sheet1, "IDC_LABEL", startRow, 1);
        sheet1.addValidationData(idcValidation);

        // IP分类
        DataValidation ipClassValidation = getDataValidationCustomFormula(sheet1, "ipClassType", startRow, 2);
        sheet1.addValidationData(ipClassValidation);

        Sheet sheet2 = wb.getSheet("IP地址和VLAN分配表");
        // IDC
        DataValidation idcValidation2 = getDataValidationCustomFormula(sheet1, "IDC_LABEL2", startRow, 1);
        sheet2.addValidationData(idcValidation2);

        DataValidation business1Validation = getDataValidationCustomFormula(sheet1, "business1", startRow, 2);
        sheet2.addValidationData(business1Validation);

        DataValidation business2Validation = getDataValidationCustomFormula(sheet1, "INDIRECT($B2)", startRow, 3);
        sheet2.addValidationData(business2Validation);

        // IP分类
        DataValidation ipClassValidation2 = getDataValidationCustomFormula(sheet1, "ipClassType2", startRow, 4);
        sheet2.addValidationData(ipClassValidation2);
    }

    /**
     * 生成下拉框及提示
     *
     * @param formulaString
     *            验证表达式
     * @param columnIndex
     *            从第二行开始
     * @return
     */
    public static DataValidation getDataValidationByFormula(String formulaString, int columnIndex) {
        // 加载下拉列表内容
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(formulaString);
        // 设置数据有效性加载在哪个单元格上。
        // 四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(1, XLS_MAX_ROW, columnIndex - 1, columnIndex - 1);
        // 数据有效性对象
        DataValidation dataValidationList = new HSSFDataValidation(regions, constraint);
        if (formulaString.equals("IDC_LOCATION")) {
            // 特定行 取消---->只能选下拉框内容，可自己输入新的内容
            dataValidationList.setShowErrorBox(false);
        } else {
            // 设置输入信息提示信息
            dataValidationList.createPromptBox("下拉选择提示", "请使用下拉方式选择合适的值！");
            // 设置输入错误提示信息
            dataValidationList.createErrorBox("选择错误提示", "你输入的值未在备选列表中，请下拉选择合适的值！");
        }
        return dataValidationList;
    }

    /**
     * 使用已定义的数据源方式设置一个数据验证
     *
     * @param formulaString
     * @param naturalRowIndex
     * @param naturalColumnIndex
     * @return
     */
    private static DataValidation getDataValidationByFormula(String formulaString, int naturalRowIndex, int naturalColumnIndex) {
        // 创建下拉列表数据
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(formulaString);
        // 设置数据有效性加载在哪个单元格上。
        // 四个参数分别是：起始行、终止行、起始列、终止列
        int firstRow = naturalRowIndex - 1;
        int lastRow = naturalRowIndex - 1;
        int firstCol = naturalColumnIndex - 1;
        int lastCol = naturalColumnIndex - 1;
        // 设置 第几列 第几行 为下拉列表
        // CellRangeAddressList regions = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, XLS_MAX_ROW, firstCol, lastCol);

        // 数据有效性对象 绑定
        DataValidation data_validation_list = new HSSFDataValidation(regions, constraint);

        if (formulaString.equals("IDC_LOCATION")) {
            // 特定行 取消---->只能选下拉框内容，可自己输入新的内容
            data_validation_list.setShowErrorBox(false);
        } else {
            // 设置输入信息提示信息
            data_validation_list.createPromptBox("下拉选择提示", "请使用下拉方式选择合适的值！");
            // 设置输入错误提示信息
            data_validation_list.createErrorBox("选择错误提示", "你输入的值未在备选列表中，请下拉选择合适的值！");
        }

        return data_validation_list;
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

    private static DataValidation getDataValidationByDate(int naturalRowIndex, int naturalColumnIndex) {
        // 加载下拉列表内容
        DVConstraint constraint = DVConstraint.createDateConstraint(DVConstraint.OperatorType.BETWEEN, "1900-01-01", "5000-01-01",
                "yyyy-mm-dd");
        // 设置数据有效性加载在哪个单元格上。
        // 四个参数分别是：起始行、终止行、起始列、终止列
        int firstRow = naturalRowIndex - 1;
        int lastRow = naturalRowIndex - 1;
        int firstCol = naturalColumnIndex - 1;
        int lastCol = naturalColumnIndex - 1;
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        // 数据有效性对象
        DataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
        // 设置输入信息提示信息
        data_validation_list.createPromptBox("日期格式提示", "请按照'yyyy-mm-dd'格式输入日期值！");
        // 设置输入错误提示信息
        data_validation_list.createErrorBox("日期格式错误提示", "你输入的日期格式不符合'yyyy-mm-dd'格式规范，请重新输入！");
        return data_validation_list;
    }

    public static boolean isNum(String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?)$");
    }
}
