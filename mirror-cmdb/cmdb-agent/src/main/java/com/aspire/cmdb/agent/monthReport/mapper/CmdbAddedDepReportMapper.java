package com.aspire.cmdb.agent.monthReport.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbAddedDepReportMapper
 * Author:   hangfang
 * Date:     2020/2/17
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface CmdbAddedDepReportMapper {

    /**
     * 统计
     */
    List<Map<String, String>>  count();

    /**
     * 存储
     */
    void save(List<Map<String, String>> reports);

    /**
     * 存储利用率等
     */
    void saveAlert(List<Map<String, String>> reports);
}
