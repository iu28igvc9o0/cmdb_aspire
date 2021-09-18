package com.aspire.mirror.alert.server.dao.notify;

import com.aspire.mirror.alert.server.dao.notify.po.AlertNotifyConfigLogDetail;
import com.aspire.mirror.alert.server.dao.notify.po.AlertVoiceNotifyDetail;
import com.aspire.mirror.alert.server.dao.notify.po.AlertVoiceNotifyReqDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertVoiceNotifyDao {

    void createdAlertVoiceNotify(AlertVoiceNotifyReqDTO request);

    AlertVoiceNotifyDetail getAlertVoiceNotify(@Param("creator") String creator);

    void deleteVoiceNotifyByCreator(@Param("creator") String creator);

    void updateVoiceAlertId(@Param("creator") String creator,
                            @Param("voiceAlertId") String voiceAlertId);

    void insertAlertNotifyConfigLog(List<AlertNotifyConfigLogDetail> request);

    List<String> getAlertNotifyConfigLog(@Param("id") String id);

}
