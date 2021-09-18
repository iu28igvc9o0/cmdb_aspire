package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.assessment;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.assessment.IITDeviceCountAPI;
import com.aspire.mirror.composite.service.cmdb.payload.CompITDeviceCoutListResp;
import com.aspire.mirror.composite.service.cmdb.payload.CompITDeviceCoutRequest;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceProduce;
import com.aspire.ums.cmdb.assessment.payload.CmdbItDeviceCount;
import com.aspire.ums.cmdb.assessment.payload.CmdbProduceAssessment;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.assessment.CmdbDeviceProduceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.assessment.CmdbITDeviceCountClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.assessment.CmdbProduceAssessmentClient;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbITDeviceCountController
 * Author:   hangfang
 * Date:     2019/6/25
 * Description: des
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

@RestController
@Slf4j
public class CmdbITDeviceCountController implements IITDeviceCountAPI {

    @Autowired
    private CmdbITDeviceCountClient deviceCountClient;

    @Autowired
    private CmdbDeviceProduceClient deviceProduceClient;

    @Autowired
    private CmdbProduceAssessmentClient produceAssessmentClient;

    /**
     * 查询所有设备量信息
     * @return 设备量
     */
    @Override
    public Result<CmdbItDeviceCount> list(@RequestParam(value = "pageNum",required = false) Integer pageNum,
                                          @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                          @RequestParam(value = "page", required = false) String page,
                                          @RequestBody CmdbItDeviceCount deviceCount) {
//        CompITDeviceCoutListResp coutListResp = new CompITDeviceCoutListResp();
//        List<Map<String, Map<String, CmdbItDeviceCount>>> result = new ArrayList<>();
        Result<CmdbItDeviceCount> res = new Result<>();
        List<CmdbItDeviceCount> deviceCounts =  deviceCountClient.list(deviceCount);
        if (deviceCounts.size() > 0 && StringUtils.isNotEmpty(page) && page.contains("approve")){
            Integer status = deviceCounts.get(0).getStatus();
            if (status == 0 || status == 2) {
                res.setCount(0);
                return res;
            }
        }
//        List<CmdbDeviceProduce> deviceProduces = deviceProduceClient.list();
//        for (CmdbDeviceProduce produce : deviceProduces) {
//            Map<String, CmdbItDeviceCount> temp = new HashMap<>();
//            Map<String, Map<String, CmdbItDeviceCount>> proCount = new HashMap<>();
//            proCount.put(produce.getName(), temp);
//            result.add(proCount);
//        }
//
//        for (CmdbItDeviceCount count : deviceCounts) {
//            result.forEach(t -> {
//                if (t.containsKey(count.getProduce())) {
//                    t.get(count.getProduce()).put(count.getDeviceTypeId(), count);
//                }
//            });
//        }
//        coutListResp.setData(result);
//        coutListResp.setTotal(result.size());
        res.setData(deviceCounts);
        // 手动分页
//        if (null != pageNum && pageNum > 0 && null != pageSize ) {
//            Map<String, Map<String, CmdbItDeviceCount>> temp = new HashMap<>();
//            int index = 0;
//            int startIndex = (pageNum-1) * pageSize;
//            int endIndex = startIndex + pageSize;
//            for (Map.Entry entry : result.entrySet()) {
//                if (index >= startIndex && index < endIndex) {
//                    temp.put((String) entry.getKey(), (Map<String, CmdbItDeviceCount>)entry.getValue());
//                }
//                if (index >= endIndex) {
//                    break;
//                }
//                index ++;
//            }
//            coutListResp.setData(temp);
//        }
        if (deviceCounts.size() > 0 && StringUtils.isNotEmpty(page) && ("evaluate".equals(page) || "approve-eval".equals(page))){
            List<String> countIds = new ArrayList<>();
           deviceCounts.forEach(count -> {
               countIds.add(count.getId());
           });
            List<CmdbProduceAssessment> produceAssessments = produceAssessmentClient.listByCountIds(countIds);
           deviceCounts.forEach(count -> {
               List<CmdbProduceAssessment> proAssessments = new ArrayList<>();
              produceAssessments.forEach(assessment -> {
                  if (count.getId().equals(assessment.getDeviceCountId())) {
                      proAssessments.add(assessment);
                  }
              });
              count.setProduceAssessment(proAssessments);
           });
        }

        return res;
    }

    /**
     * 存储设备量信息
     */
    @Override
    public Map<String, Object> save(@RequestBody CompITDeviceCoutRequest deviceCoutRequest) {
        Map<String, Object> result = new HashMap<>();
        CompITDeviceCoutRequest.User user =  deviceCoutRequest.getUser();
        List<CmdbItDeviceCount> deviceCounts = new ArrayList<>();
        // 查询是否存在审批驳回状态
        CmdbItDeviceCount query = new CmdbItDeviceCount();
        List<CmdbItDeviceCount> queryResult = deviceCountClient.list(query);
        query.setStatus(0);
        List<CmdbItDeviceCount> queryStatusResult = deviceCountClient.list(query);
        if (queryResult.size() > 0 && queryStatusResult.size() == 0) {
            result.put("success", false);
            result.put("message", "保存/更新失败,当前季度已审批或待审批");
            return result;
        }
        deviceCoutRequest.getDeviceCounts().forEach(dCount ->{
//            for (Map.Entry entry : dCount.entrySet()) {
//                Map<String, CmdbItDeviceCount> deviceCount = (Map<String, CmdbItDeviceCount>) entry.getValue();
//                for (CmdbItDeviceCount count : deviceCount.values()) {
                    if (StringUtils.isEmpty(dCount.getId())) {
                        dCount.setCreateUsername(user.getCreateUsername());
                        dCount.setCreateUserPhone(user.getCreateUserPhone());
                        dCount.setProvince(user.getProvince());
                        dCount.setQuarter(user.getQuarter());
                    }
                    deviceCounts.add(dCount);
//                }
//            }
        });

        return deviceCountClient.save(deviceCounts);
    }

    @Override
    public Map<String, Object> delete(@RequestBody List<String> produces) {
        return deviceCountClient.delete(produces);
    }

    @Override
    public List<Map<String, Object>> getProvinceStatus(@RequestParam("quarter") String quarter) {
        return deviceCountClient.getProvinceStatus(quarter);
    }

    @Override
    public JSONObject getProduceAndDeviceList(@RequestBody Map<String, String> requestMp, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map<String, Object>> list = deviceCountClient.getProduceAndDeviceList(requestMp);
            String fileName = "评估得分信息导入模板.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (fileName, "UTF-8"))));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            String[] header = new String[] {"厂商名称[必填]","设备类型[必填]","培训服务[必填]","培训服务得分描述[必填]",
                    "属地化服务[必填]","属地化服务得分描述[必填]","服务专业性[必填]","服务专业性得分描述[必填]",
                    "服务稳定性和持续性[必填]","服务稳定性和持续性得分描述[必填]","与设备规范要求一致（总体性能、质量、技术规范、产品指标参数）[必填]",
                    "与设备规范要求一致（总体性能、质量、技术规范、产品指标参数）得分描述[必填]","技术文档质量[必填]","技术文档质量得分描述[必填]"};
            String[] keys = new String[] {"produce","pName"};
            OutputStream os = response.getOutputStream();// 取得输出流
            ExportExcelUtil eeu = new ExportExcelUtil();
            Workbook book = new SXSSFWorkbook(128);
            eeu.exportExcel(book, 0, "评估得分信息导入模板", header, list, keys);
            book.write(os);
            os.flush();
            os.close();
            jsonObject.put("flag", "success");
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return jsonObject;
    }
}
