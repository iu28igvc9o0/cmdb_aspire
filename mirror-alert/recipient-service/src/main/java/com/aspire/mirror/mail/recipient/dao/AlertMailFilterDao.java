package com.aspire.mirror.mail.recipient.dao;

import com.aspire.mirror.mail.recipient.po.AlertMailFilter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertMailFilterDao {
    List<AlertMailFilter> selectFilterByReceiver(@Param(value = "receiver") String receiver);
}
