package com.aspire.mirror.log.server.biz;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.log.api.dto.ConfigDataResponse;
import com.aspire.mirror.log.api.dto.ConfigDataSearchRequest;
import com.aspire.mirror.log.api.dto.ConfigFileUploadReq;

import java.util.List;
import java.util.Set;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.log.server.biz
 * 类名称:    ConfigDataBiz.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/6/19 14:40
 * 版本:      v1.0
 */
public interface ConfigDataBiz {
    PageResponse<ConfigDataResponse> getConfigData(ConfigDataSearchRequest request);

    ConfigDataResponse getConfigById(String index, String id);

    /**
     * 根据索引和ip精确查找配置文件并返回一条数据
     * @param index
     * @param ip
     * @return
     */
    ConfigDataResponse getConfigByIp(String index, String idcType, String ip);

    /**
     *
     * @param idcType
     * @param ip
     * @return
     */
    Set<String> getIndexByIp(String idcType, String ip);

    void uploadConfigFile(List<ConfigFileUploadReq> request);
}
