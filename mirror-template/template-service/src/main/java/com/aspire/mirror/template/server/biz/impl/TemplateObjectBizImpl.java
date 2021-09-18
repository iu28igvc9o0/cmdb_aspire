package com.aspire.mirror.template.server.biz.impl;

import java.util.List;
import java.util.UUID;

import com.aspire.mirror.template.api.dto.model.TemplateObjectDTO;
import com.aspire.mirror.template.server.dao.TemplateObjectDao;
import com.aspire.mirror.template.server.biz.TemplateObjectBiz;
import com.aspire.mirror.template.server.dao.po.TemplateObject;
import com.aspire.mirror.template.server.dao.po.transform.TemplateObjectTransformer;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * monitor_template_device业务层实现类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.template.server.biz.impl
 * 类名称:    TemplateDeviceBizImpl.java
 * 类描述:    monitor_template_device业务逻辑层实现类
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Service
public class TemplateObjectBizImpl implements TemplateObjectBiz {

    /**
     * 批量新增monitor_template_device数据
     *
     * @param listTemplateObjectDTO 模板关联对象集合对象
     * @return int 新增数据条数
     */
    public int insertByBatch(final List<TemplateObjectDTO> listTemplateObjectDTO){
        if (CollectionUtils.isEmpty(listTemplateObjectDTO)) {
            LOGGER.error("method[insertByBatch] param[listTemplateObjectDTO] is null");
            throw new RuntimeException("param[listTemplateObjectDTO] is null");
        }

        List<TemplateObject> listTemplateDevice = TemplateObjectTransformer.toPo(listTemplateObjectDTO);
        for (TemplateObject templateObject : listTemplateDevice) {
            templateObject.setTemplateObjectId(UUID.randomUUID().toString());
        }
        return templateObjectDao.insertByBatch(listTemplateDevice);
    }
    /**
     *
     * @param templateId 模板Id
     * @return
     */
    @Override
    public List<TemplateObjectDTO> selectByTemplateId(String templateId) {
        TemplateObject param = new TemplateObject();
        param.setTemplateId(templateId);
        List<TemplateObject> templateObjectList = templateObjectDao.select(param);
        return TemplateObjectTransformer.fromPo(templateObjectList);
    }

    @Override
    public int deleteByTemplateIds(String templateIds) {
        if (StringUtils.isEmpty(templateIds)) {
            return 0;
        }
        String[] templateIdArrays = templateIds.split(",");
        return templateObjectDao.deleteByTemplateIds(templateIdArrays);
    }

    /** slf4j*/
    private static final Logger   LOGGER = LoggerFactory.getLogger(TemplateObjectBizImpl.class);

    /** 数据访问层接口*/
    @Autowired
    private TemplateObjectDao templateObjectDao;

} 
