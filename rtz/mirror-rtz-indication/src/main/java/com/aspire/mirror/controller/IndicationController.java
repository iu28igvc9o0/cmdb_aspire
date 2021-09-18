package com.aspire.mirror.controller;

import com.aspire.common.JsonUtil;
import com.aspire.mirror.IIndicationAPI;
import com.aspire.mirror.api.ThemeAPI;
import com.aspire.mirror.entity.*;
import com.aspire.mirror.schedule.realnational.RtzIndicationSchedule;
import com.aspire.mirror.service.*;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.TimeUtil;
import com.aspire.mirror.util.XMLUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA. User: jw.zhu Date: 2018/10/29 软探针异常指标监控系统
 * com.aspire.mirror.controller.IndicationController
 */
@RestController
@Slf4j
public class IndicationController extends BaseController implements IIndicationAPI {

    @Autowired
    private IIndicationDataService indicationDataService;
    @Autowired
    private ISendMailService mailService;
    @Autowired
    private IIndicationLimitService iIndicationLimitService;
    @Autowired
	private RtzIndicationSchedule indicationSchedule;
    @Autowired
	private IIndicationDimService dimService;
    @Autowired
	private IIndicationThemeService themeService;

    @Override
    public JSONArray getIndicationList(@RequestParam(value = "indicationOwner") String indicationOwner,
                                       @RequestParam(value = "indicationCatalog") String indicationCatalog,
                                       @RequestParam(value = "indicationPosition") String indicationPosition,
                                       @RequestParam(value = "indicationFrequency") String indicationFrequency) throws RuntimeException {
        return indicationDataService.getIndicationList(indicationOwner, indicationCatalog, indicationPosition, indicationFrequency);
    }

    @Override
    public JSONObject getIndicationData(@RequestParam(value = "indicationOwner") String indicationOwner,
                                        @RequestParam(value = "indicationCatalog") String indicationCatalog,
                                        @RequestParam(value = "indicationPosition") String indicationPosition,
                                        @RequestParam(value = "indicationFrequency") String indicationFrequency,
                                        @RequestParam(value = "calcDate") String calcDate,
										@RequestParam(value = "provinceCode", required = false) String provinceCode,
										@RequestParam(value = "period", required = false) String period) throws RuntimeException {
		Map<String, String> filter = new HashMap<String, String>();
		filter.put("provinceCode", provinceCode);
		if (IndicationConst.INDICATION_FREQUENCY_MINUTE.equals(indicationFrequency)) {
			return indicationDataService.getMinuteIndicationData(indicationOwner, indicationCatalog, indicationPosition, indicationFrequency, calcDate, filter);
		} else {
			return indicationDataService.getIndicationData(indicationOwner, indicationCatalog, indicationPosition, indicationFrequency, calcDate, filter);
		}
    }

    @Override
    public JSONObject getSendEmailContent(@RequestParam(value = "indicationOwner") String indicationOwner,
                                                     @RequestParam(value = "indicationCatalog") String indicationCatalog,
                                                     @RequestParam(value = "indicationPosition") String indicationPosition,
                                                     @RequestParam(value = "indicationFrequency") String indicationFrequency,
                                                     @RequestParam(value = "calcDate") String calcDate) throws RuntimeException {
        return mailService.getSendEmailData(indicationOwner, indicationCatalog, indicationPosition, indicationFrequency, calcDate);
    }

    @Override
    public JSONArray reloadIndication() throws RuntimeException {
        XMLUtil.loadIndication(false);
        return JSONArray.fromObject(XMLUtil.ALL_INDICATION_LIST);
    }

