package com.aspire.cmdb.agent.monthReport.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbDepInfoReportMapper
 * Author:   hangfang
 * Date:     2020/2/17
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface CmdbDepInfoReportMapper {

    /**
     * 统计
     */
    List<Map<String, String>>  count();


    /**
     * 存储cmdb数据
     */
    void save(List<Map<String, String>> reports);

    /**
     * 存储bpm返回数据
     */
    void saveBpm(List<Map<String, String>> reports);
    /**
     * 存储bpm返回故障数据
     */
    void saveBpmFault(List<Map<String, String>> reports);

    /**
     * 存储从告警返回数据
     */
    void saveAlert(List<Map<String, String>> reports);

}
