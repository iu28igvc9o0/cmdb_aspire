package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.icon;

import com.aspire.mirror.composite.service.cmdb.icon.ICmdbIconAPI;
import com.aspire.ums.cmdb.icon.payload.CmdbIcon;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.icon.CmdbIconClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: InstanceController
 * Author:   HANGFANG
 * Date:     2019/5/24 14:56
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class IconController implements ICmdbIconAPI {

    @Autowired
    CmdbIconClient cmdbIconClient;

    @Override
    public Object getIcon(@RequestBody(required = false) CmdbIcon icon, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        return cmdbIconClient.getIcon(icon, pageNumber, pageSize);
    }

    @Override
    public Map<String, Object> uploadIcon(HttpServletRequest request) {
        return null;
    }
}
