package com.aspire.ums.cmdb.cmic.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbCmicInstanceService
 * Author:   zhu.juwang
 * Date:     2020/6/1 16:01
 * Description: 中移互联网主机实例接口类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICmdbCmicInstanceService {
    /**
     * 修改IP地址信息
     * @param userName 修改用户
     * @param ipData ip地址数据
     * @param operatorType 操作方式
     * @return
     */
    Map<String, Object> updateIpInfo(String userName, Map<String, Object> ipData, String operatorType);

    /**
     * 批量新增地址库
     * @param list 录入数据集合
     */
    void batchInsertIpRepository(@Param("list") List<Map<String, Object>> list);

    /**
     * 批量新增内网地址库
     * @param list 录入数据集合
     */
    void batchInsertInnerRepository(@Param("list") List<Map<String, Object>> list);
    /**
     * 批量新增内网网段
     * @param list 录入数据集合
     */
    void batchInsertInnerSegment(@Param("list") List<Map<String, Object>> list);
    /**
     * 批量新增内网IP
     * @param list 录入数据集合
     */
    void batchInsertInnerIp(@Param("list") List<Map<String, Object>> list);
    /**
     * 批量新增操作日志
     * @param list 录入数据集合
     */
    void batchInsertOperateLog(@Param("list") List<Map<String, Object>> list);
    /**
     * 批量新增公网IP
     * @param list 录入数据集合
     */
    void batchInsertPublicRepository(List<Map<String, Object>> list);
    /**
     * 批量新增公网IP
     * @param list 录入数据集合
     */
    void batchInsertPublicSegment(List<Map<String, Object>> list);
    /**
     * 批量新增公网IP
     * @param list 录入数据集合
     */
    void batchInsertPublicIp(List<Map<String, Object>> list);
    /**
     * 批量新增IPV6
     * @param list 录入数据集合
     */
    void batchInsertIPV6Repository(List<Map<String, Object>> list);
    /**
     * 批量新增IPV6IP
     * @param list 录入数据集合
     */
    void batchInsertIPV6Segment(List<Map<String, Object>> list);
    /**
     * 批量新增IPV6IP
     * @param list 录入数据集合
     */
    void batchInsertIPV6Ip(List<Map<String, Object>> list);

    void updateAssignStatus4InnerIp(Map<String, String> params);

    void batchInsertServerProject(@Param("list") List<Map<String, Object>> list);

    void batchInsertServerCabinet(@Param("list") List<Map<String, Object>> list);

    void batchInsertServerCabinetRecord(@Param("list") List<Map<String, Object>> list);

    void batchInsertNetworkLineMgrList(@Param("list") List<Map<String, Object>> list);

    void batchInsertNetworkLineRecord(@Param("list") List<Map<String, Object>> list);

    /**
     * 插入网络线路汇总历史记录
     *
     * @param
     * @return
     */
    void batchInsertNetworkLineHis(List<Map<String, Object>> list);

    /**
     * 批量插入线路汇总结算
     * 
     * @param
     * @return
     */
    void batchInsertNetworkLineBill(List<Map<String, Object>> list);

    /**
     * 插入机柜申请记录(加电/下电)时,更新机柜汇总数量.
     * 
     * @param
     * @return
     */
    void updateServerCabinet(String optType, Map<String, Object> params);

    void genServerCabinetBillMonthly();

}
