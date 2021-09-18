package com.aspire.mirror.ops.dao;

import com.aspire.mirror.ops.api.domain.GroupObjectDetail;
import com.aspire.mirror.ops.api.domain.GroupRelationDetail;
import com.aspire.mirror.ops.api.domain.GroupRelationQueryModel;
import com.aspire.mirror.ops.api.domain.OpsGroup;
import com.aspire.mirror.ops.api.domain.OpsGroupRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.dao
 * 类名称:    OpsGroupDao.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/3/5 15:57
 * 版本:      v1.0
 */
@Mapper
public interface OpsGroupDao {
    List<OpsGroup> queryGroupList(OpsGroup opsGroup);

    void saveGroup(OpsGroup opsGroup);

    void updateGroup(OpsGroup opsGroup);

    void deleteGroup(Long groupId);

    void saveBatchGroupRelation(List<OpsGroupRelation> groupRelationList);

    Integer querGroupRelationListTotalSize(GroupRelationQueryModel groupRelationQueryModel);

    List<GroupRelationDetail> querGroupRelationList(GroupRelationQueryModel groupRelationQueryModel);

    void deleteGroupRelationList(@Param("group_relation_ids") String groupRelationIds);

    void deleteGroupRelationByGroupIdListAndObjectId(@Param("groupIdList") List<Long> groupIdList, @Param("objectId") Long objectId, @Param("objectType") String objectType);

    void deleteGroupRelationByObjectIdAndType(@Param("objectId") Long objectId, @Param("objectType") String objectType);

    Integer querGroupObjectListTotalSize(GroupRelationQueryModel groupRelationQueryModel);

    List<GroupObjectDetail> querGroupObjectList(GroupRelationQueryModel groupRelationQueryModel);

    List<GroupRelationDetail> querGroupRelationListByGroupId(@Param("groupId") Long groupId, @Param("objectType") String objectType);
    

    int queryGroupCountByName(String groupName);

    List<OpsGroupRelation> queryGroupRelationBase(OpsGroupRelation opsGroupRelation);

    OpsGroup queryGroupByGroupId(@Param("groupId") Long groupId);

    List<OpsGroup> queryGroupByParentId(@Param("parentId") Long parentId);
}
