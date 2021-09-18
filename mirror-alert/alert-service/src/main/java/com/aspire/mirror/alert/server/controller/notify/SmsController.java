package com.aspire.mirror.alert.server.controller.notify;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.alert.api.dto.notify.SmsListParam;
import com.aspire.mirror.alert.api.dto.notify.SmsRecordReq;
import com.aspire.mirror.alert.api.dto.notify.SmsTemplateDetailVo;
import com.aspire.mirror.alert.api.service.notify.SmsService;
import com.aspire.mirror.alert.server.aspect.RequestAuthContext;
import com.aspire.mirror.alert.server.biz.notify.SmsBiz;
import com.aspire.mirror.alert.server.dao.notify.po.SmsRecord;
import com.aspire.mirror.alert.server.dao.notify.po.SmsTemplate;
import com.aspire.mirror.alert.server.dao.notify.po.SmsTemplateDetail;
import com.aspire.mirror.alert.server.vo.notify.SmsRecordExportVo;
import com.aspire.mirror.alert.server.vo.notify.SmsRecordReqVo;
import com.aspire.mirror.alert.server.vo.notify.SmsTemplateVo;
import com.aspire.mirror.alert.server.util.PayloadParseUtil;
import com.aspire.mirror.common.entity.PageResponse;

/**
 * @author menglinjie
 */
@RestController
public class SmsController implements SmsService {

    @Resource
    private SmsBiz smsBiz;


    @Override
    public Map<String, Object> sendSms(@RequestBody SmsRecordReq req) {
    	RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
	    String account = authCtx.getUser().getUsername();
    	req.setSenderUuid(account);
        return smsBiz.sendSms(PayloadParseUtil.jacksonBaseParse(SmsRecordReqVo.class,req));
    }

    @Override
    public PageResponse<SmsRecordReq> findSmsList(@RequestBody SmsListParam param) throws ParseException {
        PageResponse<SmsRecord>  smsRecordPage = smsBiz.findSmsList(param.getStartTime(),param.getEndTime(),param.getReceiver(),
        		param.getContent(),param.getStatus(),param.getPageNo(),param.getPageSize());
        List<SmsRecord> smsRecordList = smsRecordPage.getResult();
        List<SmsRecordReq> recordReqList = PayloadParseUtil.jacksonBaseParse(SmsRecordReq.class,smsRecordList);
        PageResponse<SmsRecordReq>  smsRecordReqPage = new PageResponse<>();
        smsRecordReqPage.setCount(smsRecordPage.getCount());
        smsRecordReqPage.setResult(recordReqList);
        return smsRecordReqPage;
    }

    @Override
    public Map<String, Object> addSmsTemplate(@RequestBody List<com.aspire.mirror.alert.api.dto.notify.SmsTemplateVo> voList) {
    	Map<String, Object> map = new HashMap<>();
    	voList.forEach(it -> smsBiz.addSmsTemplate(PayloadParseUtil.jacksonBaseParse(SmsTemplateVo.class,it)));
    	map.put("state","success");
        return map;
    }

    @Override
    public PageResponse<com.aspire.mirror.alert.api.dto.notify.SmsTemplateVo> findSmsTemplateList(Integer pageNo, Integer pageSize) throws ParseException {
        PageResponse<SmsTemplate>  smsTemplatePage = smsBiz.findSmsTemplateList(pageNo,pageSize);
        List<SmsTemplate> smsTemplateList = smsTemplatePage.getResult();
        List<com.aspire.mirror.alert.api.dto.notify.SmsTemplateVo> templateReqList = PayloadParseUtil.jacksonBaseParse(com.aspire.mirror.alert.api.dto.notify.SmsTemplateVo.class,smsTemplateList);
        PageResponse<com.aspire.mirror.alert.api.dto.notify.SmsTemplateVo>  smsTemplateVoPage = new PageResponse<>();
        smsTemplateVoPage.setCount(smsTemplatePage.getCount());
        smsTemplateVoPage.setResult(templateReqList);
        return smsTemplateVoPage;
    }

    @Override
    public Map<String, Object> addContentToTemplate(@RequestBody com.aspire.mirror.alert.api.dto.notify.SmsTemplateVo vo) {
        return smsBiz.addContentToTemplate(PayloadParseUtil.jacksonBaseParse(SmsTemplateVo.class,vo));
    }
    
    @Override
	public Map<String, Object> editTemplate(@RequestBody com.aspire.mirror.alert.api.dto.notify.SmsTemplateVo vo) {
    	return smsBiz.editTemplate(PayloadParseUtil.jacksonBaseParse(SmsTemplateVo.class,vo));
	}
    
    @Override
	public Map<String, Object> editTemplateContent(@RequestBody com.aspire.mirror.alert.api.dto.notify.SmsTemplateVo vo) {
    	return smsBiz.editTemplateContent(PayloadParseUtil.jacksonBaseParse(SmsTemplateVo.class,vo));
	}

	@Override
	public Map<String, Object> deleteTemplateContent(@RequestBody com.aspire.mirror.alert.api.dto.notify.SmsTemplateVo vo) {
		return smsBiz.deleteTemplateContent(PayloadParseUtil.jacksonBaseParse(SmsTemplateVo.class,vo));
	}

    @Override
    public Map<String, Object> deleteTemplate(@RequestBody com.aspire.mirror.alert.api.dto.notify.SmsTemplateVo vo) {
        return smsBiz.deleteTemplate(PayloadParseUtil.jacksonBaseParse(SmsTemplateVo.class,vo));
    }

    @Override
    public PageResponse<SmsTemplateDetailVo> findDetailListByCondition(String templateId,String content, Integer pageNo, Integer pageSize) throws ParseException {
        PageResponse<SmsTemplateDetail>  smsTemplateDetailPage = smsBiz.findDetailListByCondition(templateId,content,pageNo,pageSize);
        List<SmsTemplateDetail> smsTemplateDetailList = smsTemplateDetailPage.getResult();
        List<SmsTemplateDetailVo> templateDetailReqList = PayloadParseUtil.jacksonBaseParse(SmsTemplateDetailVo.class,smsTemplateDetailList);
        PageResponse<SmsTemplateDetailVo>  smsTemplateDetailVoPage = new PageResponse<>();
        smsTemplateDetailVoPage.setCount(smsTemplateDetailPage.getCount());
        smsTemplateDetailVoPage.setResult(templateDetailReqList);
        return smsTemplateDetailVoPage;
    }

    @Override
    public Map<String, Object> deleteSmsRecord(@RequestBody SmsRecordReq req) {
        return smsBiz.deleteSmsRecord(PayloadParseUtil.jacksonBaseParse(SmsRecordReqVo.class,req));
    }

    @Override
    public Map<String, Object> exportSmsRecord(@RequestBody com.aspire.mirror.alert.api.dto.notify.SmsRecordExportVo vo) throws ParseException {
        return smsBiz.exportSmsRecord(PayloadParseUtil.jacksonBaseParse(SmsRecordExportVo.class, vo));
    }
}
