package com.aspire.ums.cmdb.client;

import com.aspire.mirror.elasticsearch.api.service.cmdb.ICmdbApprovalService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbApprovalESClient
 * Author:   hangfang
 * Date:     2019/9/18
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(name = "ELASTICSEARCH-SERVICE")
public interface CmdbApprovalESClient extends ICmdbApprovalService {
}
