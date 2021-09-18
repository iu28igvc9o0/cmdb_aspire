package com.aspire.mirror.log.server.controller;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.log.api.dto.ConfigCompareLogsResp;
import com.aspire.mirror.log.api.dto.ConfigCompareReq;
import com.aspire.mirror.log.api.dto.ConfigCompareResp;
import com.aspire.mirror.log.api.dto.ConfigDataResponse;
import com.aspire.mirror.log.api.service.IConfigCompareService;
import com.aspire.mirror.log.server.biz.ConfigCompareBiz;
import com.aspire.mirror.log.server.biz.ConfigDataBiz;
import com.aspire.mirror.log.server.biz.FtpService;
import com.aspire.mirror.log.server.dao.po.ConfigCompare;
import com.aspire.mirror.log.server.dao.po.ConfigCompareLogs;
import com.aspire.mirror.log.server.util.PayloadParseUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.*;

/**
 * @author baiwenping
 * @Title: ConfigCompareController
 * @Package com.aspire.mirror.log.server.controller
 * @Description: TODO
 * @date 2020/12/21 10:30
 */
@Slf4j
@RestController
public class ConfigCompareController implements IConfigCompareService {

    @Autowired
    private ConfigCompareBiz configCompareBiz;

    @Autowired
    private ConfigDataBiz configDataBiz;

    @Autowired
    private FtpService ftpService;

    /**
     * 获取比对列表
     *
     * @param masterIp
     * @param backupIp
     * @param compareTimeFrom
     * @param compareTimeTo
     * @param compareType
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResponse<ConfigCompareResp> getCompareList(@RequestParam(name = "masterIp",required = false) String masterIp,
                                                          @RequestParam(name = "backupIp",required = false) String backupIp,
                                                          @RequestParam(name = "compareTimeFrom",required = false) String compareTimeFrom,
                                                          @RequestParam(name = "compareTimeTo",required = false) String compareTimeTo,
                                                          @RequestParam(name = "compareType",required = false) String compareType,
                                                          @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                                          @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize) {
        PageResponse<ConfigCompare> comparePage = configCompareBiz.getCompareList(masterIp, backupIp, compareTimeFrom, compareTimeTo, compareType, pageNum, pageSize);
        PageResponse<ConfigCompareResp> pageResponse = new PageResponse<>();
        pageResponse.setCount(comparePage.getCount());
        pageResponse.setResult(PayloadParseUtil.jacksonBaseParse(ConfigCompareResp.class, comparePage.getResult()));
        return pageResponse;
    }

    /**
     * 导出比对清单
     *
     * @param ids
     * @return
     */
    @Override
    public List<ConfigCompareResp> exportCompareList(@RequestBody List<Integer> ids) {
        return PayloadParseUtil.jacksonBaseParse(ConfigCompareResp.class, configCompareBiz.getCompareListByIds(ids));
    }

    /**
     * 比对
     *
     * @param id
     * @param masterIndex
     * @param backupIndex
     * @return
     */
    @Override
    public Map<String, Object> compare(@PathVariable("id") Integer id,
                                       @RequestParam(name = "username") String username,
                                     @RequestParam(name = "masterIndex") String masterIndex,
                                     @RequestParam(name = "backupIndex") String backupIndex) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("flag", false);
        ConfigCompare configCompare = configCompareBiz.getCompareById(id);
        if (configCompare == null) {
            map.put("message", "主备设备配置文件不存在");
            return map;
        }
        String brand = configCompare.getBrand();

        ConfigDataResponse masterConfig = configDataBiz.getConfigByIp(masterIndex, configCompare.getIdcType(), configCompare.getMasterIp());
        if (masterConfig == null) {
            map.put("message", "主设备配置文件为空");
            return map;
        }
        ConfigDataResponse backupConfig = configDataBiz.getConfigByIp(masterIndex, configCompare.getIdcType(), configCompare.getBackupIp());
        if (backupConfig == null) {
            map.put("message", "备份设备配置文件为空");
            return map;
        }

