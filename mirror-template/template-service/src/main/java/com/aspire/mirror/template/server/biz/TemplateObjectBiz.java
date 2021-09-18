package com.aspire.mirror.template.server.biz;

import java.util.List;

import com.aspire.mirror.template.api.dto.model.TemplateObjectDTO;

/**
 * 模板关联对象业务层接口
 *
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.server.biz
 * 类名称:   TemplateObjectBiz.java
 * 类描述:   模板关联对象业务逻辑层接口
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 12:14:48
 */
public interface TemplateObjectBiz {

    /**
     * 批量新增monitor_template_device数据
     *
     * @param listTemplateDeviceDTO monitor_template_deviceDTO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<TemplateObjectDTO> listTemplateDeviceDTO);
    /**


    /**
     * 根据模板ID查询列表
     *
     * @param templateId 模板Id
     * @return
     */
    List<TemplateObjectDTO> selectByTemplateId(String templateId);

    /**
     * 根据模板删除
     *
     * @param templateIds 模板ID
     * @return
     */
    int deleteByTemplateIds(String templateIds);
} 
