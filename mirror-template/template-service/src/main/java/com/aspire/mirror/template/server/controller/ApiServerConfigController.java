package com.aspire.mirror.template.server.controller;

import com.aspire.mirror.template.api.dto.ApiServerConfigCreateRequest;
import com.aspire.mirror.template.api.dto.ApiServerConfigCreateResponse;
import com.aspire.mirror.template.api.dto.ApiServerConfigDetailResponse;
import com.aspire.mirror.template.api.dto.ApiServerConfigUpdateRequest;
import com.aspire.mirror.template.api.dto.ApiServerConfigUpdateResponse;
import com.aspire.mirror.template.api.dto.model.ApiServerConfigDTO;
import com.aspire.mirror.template.api.dto.vo.ApiServerConfigVO;
import com.aspire.mirror.template.api.service.ApiServerConfigService;
import com.aspire.mirror.template.server.biz.ApiServerConfigBiz;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * monitor_api_server_config控制层实现类
 * <p>
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.server.controller
 * 类名称:   ApiServerConfigController.java
 * 类描述:   monitor_api_server_config业务逻辑层实现类
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 12:14:48
 */
@RestController
@CacheConfig(cacheNames = "ApiServerConfigCache")
public class ApiServerConfigController implements ApiServerConfigService {

    /**
     * 创建monitor_api_server_config信息
     *
     * @param apiServerConfigCreateRequest monitor_api_server_config创建请求对象
     * @return ApiServerConfigCreateResponse monitor_api_server_config创建响应对象
     */
    public ApiServerConfigCreateResponse createdApiServerConfig(@RequestBody final ApiServerConfigCreateRequest
                                                                        apiServerConfigCreateRequest) {
        if (null == apiServerConfigCreateRequest) {
            LOGGER.error("created param apiServerConfigCreateRequest is null");
            throw new RuntimeException("apiServerConfigCreateRequest is null");
        }
        ApiServerConfigDTO apiServerConfigDTO = new ApiServerConfigDTO();
        BeanUtils.copyProperties(apiServerConfigCreateRequest, apiServerConfigDTO);
        apiServerConfigBiz.insert(apiServerConfigDTO);
        ApiServerConfigCreateResponse apiServerConfigCreateResponse = new ApiServerConfigCreateResponse();
        BeanUtils.copyProperties(apiServerConfigDTO, apiServerConfigCreateResponse);
        return apiServerConfigCreateResponse;
    }

