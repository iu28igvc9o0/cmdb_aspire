package com.aspire.mirror.elasticsearch.api.service.zabbix;

import com.aspire.mirror.elasticsearch.api.dto.LldpInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * lldp信息服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.elasticsearch.api.service.zabbix
 * 类名称:    ILLdpInfoService.java
 * 类描述:    lldp信息服务
 * 创建人:    JinSu
 * 创建时间:  2019/9/18 9:54
 * 版本:      v1.0
 */
@RequestMapping("${version}/lldp")
public interface ILLdpInfoService {
    @GetMapping("querylldpInfoByidcAndIp")
    List<LldpInfo> querylldpInfoByidcAndIp(@RequestParam(value = "idcType", required = false) String idcType,
                                           @RequestParam(value = "ips", required = false) String ips);
}
