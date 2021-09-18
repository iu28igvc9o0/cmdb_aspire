package com.aspire.mirror.alert.server.dao.notify;
import com.aspire.mirror.alert.server.dao.alert.po.Alerts;
import com.aspire.mirror.alert.server.dao.notify.po.AlertSubscribeRules;
import com.aspire.mirror.alert.server.dao.notify.po.AlertSubscribeRulesDetail;
import com.aspire.mirror.alert.server.dao.notify.po.AlertSubscribeRulesManagement;
import com.aspire.mirror.alert.server.dao.notify.po.CreateAlertSubscribeRules;
import com.aspire.mirror.alert.server.dao.notify.po.ExportAlertSubscribeRulesManagement;
import com.aspire.mirror.alert.server.dao.notify.po.Reciver;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface AlertSubscribeRulesDao {

    AlertSubscribeRules getSubscribeRules(@Param("id")String id);


    List<AlertSubscribeRules> query(AlertSubscribeRules alertSubscribeRules);

    Integer findPageWithCount(AlertSubscribeRules alertSubscribeRules);


    List<AlertSubscribeRules> queryRules(AlertSubscribeRules alertSubscribeRules);

    Integer findRulesWithCount(AlertSubscribeRules alertSubscribeRules);

     void updateRules(@Param("idlist") List<String> idlist, @Param("isOpen") String isOpen);

     void deteleRules(@Param("idlist")List<String> idlist);

     List<ExportAlertSubscribeRulesManagement> export(@Param("idlist")List<String> idlist);

    List<AlertSubscribeRulesManagement> querySubscribeRules();

    void CreateSubscribeRules(CreateAlertSubscribeRules createAlertSubscribeRules);

    List<Alerts> selectByIds(List<String> asList);

    void insert(AlertSubscribeRulesManagement alertSubscribeRulesManagement);

    void insertRevicer(Reciver reciver);

    void deleteReciver(List<String> uuidList);

    void deleteAlertSubscribeRules(List<String> uuidList);

    void deleteAlertSubscribeRulesManagement(List<String> uuidList);

    void deleteAlertSubscribeRulesManagementByAlertId(@Param("id")String id);

    List<AlertSubscribeRulesDetail> getAlertSubscribeRulesNotifyConfig(@Param(value = "params")Map<String, String> params);

    List<Reciver> getRevicerInformationByAlertSubscribe_rulesId(@Param("id") String id);

    Map<String, Object> detailById(String alertId);

    List<Map<String, Object>> selectBySubscribeRulesIds(List<String> alertIdList);
    AlertSubscribeRulesDetail GetSubscribeRulesById(@Param("id") String id);
    List<Reciver>GetSubscribeRulesRevicerById(@Param("id") String id);

    List<Map<String, Object>> getReSendAlerts(@Param("id")String id,@Param("recurrenceCount")Integer recurrenceCount);

    void updateAlertSubscribeules(Map<String, Object> map);

    List<AlertSubscribeRulesManagement> querySubscribeRulesManagementById(@Param("id")String id);

    List<Map<String, Object>> querySubscribeRulesManagement(Map<String, String> params);

    List<Map<String, Object>>querySubscribeRulesManagementHis(Map<String, String> params);

    void deleteSubscribeRulesManagement(List<String> uuidList);

    AlertSubscribeRulesManagement selectAlertSubscribeRulesManagementBysubscribeRules(@Param("subscribeRules")String subscribeRules);

    void update(AlertSubscribeRulesManagement alertSubscribeRulesManagement);

    void updateRevicer(Reciver reciver);

    void updateSubscribeRules(CreateAlertSubscribeRules createAlertSubscribeRules1);


    AlertSubscribeRulesManagement querySubscribeRulesManagementByAlertId(@Param("id")String id,@Param("alertSubscribeRulesId")String subscribeRules);

    List<AlertSubscribeRules> queryAlertSubscribeRules();

}
