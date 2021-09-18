package com.migu.tsg.microservice.atomicservice.rbac.dao.po.transform;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ModuleCustomized;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ModuleCustomizedDTO;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 对象转换类
 * <p>
 * 项目名称:  mirror平台
 * 包:     com.aspire.mirror.configManagement.entity   
 * 类名称:     ModuleCustomized
 * 类描述:    对应的PO与DTO的转换类
 * 创建人:     曾祥华
 * 创建时间:     2019-07-17 14:04:59
 */
public final class ModuleCustomizedTransformer {
	private ModuleCustomizedTransformer() {
    }
    
    /**
     * 将PO实体转换为动作DTO实体
     *
     * @param moduleCustomized 动作PO实体
     * @return ModuleCustomizedDTO 动作DTO实体
     */
    public static ModuleCustomizedDTO fromPo(final ModuleCustomized moduleCustomized) {
        if (null == moduleCustomized) {
            return null;
        }

        ModuleCustomizedDTO moduleCustomizedDTO = new ModuleCustomizedDTO();
        
			moduleCustomizedDTO.setUserId(moduleCustomized.getUserId());
			moduleCustomizedDTO.setModuleId(moduleCustomized.getModuleId());
			moduleCustomizedDTO.setContent(moduleCustomized.getContent());

        return moduleCustomizedDTO;
    }

    /**
     * 将业务实体对象集合转换为动作持久化对象集合
     *
     * @param listModuleCustomized 业务实体对象集合
     * @return List<ModuleCustomizedDTO> 持久化对象集合
     */
    public static List<ModuleCustomizedDTO> fromPo(final List<ModuleCustomized> listModuleCustomized) {
        if (CollectionUtils.isEmpty(listModuleCustomized)) {
            return Lists.newArrayList();
        }
        List<ModuleCustomizedDTO> listModuleCustomizedDTO = Lists.newArrayList();

        for (ModuleCustomized moduleCustomized : listModuleCustomized) {
            listModuleCustomizedDTO.add(ModuleCustomizedTransformer.fromPo(moduleCustomized));
        }
        return listModuleCustomizedDTO;
    }

    /**
     * 将DTO实体转换为动作PO实体
     *
     * @param moduleCustomizedDTO DTO实体类
     * @return ModuleCustomized PO实体
     */
    public static ModuleCustomized toPo(final ModuleCustomizedDTO moduleCustomizedDTO) {
        if (null == moduleCustomizedDTO) {
            return null;
        }

        ModuleCustomized moduleCustomized = new ModuleCustomized();
        
			moduleCustomized.setUserId(moduleCustomizedDTO.getUserId());
			moduleCustomized.setModuleId(moduleCustomizedDTO.getModuleId());
			moduleCustomized.setContent(moduleCustomizedDTO.getContent());
        
        return moduleCustomized;
    }

    /**
     * 将业务实体对象集合转换为动作持久化对象集合
     *
     * @param listModuleCustomizedDTO 业务实体对象集合
     * @return List<ModuleCustomized> 持久化对象集合
     */
    public static List<ModuleCustomized> toPo(final List<ModuleCustomizedDTO> listModuleCustomizedDTO) {
        if (CollectionUtils.isEmpty(listModuleCustomizedDTO)) {
            return Lists.newArrayList();
        }
        List<ModuleCustomized> listModuleCustomized = Lists.newArrayList();

        for (ModuleCustomizedDTO moduleCustomizeddTO : listModuleCustomizedDTO) {
            listModuleCustomized.add(ModuleCustomizedTransformer.toPo(moduleCustomizeddTO));
        }
        return listModuleCustomized;
    }

    /**
     * 将业务实体对象集合转换为Map
     *
     * @param listModuleCustomizedDTO 业务实体对象集合
     * @return Map<String,ModuleCustomizedDTO> Map[key=String|value=ModuleCustomizedDTO]
     */
    public static Map<String, ModuleCustomizedDTO> toDTOMap(final List<ModuleCustomizedDTO> listModuleCustomizedDTO) {
        if (CollectionUtils.isEmpty(listModuleCustomizedDTO)) {
            return Maps.newHashMap();
        }
        Map<String, ModuleCustomizedDTO> moduleCustomizedDTOMaps = Maps.newHashMap();

        for (ModuleCustomizedDTO moduleCustomizedDTO : listModuleCustomizedDTO) {
            moduleCustomizedDTOMaps.put(moduleCustomizedDTO.getUserId(), moduleCustomizedDTO);
        }
        return moduleCustomizedDTOMaps;
    }

    /**
     * 将业务实体对象集合中的主键进行提取并封装成数组
     *
     * @param listModuleCustomizedDTO 业务实体对象集合
     * @return String[] 主键数组
     */
    public static String[] toDTOPrimaryKeyArrays(final List<ModuleCustomizedDTO> listModuleCustomizedDTO) {
        if (CollectionUtils.isEmpty(listModuleCustomizedDTO)) {
            return null;
        }
        int size = listModuleCustomizedDTO.size();
        String[] moduleCustomizedIdArrays = new String[size];
        for (int i = 0; i < size; i++) {
            moduleCustomizedIdArrays[i] = listModuleCustomizedDTO.get(i).getUserId();
        }
        return moduleCustomizedIdArrays;
    }

}
