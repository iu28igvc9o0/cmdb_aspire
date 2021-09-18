package com.aspire.ums.cmdb.IpResource.service.impl;

import com.aspire.ums.cmdb.IpResource.mapper.CmdbConfigDictMapper;
import com.aspire.ums.cmdb.IpResource.service.AssetExportExcelService;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.sync.entity.CmdbBusinessLine;
import com.aspire.ums.cmdb.sync.mapper.CmdbBusinessLineMapper;
import com.aspire.ums.cmdb.util.StringUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fanwenhui
 * @date 2020-06-18 15:07
 * @description
 */
@Slf4j
@Service
public class AssetExportExcelServiceImpl implements AssetExportExcelService {

    @Autowired
    private CmdbConfigDictMapper configDictMapper;
    @Autowired
    private CmdbBusinessLineMapper cmdbBusinessLineMapper;

    @Override
    public List<List<Object>> getAssetExportInfo() {

        List<Object> deviceIpList = Lists.newArrayList(); // 主IP地址(必填)
        buildOtherInfo(deviceIpList,new String[]{},"DEVICE_IP");

        List<Object> business1 = Lists.newArrayList(); // 一级业务(必填)
        List<Object> business2 = Lists.newArrayList(); // 二级业务
        buildBusinessInfo(business1,business2);

        List<Object> idcTypeLists = Lists.newArrayList(); // 所属位置(必填)
        List<Object> idcLists = Lists.newArrayList(); // IDC位置(必填)
        buildConfigDictList(idcTypeLists,idcLists,"IDC_LABEL","IDC","idcType");

        List<Object> idcLocationTypeLists = Lists.newArrayList(); // 机房位置
        buildConfigDictList(idcLocationTypeLists,null,"IDC_LOCATION",null,"idcLocationType");

        List<Object> deviceBelongNetworkLayerLists = Lists.newArrayList(); // 设备网络层级 需要去重
        buildDiffConfigDictList(deviceBelongNetworkLayerLists,"DEVICE_NETWORK_LAYER_NAME","deviceBelongNetworkLayer");

        List<Object> applicationModuleTypeLists = Lists.newArrayList(); // 应用模块 需要去重
        buildDiffConfigDictList(applicationModuleTypeLists,"APPLICATIONMODULE","applicationModuleType");

        List<Object> deviceClassLists = Lists.newArrayList(); // 设备分类(必填)
        List<Object> deviceModelLists = Lists.newArrayList(); // 设备型号(必填)
        List<Object> deviceTypeLists = Lists.newArrayList(); // 设备类型(必填)
        buildDeviceInfo(deviceClassLists,deviceModelLists,deviceTypeLists,"deviceClass","deviceModel","deviceType");

        List<Object> deviceOsTypeLists = Lists.newArrayList(); // 设备系统类型(必填)
        List<String> driverTypeValues = getDriverTypeList(deviceOsTypeLists, "device_os_type", "deviceOsType");

        List<Object> deviceStatus = Lists.newArrayList(); // 设备状态(上电/下电)(必填)
        buildOtherInfo(deviceStatus,new String[]{"上电","下电"},"deviceStatus");

        List<Object> managed_by_ansibleList = Lists.newArrayList(); // 是否ansible管理(1=是/0=否)(必填)
        buildOtherInfo(managed_by_ansibleList,new String[]{"是", "否"},"managed_by_ansible");

        List<Object> mgd_by_poolList = Lists.newArrayList(); // 是否资源池管理设备(1=是/0=否)(必填)
        buildOtherInfo(mgd_by_poolList,new String[]{"是", "否"},"mgd_by_pool");

        List<Object> host_backupList = Lists.newArrayList(); // 主备关系
        buildOtherInfo(host_backupList,new String[]{"主", "备1", "备2", "备3", "备4", "备5", "备6", "备7", "备8", "备9", "备10", "备11", "备12"},"host_backup");

        List<Object> zabbix_agent_monitor_flagList = Lists.newArrayList();
        buildOtherInfo(zabbix_agent_monitor_flagList,new String[]{"是", "否"},"zabbix_agent_monitor_flag");

        List<Object> amp_agent_monitor_flagList = Lists.newArrayList();
        buildOtherInfo(amp_agent_monitor_flagList,new String[]{"是", "否"},"amp_agent_monitor_flag");

        List<Object> filebeat_agent_monitor_flagList = Lists.newArrayList();
        buildOtherInfo(filebeat_agent_monitor_flagList,new String[]{"是", "否"},"filebeat_agent_monitor_flag");

        List<Object> filebeat_log_pathList = Lists.newArrayList();
        buildOtherInfo(filebeat_log_pathList,new String[]{},"filebeat_log_path");

        List<Object> proxy_ipList = Lists.newArrayList(); // 网络设备时,自动化agent安装必须提供proxyIp
        buildOtherInfo(proxy_ipList,new String[]{},"proxy_ip");

        List<Object> resource_planList = Lists.newArrayList();
        buildOtherInfo(resource_planList,new String[]{"计划内", "计划外"},"resource_plan");

        List<Object> vm_creation_dateList = Lists.newArrayList();// VM创建时间 VMCREATION_DATE
        buildOtherInfo(vm_creation_dateList,new String[]{},"vm_creation_date");

        List<Object> blong_toList = Lists.newArrayList();
        buildOtherInfo(blong_toList,new String[]{},"blong_to");

        List<Object> business_ipList1 = Lists.newArrayList();
        buildOtherInfo(business_ipList1,new String[]{},"business_ip1");

        List<Object> business_ipList2 = Lists.newArrayList();
        buildOtherInfo(business_ipList2,new String[]{},"business_ip2");

        List<Object> other_ipList = Lists.newArrayList();
        buildOtherInfo(other_ipList,new String[]{},"other_ip");

        List<Object> device_log_nameList = Lists.newArrayList();
        buildOtherInfo(device_log_nameList,new String[]{},"device_log_name");

        List<Object> device_cellList = Lists.newArrayList();
        buildOtherInfo(device_cellList,new String[]{},"device_cell");

        List<Object> box_numList = Lists.newArrayList();
        buildOtherInfo(box_numList,new String[]{},"box_num");

        List<Object> slot_numList = Lists.newArrayList();
        buildOtherInfo(slot_numList,new String[]{},"slot_num");

        List<Object> box_mgd_ipList = Lists.newArrayList();
        buildOtherInfo(box_mgd_ipList,new String[]{},"box_mgd_ip");

        List<Object> exsi_ipList = Lists.newArrayList();
        buildOtherInfo(exsi_ipList,new String[]{},"exsi_ip");

        List<Object> vm_nameList = Lists.newArrayList();
        buildOtherInfo(vm_nameList,new String[]{},"vm_name");

        List<Object> vm_ipList = Lists.newArrayList();
        buildOtherInfo(vm_ipList,new String[]{},"vm_ip");

        List<Object> device_standardList = Lists.newArrayList();
        buildOtherInfo(device_standardList,new String[]{},"device_standard");

        List<Object> device_snList = Lists.newArrayList();
        buildOtherInfo(device_snList,new String[]{},"device_sn");

        List<Object> b_card_snList = Lists.newArrayList();
        buildOtherInfo(b_card_snList,new String[]{},"b_card_sn");

        List<Object> asset_numberList = Lists.newArrayList();
        buildOtherInfo(asset_numberList,new String[]{},"asset_number");

        List<Object> dis_st_dirList = Lists.newArrayList();
        buildOtherInfo(dis_st_dirList,new String[]{},"dis_st_dir");

        List<Object> maintencePurchaseTypeLists = Lists.newArrayList();
        buildDiffConfigDictList(maintencePurchaseTypeLists,"maintence_purchase_flag","maintencePurchaseType");

        List<Object> out_of_maintence_dateList = Lists.newArrayList();
        buildOtherInfo(out_of_maintence_dateList,new String[]{},"out_of_maintence_date");

        List<Object> maintence_factory_nameList = Lists.newArrayList();
        buildOtherInfo(maintence_factory_nameList,new String[]{},"maintence_factory_name");

        List<Object> maintence_provider_contractList = Lists.newArrayList();
        buildOtherInfo(maintence_provider_contractList,new String[]{},"maintence_provider_contract");

        List<Object> maintence_factory_contractList = Lists.newArrayList();
        buildOtherInfo(maintence_factory_contractList,new String[]{},"maintence_factory_contract");

        List<Object> maintence_start_timeList = Lists.newArrayList();
        buildOtherInfo(maintence_start_timeList,new String[]{},"maintence_start_time");

        List<Object> maintence_end_timeList = Lists.newArrayList();
        buildOtherInfo(maintence_end_timeList,new String[]{},"maintence_end_time");

        List<Object> maintence_purchase_typeList = Lists.newArrayList();
        buildOtherInfo(maintence_purchase_typeList,new String[]{},"maintence_purchase_type");

        List<Object> maintence_adminList = Lists.newArrayList();
        buildOtherInfo(maintence_adminList,new String[]{},"maintence_admin");

        List<Object> venderMaintenceTypeLists = Lists.newArrayList(); // 是否需原厂维保
        buildDiffConfigDictList(venderMaintenceTypeLists,"vender_maintence_flag","venderMaintenceType");

        List<Object> maintencePurchaseDescTypeLists = Lists.newArrayList(); // 原厂维保购买必要性说明
        buildDiffConfigDictList(maintencePurchaseDescTypeLists,"maintence_purchase_desc","maintencePurchaseDescType");

        List<Object> online_timeList = Lists.newArrayList();
        buildOtherInfo(online_timeList,new String[]{},"online_time");

        List<Object> console_ipList = Lists.newArrayList();
        buildOtherInfo(console_ipList,new String[]{},"console_ip");

        List<Object> console_vlanList = Lists.newArrayList();
        buildOtherInfo(console_vlanList,new String[]{},"console_vlan");

        List<Object> console_maskList = Lists.newArrayList();
        buildOtherInfo(console_maskList,new String[]{},"console_mask");

        List<Object> console_gwList = Lists.newArrayList();
        buildOtherInfo(console_gwList,new String[]{},"console_gw");

        List<Object> console_userList = Lists.newArrayList();
        buildOtherInfo(console_userList,new String[]{},"console_user");

        List<Object> console_passwordList = Lists.newArrayList();
        buildOtherInfo(console_passwordList,new String[]{},"console_password");

        List<Object> business_vlanList = Lists.newArrayList();
        buildOtherInfo(business_vlanList,new String[]{},"business_vlan");

        List<Object> local_diskList = Lists.newArrayList();
        buildOtherInfo(local_diskList,new String[]{},"local_disk");

        List<Object> network_areaList = Lists.newArrayList();
        buildOtherInfo(network_areaList,new String[]{},"network_area");

        List<Object> remarkList = Lists.newArrayList();
        buildOtherInfo(remarkList,new String[]{},"remark");

        List<Object> trans_costList = Lists.newArrayList(); //转资成本
        buildOtherInfo(trans_costList,new String[]{},"trans_cost");

        List<Object> unit_priceList = Lists.newArrayList(); //单价
        buildOtherInfo(unit_priceList,new String[]{},"unit_price");

        List<Object> prorate_dateList = Lists.newArrayList(); //按比例分摊日期
        buildOtherInfo(prorate_dateList,new String[]{},"prorate_date");

        List<Object> service_lifeList = Lists.newArrayList(); //使用年限
        buildOtherInfo(service_lifeList,new String[]{},"service_life");

        List<Object> equipmentRiskLevelTypeLists = Lists.newArrayList(); //设备风险等级
        buildDiffConfigDictList(equipmentRiskLevelTypeLists,"EQUIPMENT_RISK_LEVEL","EquipmentRiskLevel");

        List<Object> deviceFactoryNameLists = Lists.newArrayList(); //设备厂家
        buildDiffConfigDictList(deviceFactoryNameLists,"deviceFactoryName","deviceFactoryName");

        List<Object> projectBelongToLists = Lists.newArrayList(); // 项目归属
        buildDiffConfigDictList(projectBelongToLists,"cmdbProjectBelongTo","cmdbProjectBelongTo");

        List<Object> initialNatIpList = Lists.newArrayList(); // nat前IP
        buildOtherInfo(initialNatIpList,new String[]{},"initialNatIp");

        List<Object> driverTypeLists = Lists.newArrayList(); // 资源驱动信息
        if(!deviceOsTypeLists.isEmpty()) {
            List<String> winDriverList = Arrays.asList("独立主机", "域控主机", "域内主机");
            List<String> networkDriverList = Arrays.asList("SSH", "TELET");
            driverTypeValues.addAll(winDriverList);
            driverTypeValues.addAll(networkDriverList);
            buildOtherInfo(driverTypeLists,driverTypeValues.toArray(new String[0]),"driver_type");
        }

        List<Object> devDomainNameList = Lists.newArrayList(); // 域名城
        buildOtherInfo(devDomainNameList,new String[]{},"devDomainName");

        List<Object> impDevDomainCtrlerList = Lists.newArrayList(); // 所属域控制器
        buildOtherInfo(impDevDomainCtrlerList,new String[]{},"impDevDomainCtrler");

        List<Object> ipTypeList = Lists.newArrayList(); // IP启用类型
        buildOtherInfo(ipTypeList,new String[]{"IPV6", "IPV4"},"ipType");

        List<Object> ipv6List = Lists.newArrayList(); // IPv6地址
        buildOtherInfo(ipv6List,new String[]{},"ipv6");

        List<Object> responsibleUserList = Lists.newArrayList(); // 责任人
        buildOtherInfo(responsibleUserList,new String[]{},"responsibleUser");

        List<Object> impIsUserDefineList = Lists.newArrayList(); // 账号自学习
        buildOtherInfo(impIsUserDefineList,new String[]{"是", "否"},"impIsUserDefine");

        List<Object> adminAcctList = Lists.newArrayList(); // 管理员账号
        buildOtherInfo(adminAcctList,new String[]{},"adminAcct");

        List<Object> adminPwdList = Lists.newArrayList(); // 管理员密码
        buildOtherInfo(adminPwdList,new String[]{},"adminPwd");

        List<Object> adminPrmtSymbolList = Lists.newArrayList(); // 管理员提示符
        buildOtherInfo(adminPrmtSymbolList,new String[]{},"adminPrmtSymbol");

        List<Object> prmtSymbolList = Lists.newArrayList(); // 用户提示符
        buildOtherInfo(prmtSymbolList,new String[]{},"prmtSymbol");

        List<Object> synAcctList = Lists.newArrayList(); // 同步账号名称
        buildOtherInfo(synAcctList,new String[]{},"synAcct");

        List<Object> synAcctPwdList = Lists.newArrayList(); // 同步账号密码
        buildOtherInfo(synAcctPwdList,new String[]{},"synAcctPwd");

        List<Object> isAccountSynList = Lists.newArrayList(); // 网络设备资源定义方式
        buildOtherInfo(isAccountSynList,new String[]{"是", "否"},"isAccountSyn");

        List<Object> logonPwdList = Lists.newArrayList(); // 网络设备登录密码
        buildOtherInfo(logonPwdList,new String[]{},"logonPwd");

        List<Object> resNetdevEnableCmdList = Lists.newArrayList(); // 网络设备使能命令
        buildOtherInfo(resNetdevEnableCmdList,new String[]{},"resNetdevEnableCmd");

        List<Object> resNetdevEnablePwdList = Lists.newArrayList(); // 网络设备使能密码
        buildOtherInfo(resNetdevEnablePwdList,new String[]{},"resNetdevEnablePwd");

        List<Object> resEquipmentPrmtList = Lists.newArrayList(); // 设备登录成功提示符
        buildOtherInfo(resEquipmentPrmtList,new String[]{},"resEquipmentPrmt");

        List<Object> resEnableUserPrmtList = Lists.newArrayList(); // 使能主账号登录成功提示符
        buildOtherInfo(resEnableUserPrmtList,new String[]{},"resEnableUserPrmt");

        List<Object> impSubProxyServerList = Lists.newArrayList(); // 从帐号同步服务器
        buildOtherInfo(impSubProxyServerList,new String[]{},"impSubProxyServer");

        List<Object> impFortServerList = Lists.newArrayList(); // 堡垒主机名称
        buildOtherInfo(impFortServerList,new String[]{},"impFortServer");

        List<Object> resCommunicationLoginTypeLists = Lists.newArrayList(); // 通讯方式对应登陆类型 resCommunicationLoginType
        buildConfigDictList(resCommunicationLoginTypeLists,null,"resCommunicationLoginType",null,"loginType");

        List<Object> companyIdList = Lists.newArrayList(); // 资源制造商标识
        buildOtherInfo(companyIdList,new String[]{},"companyId");

        List<Object> proSeriesList = Lists.newArrayList(); // 产品系列标识
        buildOtherInfo(proSeriesList,new String[]{},"proSeries");

        List<Object> proVersionList = Lists.newArrayList(); // 资源版本号信息
        buildOtherInfo(proVersionList,new String[]{},"proVersion");

        List<Object> resLinkServerPortList = Lists.newArrayList(); // 连接主机端口
        buildOtherInfo(resLinkServerPortList,new String[]{},"resLinkServerPort");

        List<Object> devDomainDnList = Lists.newArrayList(); // 域DN
        buildOtherInfo(devDomainDnList,new String[]{},"devDomainDn");

        List<Object> adminProtocalList = Lists.newArrayList(); // 管理协议
        buildOtherInfo(adminProtocalList,new String[]{},"adminProtocal");

        List<Object> adminPortList = Lists.newArrayList(); // 管理端口
        buildOtherInfo(adminPortList,new String[]{},"adminPort");

        List<Object> fieldList = Lists.newArrayList(); // 归属域
        buildOtherInfo(fieldList,new String[]{},"field");

        List<Object> groupUUIDList = Lists.newArrayList(); // 资源目录标识
        buildOtherInfo(groupUUIDList,new String[]{},"groupUUID");

        List<Object> hostNameList = Lists.newArrayList(); // 主机名
        buildOtherInfo(hostNameList,new String[]{},"hostName");

        List<Object> manageModeList = Lists.newArrayList(); // 管理方式
        buildOtherInfo(manageModeList,new String[]{},"manageMode");

        List<Object> commPortList = Lists.newArrayList(); // 通信端口号
        buildOtherInfo(commPortList,new String[]{},"commPort");

        List<Object> isSetMaxConnList = Lists.newArrayList(); // 归属域
        buildOtherInfo(isSetMaxConnList,new String[]{"是", "否"},"isSetMaxConn");

        List<Object> maxConnectionList = Lists.newArrayList(); // 最大链接数
        buildOtherInfo(maxConnectionList,new String[]{},"maxConnection");

        // 构建资产模板的数据
        List<List<Object>> result = new ArrayList<>();
        result.add(deviceIpList);
        result.add(business1);
        result.add(business2);
        result.add(idcLists);
        result.add(idcTypeLists);
        result.add(idcLocationTypeLists);
        result.add(deviceClassLists);
        result.add(deviceModelLists);
        result.add(deviceTypeLists);
        result.add(deviceOsTypeLists);// 10

        result.add(deviceBelongNetworkLayerLists);// 11
        result.add(applicationModuleTypeLists);// 12 2018-12-22created
        result.add(deviceStatus);
        result.add(managed_by_ansibleList);
        result.add(mgd_by_poolList);
        result.add(zabbix_agent_monitor_flagList);// 16 //是否配置zabbix 2019-01-03
        result.add(amp_agent_monitor_flagList);// 17 //是否安装自动化agent 2019-02-18
        result.add(filebeat_agent_monitor_flagList);// 18
        result.add(filebeat_log_pathList);// 19
        result.add(proxy_ipList);// 20

        result.add(host_backupList);// 21
        result.add(resource_planList);// 22
        result.add(vm_creation_dateList);// 22-40没有下拉
        result.add(projectBelongToLists);// 项目归属
        result.add(business_ipList1);
        result.add(business_ipList2);
        result.add(other_ipList);

        result.add(device_log_nameList);
        result.add(device_cellList);
        result.add(box_numList);
        result.add(slot_numList);
        result.add(box_mgd_ipList);
        result.add(exsi_ipList);
        result.add(vm_nameList);
        result.add(vm_ipList);
        result.add(device_standardList);
        result.add(device_snList);
        result.add(b_card_snList);
        result.add(asset_numberList);
        result.add(dis_st_dirList);// 40

        result.add(maintencePurchaseTypeLists);// 41 下拉
        result.add(out_of_maintence_dateList);// 42
        result.add(venderMaintenceTypeLists);// 43 下拉
        result.add(maintencePurchaseDescTypeLists);// 44下拉

        result.add(maintence_factory_nameList);// 45-67无下拉
        result.add(maintence_provider_contractList);
        result.add(maintence_factory_contractList);
        result.add(maintence_start_timeList);
        result.add(maintence_end_timeList);
        result.add(maintence_purchase_typeList);
        result.add(maintence_adminList);
        result.add(online_timeList);
        result.add(console_ipList);
        result.add(console_vlanList);
        result.add(console_maskList);
        result.add(console_gwList);
        result.add(console_userList);
        result.add(console_passwordList);
        result.add(business_vlanList);
        result.add(local_diskList);
        result.add(network_areaList);
        result.add(remarkList);// 63

        result.add(trans_costList);
        result.add(unit_priceList);
        result.add(prorate_dateList);
        result.add(service_lifeList);// 67
        result.add(equipmentRiskLevelTypeLists);// 设备风险等级
        result.add(deviceFactoryNameLists);// 设备厂家

        result.add(initialNatIpList);// nat前IP
        result.add(driverTypeLists);// 资源驱动信息 70
        result.add(devDomainNameList);// 域名城
        result.add(impDevDomainCtrlerList);// 所属域控制器
        result.add(ipTypeList);// IP启用类型 71
        result.add(ipv6List);// IPv6地址
        result.add(responsibleUserList);// 责任人
        result.add(impIsUserDefineList);// 账号自学习
        result.add(adminAcctList);// 管理员账号
        result.add(adminPwdList);// 管理员密码
        result.add(adminPrmtSymbolList);// 管理员提示符
        result.add(prmtSymbolList);// 用户提示符
        result.add(synAcctList);// 同步账号名称
        result.add(synAcctPwdList);// 同步账号密码
        result.add(isAccountSynList);// 网络设备资源定义方式
        result.add(logonPwdList);// 网络设备登录密码
        result.add(resNetdevEnableCmdList);// 网络设备使能命令
        result.add(resNetdevEnablePwdList);// 网络设备使能密码
        result.add(resEquipmentPrmtList);// 设备登录成功提示符
        result.add(resEnableUserPrmtList);// 使能主账号登录成功提示符

        result.add(impSubProxyServerList);// 从帐号同步服务器
        result.add(impFortServerList);// 堡垒主机名称
        result.add(resCommunicationLoginTypeLists);// 通讯方式对应登陆类型
        result.add(companyIdList);// 资源制造商标识
        result.add(proSeriesList);// 产品系列标识
        result.add(proVersionList);// 资源版本号信息
        result.add(resLinkServerPortList);// 连接主机端口
        result.add(devDomainDnList);// 域DN
        result.add(adminProtocalList);// 管理协议
        result.add(adminPortList);// 管理端口
        result.add(fieldList);// 归属域
        result.add(groupUUIDList);// 资源目录标识
        result.add(hostNameList);// 主机名
        result.add(manageModeList);// 管理方式
        result.add(commPortList);// 通信端口号
        result.add(isSetMaxConnList);// 是否最大链接数，Y表是，N表否
        result.add(maxConnectionList);// 最大链接数

        return result;
    }