    @Override
	public void reCalcIndication(@RequestParam(value = "indicationOwner") String indicationOwner,
								 @RequestParam(value = "indicationCatalog") String indicationCatalog,
								 @RequestParam(value = "indicationPosition", required = false) String indicationPosition,
								 @RequestParam(value = "indicationFrequency") String indicationFrequency,
								 @RequestParam(value = "beginDate") String beginDate,
								 @RequestParam(value = "endDate") String endDate) throws RuntimeException {
		List<ThemeEntity> themeList = XMLUtil.getThemeList(indicationFrequency);
		List<String> dateList = getDateList(beginDate, endDate, indicationFrequency);
		String bPattern = "yyyyMMdd";
		if (beginDate.length() == 10) {
			bPattern = "yyyyMMddHH";
		} else if (beginDate.length() == 14) {
			bPattern = "yyyyMMddHHmmss";
		}
		DateFormat dateFormat = new SimpleDateFormat(bPattern);
		if (themeList != null && themeList.size() > 0) {
			for (String date : dateList) {
				Calendar calendar = Calendar.getInstance();
				try {
					calendar.setTime(dateFormat.parse(date));
				} catch (ParseException e) {
					log.error(e.getMessage());
				}
				String[] dates = indicationSchedule.getSdkDate(calendar, indicationFrequency);
				indicationSchedule.calc(dates, themeList);
			}
		}
	}

	private List<String> getDateList(String beginDate, String endDate, String indicationFrequency) {
		List<String> dateList = new ArrayList<String>();
		String bPattern = "yyyyMMdd";
		if (beginDate.length() == 10) {
			bPattern = "yyyyMMddHH";
		} else if (beginDate.length() == 14) {
			bPattern = "yyyyMMddHHmmss";
		}
		try {
			Date bDate = DateUtils.parseDate(beginDate, new String[]{bPattern});
			Calendar beginCalender = Calendar.getInstance();
			beginCalender.setTime(bDate);
			Calendar endCalender = Calendar.getInstance();
			Date eDate = DateUtils.parseDate(endDate, new String[]{bPattern});
			endCalender.setTime(eDate);
			while (beginCalender.getTime().getTime() <= endCalender.getTime().getTime()) {
				if (indicationFrequency.equals(IndicationConst.INDICATION_FREQUENCY_DAY)) {
					dateList.add(DateFormatUtils.format(beginCalender.getTime(), bPattern));
				} else {
					for (int i = 0; i < 24; i ++) {
						String time = TimeUtil.addNhour(DateFormatUtils.format(beginCalender.getTime(), bPattern), null, i,"yyyyMMddHH");
						if (Long.parseLong(time) > Long.parseLong(endDate)) {
							break;
						}
						if (Long.parseLong(time) < Long.parseLong(beginDate)) {
							continue;
						}
						dateList.add(time);
					}
				}
				beginCalender.add(Calendar.DATE, 1);
			}
		} catch (ParseException e) {
			log.error(e.getMessage());
		}
		return dateList;
	}


    @Override
	public String saveGatewayMerit(@RequestBody JSONObject meritsLimits){
    	JSONArray array = (JSONArray) meritsLimits.get("meritLimits");
		List<IndicationLimitEntity> meritLimitsList = (List<IndicationLimitEntity>)JSONArray.toCollection(array, IndicationLimitEntity.class);
		iIndicationLimitService.saveMerits(meritLimitsList,false);
		return "200";
	}
    
    @Override
    public void synchronizationLimits() {
    	XMLUtil.loadIndication(true);
    	List<IndicationEntity> indications = XMLUtil.ALL_INDICATION_LIST;
    	List<IndicationLimitEntity> limits = new ArrayList<IndicationLimitEntity>();
    	NumberFormat nf = NumberFormat.getInstance();
    	  
    	for(IndicationEntity in:indications) {
    		IndicationLimitEntity limit = in.getLimitEntity();
    		if(in.getIndicationUnit().equals("万")) {
    			int unit = 10000;
    			if(!limit.getMaxValue().equalsIgnoreCase("nan")) {
    				limit.setMaxValue(nf.format(new BigDecimal(limit.getMaxValue()).divide(new BigDecimal(unit), 2,RoundingMode.HALF_UP)).toString());
    			}
    			if(!limit.getMinValue().equalsIgnoreCase("nan")) {
    				limit.setMinValue(nf.format(new BigDecimal(limit.getMinValue()).divide(new BigDecimal(unit), 2,RoundingMode.HALF_UP)).toString());
    			}
    			if(!limit.getChangeValueMax().equalsIgnoreCase("nan")) {
    				limit.setChangeValueMax(nf.format(new BigDecimal(limit.getChangeValueMax()).divide(new BigDecimal(unit), 2,RoundingMode.HALF_UP)).toString());
    			}
    			if(!limit.getChangeValueMin().equalsIgnoreCase("nan")) {
    				limit.setChangeValueMin(nf.format(new BigDecimal(limit.getChangeValueMin()).divide(new BigDecimal(unit), 2,RoundingMode.HALF_UP)).toString());
    			}
    		}
    		limits.add(in.getLimitEntity());
    	}
    	iIndicationLimitService.saveMerits(limits,true);
    }

