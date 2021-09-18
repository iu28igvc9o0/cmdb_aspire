package com.aspire.fileCheck.controller;

import com.aspire.fileCheck.FileUploadDelayDetectionAPI;
import com.aspire.fileCheck.entity.FileIndicationEntity;
import com.aspire.fileCheck.service.IFileIndicationJobManagerService;
import com.aspire.fileCheck.service.IFileIndicationPeriodConfigService;
import com.aspire.fileCheck.service.IFileIndicationUploadService;
import com.aspire.fileCheck.service.IMirrorDictService;
import com.aspire.fileCheck.util.FileMirrorDict;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
public class FileUploadDelayDetectionController implements FileUploadDelayDetectionAPI {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FileUploadDelayDetectionController.class);

    @Autowired
    private IFileIndicationPeriodConfigService iFileIndicationPeriodConfigService;
    @Autowired
    private IMirrorDictService mirrorDictService;
    @Autowired
    private IFileIndicationUploadService fileIndicationUploadService;
    @Autowired
    private IFileIndicationJobManagerService jobManagerService;

    /**
     * 获取异常数据
     * @param type
     * @param province
     * @param date
     * @param period
     * @param detection
     * @return Object
     */
    public Object getIndicationData(@RequestParam(value = "type") String type,
                                    @RequestParam(value = "province",required = false) String province,
                                    @RequestParam(value = "date",required = false) String date,
                                    @RequestParam(value = "period",required = false) String period,
                                    @RequestParam(value = "detection",required = false) String detection,
                                    @RequestParam(value = "fileIndication", required = false) String fileIndication) {
        LOGGER.info("[Request]>>>"+type+">>>"+province+">>>"+date+">>>"+period+">>>"+detection);
        Map<String,Object> response = Maps.newHashMap();

        province = "000".equals(province) ? "" : province;
        Integer newDetection = "0".equals(detection) ? null : Integer.parseInt(detection);
        // 组装日期
        if (StringUtils.isNotBlank(date)) {
            date = date.split("-")[0] + date.split("-")[1] + date.split("-")[2];
        }
        //请求参数
        Map<String,Object> param = Maps.newHashMap();
        param.put("province",province);
        param.put("type",type);
        param.put("period",period);
        param.put("date",date);
        param.put("detection",newDetection);

        List<FileIndicationEntity> FileIndicationList = iFileIndicationPeriodConfigService.getFileIndication(type);
        for (FileIndicationEntity indicationEntity : FileIndicationList) {
            if (StringUtils.isNotEmpty(fileIndication) && indicationEntity.getFileIndicationId() != Integer.parseInt(fileIndication)) {
                continue;
            }
            String name = indicationEntity.getFileIndicationName();
            param.put("fileIndicationId",indicationEntity.getFileIndicationId());
            final List<Map<String, String>> fileUploadDelayDetectionData = fileIndicationUploadService.getFileUploadDelayDetectionData(param);
            if (fileUploadDelayDetectionData.isEmpty()) {
                response.put(name,new ArrayList<Map<String, String>>());
            } else {
                response.put(name,fileUploadDelayDetectionData);
            }
        }
        LOGGER.info("[Response]>>>"+response);
        return response;
    }


    /**
     * 获取文件上传延时状态
     * @return Object
     */
    public Object getFileUploadDetectionStatus() {
        return mirrorDictService.getMirrorDictByDictName(FileMirrorDict.LAZY_STATUS);
    }

    /**
     * 获取文件检测时间段
     * @return Object
     */
    public Object getFileCheckTimePeriod() {
        return mirrorDictService.getMirrorDictByDictName(FileMirrorDict.PERIOD);
    }

    @Override
    public Object getLatestRunningPeriod(@RequestParam(value = "jobName") String jobName) {
        return jobManagerService.getLatestJobManagerByJobName(jobName);
    }
}
