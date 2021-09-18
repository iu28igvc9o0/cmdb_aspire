package com.aspire.mirror.template.server.biz.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.aspire.mirror.template.api.dto.model.ApiServerConfigDTO;
import com.aspire.mirror.template.server.biz.ApiServerConfigBiz;
import com.aspire.mirror.template.server.dao.ApiServerConfigDao;
import com.aspire.mirror.template.server.dao.po.ApiServerConfig;
import com.aspire.mirror.template.server.dao.po.transform.ApiServerConfigTransformer;

/**
 * monitor_api_server_config业务层实现类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.biz.impl
 * 类名称:    ApiServerConfigBizImpl.java
 * 类描述:    monitor_api_server_config业务逻辑层实现类
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
@Service
public class ApiServerConfigBizImpl implements ApiServerConfigBiz {

    /**
     * 新增数据
     *
     * @param apiServerConfigDTO monitor_api_server_configDTO对象
     * @return int 新增数据条数
     */
    public int insert(final ApiServerConfigDTO apiServerConfigDTO) {
        if (null == apiServerConfigDTO) {
            LOGGER.error("method[insert] param[apiServerConfigDTO] is null");
            throw new RuntimeException("param[apiServerConfigDTO] is null");
        }
        ApiServerConfig apiServerConfig = ApiServerConfigTransformer.toPo(apiServerConfigDTO);
        return apiServerConfigDao.insert(apiServerConfig);
    }

    /**
     * 批量新增monitor_api_server_config数据
     *
     * @param listApiServerConfigDTO monitor_api_server_configDTO集合对象
     * @return int 新增数据条数
     */
    public int insertByBatch(final List<ApiServerConfigDTO> listApiServerConfigDTO) {
        if (CollectionUtils.isEmpty(listApiServerConfigDTO)) {
            LOGGER.error("method[insertByBatch] param[listApiServerConfigDTO] is null");
            throw new RuntimeException("param[listApiServerConfigDTO] is null");
        }
        List<ApiServerConfig> listApiServerConfig = ApiServerConfigTransformer.toPo(listApiServerConfigDTO);
        return apiServerConfigDao.insertByBatch(listApiServerConfig);
    }

    /**
     * 根据主键删除数据
     *
     * @param apiServerId 主键
     * @return int 删除数据条数
     */
    public int deleteByPrimaryKey(final String apiServerId) {
        if (StringUtils.isEmpty(apiServerId)) {
            LOGGER.error("method[eleteByPrimaryKey] param[apiServerId] is null");
            throw new RuntimeException("param[apiServerId] is null");
        }
        return apiServerConfigDao.deleteByPrimaryKey(apiServerId);
    }

    /**
     * 根据主键数组批量删除数据
     *
     * @param apiServerIdArrays 主键数组
     * @return int 删除数据条数
     */
    public int deleteByPrimaryKeyArrays(final String[] apiServerIdArrays) {
        if (ArrayUtils.isEmpty(apiServerIdArrays)) {
            LOGGER.error("method[deleteByPrimaryKeyArrays] param[apiServerIdArrays] is null");
            throw new RuntimeException("param[apiServerIdArrays] is null");
        }
        return apiServerConfigDao.deleteByPrimaryKeyArrays(apiServerIdArrays);
    }

    /**
     * 根据条件删除数据
     *
     * @param apiServerConfigDTO monitor_api_server_configDTO对象
     * @return int 删除数据条数
     */
    public int delete(final ApiServerConfigDTO apiServerConfigDTO) {
        if (null == apiServerConfigDTO) {
            LOGGER.error("method[delete] param[apiServerConfigDTO] is null");
            throw new RuntimeException("param[apiServerConfigDTO] is null");
        }
        ApiServerConfig apiServerConfig = ApiServerConfigTransformer.toPo(apiServerConfigDTO);
        return apiServerConfigDao.delete(apiServerConfig);
    }

    /**
     * 根据参数选择性更新数据
     *
     * @param apiServerConfigDTO monitor_api_server_configDTO对象
     * @return int 数据条数
     */
    public int updateByPrimaryKeySelective(final ApiServerConfigDTO apiServerConfigDTO) {
        if (null == apiServerConfigDTO) {
            LOGGER.error("method[updateByPrimaryKey] param[apiServerConfigDTO] is null");
            throw new RuntimeException("param[apiServerConfigDTO] is null");
        }
        ApiServerConfig apiServerConfig = ApiServerConfigTransformer.toPo(apiServerConfigDTO);
        return apiServerConfigDao.updateByPrimaryKeySelective(apiServerConfig);
    }

    /**
     * 根据主键更新数据
     *
     * @param apiServerConfigDTO monitor_api_server_configDTO对象
     * @return int 数据条数
     */
    public int updateByPrimaryKey(final ApiServerConfigDTO apiServerConfigDTO) {
        if (null == apiServerConfigDTO) {
            LOGGER.error("method[updateByPrimaryKey] param[apiServerConfigDTO] is null");
            throw new RuntimeException("param[apiServerConfigDTO] is null");
        }
        if (StringUtils.isEmpty(apiServerConfigDTO.getApiServerId())) {
            LOGGER.warn("method[updateByPrimaryKey] param[apiServerId] is null");
            throw new RuntimeException("param[apiServerId] is null");
        }
        ApiServerConfig apiServerConfig = ApiServerConfigTransformer.toPo(apiServerConfigDTO);
        return apiServerConfigDao.updateByPrimaryKey(apiServerConfig);
    }

    /**
     * 根据主键查询
     *
     * @param apiServerId 主键
     * @return ApiServerConfigDTO 返回对象
     */
    public ApiServerConfigDTO selectByPrimaryKey(final String apiServerId) {
        ApiServerConfig apiServerConfig = apiServerConfigDao.selectByPrimaryKey(apiServerId);
        if (StringUtils.isEmpty(apiServerId)) {
            LOGGER.warn("method[selectByPrimaryKey] param[apiServerId] is null");
            return null;
        }
        return ApiServerConfigTransformer.fromPo(apiServerConfig);
    }

    /**
     * 根据主键数组查询
     *
     * @param apiServerIdArrays 主键数组
     * @return List<ApiServerConfigDTO> 返回集合对象
     */
    public List<ApiServerConfigDTO> selectByPrimaryKeyArrays(final String[] apiServerIdArrays) {
        if (ArrayUtils.isEmpty(apiServerIdArrays)) {
            LOGGER.warn("method[selectByPrimaryKeyArrays] param[apiServerIdArrays] is null");
            return Collections.emptyList();
        }
        List<ApiServerConfig> listApiServerConfig = apiServerConfigDao.selectByPrimaryKeyArrays(apiServerIdArrays);
        return ApiServerConfigTransformer.fromPo(listApiServerConfig);
    }

    @Override
    public List<ApiServerConfigDTO> selectByRoomIdArrays(String[] roomIdArr) {
        if (ArrayUtils.isEmpty(roomIdArr)) {
            LOGGER.warn("method[selectByRoomIdArrays] param[roomIdArr] is null");
            return Collections.emptyList();
        }
        List<ApiServerConfig> listApiServerConfig = apiServerConfigDao.selectByRoomIdArrays(roomIdArr);
        return ApiServerConfigTransformer.fromPo(listApiServerConfig);
    }

    /**
     * 根据dto实体查询列表
     *
     * @param apiServerConfigDTO monitor_api_server_configDTO对象
     * @return List<ApiServerConfig>  返回集合
     */
    public List<ApiServerConfigDTO> select(final ApiServerConfigDTO apiServerConfigDTO) {
        if (null == apiServerConfigDTO) {
            LOGGER.warn("select Object apiServerConfigDTO is null");
            return Collections.emptyList();
        }
        ApiServerConfig apiServerConfig = ApiServerConfigTransformer.toPo(apiServerConfigDTO);
        List<ApiServerConfig> listApiServerConfig = apiServerConfigDao.select(apiServerConfig);
        return ApiServerConfigTransformer.fromPo(listApiServerConfig);
    }

    /**
     * 根据DTO实体查询条数
     *
     * @param apiServerConfigDTO monitor_api_server_configDTO对象
     * @return int 数据条数
     */
    public int selectCount(final ApiServerConfigDTO apiServerConfigDTO) {
        if (null == apiServerConfigDTO) {
            LOGGER.warn("selectCount Object apiServerConfigDTO is null");
        }
        ApiServerConfig apiServerConfig = ApiServerConfigTransformer.toPo(apiServerConfigDTO);
        return apiServerConfigDao.selectCount(apiServerConfig);
    }

    /**
     * 根据机房查对象
     *
     * @param room 机房
     * @return
     */
    @Override
    public ApiServerConfigDTO selectByRoom(String room) {
        ApiServerConfig param = new ApiServerConfig();
        param.setRoom(room);
        List<ApiServerConfig> list = apiServerConfigDao.select(param);
        if (!CollectionUtils.isEmpty(list)) {
            return ApiServerConfigTransformer.fromPo(list.get(0));
        }
        return null;
    }

    /**
     * slf4j
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiServerConfigBizImpl.class);

    /**
     * 数据访问层接口
     */
    @Autowired
    private ApiServerConfigDao apiServerConfigDao;

} 