    /**
     * 构建资产表需要的数据参数（不去重）
     * @param list1 第1种类型的数据
     * @param list2 第2种类型的数据
     * @param label1 第1种类型
     * @param label2 第2种类型
     * @param idcType 需要查询的字典表类型
     */
    private void buildConfigDictList(List<Object> list1,List<Object> list2,String label1,String label2,String idcType) {
        String[] tempList = null;
        List<String> stringList1 = new ArrayList<>();
        List<String> stringList2 = new ArrayList<>();

        List<ConfigDict> configDictList = configDictMapper.listByEntity(new ConfigDict(idcType));
        if (!configDictList.isEmpty()) {
            configDictList.forEach(dict -> {
                stringList1.add(dict.getName());
                stringList2.add(dict.getValue());
            });
            tempList = stringList1.toArray(new String[0]);
            list1.add(tempList);
            list1.add(label1);

            if (StringUtils.isNotEmpty(label2)) {
                tempList = stringList2.toArray(new String[1]);
                list2.add(tempList);
                list2.add(label2);
            }
        }
    }

    /**
     * 构建资产表需要的数据参数（去重）
     * @param list1 第1种类型的数据
     * @param label1 第1种类型
     * @param idcType 需要查询的字典表类型
     */
    private void buildDiffConfigDictList(List<Object> list1,String label1,String idcType) {
        String[] tempList = null;
        List<String> stringList1 = new ArrayList<>();
        List<String> stringList2 = new ArrayList<>();

        List<ConfigDict> configDictList = configDictMapper.listByEntity(new ConfigDict(idcType));
        if (!configDictList.isEmpty()) {
            configDictList.forEach(dict -> {
                stringList1.add(dict.getName());
                stringList2.add(dict.getValue());
            });
            tempList = stringList1.toArray(new String[0]);
            list1.add(tempList);
            list1.add(label1);
        }
    }

