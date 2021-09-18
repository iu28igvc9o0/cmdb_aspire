package com.aspire.ums.cmdb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aspire.ums.cmdb.demand.entity.DemandExcel;

public class ExcelReaderUtils {

    private static Logger logger = LoggerFactory.getLogger(ExcelReaderUtils.class);

    private POIFSFileSystem fs;

    private Workbook wb;

    private Sheet sheet;

    private Row row;

    /**
     * 读取Excel表格表头的内容
     * 
     * @param is
     * @return String 表头内容的数组
     */
    public String[] readExcelTitle(InputStream is) {
        try {
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = wb.getSheetAt(0);
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        System.out.println("colNum:" + colNum);
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            // title[i] = getStringCellValue(row.getCell((short) i));
            title[i] = getCellFormatValue(row.getCell((short) i));
        }
        return title;
    }

    /**
     * 读取Excel数据内容
     * 
     * @param is
     * @return Map 包含单元格数据内容的Map对象
     */
    public Map<Integer, String> readExcelContent(InputStream is) {
        Map<Integer, String> content = new HashMap<Integer, String>();
        String str = "";
        try {
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            while (j < colNum) {
                // 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
                // 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
                // str += getStringCellValue(row.getCell((short) j)).trim() +
                // "-";
                str += getCellFormatValue(row.getCell((short) j)).trim() + "    ";
                j++;
            }
            content.put(i, str);
            str = "";
        }
        return content;
    }

    /**
     * 获取单元格数据内容为字符串类型的数据
     * 
     * @param cell
     *            Excel单元格
     * @return String 单元格数据内容
     */
    private String getStringCellValue(Cell cell) {
        String strCell = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        if (cell == null) {
            return "";
        }
        return strCell;
    }

