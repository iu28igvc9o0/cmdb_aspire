package com.aspire.cmdb.agent.sync.service;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICloudSyncService
 * Author:   zhu.juwang
 * Date:     2019/11/28 19:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICloudSyncService {
    /**
     * 处理同步数据
     * @param data
     */
    void syncData(Object data);
}
