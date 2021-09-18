package com.aspire.mirror.alert.server.biz.notify.impl;

import com.aspire.mirror.alert.server.biz.ftp.FtpService;
import com.aspire.mirror.alert.server.biz.notify.SmsBiz;
import com.aspire.mirror.alert.server.biz.helper.SmSendHelper;
import com.aspire.mirror.alert.server.dao.notify.SmsRecordDao;
import com.aspire.mirror.alert.server.dao.notify.SmsTemplateDao;
import com.aspire.mirror.alert.server.dao.notify.SmsTemplateDetailDao;
import com.aspire.mirror.alert.server.dao.notify.po.SmsRecord;
import com.aspire.mirror.alert.server.dao.notify.po.SmsTemplate;
import com.aspire.mirror.alert.server.dao.notify.po.SmsTemplateDetail;
import com.aspire.mirror.alert.server.vo.notify.SmsRecordExportVo;
import com.aspire.mirror.alert.server.vo.notify.SmsRecordReqVo;
import com.aspire.mirror.alert.server.vo.notify.SmsTemplateVo;
import com.aspire.mirror.alert.server.util.ExportExcelUtil;
import com.aspire.mirror.alert.server.util.TransformUtils;
import com.aspire.mirror.common.entity.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author menglinjie
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SmsBizImpl implements SmsBiz {

    @Resource
    private SmSendHelper smSendHelper;
    @Resource
    private SmsRecordDao smsRecordDao;
    @Resource
    private SmsTemplateDao smsTemplateDao;
    @Resource
    private SmsTemplateDetailDao smsTemplateDetailDao;
    @Resource
    private FtpService ftpService;

    @Override
    public Map<String, Object> sendSms(SmsRecordReqVo req) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String,Object>> receivers = req.getReceivers();
        Map<String,String> stringStringMap = new HashMap<>();
        Map<String,String> stringStringMap1 = new HashMap<>();
        List<String> mobileList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(receivers)){
            for (Map<String,Object> receiver : receivers){
            	String mobile  = String.valueOf(receiver.get("mobile"));
            	if(stringStringMap.containsKey(mobile)){
            		continue;
            	}
            	if(receiver.containsKey("uuid")){
            		String uuid  = String.valueOf(receiver.get("uuid"));
            		stringStringMap.put(mobile,uuid);
                }
            	if(receiver.containsKey("name")){
            		String name  = String.valueOf(receiver.get("name"));
            		stringStringMap1.put(mobile,name);
                }
                mobileList.add(mobile);
            }
        }

        String content = req.getContent();
        if (CollectionUtils.isNotEmpty(mobileList)){
        	Map<String,String> mobileIdMap = new HashMap<>();
        	for(String mobile : mobileList){
        		mobileIdMap.put(mobile, UUID.randomUUID().toString());
        	}
            //发送短信
            boolean flag = smSendHelper.sendSms(mobileList, content,mobileIdMap);
            String errorLog = null;
            Integer status = 1;
            if (!flag) {
                errorLog =  "send sms failed!";
                status = 0;
            }
            //保存发送记录
            Date date = new Date();
            for (String mobile : mobileList){
            	String uuid = mobileIdMap.get(mobile);
                req.setId(uuid);
                req.setReceiverMobile(mobile);
                if (stringStringMap.containsKey(mobile)){
                    req.setReceiverUuid(stringStringMap.get(mobile));
                }
                if (stringStringMap1.containsKey(mobile)){
                    req.setReceiverName(stringStringMap1.get(mobile));
                }
                req.setCreateTime(date);
                req.setStatus(status);
                req.setIsDelete(0);
                req.setErrorLog(errorLog);
                SmsRecord smsRecord = TransformUtils.transform(SmsRecord.class, req);
                smsRecordDao.insert(smsRecord);
            }
        }
        map.put("state","success");
        return map;
    }

    @Override
    public PageResponse<SmsRecord> findSmsList(String startTime, String endTime, String receiver,
                                           String content, Integer status, Integer pageNo,
                                           Integer pageSize) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDateTime = null;
        Date endDateTime = null;
        if (StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            startDateTime =formatter.parse(startTime);
            endDateTime =formatter.parse(endTime);
        }
        int count = smsRecordDao.countFindSmsList(startDateTime,
                endDateTime,receiver,content,status);
        List<SmsRecord> list = smsRecordDao.findSmsList(startDateTime,
                endDateTime,receiver,content, status,(pageNo-1)*pageSize, pageSize);
        PageResponse<SmsRecord> smsRecordPage = new PageResponse<>();
        smsRecordPage.setResult(list);
        smsRecordPage.setCount(count);
        return smsRecordPage;
    }

    @Override
    public Map<String, Object> addSmsTemplate(SmsTemplateVo vo) {
        Map<String, Object> map = new HashMap<>();
        if(vo.getId() == null){
	        Date date = new Date();
	        vo.setId(UUID.randomUUID().toString());
	        vo.setCreateTime(date);
	        SmsTemplate smsTemplate = TransformUtils.transform(SmsTemplate.class, vo);
	        smsTemplateDao.insert(smsTemplate);
        }else{
        	//编辑，ID不为空
        	Date date = new Date();
  	        vo.setUpdateTime(date);
  	        SmsTemplate smsTemplate = TransformUtils.transform(SmsTemplate.class, vo);
        	smsTemplateDao.updateByPrimaryKey(smsTemplate);
        }
        map.put("state","success");
        return map;
    }

    @Override
    public PageResponse<SmsTemplate> findSmsTemplateList(Integer pageNo, Integer pageSize) {
        List<SmsTemplate> all =  smsTemplateDao.selectAll();
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize =Integer.MAX_VALUE;
        }
        List<SmsTemplate> list = smsTemplateDao.findSmsTemplateList((pageNo-1)*pageSize, pageSize);
        PageResponse<SmsTemplate> smsTemplatePage = new PageResponse<>();
        smsTemplatePage.setResult(list);
        smsTemplatePage.setCount(all.size());
        return smsTemplatePage;
    }

    @Override
    public Map<String, Object> addContentToTemplate(SmsTemplateVo vo) {
        Map<String, Object> map = new HashMap<>();
        Date date = new Date();
        String templateId = vo.getId();
        //先判断是否为新建模板
        if (StringUtils.isBlank(vo.getId()) && StringUtils.isNotBlank(vo.getName())){
            vo.setId(UUID.randomUUID().toString());
            vo.setCreateTime(date);
            vo.setUpdateTime(date);
            SmsTemplate smsTemplate = TransformUtils.transform(SmsTemplate.class, vo);
            smsTemplateDao.insert(smsTemplate);
            templateId = smsTemplate.getId();
        }
        if (StringUtils.isNotBlank(vo.getContent())){
            SmsTemplateDetail templateDetail = new SmsTemplateDetail();
            templateDetail.setId(UUID.randomUUID().toString());
            templateDetail.setContent(vo.getContent());
            templateDetail.setSmsTemplateId(templateId);
            templateDetail.setCreateTime(date);
            smsTemplateDetailDao.insert(templateDetail);
        }
        map.put("state","success");
        return map;
    }
    

	@Override
	public Map<String, Object> editTemplateContent(SmsTemplateVo vo) {
		Map<String, Object> map = new HashMap<>();
		SmsTemplateDetail templateDetail = new SmsTemplateDetail();
		templateDetail.setContent(vo.getContent());
		templateDetail.setId(vo.getId());
		smsTemplateDetailDao.updateByPrimaryKey(templateDetail);
		map.put("state","success");
	    return map;
	}

	@Override
	public Map<String, Object> deleteTemplateContent(SmsTemplateVo vo) {
		Map<String, Object> map = new HashMap<>();
		smsTemplateDetailDao.deleteByPrimaryKey(vo.getId());
		map.put("state","success");
	    return map;
	}

    @Override
    public Map<String, Object> deleteTemplate(SmsTemplateVo vo) {
        Map<String, Object> map = new HashMap<>();
        if(CollectionUtils.isNotEmpty(vo.getIdList())){
        	vo.getIdList().forEach(it -> deleteTemplate2(it));
        }
        if(vo.getId() != null){
        	deleteTemplate2(vo.getId());
        }
        map.put("state","success");
        return map;
    }

    private void deleteTemplate2(String id) {
    	smsTemplateDao.deleteByPrimaryKey(id);
        smsTemplateDetailDao.deleteByTemplateId(id);
	}

	@Override
    public PageResponse<SmsTemplateDetail> findDetailListByCondition(String templateId, String content, Integer pageNo, Integer pageSize) {
        Integer count =  smsTemplateDetailDao.countFindDetailListByCondition(templateId,content);
        List<SmsTemplateDetail> list = smsTemplateDetailDao.findDetailListByCondition(templateId,content,(pageNo-1)*pageSize, pageSize);
        PageResponse<SmsTemplateDetail> smsTemplateDetailPage = new PageResponse<>();
        smsTemplateDetailPage.setResult(list);
        smsTemplateDetailPage.setCount(count);
        return smsTemplateDetailPage;
    }

    @Override
    public Map<String, Object> deleteSmsRecord(SmsRecordReqVo req) {
        Map<String, Object> map = new HashMap<>();
        if(CollectionUtils.isNotEmpty(req.getIdList())){
        	req.getIdList().forEach(it ->smsRecordDao.updateIsDelete(it));
        }
        if(req.getId() != null){
        	smsRecordDao.updateIsDelete(req.getId());
        }
        map.put("state","success");
        return map;
    }

    @Override
    public Map<String, Object> exportSmsRecord(SmsRecordExportVo vo) throws ParseException {
        Map<String, Object> map = new HashMap<>();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDateTime = null;
        Date endDateTime = null;
        if (StringUtils.isNotBlank(vo.getStartTime())&&StringUtils.isNotBlank(vo.getEndTime())){
            startDateTime =formatter.parse(vo.getStartTime());
            endDateTime =formatter.parse(vo.getEndTime());
        }
        List<SmsRecord> list = smsRecordDao.findSmsList(startDateTime,
                endDateTime,vo.getReceiver(),vo.getContent(), vo.getStatus(),null, null);
        String[] headerList = {"状态    "," 联系人      ","短信内容    "," 发送时间      " };
        String[] keyList = {"status","receiverMobile","content","createTime" };


        String title = "往来记录";
        String fileName = title+".xlsx";

        try {
            List<Map<String, Object>> dataLists = new ArrayList<Map<String,Object>>();

            for (SmsRecord smsRecord : list) {
                Map<String, Object>  stringObjectMap= new HashMap<>();
                if (smsRecord.getStatus() == 1){
                    stringObjectMap.put("status","发送成功");
                }else {
                    stringObjectMap.put("status","发送失败");
                }
                String receiverName = smsRecord.getReceiverName() == null ? "" :smsRecord.getReceiverName() + "<";
                String receiver =  receiverName + smsRecord.getReceiverMobile().toString() + (smsRecord.getReceiverName() == null ? "": ">");
                stringObjectMap.put("receiverMobile", receiver);
                stringObjectMap.put("content",smsRecord.getContent());
                String createTimeStr = formatter.format(smsRecord.getCreateTime());
                stringObjectMap.put("createTime",createTimeStr);

                dataLists.add(stringObjectMap);

            }
            //excel constuct
            ExportExcelUtil eeu = new ExportExcelUtil();
            SXSSFWorkbook book = new SXSSFWorkbook(128);
            book.setCompressTempFiles(true);
            eeu.exportExcel(book, 0, title, headerList, dataLists, keyList );

            ByteArrayOutputStream ops = null;
            ByteArrayInputStream in = null;
            try {
                ops = new ByteArrayOutputStream();
                book.write(ops);
                byte[] b = ops.toByteArray();
                in = new ByteArrayInputStream(b);
                map = ftpService.uploadtoFTP(fileName, in);
                ops.flush();
            } catch (Exception e) {
                log.error("导出excel失败，失败原因：", e);
                map.put("code","0005");
                map.put("message", e.getMessage());
            }finally {
                IOUtils.closeQuietly(book);
                IOUtils.closeQuietly(ops);
                IOUtils.closeQuietly(in);
                return map;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return map;
    }

	@Override
	public Map<String, Object> editTemplate(SmsTemplateVo vo) {
		 Map<String, Object> map = new HashMap<>();
		 SmsTemplate smsTemplate = TransformUtils.transform(SmsTemplate.class, vo);
		 smsTemplateDao.updateByPrimaryKey(smsTemplate);
		 map.put("state","success");
		 return map;
	}
}
