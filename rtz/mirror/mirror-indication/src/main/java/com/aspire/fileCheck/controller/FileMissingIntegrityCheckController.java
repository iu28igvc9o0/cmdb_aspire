package com.aspire.fileCheck.controller;

import com.aspire.fileCheck.entity.FileIndicationEntity;
import com.aspire.fileCheck.service.IFileIndicationPeriodConfigService;
import com.aspire.fileCheck.service.IFileMissingCheckService;
import com.aspire.fileCheck.service.IMirrorDictService;
import com.aspire.fileCheck.util.FileMirrorDict;
import com.aspire.fileCheck.FileMissingIntegrityCheckAPI;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
public class FileMissingIntegrityCheckController implements FileMissingIntegrityCheckAPI {

    @Autowired
    private IFileIndicationPeriodConfigService iFileIndicationPeriodConfigService;

    @Autowired
    private IFileMissingCheckService fileMissingCheckService;

    @Autowired
    private IMirrorDictService mirrorDictService;

    /**
     *
     * @param type
     * @param province
     * @param date
     * @param period
     * @param missing
     * @return
     */
    public Object getFileMissingIntegrityCheckData(@RequestParam(value = "type") String type,
                                                   @RequestParam(value = "province",required = false) String province,
                                                   @RequestParam(value = "date",required = false) String date,
                                                   @RequestParam(value = "period",required = false) String period,
                                                   @RequestParam(value = "missing",required = false) String missing,
                                                   @RequestParam(value = "fileIndication", required = false) String fileIndication,
                                                   @RequestParam(value = "currentPage") Integer currentPage,
                                                   @RequestParam(value = "pageSize") Integer pageSize) {
        return fileMissingCheckService.getAllFileMissingData(type, province, date, period, missing, fileIndication, currentPage, pageSize);
    }

    @Override
    public Object getFileMissingIntegrityCheckStatus() {
        return mirrorDictService.getMirrorDictByDictName(FileMirrorDict.FILE_STATE_STATUS);
    }

}
