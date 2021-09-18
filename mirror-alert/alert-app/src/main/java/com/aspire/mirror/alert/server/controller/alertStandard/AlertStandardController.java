package com.aspire.mirror.alert.server.controller.alertStandard;

import com.aspire.mirror.alert.api.dto.alertStandard.AlertStandardReq;
import com.aspire.mirror.alert.api.dto.alertStandard.AlertStandardResp;
import com.aspire.mirror.alert.api.service.alertStandard.AlertStandardService;
import com.aspire.mirror.alert.server.biz.alertStandard.AlertStandardBiz;
import com.aspire.mirror.alert.server.dao.alertStandard.po.AlertStandard;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @projectName: AlertStandardController
 * @description: 类
 * @author: luowenbo
 * @create: 2020-06-10 17:24
 **/
@RestController
public class AlertStandardController implements AlertStandardService {
    @Autowired
    private AlertStandardBiz biz;

    @Override
    public ResponseEntity<String> insert(@RequestBody AlertStandardReq asReq) {
        if (asReq == null) {
            throw new RuntimeException("create param is null");
        }
        AlertStandard as = new AlertStandard();
        BeanUtils.copyProperties(asReq,as);
        biz.insert(as);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> update(@RequestBody AlertStandardReq asReq) {
        if (asReq.getId() == null || "".equals(asReq.getId() )) {
            throw new RuntimeException("update id is null");
        }
        AlertStandard as = new AlertStandard();
        BeanUtils.copyProperties(asReq,as);
        biz.update(as);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteByIds(@RequestParam("operator") String operator,
                                              @RequestBody String[] ids) {
        if (StringUtils.isEmpty(ids)) {
            throw new RuntimeException("delete ids is null");
        }
        biz.deleteByIds(operator,ids);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Override
    public PageResponse<AlertStandardResp> listWithPage(@RequestParam(value = "deviceClass",required = false) String deviceClass,
                                                        @RequestParam(value = "deviceType",required = false) String deviceType,
                                                        @RequestParam(value = "monitorKey",required = false) String monitorKey,
                                                        @RequestParam(value = "pageNo",defaultValue = "1") int pageNo,
                                                        @RequestParam(value = "pageSize",defaultValue = "50") int pageSize) {
        Map<String,Object> paramMp = new HashMap<>();
        paramMp.put("deviceClass",deviceClass);
        paramMp.put("deviceType",deviceType);
        paramMp.put("monitorKey",monitorKey);
        paramMp.put("pageNo",pageNo);
        paramMp.put("pageSize",pageSize);

        PageResponse<AlertStandard> pageResult = biz.listWithPage(paramMp);
        List<AlertStandardResp> listAlert = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(pageResult.getResult())) {
            for (AlertStandard standard : pageResult.getResult()) {
                AlertStandardResp standardResp = new AlertStandardResp();
                BeanUtils.copyProperties(standard, standardResp);
                listAlert.add(standardResp);
            }
        }

        PageResponse<AlertStandardResp> result = new PageResponse<>();
        result.setCount(pageResult.getCount());
        result.setResult(listAlert);
        return result;
    }

    @Override
    public ResponseEntity<String> operatorStatus(@RequestParam("operator") String operator,
                                                 @RequestBody String[] ids) {
        if(ids.length == 0) {
            throw new RuntimeException("update id is null");
        }
        biz.operatorStatus(operator,ids);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateHistory(@RequestBody Map<String,String> req) {
        String start = req.get("startTime");
        String end = req.get("endTime");
        String operator = req.get("operator");
        if(start == null || end == null) {
            throw new RuntimeException("time is null");
        }
        biz.updateAlertHistory(operator,start,end);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateHistoryOneRow(@PathVariable("id") String id,
                                                      @RequestBody Map<String,String> req) {
        String start = req.get("startTime");
        String end = req.get("endTime");
        String operator = req.get("operator");
        if(start == null || end == null) {
            throw new RuntimeException("time is null");
        }
        biz.updateAlertHistoryOneRow(operator,id,start,end);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> importAlertStandard(@RequestPart("file") MultipartFile file,
                                                      @RequestParam("operator") String operator) {
        biz.uploadAS(file,operator);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Override
    public List<Map<String,Object>> exportAlertStandard(@RequestParam(value = "deviceClass",required = false) String deviceClass,
                                                      @RequestParam(value = "deviceType",required = false) String deviceType,
                                                      @RequestParam(value = "monitorKey",required = false) String monitorKey) {
        Map<String,Object> paramMp = new HashMap<>();
        paramMp.put("deviceClass",deviceClass);
        paramMp.put("deviceType",deviceType);
        paramMp.put("monitorKey",monitorKey);
        return biz.exportAll(paramMp);
    }
}
