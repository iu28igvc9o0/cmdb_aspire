package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.mainten;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.mainten.IMaintenHardWareService;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.maintenance.payload.Hardware;
import com.aspire.ums.cmdb.maintenance.payload.HardwareRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten.MaintenHardWareClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 类名称: MaintenHardWareController
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/7/31 11:22
 * 版本: v1.0
 */
@RestController
@Slf4j
public class MaintenHardWareController implements IMaintenHardWareService {

    @Autowired
    private MaintenHardWareClient hardWareClient;

    @Override
    public Map<String, Object> updateHardware(@RequestBody Hardware hardware) {
        return hardWareClient.updateHardware(hardware);
    }

    @Override
    public Map<String, Object> batchUpdateHardware(@RequestBody Hardware hardwareList) {
        return hardWareClient.batchUpdateHardware(hardwareList);
    }

    @Override
    public Result<Hardware> selectHardwareByPage(@RequestBody HardwareRequest request) {
        Result<Hardware> rs = hardWareClient.selectHardwareByPage(request);
        return rs;
    }

    @Override
    public List<Hardware> getHardwareList(@RequestBody HardwareRequest request) {
        return hardWareClient.getHardwareList(request);
    }

    @Override
    public Map<String, String> export(HttpServletResponse response, @RequestBody HardwareRequest sendRequest) {
        Map<String, String> returnMap = new HashMap<>();
        try{
            List<Map<String, Object>> dataList = hardWareClient.export(sendRequest);
            // 查询出总数，计算页数
//            sendRequest.setPageNum(1);
//            sendRequest.setPageSize(100);
//            Result<Hardware> hardwares= hardWareClient.selectHardwareByPage(sendRequest);
//            String resString = JSON.toJSONString(hardwares);
//            JSONObject returnJson = JSONObject.parseObject(resString);
//            int totalCount = hardwares.getCount();
//            int totalPage = (int)Math.ceil(totalCount/100.0);
//            List<Map<String, Object>> dataList = new LinkedList<>();
//            dataList.addAll((List<Map<String, Object>>) returnJson.get("data"));
//            if(totalPage > 1) {
//                for(int index =2 ; index <= totalPage ; index++) {
//                    sendRequest.setPageNum(index);
//                    Result<Hardware> tmpHardwares = hardWareClient.selectHardwareByPage(sendRequest);
//                    String tmpStr = JSON.toJSONString(tmpHardwares);
//                    JSONObject tmpJson = JSONObject.parseObject(tmpStr);
//                    dataList.addAll((List<Map<String, Object>>) tmpJson.get("data"));
//                }
//            }
            String[] headerList = {"设备序列号","资源池","一级部门","二级部门","业务系统","设备分类","设备类型",
                    "设备型号","设备名称","IP","资产编号","项目名称","合同编号","合同供应商","合同联系人","合同联系人电话",
                    "合同联系人邮箱","维保类型","设备区域","维保对象类型","采购类型","金额(万)","服务供应商","服务联系人","服务联系人电话",
                    "服务联系人邮箱","服务类型","服务开始时间","出保时间","是否购买维保","是否需原厂维保","原厂维保购买必要性说明","维保管理员","税前(万)","税率","税后(万)",
                    "单价(万)","总价(万)","结算方式","折扣后金额(万)","折扣率"};
            String[] keyList = {"device_serial_number","resource_pool","department1","department2","system_name","device_classify","device_type",
                    "device_model","device_name","ip","assets_number","project_name","project_no", "contract_produce","contract_produce_name","contract_produce_chone",
                    "contract_produce_email","maintenance_type","device_area","maintenance_project_type", "procure_type","money","mainten_produce","mainten_produce_name","mainten_produce_phone",
                    "mainten_produce_email","service_type","service_start_time","warranty_date","buy_mainten","origin_buy","origin_buy_explain","admin","pre_tax","tax_rate","after_tax",
                    "unit_price","total_price","pay_method","discount_amount","discount_rate"};
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String type = sendRequest.getType();
            if (StringUtils.isNotEmpty(type) && "template".equals(type.toLowerCase(Locale.ENGLISH))) {
                headerList = new String[] {"设备序列号[必填]","出保时间[必填]","是否购买维保",
                        "是否需原厂维保","原厂维保购买必要性说明","维保管理员"};
                keyList = new String[] {"device_serial_number","warranty_date","buy_mainten",
                        "origin_buy","origin_buy_explain","admin"};
            } else {
                if (!dataList.isEmpty()) {
                    for (Map<String, Object> map  : dataList) {
                        if(null == map.get("warranty_date") || null == map.get("service_start_time")) {
                            continue;
                        }
                        Long sDate = Long.valueOf(String.valueOf(map.get("service_start_time")));
                        Long wDate = Long.valueOf(String.valueOf(map.get("warranty_date")));
                        Date service_start_time = new Date(sDate);
                        Date warranty_date = new Date(wDate);
                        map.put("warranty_date", sdf.format(warranty_date));
                        map.put("service_start_time", sdf.format(service_start_time));
                    }
                }
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            String fileName = dateFormat.format(new Date()) + "维保硬件列表.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
//            if (sendRequest != null && sendRequest.size() > 0) {
                // 取得输出流
                OutputStream os = response.getOutputStream();
                ExportExcelUtil eeu = new ExportExcelUtil();
                Workbook book = new SXSSFWorkbook(128);
                eeu.exportExcel(book, 0, "维保硬件列表", headerList, dataList, keyList);
                book.write(os);
                os.flush();
                os.close();
//            }
            returnMap.put("code", "success");
            response.setStatus(HttpStatus.NO_CONTENT.value());
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
            returnMap.put("code", "error");
            returnMap.put("message", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return returnMap;
    }

    @Override
    public Map<String, Object> queryIsExist(@RequestParam("deviceSerialNumber") String deviceSerialNumber, @RequestParam("warrantyDate") String warrantyDate) {
        return hardWareClient.queryIsExist(deviceSerialNumber,warrantyDate);
    }

    @Override
    public Map<String, Object> queryInfoByNameAndDeviceSn(@RequestParam("projectName") String projectName,@RequestParam("deviceSn") String deviceSn) {
        return hardWareClient.queryInfoByNameAndDeviceSn(projectName,deviceSn);
    }
}