    /**
     * 获取单元格数据内容为日期类型的数据
     *
     * @param cell
     *            Excel单元格
     * @return String 单元格数据内容
     */
    private String getDateCellValue(Cell cell) {
        String result = "";
        try {
            int cellType = cell.getCellType();
            if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
                Date date = cell.getDateCellValue();
                result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
            } else if (cellType == HSSFCell.CELL_TYPE_STRING) {
                String date = getStringCellValue(cell);
                result = date.replaceAll("[年月]", "-").replace("日", "").trim();
            } else if (cellType == HSSFCell.CELL_TYPE_BLANK) {
                result = "";
            }
        } catch (Exception e) {
            System.out.println("日期格式不正确!");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据HSSFCell类型设置数据
     *
     * @param cell
     * @return
     */
    private static String getCellFormatValue(Cell cell) {
        String cellvalue = null;
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
                case HSSFCell.CELL_TYPE_NUMERIC:
                case HSSFCell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式

                        // 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                        // cellvalue = cell.getDateCellValue().toLocaleString();

                        // 方法2：这样子的data格式是不带带时分秒的：2011-10-12
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cellvalue = sdf.format(date);

                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        Long number = (long) cell.getNumericCellValue();
                        cellvalue = String.valueOf(number);
                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case HSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = null;
            }
        } else {
            cellvalue = null;
        }
        return cellvalue;

    }

    /**
     * <p>
     * Description: cmdb资产收集解析方法
     * </p>
     *
     * @param file
     * @return 解释excel表格数据，并写入DemandExcel对象中
     * @throws Exception
     */
    public List<DemandExcel> doUploadDemandData(File file) throws Exception {
        Workbook wb = null;
        Sheet sheet = null;
        Row row = null;// 表格行
        int t = 0;
        int j = 0;
        DemandExcel tpm = null;
        String str = null;
        List<DemandExcel> list = new ArrayList<DemandExcel>();
        boolean convertSuccess = true;// 时间格式是否正确
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        InputStream is = null;
        try {
            // 将文件转成文件输入流
            is = new FileInputStream(file);
            // 判断Excel版本
            if (file.getName().toUpperCase(Locale.ENGLISH).endsWith(".XLSX")) {
                wb = new XSSFWorkbook(is);// Excel 2007
            } else if (file.getName().toUpperCase(Locale.ENGLISH).endsWith(".XLS")) {
                wb = new HSSFWorkbook(is);// Excel 2003
            } else {
                throw new Exception("请录入正确的文件格式！");
            }

            // 获得第一个表格页
            sheet = wb.getSheetAt(0);
            // 标题总行数
            row = sheet.getRow(0);

            int colNum = row.getPhysicalNumberOfCells();
            if (colNum > 171) {
                throw new Exception("列数");
            }
            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum > 1000) {
                throw new Exception("行数");
            }

            // 遍历数据
            // 从第二行开始,第一行为标题，
            for (j = 1; j <= sheet.getLastRowNum(); j++) {

                row = sheet.getRow(j);
                t = 0;
                tpm = new DemandExcel();
                while (t < colNum) {
                    // 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
                    str = getCellFormatValue1(row.getCell(t));
                    switch (t + 1) {
                        case 1:
                            tpm.setDepartment(str);
                            break;
                        case 2:
                            tpm.setTenant(str);
                            break;
                        case 3:
                            tpm.setTenant_phone(str);
                            break;
                        case 4:
                            tpm.setBiz_system(str);
                            break;
                        case 5:
                            tpm.setIs_project(str);
                            break;
                        case 6:
                            tpm.setProject_time(str);
                            break;
                        case 7:
                            tpm.setSubmit_time(str);
                            break;
                        case 8:
                            tpm.setCycle_time(str);
                            break;
                        case 9:
                            tpm.setIs_host_maintenance(str);
                            break;
                        case 10:
                            tpm.setIs_disaster(str);
                            break;
                        case 11:
                            tpm.setDisaster_type(str);
                            break;
                        case 12:
                            tpm.setWlan_requirement(str);
                            break;
                        case 13:
                            tpm.setCommission_time(str);
                            break;
                        case 14:
                            tpm.setIs_idc_requirement(str);
                            break;
                        case 15:
                            tpm.setIdc_requirement(str);
                            break;
                        case 16:
                            tpm.setVm_1(str);
                            break;
                        case 17:
                            tpm.setVm_1_scene(str);
                            break;
                        case 18:
                            tpm.setVm_1_use(str);
                            break;
                        case 19:
                            tpm.setVm_2(str);
                            break;
                        case 20:
                            tpm.setVm_2_scene(str);
                            break;
                        case 21:
                            tpm.setVm_2_use(str);
                            break;
                        case 22:
                            tpm.setVm_3(str);
                            break;
                        case 23:
                            tpm.setVm_3_scene(str);
                            break;
                        case 24:
                            tpm.setVm_3_use(str);
                            break;
                        case 25:
                            tpm.setVm_4(str);
                            break;
                        case 26:
                            tpm.setVm_4_scene(str);
                            break;
                        case 27:
                            tpm.setVm_4_use(str);
                            break;
                        case 28:
                            tpm.setVm_5(str);
                            break;
                        case 29:
                            tpm.setVm_5_scene(str);
                            break;
                        case 30:
                            tpm.setVm_5_use(str);
                            break;
                        case 31:
                            tpm.setVm_6(str);
                            break;
                        case 32:
                            tpm.setVm_6_scene(str);
                            break;
                        case 33:
                            tpm.setVm_6_use(str);
                            break;
                        case 34:
                            tpm.setVm_7(str);
                            break;
                        case 35:
                            tpm.setVm_7_scene(str);
                            break;
                        case 36:
                            tpm.setVm_7_use(str);
                            break;
                        case 37:
                            tpm.setPhysical_1(str);
                            break;
                        case 38:
                            tpm.setPhysical_1_scene(str);
                            break;
                        case 39:
                            tpm.setPhysical_1_use(str);
                            break;
                        case 40:
                            tpm.setPhysical_2(str);
                            break;
                        case 41:
                            tpm.setPhysical_2_scene(str);
                            break;
                        case 42:
                            tpm.setPhysical_2_use(str);
                            break;
                        case 43:
                            tpm.setPhysical_3(str);
                            break;
                        case 44:
                            tpm.setPhysical_3_scene(str);
                            break;
                        case 45:
                            tpm.setPhysical_3_use(str);
                            break;
                        case 46:
                            tpm.setPhysical_4(str);
                            break;
                        case 47:
                            tpm.setPhysical_4_scene(str);
                            break;
                        case 48:
                            tpm.setPhysical_4_use(str);
                            break;
                        case 49:
                            tpm.setPhysical_5(str);
                            break;
                        case 50:
                            tpm.setPhysical_5_scene(str);
                            break;
                        case 51:
                            tpm.setPhysical_5_use(str);
                            break;
                        case 52:
                            tpm.setBasic_1(str);
                            break;
                        case 53:
                            tpm.setBasic_1_scene(str);
                            break;
                        case 54:
                            tpm.setBasic_1_use(str);
                            break;
                        case 55:
                            tpm.setBasic_2(str);
                            break;
                        case 56:
                            tpm.setBasic_2_scene(str);
                            break;
                        case 57:
                            tpm.setBasic_2_use(str);
                            break;
                        case 58:
                            tpm.setBasic_3(str);
                            break;
                        case 59:
                            tpm.setBasic_3_scene(str);
                            break;
                        case 60:
                            tpm.setBasic_3_use(str);
                            break;
                        case 61:
                            tpm.setBasic_4(str);
                            break;
                        case 62:
                            tpm.setBasic_4_scene(str);
                            break;
                        case 63:
                            tpm.setBasic_4_use(str);
                            break;
                        case 64:
                            tpm.setBasic_5(str);
                            break;
                        case 65:
                            tpm.setBasic_5_scene(str);
                            break;
                        case 66:
                            tpm.setBasic_5_use(str);
                            break;
                        case 67:
                            tpm.setBasic_6(str);
                            break;
                        case 68:
                            tpm.setBasic_6_scene(str);
                            break;
                        case 69:
                            tpm.setBasic_6_use(str);
                            break;
                        case 70:
                            tpm.setDatabase_1(str);
                            break;
                        case 71:
                            tpm.setDatabase_1_scene(str);
                            break;
                        case 72:
                            tpm.setDatabase_1_use(str);
                            break;
                        case 73:
                            tpm.setDatabase_2(str);
                            break;
                        case 74:
                            tpm.setDatabase_2_scene(str);
                            break;
                        case 75:
                            tpm.setDatabase_2_use(str);
                            break;
                        case 76:
                            tpm.setDatabase_3(str);
                            break;
                        case 77:
                            tpm.setDatabase_3_scene(str);
                            break;
                        case 78:
                            tpm.setDatabase_3_use(str);
                            break;
                        case 79:
                            tpm.setDatabase_4(str);
                            break;
                        case 80:
                            tpm.setDatabase_4_scene(str);
                            break;
                        case 81:
                            tpm.setDatabase_4_use(str);
                            break;
                        case 82:
                            tpm.setDatabase_5(str);
                            break;
                        case 83:
                            tpm.setDatabase_5_scene(str);
                            break;
                        case 84:
                            tpm.setDatabase_5_use(str);
                            break;
                        case 85:
                            tpm.setFbs_1(str);
                            break;
                        case 86:
                            tpm.setFbs_1_scene(str);
                            break;
                        case 87:
                            tpm.setFbs_1_use(str);
                            break;
                        case 88:
                            tpm.setFbs_2(str);
                            break;
                        case 89:
                            tpm.setFbs_2_scene(str);
                            break;
                        case 90:
                            tpm.setFbs_2_use(str);
                            break;
                        case 91:
                            tpm.setMessage_1(str);
                            break;
                        case 92:
                            tpm.setMessage_1_scene(str);
                            break;
                        case 93:
                            tpm.setMessage_1_use(str);
                            break;
                        case 94:
                            tpm.setMessage_2(str);
                            break;
                        case 95:
                            tpm.setMessage_2_scene(str);
                            break;
                        case 96:
                            tpm.setMessage_2_use(str);
                            break;
                        case 97:
                            tpm.setMessage_3(str);
                            break;
                        case 98:
                            tpm.setMessage_3_scene(str);
                            break;
                        case 99:
                            tpm.setMessage_3_use(str);
                            break;
                        case 100:
                            tpm.setApp_1(str);
                            break;
                        case 101:
                            tpm.setApp_1_scene(str);
                            break;
                        case 102:
                            tpm.setApp_1_use(str);
                            break;
                        case 103:
                            tpm.setApp_2(str);
                            break;
                        case 104:
                            tpm.setApp_2_scene(str);
                            break;
                        case 105:
                            tpm.setApp_2_use(str);
                            break;
                        case 106:
                            tpm.setApp_3(str);
                            break;
                        case 107:
                            tpm.setApp_3_scene(str);
                            break;
                        case 108:
                            tpm.setApp_3_use(str);
                            break;
                        case 109:
                            tpm.setHaproxy_1(str);
                            break;
                        case 110:
                            tpm.setHaproxy_1_scene(str);
                            break;
                        case 111:
                            tpm.setHaproxy_1_use(str);
                            break;
                        case 112:
                            tpm.setHaproxy_2(str);
                            break;
                        case 113:
                            tpm.setHaproxy_2_scene(str);
                            break;
                        case 114:
                            tpm.setHaproxy_2_use(str);
                            break;
                        case 115:
                            tpm.setFbs_service_1(str);
                            break;
                        case 116:
                            tpm.setFbs_service_1_scene(str);
                            break;
                        case 117:
                            tpm.setFbs_service_1_use(str);
                            break;
                        case 118:
                            tpm.setFbs_service_2(str);
                            break;
                        case 119:
                            tpm.setFbs_service_2_scene(str);
                            break;
                        case 120:
                            tpm.setFbs_service_2_use(str);
                            break;
                        case 121:
                            tpm.setSearch_1(str);
                            break;
                        case 122:
                            tpm.setSearch_1_scene(str);
                            break;
                        case 123:
                            tpm.setSearch_1_scene(str);
                            break;
                        case 124:
                            tpm.setRegistry_1(str);
                            break;
                        case 125:
                            tpm.setRegistry_1_scene(str);
                            break;
                        case 126:
                            tpm.setRegistry_1_use(str);
                            break;
                        case 127:
                            tpm.setFlow_1(str);
                            break;
                        case 128:
                            tpm.setFlow_1_scene(str);
                            break;
                        case 129:
                            tpm.setFlow_1_use(str);
                            break;
                        case 130:
                            tpm.setFlow_2(str);
                            break;
                        case 131:
                            tpm.setFlow_2_scene(str);
                            break;
                        case 132:
                            tpm.setFlow_2_use(str);
                            break;
                        case 133:
                            tpm.setLang_1(str);
                            break;
                        case 134:
                            tpm.setLang_1_scene(str);
                            break;
                        case 135:
                            tpm.setLang_1_use(str);
                            break;
                        case 136:
                            tpm.setLang_2(str);
                            break;
                        case 137:
                            tpm.setLang_2_scene(str);
                            break;
                        case 138:
                            tpm.setLang_2_use(str);
                            break;
                        case 139:
                            tpm.setLang_3(str);
                            break;
                        case 140:
                            tpm.setLang_3_scene(str);
                            break;
                        case 141:
                            tpm.setLang_3_use(str);
                            break;
                        case 142:
                            tpm.setLang_4(str);
                            break;
                        case 143:
                            tpm.setLang_4_scene(str);
                            break;
                        case 144:
                            tpm.setLang_4_use(str);
                            break;
                        case 145:
                            tpm.setLang_5(str);
                            break;
                        case 146:
                            tpm.setLang_5_scene(str);
                            break;
                        case 147:
                            tpm.setLang_5_use(str);
                            break;
                        case 148:
                            tpm.setLang_6(str);
                            break;
                        case 149:
                            tpm.setLang_6_scene(str);
                            break;
                        case 150:
                            tpm.setLang_6_use(str);
                            break;
                        case 151:
                            tpm.setCicd_1(str);
                            break;
                        case 152:
                            tpm.setCicd_1_scene(str);
                            break;
                        case 153:
                            tpm.setCicd_1_use(str);
                            break;
                        case 154:
                            tpm.setDevelop_1(str);
                            break;
                        case 155:
                            tpm.setDevelop_1_scene(str);
                            break;
                        case 156:
                            tpm.setDevelop_1_use(str);
                            break;
                        case 157:
                            tpm.setDevelop_2(str);
                            break;
                        case 158:
                            tpm.setDevelop_2_scene(str);
                            break;
                        case 159:
                            tpm.setDevelop_2_use(str);
                            break;
                        case 160:
                            tpm.setNetwork_1(str);
                            break;
                        case 161:
                            tpm.setNetwork_1_scene(str);
                            break;
                        case 162:
                            tpm.setNetwork_1_use(str);
                            break;
                        case 163:
                            tpm.setNetwork_2(str);
                            break;
                        case 164:
                            tpm.setNetwork_2_scene(str);
                            break;
                        case 165:
                            tpm.setNetwork_2_use(str);
                            break;
                        case 166:
                            tpm.setNetwork_3(str);
                            break;
                        case 167:
                            tpm.setNetwork_3_scene(str);
                            break;
                        case 168:
                            tpm.setNetwork_3_use(str);
                            break;
                        case 169:
                            tpm.setNetwork_14(str);
                            break;
                        case 170:
                            tpm.setNetwork_14_scene(str);
                            break;
                        case 171:
                            tpm.setNetwork_14_use(str);
                            break;
                    }

                    t++;
                }

                list.add(tpm);
                str = "";
            }
        } catch (Exception e) {
            logger.error("资源收集信息excel解析失败:", e);
            if (e.getMessage().contains("列数")) {
                throw new Exception("抱歉，已超出文件的列数限制，请重新填写！");
            } else if (e.getMessage().contains("行数")) {
                throw new Exception("抱歉，已超出文件的行数限制，请分批填写！");
            } else {
                logger.error("文件读取发生异常" + "第" + (j + 1) + "行" + "第" + (t + 1) + "列填写数据有误!!!");
                throw e;
            }
        } finally {
            if (null != is) {
                is.close();
            }
        }
        return list;

    }

    /**
     * 根据HSSFCell类型设置数据
     *
     * @param cell
     * @return
     */
    private static String getCellFormatValue1(Cell cell) {
        String cellvalue = null;
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
                case HSSFCell.CELL_TYPE_NUMERIC:
                case HSSFCell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式

                        // 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                        // cellvalue = cell.getDateCellValue().toLocaleString();

                        // 方法2：这样子的data格式是不带带时分秒的：2011-10-12
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cellvalue = sdf.format(date);
                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        // Long number = (long) cell.getNumericCellValue();
                        // 存在小数点
                        Double number = cell.getNumericCellValue();
                        cellvalue = String.valueOf(number);
                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case HSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = null;
            }
        } else {
            cellvalue = null;
        }
        return cellvalue;

    }

    private static final Pattern NUMBER_CHECK_PATTERN = Pattern.compile("^(-|\\+)?\\d+(\\.\\d+)?$");

    private String validateNum(Integer rowIdx, Integer columnIdx, String numStr) throws Exception {
        if (StringUtils.isEmpty(numStr)) {
            return null;
        }
        Matcher isNum = NUMBER_CHECK_PATTERN.matcher(numStr);
        if (isNum.matches()) {
            return numStr;
        } else {
            throw new Exception("资产收集导入文件: 第 " + rowIdx + "行, 第 " + columnIdx + " 列, 值：" + numStr + " 不合法!"
                    + "\n请确保单元格内容不包含字母和其它非数字字符!!!");
        }
    }

}
