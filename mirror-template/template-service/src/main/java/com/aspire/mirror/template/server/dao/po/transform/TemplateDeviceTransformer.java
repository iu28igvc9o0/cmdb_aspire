package com.aspire.mirror.template.server.dao.po.transform;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import com.aspire.mirror.template.server.dao.po.TemplateDevice;
import com.aspire.mirror.template.api.dto.model.TemplateDeviceDTO;

import java.util.List;
import java.util.Map;

/**
 * monitor_template_device对象转换类
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao.po.transform
 * 类名称:    TemplateDeviceTransformer.java
 * 类描述:    monitor_template_device对应的PO与DTO的转换类
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
public final class TemplateDeviceTransformer  {

    private TemplateDeviceTransformer(){
    }

   /**
    * 将monitor_template_devicePO实体转换为monitor_template_deviceDTO实体
    *
    * @param  templateDevice monitor_template_devicePO实体
    * @return TemplateDeviceDTO monitor_template_deviceDTO实体
    */
    public static TemplateDeviceDTO fromPo(final TemplateDevice templateDevice) {
        if (null == templateDevice) {
            return null;
        }
        
        TemplateDeviceDTO templateDeviceDTO = new TemplateDeviceDTO();
        templateDeviceDTO.setTemplateDeviceId(templateDevice.getTemplateDeviceId());
        templateDeviceDTO.setTemplateId(templateDevice.getTemplateId());
        templateDeviceDTO.setDeviceId(templateDevice.getDeviceId());

        return templateDeviceDTO;
    }

    /**
     * 将monitor_template_device业务实体对象集合转换为monitor_template_device持久化对象集合
     *
     * @param listTemplateDevice monitor_template_device业务实体对象集合
     * @return List<TemplateDeviceDTO> monitor_template_device持久化对象集合
     */
    public static List<TemplateDeviceDTO> fromPo(final List<TemplateDevice> listTemplateDevice) {
        if (CollectionUtils.isEmpty(listTemplateDevice)) {
            return Lists.newArrayList();
        }
        List<TemplateDeviceDTO> listTemplateDeviceDTO = Lists.newArrayList();

        for (TemplateDevice templateDevice : listTemplateDevice) {
            listTemplateDeviceDTO.add(TemplateDeviceTransformer.fromPo(templateDevice));
        }
        return listTemplateDeviceDTO;
    }

   /**
    * 将monitor_template_deviceDTO实体转换为monitor_template_devicePO实体
    *
    * @param  templateDeviceDTO monitor_template_deviceDTO实体类
    * @return TemplateDevice monitor_template_devicePO实体
    */
    public static TemplateDevice toPo(final TemplateDeviceDTO templateDeviceDTO) {
        if (null == templateDeviceDTO) {
            return null;
        }

        TemplateDevice templateDevice = new TemplateDevice();
        templateDevice.setTemplateDeviceId(templateDeviceDTO.getTemplateDeviceId());
        templateDevice.setTemplateId(templateDeviceDTO.getTemplateId());
        templateDevice.setDeviceId(templateDeviceDTO.getDeviceId());

        return templateDevice;
    }

    /**
     * 将monitor_template_device业务实体对象集合转换为monitor_template_device持久化对象集合
     *
     * @param listTemplateDeviceDTO monitor_template_device业务实体对象集合
     * @return List<TemplateDevice> monitor_template_device持久化对象集合
     */
    public static List<TemplateDevice> toPo(final List<TemplateDeviceDTO> listTemplateDeviceDTO) {
        if (CollectionUtils.isEmpty(listTemplateDeviceDTO)) {
            return Lists.newArrayList();
        }
        List<TemplateDevice> listTemplateDevice = Lists.newArrayList();

        for (TemplateDeviceDTO templateDevicedTO : listTemplateDeviceDTO) {
            listTemplateDevice.add(TemplateDeviceTransformer.toPo(templateDevicedTO));
        }
        return listTemplateDevice;
    }
    /**
     * 将monitor_template_device业务实体对象集合转换为Map
     *
     * @param listTemplateDeviceDTO monitor_template_device业务实体对象集合
     * @return Map<String, TemplateDeviceDTO> Map[key=String|value=TemplateDeviceDTO]
     */
    public static Map<String, TemplateDeviceDTO> toDTOMap(final List<TemplateDeviceDTO> listTemplateDeviceDTO) {
        if (CollectionUtils.isEmpty(listTemplateDeviceDTO)) {
            return Maps.newHashMap();
        }
        Map<String, TemplateDeviceDTO> templateDeviceDTOMaps = Maps.newHashMap();

        for (TemplateDeviceDTO templateDeviceDTO : listTemplateDeviceDTO) {
            templateDeviceDTOMaps.put(templateDeviceDTO.getTemplateDeviceId(), templateDeviceDTO);
        }
        return templateDeviceDTOMaps;
    }

    /**
     * 将monitor_template_device业务实体对象集合中的主键进行提取并封装成数组
     *
     * @param listTemplateDeviceDTO monitor_template_device业务实体对象集合
     * @return String[] 主键数组
     */
    public static String[] toDTOPrimaryKeyArrays(final List<TemplateDeviceDTO> listTemplateDeviceDTO) {
        if (CollectionUtils.isEmpty(listTemplateDeviceDTO)) {
            return null;
        }
        int size = listTemplateDeviceDTO.size();
        String[] templateDeviceIdArrays = new String[size];
        for (int i = 0 ;i< size; i++) {
            templateDeviceIdArrays[i] = listTemplateDeviceDTO.get(i).getTemplateDeviceId();
        }
        return templateDeviceIdArrays;
        }
} 
