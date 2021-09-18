package com.aspire.mirror.controller;

import com.aspire.common.BaseController;
import com.aspire.common.FactoryUtils;
import com.aspire.mirror.IMirrorAPI;
import com.aspire.mirror.entity.IndicationEntity;
import com.aspire.mirror.entity.ProcessEntity;
import com.aspire.mirror.service.IIndicationDataService;
import com.aspire.mirror.service.IIndicationService;
import com.aspire.mirror.service.IProcessService;
import com.aspire.mirror.service.SendEmailService;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.real_mirror.indication.AbstractRealMirrorIndicationFactory;
import com.aspire.real_mirror.indication.actual.AbstractRealActualFactory;
import com.aspire.real_mirror.indication.hour.AbstractRealHourFactory;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: jw.zhu Date: 2018/10/29 软探针异常指标监控系统
 * com.aspire.mirror.controller.MirrorController
 */
@RestController
@Slf4j
public class MirrorController extends BaseController<MirrorController> implements IMirrorAPI {

    @Autowired
    private IIndicationService indicationService;
    @Autowired
    private IProcessService processService;
	@Autowired
	private IIndicationDataService dataService;
    @Autowired
    private SendEmailService sendEmailService;

    @Override
    public void calcIndication(@RequestParam(value = "formPlat", required = false) String formPlat,
                               @RequestParam(value = "indicationOwner") String indicationOwner,
                               @RequestParam(value = "indicationFrequency") String indicationFrequency,
                               @RequestParam(value = "beginDate") String beginDateStr,
                               @RequestParam(value = "endDate") String endDateStr,
                               @RequestParam(value = "indicationId", required = false) Integer indicationId) throws ParseException {
        if (StringUtils.isEmpty(formPlat)) {
            formPlat = "Mirror auto jab";
        }
        log.info("Call calc indication form {}.", formPlat);
        if (formPlat.equals("Spark")) {
            //设置计算最近4天的数据
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date beginDate = dateFormat.parse(beginDateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(beginDate);
            calendar.add(Calendar.DATE, -4);
            beginDateStr = dateFormat.format(calendar.getTime());
        }
        if (indicationOwner.equals(IndicationConst.INDICATION_OWNER_ALL) || indicationOwner.equals("1")) {
            if (indicationFrequency.equals(IndicationConst.INDICATION_FREQUENCY_DAY) || indicationFrequency.equals("2")) {
                try {
                    long start = new Date().getTime();
                    log.info("Box day indication : start at {}.", start);
                    List<IndicationEntity> dayList = indicationService.getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(
                            IndicationConst.INDICATION_OWNER_ALL, null, IndicationConst.INDICATION_FREQUENCY_DAY, null, null);
                    dataService.runDayCalc(beginDateStr, endDateStr, dayList, indicationId);
                    long end = new Date().getTime();
                    log.info("Day Indication : end at {}. use time {} s.", end, (end - start) / 1000);
                } catch (ParseException e) {
                    log.error("Date parse error. beginDate:{} endDate:{}", beginDateStr, endDateStr ,e);
                }
            }
            if (indicationFrequency.equals(IndicationConst.INDICATION_FREQUENCY_HOUR)) {
                try {
                    long start = new Date().getTime();
                    log.info("Box day indication : start at {}.", start);
                    List<IndicationEntity> hourList = indicationService.getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(
                            IndicationConst.INDICATION_OWNER_ALL, null, IndicationConst.INDICATION_FREQUENCY_HOUR, null, null);
                    dataService.runHourCalc(beginDateStr, endDateStr, hourList, indicationId);
                    long end = new Date().getTime();
                    log.info("Hour Indication : end at {}. use time {} s.", end, (end - start) / 1000);
                } catch (ParseException e) {
                    log.error("Date parse error. beginDate:{} endDate:{}", beginDateStr, endDateStr ,e);
                }
            }
            //发送邮件
            if (formPlat.equals("Spark")) {
                log.info("Spark send waiting calc finish ...");
                try {
                    sendEmailService.autoSendEmail(IndicationConst.INDICATION_OWNER_ALL, IndicationConst.CATALOG_BOX,
                            IndicationConst.INDICATION_CYCLE_DAY, IndicationConst.INDICATION_FREQUENCY_DAY, endDateStr);
                } catch (Exception e) {
                    log.error("Spark: Send email error. {}", e.getMessage(), e);
                }
                try {
                    sendEmailService.autoSendEmail(IndicationConst.INDICATION_OWNER_ALL, IndicationConst.CATALOG_GATEWAY,
                            IndicationConst.INDICATION_CYCLE_DAY, IndicationConst.INDICATION_FREQUENCY_DAY, endDateStr);
                } catch (Exception e) {
                    log.error("Spark: Send email error. {}", e.getMessage(), e);
                }
            }
        }
        if (indicationOwner.equals(IndicationConst.INDICATION_OWNER_REAL)  || indicationOwner.equals("2") ) {
                List<IndicationEntity> list = indicationService.getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(indicationOwner,
                        IndicationConst.CATALOG_BOX, indicationFrequency, null, null);
                if (list != null && list.size() > 0) {
                    for (IndicationEntity entity : list) {
                        try {
                            log.info("Begin handler indication {}. data {} started at {}", entity.getIndicationName(), beginDateStr, new Date().getTime());
                            //根据指标ID, 查找对应的处理类 进行计算
                            ProcessEntity processEntity = processService.getProcessByIndicationId(entity.getIndicationId());
                            if (processEntity == null) {
                                log.error("Can't find indication_id " + entity.getIndicationId() + " process class. Please check it.");
                                continue;
                            }
                            log.info("Handler indication {} process class [{}]", entity.getIndicationName(), processEntity.getProcessClass());
                            AbstractRealMirrorIndicationFactory factory = FactoryUtils.invokeRealFactory(processEntity.getProcessClass());
                            if (factory instanceof AbstractRealActualFactory || factory instanceof AbstractRealHourFactory) {
                                factory.setBeginTime(beginDateStr);
                                factory.setEndTime(endDateStr);
                            } else {
                                factory.setCalcDate(beginDateStr);
                            }
                            factory.setIndicationEntity(entity);
                            factory.setProcessEntity(processEntity);
                            factory.init();
                        } catch (Exception e) {
                            log.error("Handler day indication {} error:{}", entity.getIndicationName(), e.getMessage(), e);
                        }
                    }
                }
        }
    }

	@Override
	public String getExceptionData(String indicationOwner, String catalogBox, String indicationCycle,
								   String indicationFrequency,String dateTime, String group, String provinceCode) {
		return dataService.getExceptionData(indicationOwner, catalogBox, indicationCycle, indicationFrequency,dateTime, group, provinceCode, null).toString();
	}
	@Override
	public void synchronizationIndicationData() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        String calcDate = DateFormatUtils.format(c.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN);
        log.info("清理数据开始：*****************************************");
        dataService.clearData(calcDate);
        log.info("清理数据结束：*****************************************");
    }

    @Override
    public JSONObject getEMail(String indicationOwner, String catalogBox, String indicationCycle, String indicationFrequency, String dateTime) {
        return null;
    }


}
