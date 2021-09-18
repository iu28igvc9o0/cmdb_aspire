package com.aspire.mirror.template.server.biz.impl;

import com.aspire.mirror.template.api.dto.model.TemplateDataSyncDTO;
import com.aspire.mirror.template.server.biz.TemplateDataSyncBiz;
import com.aspire.mirror.template.server.dao.TemplateDataSyncDao;
import com.aspire.mirror.template.server.dao.po.TemplateDataSync;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 模板数据同步实现
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.template.server.biz.impl
 * 类名称:    TemplateDataSyncBizImpl.java
 * 类描述:    模板数据同步实现
 * 创建人:    JinSu
 * 创建时间:  2018/9/11 10:57
 * 版本:      v1.0
 */
@Service
public class TemplateDataSyncBizImpl implements TemplateDataSyncBiz {
    @Override
    public void insert(TemplateDataSyncDTO templateDataSyncDTO) {
        TemplateDataSync templateDataSync = new TemplateDataSync();
        BeanUtils.copyProperties(templateDataSyncDTO, templateDataSync);
        templateDataSyncDao.insert(templateDataSync);
    }

    @Override
    public List<TemplateDataSyncDTO> selectByStartSyncIdAndSyncType(int syncId, String syncType) {
        List<TemplateDataSync> templateDataSyncList = templateDataSyncDao.selectByStartSyncIdAndSyncType(syncId,syncType);
        List<TemplateDataSyncDTO> templateDataSyncDTOList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(templateDataSyncList)) {
            for (TemplateDataSync templateDataSync : templateDataSyncList) {
                TemplateDataSyncDTO templateDataSyncDTO = new TemplateDataSyncDTO();
                BeanUtils.copyProperties(templateDataSync, templateDataSyncDTO);
                templateDataSyncDTOList.add(templateDataSyncDTO);
            }
        }
        return templateDataSyncDTOList;
    }

    @Autowired
    private TemplateDataSyncDao templateDataSyncDao;
}
