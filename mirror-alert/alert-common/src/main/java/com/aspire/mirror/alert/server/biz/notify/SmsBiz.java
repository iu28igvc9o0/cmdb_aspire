package com.aspire.mirror.alert.server.biz.notify;

import com.aspire.mirror.alert.server.dao.notify.po.SmsRecord;
import com.aspire.mirror.alert.server.dao.notify.po.SmsTemplate;
import com.aspire.mirror.alert.server.dao.notify.po.SmsTemplateDetail;
import com.aspire.mirror.alert.server.vo.notify.SmsRecordExportVo;
import com.aspire.mirror.alert.server.vo.notify.SmsRecordReqVo;
import com.aspire.mirror.alert.server.vo.notify.SmsTemplateVo;
import com.aspire.mirror.common.entity.PageResponse;

import java.text.ParseException;
import java.util.Map;

/**
 * @author menglinjie
 */
public interface SmsBiz {

    /**
     * 发送短信
     * @param req
     * @return
     */
    Map<String, Object> sendSms(SmsRecordReqVo req);

    /**
     * 查询短信列表
     * @param startTime
     * @param endTime
     * @param receiver
     * @param content
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     * @throws ParseException
     */
    PageResponse<SmsRecord> findSmsList(String startTime, String endTime, String receiver, String content,
                                        Integer status, Integer pageNo, Integer pageSize) throws ParseException;

    /**
     * 新增短信模板分类
     * @param vo
     * @return
     */
    Map<String, Object> addSmsTemplate(SmsTemplateVo vo);
    
    /**
     * 编辑短信模板内容
     * @param vo
     * @return
     */
	Map<String, Object> editTemplateContent(SmsTemplateVo jacksonBaseParse);
	
	/**
     * 删除短信模板内容
     * @param vo
     * @return
     */
	Map<String, Object> deleteTemplateContent(SmsTemplateVo jacksonBaseParse);

    /**
     * 查询模板分类列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageResponse<SmsTemplate> findSmsTemplateList(Integer pageNo, Integer pageSize);

    /**
     * 将短信保存为模板
     * @param vo
     * @return
     */
    Map<String, Object> addContentToTemplate(SmsTemplateVo vo);

    /**
     * 删除模板
     * @param vo
     * @return
     */
    Map<String, Object> deleteTemplate(SmsTemplateVo vo);

    /**
     * 根据条件查询模板短信
     * @param templateId
     * @param content
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageResponse<SmsTemplateDetail> findDetailListByCondition(String templateId, String content, Integer pageNo, Integer pageSize);

    /**
     * 删除短信
     * @param req
     * @return
     */
    Map<String, Object> deleteSmsRecord(SmsRecordReqVo req);

    /**
     * 导出短信记录
     * @param vo
     */
    Map<String, Object> exportSmsRecord(SmsRecordExportVo vo) throws ParseException;
    
    /**
     * 编辑模板名字
     * @param jacksonBaseParse
     * @return
     */
	Map<String, Object> editTemplate(SmsTemplateVo jacksonBaseParse);

}
