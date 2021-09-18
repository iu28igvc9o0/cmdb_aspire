package com.aspire.mirror.indication;

import com.aspire.common.EnvConfigProperties;
import com.aspire.common.FactoryUtils;
import com.aspire.mirror.bean.IndicationLimitEntity;
import com.aspire.mirror.entity.*;
import com.aspire.mirror.service.*;
import com.aspire.mirror.service.impl.SendMinuteEmailServiceImpl;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import com.cmcc.family.alertagent.AlertAgent;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public abstract class AbstractIndicationFactory<T> {

    public Logger LOGGER = LoggerFactory.getLogger(getClass());
    public IIndicationService indicationService;
    public IIndicationItemService indicationItemService;
    public IProcessService processService;
    public IIndicationDataService dataService;
    public IIndicationProvinceService provinceService;
    private EnvConfigProperties envConfigProperties;
    //1000条 提交一次数据库
    private static final Integer LIMIT_SUBMIT_DATA_LENGTH = 1000;
    /**
     * 需要提交的计算结果集合. 每1000条, 提交一次
     */
    private ArrayList<IndicationDataEntity> commitDataList = new ArrayList<IndicationDataEntity>();

    /**
     * commitDataList达到1000条时, 枷锁
     */
    private ReentrantLock lock = new ReentrantLock();

    /**
     * 指标处理类对象
     */
    @Setter @Getter
    public ProcessEntity processEntity;

    /**
     * 处理的指标实体
     */
    @Setter @Getter
    public IndicationEntity indicationEntity;

    /**
     * 从计算日期开始往前的7个自然日集合 排列为 n n-1 n-2 n-3 n-4 n-5 n-6
     */
    @Setter @Getter
    public List<String> PREV_SEVEN_DAYS_LIST;

    /**
     * 最近7天的数据集合
     */
    @Setter @Getter
    public List<IndicationDataEntity> SEVEN_DATA_LIST = new ArrayList<IndicationDataEntity>();

    /**
     * 所有省份集合
     */
    @Getter @Setter
    public List<IndicationProvinceEntity> PROVINCE_LIST = IndicationUtils.getProvinceList();

    /**
     * 指标的主指标项
     */
    @Setter @Getter
    public Integer PRIMARY_ITEM_ID;

    /**
     * 计算时间
     */
    @Setter @Getter
    public String calcDate;

    /**
     * 前一天时间
     */
    @Setter @Getter
    public String prevDate;


    public void init() {
        initSpringBeans();
        beforeCalc();
        initSevenDays();
        initPrimaryItem();
        initPrevDate();
        initSDK();
        submitData(true);
        initSevenDataList();
        calc();
        afterCalc();
    }

    /**
     * 初始化Spring管理的对象
     */
    public void initSpringBeans() {
        indicationService = FactoryUtils.getBean(IIndicationService.class);
        indicationItemService = FactoryUtils.getBean(IIndicationItemService.class);
        processService = FactoryUtils.getBean(IProcessService.class);
        dataService = FactoryUtils.getBean(IIndicationDataService.class);
        provinceService = FactoryUtils.getBean(IIndicationProvinceService.class);
        envConfigProperties = FactoryUtils.getBean(EnvConfigProperties.class);
    }

    /**
     * 处理指标前做的操作
     * 更新数据库 该指标状态为PROCESS 时间为now()
     */
    public void beforeCalc() {
        LOGGER.info("Start handler change indication process_status {} -> {}", processEntity.getProcessStatus(),
                IndicationConst.PROCESS_STATUS_PROCESSING);
        ProcessEntity cloneEntity = (ProcessEntity) processEntity.clone();
        cloneEntity.setProcessStatus(IndicationConst.PROCESS_STATUS_PROCESSING);
        cloneEntity.setProcessLastTime(new Date());
        processService.updateProcess(cloneEntity);
    }

    /**
     * 初始化最近7天的日期
     */
    public abstract void initSevenDays();

    /**
     * 初始化最近7天的日期
     */
    public void initSevenDataList() {
        for (String dateStr : PREV_SEVEN_DAYS_LIST) {
            List<IndicationDataEntity> dataList = dataService.getIndicationDataByIndicationIdAndCalcDate(indicationEntity.getIndicationId(),dateStr);
            if (dataList != null && dataList.size() > 0) {
                SEVEN_DATA_LIST.addAll(dataList);
            }
        }
    }

    /**
     * 初始化SDK数据 初始化最近20个自然日各省的数据, 方便均值等计算
     */
    public abstract void initSDK();

    /**
     * 初始化主指标项
     */
    public void initPrimaryItem() {
        for (IndicationAllItemEntity itemEntity : indicationEntity.getIndicationItemList()) {
            if (itemEntity.getPrimaryItem() == 1) {
                this.PRIMARY_ITEM_ID = itemEntity.getIndicationItemId();
                break;
            }
        }
    }

    /**
     * 初始化前一天日期时间
     */
    public void initPrevDate() {
        String tempDate = calcDate.substring(0,8);
        try {
            Date date = DateUtils.parseDate(tempDate, new String[]{IndicationConst.QUERY_MONGODB_DATE_PATTERN});
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -1);
            prevDate = DateFormatUtils.format(calendar.getTime(), IndicationConst.QUERY_MONGODB_DATE_PATTERN) +
                    calcDate.replace(tempDate,"");
        } catch (ParseException e) {
            LOGGER.error("Parse date {} error.", tempDate, e);
        }
    }

    /**
     * 计算指标方法
     * todo 该方法可以考虑线程处理
     */
    public void calc() {
//        if (this.SEVEN_DATA_LIST == null || this.SEVEN_DATA_LIST.size() == 0) {
//            LOGGER.error("指标[{}]未获取到最近7天的数据, 无法计算.", indicationEntity.getIndicationName());
//            return;
//        }
        for (IndicationAllItemEntity itemEntity : indicationEntity.getIndicationItemList()) {
            //7日均值
            if (itemEntity.getIndicationItemName().equals(IndicationConst.INDICATION_ITEM_NAME_SEVEN_AVG)) {
                calcSevenAvg(itemEntity);
            }
            //前一天数据
            else if (itemEntity.getIndicationItemName().equals(IndicationConst.INDICATION_ITEM_NAME_PREV_VALUE)) {
                calcPrevDayValue(itemEntity);
            }
            //逐日变动值
            else if (itemEntity.getIndicationItemName().equals(IndicationConst.INDICATION_ITEM_NAME_EVERY_CHANGE)) {
                calcChangeValue(itemEntity);
            }
            //逐日变动率
            else if (itemEntity.getIndicationItemName().equals(IndicationConst.INDICATION_ITEM_NAME_EVERY_CHANGE_RATE)) {
                calcChangeRate(itemEntity);
            }
            else if (itemEntity.getIndicationItemName().equals(IndicationConst.INDICATION_ITEM_NAME_EXCEPTION_REASON)) {
                calcExceptionReason(itemEntity);
            }
            //前一天此时间段数据
            else if (itemEntity.getIndicationItemName().equals(IndicationConst.INDICATION_ITEM_NAME_PREV_DAY_HOUR_VALUE)) {
                calcPrevDayValue(itemEntity);
            }
            //逐小时变动值
            else if (itemEntity.getIndicationItemName().equals(IndicationConst.INDICATION_ITEM_NAME_EVERY_HOUR_CHANGE)) {
                calcChangeValue(itemEntity);
            }
            //逐小时变动率
            else if (itemEntity.getIndicationItemName().equals(IndicationConst.INDICATION_ITEM_NAME_EVERY_HOUR_CHANGE_RATE)) {
                calcChangeRate(itemEntity);
            }
            //其他指标特有项
            else {
                calcSpecialItem(itemEntity);
            }
        }
    }

    /**
     * 判断是否跳过指定省份的计算 如果是省份指标 全国省份则跳过, 如果是全国指标 非全国省份则跳过
     * @param provinceCode 省份编码
     * @return
     */
    public boolean isSkipProvince(String provinceCode) {
        //传入省份为全国 但是是非全国指标. 跳过
        if (provinceCode.equals(IndicationConst.COUNTRY_PROVINCE_CODE) &&
                !indicationEntity.getIndicationGroup().equals(IndicationConst.INDICATION_GROUP_COUNTRY)) {
            return true;
        }
        //传入省份为非全国 但是是全国指标. 跳过
        if (!provinceCode.equals(IndicationConst.COUNTRY_PROVINCE_CODE) &&
                indicationEntity.getIndicationGroup().equals(IndicationConst.INDICATION_GROUP_COUNTRY)) {
            return true;
        }
        return false;
    }

    /**
     * 计算7日均值
     */
    public void calcSevenAvg(IndicationAllItemEntity itemEntity) {
        for (IndicationProvinceEntity provinceEntity : PROVINCE_LIST) {
            if (isSkipProvince(provinceEntity.getProvinceCode())) {
                continue;
            }
            Double total = 0.0;
            for (String day : this.PREV_SEVEN_DAYS_LIST) {
                String value = getItemData(day, provinceEntity.getProvinceCode(), PRIMARY_ITEM_ID);
                value = value == null ? "0.0" : value;
                total = IndicationUtils.formatSum(total, value);
            }
            String formatValue = IndicationUtils.formatRate(total, 7, 1, 4);
            //String formatUnit = IndicationUtils.formatUnit(formatValue, indicationEntity.getIndicationUnit());
            submitData(itemEntity, provinceEntity.getProvinceCode(), calcDate, formatValue, formatValue, false);
        }
    }

    /**
     * 根据日期、省份获取指标项的值
     * @param date 日期
     * @param provinceCode 省份
     * @param itemId 指标项
     * @return
     */
    public String getItemData(String date, String provinceCode, Integer itemId) {
        String value = null;
        for (IndicationDataEntity dataEntity : SEVEN_DATA_LIST) {
            if (dataEntity.getProvinceCode().equals(provinceCode) && dataEntity.getCalcDate().equals(date)) {
                if (dataEntity.getIndicationItemId().equals(itemId)) {
                    value = dataEntity.getCalcValue();
                    break;
                }
            }
        }
        if (value == null) {
            LOGGER.error("指标[{}]-日期[{}]-省份[{}]未获取到指标项ID[{}]数据.", indicationEntity.getIndicationName(), date,
                    provinceCode, itemId);
        }
        return value;
    }

    /**
     * 计算前一天数据
     */
    public void calcPrevDayValue(IndicationAllItemEntity itemEntity) {
        for (IndicationProvinceEntity provinceEntity : PROVINCE_LIST) {
            if (isSkipProvince(provinceEntity.getProvinceCode())) {
                continue;
            }
            String prevValue = getItemData(prevDate, provinceEntity.getProvinceCode(), PRIMARY_ITEM_ID);
            if (prevValue == null) {
                LOGGER.error("指标[{}]-省份[{}]未获取到前一日数据, 无法计算前一日数值", indicationEntity.getIndicationName(),
                        provinceEntity.getProvinceCode());
                continue;
            }
            submitData(itemEntity, provinceEntity.getProvinceCode(), calcDate, prevValue, prevValue, false);
        }
    }

    /**
     * 计算前一天此时间段数据
     */
    public void calcPrevDayHourValue(IndicationAllItemEntity itemEntity) {

    }

    /**
     * 计算逐小时变动值
     */
    public void calcHourChangeValue(IndicationAllItemEntity itemEntity) {
        //实时指标/小时指标子类提供具体方法实现
    }

    /**
     * 计算逐小时变动率
     */
    public void calcHourChangeValueRate(IndicationAllItemEntity itemEntity) {
        //实时指标/小时指标子类提供具体方法实现
    }

    /**
     * 计算逐日变动值
     */
    public void calcChangeValue(IndicationAllItemEntity itemEntity) {
        for (IndicationProvinceEntity provinceEntity : PROVINCE_LIST) {
            if (isSkipProvince(provinceEntity.getProvinceCode())) {
                continue;
            }
            String value = getItemData(calcDate, provinceEntity.getProvinceCode(), PRIMARY_ITEM_ID);
            if (value == null) {
                LOGGER.error("指标[{}]-省份[{}]未获取到今日[{}]数据, 无法计算逐日变动值", indicationEntity.getIndicationName(),
                        provinceEntity.getProvinceCode(), calcDate);
                break;
            }
            String prevValue = getItemData(prevDate, provinceEntity.getProvinceCode(), PRIMARY_ITEM_ID);
            if (prevValue == null) {
                LOGGER.error("指标[{}]-省份[{}]未获取到前一日[{}]数据, 无法计算逐日变动值", indicationEntity.getIndicationName(),
                        provinceEntity.getProvinceCode(), prevDate);
                break;
            }
            Double changeValue = Double.parseDouble(IndicationUtils.formatEToDouble(value))
                    - Double.parseDouble(IndicationUtils.formatEToDouble(prevValue));
            String change = IndicationUtils.formatRate(changeValue, 1, 1, 4);
            submitData(itemEntity, provinceEntity.getProvinceCode(), calcDate, change, change, false);
        }
    }

    /**
     * 计算逐日变动率
     */
    public void calcChangeRate(IndicationAllItemEntity itemEntity) {
        for (IndicationProvinceEntity provinceEntity : PROVINCE_LIST) {
            if (isSkipProvince(provinceEntity.getProvinceCode())) {
                continue;
            }
            String value = getItemData(calcDate, provinceEntity.getProvinceCode(), PRIMARY_ITEM_ID);
            String prevValue = getItemData(prevDate, provinceEntity.getProvinceCode(), PRIMARY_ITEM_ID);
            String changeRate = "0.0";
            if (value == null) {
                LOGGER.error("指标[{}]-省份[{}]未获取到今日[{}]数据, 无法计算逐日变动率", indicationEntity.getIndicationName(),
                        provinceEntity.getProvinceCode(), calcDate);
            } else if (prevValue == null) {
                LOGGER.error("指标[{}]-省份[{}]未获取到前一天[{}]数据, 无法计算逐日变动率", indicationEntity.getIndicationName(),
                        provinceEntity.getProvinceCode(), prevDate);
            } else {
                Double changeValue = Double.parseDouble(IndicationUtils.formatEToDouble(value))
                        - Double.parseDouble(IndicationUtils.formatEToDouble(prevValue));
                changeRate = IndicationUtils.formatRate(changeValue, prevValue, 100, 4);
            }
            submitData(itemEntity, provinceEntity.getProvinceCode(), calcDate, changeRate, changeRate, false);
        }
    }

    /**
     * 计算异常原因
     * @param itemEntity
     */
    public void calcExceptionReason(IndicationAllItemEntity itemEntity) {
        for (IndicationProvinceEntity provinceEntity : PROVINCE_LIST) {
            if (isSkipProvince(provinceEntity.getProvinceCode())) {
                continue;
            }
            List<String> reason = new ArrayList<String>();
            IndicationLimitEntity limitEntity = indicationEntity.getLimitEntity();
            if (limitEntity == null) {
                LOGGER.warn("指标[{}]未设置阈值,跳过异常检查.", indicationEntity.getIndicationName());
                continue;
            }
            String value = getItemData(calcDate, provinceEntity.getProvinceCode(), PRIMARY_ITEM_ID);
            String prevValue = getItemData(prevDate, provinceEntity.getProvinceCode(), PRIMARY_ITEM_ID);
            if (value == null) {
                LOGGER.error("指标[{}]-省份[{}]未获取到今日数据, 无法计算异常原因", indicationEntity.getIndicationName(),
                        provinceEntity.getProvinceCode());
                continue;
            }
            Double todayValue = Double.parseDouble(value);
            //对比上限
            if (StringUtils.isNotBlank(limitEntity.getMaxValue())
                    && !limitEntity.getMaxValue().equalsIgnoreCase("nan")) {
                Double maxValue = Double.parseDouble(limitEntity.getMaxValue());
                if (todayValue > maxValue) {
                    reason.add("超过上限");
                }
            }
            //对比下限
            if (StringUtils.isNotBlank(limitEntity.getMinValue())
                    && !limitEntity.getMinValue().equalsIgnoreCase("nan")) {
                Double minValue = Double.parseDouble(limitEntity.getMinValue());
                if (todayValue < minValue) {
                    reason.add("低于下限");
                }
            }
            if (prevValue == null) {
                LOGGER.error("指标[{}]-省份[{}]未获取到前一天, 无法计算异常原因", indicationEntity.getIndicationName(),
                        provinceEntity.getProvinceCode());
            }
            if (prevValue != null) {
                Double changeData = Double.parseDouble(IndicationUtils.formatEToDouble(value))
                        - Double.parseDouble(IndicationUtils.formatEToDouble(prevValue));
                //对比变动值上限
                if (StringUtils.isNotBlank(limitEntity.getChangeValueMax())
                        && !limitEntity.getChangeValueMax().equalsIgnoreCase("nan")) {
                    Double maxChangeValue = Double.parseDouble(limitEntity.getChangeValueMax());
                    if (changeData > maxChangeValue) {
                        reason.add("超过变动值上限");
                    }
                }
                //对比变动值下限
                if (StringUtils.isNotBlank(limitEntity.getChangeValueMin())
                        && !limitEntity.getChangeValueMin().equalsIgnoreCase("nan")) {
                    Double minChangeValue = Double.parseDouble(limitEntity.getChangeValueMin());
                    if (changeData < minChangeValue) {
                        reason.add("低于变动值下限");
                    }
                }
                Double changeRate = Double.parseDouble(IndicationUtils.formatRate(changeData, prevValue, 100, 4));
                //对比变动率上限
                if (StringUtils.isNotBlank(limitEntity.getChangeRateMax())
                        && !limitEntity.getChangeRateMax().equalsIgnoreCase("nan")) {
                    Double maxChangeRate = Double.parseDouble(limitEntity.getChangeRateMax());
                    if (changeRate > maxChangeRate) {
                        reason.add("超过变动率上限");
                    }
                }
                //对比变动率下限
                if (StringUtils.isNotBlank(limitEntity.getChangeRateMin())
                        && !limitEntity.getChangeRateMin().equalsIgnoreCase("nan")) {
                    Double minChangeRate = Double.parseDouble(limitEntity.getChangeRateMin());
                    if (changeRate < minChangeRate) {
                        reason.add("低于变动率下限");
                    }
                }
            }
            String reasons = StringUtils.join(reason, ",");
            submitData(itemEntity, provinceEntity.getProvinceCode(), calcDate, reasons, reasons, false);
        }
    }

    /**
     * 需要子类重写 计算除以上公共的指标项及主指标项以外的项
     */
    public abstract void calcSpecialItem(IndicationAllItemEntity itemEntity);

    /**
     * 提交计算结果
     * @param commitNow 是否现在提交
     */
    public void submitData(boolean commitNow) {
        submitData(null, null, null, null, null, commitNow);
    }

    /**
     * 提交计算结果
     * @param itemEntity 指标项
     * @param provinceCode 省份编码
     * @param calcDate 计算日期
     * @param calcValue 计算值
     * @param commitNow 是否现在提交
     */
    public void submitData(IndicationAllItemEntity itemEntity, String provinceCode, String calcDate,
                           Object calcValue, Object origCalcValue, boolean commitNow) {
        while (lock.isLocked()) {
            LOGGER.info("Submit data list already limit {}. Please wait 1s.", LIMIT_SUBMIT_DATA_LENGTH);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.error("Thread sleep error.", e);
                throw new RuntimeException("Thread sleep error.", e);
            }
        }
        if (itemEntity != null) {
            if (isSkipProvince(provinceCode)) {
                return;
            }
            IndicationProvinceEntity provinceEntity = new IndicationProvinceEntity(provinceCode);
            IndicationDataEntity dataEntity = new IndicationDataEntity();
            dataEntity.setIndicationEntity(indicationEntity);
            dataEntity.setIndicationAllItemEntity(itemEntity);
            dataEntity.setProvinceEntity(provinceEntity);
            dataEntity.setCalcDate(calcDate);
            dataEntity.setCalcValue(String.valueOf(calcValue));
            dataEntity.setOrigCalcValue(String.valueOf(origCalcValue));
            dataEntity.setModifyDate(new Date());
            commitDataList.add(dataEntity);
        }
        if (commitDataList.size() > LIMIT_SUBMIT_DATA_LENGTH || commitNow) {
            LOGGER.info("Submit data list already limit {}. ReentrantLock locked and commit data to database.", LIMIT_SUBMIT_DATA_LENGTH);
            lock.lock();
            try {
                if (commitDataList.size() > 0) {
                    //提交数据
                    dataService.batchInsert(commitDataList);
                }
                lock.unlock();
                LOGGER.info("Commit data to database successfully. ReentrantLock unlocked.");
                commitDataList.clear();
            } catch (Exception e) {
                lock.unlock();
                LOGGER.error("Commit indication data error", e);
                throw new RuntimeException("Commit indication data error", e);
            }
        }
    }

    public void afterCalc() {
        if (commitDataList.size() > 0) {
            submitData(true);
        }
        LOGGER.info("Finish indication change process_status {} -> {}", processEntity.getProcessStatus(),
                IndicationConst.PROCESS_STATUS_FINISH);
        ProcessEntity cloneEntity = (ProcessEntity) processEntity.clone();
        cloneEntity.setProcessStatus(IndicationConst.PROCESS_STATUS_FINISH);
        cloneEntity.setProcessLastTime(new Date());
        processService.updateProcess(cloneEntity);
    }
    
    public void sendEmail(String title,String content) {
    	 log.info("由kafka将信息通过Email发送  请求 ---> 开始");
         log.info("发送邮件内容:"+content);
         final AlertAgent alert = new AlertAgent(envConfigProperties.getAlertAgent().getServer());
         alert.sendAlert(envConfigProperties.getRealNationalWeb().getAutoSendTopic(), title, content, false);
         log.info("由kafka将信息通过Email发送  请求 ---> 结束");
    }

}
