package com.aspire.ums.cmdb.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class POICascadeUtils2 {  
	
     	private static String EXCEL_HIDE_SHEET_NAME = "excelhidesheetname";
     	
        private HashMap map = new HashMap();    
        //设置下拉列表的内容       
       
        private static String[]  headerLists;
        private static List<List<Object>> result;
        
        private static List<String> hideString;
        private static int tempSize;
        private static int tempSize1;
        private static String[] tempRow =null;
        
  
        public static void  createExcelMo(Workbook wb, String title){
                Sheet sheet = wb.createSheet(title);
                // 生成一个样式  
                CellStyle style = wb.createCellStyle();
                
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
                 //style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐 
                 style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
                 style1.setWrapText(false);// 指定单元格自动换行 

                
              // 产生表格标题行  

                 Row row = sheet.createRow(0);
                 String [] testva= new String[headerLists.length];
                 for (int i = 0; i < headerLists.length; i++) {  
                 	 testva[i]="888888888888888888";//表格宽度
                     Cell cell = row.createCell(i);

                     cell.setCellStyle(style);  

                     HSSFRichTextString text = new HSSFRichTextString(headerLists[i]);

                     cell.setCellValue(text.toString());
                 }
                 for(int i=0;i<testva.length;i++)
         		{//改变列的宽度
         			String t=testva[i]+"88888888";
         			//如果数据长度大于列宽允许设置的最大值,将列宽设置为默认值
         			if(t.getBytes().length*256>62580){
         				sheet.setColumnWidth(i,"888888888".getBytes().length*256);
         			}else{
         				sheet.setColumnWidth(i,t.getBytes().length*256);
         			}						
         		}	
        }     
        
        
        
        public static void  createExcelMoToResourceCollect(Workbook wb, String title){
            Sheet sheet = wb.createSheet(title);
            // 生成一个样式  
            CellStyle style = wb.createCellStyle();
            
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
             //style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐 
             style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
             style1.setWrapText(false);// 指定单元格自动换行 

            
          // 产生表格标题行  
             //创建第一行标题行
             Cell cell = null;
             Row row = null;
             row = sheet.createRow(0);
             sheet.addMergedRegion(new CellRangeAddress(0,1,0,0));
             cell = row.createCell(0);
             cell.setCellValue("一级部门");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
             cell = row.createCell(1);
             cell.setCellValue("二级部门");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
             cell = row.createCell(2);
             cell.setCellValue("应用系统");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0,1, 3, 3));
             cell = row.createCell(3);
             cell.setCellValue("是否立项");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
             cell = row.createCell(4);
             cell.setCellValue("立项时间");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));
             cell = row.createCell(5);
             cell.setCellValue("资源需求提出时间");
             cell.setCellStyle(style);
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 6, 6));
             cell = row.createCell(6);
             cell.setCellValue("资源预期投产时间");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 7, 7));
             cell = row.createCell(7);
             cell.setCellValue("系统描述");
             cell.setCellStyle(style);
             
             
             
             sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 15));
             cell = row.createCell(8);
             cell.setCellStyle(style);
             cell.setCellValue("虚拟机");
             cell = row.createCell(9);
             cell.setCellStyle(style);
             cell = row.createCell(10);
             cell.setCellStyle(style);
             cell = row.createCell(11);
             cell.setCellStyle(style);
             cell = row.createCell(12);
             cell.setCellStyle(style);
             cell = row.createCell(13);
             cell.setCellStyle(style);
             cell = row.createCell(14);
             cell.setCellStyle(style);
             cell = row.createCell(15);
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(0, 0, 16, 20));
             
             cell = row.createCell(16);
             cell.setCellValue("物理机");
             cell.setCellStyle(style);
             cell = row.createCell(17);
             cell.setCellStyle(style);
             cell = row.createCell(18);
             cell.setCellStyle(style);
             cell = row.createCell(19);
             cell.setCellStyle(style);
             cell = row.createCell(20);
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 21, 21));
             cell = row.createCell(21);
             cell.setCellValue("分布式文件存储(TB)");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 22, 22));
             cell = row.createCell(22);
             cell.setCellValue("分布式块存储(TB)");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 23, 23));
             cell = row.createCell(23);
             cell.setCellValue("对象存储(TB)");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 24, 24));
             cell = row.createCell(24);
             cell.setCellValue("FC-SAN存储(TB)");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 25, 25));
             cell = row.createCell(25);
             cell.setCellValue("IP-SAN存储(TB)");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 26, 26));
             cell = row.createCell(26);
             cell.setCellValue("FC-SAN裸盘(TB)");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 27, 27));
             cell = row.createCell(27);
             cell.setCellValue("IP-SAN裸盘(TB)");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 28, 28));
             cell = row.createCell(28);
             cell.setCellValue("备份存储(TB)");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 29, 29));
             cell = row.createCell(29);
             cell.setCellValue("CMNET地址需求(个");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 30, 30));
             cell = row.createCell(30);
             cell.setCellValue("CMNET带宽资源需求(Gbps)");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 31, 31));
             cell = row.createCell(31);
             cell.setCellValue("IP地址需求(个)");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 32, 32));
             cell = row.createCell(32);
             cell.setCellValue("至IP专网带宽(Gbps)");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 0, 33, 37));
             cell = row.createCell(33);
             cell.setCellValue("数据库");
             cell.setCellStyle(style);
             cell.setCellStyle(style);
             cell = row.createCell(34);
             cell.setCellStyle(style);
             cell = row.createCell(35);
             cell.setCellStyle(style);
             cell = row.createCell(36);
             cell.setCellStyle(style);
             cell = row.createCell(37);
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(0, 0, 38, 39));
             cell = row.createCell(38);
             cell.setCellValue("分布式缓存");
             cell.setCellStyle(style);
             cell = row.createCell(39);
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 0, 40, 42));
             cell = row.createCell(40);
             cell.setCellValue("消息中间件");
             cell.setCellStyle(style);
             cell = row.createCell(41);
             cell.setCellStyle(style);
             cell = row.createCell(42);
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(0, 0, 43, 45));
             cell = row.createCell(43);
             cell.setCellValue("应用中间件");
             cell.setCellStyle(style);
             cell = row.createCell(44);
             cell.setCellStyle(style);
             cell = row.createCell(45);
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(0, 0, 46, 47));
             cell = row.createCell(46);
             cell.setCellValue("负载均衡");
             cell.setCellStyle(style);
             cell = row.createCell(47);
             cell.setCellStyle(style);
             

             sheet.addMergedRegion(new CellRangeAddress(0, 0, 48, 49));
             cell = row.createCell(48);
             cell.setCellValue("分布式协调服务");
             cell.setCellStyle(style);
             cell = row.createCell(49);
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(0, 0, 50, 51));
             cell = row.createCell(50);
             cell.setCellValue("搜索中间件");
             cell.setCellStyle(style);
             cell = row.createCell(51);
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 0, 52, 53));
             cell = row.createCell(52);
             cell.setCellValue("工作流");
             cell.setCellStyle(style);
             cell = row.createCell(53);
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(0, 0, 54, 59));
             cell = row.createCell(54);
             cell.setCellValue("工作流");
             cell.setCellStyle(style);
             cell = row.createCell(55);
             cell.setCellStyle(style);
             cell = row.createCell(56);
             cell.setCellStyle(style);
             cell = row.createCell(57);
             cell.setCellStyle(style);
             cell = row.createCell(58);
             cell.setCellStyle(style);
             cell = row.createCell(59);
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 0, 54, 59));
             cell = row.createCell(54);
             cell.setCellValue("语言类基础镜像");
             cell.setCellStyle(style);
             cell = row.createCell(55);
             cell.setCellStyle(style);
             cell = row.createCell(56);
             cell.setCellStyle(style);
             cell = row.createCell(57);
             cell.setCellStyle(style);
             cell = row.createCell(58);
             cell.setCellStyle(style);
             cell = row.createCell(59);
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 0, 60, 60));
             cell = row.createCell(60);
             cell.setCellValue("CI/CD");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 0, 61, 62));
             cell = row.createCell(61);
             cell.setCellValue("开发框架");
             cell.setCellStyle(style);
             cell = row.createCell(62);
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 63, 63));
             cell = row.createCell(63);
             cell.setCellValue("与前期上报需求有无变化，如有，请说明原因");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 64, 64));
             cell = row.createCell(64);
             cell.setCellValue("物理局址要求");
             cell.setCellStyle(style);
             
            
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 65, 65));
             cell = row.createCell(65);
             cell.setCellValue("部门负责人");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 66, 66));
             cell = row.createCell(66);
             cell.setCellValue("接口联系人");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 67, 67));
             cell = row.createCell(67);
             cell.setCellValue("联系人电话");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(0, 1, 68, 68));
             cell = row.createCell(68);
             cell.setCellValue("联系人邮箱");
             cell.setCellStyle(style);
             
             
             row = sheet.createRow(1);
             sheet.addMergedRegion(new CellRangeAddress(1,1,8,8));
             cell = row.createCell(8);
             cell.setCellValue("16核、128G、200G系统磁盘");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,9,9));
             cell = row.createCell(9);
             cell.setCellValue("8核、64G、200G系统磁盘");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,10,10));
             cell = row.createCell(10);
             cell.setCellValue("8核、32G、200G系统磁盘");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,11,11));
             cell = row.createCell(11);
             cell.setCellValue("8核、16G、200G系统磁盘");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,12,12));
             cell = row.createCell(12);
             cell.setCellValue("4核、32G、200G系统磁盘");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,13,13));
             cell = row.createCell(13);
             cell.setCellValue("4核、16G、200G系统磁盘");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,14,14));
             cell = row.createCell(14);
             cell.setCellValue("4核、8G、200G系统磁盘");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,15,15));
             cell = row.createCell(15);
             cell.setCellValue("2核、4G、200G系统磁盘");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,16,16));
             cell = row.createCell(16);
             cell.setCellValue("虚拟机宿主机/低端应用服务器");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,17,17));
             cell = row.createCell(17);
             cell.setCellValue("分析型服务器（MPP服务器）（台）");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,18,18));
             cell = row.createCell(18);
             cell.setCellValue("分布式服务器（Hadoop服务器）（台）");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,19,19));
             cell = row.createCell(19);
             cell.setCellValue("缓存型服务器（台）");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,20,20));
             cell = row.createCell(20);
             cell.setCellValue("高端应用服务器（台）");
             cell.setCellStyle(style);
             
             
            
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,33,33));
             cell = row.createCell(33);
             cell.setCellValue("mysql");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,34,34));
             cell = row.createCell(34);
             cell.setCellValue("MongoDB");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,35,35));
             cell = row.createCell(35);
             cell.setCellValue("PostgreSQL");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,36,36));
             cell = row.createCell(36);
             cell.setCellValue("内存数据库");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,37,37));
             cell = row.createCell(37);
             cell.setCellValue("其它");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,38,38));
             cell = row.createCell(38);
             cell.setCellValue("Redis");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,39,39));
             cell = row.createCell(39);
             cell.setCellValue("Memcached");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,40,40));
             cell = row.createCell(40);
             cell.setCellValue("ActiveMQ");
             cell.setCellStyle(style);
             
            
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,41,41));
             cell = row.createCell(41);
             cell.setCellValue("Kafka");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,42,42));
             cell = row.createCell(42);
             cell.setCellValue("RocketMQ");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,43,43));
             cell = row.createCell(43);
             cell.setCellValue("Apache HTTP Server");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,44,44));
             cell = row.createCell(44);
             cell.setCellValue("Jboos EAP");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,45,45));
             cell = row.createCell(45);
             cell.setCellValue("Tomcat");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,46,46));
             cell = row.createCell(46);
             cell.setCellValue("Nginx");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,47,47));
             cell = row.createCell(47);
             cell.setCellValue("Haproxy");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,48,48));
             cell = row.createCell(48);
             cell.setCellValue("Zookeeper");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,49,49));
             cell = row.createCell(49);
             cell.setCellValue("ETCD");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,50,50));
             cell = row.createCell(50);
             cell.setCellValue("Elasticsearch");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,51,51));
             cell = row.createCell(51);
             cell.setCellValue("DockerRegistry");
             cell.setCellStyle(style);
             
             
             
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,52,52));
             cell = row.createCell(52);
             cell.setCellValue("JBPM");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,53,53));
             cell = row.createCell(53);
             cell.setCellValue("activity");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,54,54));
             cell = row.createCell(54);
             cell.setCellValue("openjdk");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,55,55));
             cell = row.createCell(55);
             cell.setCellValue("Python");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,56,56));
             cell = row.createCell(56);
             cell.setCellValue("Go");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,57,57));
             cell = row.createCell(57);
             cell.setCellValue("Node.js");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,58,58));
             cell = row.createCell(58);
             cell.setCellValue("Ruby / Ruby on Rails");
             cell.setCellStyle(style);
             
             
             
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,59,59));
             cell = row.createCell(59);
             cell.setCellValue(".NET Core");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,60,60));
             cell = row.createCell(60);
             cell.setCellValue("Jenkins");
             cell.setCellStyle(style);
             
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,61,61));
             cell = row.createCell(61);
             cell.setCellValue("Spring Cloud");
             cell.setCellStyle(style);
             
             sheet.addMergedRegion(new CellRangeAddress(1,1,62,62));
             cell = row.createCell(62);
             cell.setCellValue("其它");
             cell.setCellStyle(style);
             
            
             
             
             
            

             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             

             
             
             
             
             
             
