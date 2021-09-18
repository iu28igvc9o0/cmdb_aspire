package com.aspire.ums.cmdb.util;

import com.aspire.ums.cmdb.resource.entity.*;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelReaderUtils {

	private static Logger logger = Logger.getLogger(ExcelReaderUtils.class);

	private POIFSFileSystem fs;
	private Workbook wb;
	private Sheet sheet;
	private Row row;

	/**
	 * 读取Excel表格表头的内容
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
//					cellvalue = cell.getDateCellValue().toLocaleString();

					// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
					 Date date = cell.getDateCellValue();
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					 cellvalue = sdf.format(date);

				}
				// 如果是纯数字
				else {
					// 取得当前Cell的数值
					//Long number = (long) cell.getNumericCellValue();
					//存在小数点
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
	 */
	public List<ThirdPartyMaintenance> doUploadStudentData(File file) {

		Workbook wb = null;
		Sheet sheet = null;
		Row row = null;// 表格行
		ThirdPartyMaintenance tpm = null;
		String str = null;
		List<ThirdPartyMaintenance> list = new ArrayList<ThirdPartyMaintenance>();
		boolean convertSuccess = true;// 时间格式是否正确
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// 将文件转成文件输入流
			InputStream is = new FileInputStream(file);
			// 判断Excel版本
			if (file.getName().toUpperCase().endsWith(".XLSX")) {
				wb = new XSSFWorkbook(is);// Excel 2007
			} else {
				wb = new HSSFWorkbook(is);// Excel 2003
			}
			// 获得第一个表格页
			sheet = wb.getSheetAt(0);
			// 标题总行数
			row = sheet.getRow(0);
			int colNum = row.getPhysicalNumberOfCells();
			// 遍历数据
			// 从第二行开始,第一行为标题，
			for (int j = 1; j <= sheet.getLastRowNum(); j++) {

				row = sheet.getRow(j);
				int t = 0;
				tpm = new ThirdPartyMaintenance();
				while (t < colNum) {
					// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
					// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
					// str = getStringCellValue(row.getCell(t)).trim();
					str = getCellFormatValue(row.getCell(t));
					switch (t + 1) {
					case 1:
						tpm.setType(str);
						break;
					case 2:
						tpm.setDeviceName(str);
						break;
					case 3:
						tpm.setDeviceVender(str);
						break;
					case 4:
						tpm.setSystemName(str);
						break;
					case 5:
						tpm.setDeviceType(str);
						break;
					case 6:
						tpm.setDeviceSerialNumber(str);
						break;
					case 7:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setNetworkTime(str);
						} else {
							tpm.setNetworkTime(null);
						}
						break;
					case 8:
						try {
							if (StringUtils.isNotEmpty(str)) {
								// 时间格式的检验
								format.setLenient(false);
								format.parse(str);
								tpm.setDateOfWarranty(str);
							} else {
								tpm.setDateOfWarranty(null);
							}
						} catch (ParseException e) {
							return null;
						}
						break;
					case 9:
						try {
							if (StringUtils.isNotEmpty(str)) {
								// 时间格式的检验
								format.setLenient(false);
								format.parse(str);
								tpm.setDateOfWarranty02(str);
							} else {
								tpm.setDateOfWarranty02(null);
							}
						} catch (ParseException e) {
							return null;
						}
						break;
					case 10:
						try {
							if (StringUtils.isNotEmpty(str)) {
								// 时间格式的检验
								format.setLenient(false);
								format.parse(str);
								tpm.setDateOfWarranty03(str);
							} else {
								tpm.setDateOfWarranty03(null);
							}
						} catch (ParseException e) {
							return null;
						}
						break;
					case 11:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setDeviceNumber(Integer.parseInt(str));
						} else {
							tpm.setDeviceNumber(null);
						}
						break;
					case 12:
						tpm.setConfigurationSoftware(str);
						break;
					case 13:
						tpm.setOsNameVersion(str);
						break;
					case 14:
						tpm.setSystemInergrationName(str);
						break;
					case 15:
						tpm.setServiceLevelPurchase(str);
						break;
					case 16:
						tpm.setComputerRoom(str);
						break;
					case 17:
						tpm.setCabinet(str);
						break;
					case 18:
						tpm.setRemark(str);
						break;
					case 19:
						tpm.setThirdpartServiceChoice(str);
						break;
					case 20:
						tpm.setMaintenanceServicePurchase(str);
						break;
					}
					t++;
				}
				list.add(tpm);
				str = "";
			}
		} catch (Exception e) {
			logger.error("硬件维保服务excel解析失败:", e);
		}
		return list;
	}

	/**
	 * <p>
	 * Description: 业务线对应表解析方法
	 * </p>
	 * 
	 * @param file
	 * @return
	 */
	public List<BusinessLineCorrespondence> doUploadBusinessLineData(File file) throws IOException {

		Workbook wb = null;
		Sheet sheet = null;
		Row row = null;// 表格行
		SoftwareMaintenance tpm = null;
		BusinessLineCorrespondence blc = null;
		String str = null;
		List<BusinessLineCorrespondence> list = new ArrayList<BusinessLineCorrespondence>();
		boolean convertSuccess = true;// 时间格式是否正确
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// 将文件转成文件输入流
			InputStream is = new FileInputStream(file);
			// 判断Excel版本
			if (file.getName().toUpperCase().endsWith(".XLSX")) {
				wb = new XSSFWorkbook(is);// Excel 2007
			} else {
				wb = new HSSFWorkbook(is);// Excel 2003
			}
			// 获得第一个表格页
			sheet = wb.getSheetAt(0);
			// 标题总行数
			row = sheet.getRow(0);
			int colNum = row.getPhysicalNumberOfCells();
			// 遍历数据
			// 从第二行开始,第一行为标题，
			for (int j = 1; j <= sheet.getLastRowNum(); j++) {

				row = sheet.getRow(j);
				int t = 0;
				blc = new BusinessLineCorrespondence();
				String temp = "";
				while (t < colNum) {
					// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
					// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
					// str = getStringCellValue(row.getCell(t)).trim();
					str = getCellFormatValue(row.getCell(t));
					switch (t + 1) {
					case 1:
						blc.setId(Integer.parseInt(str));
						break;
					case 2:
						if (StringUtils.isNotEmpty(str)) {
							temp = str;
						}
						blc.setBusiness_line_new(temp);
						// blc.setBusiness_line_new(str);
						break;
					case 3:
						if (StringUtils.isNotEmpty(str)) {
							temp = str;
						}
						blc.setBusiness_level1_name(temp);
						break;

					}
					t++;
				}
				list.add(blc);
				str = "";
			}
		} catch (Exception e) {
			logger.error("软件维保服务excel解析失败:", e);
			throw e;

		}
		return list;
	}

	/**
	 * <p>
	 * Description: 软件维保解析方法
	 * </p>
	 * 
	 * @param file
	 * @return
	 */
	public List<SoftwareMaintenance> doUploadStudentSoftwareData(File file) throws IOException {

		Workbook wb = null;
		Sheet sheet = null;
		Row row = null;// 表格行
		SoftwareMaintenance tpm = null;
		String str = null;
		List<SoftwareMaintenance> list = new ArrayList<SoftwareMaintenance>();
		boolean convertSuccess = true;// 时间格式是否正确
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// 将文件转成文件输入流
			InputStream is = new FileInputStream(file);
			// 判断Excel版本
			if (file.getName().toUpperCase().endsWith(".XLSX")) {
				wb = new XSSFWorkbook(is);// Excel 2007
			} else {
				wb = new HSSFWorkbook(is);// Excel 2003
			}
			// 获得第一个表格页
			sheet = wb.getSheetAt(0);
			// 标题总行数
			row = sheet.getRow(0);
			int colNum = row.getPhysicalNumberOfCells();
			// 遍历数据
			// 从第二行开始,第一行为标题，
			for (int j = 1; j <= sheet.getLastRowNum(); j++) {

				row = sheet.getRow(j);
				int t = 0;
				tpm = new SoftwareMaintenance();
				while (t < colNum) {
					// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
					// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
					// str = getStringCellValue(row.getCell(t)).trim();
					str = getCellFormatValue(row.getCell(t));
					switch (t + 1) {
					case 1:
						tpm.setProject(str);
						break;
					case 2:
						tpm.setClassify(str);
						break;
					case 3:
						tpm.setSoftwareName(str);
						break;
					case 4:
						tpm.setUnit(str);
						break;
					case 5:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setEngineeringNumber(Integer.parseInt(str));
						} else {
							tpm.setEngineeringNumber(null);
						}
						break;
					case 6:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setNumber(Integer.parseInt(str));
						} else {
							tpm.setNumber(null);
						}
						break;
					case 7:
						tpm.setConfiguration(str);
						break;
					case 8:
						tpm.setVendor(str);
						break;
					case 9:
						tpm.setAuthorizationFile(str);
						break;
					case 10:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setOutOfDate(str);
						} else {
							tpm.setOutOfDate(null);
						}
						break;
					case 11:
						tpm.setRemark(str);
						break;
					}
					t++;
				}
				list.add(tpm);
				str = "";
			}
		} catch (Exception e) {
			logger.error("软件维保服务excel解析失败:", e);
			throw e;

		}
		return list;
	}

	/**
	 * <p>
	 * Description: cmdb资产管理解析方法
	 * </p>
	 * 
	 * @param file
	 * @return 解释excel表格数据，并写入CmdbHostAssetsExcelData对象中
	 * @throws Exception
	 */
	public List<CmdbHostAssetsExcelData> doUploadCmdbHostAssetsExcelData(File file) throws Exception {
		Workbook wb = null;
		Sheet sheet = null;
		Row row = null;// 表格行
		CmdbHostAssetsExcelData tpm = null;
		String str = null;
		List<CmdbHostAssetsExcelData> list = new ArrayList<CmdbHostAssetsExcelData>();
		boolean convertSuccess = true;// 时间格式是否正确
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// 将文件转成文件输入流
			InputStream is = new FileInputStream(file);
			// 判断Excel版本
			if (file.getName().toUpperCase().endsWith(".XLSX")) {
				wb = new XSSFWorkbook(is);// Excel 2007
			} else if (file.getName().toUpperCase().endsWith(".XLS")) {
				wb = new HSSFWorkbook(is);// Excel 2003
			} else {
				throw new Exception("请录入正确的文件格式！");
			}

			// 获得第一个表格页
			sheet = wb.getSheetAt(0);
			// 标题总行数
			row = sheet.getRow(0);

			int colNum = row.getPhysicalNumberOfCells();
			if (colNum > 53) {
				throw new Exception("列数");
			}
			int lastRowNum = sheet.getLastRowNum();
			if (lastRowNum > 1000) {
				throw new Exception("行数");
			}

			// 遍历数据
			// 从第二行开始,第一行为标题，
			for (int j = 1; j <= sheet.getLastRowNum(); j++) {

				row = sheet.getRow(j);
				int t = 0;
				tpm = new CmdbHostAssetsExcelData();
				String id = null;
				while (t < colNum) {
					// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
					// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
					// str = getStringCellValue(row.getCell(t)).trim();
					str = getCellFormatValue(row.getCell(t));
					tpm.setSuiteID(j + "");
					switch (t + 1) {
					case 1:
						tpm.setDevice_ip(str);
						break;
					case 2:
						tpm.setBusiness_level1(str);
						break;
					case 3:
						tpm.setBusiness_level2(str);
						break;
					case 4:
						tpm.setIdc(str);
						break;
					case 5:
						tpm.setIdc_label(str);
						break;
					case 6:
						tpm.setIdc_location(str);
						break;
					case 7:
						tpm.setDevice_class(str);
						break;
					case 8:
						if (str != null) {
							if (str.contains("re")) {
								str = str.substring(0, str.length() - 2);
							}
							str = str.replace('l', '（').replace('r', '）');
						}
						tpm.setDevice_type(str);
						break;
					case 9:
						tpm.setDevice_model(str);
						break;
					case 10:
						tpm.setDevice_os_type(str);
						break;
					case 11:
						tpm.setDevice_status(str);
						break;
					case 12:
						if (str == null || str.trim() == "") {
							tpm.setManaged_by_ansiblertable("True");
						} else {
							if (str.trim().equals("是")) {
								tpm.setManaged_by_ansiblertable("True");
							} else {
								tpm.setManaged_by_ansiblertable("False");
							}
						}
						break;
					case 13:
						if (str == null || str.trim() == "") {
							tpm.setMgd_by_pool("False");
						} else {
							if (str.trim().equals("是")) {
								tpm.setMgd_by_pool("True");
							} else {
								tpm.setMgd_by_pool("False");
							}
						}
						break;
					case 14:
						tpm.setHost_backup(str);
						break;
					case 15:
						tpm.setDevice_maintenance(str);
						break;
					case 16:
						tpm.setDevice_maintenance_vender(str);
						break;
					case 17:
						tpm.setResource_plan(str);
						break;
					case 18:
						tpm.setDis_st_type(str);
						break;
					case 19:
						tpm.setBlong_to(str);
						break;
					case 20:
						tpm.setBlock_size(str);
						break;
					case 21:
						tpm.setDis_storage(str);
						break;
					case 22:
						tpm.setOther_ip(str);
						break;
					case 23:
						tpm.setDevice_log_name(str);
						break;
					case 24:
						tpm.setDevice_cell(str);
						break;
					case 25:
						tpm.setBox_num(str);
						break;
					case 26:
						tpm.setSlot_num(str);
						break;
					case 27:
						tpm.setBox_mgd_ip(str);
						break;
					case 28:
						tpm.setExsi_ip(str);
						break;
					case 29:
						tpm.setVm_name(str);
						break;
					case 30:
						tpm.setVm_ip(str);
						break;
					case 31:
						tpm.setDevice_standard(str);
						break;
					case 32:
						tpm.setDevice_sn(str);
						break;
					case 33:
						tpm.setB_card_sn(str);
						break;
					case 34:
						tpm.setAsset_number(str);
						break;
					case 35:
						tpm.setDis_st_dir(str);
						break;
					case 36:
						tpm.setMaintenance_time(str);
						break;
					case 37:
						tpm.setOnline_time(str);
						break;
					case 38:
						tpm.setConsole_ip(str);
						break;
					case 39:
						tpm.setConsole_vlan(str);
						break;
					case 40:
						tpm.setConsole_mask(str);
						break;
					case 41:
						tpm.setConsole_gw(str);
						break;
					case 42:
						tpm.setConsole_user(str);
						break;
					case 43:
						tpm.setConsole_password(str);
						break;
					case 44:
						tpm.setBusiness_vlan(str);
						break;
					case 45:
						tpm.setLocal_disk(str);
						break;
					case 46:
						tpm.setMount_disk(str);
						break;
					case 47:
						tpm.setNetwork_area(str);
						break;
					case 48:
						tpm.setRemark(str);
						break;
					case 49:
						tpm.setTrans_cost(str);
						break;
					case 50:
						tpm.setUnit_price(str);
						break;
					case 51:
						tpm.setProrate_date(str);
						break;
					case 52:
						tpm.setService_life(str);
						break;
					case 53:
						tpm.setIs_assign(str);
						break;
					}

					t++;
				}

				list.add(tpm);
				str = "";
			}
		} catch (Exception e) {
			logger.error("CMDB设备资产信息excel解析失败:", e);
			if (e.getMessage().contains("列数")) {
				throw new Exception("抱歉，已超出文件的列数限制，请重新填写！");
			} else if (e.getMessage().contains("行数")) {
				throw new Exception("抱歉，已超出文件的行数限制，请分批填写！");
			} else {
				throw new Exception("文件读取发生异常");
			}
		}
		return list;
		/*
		 * Workbook wb = null; Sheet sheet = null; Row row = null;// 表格行
		 * CmdbHostAssetsExcelData tpm = null; String str = null;
		 * List<CmdbHostAssetsExcelData> list = new
		 * ArrayList<CmdbHostAssetsExcelData>(); boolean convertSuccess=true;//时间格式是否正确
		 * // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写； SimpleDateFormat format = new
		 * SimpleDateFormat("yyyy-MM-dd"); try { // 将文件转成文件输入流 InputStream is = new
		 * FileInputStream(file); // 判断Excel版本 if
		 * (file.getName().toUpperCase().endsWith(".XLSX")) { wb = new
		 * XSSFWorkbook(is);// Excel 2007 } else
		 * if(file.getName().toUpperCase().endsWith(".XLS")) { wb = new
		 * HSSFWorkbook(is);// Excel 2003 }else{ throw new Exception("请录入正确的文件格式！"); }
		 * 
		 * 
		 * 
		 * 
		 * // 获得第一个表格页 sheet = wb.getSheetAt(0); //标题总行数 row = sheet.getRow(0);
		 * 
		 * int colNum = row.getPhysicalNumberOfCells(); if (colNum>48) { throw new
		 * Exception("列数"); } int lastRowNum = sheet.getLastRowNum(); if
		 * (lastRowNum>1000) { throw new Exception("行数"); }
		 * 
		 * // 遍历数据 //从第二行开始,第一行为标题， for (int j = 1; j <= sheet.getLastRowNum(); j++) {
		 * 
		 * row = sheet.getRow(j); int t = 0; tpm = new CmdbHostAssetsExcelData(); String
		 * id=null; while (t < colNum) { //
		 * 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据 //
		 * 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean // str =
		 * getStringCellValue(row.getCell(t)).trim(); str =
		 * getCellFormatValue(row.getCell(t)); tpm.setSuiteID(j+""); switch (t+1) { case
		 * 1: tpm.setDevice_ip(str); break; case 2: tpm.setBusiness_level1(str); break;
		 * case 3: tpm.setBusiness_level2(str); break; case 4: tpm.setIdc(str); break;
		 * case 5: tpm.setIdc_label(str); break; case 6: tpm.setIdc_location(str);
		 * break; case 7: tpm.setDevice_class(str); break; case 8: str =
		 * str.replace('l', '（').replace('r', '）'); tpm.setDevice_type(str); case 9:
		 * tpm.setDevice_model(str); break; case 10: tpm.setDevice_os_type(str); break;
		 * case 11: tpm.setDevice_status(str); break; case 12: tpm.setBlock_size(str);
		 * break; case 13: tpm.setDis_storage(str); break; case 14: if
		 * (str==null||str.trim()=="") { tpm.setManaged_by_ansiblertable("True"); } else
		 * { if (str.equals("是")) { tpm.setManaged_by_ansiblertable("True"); } else {
		 * tpm.setManaged_by_ansiblertable("False"); } } break; case 15: if
		 * (str==null||str.trim()=="") { tpm.setMgd_by_pool("False"); }else{ if
		 * (str.equals("是")) { tpm.setMgd_by_pool("True"); } else {
		 * tpm.setMgd_by_pool("False"); } } break; case 16:
		 * tpm.setDevice_maintenance(str); break; case 17:
		 * tpm.setDevice_maintenance_vender(str); break; case 18: tpm.setOther_ip(str);
		 * break; case 19: tpm.setDevice_log_name(str); break; case 20:
		 * tpm.setHost_backup(str); break; case 21: tpm.setDevice_cell(str); break; case
		 * 22: tpm.setBox_num(str); break; case 23: tpm.setSlot_num(str); break; case
		 * 24: tpm.setBox_mgd_ip(str); break; case 25: tpm.setExsi_ip(str); break; case
		 * 26: tpm.setVm_name(str); break; case 27: tpm.setVm_ip(str); break; case 28:
		 * tpm.setDevice_standard(str); break; case 29: tpm.setDevice_sn(str); break;
		 * case 30: tpm.setB_card_sn(str); break; case 31: tpm.setAsset_number(str);
		 * break; case 32: tpm.setDis_st_dir(str); break; case 33:
		 * tpm.setMaintenance_time(str); break; case 34: tpm.setOnline_time(str); break;
		 * case 35: tpm.setDis_st_type(str); break; case 36: tpm.setResource_plan(str);
		 * break; case 37: tpm.setBlong_to(str); break; case 38: tpm.setConsole_ip(str);
		 * break; case 39: tpm.setConsole_vlan(str); break; case 40:
		 * tpm.setConsole_mask(str); break; case 41: tpm.setConsole_gw(str); break; case
		 * 42: tpm.setConsole_user(str); break; case 43: tpm.setConsole_password(str);
		 * break; case 44: tpm.setBusiness_vlan(str); break; case 45:
		 * tpm.setLocal_disk(str); break; case 46: tpm.setMount_disk(str); break; case
		 * 47: tpm.setNetwork_area(str); break; case 48: tpm.setRemark(str); break; }
		 * t++; }
		 * 
		 * list.add(tpm); str = ""; } } catch (Exception e) {
		 * logger.error("CMDB设备资产信息excel解析失败:",e); if (e.getMessage().contains("列数")) {
		 * throw new Exception("抱歉，已超出文件的列数限制，请重新填写！"); }else if
		 * (e.getMessage().contains("行数")) { throw new
		 * Exception("抱歉，已超出文件的行数限制，请分批填写！"); }else{ throw new Exception("文件读取发生异常"); }
		 * } return list;
		 */

	}

	/**
	 * <p>
	 * Description: 局数据录入excel解析方法
	 * </p>
	 * 
	 * @param file
	 * @return
	 */
	public static List<CmdbPartData> getCmdbPartDataTemp(File file, String fileName) throws Exception {

		Workbook wb = null;
		Sheet sheet = null;
		Row row = null;// 表格行
		CmdbPartData tpm = null;
		String str = null;
		List<CmdbPartData> list = new ArrayList<CmdbPartData>();
		boolean convertSuccess = true;// 时间格式是否正确
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 将文件转成文件输入流
		InputStream is = new FileInputStream(file);
		// 判断Excel版本
		if (fileName.toUpperCase().endsWith(".XLSX")) {
			wb = new XSSFWorkbook(is);// Excel 2007
		} else if (fileName.toUpperCase().endsWith(".XLS")) {
			wb = new HSSFWorkbook(is);// Excel 2003
		} else {
			throw new Exception("请录入正确的文件格式！");
		}
		int colNum = 0;
		try {
			// 获得第三个表格页
			sheet = wb.getSheetAt(2);
			// 标题总行数
			// row = sheet.getRow(1);
			colNum = 19;
			// colNum = row.getPhysicalNumberOfCells();
			// if(colNum!=19){
			// throw new Exception("请按模板录入");
			// }
		} catch (Exception e) {
			throw new Exception("请按模板录入");
		}
		// 遍历数据
		// 从第三行开始,第一二行为标题，
		for (int j = 2; j <= sheet.getLastRowNum(); j++) {

			row = sheet.getRow(j);
			if (row == null) {
				break;
			}
			int t = 0;
			tpm = new CmdbPartData();
			while (t < colNum) {
				// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
				// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
				// str = getStringCellValue(row.getCell(t)).trim();
				Cell c = row.getCell(t);
				str = getCellFormatValue(c);
				switch (t + 1) {
				case 1:
					break;
				case 2:
					tpm.setCode(str);
					break;
				case 3:
					tpm.setUpdateFlag(str);
					break;
				case 4:
					tpm.setUpdateDescription(str);
					break;
				case 5:
					tpm.setEffectiveDate(str);
					break;
				case 6:
					tpm.setDisableDate(str);
					break;
				case 7:
					tpm.setMakeRange(str);
					break;
				case 8:
					tpm.setBusinessCode(str);
					break;
				case 9:
					tpm.setBusinessName(str);
					break;
				case 10:
					tpm.setBusinessDenominate(str);
					break;
				case 11:
					tpm.setUsageWay(str);
					break;
				case 12:
					tpm.setJoinAPN(str);
					break;
				case 13:
					if (str == null || "".equals(str)) {
						tpm.setIpAddress(null);
					} else {
						tpm.setIpAddress(str.trim());
					}
					break;
				case 14:
					tpm.setProtocolNumber(str);
					break;
				case 15:
					tpm.setPortNumber(str);
					break;
				case 16:
					if (str == null || "".equals(str)) {
						tpm.setUrlName(null);
					} else {
						tpm.setUrlName(str.trim());
					}
					break;
				case 17:
					tpm.setFluxExpenses(str);
					break;
				case 18:
					tpm.setDescriptionIpFunction(str);
					break;
				case 19:
					tpm.setDescriptionDialandTestNum(str);
					break;
				}
				t++;
			}
			/*
			 * 20180410,需求修改为IP可为空，为空时仅检查URL if(tpm.getIpAddress()!=null){ list.add(tpm); }
			 */
			list.add(tpm);
		}
		return list;
	}

	/**
	 * 局数据比对录入数据解析
	 * 
	 * @param file
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> getCmdbPartDataCompare(File file, String fileName) throws Exception {
		Workbook wb = null;
		Sheet sheet = null;
		Row row = null;// 表格行
		Map tpm = null;
		String str = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		boolean convertSuccess = true;// 时间格式是否正确
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 将文件转成文件输入流
		InputStream is = new FileInputStream(file);
		// 判断Excel版本
		if (fileName.toUpperCase().endsWith(".XLSX")) {
			wb = new XSSFWorkbook(is);// Excel 2007
		} else if (fileName.toUpperCase().endsWith(".XLS")) {
			wb = new HSSFWorkbook(is);// Excel 2003
		} else {
			throw new Exception("请录入正确的文件格式！");
		}
		int colNum = 0;
		try {
			// 获得第三个表格页
			sheet = wb.getSheetAt(0);
			// 标题总行数
			colNum = 3;
		} catch (Exception e) {
			throw new Exception("请按模板录入");
		}
		// 遍历数据
		// 从第二行开始,第一行为标题，
		for (int j = 1; j <= sheet.getLastRowNum(); j++) {
			row = sheet.getRow(j);
			if (row == null) {
				break;
			}
			int t = 0;
			tpm = new HashMap<String, String>();
			while (t < colNum) {
				// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
				// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
				// str = getStringCellValue(row.getCell(t)).trim();
				Cell c = row.getCell(t);
				str = getCellFormatValue(c);
				switch (t + 1) {
				case 1:
					tpm.put("serial", str);
					break;
				case 2:
					if (str == null) {
						tpm.put("ip", null);
					} else {
						tpm.put("ip", str.trim());
					}
					break;
				case 3:
					tpm.put("url", str);
					break;
				}
				t++;
			}
			if (tpm.get("ip") != null || tpm.get("url") != null) {
				list.add(tpm);
			}
		}
		return list;
	}

	/**
	 * 
	 * @param file
	 *            解析文件对象
	 * @param fileName
	 *            文件名（检验是否为excel文件）
	 * @param sheetNum
	 *            解析第几个sheet
	 * @param key
	 *            返回集合的key
	 * @param index
	 *            从哪行开始解析数据
	 * @return 返回集合
	 * @throws Exception
	 */

	public static List<Map<String, String>> importCommon(File file, String fileName, int sheetNum, List<String> key,
			int index) throws Exception {
		Workbook wb = null;
		Sheet sheet = null;
		Row row = null;// 表格行
		Map tpm = null;
		String str = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		boolean convertSuccess = true;// 时间格式是否正确
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 将文件转成文件输入流
		InputStream is = new FileInputStream(file);
		// 判断Excel版本
		if (fileName.toUpperCase().endsWith(".XLSX")) {
			wb = new XSSFWorkbook(is);// Excel 2007
		} else if (fileName.toUpperCase().endsWith(".XLS")) {
			wb = new HSSFWorkbook(is);// Excel 2003
		} else {
			throw new Exception("请录入正确的文件格式！");
		}
		try {
			// 获得第三个表格页
			sheet = wb.getSheetAt(sheetNum);
		} catch (Exception e) {
			throw new Exception("请按模板录入");
		}
		// 遍历数据
		for (int j = index; j <= sheet.getLastRowNum(); j++) {
			row = sheet.getRow(j);
			if (row == null) {
				break;
			}
			int t = 0;
			tpm = new HashMap<String, String>();
			while (t < key.size()) {
				Cell c = row.getCell(t);
				str = getCellFormatValue(c);
				tpm.put(key.get(t), str);
				t++;
			}
			list.add(tpm);
		}
		return list;
	}

	/**
	 * <p>
	 * Description: 高级维保解析方法
	 * </p>
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public List<CmdbAdvancedMaintance> doUploadAdvancedMaintanceData(File file) throws Exception {
int num =-1;
		Workbook wb = null;
		Sheet sheet = null;
		Row row = null;// 表格行
		CmdbAdvancedMaintance cam = null;
		String str = null;
		List<CmdbAdvancedMaintance> list = new ArrayList<CmdbAdvancedMaintance>();
		boolean convertSuccess = true;// 时间格式是否正确
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// 将文件转成文件输入流
			InputStream is = new FileInputStream(file);
			// 判断Excel版本
			if (file.getName().toUpperCase().endsWith(".XLSX")) {
				wb = new XSSFWorkbook(is);// Excel 2007
			} else {
				wb = new HSSFWorkbook(is);// Excel 2003
			}
			// 获得第一个表格页
			sheet = wb.getSheetAt(0);
			// 标题总行数
			row = sheet.getRow(0);
			int colNum = row.getPhysicalNumberOfCells();
			// 遍历数据
			// 从第二行开始,第一行为标题，
			for (int j = 1; j <= sheet.getLastRowNum(); j++) {

				row = sheet.getRow(j);
				int t = 0;
				cam = new CmdbAdvancedMaintance();
				while (t < colNum) {
					// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
					// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
					// str = getStringCellValue(row.getCell(t)).trim();
					str = getCellFormatValue(row.getCell(t));
					switch (t + 1) {
					case 1:
						num=1;
						cam.setProduct(str);
						break;
					case 2:
						cam.setService_com(str);
						;
						break;
					case 3:
						cam.setCooperate_com(str);
						;
						break;
					case 4:
						cam.setContent(str);
						break;
					case 5:
						cam.setNum(str);
						break;
					case 6:
						cam.setUnit(str);
						break;
					case 7:
						cam.setBegin_date(str);
						;
						break;
					case 8:
						cam.setEnd_date(str);
						;
						break;
					case 9:
						cam.setRemarks(str);
						break;
					}
					t++;
				}
				list.add(cam);
				str = "";
			}
		} catch (Exception e) {
			logger.error("高级维保服务excel解析失败:", e);
			throw new Exception("请按模板录入");
		}
		return list;
	}

	/**
	 * <p>
	 * Description: 网络设备管理解析方法
	 * </p>
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public List<NetDeviceMgr> doUploadNetDeviceMgr(File file) throws Exception {

		Workbook wb = null;
		Sheet sheet = null;
		Row row = null;// 表格行
		NetDeviceMgr ndm = null;
		String str = null;
		List<NetDeviceMgr> list = new ArrayList<NetDeviceMgr>();
		boolean convertSuccess = true;// 时间格式是否正确
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// 将文件转成文件输入流
			InputStream is = new FileInputStream(file);
			// 判断Excel版本
			if (file.getName().toUpperCase().endsWith(".XLSX")) {
				wb = new XSSFWorkbook(is);// Excel 2007
			} else {
				wb = new HSSFWorkbook(is);// Excel 2003
			}
			// 获得第一个表格页
			sheet = wb.getSheetAt(0);
			// 标题总行数
			row = sheet.getRow(0);
			int colNum = row.getPhysicalNumberOfCells();
			// 遍历数据
			// 从第二行开始,第一行为标题，
			for (int j = 1; j <= sheet.getLastRowNum(); j++) {

				row = sheet.getRow(j);
				int t = 0;
				ndm = new NetDeviceMgr();
				while (t < colNum) {
					// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
					// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
					// str = getStringCellValue(row.getCell(t)).trim();
					str = getCellFormatValue(row.getCell(t));
					switch (t + 1) {
					case 1:
						ndm.setIp(str);
						break;
					case 2:
						ndm.setDeviceType(str);
						;
						break;
					case 3:
						ndm.setIdc(str);
						;
						break;
					}
					t++;
				}
				list.add(ndm);
				str = "";
			}
		} catch (Exception e) {
			logger.error("网络设备管理excel解析失败:", e);
			throw new Exception("请按模板录入");
		}
		return list;
	}

	/**
	 * <p>
	 * Description: cmdb资产收集解析方法
	 * </p>
	 * 
	 * @param file
	 * @return 解释excel表格数据，并写入ResourceDemandColletExcel对象中
	 * @throws Exception
	 */
	public List<ResourceDemandColletExcel> doUploadResourceCollectData(File file) throws Exception {
		Workbook wb = null;
		Sheet sheet = null;
		Row row = null;// 表格行
		int t = 0;
		int j = 0;
		ResourceDemandColletExcel tpm = null;
		String str = null;
		List<ResourceDemandColletExcel> list = new ArrayList<ResourceDemandColletExcel>();
		boolean convertSuccess = true;// 时间格式是否正确
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// 将文件转成文件输入流
			InputStream is = new FileInputStream(file);
			// 判断Excel版本
			if (file.getName().toUpperCase().endsWith(".XLSX")) {
				wb = new XSSFWorkbook(is);// Excel 2007
			} else if (file.getName().toUpperCase().endsWith(".XLS")) {
				wb = new HSSFWorkbook(is);// Excel 2003
			} else {
				throw new Exception("请录入正确的文件格式！");
			}

			// 获得第一个表格页
			sheet = wb.getSheetAt(0);
			// 标题总行数
			row = sheet.getRow(0);

			int colNum = row.getPhysicalNumberOfCells();
			if (colNum > 100) {
				throw new Exception("列数");
			}
			int lastRowNum = sheet.getLastRowNum();
			if (lastRowNum > 1000) {
				throw new Exception("行数");
			}
			

			// 遍历数据
			// 从第三行开始,第一二行为标题，
			for (j = 2; j <= sheet.getLastRowNum(); j++) {

				row = sheet.getRow(j);
				 t = 0;
				tpm = new ResourceDemandColletExcel();
				String id = null;
				while (t < colNum) {
					// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
					// str = getStringCellValue(row.getCell(t)).trim();
					str = getCellFormatValue1(row.getCell(t));
					tpm.setId(j + "");
					switch (t + 1) {
					case 1:
						tpm.setPrimary_department(str);
						break;
					case 2:
						tpm.setSecondary_department(str);
						break;
					case 3:
						tpm.setApp_system(str);
						break;
					case 4:
						tpm.setIs_project(str);
						break;

					case 5:
						 if (StringUtils.isNotEmpty(str)) {
								tpm.setProject_time(str);
							} else {
								tpm.setProject_time(null);
							}
						break;

					case 6:
						 if (StringUtils.isNotEmpty(str)) {
								tpm.setResource_put_date(str);
							} else {
								tpm.setResource_put_date(null);
							}
						break;
					case 7:
					   if (StringUtils.isNotEmpty(str)) {
							tpm.setResource_produce_date(str);
						} else {
							tpm.setResource_produce_date(null);
						}
						break;
					case 8:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setSystem_description(str);
						} else {
							tpm.setSystem_description(null);
						}
						break;
					case 9:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setVm_model1(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setVm_model1(null);
						}

						break;
					case 10:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setVm_model2(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setVm_model2(null);
						}

						break;
					case 11:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setVm_model3(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setVm_model3(null);
						}

						break;

						
					case 12:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setVm_model4(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setVm_model4(null);
						}

						break;
					case 13:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setVm_model5(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setVm_model5(null);
						}

						break;
					case 14:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setVm_model6(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setVm_model6(null);
						}

						break;
					case 15:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setVm_model7(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setVm_model7(null);
						}

						break;
					case 16:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setVm_model8(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setVm_model8(null);
						}

						break;
					case 17:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setPm_low_app_server(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));

						} else {
							tpm.setPm_low_app_server(null);
						}
						break;
					case 18:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setPm_analytical_server(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setPm_analytical_server(null);
						}

						break;

					case 19:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setPm_distributed_server(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setPm_distributed_server(null);
						}

						break;
					case 20:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setPm_cache_server(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setPm_cache_server(null);
						}

						break;
					case 21:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setPm_high_app_server(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setPm_high_app_server(null);
						}

						break;
					case 22:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setStorage_distributd_file_capacity(validateNum(j + 1,t + 1, str));
						} else {
							tpm.setStorage_distributd_file_capacity(null);
						}

						break;
					case 23:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setStorage_distributd_block_performance(validateNum(j + 1,t + 1, str));
						} else {
							tpm.setStorage_distributd_block_performance(null);
						}

						break;
					case 24:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setStorage_object_capacity(validateNum(j + 1,t + 1, str));
						} else {
							tpm.setStorage_object_capacity(null);
						}

						break;
					case 25:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setStorage_fc_san_capacity(validateNum(j + 1,t + 1, str));
						} else {
							tpm.setStorage_fc_san_capacity(null);
						}

						break;

					case 26:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setStorage_ip_san_capacity(validateNum(j + 1,t + 1, str));
						} else {
							tpm.setStorage_ip_san_capacity(null);
						}

						break;
					case 27:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setStorage_fc_san_naked(validateNum(j + 1,t + 1, str));
						} else {
							tpm.setStorage_fc_san_naked(null);
						}

						break;
					case 28:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setStorage_ip_san_naked(validateNum(j + 1,t + 1, str));
						} else {
							tpm.setStorage_ip_san_naked(null);
						}

						break;
					case 29:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setStorage_back_up(validateNum(j + 1,t + 1, str));
						} else {
							tpm.setStorage_back_up(null);
						}

						break;
					case 30:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setCmnet_address(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setCmnet_address(null);
						}

						break;
					case 31:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setBandwidth_gbps_cmnet(validateNum(j + 1,t + 1, str));
						} else {
							tpm.setBandwidth_gbps_cmnet(null);
						}

						break;
					case 32:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setIp_address(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setIp_address(null);
						}

						break;
					case 33:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setBandwidth_gbps_ip(validateNum(j + 1,t + 1, str));
						} else {
							tpm.setBandwidth_gbps_ip(null);
						}

						break;
					case 34:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setDb_mysql(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setDb_mysql(null);
						}

						break;
					case 35:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setDb_mongodb(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setDb_mongodb(null);
						}

						break;
					case 36:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setDb_postgresql(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setDb_postgresql(null);
						}

						break;
					case 37:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setDb_memory(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setDb_memory(null);
						}

						break;
					case 38:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setDb_other(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setDb_other(null);
						}

						break;
					case 39:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setCache_redis(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setCache_redis(null);
						}

						break;
					case 40:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setCache_memcached(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setCache_memcached(null);
						}

						break;
					case 41:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setInfo_middleware_activemq(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setInfo_middleware_activemq(null);
						}

						break;
					case 42:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setInfo_middleware_kafka(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setInfo_middleware_kafka(null);
						}

						break;
					case 43:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setInfo_middleware_rocketmq(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setInfo_middleware_rocketmq(null);
						}

						break;
					case 44:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setApp_middleware_apache(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setApp_middleware_apache(null);
						}

						break;
					case 45:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setApp_middleware_jboos(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setApp_middleware_jboos(null);
						}

						break;
					case 46:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setApp_middleware_tomcat(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setApp_middleware_tomcat(null);
						}

						break;
					case 47:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setBalancer_nginx(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setBalancer_nginx(null);
						}

						break;
					case 48:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setBalancer_haproxy(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setBalancer_haproxy(null);
						}

						break;
					case 49:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setServer_zookeeper(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setServer_zookeeper(null);
						}

						break;
					case 50:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setServer_etcd(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setServer_etcd(null);
						}

						break;
					case 51:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setSearch_middleware_es(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setSearch_middleware_es(null);
						}

						break;
					case 52:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setServer_docker_registry(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setServer_docker_registry(null);
						}

						break;
					case 53:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setStream_jbpm(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setStream_jbpm(null);
						}

						break;
					case 54:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setStream_activity(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setStream_activity(null);
						}

						break;
					case 55:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setImage_openjdk(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setImage_openjdk(null);
						}

						break;
					case 56:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setImage_python(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setImage_python(null);
						}

						break;
					case 57:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setImage_go(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setImage_go(null);
						}

						break;
					case 58:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setImage_nodejs(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setImage_nodejs(null);
						}

						break;
					case 59:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setImage_ruby(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setImage_ruby(null);
						}

						break;
					case 60:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setImage_net(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setImage_net(null);
						}

						break;
					case 61:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setCicd_jenkins(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setCicd_jenkins(null);
						}

						break;
					case 62:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setFramework_springcloud(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setFramework_springcloud(null);
						}

						break;
					case 63:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setFramework_other(validateNum(j + 1,t + 1, str.substring(0,str.indexOf("."))));
						} else {
							tpm.setFramework_other(null);
						}

						break;
					case 64:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setSpecial_requirement(str);
						} else {
							tpm.setSpecial_requirement(null);
						}

						break;
					case 65:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setAddress_requirement(str);
						} else {
							tpm.setAddress_requirement(null);
						}

						break;
					case 66:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setDepartment_head(str);
						} else {
							tpm.setDepartment_head(null);
						}

						break;
					case 67:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setInter_contact(str);
						} else {
							tpm.setInter_contact(null);
						}


						break;
					
					case 68:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setPhone(str);
						} else {
							tpm.setPhone(null);
						}

						break;
					case 69:
						if (StringUtils.isNotEmpty(str)) {
							tpm.setEmail(str);
						} else {
							tpm.setEmail(null);
						}

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
				logger.error("文件读取发生异常"+"第"+(j+1)+"行"+"第"+(t+1)+"列填写数据有误!!!");
				throw e;
			}
		}
		return list;

	}

	/**
	 * <p>
	 * Description: cmdb资产预分配解析方法
	 * </p>
	 *
	 * @param file
	 * @return 解释excel表格数据，并写入ResourceDemandColletExcel对象中
	 * @throws Exception
	 */
	public List<ResourcePreDistributionExcel> doUploadResourcePreDistributionData(File file) throws Exception {
		Workbook wb = null;
		Sheet sheet = null;
		Row row = null;// 表格行
		int t = 0;
		int j = 0;
		ResourcePreDistributionExcel tpm = null;
		String str = null;
		List<ResourcePreDistributionExcel> list = new ArrayList<ResourcePreDistributionExcel>();
		boolean convertSuccess = true;// 时间格式是否正确
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// 将文件转成文件输入流
			InputStream is = new FileInputStream(file);
			// 判断Excel版本
			if (file.getName().toUpperCase().endsWith(".XLSX")) {
				wb = new XSSFWorkbook(is);// Excel 2007
			} else if (file.getName().toUpperCase().endsWith(".XLS")) {
				wb = new HSSFWorkbook(is);// Excel 2003
			} else {
				throw new Exception("请录入正确的文件格式！");
			}

			// 获得第一个表格页
			sheet = wb.getSheetAt(0);
			// 标题总行数
			row = sheet.getRow(0);

			int colNum = row.getPhysicalNumberOfCells();
			if (colNum > 100) {
				throw new Exception("列数");
			}
			int lastRowNum = sheet.getLastRowNum();
			if (lastRowNum > 1000) {
				throw new Exception("行数");
			}


			// 遍历数据
			// 从第二行开始
			for (j = 1; j <= sheet.getLastRowNum(); j++) {

				row = sheet.getRow(j);
				t = 0;
				tpm = new ResourcePreDistributionExcel();
				String id = null;
				while (t < colNum) {
					// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
					// str = getStringCellValue(row.getCell(t)).trim();
					str = getCellFormatValue1(row.getCell(t));
					tpm.setId(j + "");
					switch (t + 1) {
						case 1:
							tpm.setResource_pool(str);
							break;
						case 2:
							tpm.setPrimary_department(str);
							break;
						case 3:
							tpm.setSecondary_department(str);
							break;
						case 4:
							tpm.setApp_system(str);
							break;
						case 5:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setStatus(str);
							} else {
								tpm.setStatus(null);
							}
							break;


						case 6:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setVm_model1(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setVm_model1(null);
							}

							break;
						case 7:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setVm_model2(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setVm_model2(null);
							}

							break;
						case 8:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setVm_model3(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setVm_model3(null);
							}

							break;


						case 9:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setVm_model4(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setVm_model4(null);
							}

							break;
						case 10:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setVm_model5(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setVm_model5(null);
							}

							break;
						case 11:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setVm_model6(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setVm_model6(null);
							}

							break;
						case 12:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setVm_model7(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setVm_model7(null);
							}

							break;
						case 13:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setVm_model8(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setVm_model8(null);
							}

							break;
						case 14:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setPm_low_app_server(str.substring(0,str.indexOf(".")));

							} else {
								tpm.setPm_low_app_server(null);
							}
							break;
						case 15:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setPm_analytical_server(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setPm_analytical_server(null);
							}

							break;

						case 16:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setPm_distributed_server(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setPm_distributed_server(null);
							}

							break;
						case 17:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setPm_cache_server(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setPm_cache_server(null);
							}

							break;
						case 18:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setPm_high_app_server(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setPm_high_app_server(null);
							}

							break;
						case 19:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setStorage_distributd_file_capacity(str);
							} else {
								tpm.setStorage_distributd_file_capacity(null);
							}

							break;
						case 20:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setStorage_distributd_block_performance(str);
							} else {
								tpm.setStorage_distributd_block_performance(null);
							}

							break;
						case 21:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setStorage_object_capacity(str);
							} else {
								tpm.setStorage_object_capacity(null);
							}

							break;
						case 22:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setStorage_fc_san_capacity(str);
							} else {
								tpm.setStorage_fc_san_capacity(null);
							}

							break;

						case 23:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setStorage_ip_san_capacity(str);
							} else {
								tpm.setStorage_ip_san_capacity(null);
							}

							break;
						case 24:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setStorage_fc_san_naked(str);
							} else {
								tpm.setStorage_fc_san_naked(null);
							}

							break;
						case 25:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setStorage_ip_san_naked(str);
							} else {
								tpm.setStorage_ip_san_naked(null);
							}

							break;
						case 26:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setStorage_back_up(str);
							} else {
								tpm.setStorage_back_up(null);
							}

							break;
						case 27:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setCmnet_address(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setCmnet_address(null);
							}

							break;
						case 28:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setBandwidth_gbps_cmnet(str);
							} else {
								tpm.setBandwidth_gbps_cmnet(null);
							}

							break;
						case 29:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setIp_address(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setIp_address(null);
							}

							break;
						case 30:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setBandwidth_gbps_ip(str);
							} else {
								tpm.setBandwidth_gbps_ip(null);
							}

							break;
						case 31:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setDb_mysql(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setDb_mysql(null);
							}

							break;
						case 32:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setDb_mongodb(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setDb_mongodb(null);
							}

							break;
						case 33:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setDb_postgresql(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setDb_postgresql(null);
							}

							break;
						case 34:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setDb_memory(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setDb_memory(null);
							}

							break;
						case 35:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setDb_other(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setDb_other(null);
							}

							break;
						case 36:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setCache_redis(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setCache_redis(null);
							}

							break;
						case 37:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setCache_memcached(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setCache_memcached(null);
							}

							break;
						case 38:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setInfo_middleware_activemq(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setInfo_middleware_activemq(null);
							}

							break;
						case 39:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setInfo_middleware_kafka(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setInfo_middleware_kafka(null);
							}

							break;
						case 40:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setInfo_middleware_rocketmq(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setInfo_middleware_rocketmq(null);
							}

							break;
						case 41:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setApp_middleware_apache(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setApp_middleware_apache(null);
							}

							break;
						case 42:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setApp_middleware_jboos(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setApp_middleware_jboos(null);
							}

							break;
						case 43:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setApp_middleware_tomcat(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setApp_middleware_tomcat(null);
							}

							break;
						case 44:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setBalancer_nginx(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setBalancer_nginx(null);
							}

							break;
						case 45:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setBalancer_haproxy(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setBalancer_haproxy(null);
							}

							break;
						case 46:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setServer_zookeeper(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setServer_zookeeper(null);
							}

							break;
						case 47:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setServer_etcd(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setServer_etcd(null);
							}

							break;
						case 48:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setSearch_middleware_es(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setSearch_middleware_es(null);
							}

							break;
						case 49:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setServer_docker_registry(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setServer_docker_registry(null);
							}

							break;
						case 50:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setStream_jbpm(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setStream_jbpm(null);
							}

							break;
						case 51:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setStream_activity(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setStream_activity(null);
							}

							break;
						case 52:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setImage_openjdk(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setImage_openjdk(null);
							}

							break;
						case 53:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setImage_python(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setImage_python(null);
							}

							break;
						case 54:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setImage_go(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setImage_go(null);
							}

							break;
						case 55:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setImage_nodejs(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setImage_nodejs(null);
							}

							break;
						case 56:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setImage_ruby(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setImage_ruby(null);
							}

							break;
						case 57:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setImage_net(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setImage_net(null);
							}

							break;
						case 58:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setCicd_jenkins(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setCicd_jenkins(null);
							}

							break;
						case 59:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setFramework_springcloud(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setFramework_springcloud(null);
							}

							break;
						case 60:
							if (StringUtils.isNotEmpty(str)) {
								tpm.setFramework_other(str.substring(0,str.indexOf(".")));
							} else {
								tpm.setFramework_other(null);
							}

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
				throw new Exception("文件读取发生异常"+"第"+(j)+"行"+"第"+(t+1)+"列填写数据有误");
			}
		}
		return list;
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
			throw new Exception("资产收集导入文件: 第 " + rowIdx + "行, 第 " + columnIdx + " 列, 值："+ numStr + " 不合法!"
					+"\n请确保单元格内容不包含字母和其它非数字字符!!!");
		}
	}

	public static void main(String[] args) {
		ExcelReaderUtils readerUtils = new ExcelReaderUtils();
		try {
			readerUtils.validateNum(10,9, "10T");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
