package com.migu.tsg.microservice.atomicservice.rbac.dao;

import com.migu.tsg.microservice.atomicservice.rbac.dao.po.SysVersion;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author menglinjie
 */
@Mapper
public interface SysVersionDao {
    void importSysVersion(SysVersion sysVersion);

    SysVersion selectSysVersion();
}
