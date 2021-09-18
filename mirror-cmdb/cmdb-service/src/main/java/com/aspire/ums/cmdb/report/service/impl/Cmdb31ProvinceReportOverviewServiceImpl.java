package com.aspire.ums.cmdb.report.service.impl;

import com.aspire.ums.cmdb.report.mapper.Cmdb31ProvinceOverviewMapper;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceReportSetting;
import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceTable;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportOverviewService;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportSettingService;
import com.aspire.ums.cmdb.report.service.ICmdb31ProvinceReportTableService;
import com.aspire.ums.cmdb.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: Cmdb31ProvinceReportOverviewServiceImpl
 * Author:   hangfang
 * Date:     2020/5/7
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class Cmdb31ProvinceReportOverviewServiceImpl implements ICmdb31ProvinceReportOverviewService {
    @Autowired
    private ICmdb31ProvinceReportSettingService settingService;

    @Autowired
    private ICmdb31ProvinceReportTableService tableService;

    @Autowired
    private Cmdb31ProvinceOverviewMapper overviewMapper;
    @Override
    public Map<String, Object> getReportOverview(String tableId, String month, String type) {
        if (!"overview".equals(type) && !"export".equals(type)) {
            throw new RuntimeException("类型为[" + type + "] 不可查看资源总览");
        }
        Cmdb31ProvinceTable table = tableService.getById(tableId, type);
        if (table == null || !table.getShowPage().contains(type)) {
            throw new RuntimeException("id为[" + tableId + "]的表数据不存在 或 不存在于总览报表中");
        }
        return getResult(table, month, type);
    }


    private Map<String, Object> getResult(Cmdb31ProvinceTable table, String month, String type) {
        Map<String, Object> result = new HashMap<>();
        List<String>  tableIds= new ArrayList<>();
        if (StringUtils.isEmpty(table.getContain())) {
            tableIds.add(table.getId());
        } else {
            tableIds = Arrays.asList(table.getContain().split(","));
        }
        List<String> headerList = new ArrayList<>();
        List<Cmdb31ProvinceReportSetting> tableSettings = settingService.listByTableIds(tableIds, type);
        List<String> dynamicHeaders = new ArrayList<>();
        tableSettings.forEach(item -> {
            if (table.getEnableMergeTitle() != null && table.getEnableMergeTitle() == 1) {
                item.setResourceType(item.getResourceGroup() + item.getResourceType());
                dynamicHeaders.add(item.getResourceType());
            } else {
                dynamicHeaders.add(item.getResourceType());
            }
        });
        result.put("tableName", table.getName());
        result.put("headers", headerList);
        switch (table.getName()) {
            case  "资源数据汇总":
                headerList.add("省份名称");
                headerList.add("资源池名称");
                headerList.add("接口人");
                headerList.addAll(dynamicHeaders);
                result.put("tableData",  overviewMapper.getResourceView(tableIds, month, tableSettings));
                break;
            case  "各省平台可用性汇总":
            case  "各省工单处理及时率汇总":
                headerList.add("省份名称");
                headerList.addAll(dynamicHeaders);
                headerList.add("审核状态");
                result.put("tableData",  overviewMapper.getHandleTimeView(tableIds, month, tableSettings));
                break;
            default:
                headerList.addAll(dynamicHeaders);
                headerList.add("审核状态");
                result.put("tableData",  overviewMapper.getDefaultView(tableIds, month, tableSettings));
        }
        return result;
    }
}
