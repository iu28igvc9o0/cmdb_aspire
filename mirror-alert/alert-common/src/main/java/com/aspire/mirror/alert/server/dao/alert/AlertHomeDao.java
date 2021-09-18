package com.aspire.mirror.alert.server.dao.alert;

import com.aspire.mirror.alert.server.dao.alert.po.Alerts;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsHis;
import com.aspire.mirror.common.entity.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlertHomeDao {

    List<Alerts> getHomeActivityData(Page page);

    List<Alerts> getHomeConfirmData(Page page);

    List<AlertsHis> getHomeAlertHisData(Page page);

    List<String> getActiveAlertSourceList();
    
    List<Alerts> getHomeActivityViewCount(Page page);

    List<Alerts> getHomeConfirmViewCount(Page page);

    List<AlertsHis> exportHis(Page page);
}
