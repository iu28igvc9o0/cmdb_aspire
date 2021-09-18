package com.aspire.mirror.alert.server.biz.alertStandard;

import com.aspire.mirror.alert.server.dao.alertStandard.po.AlertStandard;
import com.aspire.mirror.common.entity.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @projectName: AlertStandardBiz
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-06-10 15:43
 **/
public interface AlertStandardBiz {

    void insert(AlertStandard as);
    void update(AlertStandard as);
    void deleteByIds(String operator,String[] ids);
    PageResponse<AlertStandard> listWithPage(Map<String,Object> mp);
    void operatorStatus(String operator,String[] ids);
    void updateAlertHistory(String operator,String start, String end);
    void updateAlertHistoryOneRow(String operator,String id,String start,String end);
    void uploadAS(MultipartFile file,String operator);
    List<Map<String,Object>> exportAll(Map<String,Object> mp);
}
