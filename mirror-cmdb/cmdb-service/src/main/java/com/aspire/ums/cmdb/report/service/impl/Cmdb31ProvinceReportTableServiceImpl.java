package com.aspire.ums.cmdb.report.service.impl;

import com.aspire.ums.cmdb.report.mapper.Cmdb31ProvinceTableMapper;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReportSetting;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceTable;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportSettingService;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportTableService;
import com.aspire.ums.cmdb.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: Cmdb31ProvinceReportTableServiceImpl
 * Author:   hangfang
 * Date:     2020/4/20
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class Cmdb31ProvinceReportTableServiceImpl implements ICmdb31ProvinceReportTableService {

    @Autowired
    private Cmdb31ProvinceTableMapper mapper;

    @Autowired
    private ICmdb31ProvinceReportSettingService reportSettingService;
    @Override
    public List<Cmdb31ProvinceTable> listByOwnerAndPage(String resourceOwner, String showPage) {
        List<Cmdb31ProvinceTable> tableList = mapper.listByOwnerAndPage(resourceOwner, showPage);
        for (Cmdb31ProvinceTable table : tableList) {
            table.setSettingList(reportSettingService.listByTableId(table.getId(), showPage));
        }
        return tableList;
    }

    /**
     * 根据表id获取详情
     */
    @Override
    public Cmdb31ProvinceTable getById(String id, String showPage) {
        Cmdb31ProvinceTable table = mapper.getById(id);
        if (table == null) {
            throw new RuntimeException("表 id:" + id + " 不存在");
        }
        table.setSettingList(reportSettingService.listByTableId(table.getId(), showPage));
        return table;
    }

    /**
     * 根据表名获取详情
     */
    @Override
    public Cmdb31ProvinceTable getByName(String name, String showPage) {
        Cmdb31ProvinceTable table = mapper.getByName(name);
        if (table == null) {
            throw new RuntimeException("表 [" + name + "] 不存在");
        }
        table.setSettingList(reportSettingService.listByTableId(table.getId(), showPage));
        return table;
    }

    @Override
    public Map<String, Object> getTitlesByParams(Map<String, String> queryParams) {
        if (!queryParams.containsKey("resourceOwner") || !StringUtils.isNotEmpty(queryParams.get("resourceOwner"))) {
            throw new RuntimeException("Query params.resourceOwner is require.");
        }
        if (!queryParams.containsKey("type") || !StringUtils.isNotEmpty(queryParams.get("type"))) {
            throw new RuntimeException("Query params.type is require.");
        }
        String resourceOwner = queryParams.get("resourceOwner");
        //根据type类型返回表和列
        String showPage = queryParams.get("type");
        List<Cmdb31ProvinceTable> tableList = this.listByOwnerAndPage(resourceOwner, showPage);
        if (tableList == null || tableList.size() == 0) {
            return new HashMap<>();
        }
        Map<String, Object> returnMap = new LinkedHashMap<>();
        for (Cmdb31ProvinceTable table: tableList) {
            Map<String, List<Cmdb31ProvinceReportSetting>> secondMap = new LinkedHashMap<>();
            for (Cmdb31ProvinceReportSetting setting : table.getSettingList()) {
                if (!secondMap.containsKey(setting.getResourceGroup())) {
                    secondMap.put(setting.getResourceGroup(), new ArrayList<>());
                }
                secondMap.get(setting.getResourceGroup()).add(setting);
            }
            returnMap.put(table.getName(), secondMap);
        }
        log.info("Get resource setting -> {}", returnMap);
        return returnMap;
    }
}
