package com.aspire.mirror.template.server.dao.po.transform;

import com.aspire.mirror.template.api.dto.model.TemplateObjectDTO;
import com.aspire.mirror.template.server.dao.po.TemplateObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 模板关联对象对象转换类
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao.po.transform
 * 类名称:    TemplateObjectTransformer.java
 * 类描述:    模板关联对象对应的PO与DTO的转换类
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
public final class TemplateObjectTransformer {

    private TemplateObjectTransformer(){
    }

   /**
    * 将模板关联对象PO实体转换为模板关联对象DTO实体
    *
    * @param  templateObject 模板关联对象PO实体
    * @return TemplateObjectDTO 模板关联对象DTO实体
    */
    public static TemplateObjectDTO fromPo(final TemplateObject templateObject) {
        if (null == templateObject) {
            return null;
        }
        
        TemplateObjectDTO templateObjectDTO = new TemplateObjectDTO();
        templateObjectDTO.setTemplateObjectId(templateObject.getTemplateObjectId());
        templateObjectDTO.setTemplateId(templateObject.getTemplateId());
        templateObjectDTO.setObjectId(templateObject.getObjectId());
        templateObjectDTO.setObjectType(templateObject.getObjectType());
        return templateObjectDTO;
    }

    /**
     * 将模板关联对象业务实体对象集合转换为模板关联对象持久化对象集合
     *
     * @param listTemplateDevice 模板关联对象业务实体对象集合
     * @return List<TemplateObjectDTO> 模板关联对象持久化对象集合
     */
    public static List<TemplateObjectDTO> fromPo(final List<TemplateObject> listTemplateDevice) {
        if (CollectionUtils.isEmpty(listTemplateDevice)) {
            return Lists.newArrayList();
        }
        List<TemplateObjectDTO> listTemplateDeviceDTO = Lists.newArrayList();

        for (TemplateObject templateDevice : listTemplateDevice) {
            listTemplateDeviceDTO.add(TemplateObjectTransformer.fromPo(templateDevice));
        }
        return listTemplateDeviceDTO;
    }

   /**
    * 将模板关联对象DTO实体转换为模板关联对象PO实体
    *
    * @param  templateObjectDTO 模板关联对象DTO实体类
    * @return TemplateObject 模板关联对象PO实体
    */
    public static TemplateObject toPo(final TemplateObjectDTO templateObjectDTO) {
        if (null == templateObjectDTO) {
            return null;
        }

        TemplateObject templateObject = new TemplateObject();
        templateObject.setTemplateObjectId(templateObjectDTO.getTemplateObjectId());
        templateObject.setObjectId(templateObjectDTO.getObjectId());
        templateObject.setObjectType(templateObjectDTO.getObjectType());
        templateObject.setTemplateId(templateObjectDTO.getTemplateId());
        return templateObject;
    }

    /**
     * 将模板关联对象业务实体对象集合转换为模板关联对象持久化对象集合
     *
     * @param listTemplateDeviceDTO 模板关联对象业务实体对象集合
     * @return List<TemplateObject> 模板关联对象持久化对象集合
     */
    public static List<TemplateObject> toPo(final List<TemplateObjectDTO> listTemplateDeviceDTO) {
        if (CollectionUtils.isEmpty(listTemplateDeviceDTO)) {
            return Lists.newArrayList();
        }
        List<TemplateObject> listTemplateDevice = Lists.newArrayList();

        for (TemplateObjectDTO templateDevicedTO : listTemplateDeviceDTO) {
            listTemplateDevice.add(TemplateObjectTransformer.toPo(templateDevicedTO));
        }
        return listTemplateDevice;
    }
} 
