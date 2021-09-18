package com.aspire.mirror.alert.server.dao.notify;

import com.aspire.mirror.alert.server.dao.notify.po.AlertNotifyTemplate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.dao
 * @Author: baiwenping
 * @CreateTime: 2020-03-05 14:24
 * @Description: ${Description}
 */
@Mapper
public interface AlertNotifyTemplateMapper {

    /**
     * 根据名称查询通知模板
     * @param templateName
     * @return
     */
    List<AlertNotifyTemplate> selectByName (String templateName);

    /**
     * 新增模板
     * @param alertNotifyTemplate
     */
    void insert(AlertNotifyTemplate alertNotifyTemplate);

    /**
     * 修改模板
     * @param alertNotifyTemplate
     */
    void update(AlertNotifyTemplate alertNotifyTemplate);

    /**
     * 删除模板
     * @param templateName
     */
    void deleteByName (String templateName);

}
