package com.migu.tsg.microservice.atomicservice.rbac.controller;

import com.migu.tsg.microservice.atomicservice.rbac.biz.SysVersionBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.SysMenu;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.SysVersion;
import com.migu.tsg.microservice.atomicservice.rbac.dto.SysMenuResp;
import com.migu.tsg.microservice.atomicservice.rbac.dto.SysVersionRes;
import com.migu.tsg.microservice.atomicservice.rbac.service.SysVersionService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author menglinjie
 */
@RestController
public class SysVersionController implements SysVersionService {
    @Resource
    private SysVersionBiz sysVersionBiz;

    @Override
    public Map<Boolean, Object> importSysVersion(MultipartFile multipartFile) throws Exception {
        if (null == multipartFile) {
            Map<Boolean, Object> map = new HashMap<>();
            map.put(false, "导入平台信息失败！");
            return map;
        }
        return sysVersionBiz.importSysVersion(multipartFile);
    }

    @Override
    public SysVersionRes selectSysVersion() {
        SysVersion sysVersion = sysVersionBiz.selectSysVersion();
        SysVersionRes res = new SysVersionRes();
        BeanUtils.copyProperties(sysVersion, res);
        return res;
    }
}
