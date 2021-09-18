package com.migu.tsg.microservice.atomicservice.composite.controller.notify;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.aspire.mirror.alert.api.dto.notify.*;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.notify.CompSmsRecordExportVo;
import com.aspire.mirror.composite.payload.notify.CompSmsRecordReq;
import com.aspire.mirror.composite.payload.notify.CompSmsTemplateVo;
import com.aspire.mirror.composite.payload.notify.SmsListParamReq;
import com.aspire.mirror.composite.service.notify.ICompSmsService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.notify.SmsServiceClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author menglinjie
 */
@RestController
@Slf4j
public class CompSmsController implements ICompSmsService {


    @Resource
    private SmsServiceClient smsServiceClient;

    @Override
    public ResponseEntity<Map> sendSms(@RequestBody CompSmsRecordReq compSmsRecordReq) {
        Map<String ,Object> map = new HashMap<>();
        if (null == compSmsRecordReq) {
            log.error("sms >>> sendSms Request is null");
            throw new RuntimeException("sendSms Request is null");
        }
        try {
            map = smsServiceClient.sendSms(PayloadParseUtil.jacksonBaseParse(SmsRecordReq.class, compSmsRecordReq));
        } catch (Exception e) {
            log.error("sms >>> sendSms Error is {}",e);
            map.put("state",e.toString());
            return new ResponseEntity<Map>(map, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map> findSmsList(@RequestBody SmsListParamReq smsParam) {
        Map<String ,Object> map = new HashMap<>();
        if (null == smsParam.getPageNo()||null == smsParam.getPageSize() ) {
            log.error("sms >>> pageNo or pageSize is null");
            throw new RuntimeException("pageNo or pageSize is null");
        }
        try {
        	
            PageResponse<SmsRecordReq> smsRecordReqPage = smsServiceClient.findSmsList(PayloadParseUtil.jacksonBaseParse(SmsListParam.class, smsParam));
            map.put("state","success");
            map.put("data",smsRecordReqPage);
        } catch (Exception e) {
            log.error("sms >>> findSmsList Error is {}",e);
            map.put("state",e.toString());
            return new ResponseEntity<Map>(map, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map> addSmsTemplate(@RequestBody List<CompSmsTemplateVo> voList) {
        Map<String ,Object> map = new HashMap<>();
        try {
            map = smsServiceClient.addSmsTemplate(PayloadParseUtil.jacksonBaseParse(SmsTemplateVo.class, voList));
        } catch (Exception e) {
            log.error("sms >>> addSmsTemplate Error is {}",e);
            map.put("state",e.toString());
            return new ResponseEntity<Map>(map, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }
    

    @Override
    public ResponseEntity<Map> editTemplate(@RequestBody CompSmsTemplateVo vo) {
        Map<String ,Object> map = new HashMap<>();
        if (StringUtils.isBlank(vo.getName())) {
            log.error("sms >>> editSmsTemplate Request(name) is null");
            throw new RuntimeException("editSmsTemplate Request(name) is null");
        }
        try {
            map = smsServiceClient.editTemplate(PayloadParseUtil.jacksonBaseParse(SmsTemplateVo.class, vo));
        } catch (Exception e) {
            log.error("sms >>> editSmsTemplate Error is {}",e);
            map.put("state",e.toString());
            return new ResponseEntity<Map>(map, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }
    
	@Override
	public ResponseEntity<Map> deleteTemplateContent(@RequestBody CompSmsTemplateVo vo) {
		Map<String ,Object> map = new HashMap<>();
        if (StringUtils.isBlank(vo.getId())) {
            log.error("sms >>> deleteTemplateContent Request(id) is null");
            throw new RuntimeException("deleteTemplateContent Request(id) is null");
        }
        try {
            map = smsServiceClient.deleteTemplateContent(PayloadParseUtil.jacksonBaseParse(SmsTemplateVo.class, vo));
        } catch (Exception e) {
            log.error("sms >>> deleteTemplateContent Error is {}",e);
            map.put("state",e.toString());
            return new ResponseEntity<Map>(map, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map>(map, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Map> editTemplateContent(@RequestBody CompSmsTemplateVo vo) {
		Map<String ,Object> map = new HashMap<>();
		if (StringUtils.isBlank(vo.getId())) {
            log.error("sms >>> editTemplateContent Request(id) is null");
            throw new RuntimeException("editTemplateContent Request(id) is null");
        }
        try {
            map = smsServiceClient.editTemplateContent(PayloadParseUtil.jacksonBaseParse(SmsTemplateVo.class, vo));
        } catch (Exception e) {
            log.error("sms >>> deleteTemplateContent Error is {}",e);
            map.put("state",e.toString());
            return new ResponseEntity<Map>(map, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map>(map, HttpStatus.OK);
	}
    
    @Override
    public ResponseEntity<Map> findSmsTemplateList(Integer pageNo, Integer pageSize) throws ParseException {
        Map<String ,Object> map = new HashMap<>();
        try {
            PageResponse<SmsTemplateVo> smsTemplateVoPage = smsServiceClient.findSmsTemplateList(pageNo,pageSize);
            map.put("state","success");
            map.put("data",smsTemplateVoPage);
        } catch (Exception e) {
            log.error("sms >>> findSmsTemplateList Error is {}",e);
            map.put("state",e.toString());
            return new ResponseEntity<Map>(map, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map> addContentToTemplate(@RequestBody CompSmsTemplateVo vo) {
        Map<String ,Object> map = new HashMap<>();
        if (StringUtils.isBlank(vo.getContent())) {
            log.error("sms >>> addContentToTemplate Request(content) is null");
            throw new RuntimeException("addSmsTemplate Request(content) is null");
        }
        try {
            map = smsServiceClient.addContentToTemplate(PayloadParseUtil.jacksonBaseParse(SmsTemplateVo.class, vo));
        } catch (Exception e) {
            log.error("sms >>> addContentToTemplate Error is {}",e);
            map.put("state",e.toString());
            return new ResponseEntity<Map>(map, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map> deleteTemplate(@RequestBody CompSmsTemplateVo vo) {
        Map<String ,Object> map = new HashMap<>();
        try {
            map = smsServiceClient.deleteTemplate(PayloadParseUtil.jacksonBaseParse(SmsTemplateVo.class, vo));
        } catch (Exception e) {
            log.error("sms >>> deleteTemplate Error is {}",e);
            map.put("state",e.toString());
            return new ResponseEntity<Map>(map, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map> findDetailListByCondition(String templateId,String content, Integer pageNo, Integer pageSize) throws ParseException {
        Map<String ,Object> map = new HashMap<>();
        if (null == pageNo ||null == pageSize ) {
            log.error("sms >>> pageNo or pageSize is null");
            throw new RuntimeException("pageNo or pageSize is null");
        }
        try {
            PageResponse<SmsTemplateDetailVo> smsTemplateDetailPage = smsServiceClient.findDetailListByCondition(templateId, content, pageNo, pageSize);
            map.put("state","success");
            map.put("data",smsTemplateDetailPage);
        } catch (Exception e) {
            log.error("sms >>> findDetailListByTemplateId Error is {}",e);
            map.put("state",e.toString());
            return new ResponseEntity<Map>(map, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map> deleteSmsRecord(@RequestBody CompSmsRecordReq req) {
        Map<String ,Object> map = new HashMap<>();
        try {
            map = smsServiceClient.deleteSmsRecord(PayloadParseUtil.jacksonBaseParse(SmsRecordReq.class, req));
        } catch (Exception e) {
            log.error("sms >>> deleteSmsRecord Error is {}",e);
            map.put("state",e.toString());
            return new ResponseEntity<Map>(map, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map> exportSmsRecord(@RequestBody CompSmsRecordExportVo vo) throws ParseException {
        Map<String ,Object> map = new HashMap<>();
        try {
            map = smsServiceClient.exportSmsRecord(PayloadParseUtil.jacksonBaseParse(SmsRecordExportVo.class, vo));
        } catch (Exception e) {
            log.error("sms >>> exportSmsRecord Error is {}",e);
            map.put("state",e.toString());
            return new ResponseEntity<Map>(map, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }
}
