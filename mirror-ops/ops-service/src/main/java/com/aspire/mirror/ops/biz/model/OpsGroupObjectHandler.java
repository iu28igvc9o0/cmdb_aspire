package com.aspire.mirror.ops.biz.model;

import com.aspire.mirror.ops.api.domain.GroupRelationDetail;
import com.aspire.mirror.ops.api.domain.GroupRelationQueryModel;
import com.aspire.mirror.ops.api.domain.OpsGroupObject;
import com.aspire.mirror.ops.api.domain.OpsGroupObjectTypeEnum;
import com.aspire.mirror.ops.api.domain.OpsGroupRelation;
import com.aspire.mirror.ops.dao.OpsGroupDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.biz.model
 * 类名称:    OpsGroupObjectHandler.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/3/13 19:04
 * 版本:      v1.0
 */
@Component
public class OpsGroupObjectHandler {
    @Autowired
    private OpsGroupDao opsGroupDao;

    public void saveOpsGroup(OpsGroupObject groupObject, Long objectId, String objectType) {
        // 添加分组列表
        if (!CollectionUtils.isEmpty(groupObject.getGroupIdList())) {
            List<OpsGroupRelation> relationList = Lists.newArrayList();
            GroupRelationQueryModel queryModel = new GroupRelationQueryModel();
            queryModel.setObjectId(objectId);
            queryModel.setObjectType(objectType);
            List<GroupRelationDetail> groupRelationDetails = opsGroupDao.querGroupRelationList(queryModel);
            List<Long> existGroupId = groupRelationDetails.stream().map(GroupRelationDetail::getGroupId).collect
                    (Collectors.toList());
            for (Long groupId : groupObject.getGroupIdList()) {
                if (!existGroupId.contains(groupId)) {
                    OpsGroupRelation opsGroupRelation = new OpsGroupRelation(groupId, objectType, objectId);
                    relationList.add(opsGroupRelation);
                }
            }
            if (!CollectionUtils.isEmpty(relationList)) {
                opsGroupDao.saveBatchGroupRelation(relationList);
            }

            //删除
            List<Long> deleteGroupIdList = existGroupId.stream().filter(groupId -> !groupObject.getGroupIdList()
                    .contains(groupId)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(deleteGroupIdList)) {
                opsGroupDao.deleteGroupRelationByGroupIdListAndObjectId(deleteGroupIdList, objectId, objectType);
            }
        }
    }

    public void deleteGroupRelationByObjectIdAndType(Long objectId, String objectType) {
        opsGroupDao.deleteGroupRelationByObjectIdAndType(objectId, objectType);

    }

    public  Map<Long, List<GroupRelationDetail>> querGroupRelationMapByObjectType(String obejctType) {
        GroupRelationQueryModel queryModel = new GroupRelationQueryModel();
        queryModel.setObjectType(obejctType);
        List<GroupRelationDetail> groupRelationDetailList = opsGroupDao.querGroupRelationList(queryModel);
//        Map<Long, String> groupMap = groupRelationDetailList.stream().collect(Collectors.toMap(GroupRelationDetail::getGroupId, GroupRelationDetail::getGroupName));
        Map<Long, List<GroupRelationDetail>> groupRelationMap = Maps.newLinkedHashMap();
        for (GroupRelationDetail groupRelationDetail : groupRelationDetailList) {
            if (!groupRelationMap.keySet().contains(groupRelationDetail.getGroupId())) {
                List<GroupRelationDetail> list = Lists.newArrayList();
                list.add(groupRelationDetail);
                groupRelationMap.put(groupRelationDetail.getGroupId(), list);
            } else {
                groupRelationMap.get(groupRelationDetail.getGroupId()).add(groupRelationDetail);
            }
        }
        return groupRelationMap;
    }
}
