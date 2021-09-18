package com.aspire.mirror.zabbixintegrate.daoCmdb;

import com.aspire.mirror.zabbixintegrate.daoCmdb.po.KpiKeyToMetricName;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface KpiKeyToMetricNameMapper {

    /**
     * 查询所有
     * @return
     */
    List<KpiKeyToMetricName> selectAll();
}