    /**
     * 构建有级联关系的设备分类和设备类型数据参数
     * @param list1 1级类型的数据
     * @param list2 2级类型的数据
     * @param list3 3级类型的数据
     * @param idcType1 1级字典表类型
     * @param idcType2 2级字典表类型
     * @param idcType3 3级字典表类型
     */
    private void buildDeviceInfo(List<Object> list1,List<Object> list2,List<Object> list3,String idcType1,String idcType2,String idcType3) {
        List<ConfigDict> idcTypeList1 = configDictMapper.listByEntity(new ConfigDict(idcType1));
        List<ConfigDict> idcTypeList2 = configDictMapper.listByEntity(new ConfigDict(idcType2));
        List<ConfigDict> idcTypeList3 = configDictMapper.listByEntity(new ConfigDict(idcType3));

        List<String> deviceClassArray = new ArrayList<>();
        LinkedList<Map<String, Object>> modelLinkedList = new LinkedList<>();
        if (!idcTypeList1.isEmpty()) {
            for (ConfigDict dict1 : idcTypeList1) {
                Map<String, Object> hashMap = new HashMap<>();
                List<String> modelList = new ArrayList<>();
                List<String> typeList = new ArrayList<>();
                String ID = dict1.getId();
                for (ConfigDict dict2 : idcTypeList2) {
                    String PID2 = dict2.getPid();
                    if (StringUtils.isNotEmpty(PID2) && PID2.equals(ID)) {
                        deviceClassArray.add(dict1.getValue());
                        modelList.add(dict2.getValue());
                    }
                }
                for (ConfigDict dict3 : idcTypeList3) {
                    String PID = dict3.getPid();
                    if (PID.equals(ID)) {
                        String object = dict3.getValue();
                        if (deviceClassArray.contains(object)) {
                            object = object + "re";
                        }
                        String replace2 = object.replace('（', 'l');
                        String replace = replace2.replace('）', 'r');
                        if (!typeList.contains(replace)) {
                            typeList.add(replace);
                        }
                    }
                }

                hashMap.put("DEVICE_CLASS", dict1.getValue());
                hashMap.put("DEVICE_MODEL", modelList);
                hashMap.put("DEVICE_TYPE", typeList);
                modelLinkedList.add(hashMap);
            }
        }
        // 去重设备分类
        List<String> newList1 = new ArrayList<>();
        for (Map<String, Object> map : modelLinkedList) {
            String device_class = (String) map.get("DEVICE_CLASS");
            if (!newList1.contains(device_class)) {
                newList1.add(device_class);
            }
        }

        String[] tempList = newList1.toArray(new String[0]);
        list1.add(tempList);
        list1.add("deviceClassLists");
        list2.add(modelLinkedList);
        list2.add("deviceTypeLists");
        list3.add(modelLinkedList);
        list3.add("deviceTypeLists");
    }

