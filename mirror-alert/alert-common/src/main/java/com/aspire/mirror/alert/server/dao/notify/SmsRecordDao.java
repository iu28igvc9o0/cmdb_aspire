package com.aspire.mirror.alert.server.dao.notify;

import com.aspire.mirror.alert.server.dao.notify.po.SmsRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
/**
 * @author menglinjie
 */
@Mapper
public interface SmsRecordDao {
    int deleteByPrimaryKey(String id);

    void insert(SmsRecord record);

    SmsRecord selectByPrimaryKey(String id);

    List<SmsRecord> selectAll();

    int updateByPrimaryKey(SmsRecord record);

    List<SmsRecord> findSmsList(@Param("startDateTime") Date startDateTime,
                                @Param("endDateTime") Date endDateTime,
                                @Param("receiver") String receiver,
                                @Param("content") String content,
                                @Param("status")Integer status,
                                @Param("startNo") Integer startNo,
                                @Param("pageSize") Integer pageSize);

    int countFindSmsList(@Param("startDateTime") Date startDateTime,
                         @Param("endDateTime") Date endDateTime,
                         @Param("receiver") String receiver,
                         @Param("content") String content,
                         @Param("status")Integer status);

    void updateIsDelete(@Param("id")String id);
}