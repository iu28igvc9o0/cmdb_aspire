package com.aspire.ums.cmdb.collect.service;


import com.aspire.ums.cmdb.collect.entity.AutoDiscoveryLogShieldEntity;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AutoDiscoveryLogShieldService
 * Author:   HANGFANG
 * Date:     2019/4/19 10:44
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface AutoDiscoveryLogShieldService {

    List<String> list(String ruleId);

}