    /**
     * 根据主键删除单条monitor_api_server_config信息
     *
     * @param apiServerId 主键
     * @@return Result 返回结果
     */
    public ResponseEntity<String> deleteByPrimaryKey(@PathVariable("api_server_id") final String apiServerId) {
        try {
            apiServerConfigBiz.deleteByPrimaryKey(apiServerId);
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOGGER.error("deleteByProjectId error:" + e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据主键删除多条monitor_api_server_config信息
     *
     * @param apiServerIds 主键（以逗号分隔）
     * @@return Result 返回结果
     */
    public ResponseEntity<String> deleteByPrimaryKeyArrays(@PathVariable("api_server_ids") final String apiServerIds) {
        try {
            if (StringUtils.isEmpty(apiServerIds)) {
                throw new RuntimeException("apiServerIds is null");
            }
            String[] apiServerIdArrays = apiServerIds.split(",");
            apiServerConfigBiz.deleteByPrimaryKeyArrays(apiServerIdArrays);
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOGGER.error("deleteByPrimaryKeyArrays error:" + e.getMessage(), e);
            return new ResponseEntity<String>("删除错误！", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据主键修改monitor_api_server_config信息
     *
     * @param apiServerConfigUpdateRequest monitor_api_server_config修改请求对象
     * @return ApiServerConfigUpdateResponse monitor_api_server_config修改响应对象
     */
    public ApiServerConfigUpdateResponse modifyByPrimaryKey(@PathVariable("api_server_id") final String apiServerId,
                                                            @RequestBody final ApiServerConfigUpdateRequest
                                                                    apiServerConfigUpdateRequest) {
        ApiServerConfigDTO apiServerConfigdTO = new ApiServerConfigDTO();
        apiServerConfigdTO.setApiServerId(apiServerId);
        BeanUtils.copyProperties(apiServerConfigUpdateRequest, apiServerConfigdTO);

        apiServerConfigBiz.updateByPrimaryKey(apiServerConfigdTO);
        ApiServerConfigDTO findApiServerConfigDTO = apiServerConfigBiz.selectByPrimaryKey(apiServerId);
        ApiServerConfigUpdateResponse apiServerConfigUpdateResponse = new ApiServerConfigUpdateResponse();
        BeanUtils.copyProperties(apiServerConfigUpdateResponse, findApiServerConfigDTO);
        return apiServerConfigUpdateResponse;
    }

    /**
     * 根据主键查找monitor_api_server_config详情信息
     *
     * @param apiServerId monitor_api_server_config主键
     * @return ApiServerConfigVO monitor_api_server_config详情响应对象
     */
    public ApiServerConfigVO findByPrimaryKey(@PathVariable("api_server_id") final String apiServerId) {
        if (StringUtils.isEmpty(apiServerId)) {
            LOGGER.warn("findByPrimaryKey param apiServerId is null");
            return null;
        }
        ApiServerConfigDTO apiServerConfigDTO = apiServerConfigBiz.selectByPrimaryKey(apiServerId);
        ApiServerConfigVO apiServerConfigVO = new ApiServerConfigVO();
        if (null != apiServerConfigDTO) {
            BeanUtils.copyProperties(apiServerConfigDTO, apiServerConfigVO);
        }

        return apiServerConfigVO;
    }

    /**
     * 根据主键查询monitor_api_server_config集合信息
     *
     * @param apiServerIds monitor_api_server_config主键
     * @return ApiServerConfigVO monitor_api_server_config查询响应对象
     */
    public List<ApiServerConfigVO> listByPrimaryKeyArrays(@PathVariable("api_server_id") final String apiServerIds) {
        if (StringUtils.isEmpty(apiServerIds)) {
            LOGGER.error("listByPrimaryKeyArrays param apiServerIds is null");
            return Lists.newArrayList();
        }
        String[] apiServerIdArrays = apiServerIds.split(",");
        List<ApiServerConfigDTO> listApiServerConfigDTO = apiServerConfigBiz.selectByPrimaryKeyArrays
                (apiServerIdArrays);
        List<ApiServerConfigVO> listApiServerConfigVO = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(listApiServerConfigDTO)) {
            for (ApiServerConfigDTO apiServerConfigDTO : listApiServerConfigDTO) {
                ApiServerConfigVO apiServerConfigVO = new ApiServerConfigVO();
                BeanUtils.copyProperties(apiServerConfigDTO, apiServerConfigVO);
                listApiServerConfigVO.add(apiServerConfigVO);
            }
        }
        return listApiServerConfigVO;
    }

    @Override
    public List<ApiServerConfigVO> listApiSvrConfigsByRoomIds(@PathVariable("room_ids") String roomIds) {
        if (StringUtils.isEmpty(roomIds)) {
            LOGGER.error("listApiSvrConfigsByRoomIds param roomIds is null");
            return Lists.newArrayList();
        }
        String[] roomIdArr = roomIds.split(",");
        List<ApiServerConfigDTO> listApiServerConfigDTO = apiServerConfigBiz.selectByRoomIdArrays(roomIdArr);
        List<ApiServerConfigVO> listApiServerConfigVO = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(listApiServerConfigDTO)) {
            for (ApiServerConfigDTO apiServerConfigDTO : listApiServerConfigDTO) {
                ApiServerConfigVO apiServerConfigVO = new ApiServerConfigVO();
                BeanUtils.copyProperties(apiServerConfigDTO, apiServerConfigVO);
                listApiServerConfigVO.add(apiServerConfigVO);
            }
        }
        return listApiServerConfigVO;
    }

    /**
     * 根据机房查zabbix连接信息
     *
     * @param room 主机ID
     * @return
     */
    @Override
    public ApiServerConfigDetailResponse findByRoom(@PathVariable("room") String room) {
        if (StringUtils.isEmpty(room)) {
            LOGGER.error("参数机房不能为空");
            return null;
        }
        ApiServerConfigDTO apiServerConfigDTO = apiServerConfigBiz.selectByRoom(room);
        ApiServerConfigDetailResponse apiServerConfigVO = new ApiServerConfigDetailResponse();
        if (null != apiServerConfigDTO) {
            BeanUtils.copyProperties(apiServerConfigDTO, apiServerConfigVO);
        }
        return apiServerConfigVO;
    }

    /**
     * slf4j
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiServerConfigController.class);

    @Autowired
    private ApiServerConfigBiz apiServerConfigBiz;

}