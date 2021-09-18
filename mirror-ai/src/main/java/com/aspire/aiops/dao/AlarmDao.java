package com.aspire.aiops.dao;

import com.aspire.aiops.domain.ActiveAlarm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author ：Vincent Hu
 * @date ：Created in 6/6/2019 10:48
 * @description：${description}
 * @modified By：
 * @version: $version$
 */

//@Mapper
public interface AlarmDao {

    /*List<ActiveAlarm> selectAlarmTime(String time) throws Exception;*/

    List<Integer> selectItems(String time) throws Exception;

    List<Map<String, Object>> selectItems2(String time) throws Exception;


    Map<String,Long> selectTime(String time) throws Exception;

    List<Integer> selectSampleItems() throws Exception;

    Map<String,Long> selectSampleTime() throws Exception;

}
