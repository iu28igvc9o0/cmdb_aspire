package com.aspire.mirror.theme.server.biz.process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.theme.api.dto.CmdbInstance;
import com.aspire.mirror.theme.api.dto.model.BizThemeDimData;
import com.aspire.mirror.theme.api.dto.model.ThemeDataDTO;
import com.aspire.mirror.theme.server.biz.handler.ElasticsearchHandler;
import com.aspire.mirror.theme.server.biz.handler.themeData.ThemeCacheHolder;
import com.aspire.mirror.theme.server.biz.helper.GrokHelper;
import com.aspire.mirror.theme.server.clientservice.cmdb.CmdbServiceClient;
import com.aspire.mirror.theme.server.dao.po.BizTheme;
import com.aspire.mirror.theme.server.dao.po.BizThemeDim;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.biz.handler
 * 类名称:    LogThemeTask.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/1/8 20:15
 * 版本:      v1.0
 */
public class LogThemeTask implements Callable<String> {
    Logger logger = LoggerFactory.getLogger(LogThemeTask.class);
    @Value("${monitorToken:5245ed1b-6345-11e}")
    private String monitorToken;


    private GrokHelper grokHelper;

    private ElasticsearchHandler elasticsearchHandler;

    private RedissonClient redissonClient;

    private String themeData;

    private CmdbServiceClient cmdbServiceClient;

    private ThemeCacheHolder themeCacheHolder;

    public LogThemeTask(CmdbServiceClient cmdbServiceClient, ThemeCacheHolder themeCacheHolder, GrokHelper grokHelper, ElasticsearchHandler elasticsearchHandler, RedissonClient
            redissonClient, String themeData) {
        this.cmdbServiceClient = cmdbServiceClient;
        this.themeCacheHolder = themeCacheHolder;
        this.grokHelper = grokHelper;
        this.elasticsearchHandler = elasticsearchHandler;
        this.redissonClient = redissonClient;
        this.themeData = themeData;
    }

    @Override
    public String call() throws Exception {
        logger.info("LogThemeTask[call] beginning");
        JSONObject jsonObject = JSON.parseObject(themeData);
        String host = jsonObject.getJSONObject("fields").getString("ip");
        // 添加资源池
        String idc = jsonObject.getJSONObject("fields").getString("poolname");
        if (StringUtils.isEmpty(host)) {
            logger.error("filebeat fields ");
            return null;
        }
        String message = jsonObject.getString("message");
    // 根据IP地址获取业务系统 bpm保证传入sysCode
        CmdbInstance cmdbInstance = null;
        Map<String, String> bizSysMap = Maps.newHashMap();
        try {
//            cmdbInstance = cmdbServiceClient.queryDeviceByRoomIdAndIP(idc, host);
            Map map = Maps.newHashMap();
            map.put("token", monitorToken);
            map.put("idcType", idc);
            map.put("ip", host);
            cmdbInstance = cmdbServiceClient.queryDeviceByRoomIdAndIP(map);

            List<Map<String, Object>> cmdbBizSysList = cmdbServiceClient.getModuleData(Maps.newHashMap(), null, "default_business_system_module_id");
            cmdbBizSysList.stream().forEach(item -> {
                bizSysMap.put((String)item.get("id"), (String)item.get("bizSystem"));
            });
        }catch (Exception e) {
            logger.error("cmdb数据获取失败", e);
        }
        String sysCode = cmdbInstance.getBizSystem();
        logger.info("LogThemeTask[call] host is {} sysCode is {}", host, sysCode);
        if (!StringUtils.isEmpty(sysCode)) {
            Map<String, BizTheme> themeMap = themeCacheHolder.getThemeMap();
            Map<String, List<BizThemeDim>> themeDimMap = themeCacheHolder.getThemeDimMap();
            logger.info("LogThemeTask[call] biztheme list data is {}", themeMap.values());
            // 主题匹配
            for (BizTheme bizTheme : themeMap.values()) {
                if (bizSysMap.get(bizTheme.getObjectId()) != null && bizTheme.getObjectId().equals(sysCode)) {
                    Map<String, Object> resultMap = grokHelper.parsePattern(bizTheme.getLogReg(), message);
                    logger.info("LogThemeTask[call] log content is {}, grok resultMap is {}", message,
                            resultMap);
                    List<BizThemeDim> bizThemeDimList = themeDimMap.get(bizTheme.getThemeId());
                    boolean isSuccess = true;
                    for (BizThemeDim bizThemeDim : bizThemeDimList) {
                        if (resultMap.get(bizThemeDim.getDimCode()) == null) {
                            isSuccess = false;
                            break;
                        }
                    }
                    if (isSuccess) {
                        List<BizThemeDimData> themeDimDataList = Lists.newArrayList();
                        for (BizThemeDim bizThemeDim : bizThemeDimList) {
                            if (resultMap.get(bizThemeDim.getDimCode()) != null) {
                                BizThemeDimData bizThemeDimData = new BizThemeDimData();
                                bizThemeDimData.setDimType(bizThemeDim.getDimType());
                                bizThemeDimData.setDimValue(resultMap.get(bizThemeDim.getDimCode()).toString());
                                bizThemeDimData.setDimCode(bizThemeDim.getDimCode());
                                bizThemeDimData.setDimName(bizThemeDim.getDimName());
                                // 如果为内置字段设置表达式
                                if (bizThemeDim.getMatchFlag().equals("1")) {
                                    String pattern = "%{" + bizThemeDim.getDimReg() + ":" + bizThemeDim
                                            .getDimCode() + "}";
                                    bizThemeDimData.setPattern(pattern);
                                }
                                themeDimDataList.add(bizThemeDimData);
                            }
                        }
                        String date = DateUtil.format(new Date(), DateUtil.DATE_TIME_FORMAT);
                        // 拼装数据发送es
                        ThemeDataDTO themeDataDTO = new ThemeDataDTO();
                        themeDataDTO.setHost(host);
                        themeDataDTO.setSysCode(sysCode);
                        List<String> datas = Lists.newArrayList();
                        datas.add(message);
                        themeDataDTO.setDatas(datas);
                        themeDataDTO.setThemeCode(bizTheme.getThemeCode());
                        themeDataDTO.setTime(date);
                        themeDataDTO.setMessage(JSON.toJSONString(themeDataDTO));
                        themeDataDTO.setReceiveTime(date);
                        elasticsearchHandler.inserEs(themeDataDTO, message, themeDimDataList, bizTheme, null);
                        break;
                    }
                }
            }
        }
        return null;
    }
}
