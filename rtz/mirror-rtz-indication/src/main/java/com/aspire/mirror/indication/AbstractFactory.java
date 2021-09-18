package com.aspire.mirror.indication;

import com.aspire.mirror.entity.CommonItemEntity;
import com.aspire.mirror.entity.ItemEntity;
import com.aspire.mirror.entity.ThemeEntity;
import com.aspire.mirror.util.IndicationConst;
import com.aspire.mirror.util.IndicationUtils;
import com.aspire.mirror.util.WSDLUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import java.util.*;

@Slf4j
public abstract class AbstractFactory<T> {

    /**
     * 主题实例
     */
    @Setter @Getter
    public ThemeEntity themeEntity;
    /**
     * 主题公共指标项实例
     */
    @Setter @Getter
    public CommonItemEntity commonEntity;
    /**
     * 主题指标项实例
     */
    @Setter @Getter
    public ItemEntity itemEntity;
    /**
     * 开始时间
     */
    @Setter @Getter
    public String beginTime;
    /**
     * 结束时间
     */
    @Setter @Getter
    public String endTime;

    /**
     * wsdl地址. themeEntity.getWsdl() + itemEntity.getWsdl()
     */
    @Getter @Setter
    public String wsdl;

    /**
     * 存放集合
     */
    @Setter @Getter
    public LinkedHashMap<String, LinkedHashMap<String, Object>> itemResult = new LinkedHashMap<String, LinkedHashMap<String, Object>>();

    public Map<String, String> SDK_RESULT_MAP = new HashMap<String, String>();

    /**
     * 省份集合
     */
    public List<String> PROVINCE_LIST = new ArrayList<String>();

    /**
     * 工厂入口
     */
    public void init() {
        wsdl = themeEntity.getWsdl() + itemEntity.getWsdl();
        initProvince();
        callWsdl();
        checkWsdlResult();
        executePrimaryItem();
        executeOtherItem();
    }

    /**
     * 初始化省份列表
     */
    public void initProvince() {
        PROVINCE_LIST = new ArrayList<String>(IndicationUtils.PROVINCE_MAP.keySet());
    }

    /**
     * 调用wsdl接口. <此为通用方法> 如果刚方法调用规则不满足子类, 子类可重写该方法. 请勿直接修改此方法.
     */
    public void callWsdl() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            WSDLUtil wsdlUtil = new WSDLUtil();
            if (itemEntity.getTimeType() != null && itemEntity.getTimeType().equalsIgnoreCase("dateList")) {
                List<String> dateList = new ArrayList<String>();
                dateList.add(endTime.substring(0,8));
                SDK_RESULT_MAP = wsdlUtil.getWsdlServiceReturnMap(classLoader, wsdl, itemEntity.getMethod(),
                        dateList, null, null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
            } else {
                SDK_RESULT_MAP = wsdlUtil.getWsdlServiceReturnMap(classLoader, wsdl, itemEntity.getMethod(),
                        beginTime, endTime, null, null, null, null, IndicationConst.QUERY_MONGODB_TYPE_PROVINCE);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 检查wsdl调用的结果. 如果返回值为空. 按照约定发出邮件告警
     */
    public void checkWsdlResult() {
        if (this.SDK_RESULT_MAP == null || this.SDK_RESULT_MAP.size() == 0) {
            //todo 发送邮件告警
        }
    }

    /**
     * 执行计算主监控项. 主监控以为其他如7日均值、逐日变动值、逐日变动率均需要改主监控项得到计算. 通常只有一个
     */
    public abstract void executePrimaryItem();

    /**
     * 执行计算的其他监控项. 这些监控项一般都是默认单独计算, 其他监控项都不依赖此监控项的值
     */
    public abstract void executeOtherItem();

    /**
     * 设置监控项的值, 如果该方法不满足, 请在子类重写该方法. 请勿直接修改.
     * @param calcTime
     * @param provinceCode
     * @param mirrorColumn
     * @param value
     */
    public void addItemResult(String calcTime, String provinceCode, String mirrorColumn, Object value) {
        //判断一下 省份和全国指标
        List<String> typeList = Arrays.asList(itemEntity.getType().split("\\|"));
        if (!typeList.contains(IndicationConst.INDICATION_GROUP_COUNTRY)
                && provinceCode.equals(IndicationConst.COUNTRY_PROVINCE_CODE)) {
            return;
        }
        if (!typeList.contains(IndicationConst.INDICATION_GROUP_PROVINCE)
                && !provinceCode.equals(IndicationConst.COUNTRY_PROVINCE_CODE)) {
            return;
        }
        String executeDate = calcTime;
        int appendLength = 14-executeDate.length();
        for (int i = 0; i < appendLength; i ++) {
            executeDate += "0";
        }
        String key = executeDate + "_" + provinceCode;
        LinkedHashMap<String, Object> valueMap = new LinkedHashMap<String, Object>();
        if (!itemResult.containsKey(key)) {
            for (ItemEntity entity : this.commonEntity.getItemList()) {
                //设置ID. 产品化用来判断相同的ID进行删除. 同一天/小时发送多次数据ID应该相同
                if (entity.getName().equalsIgnoreCase("ID")) {
                    String id = (key + this.themeEntity.getThemeCode()).replaceAll("_", "").replaceAll("-","");
                    valueMap.put(entity.getMirrorColumn(), id);
                }
                if (entity.getName().equalsIgnoreCase("日期")) {
                    valueMap.put(entity.getMirrorColumn(), calcTime);
                }
                if (entity.getName().equalsIgnoreCase("省份编码")) {
                    valueMap.put(entity.getMirrorColumn(), provinceCode);
                }
                if (entity.getName().equalsIgnoreCase("省份名称")) {
                    valueMap.put(entity.getMirrorColumn(), IndicationUtils.getProvinceByCode(provinceCode));
                }
            }
            itemResult.put(key, valueMap);
        }
        valueMap = itemResult.get(key);
        valueMap.put(mirrorColumn, value);
        log.info("valueMap : {}", JSONObject.fromObject(valueMap).toString());
    }
}
