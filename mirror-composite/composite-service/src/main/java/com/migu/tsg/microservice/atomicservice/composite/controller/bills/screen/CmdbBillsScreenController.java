package com.migu.tsg.microservice.atomicservice.composite.controller.bills.screen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aspire.mirror.composite.service.bills.screen.CmdbBillsScreenAPI;
import com.aspire.ums.bills.account.payload.CmdbBillsAccountBalance;
import com.aspire.ums.bills.log.payload.BillsLog;
import com.aspire.ums.bills.screen.payload.ScreenResponse;
import com.aspire.ums.cmdb.systemManager.payload.OrgManager;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.bills.account.CmdbBillsAccountClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.bills.log.CmdbBillsLogClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.bills.screen.CmdbBillsScreenClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.OrgManagerClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PdfWatermark;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @projectName: CmdbBillsScreenController
 * @description: 类
 * @author: luowenbo
 * @create: 2020-08-07 10:45
 **/
@RestController
@Slf4j
public class CmdbBillsScreenController implements CmdbBillsScreenAPI {

    @Autowired
    private CmdbBillsScreenClient billsScreenClient;
    @Autowired
    private CmdbBillsAccountClient accountClient;
    @Autowired
    private OrgManagerClient orgManagerClient;
    @Autowired
    private CmdbBillsLogClient billsLogClient;
    private static final String FILE_NAME = "F:\\pdf\\helloPdf.pdf";


    @Override
    public ScreenResponse<Map<String, Object>> getMonthBillsWithResourceType(@RequestParam("chargeTime") String chargeTime,
                                                                             @RequestParam("department") String department,
                                                                             @RequestParam("idcType") String idcType,
                                                                             @RequestParam("resourceType") String resourceType) {
        return billsScreenClient.getMonthBillsWithResourceType(chargeTime, department, idcType, resourceType);
    }

    @Override
    public ScreenResponse<List<Map<String, Object>>> getQuotaAndBillsWithBiz(@RequestParam("chargeTime") String chargeTime,
                                                                             @RequestParam("department") String department) {
        return billsScreenClient.getQuotaAndBillsWithBiz(chargeTime, department);
    }

    @Override
    public ScreenResponse<List<Map<String, Object>>> getQuotaAndBillsWithResourceType(@RequestParam("chargeTime") String chargeTime,
                                                                                      @RequestParam("department") String department,
                                                                                      @RequestParam("bizSystem") String bizSystem,
                                                                                      @RequestParam("idcType") String idcType,
                                                                                      @RequestParam("resourceType") String resourceType) {
        return billsScreenClient.getQuotaAndBillsWithResourceType(chargeTime, department, bizSystem, idcType, resourceType);
    }

    @Override
    public ScreenResponse<Map<String, Object>> getAccountRevenueRecord(@RequestParam("year") String year,
                                                                       @RequestParam("department") String department) {
        return billsScreenClient.getAccountRevenueRecord(year,department);
    }

    @Override
    public ScreenResponse<List<Map<String, Object>>> getUnitPrice(@RequestParam("bizSystem") String bizSystem,
                                                                  @RequestParam("department") String department,
                                                                  @RequestParam("idcType") String idcType) {
        return billsScreenClient.getUnitPrice(bizSystem, department, idcType);
    }

    @Override
    public ScreenResponse<Map<String, Object>> getAccountInfo(@RequestParam("department") String department,
                                                              @RequestParam("monthEnd") String monthEnd) {
        return billsScreenClient.getAccountInfo(department, monthEnd);
    }