//             String [] testva= new String[headerLists.length];
//             for (int i = 0; i < headerLists.length; i++) {  
//             	 testva[i]="888888888888888888";//表格宽度
//                 Cell cell = row.createCell(i);  
//
//                 cell.setCellStyle(style);  
//
//                 HSSFRichTextString text = new HSSFRichTextString(headerLists[i]);  
//
//                 cell.setCellValue(text.toString());
//             }
//             for(int i=0;i<testva.length;i++)
//     		{//改变列的宽度
//     			String t=testva[i]+"88888888";
//     			//如果数据长度大于列宽允许设置的最大值,将列宽设置为默认值
//     			if(t.getBytes().length*256>62580){
//     				sheet.setColumnWidth(i,"888888888".getBytes().length*256);
//     			}else{
//     				sheet.setColumnWidth(i,t.getBytes().length*256);
//     			}						
//     		}	
    }

        public static void  createExcelMoToBatchUpdate(Workbook wb, String title, String[] headerLists){
         Sheet sheet = wb.createSheet(title);
         // 生成一个样式
         CellStyle style = wb.createCellStyle();

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
         //style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
         style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
         style1.setWrapText(false);// 指定单元格自动换行


         // 产生表格标题行
         //创建第一行标题行
         for (int i = 0 ;i < headerLists.length; i++) {
          Cell cell = null;
          Row row = null;
          row = sheet.createRow(i);
          sheet.setColumnWidth(0, 20 * 256);
          sheet.addMergedRegion(new CellRangeAddress(0,0,0,0));
          cell = row.createCell(0);
          cell.setCellValue(headerLists[i]);
          cell.setCellStyle(style);
         }


 }



 public void utilss(Workbook wb, List<List<Object>>results, String[] headerList, String title){
        	result=results;
        	headerLists=headerList;
            // 创建标题栏  
            createExcelMo(wb,title);    
            //创建隐藏的Sheet页  
            creatExcelHidePage(wb);       
            //数据验证  
            setDataValidation(wb);     
        }
        
        
        
        public void resourceUtilss(Workbook wb, List<List<Object>>results, String[] headerList, String title){
        	result=results;
        	headerLists=headerList;
            // 创建标题栏
        	createExcelMoToResourceCollect(wb,title);
            //创建隐藏的Sheet页
            //creatExcelHidePage(wb);
            //数据验证
           // setDataValidation(wb);
        }

         public void repertryInstanceUtilss(Workbook wb, List<List<Object>>results, String[] headerList, String title){
          result=results;
          headerLists=headerList;
          // 创建标题栏
          createExcelMoToBatchUpdate(wb, title, headerList);
          //创建隐藏的Sheet页
          //creatExcelHidePage(wb);
          //数据验证
          // setDataValidation(wb);
         }

        //result:{BUSINESS_LEVEL1=测试中心, BUSINESS_LEVEL2=[云测试平台, 众测平台]}
        //headerList:第一行标题
        //title:资产信息表模板
        public void cmdbUtilss(Workbook wb, List<List<Object>>results, String[] headerList, String title){
        	result=results;
        	headerLists=headerList;
            // 创建标题栏  
            createExcelMo(wb,title);    
            //创建隐藏的Sheet页  
            creatExcelHidePageForCmdb(wb);      
            //数据验证  
           setDataValidationForCmdb(wb);     
                
        }
        
        
        
        
        /**           
         * 设置模板文件的横向表头单元格的样式    
         * @param wb    
         * @return    
         */      
        private static CellStyle getTitleStyle(Workbook wb){
            CellStyle style = wb.createCellStyle();
            //对齐方式设置                  
            style.setAlignment(CellStyle.ALIGN_CENTER);
            //边框颜色和宽度设置                  
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setRightBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setTopBorderColor(IndexedColors.BLACK.getIndex());
            style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            //设置背景颜色       
            style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            //粗体字设置       
            Font font = wb.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            style.setFont(font);       
            return style;       
        }      
        /**    
         * 设置模板文件的横向表头单元格的样式    
         * @param wb
         * @return    
         */     
        public static void creatExcelHidePage(Workbook workbook){
            Sheet hideInfoSheet = workbook.createSheet(EXCEL_HIDE_SHEET_NAME);//隐藏一些信息
            //在隐藏页设置选择信息       
           
            //i需要我们手动控制
            int L=0;
            ArrayList<String> arrayList = new ArrayList<>();
            for (int i = 0; i < result.size(); i++) {
            	if (i!=0&&i!=1&&i!=5) { //此判断用于排除联动，但不排除有下拉数据和无数据
            		Row row = hideInfoSheet.createRow(L);
            		String[] rowS = (String[]) result.get(i).get(0);
               	    creatRow(row, rowS);
	               	 String rowString = (String) result.get(i).get(1);
	           		 arrayList.add(rowString);
	           		 hideString=arrayList;
               	    L++;
				} else  {
					Row row = hideInfoSheet.createRow(L);
					String[] rowS = (String[]) result.get(i).get(0);
               	    creatRow(row, rowS);
	               	 String rowString = (String) result.get(i).get(1);
	           		 arrayList.add(rowString);
	           		 hideString=arrayList;
	           		creatExcelNameList(workbook, rowString, L+1, rowS.length, true); 
	           		L++;
				}
          
            }
            
            //设置隐藏页标志       
            workbook.setSheetHidden(workbook.getSheetIndex(EXCEL_HIDE_SHEET_NAME), true);       
        }      
         
        public static void creatExcelHidePageForCmdb(Workbook workbook){
            Sheet hideInfoSheet = workbook.createSheet(EXCEL_HIDE_SHEET_NAME);//设置隐藏  页的标题
            //在隐藏页设置选择信息       
           
            //有些i需要我们手动控制
            int L=0;
            ArrayList<String> arrayList = new ArrayList<>();//把所有标题
            for (int i = 0; i < result.size(); i++) {
            	
            	if (i==0||i>=17&&i<=51) { //此判断用于  无下拉框的列
            		Row row = hideInfoSheet.createRow(L); //创建一行
            		String[] rowS = (String[]) result.get(i).get(0);//把预先数据转成字符串数组
               	    creatRow(row, rowS);//把字符串数组，遍历，设置列的内容
	               	 String rowString = (String) result.get(i).get(1);//获取标题
	           		 arrayList.add(rowString);
	           		 hideString=arrayList;
               	    L++;
				} else if (i==1) {//联动的开头者  indirect()
					Row row = hideInfoSheet.createRow(L);
					tempRow = (String[]) result.get(i).get(0);
               	    creatRow(row, tempRow);
	               	 String rowString = (String) result.get(i).get(1);
	           		 arrayList.add(rowString);
	           		 hideString=arrayList;
	           		creatExcelNameList(workbook, rowString, L+1, tempRow.length, false); 
	           		L++;
				}
            	else if(i==2) {//联动关联者
            		 
            		int k=0;
					List<Map<String, Object>> business = (List<Map<String, Object>>) result.get(i).get(0);
					for (Map<String, Object> map : business) {
						ArrayList<String> business2List = (ArrayList<String>) map.get("BUSINESS_LEVEL2");
         				String[] business2 = (String[]) business2List.toArray(new String[0]); 
         				Row row = hideInfoSheet.createRow(L);
         				creatRow(row, business2);
         				for (int j = k; j <tempRow.length; j++) {
         					String rowName=tempRow[j];
         					if (rowName.matches("[0-9]+.*")) {
         						rowName="temp"+rowName;
							}
         					 arrayList.add(rowName);
         					hideString=arrayList;
                      		creatExcelNameList(workbook,rowName, L+1, business2.length, true);
                      		L++;
                      		k++;
                      		break;
						}
         				
                  		
					}
					//把临时数据置空
					tempRow=null;
				}else if (i>=3&&i<6 ) {
					tempSize=L;
					Row row = hideInfoSheet.createRow(L);
					String[] rowS = (String[]) result.get(i).get(0);
               	    creatRow(row, rowS);
	               	 String rowString = (String) result.get(i).get(1);
	           		 arrayList.add(rowString);
	           		 hideString=arrayList;
	           		creatExcelNameList(workbook, rowString, L+1, rowS.length, false); 
	           		L++;
				}else if (i==6) {
					Row row = hideInfoSheet.createRow(L);
					tempRow = (String[]) result.get(i).get(0);
               	    creatRow(row, tempRow);
	               	 String rowString = (String) result.get(i).get(1);
	           		 arrayList.add(rowString);
	           		 hideString=arrayList;
	           		creatExcelNameList(workbook, rowString, L+1, tempRow.length, false); 
	           		L++;
	           	
				}else if (i==7) {
              		int k=0;
    				List<Map<String, Object>> device = (List<Map<String, Object>>) result.get(i).get(0);
    				for (Map<String, Object> map : device) {
    					List<String> deviceLists = (ArrayList<String>) map.get("DEVICE_TYPE");
         				String[] devices = (String[]) deviceLists.toArray(new String[0]); 
         				Row row = hideInfoSheet.createRow(L);
         				creatRow(row, devices);
         				for (int j = k; j <tempRow.length; j++) {
         					String rowName=tempRow[j];
         					arrayList.add(rowName);
         					hideString=arrayList;
                      		creatExcelNameList(workbook,rowName, L+1, devices.length, true);
                      		L++;
                      		k++;
                      		break;
					}
         				
				}
    				//把临时数据置空
    				tempRow=null;
			}else if (i==8) {
				List<Map<String, Object>> model = (List<Map<String, Object>>) result.get(i).get(0);
				for (Map<String, Object> map : model) {
					List<String> deviceLists = (ArrayList<String>) map.get("DEVICE_TYPE");
					List<String> modelLists = (ArrayList<String>) map.get("DEVICE_MODEL");
					String[] models = (String[]) modelLists.toArray(new String[0]); 
					String[] devices = (String[]) deviceLists.toArray(new String[0]); 
     				for (int m = 0; m < devices.length; m++) {
     					Row row = hideInfoSheet.createRow(L);
         				creatRow(row, models);
     					String rowName=devices[m];
     					//存在重复名称的时候，动态为其起新名字
     					if (arrayList.contains(rowName)) {
     						rowName=rowName+"repeat"+L;
						}
     					
     					arrayList.add(rowName);
     					hideString=arrayList;
                  		creatExcelNameList(workbook,rowName, L+1, models.length, true); //一个名称对应一个下拉框数据
                  		L++;
				}
			}
				
		} else if (i>=9&&i<=16) {
			tempSize1=L;
       		Row row = hideInfoSheet.createRow(L);
			String[] rowS = (String[]) result.get(i).get(0);
       	    creatRow(row, rowS);
           	 String rowString = (String) result.get(i).get(1);
       		 arrayList.add(rowString);
       		 hideString=arrayList;
       		creatExcelNameList(workbook, rowString, L+1, rowS.length, false); 
       		L++;
		}
          
            }
            
            //设置隐藏页标志       
            workbook.setSheetHidden(workbook.getSheetIndex(EXCEL_HIDE_SHEET_NAME), true);  //可以设置为false,可以查看隐藏sheet的内容排列
        } 
        
        
        
        
        
        /**    
         * 创建一个名称    
         * @param workbook    
         */      
        private static void creatExcelNameList(Workbook workbook, String nameCode, int order, int size, boolean cascadeFlag){
            Name name;
            name = workbook.createName();       
            name.setNameName(nameCode);       
            name.setRefersToFormula(EXCEL_HIDE_SHEET_NAME+"!"+creatExcelNameList(order,size,cascadeFlag));      
        }       
            
        /**    
         * 名称数据行列计算表达式    
         * @param workbook
         */      
        private static String creatExcelNameList(int order, int size, boolean cascadeFlag) {
        	char start = 'A';
        	if (cascadeFlag) {
        	if (size <= 26) {//曾经是《=25
        	char end = (char) (start + size - 1);
        	return "$" + start + "$" + order + ":$" + end + "$" + order;
        	} else {
        	char endPrefix = 'A';
        	char endSuffix = 'A';
        	if ((size - 25) / 26 == 0 || size == 51) {//26-51之间，包括边界（仅两次字母表计算）
        	if ((size - 25) % 26 == 0 ) {//边界值
        	endSuffix = (char) ('A' + 25);
        	} else {
        		endSuffix = (char) ('A' + (size - 25) % 26 - 2);
            }
        	} else {//51以上   
        	if ((size - 25) % 26 == 0 || (size - 25) % 26 == 1) {
        	endSuffix = (char) ('A' + 25);
        	endPrefix = (char) (endPrefix + (size - 25) / 26 - 1);
        	} else {
        		
        			endSuffix = (char) ('A' + (size - 25) % 26 - 2);
				
        	endPrefix = (char) (endPrefix + (size - 25) / 26);
        	}
        	}
        	String s="$" + start + "$" + order + ":$" + endPrefix + endSuffix + "$" + order;
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
			/*char start = 'A';
			if (cascadeFlag) {
			if (size <= 25) {
			char end = (char) (start + size - 1);
			String s ="$" + start + "$" + order + ":$" + end + "$" + order;
			return s;
			} else {
			char endPrefix = 'A';
			char endSuffix = 'A';
			if ((size - 25) / 26 == 0 || size == 51) {//26-51之间，包括边界（仅两次字母表计算）   
			if ((size - 25) % 26 == 0) {//边界值   
			endSuffix = (char) ('A' + 25);
			} else {
			endSuffix = (char) ('A' + (size - 25) % 26 - 1);
			}
			} else {//51以上   
			if ((size - 25) % 26 == 0) {
			endSuffix = (char) ('A' + 25);
			endPrefix = (char) (endPrefix + (size - 25) / 26 - 1);
			} else {
			endSuffix = (char) ('A' + (size - 25) % 26 - 1);
			endPrefix = (char) (endPrefix + (size - 25) / 26);
			}
			}
			String s ="$" + start + "$" + order + ":$" + endPrefix + endSuffix + "$" + order;
			return s;
					
			}
			} else {
			if (size <= 26) {
			char end = (char) (start + size - 1);
			 String s ="$" + start + "$" + order + ":$" + end + "$" + order;
			 return s;
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
			String s = "$" + start + "$" + order + ":$" + endPrefix + endSuffix + "$" + order;
			return s;
			}
			}*/
	}

            
        /**    
         * 创建一列数据    
         * @param currentRow    
         * @param textList    
         */      
        private static void creatRow(Row currentRow, String[] textList){
            if(textList!=null&&textList.length>0){       
                int i = 0;       
                for(String cellValue : textList){       
                    Cell userNameLableCell = currentRow.createCell(i++);
                    userNameLableCell.setCellValue(cellValue);       
                }       
            }       
        }/**    
         * 添加数据验证选项    
         * @param sheet
         */      
        public static void setDataValidation(Workbook wb){
            int sheetIndex = wb.getNumberOfSheets();       
            if(sheetIndex>0){       
                for(int i=0;i<sheetIndex;i++){       
                    Sheet sheet = wb.getSheetAt(i);
                    if(!EXCEL_HIDE_SHEET_NAME.equals(sheet.getSheetName())){    
                       
                        //添加验证数据        
                        for(int a=2;a<3002;a++){    

                        	
                        	DataValidation data_validation_list1 = getDataValidationByFormula(hideString.get(0),a,1);
                            sheet.addValidationData(data_validation_list1);
                            DataValidation data_validation_list2 = getDataValidationByFormula(hideString.get(1),a,2);
                            sheet.addValidationData(data_validation_list2);
                            DataValidation data_validation_list3 = getDataValidationByFormula(hideString.get(5),a,6);
                            sheet.addValidationData(data_validation_list3);
                        }    
                    }       
                }       
            }       
        }  
        
        public static void setDataValidationForCmdb(Workbook wb){
            int sheetIndex = wb.getNumberOfSheets();       
            if(sheetIndex>0){       
                for(int i=0;i<sheetIndex;i++){       
                    Sheet sheet = wb.getSheetAt(i);
                    if(!EXCEL_HIDE_SHEET_NAME.equals(sheet.getSheetName())){    
                       
                        //省份选项添加验证数据        
                        for(int a=2;a<3002;a++){    
                        	
                                                                                             
                            DataValidation data_validation_list1 = getDataValidationByFormula(hideString.get(1),a,2);
                            sheet.addValidationData(data_validation_list1);
                            DataValidation data_validation_list2 = getDataValidationByFormula("INDIRECT($B$" + a + ")",a,3);
                            sheet.addValidationData(data_validation_list2);
                            
                            
                            DataValidation data_validation_list3 = getDataValidationByFormula( hideString.get(tempSize-2),a,4);
                            sheet.addValidationData(data_validation_list3);
                            DataValidation data_validation_list4 = getDataValidationByFormula(hideString.get(tempSize-1),a,5);
                            sheet.addValidationData(data_validation_list4);
                                                                                   //包含取消提示框校验
                            DataValidation data_validation_list5 = getDataValidationByFormula(hideString.get(tempSize),a,6);
                            sheet.addValidationData(data_validation_list5);
                            
                            
                            DataValidation data_validation_list6 = getDataValidationByFormula(hideString.get(tempSize+1),a,7);
                            sheet.addValidationData(data_validation_list6);
                            DataValidation data_validation_list7 = getDataValidationByFormula("INDIRECT($G$" + a + ")",a,8);
                            sheet.addValidationData(data_validation_list7);
                            
                            DataValidation data_validation_list8 = getDataValidationByFormula("INDIRECT($H$" + a + ")",a,9);
                            sheet.addValidationData(data_validation_list8);
                            
                            DataValidation data_validation_list9 = getDataValidationByFormula(hideString.get(tempSize1-7),a,10);
                            sheet.addValidationData(data_validation_list9);
                            DataValidation data_validation_list10 = getDataValidationByFormula(hideString.get(tempSize1-6),a,11);
                            sheet.addValidationData(data_validation_list10);
                            DataValidation data_validation_list11 = getDataValidationByFormula(hideString.get(tempSize1-5),a,12);
                            sheet.addValidationData(data_validation_list11);
                            DataValidation data_validation_list12 = getDataValidationByFormula(hideString.get(tempSize1-4),a,13);
                            sheet.addValidationData(data_validation_list12);
                            DataValidation data_validation_list13 = getDataValidationByFormula(hideString.get(tempSize1-3),a,14);
                            sheet.addValidationData(data_validation_list13);
                            DataValidation data_validation_list14 = getDataValidationByFormula(hideString.get(tempSize1-2),a,15);
                            sheet.addValidationData(data_validation_list14);
                            DataValidation data_validation_list15 = getDataValidationByFormula(hideString.get(tempSize1-1),a,16);
                            sheet.addValidationData(data_validation_list15);
                            DataValidation data_validation_list16 = getDataValidationByFormula(hideString.get(tempSize1),a,17);
                            sheet.addValidationData(data_validation_list16);
                            
                        }    
                    }       
                }       
            }       
        }      

        
		/**    
         * 使用已定义的数据源方式设置一个数据验证    
         * @param formulaString    
         * @param naturalRowIndex    
         * @param naturalColumnIndex    
         * @return    
         */      
        private static DataValidation getDataValidationByFormula(String formulaString, int naturalRowIndex, int naturalColumnIndex){
            //创建下拉列表数据    
            DVConstraint constraint = DVConstraint.createFormulaListConstraint(formulaString);
            //设置数据有效性加载在哪个单元格上。         
            //四个参数分别是：起始行、终止行、起始列、终止列         
            int firstRow = naturalRowIndex-1;       
            int lastRow = naturalRowIndex-1;       
            int firstCol = naturalColumnIndex-1;       
            int lastCol = naturalColumnIndex-1; 
            //设置   第几列   第几行  为下拉列表
            CellRangeAddressList regions=new CellRangeAddressList(firstRow,lastRow,firstCol,lastCol);
            
            //数据有效性对象          绑定
            DataValidation data_validation_list = new HSSFDataValidation(regions,constraint);
            
            if (formulaString.equals("IDC_LOCATION")) {
            	//特定行    取消---->只能选下拉框内容，可自己输入新的内容
            	data_validation_list.setShowErrorBox(false);
			} else {
				  //设置输入信息提示信息       
	            data_validation_list.createPromptBox("下拉选择提示","请使用下拉方式选择合适的值！");       
	            //设置输入错误提示信息       
	            data_validation_list.createErrorBox("选择错误提示","你输入的值未在备选列表中，请下拉选择合适的值！"); 
			}
            
            return data_validation_list;       
        }    
        private static DataValidation getDataValidationByDate(int naturalRowIndex, int naturalColumnIndex){
            //加载下拉列表内容         
            DVConstraint constraint = DVConstraint.createDateConstraint(DVConstraint.OperatorType.BETWEEN,"1900-01-01", "5000-01-01", "yyyy-mm-dd");
            //设置数据有效性加载在哪个单元格上。         
            //四个参数分别是：起始行、终止行、起始列、终止列         
            int firstRow = naturalRowIndex-1;       
            int lastRow = naturalRowIndex-1;       
            int firstCol = naturalColumnIndex-1;       
            int lastCol = naturalColumnIndex-1;       
            CellRangeAddressList regions=new CellRangeAddressList(firstRow,lastRow,firstCol,lastCol);
            //数据有效性对象        
            DataValidation data_validation_list = new HSSFDataValidation(regions,constraint);
            //设置输入信息提示信息       
            data_validation_list.createPromptBox("日期格式提示","请按照'yyyy-mm-dd'格式输入日期值！");       
            //设置输入错误提示信息       
            data_validation_list.createErrorBox("日期格式错误提示","你输入的日期格式不符合'yyyy-mm-dd'格式规范，请重新输入！");       
            return data_validation_list;       
        }        
}  