package com.aspire.mirror.elasticsearch.api.service.zabbix;

import java.text.ParseException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aspire.mirror.elasticsearch.api.dto.AlertEsHistoryReq;

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
@RequestMapping("${version}/cloud")
public interface ICloudSysService {
   
	@PostMapping(value = "insertCloudVm",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void insertCloudVm(@RequestBody AlertEsHistoryReq vm) throws ParseException,IllegalAccessException;
    
	@PostMapping(value = "insertCloudPhy",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void insertCloudPhy(@RequestBody  AlertEsHistoryReq phy) throws ParseException,IllegalAccessException;
	
	@PostMapping(value = "insertCloudPhyList",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public int insertCloudPhyList(@RequestBody  List<AlertEsHistoryReq> phyList) throws ParseException,IllegalAccessException;
	
	@PostMapping(value = "insertCloudVmList",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public int insertCloudVmList(@RequestBody List<AlertEsHistoryReq> vmList) throws ParseException,IllegalAccessException;
}
