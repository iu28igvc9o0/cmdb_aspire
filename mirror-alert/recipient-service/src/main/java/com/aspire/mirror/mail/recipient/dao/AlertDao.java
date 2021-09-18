package com.aspire.mirror.mail.recipient.dao;

import com.aspire.mirror.mail.recipient.po.Alert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlertDao {
    void insert(Alert alert);
}
