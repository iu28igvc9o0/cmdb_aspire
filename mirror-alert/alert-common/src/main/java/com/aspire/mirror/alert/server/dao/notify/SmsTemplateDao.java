package com.aspire.mirror.alert.server.dao.notify;


import com.aspire.mirror.alert.server.dao.notify.po.SmsTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author menglinjie
 */
@Mapper
public interface SmsTemplateDao {
    int deleteByPrimaryKey(String id);

    int insert(SmsTemplate record);

    SmsTemplate selectByPrimaryKey(String id);

    List<SmsTemplate> selectAll();

    int updateByPrimaryKey(SmsTemplate record);

    List<SmsTemplate> findSmsTemplateList(@Param("startNo") Integer startNo,
                                          @Param("pageSize") Integer pageSize);
}