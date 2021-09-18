package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.mainten;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.mainten.IMaintenProduceInfoService;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.maintenance.payload.Concat;
import com.aspire.ums.cmdb.maintenance.payload.ProduceInfoRequest;
import com.aspire.ums.cmdb.maintenance.payload.ProduceInfoResq;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten.MaintenProduceInfoClient;
import com.migu.tsg.microservice.atomicservice.composite.common.FtpService;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ExportExcelUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Slf4j
public class MaintenProduceInfoController implements IMaintenProduceInfoService {

    @Autowired
    private MaintenProduceInfoClient produceInfoClient;

    @Autowired
    private FtpService ftpService;

    @Override
    public Result<ProduceInfoResq> selectProduceByPage(@RequestBody ProduceInfoRequest produceInfoRequest) {
        return produceInfoClient.selectProduceByPage(produceInfoRequest);
    }

    @Override
    public ProduceInfoResq getProduceById(@RequestParam("id") String id) {
        return produceInfoClient.getProduceById(id);
    }

    @Override
    public Map<String, Object> insertProduce(ProduceInfoResq produceInfoRequest, @RequestParam(value = "logo", required = false) MultipartFile logo) {
        Map<String, String> ftpMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        if(StringUtils.isEmpty(produceInfoRequest.getId())) {
            produceInfoRequest.setId(UUIDUtil.getUUID());
        }
        try {
            String[] fileNameArray =logo.getOriginalFilename().split("\\.");
            ftpMap = ftpService.uploadImageToFTP(produceInfoRequest.getId() + "." + fileNameArray[1], logo.getInputStream());
            if (!"success".equals(ftpMap.get("code"))) {
                throw new RuntimeException(ftpMap.get("message"));
            }
            String path = ftpMap.get("path");
            produceInfoRequest.setLogoUrl(path);
            resultMap = produceInfoClient.insertProduce(produceInfoRequest);
        } catch (IOException e) {
           resultMap.put("success", false);
           resultMap.put("msg", "logo图片上传失败" + e.getMessage());
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("msg", e.getMessage());
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> updateProduce(@RequestBody ProduceInfoResq produceInfoRequest) {
        return produceInfoClient.updateProduce(produceInfoRequest);
    }

    @Override
    public Map<String, Object> deleteProduce(@RequestParam("produceId") String produceId) {
        return produceInfoClient.deleteProduce(produceId);
    }

    /**
     * 根据厂商ID和类型查询联系人信息
     * @param produceId
     * @return
     */
    @Override
    public List<Concat> queryConcat(@RequestParam("produceId") String produceId,
                             @RequestParam(value = "personType", required = false) String type){
        return produceInfoClient.queryConcat(produceId, type);
    }


    @Override
    @ResAction(action = "view", resType = "cmdb-resource")
    public Map<String, String> export(HttpServletResponse response, @RequestBody Map<String, Object> sendRequest) {
        Map<String, String> returnMap = new HashMap<>();
        try{
            Map<String, Object> reqParam = new HashMap<String, Object>();
            reqParam.put("produce", sendRequest.get("produce").toString());
            reqParam.put("type", sendRequest.get("type").toString());
            reqParam.put("name", sendRequest.get("name").toString());
            reqParam.put("phone", sendRequest.get("phone").toString());
            reqParam.put("email", sendRequest.get("email").toString());

            JSONObject returnJson = produceInfoClient.export(reqParam);
//            String[] headerList = {"厂商","厂商类型","厂商备注","联系人","电话","邮箱","备注"};
//            String[] keyList = {"produce","produce_type","remark","name","phone","email","remark2"};
            String[] headerList = {"厂商","厂商类型","厂商备注"};
            String[] keyList = {"produce","produce_type","remark"};
            List<Map<String, Object>> dataList = new LinkedList<>();
            if (returnJson.containsKey("dataList") && returnJson.get("dataList") != null) {
                dataList = (List<Map<String, Object>>) returnJson.get("dataList");
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            String fileName = dateFormat.format(new Date()) + "维保厂商列表.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            if (sendRequest != null && sendRequest.size() > 0) {
                // 取得输出流
                OutputStream os = response.getOutputStream();
                ExportExcelUtil eeu = new ExportExcelUtil();
                Workbook book = new SXSSFWorkbook(128);
                eeu.exportExcel(book, 0, "维保厂商列表", headerList, dataList, keyList);
                book.write(os);
                os.flush();
                os.close();
            }
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
    public List<ProduceInfoResq> queryProduceInfoList() {
        return produceInfoClient.queryProduceInfoList();
    }

    @Override
    public Map<String, Object> addConcat(@RequestBody Concat concat) {
        return produceInfoClient.addConcat(concat);
    }

    @Override
    public Map<String, Object> updateConcat(@RequestBody Concat concat) {
        return produceInfoClient.updateConcat(concat);
    }

    @Override
    public Map<String, Object> deleteConcat(@RequestParam("id") String id) {
        return produceInfoClient.deleteConcat(id);
    }


}
