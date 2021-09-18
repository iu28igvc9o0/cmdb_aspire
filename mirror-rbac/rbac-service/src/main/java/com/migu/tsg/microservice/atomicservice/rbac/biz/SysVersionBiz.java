package com.migu.tsg.microservice.atomicservice.rbac.biz;

import com.migu.tsg.microservice.atomicservice.rbac.dao.po.SysVersion;
import com.migu.tsg.microservice.atomicservice.rbac.dto.SysVersionRes;
import org.dom4j.DocumentException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author menglinjie
 */
public interface SysVersionBiz {

    Map<Boolean, Object> importSysVersion(MultipartFile multipartFile) throws IOException, DocumentException;

    SysVersion selectSysVersion();
}
