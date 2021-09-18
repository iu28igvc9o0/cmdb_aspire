package com.aspire.mirror.ops.controller;

import com.aspire.mirror.ops.api.domain.*;
import com.aspire.mirror.ops.api.service.IOpsGroupService;
import com.aspire.mirror.ops.biz.OpsGroupBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分组管理控制层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.controller
 * 类名称:    OpsGroupController.java
 * 类描述:    分组管理控制层
 * 创建人:    JinSu
 * 创建时间:  2020/3/5 15:28
 * 版本:      v1.0
 */
@RestController
@Slf4j
public class OpsGroupController implements IOpsGroupService {
    @Autowired
    private OpsGroupBiz opsGroupBiz;

    @Override
    public List<OpsGroup> queryGroupTree(@RequestBody OpsGroup opsGroup) {
        if (opsGroup == null) {
            log.warn("query group param is null");
            return null;
        }
        opsGroup.setTop(true);
        return opsGroupBiz.queryGroupTree(opsGroup);
    }

    @Override
    public GeneralResponse saveGroup(@RequestBody OpsGroup opsGroup) {
        if (opsGroup == null) {
            log.warn("save group param is null");
            return new GeneralResponse(false);
        }
        return opsGroupBiz.saveGroup(opsGroup);
    }

    @Override
    public GeneralResponse deleteGroup(@RequestParam("group_id") Long groupId) {
        if (groupId == null) {
            log.warn("delete group param is null");
            return new GeneralResponse(false, "删除分组数据参数分组ID不允许为空");
        }
        return opsGroupBiz.deleteGroup(groupId);
    }

    @Override
    public GeneralResponse saveBatchGroupRelation(@RequestBody GroupRelationBatchAddReq groupRelationBatchAddReq) {
        if (CollectionUtils.isEmpty(groupRelationBatchAddReq.getGroupRelationList())) {
            log.warn("pending add group relation data is empty");
            return new GeneralResponse(true);
        }
        return opsGroupBiz.saveBatchGroupRelation(groupRelationBatchAddReq);
    }

    @Override
    public PageListQueryResult<GroupRelationDetail> querGroupRelationList(@RequestBody GroupRelationQueryModel groupRelationQueryModel) {
        return opsGroupBiz.querGroupRelationList(groupRelationQueryModel);
    }

    @Override
    public PageListQueryResult<GroupObjectDetail> queryObjectList(@RequestBody GroupRelationQueryModel groupRelationQueryModel) {
        return opsGroupBiz.queryObjectList(groupRelationQueryModel);
    }

    @Override
    public GeneralResponse deleteGroupRelation(@RequestParam("group_relation_ids") String groupRelationIds) {
        if (StringUtils.isEmpty(groupRelationIds)) {
            log.warn("delete group relation param is empty");
            return new GeneralResponse(false, "新增分组关系数据参数不能为空");
        }
        return  opsGroupBiz.deleteGroupRelation(groupRelationIds);
    }

    @Override
    public List<OpsResource> querOpsResourceTree(@RequestParam("objectType") String objectType) {
        return opsGroupBiz.querOpsResourceTree(objectType);
    }

    @Override
    public List<String> queryAllChildGroup(@RequestBody ChildGroupQueryModel childGroupQueryModel) {
        return opsGroupBiz.queryAllChildGroup(childGroupQueryModel);
    }
}
