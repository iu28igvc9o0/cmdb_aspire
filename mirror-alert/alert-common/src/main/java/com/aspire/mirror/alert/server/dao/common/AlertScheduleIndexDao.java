package com.aspire.mirror.alert.server.dao.common;

import com.aspire.mirror.alert.server.vo.common.AlertScheduleIndexVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertScheduleIndexDao {

    List<AlertScheduleIndexVo> getALLScheduleIndex();

    List<AlertScheduleIndexVo> getAlertScheduleIndexDetail(@Param("indexType") String indexType);

    void insertAlertScheduleIndex(AlertScheduleIndexVo request);

    void updateAlertScheduleIndex(AlertScheduleIndexVo request);

    void deleteById (@Param("id") String id);
}
