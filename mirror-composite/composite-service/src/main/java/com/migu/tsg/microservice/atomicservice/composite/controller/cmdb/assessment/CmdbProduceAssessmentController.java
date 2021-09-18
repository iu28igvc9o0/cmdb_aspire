package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.assessment;

import com.aspire.mirror.composite.service.cmdb.assessment.IProduceAssessmentAPI;
import com.aspire.mirror.composite.service.cmdb.payload.CompITDeviceCoutRequest;
import com.aspire.ums.cmdb.assessment.payload.CmdbProduceAssessment;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.assessment.CmdbDeviceProduceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.assessment.CmdbProduceAssessmentClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbProduceAssessmentController
 * Author:   hangfang
 * Date:     2019/7/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@RestController
public class CmdbProduceAssessmentController implements IProduceAssessmentAPI {

    @Autowired
    private CmdbProduceAssessmentClient produceAssessmentClient;

    @Autowired
    private CmdbDeviceProduceClient deviceProduceClient;

    @Override
    public Map<String, Object> save(@RequestBody CompITDeviceCoutRequest coutRequest) {
        List<CmdbProduceAssessment> produceAssessments = new ArrayList<>();
        if (coutRequest.getDeviceCounts() != null) {
            coutRequest.getDeviceCounts().forEach(dCount -> {
//            for (Map.Entry entry : dCount.entrySet()) {
//                Map<String, CmdbItDeviceCount> deviceCount = (Map<String, CmdbItDeviceCount>) entry.getValue();
//                for (CmdbItDeviceCount count : deviceCount.values()) {
                if (dCount.getProduceAssessment() != null && dCount.getProduceAssessment().size() > 0) {
                    produceAssessments.addAll(dCount.getProduceAssessment());
                }
//                }
//            }
            });
        }

        if (produceAssessments.size() > 0) {
            return produceAssessmentClient.save(produceAssessments);
        }
        return null;
    }

    @Override
    public Map<String, Object> approval(@RequestParam("status") Integer status,
                                        @RequestParam("province") String province,
                                        @RequestParam("quarter") String quarter) {
        return produceAssessmentClient.approval(status, province, quarter);
    }

    @Override
    public Map<String, Object> makeEvaluate(@RequestParam("quarter") String quarter,
                                            @RequestParam("deviceType") String deviceType) {
        return produceAssessmentClient.makeEvaluate(quarter, deviceType);
    }

    @Override
    public Map<String, Object> exportEvaluate(@RequestParam("quarter") String quarter,
                                              @RequestParam("deviceType") String deviceType) {
        return produceAssessmentClient.exportEvaluate(quarter, deviceType);
    }

//    @Override
//    public Map<String, Object> downloadTemplate(HttpServletResponse response) {
////        String[] firstHeaders = {"事件信息", "厂家信息", "统计信息"};
//        String[] secondHeaders = { "事件名称","级别","发生省份","发生地市","厂家","设备类型",
//                "设备型号","典配类型","维保归属","故障部件类型","故障开始时间",
//                "故障消除时间","故障处理时长","故障处理次数", "备件部件更换次数"};
//        try {
//            Map<String, List<String>> selectDownData = new HashMap<>();
////            List<CmdbDeviceProduce> produceList = deviceProduceClient.list();
////            List<String> produces = new ArrayList<>();
////            produceList.forEach(produce -> {
////                produces.add(produce.getName());
////            });
//            OutputStream os = response.getOutputStream();// 取得输出流
//            try{
//
//                String title = "test";
//                String fileName = title + ".xls";
//                response.setHeader("Content-Disposition",
//                        "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
//                response.setHeader("Connection", "close");
//                response.setHeader("Content-Type", "application/vnd.ms-excel");
//                Workbook wb = new HSSFWorkbook();
//                // 创建一个sheet
//                ExportTemplateUtil templateUtil = new ExportTemplateUtil();
//                templateUtil.createExcel(wb, title, firstHeaders, secondHeaders);
//                Sheet sheet = wb.createSheet(title);
//                Cell cell = null;
//                Row row = null;
//                Row row = sheet.createRow(0);
//                for (int i = 0; i < headerList.length; i++) {
//                    Cell cell = row.createCell(i);
//
////                    cell.setCellStyle(style);
//
//                    HSSFRichTextString text = new HSSFRichTextString(headerList[i]);
//
//                    cell.setCellValue(text.toString());
//                }
//                sheet.addMergedRegion(new CellRangeAddress(0,1,0,1));
//                cell = row.createCell(0);
//                cell.setCellValue("测试");
//// 设置第一列的1-10行为下拉列表
//                CellRangeAddressList regions = new CellRangeAddressList(1, 2, 0, 0);
//                DVConstraint constraint = DVConstraint.createExplicitListConstraint((String[]) produceList.toArray());
//                HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
////                sheet.addValidationData(dataValidation);
//                wb.write(os);
//            } catch (Exception e) {
//            log.error("导出excel失败", e);
//        } finally {
//            os.flush();
//            os.close();
//        }
//
//        } catch (Exception e) {
//            log.error("导出sssexcel失败", e);
//        }
//
//        return null;
//    }
}
