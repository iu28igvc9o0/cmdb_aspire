package com.aspire.mirror.template.server.dao.po.transform;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import com.aspire.mirror.template.server.dao.po.ApiServerConfig;
import com.aspire.mirror.template.api.dto.model.ApiServerConfigDTO;

import java.util.List;
import java.util.Map;

/**
 * monitor_api_server_config对象转换类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao.po.transform
 * 类名称:    ApiServerConfigTransformer.java
 * 类描述:    monitor_api_server_config对应的PO与DTO的转换类
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
public final class ApiServerConfigTransformer {

    private ApiServerConfigTransformer() {
    }

    /**
     * 将monitor_api_server_configPO实体转换为monitor_api_server_configDTO实体
     *
     * @param apiServerConfig monitor_api_server_configPO实体
     * @return ApiServerConfigDTO monitor_api_server_configDTO实体
     */
    public static ApiServerConfigDTO fromPo(final ApiServerConfig apiServerConfig) {
        if (null == apiServerConfig) {
            return null;
        }

        ApiServerConfigDTO apiServerConfigDTO = new ApiServerConfigDTO();
        apiServerConfigDTO.setApiServerId(apiServerConfig.getApiServerId());
        apiServerConfigDTO.setUrl(apiServerConfig.getUrl());
        apiServerConfigDTO.setUsername(apiServerConfig.getUsername());
        apiServerConfigDTO.setPassword(apiServerConfig.getPassword());
        apiServerConfigDTO.setRoom(apiServerConfig.getRoom());
        apiServerConfigDTO.setServerType(apiServerConfig.getServerType());
        return apiServerConfigDTO;
    }

    /**
     * 将monitor_api_server_config业务实体对象集合转换为monitor_api_server_config持久化对象集合
     *
     * @param listApiServerConfig monitor_api_server_config业务实体对象集合
     * @return List<ApiServerConfigDTO> monitor_api_server_config持久化对象集合
     */
    public static List<ApiServerConfigDTO> fromPo(final List<ApiServerConfig> listApiServerConfig) {
        if (CollectionUtils.isEmpty(listApiServerConfig)) {
            return Lists.newArrayList();
        }
        List<ApiServerConfigDTO> listApiServerConfigDTO = Lists.newArrayList();

        for (ApiServerConfig apiServerConfig : listApiServerConfig) {
            listApiServerConfigDTO.add(ApiServerConfigTransformer.fromPo(apiServerConfig));
        }
        return listApiServerConfigDTO;
    }

    /**
     * 将monitor_api_server_configDTO实体转换为monitor_api_server_configPO实体
     *
     * @param apiServerConfigDTO monitor_api_server_configDTO实体类
     * @return ApiServerConfig monitor_api_server_configPO实体
     */
    public static ApiServerConfig toPo(final ApiServerConfigDTO apiServerConfigDTO) {
        if (null == apiServerConfigDTO) {
            return null;
        }

        ApiServerConfig apiServerConfig = new ApiServerConfig();
        apiServerConfig.setApiServerId(apiServerConfigDTO.getApiServerId());
        apiServerConfig.setUrl(apiServerConfigDTO.getUrl());
        apiServerConfig.setUsername(apiServerConfigDTO.getUsername());
        apiServerConfig.setPassword(apiServerConfigDTO.getPassword());
        apiServerConfig.setRoom(apiServerConfigDTO.getRoom());

        return apiServerConfig;
    }

    /**
     * 将monitor_api_server_config业务实体对象集合转换为monitor_api_server_config持久化对象集合
     *
     * @param listApiServerConfigDTO monitor_api_server_config业务实体对象集合
     * @return List<ApiServerConfig> monitor_api_server_config持久化对象集合
     */
    public static List<ApiServerConfig> toPo(final List<ApiServerConfigDTO> listApiServerConfigDTO) {
        if (CollectionUtils.isEmpty(listApiServerConfigDTO)) {
            return Lists.newArrayList();
        }
        List<ApiServerConfig> listApiServerConfig = Lists.newArrayList();

        for (ApiServerConfigDTO apiServerConfigdTO : listApiServerConfigDTO) {
            listApiServerConfig.add(ApiServerConfigTransformer.toPo(apiServerConfigdTO));
        }
        return listApiServerConfig;
    }

    /**
     * 将monitor_api_server_config业务实体对象集合转换为Map
     *
     * @param listApiServerConfigDTO monitor_api_server_config业务实体对象集合
     * @return Map<String ,   ApiServerConfigDTO> Map[key=String|value=ApiServerConfigDTO]
     */
    public static Map<String, ApiServerConfigDTO> toDTOMap(final List<ApiServerConfigDTO> listApiServerConfigDTO) {
        if (CollectionUtils.isEmpty(listApiServerConfigDTO)) {
            return Maps.newHashMap();
        }
        Map<String, ApiServerConfigDTO> apiServerConfigDTOMaps = Maps.newHashMap();

        for (ApiServerConfigDTO apiServerConfigDTO : listApiServerConfigDTO) {
            apiServerConfigDTOMaps.put(apiServerConfigDTO.getApiServerId(), apiServerConfigDTO);
        }
        return apiServerConfigDTOMaps;
    }

    /**
     * 将monitor_api_server_config业务实体对象集合中的主键进行提取并封装成数组
     *
     * @param listApiServerConfigDTO monitor_api_server_config业务实体对象集合
     * @return String[] 主键数组
     */
    public static String[] toDTOPrimaryKeyArrays(final List<ApiServerConfigDTO> listApiServerConfigDTO) {
        if (CollectionUtils.isEmpty(listApiServerConfigDTO)) {
            return null;
        }
        int size = listApiServerConfigDTO.size();
        String[] apiServerIdArrays = new String[size];
        for (int i = 0; i < size; i++) {
            apiServerIdArrays[i] = listApiServerConfigDTO.get(i).getApiServerId();
        }
        return apiServerIdArrays;
    }
} 