    @Override
    public void exportBillsDetail(@RequestParam("department") String department,
                                  @RequestParam("chargeTime") String chargeTime,
                                  HttpServletResponse response) {
        OutputStream os = null;
        try{
            List<Map<String,Object>> dataList = billsScreenClient.exportBillsDetail(department, chargeTime);
            String[] headerList = {"业务系统","联系人","联系电话","一级部门","二机部门","资源池","POD池","本月合计(元)",
                    "本月裸金属已分配总量(台)","应用服务器已分配总量(台)","分析型服务器已分配总量(台)","分布式服务器已分配总量(台)","缓存型服务器已分配总量(台)","高端服务器已分配总量(台)","多节点服务器已分配总量(台)",
                    "云主机已分配总量(台)","云主机核数已分配总量(个)","云主机内存已分配总量(G)","FCSAN已分配总量(T)","IPSAN已分配总量(T)","块存储已分配总量(T)","文件存储已分配总量(T)","对象存储已分配总量(T)","备份存储已分配总量(T)",
                    "应用服务器单价","分析型服务器单价","分布式服务器单价","缓存型服务器单价","高端服务器单价","多节点服务器单价","每VCPU单价","每G内存单价","FCSAN单价","IPSAN单价","块存储单价","文件存储单价","对象存储单价","备份存储单价",
                    "应用服务器单月总价","分析型服务器单月总价","分布式服务器单月总价","缓存型服务器单月总价","高端服务器单月总价","多节点服务器单月总价","每VCPU单月总价","每G内存单月总价","FCSAN单月总价","IPSAN单月总价","块存储单月总价",
                    "文件存储单月总价","对象存储单月总价","备份存储单月总价"};
            String[] keyList = {"bizName","businessConcat","businessConcatPhone","dep1Name","dep2Name","idcName","podName","totalMoney",
                    "ljs_allocation_amount","yyfwq_allocation_amount","fxxfwq_allocation_amount","fbsfwq_allocation_amount","hcxfwq_allocation_amount","gdyyfwq_allocation_amount","djdfwq_allocation_amount",
                    "yzj_allocation_amount","yzj_vcpu_allocation_amount","yzj_memory_allocation_amount","fcsan_allocation_amount","ipsan_allocation_amount","kcc_allocation_amount","wjcc_allocation_amount","dxcc_allocation_amount",
                    "bfcc_allocation_amount","yyfwq_allocation_amount_price","fxxfwq_allocation_amount_price","fbsfwq_allocation_amount_price","hcxfwq_allocation_amount_price","gdyyfwq_allocation_amount_price","djdfwq_allocation_amount_price",
                    "yzj_vcpu_allocation_amount_price","yzj_memory_allocation_amount_price","fcsan_allocation_amount_price","ipsan_allocation_amount_price","kcc_allocation_amount_price","wjcc_allocation_amount_price","dxcc_allocation_amount_price","bfcc_allocation_amount_price",
                    "yyfwq_allocation_amount_month","fxxfwq_allocation_amount_month","fbsfwq_allocation_amount_month","hcxfwq_allocation_amount_month","gdyyfwq_allocation_amount_month","djdfwq_allocation_amount_month",
                    "yzj_vcpu_allocation_amount_month","yzj_memory_allocation_amount_month","fcsan_allocation_amount_month","ipsan_allocation_amount_month","kcc_allocation_amount_month","wjcc_allocation_amount_month","dxcc_allocation_amount_month","bfcc_allocation_amount_month"};
            String fileName = "monthBillsDetail.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            // 取得输出流
            os = response.getOutputStream();
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, "账单明细", headerList, dataList, keyList);
            book.write(os);
            os.flush();
            response.setStatus(HttpStatus.NO_CONTENT.value());
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<Map<String, Object>> getSpecificBillsWithResourceType(@RequestParam("department") String department,
                                                                      @RequestParam("chargeTime") String chargeTime) {
        return billsScreenClient.getSpecificBillsWithResourceType(department, chargeTime);
    }

    /**
     * pdfData
    * [{
     *     "idcNameList": [
     *       {
     *         "billsDeviceType": [
     *           {
     *             "deviceType": "裸金属",
     *             "totalMoney": 0,
     *             "idcName": "北京池外"
     *           }],
     * 		   "idcName": "北京池外"
     *              }
     * 	 ],
     * 	 "resourceType": "裸金属资源"
     * 	 }]
    * */
    @Override
    public Map<String, Object> exportSpecificBillsWithResourceType(@RequestParam("department") String department,
                                                                         @RequestParam("chargeTime") String chargeTime,
                                                                         HttpServletResponse response) {
        // 获取当前用户
        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
        String username = reqCtx.getUser().getUsername();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 获取pdf数据
        List<Map<String, Object>> pdfData = billsScreenClient.getSpecificBillsWithResourceType(department, chargeTime);
//        {"flag":true,"data":{"accountManagerPhone":null,"accountCode":null,"realPay":null,"accountManager":null,"balance":-44001645,"monthTotal":0,"grandTotal":null,"needPay":0},"message":"success"}
        CmdbBillsAccountBalance account = accountClient.getAccountBydepartment(department);
        ScreenResponse<Map<String,Object>> accountSource = billsScreenClient.getAccountInfo(department, chargeTime);
        if (pdfData == null) {
            throw new RuntimeException("获取账单信息失败！");
        }
        if (account == null) {
            throw new RuntimeException("获取账户信息失败！");
        }
        if (!accountSource.isFlag()) {
            throw new RuntimeException("获取账户费用信息失败！");
        }
        Map<String,Object> accountInfo = accountSource.getData();
        //告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/pdf");
        //下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=test.pdf");
        //设置中文
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = null;
        try {
            BaseFont baseFont = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
            Font titleFont = new Font(baseFont, (float) (26 * 0.75),Font.BOLD);
            Font contentFont = new Font(baseFont, (float) (16 * 0.75));
            baos = new ByteArrayOutputStream();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);
            pdfWriter.setPageEvent(new PdfWatermark(username + dateFormat.format(new Date())));
            document.open();
            Image img = Image.getInstance(this.getClass().getResource("/img/logo_ITcloud.png"));
            img.setIndentationLeft(0);
            img.scalePercent(10f);
            document.add(img);
            Paragraph title = new Paragraph("中国移动一级IT云租户账单", titleFont);
            title.setSpacingAfter(10f);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            // 添加水平线
            LineSeparator line = new LineSeparator(1f,100,BaseColor.BLACK,Element.ALIGN_CENTER,-5f);
            document.add(line);
            // 租户信息等
            String orgName = account.getOrgName();
//            String orgName = accountInfo.get("orgName") == null ? "" : accountInfo.get("orgName").toString();
            Paragraph departPar = new Paragraph("租户名称:  " + orgName, contentFont);
            departPar.setLeading(30);
            document.add(departPar);
            String cycleInfo = toHandleCycle(chargeTime);
            Chunk cycle = new Chunk(cycleInfo, contentFont);
            String accountCode = account.getAccountCode();
//            String accountCode = accountInfo.get("accountCode") == null ? "" : accountInfo.get("accountCode").toString();
            Chunk code = new Chunk("账户编码:  " + accountCode, contentFont);
            Phrase phrase = new Phrase();
            phrase.add(cycle);
            phrase.add(new Chunk("      "));
            phrase.add(code);
            Paragraph par = new Paragraph();
            par.setSpacingBefore(10f);
            par.setSpacingAfter(10f);
            par.add(phrase);
            document.add(par);
            // 添加水平线
            document.add(line);
            // 生成一个两列的表格
            toCreateTable(pdfData, account, accountInfo, document, baseFont);
            document.close();
            // 转字符串设置编码
            String result =new String(baos.toByteArray(), StandardCharsets.ISO_8859_1);
            ByteArrayInputStream inStream = new ByteArrayInputStream(
                    result.getBytes(StandardCharsets.ISO_8859_1));
            byte[] b = new byte[2048];
            int len;
            try {
                while ((len = inStream.read(b)) > 0) {
                    response.getOutputStream().write(b, 0, len);
                }
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();

            }finally {
//                waterMar = null;
//                gs = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        //  记录下载日志
        OrgManager orgManager = orgManagerClient.getById(department);
        BillsLog billsLog = new BillsLog();
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        HttpServletRequest request = sra.getRequest();
        billsLog.setOperateOBJ("计费账单");
        billsLog.setOperateType("下载");
        billsLog.setOperateContent("下载" + orgManager.getOrgName() + "租户账单");
        billsLogClient.saveBillLogs(billsLog);
        return null;
    }

    private void toCreateTable(List<Map<String, Object>> pdfData, CmdbBillsAccountBalance account, Map<String, Object> accountInfo, Document document, BaseFont baseFont) throws DocumentException {
        Font contentFont = new Font(baseFont, (float) (14 * 0.75));
        Font headerFont = new Font(baseFont, (float) (16 * 0.75));
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(30f);
        table.setSplitLate(false);
        PdfPCell cell = new PdfPCell();
        toCreateCell(table, 4, 2, 0, headerFont, "费用信息", Element.ALIGN_CENTER);
        toCreateCell(table, 1, 8, 2, headerFont, "费用项目", Element.ALIGN_LEFT);
        toCreateCell(table, 1, 4, 2, headerFont, "金额/元", Element.ALIGN_LEFT);
        toCreateCell(table, 1, 8, 2, headerFont, "费用项目", Element.ALIGN_LEFT);
        toCreateCell(table, 1, 4, 2, headerFont, "金额/元", Element.ALIGN_LEFT);
        // 处理两列数据
        PdfPTable moneyTable = new PdfPTable(2);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        int count = 0;
        // 处理费用信息两列清单
        for (Map<String, Object> deviceTypeMap : pdfData) {
            if (count == (pdfData.size() / 2 + (pdfData.size() % 2))) {
                cell.addElement(moneyTable);
                table.addCell(cell);
                moneyTable.flushContent();
                moneyTable = new PdfPTable(2);
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            }
            String resourceType = deviceTypeMap.get("resourceType").toString();
            toCreateCell(moneyTable, 2, 15, 0 , contentFont,resourceType, Element.ALIGN_LEFT);
            //设备类型
            List<Map> idcNameList = JSONArray.parseArray(JSON.toJSONString(deviceTypeMap.get("idcNameList")), Map.class);
            for(Map idc : idcNameList) {
                String idcName = idc.get("idcName").toString();
                toCreateCell(moneyTable, 2, 15, 8 , contentFont, idcName, Element.ALIGN_LEFT);
                List<Map> billsDeviceType = JSONArray.parseArray(JSON.toJSONString(idc.get("billsDeviceType")), Map.class);
                for (Map type : billsDeviceType) {
                    String deviceType = type.get("deviceType").toString();
                    String totalMoney = formatToNumber(new BigDecimal(type.get("totalMoney").toString()));
                    toCreateCell(moneyTable, 1, 15, 16, contentFont, deviceType, Element.ALIGN_LEFT);
                    toCreateCell(moneyTable, 1, 15, 16, contentFont, totalMoney, Element.ALIGN_LEFT);
                }
            }
            count++;
        }
        cell.addElement(moneyTable);
        table.addCell(cell);
        String monthTotal = accountInfo == null ? "0.00" : accountInfo.get("monthTotal") == null ? "0.00" : formatToNumber(new BigDecimal(accountInfo.get("monthTotal").toString()));
        toCreateCell(table, 4, 1, 4, contentFont, "费用合计:  " + monthTotal, Element.ALIGN_LEFT);
        //账户信息+经理信息表
        PdfPTable secondTable = new PdfPTable(2);
        secondTable.setSpacingBefore(30f);
        secondTable.setWidthPercentage(100);
        //账户信息
        PdfPTable accountTable = new PdfPTable(2);
        accountTable.setWidthPercentage(100);
        toCreateCell(accountTable,2, 13, 8, contentFont, "账户信息", Element.ALIGN_LEFT);
        toCreateCell(accountTable,1, 13, 8, contentFont, "账户项目", Element.ALIGN_LEFT);
        toCreateCell(accountTable,1, 13, 8, contentFont, "金额/元", Element.ALIGN_LEFT);
        PdfPTable accountContentTable = new PdfPTable(2);
        toCreateCell(accountContentTable,2, 15, 8, contentFont, "本月计费合计", Element.ALIGN_LEFT);
        toCreateCell(accountContentTable,1, 15, 8, contentFont, "应缴费用:", Element.ALIGN_LEFT);
        String needPay = accountInfo == null ? "0.00" :accountInfo.get("needPay") == null ? "0.00" : formatToNumber(new BigDecimal(accountInfo.get("needPay").toString()));
        toCreateCell(accountContentTable,1, 15, 8, contentFont, needPay, Element.ALIGN_LEFT);
        toCreateCell(accountContentTable,1, 15, 8, contentFont, "实缴费用:", Element.ALIGN_LEFT);
        String realPay = accountInfo == null ? "0.00" : accountInfo.get("realPay") == null ? "0.00" : formatToNumber(new BigDecimal(accountInfo.get("realPay").toString()));
        toCreateCell(accountContentTable,1, 15, 8, contentFont, realPay, Element.ALIGN_LEFT);
        toCreateCell(accountContentTable,1, 15, 8, contentFont, "本月累计已缴费:", Element.ALIGN_LEFT);
        String grandTotal = accountInfo == null ? "0.00" : accountInfo.get("grandTotal") == null ? "0.00" : formatToNumber(new BigDecimal(accountInfo.get("grandTotal").toString()));
        toCreateCell(accountContentTable,1, 15, 8, contentFont, grandTotal, Element.ALIGN_LEFT);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setMinimumHeight(100f);
        cell.addElement(accountContentTable);
        accountTable.addCell(cell);
        String balance =  accountInfo == null ? "0.00" : accountInfo.get("balance") == null ? "0.00" : formatToNumber(new BigDecimal(accountInfo.get("balance").toString()));
        toCreateCell(accountTable,2, 14, 2, contentFont, "本月账户余额:  " + balance, Element.ALIGN_LEFT);
        cell = new PdfPCell();
        cell.addElement(accountTable);
        secondTable.addCell(cell);

        //客户经理信息
        PdfPTable managerTable = new PdfPTable(2);
        managerTable.setWidthPercentage(100);
        toCreateCell(managerTable,2, 13, 8, contentFont, "客户经理信息", Element.ALIGN_LEFT);
        toCreateCell(managerTable,1, 13, 8, contentFont, "信息项", Element.ALIGN_LEFT);
        toCreateCell(managerTable,1, 13, 8, contentFont, "信息内容", Element.ALIGN_LEFT);
        PdfPTable managerContentTable = new PdfPTable(2);
        toCreateCell(managerContentTable,1, 15, 8, contentFont, "客户经理:", Element.ALIGN_LEFT);
        String accountManager = account.getAccountManager();
        toCreateCell(managerContentTable,1, 15, 8, contentFont, accountManager == null ? "" : accountManager, Element.ALIGN_LEFT);
//        toCreateCell(managerContentTable,1, 15, 8, contentFont, "客户经理邮箱:", Element.ALIGN_LEFT);
//        String accountManagerEmail = account.getAccountManagerEmail();
//        toCreateCell(managerContentTable,1, 15, 8, contentFont, accountManagerEmail == null ? "" : accountManagerEmail, Element.ALIGN_LEFT);
        toCreateCell(managerContentTable,1, 15, 8, contentFont, "联系电话:", Element.ALIGN_LEFT);
        String accountManagerPhone = account.getAccountManagerPhone();
        toCreateCell(managerContentTable,1, 15, 8, contentFont,accountManagerPhone == null ? "" : accountManagerPhone, Element.ALIGN_LEFT);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setMinimumHeight(100f);
        cell.addElement(managerContentTable);
        managerTable.addCell(cell);
        toCreateCell(managerTable,2, 14, 2, contentFont, "感谢您对信息技术中心的支持！", Element.ALIGN_LEFT);
        cell = new PdfPCell();
        cell.addElement(managerTable);
        secondTable.addCell(cell);
        document.add(table);
        document.add(secondTable);
    }

    private String toHandleCycle(String month) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            calendar.setTime(format.parse(month));
            calendar.add(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String firstday = simpleDate.format(calendar.getTime());
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            String lastday = simpleDate.format(calendar.getTime());
//            int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            return "计费周期:  " + firstday + " 至 " + lastday;
        } catch (ParseException e) {
            throw new RuntimeException("日期转换失败");
        }
    }

    private void toCreateCell(PdfPTable table, int colSpan, int showBorder, int spaceSize, Font font, String value, int alignment) {
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(100);
        String space = "";
        while (spaceSize > 0) {
            space = space + " ";
            spaceSize--;
        }
        Chunk chunkSpace = new Chunk(space, font);
        Chunk chunk = new Chunk(value, font);
        Phrase phrase = new Phrase();
        phrase.add(chunkSpace);
        phrase.add(chunk);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setMinimumHeight(font.getSize() + 10);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.disableBorderSide(showBorder);
        cell.setColspan(colSpan);
        table.addCell(cell);
    }

    /**
     * @desc 1.0~1之间的BigDecimal小数，格式化后失去前面的0,则前面直接加上0。
     * 2.传入的参数等于0，则直接返回字符串"0.00"
     * 3.大于1的小数，直接格式化返回字符串
     * @param obj obj
     * @return
     */
    public static String formatToNumber(BigDecimal obj) {
        DecimalFormat df = new DecimalFormat("#.00");
        if(obj.compareTo(BigDecimal.ZERO)==0) {
            return "0.00";
        }else if(obj.compareTo(BigDecimal.ZERO)>0&&obj.compareTo(new BigDecimal(1))<0){
            return "0"+ df.format(obj);
        }else {
            return df.format(obj);
        }
    }
}