        // 目前只做华为比对
        if ("华为".equalsIgnoreCase(brand)) {
            String masterMassage = masterConfig.getMassage();
            masterMassage = masterMassage.replaceAll("\\s{2}---.*?\\[42D.*?\\[42D","");
            String backupMassage = backupConfig.getMassage();
            backupMassage = backupMassage.replaceAll("\\s{2}---.*?\\[42D.*?\\[42D","");
            Map<String, Map<String, String>> masterSecurityPolicys = Maps.newHashMap();
            Map<String, Map<String, String>> masterNatPolicys = Maps.newHashMap();
            Set<String> masterRouteDataList = Sets.newHashSet();
            generateSetsData(masterMassage, masterSecurityPolicys, masterNatPolicys, masterRouteDataList);
            Map<String, Map<String, String>> backupSecurityPolicys = Maps.newHashMap();
            Map<String, Map<String, String>> backupNatPolicys = Maps.newHashMap();
            Set<String> backupRouteDataList = Sets.newHashSet();
            generateSetsData(backupMassage, backupSecurityPolicys, backupNatPolicys, backupRouteDataList);
            List<Map<String, String>> addDatas = Lists.newArrayList();
            List<Map<String, String>> modifyDatas = Lists.newArrayList();
            List<Map<String, String>> delDatas = Lists.newArrayList();
            // 判断安全策略
            comparePolicy(masterSecurityPolicys, backupSecurityPolicys, "security_policy", addDatas, modifyDatas, delDatas);

            comparePolicy(masterNatPolicys, backupNatPolicys, "nat_policy", addDatas, modifyDatas, delDatas);

            compareRoute(masterRouteDataList, backupRouteDataList, addDatas, modifyDatas, delDatas);

            // 上传配置文件到FTP
            // TODO
            ByteArrayInputStream in = null;
            String hHmmss = DateUtil.formatDate("HHmmss");
            try {
                String masterFileName = masterIndex + "_" + configCompare.getMasterIp() + "_" + hHmmss + ".txt";
                File f = new File(masterFileName);
                in = new ByteArrayInputStream(masterMassage.getBytes());
                Map<String, Object> map1 = ftpService.uploadtoFTPNew(masterFileName, in, "config-network");
                configCompare.setMasterConfigFile(MapUtils.getString(map1, "path"));
            } catch (Exception e) {
            }finally {
                IOUtils.closeQuietly(in);
            }

            try {
                String backupFileName = backupIndex  + "_" + configCompare.getBackupIp() + "_" + hHmmss + ".txt";
                in = new ByteArrayInputStream(backupMassage.getBytes());
                Map<String, Object> map1 = ftpService.uploadtoFTPNew(backupFileName, in, "config-network");
                configCompare.setBackupConfigFile(MapUtils.getString(map1, "path"));
            } catch (Exception e) {
            }finally {
                IOUtils.closeQuietly(in);
            }

            // 插入数据
            configCompare.setAddCount(addDatas.size());
            configCompare.setModifyCount(modifyDatas.size());
            configCompare.setDelCount(delDatas.size());
            configCompare.setAddDatas(JSON.toJSONString(addDatas));
            configCompare.setModifyDatas(JSON.toJSONString(modifyDatas));
            configCompare.setDelDatas(JSON.toJSONString(delDatas));
            Date now = new Date();
            configCompare.setCompareTime(now);
            configCompareBiz.update(configCompare);

            ConfigCompareLogs configCompareLogs = new ConfigCompareLogs();
            configCompareLogs.setCompareId(configCompare.getId());
            configCompareLogs.setMasterIp(configCompare.getMasterIp());
            configCompareLogs.setBackupIp(configCompare.getBackupIp());
            configCompareLogs.setMasterConfigFile(configCompare.getMasterConfigFile());
            configCompareLogs.setBackupConfigFile(configCompare.getBackupConfigFile());
            configCompareLogs.setIdcType(configCompare.getIdcType());
            configCompareLogs.setCompareTime(configCompare.getCompareTime());
            configCompareLogs.setCompareUser(username);
            StringBuilder addSb = new StringBuilder();
            for (Map<String, String> addData: addDatas) {
                String masterName = addData.get("master_name");
                if (StringUtils.isNotEmpty(masterName)) {
                    addSb.append("策略名称:").append(masterName).append(";");
                }
                addSb.append("策略内容:").append(addData.get("master_content")).append(";");
            }
            configCompareLogs.setAddResult(addSb.toString());
            StringBuilder modifySb = new StringBuilder();
            for (Map<String, String> modifyData: modifyDatas) {
                modifySb.append(modifyData.get("compare_result"));
            }
            configCompareLogs.setModifyResult(modifySb.toString());
            StringBuilder delSb = new StringBuilder();
            for (Map<String, String> delData: delDatas) {
                String masterName = delData.get("backup_name");
                if (StringUtils.isNotEmpty(masterName)) {
                    delSb.append("策略名称:").append(masterName).append(";");
                }
                delSb.append("策略内容:").append(delData.get("backup_content")).append(".");
            }
            configCompareLogs.setDelResult(delSb.toString());
            configCompareBiz.insertLogs(configCompareLogs);
        } else {
            // PASS
        }



