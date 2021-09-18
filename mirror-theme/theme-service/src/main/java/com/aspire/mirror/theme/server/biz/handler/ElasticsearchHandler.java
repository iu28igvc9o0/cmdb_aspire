package com.aspire.mirror.theme.server.biz.handler;

import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.theme.api.dto.CmdbInstance;
import com.aspire.mirror.theme.api.dto.model.BizThemeDimData;
import com.aspire.mirror.theme.api.dto.model.ThemeDataDTO;
import com.aspire.mirror.theme.server.biz.helper.GrokHelper;
import com.aspire.mirror.theme.server.biz.helper.QueueHelper;
import com.aspire.mirror.theme.server.clientservice.cmdb.CmdbServiceClient;
import com.aspire.mirror.theme.server.clientservice.elasticsearch.ThemeDataServiceClient;
import com.aspire.mirror.theme.server.dao.BizThemeDao;
import com.aspire.mirror.theme.server.dao.ThemeKeyDao;
import com.aspire.mirror.theme.server.dao.po.BizTheme;
import com.aspire.mirror.theme.server.dao.po.ThemeKey;
import com.google.common.collect.Maps;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * es处理类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.biz.handler
 * 类名称:    ElasticsearchHandler.java
 * 类描述:    es处理类
 * 创建人:    JinSu
 * 创建时间:  2018/10/25 15:56
 * 版本:      v1.0
 */
@Component
public class ElasticsearchHandler {
    Logger logger = LoggerFactory.getLogger(ElasticsearchHandler.class);
    @Value("${elasticsearch.indexName}")
    private String es_index;
    private static Set<String> indexSet = new HashSet<String>();
    @Value("${monitorToken:5245ed1b-6345-11e}")
    private String monitorToken;
    @Autowired
    private QueueHelper queueHelper;

    @Autowired
    private BizThemeDao bizThemeDao;

    @Autowired
    private GrokHelper grokHelper;

    @Autowired
    private ThemeKeyDao themeKeyDao;

    @Autowired(required = false)
    private CmdbServiceClient cmdbService;

    @Autowired
    private ThemeDataServiceClient themeDataService;

    public void inserEs(ThemeDataDTO themeDataDTO, String data, List<BizThemeDimData> themeDimDataList, BizTheme
            bizTheme, String error) {
        logger.info("begin to insert es ================");
        try {
            String indexName = bizTheme.getIndexName();
            if (null == indexName) {
                return;
            }
            Map<String, Object> json = new HashMap<>();
            json.put("message", themeDataDTO.getMessage());

            Date date = StringUtils.isEmpty(themeDataDTO.getReceiveTime()) ? new Date()
                    : DateUtil.getDate(themeDataDTO.getReceiveTime(), "yyyyMMddHHmmss");
            // 正式状态才会保存维度为10 的值为@timestamp
            if (null != themeDimDataList && !themeDimDataList
                    .isEmpty()
                    && null != bizTheme && bizTheme.getStatus().equals("0")) {
                for (BizThemeDimData jkzbDimDto : themeDimDataList) {
                    if (jkzbDimDto.getDimCode().equals("timestamp")) {
                        date = getDate(jkzbDimDto);
                    }
                }
            }
            json.put("@timestamp", new DateTime(date).toString());

            if (!StringUtils.isEmpty(themeDataDTO.getHost())) {
                json.put("host", themeDataDTO.getHost());
            }
            if (!StringUtils.isEmpty(themeDataDTO.getSysCode())) {
                json.put("biz_code", themeDataDTO.getSysCode().toUpperCase().trim());
            }
            if (!StringUtils.isEmpty(themeDataDTO.getDeviceIp())) {
                json.put("device_ip", themeDataDTO.getDeviceIp());
                // TODO idc取值
                Map map = Maps.newHashMap();
                map.put("token", monitorToken);
//                map.put("idcType", "");
                map.put("ip", themeDataDTO.getDeviceIp());
                CmdbInstance cmdbInstance = cmdbService.queryDeviceByRoomIdAndIP(map);
                if (cmdbInstance != null) {
                    json.put("device_id", cmdbInstance.getId());
                }
            }
            if (!StringUtils.isEmpty(themeDataDTO.getThemeCode())) {
                json.put("theme_code", themeDataDTO.getThemeCode().toUpperCase().trim());
            }
            if (!StringUtils.isEmpty(themeDataDTO.getReceiveTime())) {
                json.put("receive_time", themeDataDTO.getReceiveTime());
            }
            if (null != error) {
                json.put("success", "1"); // 失败
                json.put("desc", error);
            } else {
                json.put("success", "0"); // 成功
            }
            String idFieldName = null;
            ThemeKey themeKey = themeKeyDao.selectByThemeId(bizTheme.getThemeId());
            if (themeKey != null && themeKey.getDimCode()!=null) {
                idFieldName = themeKey.getDimCode();
            }
            if (null != themeDimDataList) {
                // Aggregate_Flag
                json.put("aggregate_flag", "0");

                if (null != bizTheme) {
                    json.put("node_code", bizTheme.getThemeId());
                    json.put("dim_flag", data);
                    // ES默认字段类型为字符,如果指标编码类型是字符，不做处理。
                    // JCZB_Dim_Value
                    if (!CollectionUtils.isEmpty(themeDimDataList)) {
                        // 格式化数字和时间类型维度
                        Set<String> dimCodeList = themeDimDataList.stream().map(dimDto -> dimDto.getDimCode()).collect(Collectors.toSet());
                        if (dimCodeList.contains("id")) {
                            idFieldName = "id";
                        }
                        // 修改ES主题数据结构
                        Map<String, Object> param = Maps.newHashMap();
                        param.put("indexName", indexName);
                        param.put("dimList", themeDimDataList);
                        themeDataService.updateMappingByIndexName(param);
                        for (BizThemeDimData jkzbDimDto : themeDimDataList) {
                            if (jkzbDimDto.getDimType().equals("1")) {
                                String jkzbDimValue = jkzbDimDto.getDimValue();
                                // 数字类型，如果是千位计数法，做去除逗号处理
                                jkzbDimValue = jkzbDimValue.replace(",", "");
                                json.put(jkzbDimDto.getDimCode(), jkzbDimValue);
                            } else if (jkzbDimDto.getDimType().equals("3")) {
                                Date dimDate = DateUtil.getTime(jkzbDimDto.getDimValue());
                                if (dimDate == null) {
                                    dimDate = getDate(jkzbDimDto);
                                }
                                json.put(jkzbDimDto.getDimCode(),
                                        new DateTime(dimDate).toString());
                            } else {
                                json.put(jkzbDimDto.getDimCode(), jkzbDimDto.getDimValue());
                            }
                        }
                    }
                }
            }
//            IndexRequestBuilder lbr;
            queueHelper.put(indexName, idFieldName, json);
//            if (null == bizTheme || (bizTheme.getStatus().equals("1"))) {
//                // 调试状态，立即入库
//                BulkRequestBuilder bulkRequest = elasticSearcHelper.getClient().prepareBulk();
//                bulkRequest.add(lbr);
//                BulkResponse bulkResponse = bulkRequest.execute().actionGet();
//                if (bulkResponse.hasFailures()) {
//                    logger.error("es数据即时入库失败 :" + bulkResponse.getItems().toString());
//                }
//            } else {
                if (bizTheme.getUpSwitch().equals("0")) {
                    // 修改解析状态，最近上报时间，最近成功上报值,周期
                    BizTheme updateBizTheme = new BizTheme();
                    updateBizTheme.setThemeId(bizTheme.getThemeId());
                    updateBizTheme.setUpStatus(null != error ? "1" : "0");
                    if (null == error) {
                        if (null != bizTheme.getLastUpTime() && date.getTime() > bizTheme.getLastUpTime().getTime()) {
                            Long interval = (date.getTime() - bizTheme.getLastUpTime().getTime()) / 1000 / 60;
                            updateBizTheme.setInterval(interval.intValue());
                        }
                        updateBizTheme.setLastUpTime(date);
                        if (!CollectionUtils.isEmpty(themeDimDataList) && null != data && null ==
                                error) {
                            updateBizTheme.setLastUpValue(data);
                        }

                    } else {
                        updateBizTheme.setLastFailTime(date);
                    }
                    bizThemeDao.updateByPrimaryKey(updateBizTheme);
                }
//            }
        } catch (Exception e) {
            logger.error("es数据入库失败！message=" + themeDataDTO.getMessage(), e);
        }
    }

