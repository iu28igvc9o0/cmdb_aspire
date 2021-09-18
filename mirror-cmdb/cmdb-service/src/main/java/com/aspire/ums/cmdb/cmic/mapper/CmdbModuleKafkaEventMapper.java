package com.aspire.ums.cmdb.cmic.mapper;

import com.aspire.ums.cmdb.cmic.payload.CmdbModuleKafkaEvent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbModuleKafkaEventMapper
 * Author:   hangfang
 * Date:     2020/9/18
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface CmdbModuleKafkaEventMapper {

    List<CmdbModuleKafkaEvent> listByModuleId(@Param("needSyncModuleId") String needSyncModuleId);
}
