package com.aspire.mirror.template.server.biz;

import com.aspire.mirror.template.api.dto.model.TemplateDataSyncDTO;
import com.aspire.mirror.template.server.dao.po.TemplateDataSync;

import java.util.List;

/**
 * 模板数据同步
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.template.server.biz
 * 类名称:    TemplateDataSyncBiz.java
 * 类描述:    模板数据同步
 * 创建人:    JinSu
 * 创建时间:  2018/9/11 9:28
 * 版本:      v1.0
 */
public interface TemplateDataSyncBiz {
    /**
     * 插入模板数据同步信息
     * @param templateDataSyncDTO 模板数据同步DTO
     */
    void insert(TemplateDataSyncDTO templateDataSyncDTO);
    /**
     * 查询模板同步数据
     */
    List<TemplateDataSyncDTO> selectByStartSyncIdAndSyncType(int syncId, String syncType);
}