	//@Override
	public JSONArray syncTheme() {
    	JSONArray returnArray = new JSONArray();
		//获取主题
		List<IndicationThemeEntity> themeList = themeService.getAllTheme();
		//根据主题ID 获取主题DIM列表
		for (IndicationThemeEntity themeEntity : themeList) {
			try {
				String themeInfo = ThemeAPI.getThemeInfo(themeEntity.getThemeId());
				log.info("theme info -> {}", themeInfo);
				JSONObject themeJson = JSONObject.fromObject(themeInfo);
				JSONArray dimArray = themeJson.getJSONArray("dim_list");
				List<IndicationDimEntity> newDimList = new LinkedList<IndicationDimEntity>();
				if (dimArray.size() > 0) {
					for (Object dim : dimArray) {
						JSONObject dimJson = JSONObject.fromObject(dim);
						IndicationDimEntity dimEntity = new IndicationDimEntity();
						dimEntity.setThemeId(themeEntity.getThemeId());
						dimEntity.setThemeCode(themeEntity.getThemeCode());
						dimEntity.setThemeName(JsonUtil.getString(themeJson, "theme_name", ""));
						dimEntity.setDimId(JsonUtil.getString(dimJson, "id", ""));
						dimEntity.setDimCode(JsonUtil.getString(dimJson, "dim_code", ""));
						dimEntity.setDimName(JsonUtil.getString(dimJson, "dim_name", ""));
						dimEntity.setDimOrder(JsonUtil.getString(dimJson, "dim_order", ""));
						dimEntity.setDimType(JsonUtil.getString(dimJson, "dim_type", ""));
						dimEntity.setDimReg(JsonUtil.getString(dimJson, "dim_reg", ""));
						dimEntity.setMatchFlag(JsonUtil.getString(dimJson, "match_flag", ""));
						newDimList.add(dimEntity);
					}
				}
				List<IndicationDimEntity> dimList = dimService.getDimListByThemeCode(themeEntity.getThemeCode());
				List<IndicationDimEntity> mergeList = new LinkedList<IndicationDimEntity>();
				if (newDimList.size() > 0) {
					for (IndicationDimEntity newEntity : newDimList) {
						if (dimList != null && dimList.size() >0) {
							for (IndicationDimEntity dimEntity : dimList) {
								//复制属性值
								if (newEntity.getThemeId().equals(dimEntity.getThemeId()) && newEntity.getDimCode().equals(dimEntity.getDimCode())) {
									newEntity.setHandlerClass(dimEntity.getHandlerClass());
									newEntity.setHandlerWsdl(dimEntity.getHandlerWsdl());
									newEntity.setHandlerMethod(dimEntity.getHandlerMethod());
								}
							}
						}
						mergeList.add(newEntity);
					}
				}
				if (mergeList.size() >0) {
					dimService.batchInsert(mergeList);
				}
				//删除产品化主题中已经删除的维度
				if (dimList != null && dimList.size() >0) {
					for (IndicationDimEntity dimEntity : dimList) {
						boolean isDelete = true;
						for (IndicationDimEntity newEntity : newDimList) {
							if (newEntity.getDimId().equals(dimEntity.getDimId())) {
								isDelete = false ;
								break;
							}
						}
						if (isDelete) {
							dimService.deleteDim(dimEntity);
						}
					}
				}
			} catch (Exception e) {
				String message = String.format("Sync theme %s data error. message -> %s", themeEntity.getThemeId(), e.getMessage());
				log.error(message,  e);
				returnArray.add(message);
			}
		}
		if (returnArray.size() == 0) {
			returnArray.add("Sync success.");
		}
		return returnArray;
	}

