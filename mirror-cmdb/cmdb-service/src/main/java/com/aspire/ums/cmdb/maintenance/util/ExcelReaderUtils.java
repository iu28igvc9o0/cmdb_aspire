package com.aspire.ums.cmdb.maintenance.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;

import com.aspire.ums.cmdb.maintenance.entity.MaintenHardware;

public class ExcelReaderUtils {

    private static Logger logger = LoggerFactory.getLogger(ExcelReaderUtils.class);

    private POIFSFileSystem fs;

    private Workbook wb;

    private Sheet sheet;

    private Row row;

    /**
     * 读取Excel表格表头的内容
     * 
     * @param InputStream
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
     * @param InputStream
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

                case HSSFCell.CELL_TYPE_BOOLEAN:
                    cellvalue = cell.getBooleanCellValue() == true ? "True" : "False";
                    break;

                default:
                    cellvalue = null;
            }
        } else {
            cellvalue = null;
        }
        return cellvalue;

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

    /**
     * <p>
     * Description: 硬件维保解析方法
     * </p>
     * 
     * @param file
     * @return
     * @throws Exception
     */
    public List<MaintenHardware> doUploadMaintenHardwareData(MultipartFile file, List<String> resourcePoolList,
            List<String> systemNameList, List<String> deviceClassifyList, List<String> deviceTypeList,
            List<String> deviceModelList, List<String> optionsTrueFalseList, List<String> originBuyExplainList,
            List<String> maintenFactoryList, List<String> realMaintenList) throws Exception {

        List<MaintenHardware> list = new ArrayList<MaintenHardware>();
        boolean convertSuccess = true;// 时间格式是否正确
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        // 将文件转成文件输入流
        InputStream is = file.getInputStream();

        Workbook wb = null;
        // 判断Excel版本
        if (file.getOriginalFilename().endsWith(".xlsx")) {

            wb = new XSSFWorkbook(is);// Excel 2007
        } else {

            wb = new HSSFWorkbook(is);// Excel 2003
        }


        // 获得第一个表格页
        Sheet sheet = wb.getSheetAt(0);
        // 标题总行数
        Row row = sheet.getRow(0);

        int colNum = row.getPhysicalNumberOfCells();
        // 遍历数据
        // 从第二行开始,第一行为标题，
        for (int j = 1; j <= sheet.getLastRowNum(); j++) {

            row = sheet.getRow(j);
            int t = 0;
            MaintenHardware tpm = new MaintenHardware();
            while (t < colNum) {
                // 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
                // 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
                // str = getStringCellValue(row.getCell(t)).trim();
                String str = getCellFormatValue(row.getCell(t));
                switch (t + 1) {
                    case 1:
                        tpm.setProvince(str);
                        break;
                    case 2:
                        tpm.setCity(str);
                        break;
                    case 3:
                        if (str == null) {
                            throw new RuntimeException("第" + (j + 1) + "行资源池为空");
                        }
                        if (!resourcePoolList.contains(str)) {

                            throw new RuntimeException("第" + (j + 1) + "行资源池不在范围");
                        }
                        tpm.setResourcePool(str);
                        break;
                    case 4:
                        if (str.length() > 0) {
                            if (!systemNameList.contains(str)) {
                                throw new RuntimeException("第" + (j + 1) + "行所属业务不在范围");
                            }
                        }
                        tpm.setSystemName(str);
                        break;
                    case 5:
                        if (str == null) {
                            throw new RuntimeException("第" + (j + 1) + "行设备分类为空");
                        }
                        if (!deviceClassifyList.contains(str)) {

                            throw new RuntimeException("第" + (j + 1) + "行设备分类不在范围");
                        }

                        tpm.setDeviceClassify(str);
                        break;
                    case 6:
                        if (str == null) {
                            throw new RuntimeException("第" + (j + 1) + "行设备类型为空");
                        }
                        if (!deviceTypeList.contains(str)) {

                            throw new RuntimeException("第" + (j + 1) + "行设备类型不在范围");
                        }
                        tpm.setDeviceType(str);
                        break;
                    case 7:
                        if (str == null) {
                            throw new RuntimeException("第" + (j + 1) + "行设备型号为空");
                        }
                        if (!deviceModelList.contains(str)) {

                            throw new RuntimeException("第" + (j + 1) + "行设备型号不在范围");
                        }
                        tpm.setDeviceModel(str);
                        break;
                    case 8:
                        if (str == null) {
                            throw new RuntimeException("第" + (j + 1) + "行设备名称为空");
                        }
                        tpm.setDeviceName(str);
                        break;
                    case 9:
                        if (str == null) {
                            throw new RuntimeException("第" + (j + 1) + "行序列号为空");
                        }
                        tpm.setDeviceSerialNumber(str);
                        break;
                    case 10:
                        if (str == null) {
                            throw new RuntimeException("第" + (j + 1) + "行资产编号为空");
                        }
                        tpm.setAssetsNumber(str);
                        break;
                    case 11:
                        if (str == null) {
                            throw new RuntimeException("第" + (j + 1) + "行出保时间为空");
                        }
                        if (StringUtils.isNotBlank(str)) {

                            tpm.setWarrantyDate(format.parse(str));
                        } else {
                            tpm.setWarrantyDate(null);
                        }
                        break;
                    case 12:
                        if (str == null) {
                            throw new RuntimeException("第" + (j + 1) + "行是否购买维保为空");
                        }
                        if (!optionsTrueFalseList.contains(str)) {

                            throw new RuntimeException("第" + (j + 1) + "行是否购买维保不在范围");
                        }

                        tpm.setBuyMainten(str);
                        break;
                    case 13:
                        if (str.length() > 0) {
                            if (!optionsTrueFalseList.contains(str)) {
                                throw new RuntimeException("第" + (j + 1) + "行是否需原厂维保不在范围");
                            }
                        }
                        tpm.setOriginBuy(str);
                        break;
                    case 14:
                        if (str.length() > 0) {
                            if (!originBuyExplainList.contains(str)) {
                                throw new RuntimeException("第" + (j + 1) + "行原厂维保购买必要性说明不在范围");
                            }
                        }
                        tpm.setOriginBuyExplain(str);
                        break;

                    case 15:
                        if (str.length() > 0) {
                            if (!maintenFactoryList.contains(str)) {
                                throw new RuntimeException("第" + (j + 1) + "行业务建议维保厂家不在范围");
                            }
                        }
                        tpm.setAdviceMaintenFactory(str);
                        break;
                    case 16:
                        if (str == null) {
                            throw new RuntimeException("第" + (j + 1) + "行维保厂家为空");
                        }
                        if (!maintenFactoryList.contains(str)) {

                            throw new RuntimeException("第" + (j + 1) + "行维保厂家不在范围");
                        }

                        tpm.setMaintenFactory(str);
                        break;
                    case 17:
                        if (str != null && str.length() > 0) {
                            Pattern p = Pattern.compile("^((13|14|15|17|18)[0-9]{1}\\d{8})$");
                            boolean isMatch = p.matcher(str).matches();
                            if (!isMatch) {
                                throw new RuntimeException("第" + (j + 1) + "行维保供应商联系手机格式错误");
                            }
                        }
                        tpm.setMaintenSupplyContact(str);
                        break;

                    case 18:
                        if (str != null && str.length() > 0) {
                            Pattern p = Pattern.compile("^((13|14|15|17|18)[0-9]{1}\\d{8})$");
                            boolean isMatch = p.matcher(str).matches();
                            if (!isMatch) {
                                throw new RuntimeException("第" + (j + 1) + "行维保厂家联系手机格式错误");
                            }
                        }
                        tpm.setMaintenFactoryContact(str);
                        break;
                    case 19:
                        if (StringUtils.isNotBlank(str)) {
                            tpm.setMaintenBeginDate(format.parse(str));
                        } else {
                            tpm.setMaintenBeginDate(null);
                        }
                        break;
                    case 20:
                        if (StringUtils.isNotBlank(str)) {
                            tpm.setMaintenEndDate(format.parse(str));
                        } else {
                            tpm.setMaintenEndDate(null);
                        }
                        break;
                    case 21:
                        if (str.length() > 0) {
                            if (!realMaintenList.contains(str)) {
                                throw new RuntimeException("第" + (j + 1) + "行原厂维保购买必要性说明不在范围");
                            }
                        }
                        tpm.setRealMaintenType(str);
                        break;
                    case 22:
                        tpm.setAdmin(str);
                        break;

                }
                t++;
            }
            list.add(tpm);

        }

        // hangfang 2020.07.30 资源未释放：流
        try {
            is.close();
            wb.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
        is.close();
        return list;
    }

    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    // @描述：是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * 验证EXCEL文件
     * 
     * @param filePath
     * @return
     */
    public static boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            return false;
        }
        return true;
    }

}
