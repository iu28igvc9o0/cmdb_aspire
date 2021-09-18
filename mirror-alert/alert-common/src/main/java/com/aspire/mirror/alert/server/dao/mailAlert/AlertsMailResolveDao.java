package com.aspire.mirror.alert.server.dao.mailAlert;

import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailRecipient;
import com.aspire.mirror.common.entity.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertsMailResolveDao {

    AlertMailRecipient selectRecipientById(@Param(value = "id") Integer id);

    List<AlertMailRecipient> selectActiveRecipients();

    List<AlertMailRecipient> selectRecipientByAccount(@Param(value = "recipient") String recipient);

    void insertRecipient(AlertMailRecipient recipient);

    void updateRecipient(AlertMailRecipient recipient);

    List<AlertMailRecipient> recipientsPageList(Page page);

    Integer recipientsCount(Page page);

    void deleteRecipient(@Param(value = "id") Integer id);
}
