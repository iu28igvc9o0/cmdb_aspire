package com.aspire.cmdb.agent.collect.mapper;

import com.aspire.cmdb.agent.collect.entity.EsAgentDataEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbSyncSuyanRelationMapper
 * Author:   hangfang
 * Date:     2020/9/14
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

@Mapper
public interface EsAgentDataMapper {
    void insert(EsAgentDataEntity dataEntity);
    void deleteByAgentDate(@Param("agentDate") String agentDate);
}
