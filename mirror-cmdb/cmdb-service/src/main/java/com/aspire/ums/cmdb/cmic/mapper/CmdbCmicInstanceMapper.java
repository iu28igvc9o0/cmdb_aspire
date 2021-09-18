package com.aspire.ums.cmdb.cmic.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司 FileName: CmdbCmicInstanceMapper Author: zhu.juwang Date: 2020/7/10 13:52 Description:
 * ${DESCRIPTION} History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
@Mapper
public interface CmdbCmicInstanceMapper {

    /**
     * 批量新增地址库
     * 
     * @param list
     *            录入数据集合
     */
    void batchInsertIpRepository(@Param("list") List<Map<String, Object>> list);

    /**
     * 批量新增内网地址库
     * 
     * @param list
     *            录入数据集合
     */
    void batchInsertInnerRepository(@Param("list") List<Map<String, Object>> list);

    /**
     * 批量新增内网网段
     * 
     * @param list
     *            录入数据集合
     */
    void batchInsertInnerSegment(@Param("list") List<Map<String, Object>> list);

    /**
     * 批量新增内网IP
     * 
     * @param list
     *            录入数据集合
     */
    void batchInsertInnerIp(@Param("list") List<Map<String, Object>> list);

    /**
     * 批量新增操作日志
     * 
     * @param list
     *            录入数据集合
     */
    void batchInsertOperateLog(@Param("list") List<Map<String, Object>> list);

    /**
     * 批量新增公网IP
     * 
     * @param list
     *            录入数据集合
     */
    void batchInsertPublicRepository(@Param("list") List<Map<String, Object>> list);

    /**
     * 批量新增公网IP
     * 
     * @param list
     *            录入数据集合
     */
    void batchInsertPublicSegment(@Param("list") List<Map<String, Object>> list);

    /**
     * 批量新增公网IP
     * 
     * @param list
     *            录入数据集合
     */
    void batchInsertPublicIp(@Param("list") List<Map<String, Object>> list);

    /**
     * 批量新增IPV6
     * 
     * @param list
     *            录入数据集合
     */
    void batchInsertIPV6Repository(List<Map<String, Object>> list);

    /**
     * 批量新增IPV6IP
     * 
     * @param list
     *            录入数据集合
     */
    void batchInsertIPV6Segment(List<Map<String, Object>> list);

    /**
     * 批量新增IPV6IP
     * 
     * @param list
     *            录入数据集合
     */
    void batchInsertIPV6Ip(List<Map<String, Object>> list);

    void updateAssignStatus4InnerIp(Map<String, String> params);

    void updateAssignStatus(@Param("tableName") String tableName, @Param("instanceIdList") List<String> instanceIdList,
            @Param("instanceData") Map<String, Object> params);

    void batchInsertServerProject(List<Map<String, Object>> list);

    void batchInsertServerCabinet(List<Map<String, Object>> list);

    void batchInsertServerCabinetRecord(List<Map<String, Object>> list);

    void batchInsertNetworkLineMgr(List<Map<String, Object>> list);

    void batchInsertNetworkLineRecord(List<Map<String, Object>> list);

    /**
     * 插入网络线路汇总历史记录
     * 
     * @param
     * @return
     */
    void batchInsertNetworkLineHis(List<Map<String, Object>> list);

    void batchInsertNetworkLineBill(List<Map<String, Object>> list);

    /**
     * 按月统计机柜结算
     * 
     * @param
     * @return
     */
    void batchInsertServerCabinetBillReport(List<Map<String, Object>> list);

    void updateServerCabinetBillTotal(Map<String, Object> params);

    List<Map<String, String>> selectServerCabinet4Bill(Map<String, Object> params);

    Integer countServerCabinetBillMonth(Map<String, String> params);

    /**
     * 插入机柜申请记录(加电/下电)时,增加机柜汇总数量.
     *
     * @param
     * @return
     */
    void incrServerCabinet(Map<String, Object> params);

    /**
     * 插入机柜申请记录(加电/下电)时,减少机柜汇总数量.
     *
     * @param
     * @return
     */
    void decrServerCabinet(Map<String, Object> params);

}