    /**
     * 返回驱动数据
     * @param list1 第1种类型的数据
     * @param label1 第1种类型
     * @param idcType 需要查询的字典表类型
     */
    private List<String> getDriverTypeList(List<Object> list1,String label1,String idcType) {
        String[] tempList = null;
        List<String> stringList1 = new ArrayList<>();
        List<ConfigDict> configDictList = configDictMapper.listByEntity(new ConfigDict(idcType));
        List<String> driverTypeValues = Lists.newArrayList();
        if (!configDictList.isEmpty()) {
            for (ConfigDict dict : configDictList) {
                String osTypeValue = dict.getName();
                stringList1.add(osTypeValue);
                if (!osTypeValue.toLowerCase().contains("windows")) {
                    driverTypeValues.add(osTypeValue);
                }
            }
            tempList = stringList1.toArray(new String[0]);
            list1.add(tempList);
            list1.add(label1);
        }
        return driverTypeValues;
    }

    /**
     * 构建其他信息
     * @param list 需要返回的数据
     * @param arr 固定值数组
     * @param label 设置的类型
     */
    private void buildOtherInfo (List<Object> list,String[] arr,String label) {
        list.add(arr);
        list.add(label);
    }

    /**
     * 构建导出业务线的excel数据
     * @param business1 一级业务线
     * @param business2 二级业务线
     */
    private void buildBusinessInfo(List<Object> business1,List<Object> business2) {
        List<Map<String, Object>> businessesList = Lists.newArrayList(); // 一级业务(必填),二级业务
        List<String> businessArray = Lists.newArrayList();
        List<CmdbBusinessLine> businesses = cmdbBusinessLineMapper.getAllBusiness("1"); // 所有的独立业务
        Map<String, List<CmdbBusinessLine>> subBusinessMap = buildSubCmdbBusinessMap(); // 所有的独立业务子模块Map
        businesses.forEach(cmdbBusiness -> {
            cmdbBusiness.setBusinessName(StringUtils.replaceChineseCharacter(cmdbBusiness.getBusinessName()));
            List<String> business2List = new ArrayList<>();
            Map<String, Object> hashMap = new HashMap<>();
            String pID = cmdbBusiness.getId();
            String businessName1 = cmdbBusiness.getBusinessName();
            List<CmdbBusinessLine> businesses2 = subBusinessMap.get(pID);
            if (null == businesses2 || businesses2.isEmpty()) {// 如何没有二级业务的话，保留一级业务
                businessArray.add(businessName1);
                business2List.add(null);
            } else {
                businesses2.forEach(cmdbBusiness2 -> {
                    businessArray.add(businessName1);
                    business2List.add(StringUtils.replaceChineseCharacter(cmdbBusiness2.getBusinessName()));
                });
            }
            hashMap.put("BUSINESS_LEVEL1", businessName1);
            hashMap.put("BUSINESS_LEVEL2", business2List);
            businessesList.add(hashMap);
        });
        List<String> newList = businessArray.stream().distinct().collect(Collectors.toList()); // 独立业务去重
        buildOtherInfo(business1,newList.toArray(new String[0]),"business1");
        business2.add(businessesList);
        business2.add("business2");
    }

