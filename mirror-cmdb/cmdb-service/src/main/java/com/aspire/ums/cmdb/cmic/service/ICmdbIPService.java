package com.aspire.ums.cmdb.cmic.service;

import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbIPService
 * Author:   zhu.juwang
 * Date:     2020/5/27 10:45
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICmdbIPService {

    /**
     * 获取指定IP类型指定网段的IP地址使用情况
     * @param conditions 查询条件
     * @return
     */
    Map<String, Object> statisticsIpUseInfo(Map<String, Object> conditions);
}
