package com.aspire.ums.cmdb.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aspire.ums.cmdb.common.BpmAccessTokenUtil;
import com.aspire.ums.cmdb.common.StringUtils;
import com.aspire.ums.cmdb.sync.client.CmdbServiceClient;
import com.aspire.ums.cmdb.sync.entity.EIPDept;
import com.aspire.ums.cmdb.sync.service.EipSyncService;
import com.aspire.ums.cmdb.sync.util.HttpUtils;
import com.aspire.ums.cmdb.systemManager.payload.OrgManager;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
@ConditionalOnExpression("${schedule.eipDept.flag:false}")
public class EipDeptSyncTaskService {

    private static final Logger logger = LoggerFactory.getLogger(EipDeptSyncTaskService.class);

    @Value("${eip.sync.dept.url}")
    private String deptInfoListGetUrl;
    @Autowired
    private CmdbServiceClient cmdbServiceClient;

    private static final String KEY_DEPTS_ADD = "addList";
    private static final String KEY_DEPTS_MODI = "modiList";
    private static final String KEY_DEPTS_DEL = "delList";

    @Resource(name = "eipDeptSyncToBpmService")
    private EipSyncService<EIPDept> eipDeptSyncToBpmService;
    @Resource(name = "eipDeptSyncToUmsService")
    private EipSyncService<EIPDept> eipDeptSyncToUmsService;

    @Value("${uc.org.manage.facade}")
    private String BPM_TOKEN_URL;
    @Value("${uc.org.manage.token.username}")
    private String BPM_TOKEN_USER;
    @Value("${uc.org.manage.token.password}")
    private String BPM_TOKEN_PASSWORD;

    @Scheduled(cron = "${eip.sync.dept.cron}")
    public void syncEIPDept() {
        String deptListStr = HttpUtils.httpGet(deptInfoListGetUrl);
        if (StringUtils.isNotEmpty(deptListStr)) {
            ArrayList<EIPDept> deptInfoList = JSON.parseObject(deptListStr, new TypeReference<ArrayList<EIPDept>>(){});
            Map<String, List<EIPDept>> deptGroup = groupEipDept(deptInfoList);
            // 同步到 UMS
            syncUms(deptGroup);
            // 同步到 BPM
            syncBpm(deptGroup);
        }
    }

    private void syncUms(Map<String, List<EIPDept>> deptGroup) {
        eipDeptSyncToUmsService.add(deptGroup.get(KEY_DEPTS_ADD));
        eipDeptSyncToUmsService.modify(deptGroup.get(KEY_DEPTS_MODI));
        eipDeptSyncToUmsService.delete(deptGroup.get(KEY_DEPTS_DEL));
    }

    private void syncBpm(Map<String, List<EIPDept>> deptGroup) {
        String accessToken = BpmAccessTokenUtil.genHttpAccessToken(BPM_TOKEN_URL, BPM_TOKEN_USER, BPM_TOKEN_PASSWORD);
        if (StringUtils.isNotEmpty(accessToken)) {
            eipDeptSyncToBpmService.add(deptGroup.get(KEY_DEPTS_ADD));
            eipDeptSyncToBpmService.modify(deptGroup.get(KEY_DEPTS_MODI));
            eipDeptSyncToBpmService.delete(deptGroup.get(KEY_DEPTS_DEL));
        } else {
            logger.error("获取BPM访问token失败! 无法同步BPM部门数据!");
        }
    }

    private Map<String, List<EIPDept>> groupEipDept(List<EIPDept> deptInfoList) {

        Map<String, List<EIPDept>> map = new HashMap<>();
        List<EIPDept> addList = Lists.newArrayList();
        List<EIPDept> modiList = Lists.newArrayList();
        List<EIPDept> delList = Lists.newArrayList();

        List<OrgManager> eipOrgList = cmdbServiceClient.getAllEipOrg();
        if (CollectionUtils.isNotEmpty(eipOrgList)) {
            Map<String, OrgManager> cnmsDeptIdMap = new HashMap<>(eipOrgList.size());
            for (OrgManager org : eipOrgList) {
                cnmsDeptIdMap.put(org.getId(), org);
            }
            Set<String> eipDeptIdSet = new HashSet<>();
            for (EIPDept eipDept : deptInfoList) {
                String deptName = eipDept.getDeptName();
                String deptId = eipDept.getDeptId();
                eipDeptIdSet.add(deptId);
                if (cnmsDeptIdMap.containsKey(deptId)) { // 如果已存在该组织, 则修改 (密码不作修改)
                    modiList.add(eipDept);
                } else { // 如果不存在该组织, 则新增
                    addList.add(eipDept);
                }
            }
            // 过滤已删除的组织
            for (OrgManager orgDept : eipOrgList) {
                if (!eipDeptIdSet.contains(orgDept.getId())) {
                    EIPDept dept = new EIPDept();
                    dept.setDeptId(orgDept.getId());
                    dept.setDeptName(orgDept.getOrgName());
                    delList.add(dept);
                }
            }
        } else { // 当查出cmdb数据列表为空时，直接进行新增组织操作
            addList.addAll(deptInfoList);
        }
        map.put(KEY_DEPTS_ADD, addList);
        map.put(KEY_DEPTS_MODI, modiList);
        map.put(KEY_DEPTS_DEL, delList);
        return map;
    }
}
