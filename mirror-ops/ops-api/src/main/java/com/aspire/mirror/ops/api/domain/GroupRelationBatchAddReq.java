package com.aspire.mirror.ops.api.domain;

import lombok.Data;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    GroupRelationBatchAddReq.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/3/5 14:22
 * 版本:      v1.0
 */
@Data
public class GroupRelationBatchAddReq{
    private List<OpsGroupRelation> groupRelationList;
    private List<Long> deleteRelationIds;
}
