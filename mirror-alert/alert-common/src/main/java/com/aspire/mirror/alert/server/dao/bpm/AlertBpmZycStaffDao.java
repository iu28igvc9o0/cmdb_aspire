package com.aspire.mirror.alert.server.dao.bpm;

import com.aspire.mirror.alert.server.dao.bpm.po.AlertBpmZycStaff;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertBpmZycStaffDao {
    /**
     * 根据资源池获取对应的监控维护接口人
     * @param zyc
     * @return
     */
    List<AlertBpmZycStaff> selectByZyc(@Param(value = "zyc") String zyc);
}
