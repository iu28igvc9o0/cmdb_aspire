package com.aspire.mirror.alert.server.biz.network;

import com.aspire.mirror.alert.server.dao.monitor.po.AlertNorm;

import java.util.List;

/**
 * @author hewang
 * @version 1.0
 * @date 2021/3/11 20:47
 */
public interface IAlertNetworkBiz {

    /**
     * 端口指标配置查询
     * @param indicatorName
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<AlertNorm> queryNetworkPortIndicators(String indicatorName, int pageNum, int pageSize);

    /**
     * 端口指标新增
     * @param responses
     * @return
     */
    String addNetworkPortIndicators(List<AlertNorm> responses);

    /**
     * Top报表类型配置查询
     * @param name
     * @return
     */
    List<AlertNorm> queryTopReportTypeConfiguration(String userName);

    /**
     * Top报表类型配置更新
     * @param list
     * @return
     */
    String updateTopReportTypeConfiguration(List<AlertNorm> list);

    /**
     * Top报表类型配置更新
     * @param id
     * @return
     */
    String deleteTopReportTypeConfiguration(Integer id);
}
