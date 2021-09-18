package com.aspire.mirror.alert.server.dao.third;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AlertsTodayWaringMessageDao {
    /**
     *     通过资源池去查询出清除和解除的告警信息
     */
    List<Map<String, Object>> getHisSummaryByIdctyp(@Param(value = "param") Map<String, Object> param);
}
