package com.aspire.mirror.alert.server.biz.mailAlert;

import com.aspire.mirror.alert.server.dao.mailAlert.po.AlertMailRecipient;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;

import java.util.List;

/**
 * 告警邮件接入
 * <p>
 * 项目名称:  mirror平台
 * 包:        om.aspire.mirror.alert.server.biz
 * 类名称:    AlertsHisBiz.java
 * 类描述:    告警业务层接口
 * 创建人:    LiangJun
 * 创建时间:  2019/5/22 11:02
 * 版本:      v1.0
 */
public interface AlertsMailResolveBiz {

    void insertRecipient(AlertMailRecipient recipient);

    List<AlertMailRecipient> selectRecipientByAccount(String account);

    AlertMailRecipient selectRecipientById(Integer id);

    void updateRecipientById(AlertMailRecipient recipient);

    void removeRecipientById(Integer id);

    PageResponse<AlertMailRecipient> selectRecipients(PageRequest pageRequest);
}
