package com.aspire.fileCheck.controller;

import com.aspire.common.BaseController;
import com.aspire.fileCheck.IFileReportAPI;
import com.aspire.fileCheck.entity.FileIndicationEntity;
import com.aspire.fileCheck.entity.FileStateOtherReportEntity;
import com.aspire.fileCheck.entity.FileStateReportItem;
import com.aspire.fileCheck.entity.ReportResponse;
import com.aspire.fileCheck.service.IFileIndicationPeriodStateService;
import com.aspire.fileCheck.service.IFileIndicationService;
import com.aspire.fileCheck.service.IMirrorDictService;
import com.aspire.fileCheck.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

@RestController
@Slf4j
public class FileReportController extends BaseController implements IFileReportAPI{
	@Autowired
    private IFileIndicationPeriodStateService iFileIndicationPeriodStateService;
	@Autowired
	private IFileIndicationService fileIndicationService;
	@Autowired
	private IMirrorDictService mirrorDictService;
	private int scale = 4;
	
	@Override
	public String getFileReport(String catolog, String provinceCode, String period, String uploadDate, String fileIndication) {
		Map<String, Map<String, Map<String, ReportResponse>>> indicationMap=  iFileIndicationPeriodStateService.getReport(catolog, provinceCode, period, uploadDate, fileIndication);
		Date now = new Date();
		String curPeriod = TimeUtil.getCalcPeriod(now);//获取检测时间段时间
		String curDateStr = getTime(now, 3);
		JSONArray indicationArray = new JSONArray();
		for(Map.Entry<String,Map<String,Map<String,ReportResponse>>> entry : indicationMap.entrySet()) {
			String key = entry.getKey();
			Map<String,Map<String,ReportResponse>> provinceMap = entry.getValue();
			
			JSONObject indicationJb = new JSONObject();
			indicationJb.put("fileIndicationName", key);
			JSONArray provinceArray = new JSONArray();
			for(Map.Entry<String,Map<String,ReportResponse>> entry1 : provinceMap.entrySet()) {
				String pKey = entry1.getKey();
				JSONObject provinceJb = new JSONObject();
				provinceJb.put("provinceName", pKey);
				provinceJb.put("uploadDate",uploadDate);
				List<ReportResponse> validData = new ArrayList<ReportResponse> ();
				for(ReportResponse re:entry1.getValue().values()) {
					FileStateReportItem data = re.getData();
					FileStateReportItem preData = re.getPreData();
					re.getDictValue();
					re.getUploadDate();
					//过滤非存在当天时区的数据
					if(!checkTime(uploadDate,curDateStr,curPeriod,re.getDictValue())) {
						if(curPeriod.equals(re.getDictValue())) {
							if(data!=null) {
								validData.add(re);
							}else {
								continue;
							}
							
						}else {
							validData.add(re);
						}
						
					}else {
						continue;
					}
					if(data !=null) {
						data.setUpdateIntegrity(data.getUpdateIntegrity()*100);
						double dataMixCount = data.getFileOneHour()+data.getFileTowHour()+data.getFileTwoMoreHour();
						if(dataMixCount>0) {
							 BigDecimal b1 ;
						     BigDecimal b2 = new BigDecimal(dataMixCount);
							 //b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
							 if(data.getFileOneHour()>0) {
								 b1 = new BigDecimal(data.getFileOneHour());
								 data.setFileOneHour(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
							 }
							 if(data.getFileTowHour()>0) {
								 b1 = new BigDecimal(data.getFileTowHour());
								 data.setFileTowHour(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
							 }
							 if(data.getFileTwoMoreHour()>0) {
								 b1 = new BigDecimal(data.getFileTwoMoreHour());
								 data.setFileTwoMoreHour(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
							 }
							
						}
					}
					if(preData!=null) {
						preData.setUpdateIntegrity(preData.getUpdateIntegrity()*100);
						double preDataMixCount = preData.getFileOneHour()+preData.getFileTowHour()+preData.getFileTwoMoreHour();
						if(preDataMixCount>0) {
							 BigDecimal b1 ;
						     BigDecimal b2 = new BigDecimal(preDataMixCount);
							 //b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
							 if(preData.getFileOneHour()>0) {
								 b1 = new BigDecimal(preData.getFileOneHour());
								 preData.setFileOneHour(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
							 }
							 if(preData.getFileTowHour()>0) {
								 b1 = new BigDecimal(preData.getFileTowHour());
								 preData.setFileTowHour(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
							 }
							 if(preData.getFileTwoMoreHour()>0) {
								 b1 = new BigDecimal(preData.getFileTwoMoreHour());
								 preData.setFileTwoMoreHour(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
							 }
							
						}
					}
					//preData.set
					//data.setFileDefectCount(fileMixCount*data.getUpdateIntegrity());
				}
				provinceJb.put("data", validData);
				if(validData.size()>0) {
					provinceArray.add(provinceJb);
				}
				//Map<String,ReportResponse> periodMap = entry1.getValue();
				
			}
			indicationJb.put("data", provinceArray);
			indicationArray.add(indicationJb);
		}
		return JSONArray.fromObject(indicationArray).toString();
	}
	
	boolean checkTime(String uploadDate,String date,String cur,String config){
		int curNum = Integer.parseInt(cur.split("_")[0].split(":")[0]);
		int configNum = Integer.parseInt(config.split("_")[0].split(":")[0]);
		//if(uploadDate.equals(date) && curNum >= configNum)
		return (uploadDate.equals(date) && curNum < configNum);
	}
	
	
	String getTime(Date curDate, int hour){
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.add(Calendar.HOUR, - hour);
        String start = DateFormatUtils.format(calendar.getTime(), TimeUtil.DATE_FORMAT_YYYY_MM_DD);
        return start;
	}
	
	@Override
	public String getPeriodDict(String type) {
		return  JSONArray.fromObject(mirrorDictService.getMirrorDictByDictName(type)).toString();
	}
	
	
	@Override
	public void checkOtherFile() {
		iFileIndicationPeriodStateService.checkOtherFile();
	}
	
	
	@Override
	public void check() {
		// todo 需要处理
//		iFileIndicationPeriodStateService.check();
	}
	
	@Override
	public void checkAndEmail(String catalog) {
		Calendar calendar = Calendar.getInstance();
		String checkDate = DateFormatUtils.format(calendar.getTime(), TimeUtil.DATE_FORMAT_YYYYMMDD);
		iFileIndicationPeriodStateService.checkFullEmail(catalog, checkDate, false);
	}
	
	@Override
	public String getOtherFileReport(String catolog, String provinceCode, String period, String uploadDate, String fileIndication) {
		Map<String,Map<String, FileStateOtherReportEntity>> indicationMap=  iFileIndicationPeriodStateService.getOtherFileReport(catolog, provinceCode, period, uploadDate, fileIndication);
		JSONArray indicationArray = new JSONArray();
		for(Map.Entry<String,Map<String, FileStateOtherReportEntity>> entry : indicationMap.entrySet()) {
			String key = entry.getKey();
			Map<String, FileStateOtherReportEntity> provinceMap = entry.getValue();
			JSONObject indicationJb = new JSONObject();
			indicationJb.put("fileIndicationName", key);
			JSONArray provinceArray = new JSONArray();
			for(Map.Entry<String, FileStateOtherReportEntity> entry1 : provinceMap.entrySet()) {
				JSONObject provinceJb = new JSONObject();
				provinceJb.put("provinceName", entry1.getValue().getProvinceName());
				provinceJb.put("uploadDate",uploadDate);
			     //BigDecimal result = a1.multiply(b1);
			     Double preCount = entry1.getValue().getPreOtherDayCount();
			     Double todayCount = entry1.getValue().getTodayDayCount();
			     if(null!= preCount) {
			    	 BigDecimal bg = new BigDecimal(preCount*100);  
			    	 provinceJb.put("preCount",bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			     }else {
			    	 provinceJb.put("preCount", preCount);
			     }
			     if(null!= todayCount) {
			    	 BigDecimal bg = new BigDecimal(todayCount*100);  
			    	 provinceJb.put("todayCount",bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			     }else {
			    	 provinceJb.put("todayCount", todayCount);
			     }
				provinceArray.add(provinceJb);
				//Map<String,ReportResponse> periodMap = entry1.getValue();
				
			}
			indicationJb.put("data", provinceArray);
			indicationArray.add(indicationJb);
		}
		return JSONArray.fromObject(indicationArray).toString();
	}

	@Override
	public void setFileCount(String startDate,String endDate) {
		iFileIndicationPeriodStateService.setFileCount(startDate, endDate);
	}

	@Override
	public JSONArray getFileIndicationByCatalog(@RequestParam("catalog") String catalog) {
		List<FileIndicationEntity> entityList = fileIndicationService.getFileIndication(catalog);
		JSONArray jsonArray = new JSONArray();
		if (entityList != null && entityList.size() > 0) {
			for (FileIndicationEntity entity : entityList) {
				jsonArray.add(entity);
			}
		}
		return jsonArray;
	}
}