    public String[] buildAssetExcelHeader() {
        String[] headerList = { "管理IP(必填)", "独立业务(必填)", "独立业务子模块", "IDC位置(必填)", "所属位置(必填)", "机房位置", "设备分类(必填)", "设备类型(必填)",
                "设备型号(必填)", "设备系统类型(必填)", "设备网络层级", "应用模块", "设备状态(必填)", "是否ansible管理(必填)", "是否资源池管理设备(必填)",
                "是否需监控(zabbix agent/snmp)(必填)", "是否需自动化纳管(agent/账号)(必填)", "是否需日志采集(filebeat agent)(必填)", "filebeat日志采集目录",
                "自动化ProxyIp", "主备关系", "资源计划性", "VM创建时间", "项目归属(必填)", "业务IP1", "业务IP2", "其它IP地址", "逻辑名", "机柜号", "刀箱号",
                "槽位号", "刀箱管理IP", "所在宿主机IP", "承载虚拟机名称", "承载虚拟机IP", "设备规格", "序列号(必填)", "板卡序列号", "资产标签号（未转资设备录入为“未转资”）(必填)",
                "分布式存储挂载目录", "是否购买维保(必填项)", "出保时间(格式：xxxx/xx/xx)(购买维保必填项)", "是否需原厂维保(购买维保必填项)", "原厂维保购买必要性(购买原厂维保必填项)",
                "维保厂家（此格只可查看）", "维保供应商联系方式（格式：XXX/*****）（此格只可查看）", "维保厂家联系方式（此格只可查看）", "本期维保起始时间（此格只可查看）",
                "本期维保结束时间（此格只可查看）", "实际购买维保类型（此格只可查看）", "维保管理人（格式：XXX/*****）（此格只可查看）", "上线时间(格式：xxxx/xx/xx)", "console IP",
                "console vlan", "console 掩码", "console 网关", "console 账号", "console 密码", "业务 vlan", "本地磁盘大小", "网络区域", "备注",
                "转资成本", "单价", "按比例分摊日期(格式：xxxx/xx/xx)", "使用年限", "设备风险等级", "设备厂家(必填)", "nat前IP", "资源驱动信息", "域名称", "所属域控制器", "IP启用类型(必填)", "IPv6地址", "责任人",
                "账号自学习", "管理员账号", "管理员密码", "管理员提示符", "用户提示符", "同步账号名称", "同步账号密码", "网络设备资源定义方式", "网络设备登录密码", "网络设备使能命令",
                "网络设备使能密码", "设备登录成功提示符", "使能主账号登录成功提示符", "从帐号同步服务器", "堡垒主机名称", "通讯方式对应登陆类型", "资源制造商标识", "产品系列标识", "资源版本号信息",
                "连接主机端口", "域DN", "通信端口号", "管理协议", "管理端口", "归属域", "资源目录标识", "主机名", "管理方式", "是否最大链接数", "最大链接数"};
        return headerList;
    }

    /**
     * 构建所有的独立业务子模块Map数据
     */
    private Map<String,List<CmdbBusinessLine>> buildSubCmdbBusinessMap() {
        Map<String,List<CmdbBusinessLine>> retMap = new HashMap<>();
        List<CmdbBusinessLine> businessList = cmdbBusinessLineMapper.getAllBusiness(""); // 所有的独立业务子模块
        if (!businessList.isEmpty()) {
            businessList.forEach(business -> {
                String pid = business.getParentId();
                List<CmdbBusinessLine> cmdbBusinessLines = retMap.get(pid);
                if (null == cmdbBusinessLines) {
                    cmdbBusinessLines = new ArrayList<>();
                }
                cmdbBusinessLines.add(business);
                retMap.put(pid,cmdbBusinessLines);
            });
        }
        return retMap;
    }
}
