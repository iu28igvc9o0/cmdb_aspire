package com.aspire.mirror.alert.server.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExportExcelUtil {


	private static final long serialVersionUID = 2165773254718823136L;


	/**

	 * @Title: exportExcel

	 * @Description: 导出Excel的方法

	 * @author: EX-WANGJIANQIANG001 @ 2012-8-31 下午03:49:08

	 * @param workbook

	 * @param sheetNum

	 * @param sheetTitle

	 * @param headers

	 * @param result

	 * @param out

	 * @throws Exception

	 */

	public void exportExcel(Workbook workbook, int sheetNum, String sheetTitle, String[] headers, List<List<String>> result, OutputStream out) throws Exception{

		// 生成一个表格

		Sheet sheet = workbook.createSheet();

		workbook.setSheetName(sheetNum,sheetTitle);

		// 生成一个样式

		CellStyle style = workbook.createCellStyle();

		// 设置样式

		style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);

		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);

		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		style.setBorderRight(HSSFCellStyle.BORDER_THIN);

		style.setBorderTop(HSSFCellStyle.BORDER_THIN);

		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		// 生成一个字体

		Font font = workbook.createFont();
		//org.apache.poi.hssf.util.HSSFColor$GREY_25_PERCENT@452267
		//font.setColor(HSSFColor.BLACK.index);

		font.setColor(HSSFColor.GREEN.index);
		font.setFontHeightInPoints((short) 12);

		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		// 把字体应用到当前的样式

		style.setFont(font);

		// 指定当单元格内容显示不下时自动换行

		style.setWrapText(true);


		// 产生表格标题行

		Row row = sheet.createRow(0);

		for (int i = 0; i < headers.length; i++) {

			Cell cell = row.createCell(i);

			cell.setCellStyle(style);

			HSSFRichTextString text = new HSSFRichTextString(headers[i]);

			cell.setCellValue(text.toString());

		}

		// 遍历集合数据，产生数据行

		if(result != null){

			int index = 1;

			for(List<String> m:result){

				row = sheet.createRow(index);

				int cellIndex = 0;

				for(String str:m){

					Cell cell = row.createCell(cellIndex);

					cell.setCellValue(str.toString());

					cellIndex++;

				}

				index++;

			}

		}

	}

	public void exportExcel(Workbook workbook, int sheetNum, String sheetTitle, String[] headers, List<Map<String, Object>> result, String[] listKey ) throws Exception{

		// 生成一个表格

		Sheet sheet = workbook.createSheet();
		workbook.setSheetName(sheetNum,sheetTitle);
		// 生成一个样式
		CellStyle style = workbook.createCellStyle();

		// 设置样式
		//style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
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
		// font.setColor(HSSFColor.BLACK.index);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 230);
		//font.setColor(HSSFColor.GREEN.index);
		// font.setFontHeightInPoints((short) 12);

		// font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		// 把字体应用到当前的样式

		style.setFont(font);



		// 指定当单元格内容显示不下时自动换行

		// style.setWrapText(true);

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
		String [] testva= new String[headers.length];
		for (int i = 0; i < headers.length; i++) {
			testva[i]="8888888";
			Cell cell = row.createCell(i);

			cell.setCellStyle(style);

			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text.toString());
			// sheet.autoSizeColumn(( short ) i );

		}
		row.setHeightInPoints(30);

		// 遍历集合数据，产生数据行


		if (result.size() != 0) {

			int index = 1;
			int cellIndex;
			Cell cell;
			String va;
			for (Map map : result) {

				row = sheet.createRow(index);
				cellIndex = 0;
				for (String key : listKey) {

					if(null ==testva[cellIndex])
					{
						testva[cellIndex]="0";
					}

					if(null !=map && null !=map.get(key))
					{
						cell = row.createCell(cellIndex);
						va=map.get(key).toString();
						if(va.getBytes().length >= testva[cellIndex].getBytes().length)
						{
							/*if(cellIndex!=0 && va.getBytes().length > testva[cellIndex-1].getBytes().length)
							{*/
							testva[cellIndex]=va;
							//}

						}
						if(!StringUtils.isEmpty(va) && isNum(va) ){
							//是数字当作double处理
							if(va.length()>11)
							{
								cell.setCellValue(va);
								cell.setCellStyle(style1);
							}else
							{
								cell.setCellValue(Double.parseDouble(va));
								cell.setCellStyle(style1);

							}
						}else
						{
							cell.setCellValue(map.get(key).toString());
							cell.setCellStyle(style1);

						}

						// sheet.setColumnWidth(cellIndex,map.get(key).toString().getBytes().length*2*256);
						/*

						if (map.get(key) instanceof Double) {
							cell.setCellValue(((Double) map.get(key)).doubleValue());
							cell.setCellStyle(style1);
						} else if (map.get(key) instanceof Integer) {
							cell.setCellValue(((Integer) map.get(key)).doubleValue());
							cell.setCellStyle(style1);
						} else if (map.get(key) instanceof Number) {
							cell.setCellValue(((Number) map.get(key)).doubleValue());
							cell.setCellStyle(style1);
						} else {
						cell.setCellValue(map.get(key).toString());
						cell.setCellStyle(style1);
					}*/
					}else
					{
						cell = row.createCell(cellIndex);
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
			//cell.setCellValue("无符合条件记录....");
		}

		for(int i=0;i<headers.length;i++)
		{
//			sheet.setColumnWidth(i,sheet.getColumnWidth(i)*17/10);
			String t=headers[i]+"88";
			//如果数据长度大于列宽允许设置的最大值,将列宽设置为默认值
//			if(t.getBytes().length*256>256*12){
//				sheet.setColumnWidth(i,"888888888888".getBytes().length*256);
//			}else{
//
//			}
			sheet.setColumnWidth(i,t.getBytes().length*256);


			//sheet.autoSizeColumn(( short ) i,true ); // 调整第一列宽度
			/*if(testva[i].length()== testva[i].getBytes().length)
			{
				String t=testva[i]+"88";
				sheet.setColumnWidth(i,t.length()*256);
			}else
			{
			//sheet.setColumnWidth(i,testva[i].length()*2*256);
			sheet.setColumnWidth(i,testva[i].getBytes().length*256);
			}*/

		}

	}

	//导出cmdb-device-assets   excel表格    模板
	public void exportExcelCMDB(Workbook workbook, int sheetNum, String sheetTitle, String[] headers, List<Map> result, String[] listKey ) throws Exception{

		// 生成一个表格

		Sheet sheet = workbook.createSheet();

		workbook.setSheetName(sheetNum,sheetTitle);
		// 生成一个样式
		CellStyle style = workbook.createCellStyle();

		// 设置样式
		//style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
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
		// font.setColor(HSSFColor.BLACK.index);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 230);
		//font.setColor(HSSFColor.GREEN.index);
		// font.setFontHeightInPoints((short) 12);

		// font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		// 把字体应用到当前的样式

		style.setFont(font);



		// 指定当单元格内容显示不下时自动换行

		// style.setWrapText(true);

		CellStyle style1 = workbook.createCellStyle();
		style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单无格的边框为粗体
		style1.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
		style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style1.setLeftBorderColor(HSSFColor.BLACK.index);
		style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style1.setRightBorderColor(HSSFColor.BLACK.index);
		style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style1.setTopBorderColor(HSSFColor.BLACK.index);
		//style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		style1.setWrapText(false);// 指定单元格自动换行



		// 产生表格标题行

		Row row = sheet.createRow(0);
		String [] testva= new String[headers.length];
		for (int i = 0; i < headers.length; i++) {
			testva[i]="888888888888888888";
			Cell cell = row.createCell(i);

			cell.setCellStyle(style);

			HSSFRichTextString text = new HSSFRichTextString(headers[i]);

			cell.setCellValue(text.toString());
			// sheet.autoSizeColumn(( short ) i );

		}

		// 遍历集合数据，产生数据行


		if (result.size() != 0) {

			int index = 1;
			for (Map map : result) {

				row = sheet.createRow(index);
				int cellIndex = 0;
				for (String key : listKey) {

					if(null ==testva[cellIndex])
					{
						testva[cellIndex]="0";
					}

					if(null !=map && null !=map.get(key))
					{
						Cell cell = row.createCell(cellIndex);
						String va=map.get(key).toString();
						if(va.getBytes().length >= testva[cellIndex].getBytes().length)
						{
							if(cellIndex!=0 && va.getBytes().length > testva[cellIndex-1].getBytes().length)
							{
								testva[cellIndex]=va;
							}

						}
						if(!StringUtils.isEmpty(va) && isNum(va) ){
							//是数字当作double处理
							if(va.length()>11)
							{
								cell.setCellValue(va);
								cell.setCellStyle(style1);
							}else
							{
								cell.setCellValue(Double.parseDouble(va));
								cell.setCellStyle(style1);

							}
						}else
						{
							cell.setCellValue(map.get(key).toString());
							cell.setCellStyle(style1);

						}

						// sheet.setColumnWidth(cellIndex,map.get(key).toString().getBytes().length*2*256);

					}else
					{
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
			// cell.setCellValue("无符合条件记录....");
		}

		for(int i=0;i<testva.length;i++)
		{
			String t=testva[i]+"88";
			//如果数据长度大于列宽允许设置的最大值,将列宽设置为默认值
			if(t.getBytes().length*256>62580){
				sheet.setColumnWidth(i,"888888888".getBytes().length*256);
			}else{
				sheet.setColumnWidth(i,t.getBytes().length*256);
			}
		}

	}
	public void exportExcel(Workbook workbook, int sheetNum, String sheetTitle, String tableTitle, String[] headers, List<Map<String,Object>> result, String[] listKey, String[] headers1, List<Map<String,Object>> result1, String[] listKey1) throws Exception{
		// 生成一个表格
		Sheet sheet = workbook.createSheet();
		workbook.setSheetName(sheetNum,sheetTitle);
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
		Font font = workbook.createFont();
		// font.setColor(HSSFColor.BLACK.index);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 230);
		style.setFont(font);
		CellStyle style1 = workbook.createCellStyle();
		style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单无格的边框为粗体
		style1.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
		style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style1.setLeftBorderColor(HSSFColor.BLACK.index);
		style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style1.setRightBorderColor(HSSFColor.BLACK.index);
		style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style1.setTopBorderColor(HSSFColor.BLACK.index);
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		style1.setWrapText(false);// 指定单元格自动换行

		//设置样式
		CellStyle titleStyle=workbook.createCellStyle();
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		//设置字体
		Font titleFont=workbook.createFont();
		titleFont.setFontHeightInPoints((short)15);
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titleFont.setFontName("宋体");
		titleStyle.setFont(titleFont);
		//合并单元格操作
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,(short)7));
		Row titleRow=null;
		Cell titleCell=null;
		titleRow=sheet.createRow(0);
		titleRow.setHeight((short) (2*256));
		titleCell=titleRow.createCell((short) 0);
		titleCell.setCellStyle(titleStyle);
		titleCell.setCellValue(new HSSFRichTextString(tableTitle));

		// 产生表格标题行
		Row row = sheet.createRow(1);
		String [] testva= new String[headers.length];
		for (int i = 0; i < headers.length; i++) {
			testva[i]="8888888";
			Cell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text.toString());
		}
		int rowNumber = 2;
		if (result.size() != 0) {
			int index = 2;
			for (Map map : result) {
				row = sheet.createRow(index);
				int cellIndex = 0;
				for (String key : listKey) {
					if(null ==testva[cellIndex])
					{
						testva[cellIndex]="0";
					}
					if(null !=map && null !=map.get(key))
					{
						Cell cell = row.createCell(cellIndex);
						String va=map.get(key).toString();
						if(va.getBytes().length >= testva[cellIndex].getBytes().length)
						{
							if(cellIndex!=0 && va.getBytes().length > testva[cellIndex-1].getBytes().length)
							{
								testva[cellIndex]=va;
							}
						}
						if(!StringUtils.isEmpty(va) && isNum(va) ){
							//是数字当作double处理
							if(va.length()>11)
							{
								cell.setCellValue(va);
								cell.setCellStyle(style1);
							}else
							{
								cell.setCellValue(Double.parseDouble(va));
								cell.setCellStyle(style1);

							}
						}else
						{
							cell.setCellValue(map.get(key).toString());
							cell.setCellStyle(style1);

						}
					}else
					{
						Cell cell = row.createCell(cellIndex);
						cell.setCellValue("");
						cell.setCellStyle(style1);
					}
					cellIndex++;
				}
				index++;
				rowNumber = index;
			}
		} else {
			// 如果内容为空，则提示无符合条件记录
			row = sheet.createRow(2);
			Cell cell = row.createCell(2);
			cell.setCellValue("无符合条件记录....");
		}

		//表格标题2
		Row row1 = sheet.createRow(rowNumber+1);
		String [] testva1= new String[headers1.length];
		for (int i = 0; i < headers1.length; i++) {
			testva1[i]="8888888";
			Cell cell = row1.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers1[i]);
			cell.setCellValue(text.toString());
		}
		if (result1.size() != 0) {
			int index = rowNumber+2;
			for (Map map : result1) {
				row = sheet.createRow(index);
				int cellIndex = 0;
				for (String key : listKey1) {
					if(null ==testva1[cellIndex])
					{
						testva1[cellIndex]="0";
					}
					if(null !=map && null !=map.get(key))
					{
						Cell cell = row.createCell(cellIndex);
						String va=map.get(key).toString();
						if(va.getBytes().length >= testva1[cellIndex].getBytes().length)
						{
							if(cellIndex!=0 && va.getBytes().length > testva1[cellIndex-1].getBytes().length)
							{
								testva1[cellIndex]=va;
							}
						}
						if(!StringUtils.isEmpty(va) && isNum(va) ){
							//是数字当作double处理
							if(va.length()>11)
							{
								cell.setCellValue(va);
								cell.setCellStyle(style1);
							}else
							{
								cell.setCellValue(Double.parseDouble(va));
								cell.setCellStyle(style1);

							}
						}else
						{
							cell.setCellValue(map.get(key).toString());
							cell.setCellStyle(style1);

						}
					}else
					{
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
		for(int i=0;i<testva.length;i++)
		{
			String t=testva[i]+"88";
			//如果数据长度大于列宽允许设置的最大值,将列宽设置为默认值
			if(t.getBytes().length*256>62580){
				sheet.setColumnWidth(i,"888888888".getBytes().length*256);
			}else{
				sheet.setColumnWidth(i,t.getBytes().length*256);
			}
		}
	}

	/**
	 * 关闭流
	 *
	 * @param aas
	 * @param os
	 */
	public void closeIO(ByteArrayInputStream aas,
						ByteArrayOutputStream os) {

		if (aas != null) {
			try {
				aas.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public  boolean isNum(String str){
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?)$");
	}

	/**
	 * <p>Description:导出两张表方法 </p>
	 * @param workbook
	 * @param sheetNum
	 * @param sheetTitle
	 * @param headers
	 * @param dataListUp
	 * @param dataListDown
	 * @param listKey
	 * @param referTime
	 * @param compareTime
	 */
	public void exportTwoSheetExcel(Workbook workbook, int sheetNum, String sheetTitle, String[] headers,
									List<Map> dataListUp, List<Map> dataListDown, String[] listKey, String referTime, String compareTime) {
		// 生成一个表格
		Sheet sheet = workbook.createSheet("上行");
		//生成第二张表
		Sheet sheet2 =  workbook.createSheet("下行");
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
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		style1.setWrapText(false);// 指定单元格自动换行

		//设置绿色字体
		CellStyle style3 = workbook.createCellStyle();
		style3.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单无格的边框为粗体
		style3.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
		style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style3.setLeftBorderColor(HSSFColor.BLACK.index);
		style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style3.setRightBorderColor(HSSFColor.BLACK.index);
		style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style3.setTopBorderColor(HSSFColor.BLACK.index);
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		style3.setWrapText(false);// 指定单元格自动换行
		// 生成一个字体
		Font font3 = workbook.createFont();
		font3.setColor(HSSFColor.BRIGHT_GREEN.index);
		style3.setFont(font3);

		//设置红色字体
		CellStyle style4 = workbook.createCellStyle();
		style4.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单无格的边框为粗体
		style4.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
		style4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style4.setLeftBorderColor(HSSFColor.BLACK.index);
		style4.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style4.setRightBorderColor(HSSFColor.BLACK.index);
		style4.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style4.setTopBorderColor(HSSFColor.BLACK.index);
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		style4.setWrapText(false);// 指定单元格自动换行
		// 生成一个字体
		Font font4 = workbook.createFont();
		font4.setColor(HSSFColor.RED.index);
		style4.setFont(font4);

		//设置标题行格式
		CellStyle style2 = workbook.createCellStyle();
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		// 把字体应用到当前的样式
		style2.setFont(font);
		// 产生表格标题行
		//第一张表
		Row firstRow = sheet.createRow(0);
		Cell cellTitle = firstRow.createCell(0);
		firstRow.setHeightInPoints((short) 20);
		// 设置表标题名
		cellTitle.setCellValue(sheetTitle + "-上行");
		// 指定合并区域
		sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 11));
		cellTitle.setCellStyle(style2);
		//第二张表
		Row firstRow2 = sheet2.createRow(0);
		Cell cellTitle2 = firstRow2.createCell(0);
		firstRow2.setHeightInPoints((short) 20);
		cellTitle2.setCellValue(sheetTitle + "-下行");
		// 指定合并区域
		sheet2.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 11));
		cellTitle2.setCellStyle(style2);

		//产生第二行
		Row secondRow = sheet.createRow(1);
		Cell secondRowCell = secondRow.createCell(2);
		secondRowCell.setCellValue("参照时间");
		secondRowCell = secondRow.createCell(3);
		secondRowCell.setCellValue(referTime);
		secondRowCell = secondRow.createCell(6);
		secondRowCell.setCellValue("比对时间");
		secondRowCell = secondRow.createCell(7);
		secondRowCell.setCellValue(compareTime);
		//产生第二行
		Row secondRow2 = sheet2.createRow(1);
		Cell secondRowCell2 = secondRow2.createCell(2);
		secondRowCell2.setCellValue("参照时间");
		secondRowCell2 = secondRow2.createCell(3);
		secondRowCell2.setCellValue(referTime);
		secondRowCell2 = secondRow2.createCell(6);
		secondRowCell2.setCellValue("比对时间");
		secondRowCell2 = secondRow2.createCell(7);
		secondRowCell2.setCellValue(compareTime);


		Row row = sheet.createRow(3);
		Row row2 = sheet2.createRow(3);
		String [] testva= new String[headers.length];
		for (int i = 0; i < headers.length; i++) {
			testva[i]="8888888";
			Cell cell = row.createCell(i);
			Cell cell2 = row2.createCell(i);
			cell.setCellStyle(style);
			cell2.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text.toString());
			cell2.setCellValue(text.toString());
		}

		// 遍历集合数据，产生数据行
		if (dataListUp.size() != 0) {
			int index = 4;
			for (Map map : dataListUp) {
				row = sheet.createRow(index);
				int cellIndex = 0;
				for (String key : listKey) {
					if(null ==testva[cellIndex]) {
						testva[cellIndex]="0";
					}
					if(null !=map && null !=map.get(key)) {
						Cell cell = row.createCell(cellIndex);
						String va=map.get(key).toString();

						//给浮动字段做修改
						if(cellIndex == 9) {
							Double floatValue=(Double)map.get(key) * 100;
							if(floatValue == 0) {
								cell.setCellValue(floatValue);
								cell.setCellStyle(style1);
							} else if(floatValue > 0 ) {
								if(floatValue.toString().length()>5) {
									va ="↑ " + floatValue.toString().substring(0,5) + "%";
								} else {
									va ="↑ " + floatValue.toString().substring(0,floatValue.toString().length()) + "%";
								}
								cell.setCellValue(va);
								cell.setCellStyle(style4);
							} else {
								if(floatValue.toString().length()>6) {
									va ="↓ " + floatValue.toString().substring(1,6) + "%";
								} else {
									va ="↓ " + floatValue.toString().substring(1,floatValue.toString().length()) + "%";
								}
								cell.setCellValue(va);
								cell.setCellStyle(style3);
							}
							cellIndex++;
							continue;
						} else if(cellIndex == 6) {
							Integer intValue= (Integer)map.get(key);
							if(intValue != null) {
								if(intValue < 10) {
									va = "0"+va+":00";
								} else {
									va = va + ":00";
								}
							}
							cell.setCellValue(va);
							cell.setCellStyle(style1);
							cellIndex++;
							continue;
						}else if(cellIndex==7){
							Double flowValue=(Double)map.get(key);
							if (flowValue>1024 &&flowValue<1024*1024){
								DecimalFormat df = new DecimalFormat("#.00");
								va = df.format(flowValue/1024);
								va=va+"Kbps";
							}else if(flowValue>1024*1024){
								DecimalFormat df = new DecimalFormat("#.00");
								va = df.format(flowValue/1024/1024);
								va=va+"Mbps";
							}else if(flowValue==0){
								va=va+"bps";
							}else{
								DecimalFormat df = new DecimalFormat("#.00");
								va = df.format(flowValue);
								va=va+"bps";
							}
							cell.setCellValue(va);
							cell.setCellStyle(style1);
							cellIndex++;
							continue;
						}else if(cellIndex==8){
							Double compareValue=(Double)map.get(key);
							if (compareValue>1024 &&compareValue<1024*1024){
								DecimalFormat df = new DecimalFormat("#.00");
								va = df.format(compareValue/1024);
								va=va+"Kbps";
							}else if(compareValue>1024*1024){
								DecimalFormat df = new DecimalFormat("#.00");
								va = df.format(compareValue/1024/1024);
								va=va+"Mbps";
							}else if(compareValue==0){
								va=va+"bps";
							}else{
								DecimalFormat df = new DecimalFormat("#.00");
								va = df.format(compareValue);
								va=va+"bps";
							}
							cell.setCellValue(va);
							cell.setCellStyle(style1);
							cellIndex++;
							continue;
						}

						if(va.getBytes().length >= testva[cellIndex].getBytes().length) {
							if(cellIndex!=0 && va.getBytes().length > testva[cellIndex-1].getBytes().length) {
								testva[cellIndex]=va;
							}
						}
						if(!StringUtils.isEmpty(va) && isNum(va) ) {
							//是数字当作double处理
							if(va.length()>11) {
								cell.setCellValue(va);
								cell.setCellStyle(style1);
							}else {
								cell.setCellValue(Double.parseDouble(va));
								cell.setCellStyle(style1);
							}
						}else {
							cell.setCellValue(map.get(key).toString());
							if(cellIndex == 10) {
								if(map.get(key).toString().equals("正常")) {
									cell.setCellStyle(style3);
								} else {
									cell.setCellStyle(style4);
								}
							} else {
								cell.setCellStyle(style1);
							}
						}
					}else {
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
			row = sheet.createRow(4);
			Cell cell = row.createCell(2);
			cell.setCellValue("无符合条件记录....");
		}

		// 遍历集合数据，产生数据行
		if (dataListDown.size() != 0) {
			int index = 4;
			for (Map map : dataListDown) {
				row = sheet2.createRow(index);
				int cellIndex = 0;
				for (String key : listKey) {
					if(null ==testva[cellIndex]) {
						testva[cellIndex]="0";
					}
					if(null !=map && null !=map.get(key)) {
						Cell cell = row.createCell(cellIndex);
						String va=map.get(key).toString();
						//给浮动字段做修改
						if(cellIndex == 9) {
							Double floatValue=(Double)map.get(key) * 100;
							if(floatValue == 0) {
								cell.setCellValue(floatValue);
								cell.setCellStyle(style1);
							} else if(floatValue > 0 ) {
								if(floatValue.toString().length()>5) {
									va ="↑ " + floatValue.toString().substring(0,5) + "%";
								} else {
									va ="↑ " + floatValue.toString().substring(0,floatValue.toString().length()) + "%";
								}
								cell.setCellValue(va);
								cell.setCellStyle(style4);
							} else {
								if(floatValue.toString().length()>6) {
									va ="↓ " + floatValue.toString().substring(1,6) + "%";
								} else {
									va ="↓ " + floatValue.toString().substring(1,floatValue.toString().length()) + "%";
								}
								cell.setCellValue(va);
								cell.setCellStyle(style3);
							}
							cellIndex++;
							continue;
						}
						else if(cellIndex == 6) {
							Integer intValue= (Integer)map.get(key);
							if(intValue != null) {
								if(intValue < 10) {
									va = "0"+va+":00";
								} else {
									va = va + ":00";
								}
							}
							cell.setCellValue(va);
							cell.setCellStyle(style1);
							cellIndex++;
							continue;
						}else if(cellIndex==7){
							Double flowValue=(Double)map.get(key);
							if (flowValue>1024 &&flowValue<1024*1024){
								DecimalFormat df = new DecimalFormat("#.00");
								va = df.format(flowValue/1024);
								va=va+"Kbps";
							}else if(flowValue>1024*1024){
								DecimalFormat df = new DecimalFormat("#.00");
								va = df.format(flowValue/1024/1024);
								va=va+"Mbps";
							}else if(flowValue==0){
								va=va+"bps";
							}else{
								DecimalFormat df = new DecimalFormat("#.00");
								va = df.format(flowValue);
								va=va+"bps";
							}
							cell.setCellValue(va);
							cell.setCellStyle(style1);
							cellIndex++;
							continue;
						}else if(cellIndex==8){
							Double compareValue=(Double)map.get(key);
							if (compareValue>1024 &&compareValue<1024*1024){
								DecimalFormat df = new DecimalFormat("#.00");
								va = df.format(compareValue/1024);
								va=va+"Kbps";
							}else if(compareValue>1024*1024){
								DecimalFormat df = new DecimalFormat("#.00");
								va = df.format(compareValue/1024/1024);
								va=va+"Mbps";
							}else if(compareValue==0){
								va=va+"bps";
							}else{
								DecimalFormat df = new DecimalFormat("#.00");
								va = df.format(compareValue);
								va=va+"bps";
							}
							cell.setCellValue(va);
							cell.setCellStyle(style1);
							cellIndex++;
							continue;
						}
						if(va.getBytes().length >= testva[cellIndex].getBytes().length) {
							if(cellIndex!=0 && va.getBytes().length > testva[cellIndex-1].getBytes().length) {
								testva[cellIndex]=va;
							}
						}
						if(!StringUtils.isEmpty(va) && isNum(va) ) {
							//是数字当作double处理
							if(va.length()>11) {
								cell.setCellValue(va);
								cell.setCellStyle(style1);
							}else {
								cell.setCellValue(Double.parseDouble(va));
								cell.setCellStyle(style1);

							}
						}else {
							cell.setCellValue(map.get(key).toString());
							if(cellIndex == 10) {
								if(map.get(key).toString().equals("正常")) {
									cell.setCellStyle(style3);
								} else {
									cell.setCellStyle(style4);
								}
							} else {
								cell.setCellStyle(style1);
							}

						}
					}else {
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
			row = sheet2.createRow(4);
			Cell cell = row.createCell(2);
			cell.setCellValue("无符合条件记录....");
		}

		for(int i=0;i<testva.length;i++) {
			String t=testva[i]+"88";
			//如果数据长度大于列宽允许设置的最大值,将列宽设置为默认值
			if(t.getBytes().length*256>62580){
				sheet.setColumnWidth(i,"888888888".getBytes().length*256);
				sheet2.setColumnWidth(i,"888888888".getBytes().length*256);
			}else{
				sheet.setColumnWidth(i,t.getBytes().length*256);
				sheet2.setColumnWidth(i,t.getBytes().length*256);
			}
		}
	}

	/**
	 * 功能：双层表格导出
	 * @param workbook excel表格实例
	 * @param sheetNum sheet位置，从0开始
	 * @param sheetTitle sheet名称
	 * @param headers1   //第一行表格
	 * @param headers2   //第二行表格
	 * @param headNum    //需要合并单元格位置，样例0;1;0;0,0;1;1;1
	 * @param result	 //结果集
	 * @param listKey    //字段名列表
	 * @throws Exception
	 */

	public void exportDouRowExcel(Workbook workbook, int sheetNum, String sheetTitle, String[] headers1, String[] headers2, String[] headNum, List<Map> result, String[] listKey ) throws Exception{

		// 生成一个表格

		Sheet sheet = workbook.createSheet();

		workbook.setSheetName(sheetNum,sheetTitle);
		// 生成一个样式
		CellStyle style = workbook.createCellStyle();

		// 设置样式
		//style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
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
		style.setFont(font);

		CellStyle style1 = workbook.createCellStyle();
		style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单无格的边框为粗体
		style1.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
		style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style1.setLeftBorderColor(HSSFColor.BLACK.index);
		style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style1.setRightBorderColor(HSSFColor.BLACK.index);
		style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style1.setTopBorderColor(HSSFColor.BLACK.index);
		//style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		style1.setWrapText(false);// 指定单元格自动换行
		// 产生表格标题行
		Row row = sheet.createRow(0);
		String [] testva= new String[headers1.length];
		for (int i = 0; i < headers1.length; i++) {
			testva[i]="8888888";
			Cell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers1[i]);
			cell.setCellValue(text.toString());
		}
		row = sheet.createRow(1);
		for (int i = 0; i < headers2.length; i++) {
			testva[i]="8888888";
			Cell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers2[i]);
			cell.setCellValue(text.toString());
		}
		//动态合并单元格
		for (int i = 0; i < headNum.length; i++) {
			String[] temp = headNum[i].split(";");
			Integer startrow = Integer.parseInt(temp[0]);
			Integer overrow = Integer.parseInt(temp[1]);
			Integer startcol = Integer.parseInt(temp[2]);
			Integer overcol = Integer.parseInt(temp[3]);
			sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
					startcol, overcol));
		}
		// 遍历集合数据，产生数据行
		if (result!=null && result.size() != 0) {

			int index = 2;
			for (Map map : result) {

				row = sheet.createRow(index);
				int cellIndex = 0;
				for (String key : listKey) {

					if(null ==testva[cellIndex])
					{
						testva[cellIndex]="0";
					}

					if(null !=map && null !=map.get(key))
					{
						Cell cell = row.createCell(cellIndex);
						String va=map.get(key).toString();
						if(va.getBytes().length >= testva[cellIndex].getBytes().length)
						{
							if(cellIndex!=0 && va.getBytes().length > testva[cellIndex-1].getBytes().length)
							{
								testva[cellIndex]=va;
							}

						}
						if(!StringUtils.isEmpty(va) && isNum(va) ){
							//是数字当作double处理
							if(va.length()>11)
							{
								cell.setCellValue(va);
								cell.setCellStyle(style1);
							}else
							{
								cell.setCellValue(Double.parseDouble(va));
								cell.setCellStyle(style1);

							}
						}else
						{
							cell.setCellValue(map.get(key).toString());
							cell.setCellStyle(style1);

						}
					}else
					{
						Cell cell = row.createCell(cellIndex);
						cell.setCellValue("");
						cell.setCellStyle(style1);
					}
					cellIndex++;
				}
				index++;
			}

		}

		for(int i=0;i<testva.length;i++)
		{
			String t=testva[i]+"88";
			//如果数据长度大于列宽允许设置的最大值,将列宽设置为默认值
			if(t.getBytes().length*256>62580){
				sheet.setColumnWidth(i,"888888888".getBytes().length*256);
			}else{
				sheet.setColumnWidth(i,t.getBytes().length*256);
			}
		}

	}

	public static Map<String, Object> objectToMap(Object obj) throws Exception {
		if(obj == null){
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			map.put(field.getName(), field.get(obj));
		}
		return map;
	}


}