    private Date getDate(BizThemeDimData jkzbDimDto) {
        Map<String, Object> result = grokHelper.parsePattern(jkzbDimDto.getPattern(), jkzbDimDto
                .getDimValue());
        String year = null, month = null, monthDay = null, hour, minute, second;
        List<String> yearList = (List) result.get("YEAR");
        for (String item : yearList) {
            if (item != null) {
                year = item;
                break;
            }
        }
        List<String> monthList = (List) result.get("MONTHNUM");
        for (String item : monthList) {
            if (item != null) {
                month = item;
                break;
            }
        }
        List<String> dayList = (List) result.get("MONTHDAY");
        for (String item : dayList) {
            if (item != null) {
                monthDay = item;
                break;
            }
        }
        hour = (String) result.get("HOUR");
        minute = (String) result.get("MINUTE");
        second = (String) result.get("SECOND");
        final Calendar c = Calendar.getInstance();
        if (year != null && month != null && monthDay != null) {
            c.set(Calendar.YEAR, Integer.valueOf(year));
            c.set(Calendar.MONTH, Integer.valueOf(month) - 1);
            c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(monthDay));
            c.set(Calendar.HOUR_OF_DAY, hour == null ? 0 : Integer.valueOf(hour));
            c.set(Calendar.MINUTE, minute == null ? 0 : Integer.valueOf(minute));
            if (second != null && (second.contains(".") || second.contains(","))) {
                String split = "";
                if (second.contains(",")) {
                    split = ",";
                } else {
                    split = "\\.";
                }
                String[] secondSplit = second.split(split);
                logger.info("ElasticsearchHandler[getDate] second is {} secondSplit is {}", second, secondSplit);
                c.set(Calendar.SECOND, Integer.valueOf(secondSplit[0]));
                c.set(Calendar.MILLISECOND, Integer.valueOf(secondSplit[1]));
            } else {
                c.set(Calendar.SECOND, second == null ? 0 : Integer.valueOf(second));
            }
        }
        return c.getTime();
    }

//    public synchronized String getIndex(String indexName) {
//        if (!indexSet.contains(indexName)) {
//            try {
//                // TODO 添加index到ES
//                indexSet.add(indexName);
//            } catch (Exception e) {
//                logger.error("索引获取异常！", e);
//            }
//
//        }
//        return indexName;
//    }
}
