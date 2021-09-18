package com.aspire.ums.cmdb.v2.instance.handler.serverProject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.aspire.ums.cmdb.util.DateUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * 网络线路汇总插入后置事件-保存历史记录
 *
 * @author jiangxuwen
 * @date 2021/2/4 13:43
 */
@Slf4j
public class NetworkLineInsertHisHandler extends AbstractServerProjectInsertFactory {

    @Override
    public void createServerProjectData(String userName, Map<String, Object> instanceData, String operateType) {
        createNetworkLineHis(userName, instanceData, operateType);
    }

    private void createNetworkLineHis(String userName, Map<String, Object> serverCabinetData, String operateType) {
        List<Map<String, Object>> serverProjectList = new ArrayList<>();
        List<Map<String, Object>> networkLineHisList = new ArrayList<>();

        Map<String, Object> serverProject = Maps.newHashMap();
        Map<String, Object> networkLineHis = Maps.newHashMap();
        String id = UUIDUtil.getUUID();
        // 网络线路 申请记录moduleId
        String moduleId = "c0eca8917b434b109b84e490c76aae67";
        CmdbConfig serverProjectModule = cmdbConfigService.getConfigByCode("moduleId:networkLineHistory");
        if (serverProjectModule != null && StringUtils.isNotBlank(serverProjectModule.getConfigValue())) {
            moduleId = serverProjectModule.getConfigValue();
        }
        serverProject.put("id", id);
        // TODO:修改moduleId
        serverProject.put("module_id", moduleId);
        serverProject.put("insert_person", userName);
        serverProject.put("insert_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        serverProject.put("update_person", userName);
        serverProject.put("update_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        serverProjectList.add(serverProject);

        networkLineHis.putAll(serverCabinetData);
        networkLineHis.put("id", id);
        networkLineHisList.add(networkLineHis);

        cmicInstanceService.batchInsertServerProject(serverProjectList);
        cmicInstanceService.batchInsertNetworkLineHis(networkLineHisList);
    }

}
