package com.aspire.mirror.ops.biz;

import com.aspire.mirror.ops.api.domain.*;
import com.aspire.mirror.ops.controller.authcontext.RequestAuthContext;
import com.aspire.mirror.ops.dao.OpsGroupDao;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分组管理业务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.biz
 * 类名称:    OpsGroupBiz.java
 * 类描述:    分组管理业务
 * 创建人:    JinSu
 * 创建时间:  2020/3/5 15:32
 * 版本:      v1.0
 */
@Service
@Transactional
public class OpsGroupBiz {

    @Autowired
    private OpsGroupDao opsGroupDao;

    public List<OpsGroup> queryGroupTree(OpsGroup opsGroup) {
        List<String> authGroupIdList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(opsGroup.getAuthGroupIdList())) {
            // authGroupIdList存放有权限的分组
            authGroupIdList.addAll(opsGroup.getAuthGroupIdList());
            List<String> tempGroupIdList = Lists.newArrayList();
            tempGroupIdList.addAll(opsGroup.getAuthGroupIdList());
//            for (String groupId : opsGroup.getAuthGroupIdList()) {
//                addChildGroup(tempGroupIdList, Long.valueOf(groupId), authGroupIdList);
//            }
            for (String groupId : opsGroup.getAuthGroupIdList()) {
                addParentGroup(tempGroupIdList, Long.valueOf(groupId));
            }
            opsGroup.setAuthGroupIdList(tempGroupIdList);
        }
        List<OpsGroup> opsGroupList = opsGroupDao.queryGroupList(opsGroup);
        getTree(opsGroupList, opsGroup.getAuthGroupIdList(), authGroupIdList);
        opsGroupList.get(0).setAuthGroupIdList(authGroupIdList);
        return opsGroupList;
    }

    public List<String> queryAllChildGroup(ChildGroupQueryModel childGroupQueryModel) {
        List<String> tempGroupIdList = Lists.newArrayList();
        if (childGroupQueryModel != null && !CollectionUtils.isEmpty(childGroupQueryModel.getGroupIdList())) {
            tempGroupIdList.addAll(childGroupQueryModel.getGroupIdList());
            for (String groupId : childGroupQueryModel.getGroupIdList()) {
                addChildGroup(tempGroupIdList, Long.valueOf(groupId));
            }
        }
        return tempGroupIdList;
    }
    private void addChildGroup(List<String> opsGroupList, Long groupId) {
            List<OpsGroup> subGroupList = opsGroupDao.queryGroupByParentId(groupId);
            if (!CollectionUtils.isEmpty(subGroupList)) {
                for (OpsGroup opsGroup: subGroupList) {
                    if (!opsGroupList.contains(opsGroup.getGroupId().toString())) {
                        opsGroupList.add(opsGroup.getGroupId().toString());
                        //只要拥有父分组权限，则子分组权限则一定有权限
//                        authGroupIdList.add(opsGroup.getGroupId().toString());
                        addChildGroup(opsGroupList, opsGroup.getGroupId());
                    }
                }
            }
    }
    private void addParentGroup(List<String> opsGroupList, Long groupId) {
        if (groupId != 1) {
            OpsGroup opsGroup = opsGroupDao.queryGroupByGroupId(groupId);
            if (!opsGroupList.contains(opsGroup.getParentId().toString())) {
                opsGroupList.add(opsGroup.getParentId().toString());
            }
            addParentGroup(opsGroupList, opsGroup.getParentId());
        }
    }

    private void getTree(List<OpsGroup> opsGroupList, List<String> opsGroupIdList, List<String> authGroupIdList) {
        for (OpsGroup opsGroup : opsGroupList) {
            if (!CollectionUtils.isEmpty(authGroupIdList) && !authGroupIdList.contains(opsGroup.getGroupId().toString())) {
                opsGroup.setDisabled(true);
            }
            OpsGroup reqCre = new OpsGroup();
            reqCre.setParentId(opsGroup.getGroupId());
            reqCre.setAuthGroupIdList(opsGroupIdList);
            List<OpsGroup> queryList = opsGroupDao.queryGroupList(reqCre);
            opsGroup.setSubGroupList(queryList);
            if (!CollectionUtils.isEmpty(queryList)) {
                getTree(queryList, opsGroupIdList, authGroupIdList);
            }
        }
    }

    public GeneralResponse saveGroup(OpsGroup opsGroup) {
        String currUser = RequestAuthContext.getRequestHeadUserName();
        Date now = new Date();
        if (opsGroup.getGroupId() == null) {
            int count = opsGroupDao.queryGroupCountByName(opsGroup.getGroupName());
            if (count > 0) {
                return new GeneralResponse(false, "The group name is already exists", opsGroup);
            }
            opsGroup.setCreater(currUser);
            opsGroup.setCreateTime(now);
            opsGroup.setUpdater(currUser);
            opsGroup.setUpdateTime(now);
            opsGroupDao.saveGroup(opsGroup);
        } else {
            opsGroup.setUpdater(currUser);
            opsGroup.setUpdateTime(now);
            opsGroupDao.updateGroup(opsGroup);
        }
        return new GeneralResponse(true, "", opsGroup);
    }

    public GeneralResponse deleteGroup(Long groupId) {
        GroupRelationQueryModel groupRelationQueryModel = new GroupRelationQueryModel();
        groupRelationQueryModel.setGroupId(groupId);
        List<GroupRelationDetail> list = opsGroupDao.querGroupRelationList(groupRelationQueryModel);
        if (!CollectionUtils.isEmpty(list)) {
//            return new GeneralResponse(false, "The group relation data is existed, Delete operation is not allowed ");
            return new GeneralResponse(false, "此分组存在关联对象，不允许删除");
        }
        OpsGroup opsGroup = new OpsGroup();
        opsGroup.setParentId(groupId);
        List<OpsGroup> groupList = opsGroupDao.queryGroupList(opsGroup);
        if (!CollectionUtils.isEmpty(groupList)) {
//            return new GeneralResponse(false, "The group children data is existed, Delete operation is not allowed ");
            return new GeneralResponse(false, "此分组存在子分组，不允许删除");
        }
        opsGroupDao.deleteGroup(groupId);
        return new GeneralResponse();
    }

    public GeneralResponse saveBatchGroupRelation(GroupRelationBatchAddReq groupRelationBatchAddReq) {
        if (!CollectionUtils.isEmpty(groupRelationBatchAddReq.getDeleteRelationIds())) {
            opsGroupDao.deleteGroupRelationList(Joiner.on(",").join(groupRelationBatchAddReq.getDeleteRelationIds()));
        }
        List<OpsGroupRelation> addRelationList = Lists.newArrayList();
        for (OpsGroupRelation opsGroupRelation : groupRelationBatchAddReq.getGroupRelationList()) {
            List<OpsGroupRelation> queryResult = opsGroupDao.queryGroupRelationBase(opsGroupRelation);
            if (CollectionUtils.isEmpty(queryResult)) {
                addRelationList.add(opsGroupRelation);
            }
        }
        if (!addRelationList.isEmpty()) {
            opsGroupDao.saveBatchGroupRelation(addRelationList);
        }
        return new GeneralResponse(true, "", groupRelationBatchAddReq);
    }

    void getSubGroupId(List<OpsGroup> groupList, List<Long> gorupIdList) {
        gorupIdList.addAll(groupList.stream().map(OpsGroup::getGroupId).collect(Collectors.toList()));
        for (OpsGroup opsGroup : groupList) {
            OpsGroup param = new OpsGroup();
            param.setParentId(opsGroup.getGroupId());
            List<OpsGroup> subGroupList = opsGroupDao.queryGroupList(param);
            if (!CollectionUtils.isEmpty(groupList)) {
                getSubGroupId(subGroupList, gorupIdList);
            }
        }
    }
    public PageListQueryResult<GroupRelationDetail> querGroupRelationList(GroupRelationQueryModel
                                                                                  groupRelationQueryModel) {
        // 支持查询分组及子分组数据
        List<Long> gorupIdList = Lists.newArrayList();
        if (groupRelationQueryModel.getGroupParentId() != null) {
            gorupIdList.add(groupRelationQueryModel.getGroupParentId());
            OpsGroup param = new OpsGroup();
            param.setParentId(groupRelationQueryModel.getGroupParentId());
            List<OpsGroup> groupList = opsGroupDao.queryGroupList(param);
            if (!CollectionUtils.isEmpty(groupList)) {
                getSubGroupId(groupList, gorupIdList);
            }
            groupRelationQueryModel.setGroupIdList(gorupIdList);
        }

        Integer totalCount = opsGroupDao.querGroupRelationListTotalSize(groupRelationQueryModel);
        List<GroupRelationDetail> list = null;
        if (totalCount > 0) {
            list = opsGroupDao.querGroupRelationList(groupRelationQueryModel);
        } else {
            list = Lists.newArrayList();
        }
        return new PageListQueryResult<>(totalCount, list);
    }

    public GeneralResponse deleteGroupRelation(String groupRelationIds) {
        opsGroupDao.deleteGroupRelationList(groupRelationIds);
        return new GeneralResponse();
    }

    public PageListQueryResult<GroupObjectDetail> queryObjectList(GroupRelationQueryModel groupRelationQueryModel) {
        Integer totalCount = opsGroupDao.querGroupObjectListTotalSize(groupRelationQueryModel);
        List<GroupObjectDetail> list = null;
        if (totalCount > 0) {
            list = opsGroupDao.querGroupObjectList(groupRelationQueryModel);
        } else {
            list = Lists.newArrayList();
        }
        return new PageListQueryResult<>(totalCount, list);
    }


    public List<OpsResource> querOpsResourceTree(String objectType) {
        OpsGroup param = new OpsGroup();
        param.setTop(true);
        List<OpsGroup> opsGroupList = opsGroupDao.queryGroupList(param);
        List<OpsResource> opsResourceList = Lists.newArrayList();
        for (OpsGroup opsGroup : opsGroupList) {
            OpsResource opsResource = new OpsResource(opsGroup.getGroupId().toString(), opsGroup.getParentId().toString(), opsGroup
                    .getGroupName(), OpsResource.RESOURCE_TYPE_GROUP);
            opsResourceList.add(opsResource);
        }
        getTree(opsResourceList, objectType);
        filterTree(opsResourceList);
        return opsResourceList;
    }

    private void filterTree(List<OpsResource> opsResourceList) {
        int index = 0;
        while (index < opsResourceList.size()) {
            if (opsResourceList.get(index).getResourceType().equals(OpsResource.RESOURCE_TYPE_GROUP)) {
                if (!CollectionUtils.isEmpty(opsResourceList.get(index).getSubResourceList())) {
                    filterTree(opsResourceList.get(index).getSubResourceList());
                }
                if (CollectionUtils.isEmpty(opsResourceList.get(index).getSubResourceList())) {
                    opsResourceList.remove(opsResourceList.get(index));
                    index--;
                }
            }
            index++;
        }
    }

    private void getTree(List<OpsResource> resourceList, String objectType) {
        for (OpsResource resource : resourceList) {
            if (resource.getResourceType().equals(OpsResource.RESOURCE_TYPE_GROUP)) {
                OpsGroup reqCre = new OpsGroup();
                reqCre.setParentId(Long.parseLong(resource.getResourceId()));
                List<OpsGroup> queryList = opsGroupDao.queryGroupList(reqCre);
                List<OpsResource> opsResourceList = Lists.newArrayList();
                for (OpsGroup opsGroup : queryList) {
                    OpsResource opsResource = new OpsResource(opsGroup.getGroupId().toString(), opsGroup.getParentId
                            ().toString(), opsGroup
                            .getGroupName(), OpsResource.RESOURCE_TYPE_GROUP);
                    opsResourceList.add(opsResource);
                }
                //设置资源数据
                if (!StringUtils.isEmpty(objectType)) {
                    List<GroupRelationDetail> groupRelationList = opsGroupDao.querGroupRelationListByGroupId(Long
                            .parseLong(resource.getResourceId()), objectType);
                    for (GroupRelationDetail groupRelation : groupRelationList) {
                        OpsResource opsResource = new OpsResource(resource.getResourceId
                                () + "_" + groupRelation.getObjectId().toString(), resource.getResourceId
                                (), groupRelation.getObjectName(), objectType);
                        opsResourceList.add(opsResource);
                    }
                }
                resource.setSubResourceList(opsResourceList);
                if (!CollectionUtils.isEmpty(opsResourceList)) {
                    getTree(opsResourceList, objectType);
                }
            }
        }
    }


}
