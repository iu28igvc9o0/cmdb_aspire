package com.aspire.cmdb.agent.monthReport.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2020/2/13
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface CmdbCompReportMapper {
    // 从cmdb设备表中统计月报数据
    List<Map<String, String>> count();
    // 存储统计数据
    void save(List<Map<String, String>> entities);
    // 存储BPM统计数据
    void saveBpm(List<Map<String, String>> entities);
}
