package com.aspire.ums.cmdb.v2.process.instance;

import com.aspire.ums.cmdb.networkCard.payload.CmdbInstanceNetworkCard;
import com.aspire.ums.cmdb.networkCard.service.ICmdbInstanceNetworkCardService;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import com.aspire.ums.cmdb.v2.process.validate.DictValidator;
import com.aspire.ums.cmdb.v2.process.validate.EmptyValidator;
import com.aspire.ums.cmdb.v2.process.validate.FormateValidator;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: NetworkCardImportFactory
 * Author:   luowenbo
 * Date:     2019/9/19 11:00
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@NoArgsConstructor
public class NetworkCardImportFactory extends ImportFactory {
    private ICmdbInstanceNetworkCardService projectService;
    private List<Map<String, Object>> networkCardStatus;
    private List<Map<String, Object>> portTypes;
    private List<Map<String, Object>> portRates;
    private List<Map<String, Object>> isDhcps;

    @Override
    public void initBasic() {
        super.initBasic();
        portTypes = getDictList("select dict_code `key`, dict_note `value` from t_cfg_dict where col_name = 'port_type'");
        networkCardStatus = getDictList("select dict_code `key`, dict_note `value` from t_cfg_dict where col_name = 'network_card_status'");
        portRates = getDictList("select dict_code `key`, dict_note `value` from t_cfg_dict where col_name = 'port_rate'");
        isDhcps = getDictList("select dict_code `key`, dict_note `value` from t_cfg_dict where col_name = 'is_dhcp'");
    }

    @Override
    public void initSpringBean() {
        if (projectService == null) {
            projectService = SpringUtils.getBean(ICmdbInstanceNetworkCardService.class);
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        CmdbInstanceNetworkCard project = new CmdbInstanceNetworkCard();
        String columnValue;
        for (String key : dataMap.keySet()) {
            columnValue = dataMap.get(key);
            if (key.contains("必填")) {
                EmptyValidator.notEmpty(key, columnValue);
            }
            if ("名称[必填]".equals(key)) {
                if(this.projectService.get(columnValue) != null) {
                    throw new RuntimeException("列["+key+"]值[" + columnValue + "]已有该名称");
                }
                project.setNetworkCardName(columnValue);
            }
            if ("链接状态".equals(key)) {
                DictValidator.validator(key, columnValue, this.networkCardStatus);
                project.setNetworkCardStatus(columnValue);
            }
            if ("端口类型[必填]".equals(key)) {
                DictValidator.validator(key, columnValue, this.portTypes);
                project.setPortType(columnValue);
            }
            if ("端口速率[必填]".equals(key)) {
                DictValidator.validator(key, columnValue, this.portRates);
                project.setPortRate(columnValue);
            }
            if ("IPv4地址".equals(key)) {
                FormateValidator.isIpFormate(key, columnValue);
                project.setIpv4Address(columnValue);
            }
            if ("IPv6地址".equals(key)) {
                FormateValidator.isIpFormate(key, columnValue);
                project.setIpv6Address(columnValue);
            }
            if ("mac地址".equals(key)) {
                FormateValidator.isMacFormate(key, columnValue);
                project.setMacAddress(columnValue);
            }
            if ("默认网关".equals(key)) {
                FormateValidator.isIpFormate(key, columnValue);
                project.setDefaultGateway(columnValue);
            }
            if ("是否DHCP[必填]".equals(key)) {
                DictValidator.validator(key, columnValue, this.isDhcps);
                project.setIsDhcp(columnValue);
            }
            if ("DHCP地址".equals(key)) {
                FormateValidator.isIpFormate(key, columnValue);
                project.setDhcpAddress(columnValue);
            }
            if ("DNS服务器".equals(key)) {
                FormateValidator.isIpFormate(key, columnValue);
                project.setDnsServer(columnValue);
            }
            if ("描述".equals(key)) {
                project.setRemark(columnValue);
            }
        }
        try {
            project.setInstanceId(super.getProcessParams().get("instanceId"));
            this.projectService.insert(project);
        } catch (Exception e) {
            throw new RuntimeException("新增信息失败:" + e.getMessage(), e);
        }
    }
}
