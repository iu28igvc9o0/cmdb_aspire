package com.aspire.mirror.template.server.dao.po.transform;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.springframework.util.CollectionUtils;

import com.aspire.mirror.template.server.dao.po.Template;
import com.aspire.mirror.template.api.dto.model.TemplateDTO;

import java.util.List;
import java.util.Map;

/**
 * monitor_template对象转换类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao.po.transform
 * 类名称:    TemplateTransformer.java
 * 类描述:    monitor_template对应的PO与DTO的转换类
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
public final class TemplateTransformer {

    private TemplateTransformer() {
    }

    /**
     * 将monitor_templatePO实体转换为monitor_templateDTO实体
     *
     * @param template monitor_templatePO实体
     * @return TemplateDTO monitor_templateDTO实体
     */
    public static TemplateDTO fromPo(final Template template) {
        if (null == template) {
            return null;
        }

        TemplateDTO templateDTO = new TemplateDTO();
        templateDTO.setTemplateId(template.getTemplateId());
        templateDTO.setName(template.getName());
        templateDTO.setDescription(template.getDescription());
        templateDTO.setCreateTime(template.getCreateTime());
        templateDTO.setType(template.getType());
        templateDTO.setUpdateTime(template.getUpdateTime());
        templateDTO.setFunType(template.getFunType());
        templateDTO.setMonType(template.getMonType());
        templateDTO.setStatus(template.getStatus());
        templateDTO.setSysType(template.getSysType());
        templateDTO.setProxyIdentity(template.getProxyIdentity());
        templateDTO.setZabbixTemplateId(template.getZabbixTemplateId());
        templateDTO.setCreater(template.getCreater());
        templateDTO.setUpdater(template.getUpdater());
        return templateDTO;
    }

    /**
     * 将monitor_template业务实体对象集合转换为monitor_template持久化对象集合
     *
     * @param listTemplate monitor_template业务实体对象集合
     * @return List<TemplateDTO> monitor_template持久化对象集合
     */
    public static List<TemplateDTO> fromPo(final List<Template> listTemplate) {
        if (CollectionUtils.isEmpty(listTemplate)) {
            return Lists.newArrayList();
        }
        List<TemplateDTO> listTemplateDTO = Lists.newArrayList();

        for (Template template : listTemplate) {
            listTemplateDTO.add(TemplateTransformer.fromPo(template));
        }
        return listTemplateDTO;
    }

    /**
     * 将monitor_templateDTO实体转换为monitor_templatePO实体
     *
     * @param templateDTO monitor_templateDTO实体类
     * @return Template monitor_templatePO实体
     */
    public static Template toPo(final TemplateDTO templateDTO) {
        if (null == templateDTO) {
            return null;
        }

        Template template = new Template();
        template.setTemplateId(templateDTO.getTemplateId());
        template.setName(templateDTO.getName());
        template.setDescription(templateDTO.getDescription());
        template.setCreateTime(templateDTO.getCreateTime());
        template.setType(templateDTO.getType());
        template.setUpdateTime(templateDTO.getUpdateTime());
        template.setFunType(templateDTO.getFunType());
        template.setMonType(templateDTO.getMonType());
        template.setStatus(templateDTO.getStatus());
        template.setSysType(templateDTO.getSysType());
        template.setProxyIdentity(templateDTO.getProxyIdentity());
        template.setZabbixTemplateId(templateDTO.getZabbixTemplateId());
        template.setCreater(templateDTO.getCreater());
        template.setUpdater(templateDTO.getUpdater());
        return template;
    }

    /**
     * 将monitor_template业务实体对象集合转换为monitor_template持久化对象集合
     *
     * @param listTemplateDTO monitor_template业务实体对象集合
     * @return List<Template> monitor_template持久化对象集合
     */
    public static List<Template> toPo(final List<TemplateDTO> listTemplateDTO) {
        if (CollectionUtils.isEmpty(listTemplateDTO)) {
            return Lists.newArrayList();
        }
        List<Template> listTemplate = Lists.newArrayList();

        for (TemplateDTO templatedTO : listTemplateDTO) {
            listTemplate.add(TemplateTransformer.toPo(templatedTO));
        }
        return listTemplate;
    }

    /**
     * 将monitor_template业务实体对象集合转换为Map
     *
     * @param listTemplateDTO monitor_template业务实体对象集合
     * @return Map<String ,   TemplateDTO> Map[key=String|value=TemplateDTO]
     */
    public static Map<String, TemplateDTO> toDTOMap(final List<TemplateDTO> listTemplateDTO) {
        if (CollectionUtils.isEmpty(listTemplateDTO)) {
            return Maps.newHashMap();
        }
        Map<String, TemplateDTO> templateDTOMaps = Maps.newHashMap();

        for (TemplateDTO templateDTO : listTemplateDTO) {
            templateDTOMaps.put(templateDTO.getTemplateId(), templateDTO);
        }
        return templateDTOMaps;
    }

    /**
     * 将monitor_template业务实体对象集合中的主键进行提取并封装成数组
     *
     * @param listTemplateDTO monitor_template业务实体对象集合
     * @return String[] 主键数组
     */
    public static String[] toDTOPrimaryKeyArrays(final List<TemplateDTO> listTemplateDTO) {
        if (CollectionUtils.isEmpty(listTemplateDTO)) {
            return null;
        }
        int size = listTemplateDTO.size();
        String[] templateIdArrays = new String[size];
        for (int i = 0; i < size; i++) {
            templateIdArrays[i] = listTemplateDTO.get(i).getTemplateId();
        }
        return templateIdArrays;
    }

//    public static void main(String[] args) {
//        Gson gson = new Gson();
//        System.out.println(gson.toJson("[{\n" +
//                "\t\"zbxA\": {\n" +
//                "\t\t\"staticsType\": 0,  --求和\n" +
//                "\t\t\"field\": [{\n" +
//                "\t\t\t\"field\": \"\",\n" +
//                "\t\t\t\"fieldComparor\": \"\",\n" +
//                "\t\t\t\"fieldValue\": \"\"\n" +
//                "\t\t}],\n" +
//                "\t\t\"date\": [{\n" +
//                "\t\t\t\"field\": \"10\",  --\n" +
//                "\t\t\t\"dateComparor\": \"1\",\n" +
//                "\t\t\t\"dateStaticsType\": \"0\",\n" +
//                "\t\t\t\"dateOptionType\": \"0\",\n" +
//                "\t\t\t\"dateValue\": \"1\",\n" +
//                "\t\t\t\"dateInterval\": \"2\"\n" +
//                "\t\t}]\n" +
//                "\t}\n" +
//                "},\n" +
//                "{\n" +
//                "\t\"zbxB\": {\n" +
//                "\t\t\"staticsType\": 1,\n" +
//                "\t\t\"field\": [{\n" +
//                "\t\t\t\"field\": \"\",\n" +
//                "\t\t\t\"fieldComparor\": \"\",\n" +
//                "\t\t\t\"fieldValue\": \"\"\n" +
//                "\t\t}],\n" +
//                "\t\t\"date\": [{\n" +
//                "\t\t\t\"field\": \"\",\n" +
//                "\t\t\t\"dateComparor\": \"\",\n" +
//                "\t\t\t\"dateStaticsType\": \"\",\n" +
//                "\t\t\t\"dateOptionType\": \"\",\n" +
//                "\t\t\t\"dateValue\": \"\",\n" +
//                "\t\t\t\"dateInterval\": \"\"\n" +
//                "\t\t}]\n" +
//                "\t}\n" +
//                "}]\n"));
//    }
} 
