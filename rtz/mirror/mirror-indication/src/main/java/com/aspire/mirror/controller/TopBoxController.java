package com.aspire.mirror.controller;

import com.aspire.common.BaseController;
import com.aspire.common.EnvConfigProperties;
import com.aspire.mirror.TopBoxAPI;
import com.aspire.mirror.bean.IndicationLimitEntity;
import com.aspire.mirror.dao.TopBoxDAO;
import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.mirror.entity.IndicationEntity;
import com.aspire.mirror.service.IIndicationLimitService;
import com.aspire.mirror.service.IIndicationService;
import com.aspire.mirror.service.SendEmailService;
import com.aspire.mirror.util.IndicationConst;
import com.cmcc.family.alertagent.AlertAgent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ws
 * Date: 2018/11/07
 * 软探针异常指标监控系统
 * com.aspire.mirror.controller.TopBoxController
 */
@RestController
@Slf4j
public class TopBoxController extends BaseController<TopBoxController> implements TopBoxAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopBoxController.class);

    @Autowired
    private IIndicationService indicationService;

    @Autowired
    private IIndicationLimitService indicationLimitService;

    @Autowired
    private TopBoxDAO topboxdao;

    @Autowired
    private EnvConfigProperties envConfigProperties;

    /**
     * 获取机顶盒指标数据
     * @return Object
     */
    public Object getIndicationData(@RequestParam(value = "indicationOwner") String indicationOwner,
                                    @RequestParam(value = "day") String day) {

        Object response = null;

        LOGGER.info("[REQUEST]>>>" + day);
        List<IndicationEntity> data = indicationService.getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(
                indicationOwner, IndicationConst.CATALOG_BOX, day,null, null);
        LOGGER.info("[RESPONSE]>>>" + data.toString());
        response = data;
        return response;
    }

    /**
     * 更新机顶盒指标数据
     * param Map<String, Object>
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public Object updateIndicationData(@RequestBody Map<String, Object> param) {
        Map<String,Object> response = Maps.newHashMap();
        LOGGER.info("[REQUEST]>>>" + param.toString());
        List<Map<String,String>> data = (List<Map<String,String>>)param.get("param");
        List<String> indicationIds = (List<String>)param.get("indicationIds");
        try {
//            topboxdao.updateIndicationData(data);
            topboxdao.deleteIndicationData(indicationIds);
            topboxdao.insertIndicationData(data);
            response.put("flag",true);
            response.put("msg","修改成功!");
        } catch (Exception e) {
            LOGGER.error("[ERROR]>>>" + e);
            response.put("flag",false);
            response.put("msg",e);
        }
        return response;
    }

    /**
     *  手动发送邮件
     * @param  param
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object sendEmail(@RequestBody Map<String, Object> param) {
        LOGGER.info("[REQUEST]>>>" + param.toString());
        List<String> privenceList = (List<String>)param.get("privenceList");
        List<Map<String,String>> emailData = (List<Map<String,String>>)param.get("data");
        String day = (String)param.get("day");
        String type = (String)param.get("type");

        for (String province : privenceList ) {
            //获取当前省份异常数据的集合
            List<Map<String,String>> provinceData = Lists.newArrayList();
            for (Map<String,String> data : emailData) {
                if (isProvience(data,province)) {
                    provinceData.add(data);
                }
            }
            if (null != provinceData && provinceData.size() > 0) {
                StringBuffer emailIndicationBuffer = new StringBuffer();
                //获取当前省份指标数据的集合
                List<IndicationEntity> getIndicationList;
                if ("全国".equals(province)) {
                    getIndicationList = indicationService.getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(
                            IndicationConst.INDICATION_OWNER_ALL, type, IndicationConst.INDICATION_FREQUENCY_DAY,
                            IndicationConst.INDICATION_GROUP_COUNTRY, null);
                } else {
                    getIndicationList = indicationService.getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(
                            IndicationConst.INDICATION_OWNER_ALL, type, IndicationConst.INDICATION_FREQUENCY_DAY,
                            IndicationConst.INDICATION_GROUP_PROVINCE, null);
                }
                String thTemplate = "<th style='border: 1px solid #000; text-align:center; font-size:13px'>%s</th>";
                String redTemplate = "<td style='border: 1px solid #000; text-align:center; font-size:13px; color:red;'>%s</td>";
                String tdTemplate = "<td style='border: 1px solid #000; text-align:center; font-size:13px;'>%s</td>";

                int num = 1;
                for (IndicationEntity indicationEntity : getIndicationList) {
                    //每个指标数据的集合
                    List<Map<String,String>> indicationData = Lists.newArrayList();
                    for (Map<String,String> map : provinceData) {
                        if (indicationEntity.getIndicationId() == Integer.valueOf(map.get("indicationId"))) {
                            indicationData.add(map);
                        }
                    }
                    StringBuffer indicationBuffer = new StringBuffer();
                    if (null != indicationData && indicationData.size() > 0) {
                        //指标表头部分
                        final IndicationLimitEntity limitEntity = indicationEntity.getLimitEntity();
                        String max = StringUtils.isNotEmpty(limitEntity.getMaxValue()) ?
                                "上限:"+limitEntity.getMaxValue()+indicationEntity.getIndicationUnit() : "NaN"+indicationEntity.getIndicationUnit();
                        String min = StringUtils.isNotEmpty(limitEntity.getMinValue()) ?
                                "下限:"+limitEntity.getMinValue()+indicationEntity.getIndicationUnit() : "NaN"+indicationEntity.getIndicationUnit();
                        String changeValueMax = StringUtils.isNotEmpty(limitEntity.getChangeValueMax()) ?
                                "变动值上限:"+limitEntity.getChangeValueMax()+indicationEntity.getIndicationUnit() : "NaN"+indicationEntity.getIndicationUnit();
                        String changeValueMin = StringUtils.isNotEmpty(limitEntity.getChangeValueMin()) ?
                                "变动值下限:"+limitEntity.getChangeValueMin()+indicationEntity.getIndicationUnit() : "NaN"+indicationEntity.getIndicationUnit();
                        String changeRateMax = StringUtils.isNotEmpty(limitEntity.getChangeRateMax()) ?
                                "变动率上限:"+limitEntity.getChangeRateMax()+"%" : "NaN %";
                        String changeRateMin = StringUtils.isNotEmpty(limitEntity.getChangeRateMin()) ?
                                "变动率下限:"+limitEntity.getChangeRateMin()+"%" : "NaN %";
                        indicationBuffer.append("<b>").append(num).append(".").append(indicationEntity.getIndicationName()).append("</b><br/>");
                        //if (!"全国".equals(province)) {
                        indicationBuffer.append("<span style='font-size:11px;'>(").append(indicationEntity.getIndicationName().substring(2)).append("--")
                                .append(max).append(",").append(min).append(",").append(changeValueMax).append(",").append(changeValueMin)
                                .append(",").append(changeRateMax).append(",").append(changeRateMin).append(")</span>").append("<br/>");
                        //}
                        //表格部分
                        StringBuffer tableBuffer = new StringBuffer();
                        tableBuffer.append("<table style=\"border-collapse:collapse; width:100%\">");

                        //设置表头
                        StringBuilder thBuffer = new StringBuilder();
                        Iterator iterator = indicationData.get(0).keySet().iterator();
                        while (iterator.hasNext()) {
                            String key = iterator.next().toString();
                            if (!"indicationId".equals(key)) {
                                thBuffer.append(String.format(thTemplate, key));
                            }
                        }
                        tableBuffer.append("<tr>").append(thBuffer).append("</tr>");
                        //设置正文
                        for (Map<String,String> trData : indicationData) {
                            StringBuilder trBuffer = new StringBuilder();
                            String reasonData = null;
                            if (trData.containsKey(IndicationConst.INDICATION_ITEM_NAME_EXCEPTION_REASON)) {
                                reasonData = trData.get(IndicationConst.INDICATION_ITEM_NAME_EXCEPTION_REASON);
                            }
                            List<String> reasonList = new ArrayList<String>();
                            if (reasonData != null) {
                                reasonList.addAll(Arrays.asList(reasonData.split(",")));
                            }
                            Iterator iterator2 = indicationData.get(0).keySet().iterator();
                            while (iterator2.hasNext()) {
                                String key = iterator2.next().toString();
                                if ("indicationId".equals(key)) {
                                    continue;
                                }
                                boolean isReasonColumn = false;
                                if (reasonList.contains("超过上限") || reasonList.contains("低于下限")) {
                                    for (IndicationAllItemEntity itemEntity : indicationEntity.getIndicationItemList()) {
                                        if (itemEntity.getPrimaryItem() == 1) {
                                            if (itemEntity.getIndicationItemName().equals(key)) {
                                                trBuffer.append(String.format(redTemplate, trData.get(key)));
                                                isReasonColumn = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (reasonList.contains("超过变动值上限") || reasonList.contains("低于变动值下限")) {
                                    if (key.contains("变动值")) {
                                        trBuffer.append(String.format(redTemplate, trData.get(key)));
                                        isReasonColumn = true;
                                    }
                                }
                                if (reasonList.contains("超过变动率上限") || reasonList.contains("低于变动率下限")) {
                                    if (key.contains("变动率")) {
                                        trBuffer.append(String.format(redTemplate, trData.get(key)));
                                        isReasonColumn = true;
                                    }
                                }
                                if (!isReasonColumn) {
                                    trBuffer.append(String.format(tdTemplate, trData.get(key)));
                                }
                            }
                            tableBuffer.append("<tr>").append(trBuffer).append("</tr>");
                        }
                        tableBuffer.append("</table>").append("<br/>");
                        indicationBuffer.append(tableBuffer);
                        num+=1;
                    }
                    emailIndicationBuffer.append(indicationBuffer);
                }
                //拼装省份邮件
                String title = "";
                String model = "";
                if (province.equals("全国")) {
                    title = IndicationConst.MAIL_COUNTRY_TITLE_TEMPLATE;
                    title = String.format(title, day, type);
                    model = IndicationConst.MAIL_COUNTRY_CONTENT_TEMPLATE;
                    model = String.format(model, day, type, emailIndicationBuffer);
                } else {
                    title = IndicationConst.MAIL_PROVINCE_TITLE_TEMPLATE;
                    title = String.format(title, province, day, type);
                    model = IndicationConst.MAIL_PROVINCE_CONTENT_TEMPLATE;
                    model = String.format(model, province, day, type, emailIndicationBuffer);
                }
                LOGGER.info("由kafka将信息通过Email发送  请求 ---> 开始");
                LOGGER.info("发送邮件内容:{}", model);
                final AlertAgent alert = new AlertAgent(envConfigProperties.getAlertAgent().getServer());
                alert.sendAlert(envConfigProperties.getNationalWeb().getSendTopic(), title, model, false);
                LOGGER.info("由kafka将信息通过Email发送  请求 ---> 结束");
            }
        }
        LOGGER.info("[RESPONSE]>>>SUCCESS");
        return "SUCCESS";
    }

    /**
     *
     * @param data
     * @param proince
     * @return
     */
    private Boolean isProvience(Map<String,String> data,String proince) {
        for (String key : data.keySet()) {
            if (proince.equals(data.get(key))) {
                return true;
            }
        }
        return false;
    }


    @Autowired
    private SendEmailService sendEmailService;

    /**
     * 自动发送邮件
     * @param dateTime
     * @return
     */
    public Object autoSendEmail(@RequestParam String indicationOwner, @RequestParam String dateTime) {
        String response;
        try {
            String frequency = IndicationConst.INDICATION_FREQUENCY_DAY;
            if (indicationOwner.equals(IndicationConst.INDICATION_OWNER_REAL)) {
                frequency = IndicationConst.INDICATION_FREQUENCY_HOUR;
            }
            response = sendEmailService.autoSendEmail(indicationOwner, IndicationConst.CATALOG_BOX,
                    IndicationConst.INDICATION_CYCLE_DAY, frequency, dateTime);
            if (indicationOwner.equals(IndicationConst.INDICATION_OWNER_ALL)) {
                response = sendEmailService.autoSendEmail(indicationOwner, IndicationConst.CATALOG_GATEWAY,
                        IndicationConst.INDICATION_CYCLE_DAY, frequency, dateTime);
            }
        } catch (Exception e) {
            response = e.getMessage();
        }
        return response;
    }

}
