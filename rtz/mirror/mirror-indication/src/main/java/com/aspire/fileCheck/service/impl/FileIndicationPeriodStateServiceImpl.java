package com.aspire.fileCheck.service.impl;

import com.aspire.common.EnvConfigProperties;
import com.aspire.fileCheck.dao.IFileIndicationDAO;
import com.aspire.fileCheck.dao.IFileIndicationPeriodConfigDAO;
import com.aspire.fileCheck.dao.IFileIndicationPeriodStateDAO;
import com.aspire.fileCheck.dao.IFileIndicationPeriodStateOtherDAO;
import com.aspire.fileCheck.entity.*;
import com.aspire.fileCheck.service.IFileIndicationPeriodConfigService;
import com.aspire.fileCheck.service.IFileIndicationPeriodStateService;
import com.aspire.fileCheck.service.IFileMissingCheckService;
import com.aspire.fileCheck.util.FileConst;
import com.aspire.fileCheck.util.SSHUtil;
import com.aspire.fileCheck.util.TimeUtil;
import com.aspire.mirror.entity.IndicationProvinceEntity;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import com.cmcc.family.alertagent.AlertAgent;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: longfeng
 * Date: 2018/11/25
 * 软探针异常指标监控系统
 * com.aspire.mirror.service.impl.IndicationDataServiceImpl
 */
@Service
@Slf4j
public class FileIndicationPeriodStateServiceImpl implements IFileIndicationPeriodStateService {
	@Autowired
	private IFileMissingCheckService fileMissingCheckService;
	@Autowired
	private IFileIndicationPeriodStateOtherDAO iFileIndicationPeriodStateOtherDAO;

	private int scale = 4;
	private int scaleTwo = 2;
	private DecimalFormat df = new DecimalFormat("######0"); //四色五入转换成整数
	
	@Autowired
	private IFileIndicationPeriodConfigService iFileIndicationPeriodConfigService;

    @Autowired
    private IFileIndicationPeriodStateDAO fileIndicationPeriodStateDAO;
    
    @Autowired
    private IFileIndicationPeriodConfigDAO fileIndicationPeriodConfigDAO;
    
    
    @Autowired
    private IFileIndicationPeriodStateOtherDAO fileIndicationPeriodStateOtherFileDAO;
    
    @Autowired
    private MirrorDictServiceImpl mirrorDictServiceImpl;
    
    @Autowired
    private EnvConfigProperties envConfigProperties;
    
    @Autowired
    IFileIndicationDAO eFileIndicationDAO;
    
