package com.aspire.ums.cmdb.cmic.service;

import com.aspire.ums.cmdb.cmic.payload.CmdbModuleKafkaEvent;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbModuleKafkaEventService
 * Author:   hangfang
 * Date:     2020/9/21
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface CmdbModuleKafkaEventService {

    List<CmdbModuleKafkaEvent> listByModuleId(String moduleId);
}
