package com.aspire.ums.cmdb.heartbeat;

import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbHeartBeatController
 * Author:   zhu.juwang
 * Date:     2019/12/13 14:12
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class CmdbHeartBeatController implements ICmdbHeartBeatAPI {
    @Override
    public int heartbeat() {
        return 200;
    }
}