        map.put("flag", true);
        return map;
    }



    private void comparePolicy (Map<String, Map<String, String>> masterSecurityDatas,
                                Map<String, Map<String, String>> backupSecurityDatas,
                                String policyType,
                                List<Map<String, String>> addDatas,
                                List<Map<String, String>> modifyDatas,
                                List<Map<String, String>> delDatas) {
        if (masterSecurityDatas.isEmpty() && backupSecurityDatas.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Map<String, String>> entry: masterSecurityDatas.entrySet()) {
            String sameName = entry.getKey();
            Map<String, String> masterPolicy = entry.getValue();
            // 过滤掉disable的策略
            if ("disable".equalsIgnoreCase(masterPolicy.get("active"))) {
                continue;
            }
            Map<String, String> backupPolicy = backupSecurityDatas.get(sameName);
            if (backupPolicy != null && !"disable".equalsIgnoreCase(backupPolicy.get("active")) ) {
                boolean checkFlag = false;
                List<String> differentList = Lists.newArrayList();
                for (Map.Entry<String, String> masterPolicyEntry: masterPolicy.entrySet()) {
                    String key = masterPolicyEntry.getKey();
                    String value = masterPolicyEntry.getValue();
                    String backupValue = backupPolicy.get(key);
                    if (StringUtils.isNotEmpty(value) && !value.equalsIgnoreCase(backupValue)) {
                        checkFlag = true;
                        String difference = String.format("主策略内容已更新为%s,备策略内容是%s", value, backupValue);
                        differentList.add(difference);
                    }
                }
                if (checkFlag) {
                    Map<String, String> difference = Maps.newHashMap();
                    difference.put("master_name", masterPolicy.get("name"));
                    difference.put("master_content", masterPolicy.get("content"));
                    difference.put("backup_name", backupPolicy.get("name"));
                    difference.put("backup_content", backupPolicy.get("content"));
                    difference.put("policyType", policyType);
                    difference.put("compare_result", Joiner.on(";").join(differentList));
                    modifyDatas.add(difference);
                }

            } else {
                Map<String, String> difference = Maps.newHashMap();
                difference.put("master_name", masterPolicy.get("name"));
                difference.put("master_content", masterPolicy.get("content"));
                difference.put("policyType", policyType);
                difference.put("compare_result", "未同步");
                addDatas.add(difference);
            }
        }

        for (Map.Entry<String, Map<String, String>> entry: backupSecurityDatas.entrySet()) {
            String sameName = entry.getKey();
            Map<String, String> backupPolicy = entry.getValue();
            // 过滤掉disable的策略
            if ("disable".equalsIgnoreCase(backupPolicy.get("active"))) {
                continue;
            }
            Map<String, String> masterPolicy = masterSecurityDatas.get(sameName);
            if (backupPolicy == null || "disable".equalsIgnoreCase(backupPolicy.get("active"))) {
                Map<String, String> difference = Maps.newHashMap();
                difference.put("backup_name", backupPolicy.get("name"));
                difference.put("backup_content", backupPolicy.get("content"));
                difference.put("policyType", policyType);
                difference.put("compare_result", String.format("主策略%s已删除，备策略未同步删除", sameName));
                delDatas.add(difference);
            }
        }
    }

    private void compareRoute (Set<String> masterRouteDatas, Set<String> backupRouteDatas,
                               List<Map<String, String>> addDatas,
                               List<Map<String, String>> modifyDatas,
                               List<Map<String, String>> delDatas) {
        if (masterRouteDatas.isEmpty() && backupRouteDatas.isEmpty()) {
            return;
        }

        for (String masterRoute: masterRouteDatas) {
            if (!backupRouteDatas.contains(masterRoute)) {
                Map<String, String> difference = Maps.newHashMap();
                difference.put("master_content", masterRoute);
                difference.put("policyType", "ip_route");
                difference.put("compare_result", "未同步");
                addDatas.add(difference);
            }
        }
        for (String masterRoute: backupRouteDatas) {
            if (!masterRouteDatas.contains(masterRoute)) {
                Map<String, String> difference = Maps.newHashMap();
                difference.put("backup_content", masterRoute);
                difference.put("policyType", "ip_route");
                difference.put("compare_result", "未同步");
                addDatas.add(difference);
            }
        }
    }

    private void generateSetsData(String mssage,
                                  Map<String, Map<String, String>> securityPolicys,
                                  Map<String, Map<String, String>> natPolicys,
                                  Set<String> routeDataList) {
        String[] lines = mssage.split("\n");
        if (securityPolicys == null) {
            securityPolicys = Maps.newHashMap();
        }
        if (natPolicys == null) {
            natPolicys = Maps.newHashMap();
        }
        if (routeDataList == null) {
            routeDataList = Sets.newHashSet();
        }
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.startsWith("security-policy")) {
                securityPolicys.putAll(getAllPolicys(i, lines));
            } else if (line.startsWith("nat-policy")) {
                natPolicys.putAll(getAllPolicys(i, lines));
            } else if (line.startsWith("ip route-static")) {
                routeDataList.add(line);
            }
        }
    }

    private Map<String, Map<String, String>> getAllPolicys (int i, String[] lines) {
        Map<String, Map<String, String>> map = Maps.newHashMap();
        String name = "";
        Set<String> disableNameSet = Sets.newHashSet();
        for (; i < lines.length; i++) {
            String line = lines[i].trim();
            if (StringUtils.isEmpty(line)) {
                continue;
            } else if (line.startsWith("rule")) {
                String[] s = line.split(" ");
                if (s.length >2) {
                    name = line.split(" ")[2];
                    Map<String, String> policyMap = Maps.newHashMap();
                    policyMap.put("name", name);
                    policyMap.put("content", "");
                    map.put(name, policyMap);
                } else {
                    name = "";
                }
            } else if (line.startsWith("disable")) {
                disableNameSet.add(name);
            } else if (line.startsWith("description")) {
                Map<String, String> policyMap = map.get(name);
                if (policyMap == null) {
                    policyMap = Maps.newHashMap();
                    policyMap.put("name", name);
                }
                if (line.contains(" ")) {
                    policyMap.put("description",line.substring(line.indexOf(" ") + 1));
                }
                String content = policyMap.get("content") == null ? line : policyMap.get("content") + "," + line;
                policyMap.put("content", content);
            } else if (line.startsWith("source-zone")) {
                Map<String, String> policyMap = map.get(name);
                if (policyMap == null) {
                    policyMap = Maps.newHashMap();
                    policyMap.put("name", name);
                }
                if (line.contains(" ")) {
                    policyMap.put("source-zone",line.substring(line.indexOf(" ") + 1));
                }
                String content = policyMap.get("content") == null ? line : policyMap.get("content") + "," + line;
                policyMap.put("content", content);
            } else if (line.startsWith("destination-zone")) {
                Map<String, String> policyMap = map.get(name);
                if (policyMap == null) {
                    policyMap = Maps.newHashMap();
                    policyMap.put("name", name);
                }
                if (line.contains(" ")) {
                    policyMap.put("destination-zone",line.substring(line.indexOf(" ") + 1));
                }
                String content = policyMap.get("content") == null ? line : policyMap.get("content") + "," + line;
                policyMap.put("content", content);
            } else if (line.startsWith("destination-address")) {
                Map<String, String> policyMap = map.get(name);
                if (policyMap == null) {
                    policyMap = Maps.newHashMap();
                    policyMap.put("name", name);
                }
                if (line.contains(" ")) {
                    policyMap.put("destination-address",line.substring(line.indexOf(" ") + 1));
                }
                String content = policyMap.get("content") == null ? line : policyMap.get("content") + "," + line;
                policyMap.put("content", content);
            } else if (line.startsWith("source-address")) {
                Map<String, String> policyMap = map.get(name);
                if (policyMap == null) {
                    policyMap = Maps.newHashMap();
                    policyMap.put("name", name);
                }
                if (line.contains(" ")) {
                    policyMap.put("source-address",line.substring(line.indexOf(" ") + 1));
                }
                String content = policyMap.get("content") == null ? line : policyMap.get("content") + "," + line;
                policyMap.put("content", content);
            } else if (line.startsWith("service")) {
                Map<String, String> policyMap = map.get(name);
                if (policyMap == null) {
                    policyMap = Maps.newHashMap();
                    policyMap.put("name", name);
                }
                if (line.contains(" ")) {
                    policyMap.put("service",line.substring(line.indexOf(" ") + 1));
                }
                String content = policyMap.get("content") == null ? line : policyMap.get("content") + "," + line;
                policyMap.put("content", content);
            } else if (line.startsWith("action")) {
                Map<String, String> policyMap = map.get(name);
                if (policyMap == null) {
                    policyMap = Maps.newHashMap();
                    policyMap.put("name", name);
                }
                if (line.contains(" ")) {
                    policyMap.put("action",line.substring(line.indexOf(" ") + 1));
                }
                String content = policyMap.get("content") == null ? line : policyMap.get("content") + "," + line;
                policyMap.put("content", content);
            } else if (line.startsWith("#")) {
                break;
            }
        }

        for (String disableName: disableNameSet) {
            map.remove(disableName);
        }

        return map;
    }
    /**
     * 新增
     * @param configCompare
     */
    public void insert(@RequestBody ConfigCompareReq configCompare) {
        if (configCompare == null
        || StringUtils.isEmpty(configCompare.getMasterIp())
        || StringUtils.isEmpty(configCompare.getBackupIp())) {
            throw new RuntimeException("ip not exist!");
        }
        ConfigCompare compare = PayloadParseUtil.jacksonBaseParse(ConfigCompare.class, configCompare);
        compare.setCreateTime(new Date());
        configCompareBiz.insert(compare);
    }

    /**
     * 导入比对列表
     *
     * @param configCompareList
     * @return
     */
    @Override
    public void importFile(@RequestBody List<ConfigCompareReq> configCompareList) {
        List<ConfigCompare> configCompares = PayloadParseUtil.jacksonBaseParse(ConfigCompare.class, configCompareList);
        Date date = new Date();
        for (ConfigCompare configCompare: configCompares) {
            configCompare.setCreateTime(date);
        }
        configCompareBiz.insertBatch(configCompares);
    }

    /**
     * 获取比对记录
     *
     * @param compareId
     * @return
     */
    @Override
    public List<ConfigCompareLogsResp> getLogs(@PathVariable("compare_id") Integer compareId) {
        return PayloadParseUtil.jacksonBaseParse(ConfigCompareLogsResp.class, configCompareBiz.getLogs(compareId));
    }

    /**
     * 获取索引列表
     * @param compareId
     * @return
     */
    public Map<String, Object> getIndex(@PathVariable("compare_id") Integer compareId) {
        Map<String, Object> map = Maps.newHashMap();
        ConfigCompare configCompare = configCompareBiz.getCompareById(compareId);
        if (configCompare == null) {
            map.put("message", "主备设备配置文件不存在");
            return map;
        }
        map.put("masterIndex", configDataBiz.getIndexByIp(configCompare.getIdcType(), configCompare.getMasterIp()));
        map.put("backupIndex", configDataBiz.getIndexByIp(configCompare.getIdcType(), configCompare.getBackupIp()));
        return map;
    }
}
