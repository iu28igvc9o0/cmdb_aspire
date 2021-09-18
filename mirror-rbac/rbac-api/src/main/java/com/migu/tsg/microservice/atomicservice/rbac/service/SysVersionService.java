package com.migu.tsg.microservice.atomicservice.rbac.service;

import com.migu.tsg.microservice.atomicservice.rbac.dto.SysVersionRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author menglinjie
 */
@Api(tags = "sys_sysVersion", description = "平台信息")
@RequestMapping("/sysVersion/v1")
public interface SysVersionService {
    /**
     * 导入平台信息
     * @param multipartFile
     * @return
     * @throws Exception
     */
    @ApiOperation("导入平台信息")
    @PostMapping(value = "/importSysVersion", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Map<Boolean,Object> importSysVersion(@RequestParam("multipartFile") MultipartFile multipartFile) throws Exception;

    /**
     * 查询平台信息
     * @return
     */
    @ApiOperation("查询平台信息")
    @PostMapping(value = "/selectSysVersion", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    SysVersionRes selectSysVersion();
}