	//@Override
	public JSONArray syncIndication() {
		List<IndicationEntity> hourList = XMLUtil.getIndicationList(IndicationConst.INDICATION_OWNER_REAL, IndicationConst.CATALOG_BOX, null,
				IndicationConst.INDICATION_FREQUENCY_HOUR);
		List<IndicationEntity> actualList = XMLUtil.getIndicationList(IndicationConst.INDICATION_OWNER_REAL, IndicationConst.CATALOG_BOX, null,
				IndicationConst.INDICATION_FREQUENCY_ACTUAL);
		actualList.addAll(hourList);
		mailService.sendHourIndicationEmail(actualList, "20190305110000");


		JSONArray returnArray = new JSONArray();
		//获取主题
//		List<IndicationThemeEntity> themeList = themeService.getAllTheme();
//		for (IndicationThemeEntity themeEntity : themeList) {
//			JSONArray itemArray = ItemAPI.getItemList(themeEntity.getThemeCode());
//			if (itemArray.size() > 0) {
//				IndicationEntity indicationEntity;
//				for (Object object : itemArray) {
//					JSONObject item = JSONObject.fromObject(object);
//					String key = item.getString("key");
//					/*
//					 * 在产品化中定义指标时, 需要严格按照规则进行定义 key 使用"_"进行分割, 每一段意义如下：
//					 * key[0]: 指标分组 全国/各省
//					 * key[1]: 指标显示位置 概览/非概览
//					 * key[2]: 指标类型 机顶盒/网关
//					 * key[3]: 指标计算颗粒度 月/日/实时/小时/分
//					 * key[4]: 指标计算频率 月/日/实时/小时/分
//					 * key[5]: 指标单位 万/%
//					 * key[6]: 指标排序, 页面中指标显示的顺序, 按照此值进行排序
//					 *
//					 * 指标名称相同的指标, 视为同一个指标. 通过indicationGroup判断是全国指标, 还是省份指标
//					 */
//					String[] keys = key.split("_");
//					indicationEntity = new IndicationEntity();
//					indicationEntity.setIndicationId(item.getString("item_id"));
//					indicationEntity.setIndicationName(item.getString("name"));
//					indicationEntity.setIndicationOwner(themeEntity.getOwner());
//					indicationEntity.setSysCode(themeEntity.getSysCode());
//					indicationEntity.setThemeCode(themeEntity.getThemeCode());
//					indicationEntity.setIndicationGroup(keys[0]);
//					indicationEntity.setIndicationPosition(keys[1]);
//					indicationEntity.setIndicationCatalog(keys[2]);
//					indicationEntity.setIndicationCycle(keys[3]);
//					indicationEntity.setIndicationFrequency(keys[4]);
//					indicationEntity.setIndicationUnit(keys[5]);
//					indicationEntity.setIndicationOrder(Integer.parseInt(keys[6]));
//					if (item.containsKey("biz_theme_exp")) {
//						indicationEntity.setBizThemeExp(item.getString("biz_theme_exp"));
//					}
//					if (isSync) {
//						// 解析指标阈值
//						if (item.containsKey("biz_theme_exp")) {
//							JSONArray expArray = JSONArray.fromObject(item.get("biz_theme_exp"));
//							if (expArray != null && expArray.size() > 0) {
//								for (Object expObj : expArray) {
//									JSONObject expJson = JSONObject.fromObject(expObj);
//									if (IndicationConst.INDICATION_ITEM_NAME_EXCEPTION_REASON.equals(expJson.get("name").toString())) {
//										indicationEntity.setLimitEntity(parseIndicationLimit(expJson.get("exp").toString(), item.getString("item_id")));
//										break;
//									}
//								}
//							}
//						}
//					} else {
//						if(item.getString("item_id").equals("871cd9df-b882-4ebf-87a2-07b529bedc33")) {
//							log.info("cccc");
//						}
//						List<IndicationLimitEntity> limits = getIndicationLimitService().getIndicationLimitByIndicationId(item.getString("item_id"));
//						if (limits.size() > 0) {
//							indicationEntity.setLimitEntity(limits.get(0));
//						}
//					}
//					boolean exists = false;
//					for (IndicationEntity entity : ALL_INDICATION_LIST) {
//						if (entity.getIndicationId().equals(indicationEntity.getIndicationId())) {
//							exists = true;
//							break;
//						}
//					}
//					if (!exists) {
//						ALL_INDICATION_LIST.add(indicationEntity);
//						log.info(
//								"Indication data {} add to ALL_INDICATION_LIST.",
//								indicationEntity.toString());
//					}
//				}
//			}
//		}
		return null;
	}
}