	@Override
	public List<FileIndicationPeriodStateEntity> getDataByDate(String uploadDate) {
		return fileIndicationPeriodStateDAO.getDataByDate(uploadDate);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public int processDate(List<FileIndicationPeriodStateEntity> newItem, List<FileIndicationPeriodStateEntity> updateItem) {
		int i = 0;
		if(newItem.size()>0) {
			i = fileIndicationPeriodStateDAO.batchInsertStates(newItem);
		}
		if(updateItem.size()>0) {
			i +=fileIndicationPeriodStateDAO.batchUpdateStates(updateItem);
		}
		return i;
	}

	@Override
	public void check(Calendar calendar) {
		//获取时间
		int minute = calendar.get(Calendar.MINUTE);
		String curDateStr = getTime(calendar.getTime(), FileConst.PERIOD_LAZY_HOUR + FileConst.CALC_LAZY_HOUR);//获取检测时间段时间
		String curPeriod = TimeUtil.getCalcPeriod(calendar.getTime());
		Integer curPeriodInt = Integer.parseInt(curPeriod.replaceAll(":","").replaceAll("-","").replaceAll(" ",""));
		String hour = curPeriod.substring(0, 2);
		String curMinutePeriod = TimeUtil.getMinutePeriod(hour, minute);
		//查询要统计的指标
		List<FileIndicationEntity> FileIndicationList = iFileIndicationPeriodConfigService.getFileIndication(null);
		try {
			//循环指标
			for(FileIndicationEntity fi : FileIndicationList) {
				Map<String, LinkedHashMap<String, LinkedHashMap<String, Object>>> indicationDataMap = new LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>>();
				List<FileIndicationPeriodConfigEntity> configs = iFileIndicationPeriodConfigService.getConfigsByIndicationId(fi.getFileIndicationId());
				for (FileIndicationPeriodConfigEntity configEntity : configs) {
					// 按照省份归类
					if (!indicationDataMap.containsKey(configEntity.getProvinceCode())) {
						indicationDataMap.put(configEntity.getProvinceCode(), new LinkedHashMap<String, LinkedHashMap<String, Object>>());
					}
					// 判断时区是否在检测范围
					String periodValue = configEntity.getMirrorDict().getDictValue();
					Integer periodInt = Integer.parseInt(periodValue.replaceAll(":", "").replaceAll("-", "").replaceAll(" ", ""));
					if (periodInt > curPeriodInt) {
						continue;
					}
					// 将小时时段 按15分钟分段存放
					String[] hourPeriods = TimeUtil.getHourPeriod(periodValue.substring(0, 2));
					Map<String, LinkedHashMap<String, Object>> periodMap = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
					for (String hourPeriod : hourPeriods) {
						Integer currMinuteInt = Integer.parseInt(curMinutePeriod.replaceAll(":", "")
								.replaceAll("-", "").replaceAll(" ", ""));
						Integer hourPeriodInt = Integer.parseInt(hourPeriod.replaceAll(":", "")
								.replaceAll("-", "").replaceAll(" ", ""));
						// 如果是当前时区的之前时区, 应该4个时间段都有
						LinkedHashMap<String, Object> tempMap = new LinkedHashMap<String, Object>();
						tempMap.put("fileCount", 0);
						tempMap.put("fileSize", 0.0);
						tempMap.put("configEntity", configEntity);
						if (periodInt < curPeriodInt) {
							String key = periodValue + "_" + hourPeriod;
							periodMap.put(key, tempMap);
						}
						// 如果等于当前时区, 那么应该是分钟归属之内的时间段
						if (periodInt.equals(curPeriodInt)) {
							if (hourPeriodInt > currMinuteInt) {
								continue;
							}
							String key = periodValue + "_" + hourPeriod;
							periodMap.put(key, tempMap);
						}
					}
					indicationDataMap.get(configEntity.getProvinceCode()).putAll(periodMap);
				}
				// 循环处理每个省份需要处理的数据
				for (String provinceCode : indicationDataMap.keySet()) {
					if (provinceCode.equals("010") && fi.getFileIndicationCode().equals("STBINFO")) {
						log.info("111");
					}
					List<RemoteFile> files = SSHUtil.getRemoteFiles(provinceCode, "/" + curDateStr + fi.getFileIndicationDir() + "/");
					for (RemoteFile file : files) {
						String fileName = file.getFileName();
						try {
							String begin = fileName.split("_")[1];
							Date beginDate;
							beginDate = DateUtils.parseDate(begin, new String[]{TimeUtil.DATE_FORMAT_YYYYMMDDHHMMSS});
							// 文件日期
							String fileDateStr = DateFormatUtils.format(beginDate, TimeUtil.DATE_FORMAT_YYYYMMDD);
							if (!fileDateStr.equals(curDateStr)) {
								continue;
							}
							// 文件归属时间段
							String fileDatePeriod = TimeUtil.formatDateToPeriod(beginDate);
							String fileHour = fileDatePeriod.substring(0, 2);
							String fileMinutePeriod = TimeUtil.getMinutePeriod(fileHour, beginDate.getMinutes());
							String key = fileDatePeriod + "_" + fileMinutePeriod;
							LinkedHashMap<String, LinkedHashMap<String, Object>> periodMap = indicationDataMap.get(provinceCode);
							if (periodMap.containsKey(key)) {
								LinkedHashMap periodData = periodMap.get(key);
								int fileCount = (Integer) periodData.get("fileCount");
								double fileSize = (Double) periodData.get("fileSize");
								periodData.put("fileCount", fileCount + 1);
								periodData.put("fileSize", fileSize + file.getFileSize());
							}
						} catch (ParseException e) {
							log.error("文件名错误" + fileName, e);
						}
					}
				}
				// 将数据转化为entity对象
				List<FileIndicationPeriodStateEntity> stateEntityList = new ArrayList<FileIndicationPeriodStateEntity>();
				for (String provinceCode : indicationDataMap.keySet()) {
					LinkedHashMap<String, LinkedHashMap<String, Object>> periodMap = indicationDataMap.get(provinceCode);
					for (String period : periodMap.keySet()) {
						String minutePeriod = period.split("_")[1];
						LinkedHashMap<String, Object> minuteMap = periodMap.get(period);
						int fileCount = (Integer) minuteMap.get("fileCount");
						double fileSize = (Double) minuteMap.get("fileSize");
						FileIndicationPeriodConfigEntity configEntity = (FileIndicationPeriodConfigEntity) minuteMap.get("configEntity");
						FileIndicationPeriodStateEntity entity = new FileIndicationPeriodStateEntity();
						entity.setPeriodConfigId(configEntity.getConfigId());
						if (fileCount > 0) {
							entity.setUploadStatus(32);
							BigDecimal b1 = new BigDecimal(fileCount);
							BigDecimal b2 = new BigDecimal(configEntity.getFixFileCount());
							double integrity = b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
							entity.setUpdateIntegrity(integrity);
						} else {
							entity.setUploadStatus(30);
							entity.setUpdateIntegrity(0);
						}
						// 设置文件大小
						if (configEntity.getFixFileSize() == null) {
							entity.setFileSizeStatus(32);
						} else {
							if (fileSize > configEntity.getFixFileSize()) {
								entity.setFileSizeStatus(32);
							} else {
								entity.setFileSizeStatus(30);
							}
						}
						entity.setUploadDate(curDateStr);
						entity.setMinutePeriod(minutePeriod);
						entity.setFileIndicationId(configEntity.getIndicationId());
						stateEntityList.add(entity);
					}
				}
				if (stateEntityList.size() > 0) {
					fileIndicationPeriodStateDAO.batchInsertStates(stateEntityList);
				}
			}
		}catch(Exception e){
			log.info("检测文件缺失和完整性失败", e);
		}
	}
	
	@Override
	public void checkOtherFile() {
		//获取时间
		Date curDate = new Date();
		String curDateStr = getTime(curDate,1);//获取检测时间段时间
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.add(Calendar.HOUR, - FileConst.CALC_LAZY_HOUR);
		String curPeriod = TimeUtil.formatDateToPeriod(calendar.getTime());
		//查询要统计的指标
		List<FileIndicationEntity> FileIndicationList = iFileIndicationPeriodConfigService.getFileIndication(null);
		//当前的时区数据
		List<FileIndicationPeriodStateOtherFileEntity> newItem = new ArrayList<FileIndicationPeriodStateOtherFileEntity>();
		try {
			Map<String,String> provincePathMap = new HashMap<String,String>();
			//循环指标
			for(FileIndicationEntity fi:FileIndicationList) {
				List<FileIndicationPeriodConfigEntity> configs = iFileIndicationPeriodConfigService.getConfigsByIndicationId(fi.getFileIndicationId());
				Map<String,Map<String, FileIndicationPeriodConfigEntity>> provinceMap = new HashMap<String,Map<String, FileIndicationPeriodConfigEntity>>();
				for(FileIndicationPeriodConfigEntity c:configs) {
					if(provinceMap.containsKey(c.getProvinceCode())) {
						Map<String, FileIndicationPeriodConfigEntity> periodMap  = provinceMap.get(c.getProvinceCode());
						periodMap.put(c.getMirrorDict().getDictValue(), c);
					}else {
						Map<String, FileIndicationPeriodConfigEntity> periodMap = new HashMap<String, FileIndicationPeriodConfigEntity>();
						periodMap.put(c.getMirrorDict().getDictValue(), c);
						provinceMap.put(c.getProvinceCode(), periodMap);
						provincePathMap.put(c.getProvinceCode(), c.getProvinceEntity().getProvinceFileDir());
					}
				}
				String path = "/"+ curDateStr+ fi.getFileIndicationDir() +"/";
				//循环省份
				for (Map.Entry<String,Map<String, FileIndicationPeriodConfigEntity>> entry : provinceMap.entrySet()) {
//					String ftpPath = provincePathMap.get(entry.getKey()) + path;
//					log.info("************************"+ ftpPath+"************************");
		            int count = 0;
		            //更换目录到当前目录  
					 List<RemoteFile> files =SSHUtil.getRemoteFiles(entry.getKey(), path);
					 //fileMap.put(date, 0);
					 //循环文件
					 int fileCount = 0;
					 for(RemoteFile file:files){
			         	String fileName = file.getFileName();
			            String begin = fileName;
			            try {
			            	begin = fileName.split("_")[1];
			            	fileCount++;
				            Date beginDate = DateUtils.parseDate(begin, new String[]{TimeUtil.DATE_FORMAT_YYYYMMDDHHMMSS});
				            String fileDateStr = DateFormatUtils.format(beginDate, TimeUtil.DATE_FORMAT_YYYYMMDD);//getTime(beginDate,0);//获取检测时间
				            if(!fileDateStr.equals(curDateStr)) {
				            	count ++;
				            }
			            }catch(Exception e) {
			            	log.info("文件名错误"+fileName,e);
			            }
			         }  
					 FileIndicationPeriodStateOtherFileEntity of = new FileIndicationPeriodStateOtherFileEntity();
				     if(count ==0  ) {
				    	 of.setOtherDayCount(0.0) ;
				     }else {
				    	 if(fileCount == 0) {
				    		 of.setOtherDayCount(0.0) ;
				    	 }else {
				    		 BigDecimal b1 = new BigDecimal(count);
						     BigDecimal b2 = new BigDecimal(fileCount);
					    	 of.setOtherDayCount(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue()) ;
				    	 }
				     }
					 of.setPeriodConfigId(entry.getValue().get(curPeriod).getConfigId());
					 of.setCreateTime(new Date());
					 of.setUploadDate(curDateStr);
					 of.setCount(count);
					 newItem.add(of);
				}
			}
			if(newItem.size()>0) {
				fileIndicationPeriodStateOtherFileDAO.batchInsertOtherFile(newItem);
			}
		}catch(Exception e){
			log.error("检测非当天文件失败失败", e);
		}
	}

	/**
	 * 判断是否需要统计的时区
	 * @param cur
	 * @param config
	 * @return
	 */
	boolean checkPeriod(String cur,String config){
		int curNum = Integer.parseInt(cur.split("_")[0].split(":")[0]);
		int configNum = Integer.parseInt(config.split("_")[0].split(":")[0]);
		return curNum >= configNum;
	}
	/**
	 * 获取时区时间 ：YYYYMMDD
	 * @param curDate
	 * @return
	 */
	String getTime(Date curDate, int hour){
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.add(Calendar.HOUR, - hour);
        String start = DateFormatUtils.format(calendar.getTime(), TimeUtil.DATE_FORMAT_YYYYMMDD);
        return start;
	}
	
	
	void calData(FileStateReportItem item,FileStateReport r){
		// luowenbo 2020-07-23 代码审计——日期格式缺陷，当多线程时，容出现幻读成员变量的现象
		// 创建局部变量，防止共享
		DecimalFormat crtDf = new DecimalFormat("######0"); //四色五入转换成整数
		if(null!=r.getDictLazyStatus()) {
			if(r.getDictLazyStatus()==1) {
				item.setFileOneHour(item.getFileOneHour()+1);
			}else if(r.getDictLazyStatus()==2) {
				item.setFileTowHour(item.getFileTowHour()+1);
			}else {
				item.setFileTwoMoreHour(item.getFileTwoMoreHour()+1);
			}
		}
		item.setFileDefectCount(r.getFixFileCount() - Integer.parseInt(crtDf.format(r.getFixFileCount() * r.getUpdateIntegrity())));
	}

	@Override
	public Map<String,Map<String,Map<String,ReportResponse>>> getReport(String catolog, String provinceCode, String period,
			String thisDate, String fileIndicationId) {
		String lastDate = thisDate;
		try {
			Date time = DateUtils.parseDate(thisDate, new String[] {TimeUtil.DATE_FORMAT_YYYYMMDD,TimeUtil.DATE_FORMAT_YYYY_MM_DD});
			 Calendar calendar = Calendar.getInstance();
	        calendar.setTime(time);
	        thisDate = DateFormatUtils.format(calendar.getTime(), TimeUtil.DATE_FORMAT_YYYYMMDD);
	        calendar.add(Calendar.DATE, -1);
	        lastDate = DateFormatUtils.format(calendar.getTime(), TimeUtil.DATE_FORMAT_YYYYMMDD);
		} catch (ParseException e) {
			log.error("获取文件文件完整度和缺失报表失败",e);
		}
		List<FileStateReport> dataList = fileIndicationPeriodStateDAO.getReport(catolog, provinceCode, period, thisDate, lastDate, fileIndicationId);
		Map<String,Map<String,Map<String,ReportResponse>>> indicationMap = new LinkedHashMap<String,Map<String,Map<String,ReportResponse>>>();
		//根据指标处理数据
		for(FileStateReport r:dataList) {
			String indicationName = r.getFileIndicationName();
			String provinceName = r.getProvinceName();
			if(indicationMap.containsKey(indicationName)) {
				Map<String,Map<String,ReportResponse>> provineDateMap = indicationMap.get(indicationName);
				if(null == r.getPeriodConfigId()) {
					continue;
				}
				//根据日期和省份处理数据
				if(provineDateMap.containsKey(provinceName)) {
					Map<String,ReportResponse> periodMap = provineDateMap.get(provinceName);
					//根据时区处理数据
					if(periodMap.containsKey(r.getDictPeriod())) {
						ReportResponse re = periodMap.get(r.getDictPeriod());
						if(r.getUploadDate().equals(lastDate)) {
							if(null!=re.getPreData()) {
								FileStateReportItem item = re.getPreData();
								calData(item,r);
							}else {
								FileStateReportItem item = new FileStateReportItem();
								BeanUtils.copyProperties(r, item);
								calData(item,r);
								re.setPreData(item);
							}
						}else {
							if(null!=re.getData()) {
								FileStateReportItem item = re.getData();
								calData(item,r);
							}else {
								FileStateReportItem item = new FileStateReportItem();
								BeanUtils.copyProperties(r, item);
								calData(item,r);
								re.setData(item);
							}
						}
					}else {
						ReportResponse re = formReportData( lastDate, r);
						periodMap.put(r.getDictPeriod(), re);
					}
					//处理时区结束
				}else {
					Map<String,ReportResponse> periodMap = new LinkedHashMap<String,ReportResponse>();
					ReportResponse re = formReportData( lastDate, r);
					periodMap.put(r.getDictPeriod(), re);
					provineDateMap.put(provinceName, periodMap);
				}
			}else {
				Map<String,Map<String,ReportResponse>> provineDateMap = new LinkedHashMap<String,Map<String,ReportResponse>>();
				if(null!=r.getPeriodConfigId()) {
					Map<String,ReportResponse> periodMap = new LinkedHashMap<String,ReportResponse>();
					ReportResponse re = formReportData( lastDate, r);
					periodMap.put(r.getDictPeriod(), re);
					provineDateMap.put(provinceName, periodMap);
				}
				indicationMap.put(r.getFileIndicationName(), provineDateMap);
			}
		}
		return indicationMap;
	}
	
	private ReportResponse formReportData(String lastDate,FileStateReport r){
		ReportResponse re = new ReportResponse();
		BeanUtils.copyProperties(r, re);
		FileStateReportItem item = new FileStateReportItem();
		BeanUtils.copyProperties(r, item);
		if(r.getUploadDate().equals(lastDate)) {
			calData(item,r);
			re.setPreData(item);
		}else {
			calData(item,r);
			re.setData(item);
		}
		return re;
	}
	
	@Override
	public Map<String,Map<String, FileStateOtherReportEntity>> getOtherFileReport(String catolog, String provinceCode, String period,
																				  String thisDate, String fileIndication) {
		String lastDate = thisDate;
		try {
			Date time = DateUtils.parseDate(thisDate, new String[] {TimeUtil.DATE_FORMAT_YYYYMMDD,TimeUtil.DATE_FORMAT_YYYY_MM_DD});
			 Calendar calendar = Calendar.getInstance();
	        calendar.setTime(time);
	        thisDate = DateFormatUtils.format(calendar.getTime(), TimeUtil.DATE_FORMAT_YYYYMMDD);
	        calendar.add(Calendar.DATE, -1);
		} catch (ParseException e) {
			log.error("获取非当天文件报表失败",e);
		}
		List<FileStateOtherReportEntity> dataList = fileIndicationPeriodStateOtherFileDAO.getOtherFileReport(catolog, provinceCode, period, thisDate, lastDate, fileIndication);
		List<FileStateOtherReportEntity> preDataList = fileIndicationPeriodStateOtherFileDAO.getOtherFileReport(catolog, provinceCode, "23:00-00:00", lastDate, lastDate, fileIndication);
		Map<String,Map<String, FileStateOtherReportEntity>> indicationMap = new LinkedHashMap<String,Map<String, FileStateOtherReportEntity>>();
		//根据指标处理数据
		for(FileStateOtherReportEntity r:dataList) {
			String indicationName = r.getFileIndicationName();
			String provinceName = r.getProvinceName();
			if(indicationMap.containsKey(indicationName)) {
				Map<String, FileStateOtherReportEntity> provineDateMap = indicationMap.get(indicationName);
				if(null == r.getPeriodConfigId()) {
					continue;
				}
				//根据日期和省份处理数据
				if(provineDateMap.containsKey(provinceName)) {
					continue;
				}else {
					FileStateOtherReportEntity bean = new FileStateOtherReportEntity();
					BeanUtils.copyProperties(r, bean);
					bean.setTodayDayCount(r.getOtherDayCount());
					bean.setUploadDate(thisDate);
					provineDateMap.put(r.getProvinceCode(), bean);
				}
				//处理日期和省份处理数据
			}else {
				Map<String, FileStateOtherReportEntity> provineDateMap = new LinkedHashMap<String, FileStateOtherReportEntity>();
				if(null!=r.getPeriodConfigId()) {
					FileStateOtherReportEntity bean = new FileStateOtherReportEntity();
					BeanUtils.copyProperties(r, bean);
					bean.setTodayDayCount(r.getOtherDayCount());
					bean.setUploadDate(thisDate);
					provineDateMap.put(r.getProvinceCode(), bean);
				}
				indicationMap.put(r.getFileIndicationName(), provineDateMap);
			}
		}
		for(FileStateOtherReportEntity r:preDataList) {
			String indicationName = r.getFileIndicationName();
			String provinceName = r.getProvinceName();
			if(indicationMap.containsKey(indicationName)) {
				Map<String, FileStateOtherReportEntity> provineDateMap = indicationMap.get(indicationName);
				if(null == r.getPeriodConfigId()) {
					continue;
				}
				//根据日期和省份处理数据
				if(provineDateMap.containsKey(provinceName)) {
					FileStateOtherReportEntity data = provineDateMap.get(provinceName);
					data.setPreOtherDayCount(r.getOtherDayCount());
				}else {
					FileStateOtherReportEntity bean = new FileStateOtherReportEntity();
					BeanUtils.copyProperties(r, bean);
					bean.setPreOtherDayCount(r.getOtherDayCount());
					bean.setUploadDate(thisDate);
					provineDateMap.put(r.getProvinceCode(), bean);
				}
			}else {
				Map<String, FileStateOtherReportEntity> provineDateMap = new LinkedHashMap<String, FileStateOtherReportEntity>();
				if(null!=r.getPeriodConfigId()) {
					FileStateOtherReportEntity bean = new FileStateOtherReportEntity();
					BeanUtils.copyProperties(r, bean);
					bean.setPreOtherDayCount(r.getOtherDayCount());
					bean.setUploadDate(thisDate);
					provineDateMap.put(r.getProvinceCode(), bean);
				}
				indicationMap.put(r.getFileIndicationName(), provineDateMap);
			}
		}
		return indicationMap;
	}

//	@Override
//	public void checkAndEmail(String catalog) {
//		//获取时间
//		Date curDate = new Date();
//		String curDateStr = getTime(curDate,FileConst.PERIOD_LAZY_HOUR + FileConst.CALC_LAZY_HOUR);//获取检测时间段时间
//		String curPeriod = TimeUtil.getCalcPeriod(curDate);
//
//
//
//
//
//
//
//
//
//
//
//
//		List<MirrorDictEntity> periodList = mirrorDictServiceImpl.getMirrorDictByDictName("PERIOD");
//		String thTemplate = "<th style='border: 1px solid #000; width:100px; text-align:center; font-size:13px'>%s</th>";
//		String tdTemplate = "<td style='border: 1px solid #000; width:100px; text-align:center; font-size:13px;'>%s</td>";
//		Map<String,String> provincePathMap = new LinkedHashMap<String,String>();
//		Map<String,Map<String,FileIndicationEntity>> provinceIndicationMap = new LinkedHashMap<String,Map<String,FileIndicationEntity>>();
//		try {
//				List<FileIndicationPeriodConfigEntity> configs = fileIndicationPeriodConfigDAO.getConfigsByIndicationCatalog(catalog);
//				Map<String,Map<String, FileIndicationPeriodConfigEntity>> provinceMap = new LinkedHashMap<String,Map<String, FileIndicationPeriodConfigEntity>>();
//				for(FileIndicationPeriodConfigEntity c:configs) {
//					if(provinceIndicationMap.containsKey(c.getProvinceCode())) {
//						Map<String,FileIndicationEntity> indicationMap = provinceIndicationMap.get(c.getProvinceCode());
//						if(!indicationMap.containsKey(c.getFileIndication().getFileIndicationId()+"")) {
//							indicationMap.put(c.getFileIndication().getFileIndicationId()+"", c.getFileIndication());
//						}
//					}else {
//						Map<String,FileIndicationEntity> indicationMap = new LinkedHashMap<String,FileIndicationEntity>();
//						indicationMap.put(c.getFileIndication().getFileIndicationId()+"",c.getFileIndication());
//						provinceIndicationMap.put(c.getProvinceCode(), indicationMap);
//					}
//					if(provinceMap.containsKey(c.getProvinceCode())) {
//						Map<String, FileIndicationPeriodConfigEntity> periodMap  = provinceMap.get(c.getProvinceCode());
//						periodMap.put(c.getMirrorDict().getDictValue(), c);
//					}else {
//						Map<String, FileIndicationPeriodConfigEntity> periodMap = new HashMap<String, FileIndicationPeriodConfigEntity>();
//						periodMap.put(c.getMirrorDict().getDictValue(), c);
//						provinceMap.put(c.getProvinceCode(), periodMap);
//						provincePathMap.put(c.getProvinceCode(), c.getProvinceEntity().getProvinceFileDir());
//					}
//				}
//
//				//循环省份
//				for (Map.Entry<String,Map<String, FileIndicationPeriodConfigEntity>> entry : provinceMap.entrySet()) {
//					StringBuilder indicationBuffer = new StringBuilder();
//					StringBuilder indicationOtherBuffer = new StringBuilder();
//					List<String> exceptionIndication = new ArrayList<String>();
//					String provincName = entry.getValue().values().iterator().next().getProvinceEntity().getProvinceName();
//					Map<String,FileIndicationEntity> indicationMap = provinceIndicationMap.get(entry.getKey());
//					if(null==indicationMap || indicationMap.isEmpty()) {
//						continue;
//					}
//					int k=0;
//					for (Map.Entry<String,FileIndicationEntity> entry1 : indicationMap.entrySet()) {
//						k++;
//						FileIndicationEntity fi = entry1.getValue();
//						String path = "/"+ curDateStr+ fi.getFileIndicationDir() +"/";
////						String ftpPath = provincePathMap.get(entry.getKey()) + path;
////						log.info("************************"+ftpPath+"************************");
//						Map<String,Integer> fileMap = new HashMap<String,Integer>();
//						List<RemoteFile> files = new ArrayList<RemoteFile>();
//						files = SSHUtil.getRemoteFiles(entry.getKey(), path);//ftpUtil.getFTPClient().listFiles();
//						for(RemoteFile file:files){
//				           	String fileName = file.getFileName();
//				           	String begin = fileName;
//				           	try {
//				           		begin = fileName.split("_")[1];
//				            	Date beginDate;
//								beginDate = DateUtils.parseDate(begin, new String[]{TimeUtil.DATE_FORMAT_YYYYMMDDHHMMSS});
//								String fileDateStr = DateFormatUtils.format(beginDate, TimeUtil.DATE_FORMAT_YYYYMMDD);
//								//非当前文件
//								if(!fileDateStr.equals(curDateStr)) {
//									if(fileMap.containsKey(curDateStr)) {
//										fileMap.put(curDateStr, fileMap.get(curDateStr)+1);
//									}else {
//										fileMap.put(curDateStr, 1);
//									}
//									continue;
//								}
//					           	String fileDatePeriod = TimeUtil.formatDateToPeriod(beginDate);
//					            	if(checkPeriod(curPeriod,fileDatePeriod)) {
//					            		if(fileMap.containsKey(fileDatePeriod)) {
//						            		fileMap.put(fileDatePeriod, fileMap.get(fileDatePeriod)+1);
//						            	}else {
//						            		fileMap.put(fileDatePeriod, 1);
//						            	}
//					            	}
//				            }catch(Exception e) {
//				           		log.error("文件名错误"+fileName,e);
//				            }
//				        }
//						StringBuilder trBuffer = new StringBuilder();
//						int fileCount = entry.getValue().get(curPeriod).getFixFileCount();
//						for(MirrorDictEntity dict:periodList) {
//							String dictPeriod = dict.getDictValue();
//							String text = "";
//							if(!checkPeriod(curPeriod, dictPeriod)) {
//								break;
//							}else {
//								if(fileMap.containsKey(dict.getDictValue())) {
//									int fileCountTemp = fileMap.get(dictPeriod);
//									BigDecimal b1 = new BigDecimal(fileCountTemp);
//									BigDecimal b2 = new BigDecimal(fileCount);
//									double rate = b1
//											.divide(b2, scaleTwo,
//													BigDecimal.ROUND_HALF_UP)
//											.doubleValue();
//									int val = new BigDecimal(rate)
//											.multiply(new BigDecimal(100))
//											.intValue();
//									if (val <= entry.getValue().get(dictPeriod)
//											.getMinAlarmLimit()) {
//
//										text = val + "%";
//									}
//								}else {
//									text = "缺失";
//								}
//							}
//							if(text!="") {
//								trBuffer.append("<tr>");
//								trBuffer.append(String.format(tdTemplate, provincName));
//								trBuffer.append(String.format(tdTemplate, curDateStr));
//								trBuffer.append(String.format(tdTemplate, dictPeriod));
//								trBuffer.append(String.format(tdTemplate, text));
//								trBuffer.append("</tr>");
//							}
//						}
//
//
//
//
//
//
//
//
//
//
//
//						if(trBuffer.length()>0) {
//							exceptionIndication.add(fi.getFileIndicationCode()+"-"+fi.getFileIndicationName());
//							if(indicationBuffer.length()==0) {
//								indicationBuffer.append("<b>一、文件上传完整性情况</b>").append("<br/><br/>");
//							}
//							indicationBuffer.append(k+".").append(fi.getFileIndicationCode()).append("-").append(fi.getFileIndicationName()).append(":").append("<br/>");
//							indicationBuffer.append("<table style=\"border-collapse:collapse; \">");
//							indicationBuffer.append("<tr>");
//							indicationBuffer.append(String.format(thTemplate, "省份"));
//							indicationBuffer.append(String.format(thTemplate, "日期"));
//							indicationBuffer.append(String.format(thTemplate, "时间段"));
//							indicationBuffer.append(String.format(thTemplate, "完整性"));
//							indicationBuffer.append("</tr>");
//							indicationBuffer.append(trBuffer);
//							indicationBuffer.append("</table>").append("<br/>");
//						}
//					}
//					if(indicationBuffer.length()>0) {
//						sendEmail(exceptionIndication,provincName, curDateStr, catalog, indicationBuffer);
//						exceptionIndication.clear();
//						//indicationBuffer.
//					}
//				}
//		}catch(Exception e){
//			log.info("检测文件缺失和完整性失败", e);
//		}
//
//	}
	
	private void sendEmail(List<String> exceptionIndication, List<String> exceptionType, String province,String day,String type,StringBuilder indication){
		String titleStr = exceptionIndication.get(0);
		if(exceptionIndication.size()>1) {
			titleStr = titleStr+"等";
		}
		String exceptionTypeString = "";
		if (exceptionType.size() > 0) {
			for (String cType : exceptionType) {
				if (!exceptionTypeString.equals("")) {
					exceptionTypeString += "、";
				}
				exceptionTypeString += cType;
			}
		}
		String title = FileConst.MAIL_FILE_CHECK_PROVINCE_TITLE_TEMPLATE;
        title = String.format(title, day,province,  type,titleStr, exceptionTypeString);
        String   model = FileConst.MAIL_FILE_CHECK_PROVINCE_CONTENT_TEMPLATE;
        StringBuffer indicationStr = new StringBuffer(exceptionIndication.get(0));
        if(exceptionIndication.size()>1) {
        	for(int i=1;i<exceptionIndication.size();i++) {
        		indicationStr.append("、").append(exceptionIndication.get(i));
            }
        }
        model = String.format(model, province,day, type, indicationStr.toString(), exceptionTypeString, indication);
		log.info("文件检查上传情况由kafka将信息通过Email发送  请求 ---> 开始");
		log.info("发送邮件内容:{}", model);
      	final AlertAgent alert = new AlertAgent(envConfigProperties.getAlertAgent().getServer());
      	alert.sendAlert(envConfigProperties.getFileCheck().getAutoSendTopic(), title, model, false);
      	log.info("由kafka将信息通过Email发送  请求 ---> 结束");
	}
	
	
	@Override
	public void setFileCount(String startDate,String endDate) {
		List<FileIndicationPeriodStateEntity> datalist = fileIndicationPeriodStateDAO.getMinUpdateIntegrity(startDate, endDate);
		fileIndicationPeriodConfigDAO.batchUpdateFileCount(datalist);
	}

	@Override
	public void checkFullEmail(String catalog, String checkDate, boolean showOtherFileStats) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", catalog);
		params.put("date", checkDate);
		List<Map<String, String>> missingList = fileMissingCheckService.getFileCheckResultForMail(params);
		//重新装载一下数据 第一层 省份 第二层 指标  第三层 每个时间段的数据
		Map<String, Map<String, List<Map<String, String>>>> missMap = this.formatData(missingList);
		List<Map<String,String>> otherList = iFileIndicationPeriodStateOtherDAO.getOtherFileData(checkDate, catalog);
		//重新装载一下数据 第一层 省份 第二层 指标  第三层 非当日文件数据
		Map<String, Map<String, List<Map<String, String>>>> otherFileMap = this.formatData(otherList);

		String thTemplate = "<th style='border: 1px solid #000; width:100px; text-align:center; font-size:13px'>%s</th>";
		String tdTemplate = "<td style='border: 1px solid #000; width:100px; text-align:center; font-size:13px;'>%s</td>";

		List<IndicationProvinceEntity> provinceList = IndicationUtils.getProvinceList();
		for(IndicationProvinceEntity provinceEntity : provinceList) {
			StringBuilder emailBuilder = new StringBuilder();
			//开始组装数据
			if (provinceEntity.getProvinceCode().equals(IndicationConst.COUNTRY_PROVINCE_CODE)) {
				continue;
			}
			List<String> exceptionIndications = new LinkedList<String>();
			//组装缺失文件内容
			Map<String, List<Map<String, String>>> missFileMap = missMap.get(provinceEntity.getProvinceCode());
			StringBuilder missBuilder = new StringBuilder();
			List<String> exceptionType = new ArrayList<String>();
			if (missFileMap != null && missFileMap.size() > 0) {
				missBuilder.append("<b>一、文件上传完整性情况</b>").append("<br/><br/>");
				int prefix = 1;
				for (String indicationKey : missFileMap.keySet()) {
					if (!exceptionIndications.contains(indicationKey)) {
						exceptionIndications.add(indicationKey);
					}
					missBuilder.append(prefix + ".").append(indicationKey).append("<br/>");
					missBuilder.append("<table style=\"border-collapse:collapse; \">");
					missBuilder.append("<tr>");
					missBuilder.append(String.format(thTemplate, "省份"));
					missBuilder.append(String.format(thTemplate, "日期"));
					missBuilder.append(String.format(thTemplate, "时间段"));
					missBuilder.append(String.format(thTemplate, "完整性"));
					missBuilder.append("</tr>");
					List<Map<String, String>> dataList = missFileMap.get(indicationKey);
					StringBuilder trBuilder = new StringBuilder();
					if (dataList != null && dataList.size() > 0) {
						for (Map<String, String> dataMap : dataList) {
							trBuilder.append("<tr>");
							trBuilder.append(String.format(tdTemplate, provinceEntity.getProvinceName()));
							trBuilder.append(String.format(tdTemplate, dataMap.get("upload_date")));
							trBuilder.append(String.format(tdTemplate, dataMap.get("period_name")));
							String uploadStatus = String.valueOf(dataMap.get("upload_status"));
							String sizeStatus = String.valueOf(dataMap.get("file_size_status"));
							// 缺失
							if (("30").equals(uploadStatus)) {
								trBuilder.append(String.format(tdTemplate, "文件个数缺失"));
								if (!exceptionType.contains("文件个数")) {
									exceptionType.add("文件个数");
								}
							} else if (("32").equals(uploadStatus) && (Double.parseDouble(String.valueOf(dataMap.get("update_integrity"))) * 100 < 100.0)) {
								trBuilder.append(String.format(tdTemplate, "文件个数缺失"));
								if (!exceptionType.contains("文件个数")) {
									exceptionType.add("文件个数");
								}
							} else if (("30").equals(sizeStatus)) {
								trBuilder.append(String.format(tdTemplate, "文件大小缺失"));
								if (!exceptionType.contains("文件大小")) {
									exceptionType.add("文件大小");
								}
							} else {
								trBuilder.append(String.format(tdTemplate, "正常"));
							}
							trBuilder.append("</tr>");
						}
					}
					missBuilder.append(trBuilder);
					missBuilder.append("</table>").append("<br/>");
					prefix ++;
				}
			}
			// 组装非当日文件的内容
			StringBuilder otherBuilder = new StringBuilder();
			if (showOtherFileStats) {
				otherBuilder.append("<b>").append("二").append("、非当天文件上传情况</b>");
				Map<String, List<Map<String, String>>> otherFileData = otherFileMap.get(provinceEntity.getProvinceCode());
				if (otherFileData != null && otherFileData.size() > 0) {
					otherBuilder.append("<br/><br/>");
					int prefix = 1;
					for (String indicationKey : otherFileData.keySet()) {
						if (!exceptionIndications.contains(indicationKey)) {
							exceptionIndications.add(indicationKey);
						}
						otherBuilder.append(prefix + ".").append(indicationKey).append("<br/>");
						otherBuilder.append("<table style=\"border-collapse:collapse; \">");
						otherBuilder.append("<tr>");
						otherBuilder.append(String.format(thTemplate, "省份"));
						otherBuilder.append(String.format(thTemplate, "日期"));
						otherBuilder.append(String.format(thTemplate, "非当天文件个数"));
						otherBuilder.append(String.format(thTemplate, "非当天文件占比"));
						otherBuilder.append("</tr>");

						List<Map<String, String>> dataList = otherFileData.get(indicationKey);
						StringBuilder trBuilder = new StringBuilder();
						if (dataList != null && dataList.size() > 0) {
							for (Map<String, String> dataMap : dataList) {
								trBuilder.append("<tr>");
								trBuilder.append(String.format(tdTemplate, dataMap.get("province_name")));
								trBuilder.append(String.format(tdTemplate, dataMap.get("upload_date")));
								trBuilder.append(String.format(tdTemplate, dataMap.get("count")));
								double otherRate = Double.parseDouble(dataMap.get("other_day_count")) * 100;
								trBuilder.append(String.format(tdTemplate, otherRate + "%"));
								trBuilder.append("</tr>");
							}
						}
						otherBuilder.append(trBuilder);
						otherBuilder.append("</table>").append("<br/>");
						prefix++;
					}
				} else {
					otherBuilder.append("<p style='text-indent: 2em; margin-top: 20px;'>无</p>");
				}
			}
			if (StringUtils.isNotEmpty(missBuilder.toString())) {
				emailBuilder.append(missBuilder);
			}
			if (StringUtils.isNotEmpty(otherBuilder.toString())) {
				emailBuilder.append(otherBuilder);
			}
			if (StringUtils.isNotEmpty(emailBuilder.toString())) {
				// 开始发送邮件
				this.sendEmail(exceptionIndications, exceptionType, provinceEntity.getProvinceName(), checkDate, catalog, emailBuilder);
			}
		}
	}

	private Map<String, Map<String, List<Map<String, String>>>> formatData(List<Map<String,String>> list) {
		//重新装载一下数据 第一层 省份 第二层 指标  第三层 数据
		Map<String, Map<String, List<Map<String, String>>>> returnMap = new LinkedHashMap<String, Map<String, List<Map<String, String>>>>();
		if (list != null && list.size() > 0) {
			for (Map<String, String> otherData : list) {
				Map<String, List<Map<String, String>>> indicationData = new LinkedHashMap<String, List<Map<String, String>>>();
				String provinceKey = otherData.get("province_code");
				if (!returnMap.containsKey(provinceKey)) {
					returnMap.put(provinceKey, indicationData);
				}
				indicationData = returnMap.get(otherData.get("province_code"));
				String indicationKey = otherData.get("file_indication_code") + "-" + otherData.get("file_indication_name");
				if (!indicationData.containsKey(indicationKey)) {
					indicationData.put(indicationKey, new LinkedList<Map<String, String>>());
				}
				indicationData.get(indicationKey).add(otherData);
			}
		}
		return returnMap;
	}

}
