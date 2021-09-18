package com.aspire.ums.cmdb.systemManager.payload;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 项目名称: 包: com.aspire.ums.cmdb.systemManager.entity 类名称: 类描述: 创建人: PJX 创建时间: 2019/5/23 18:43 版本: v1.0
 */
@Data
public class OrgManager implements Serializable {

    private static final long serialVersionUID = 7200467763541838321L;

    private String id;

    private String orgName;

    private String orgType;

    private String pid;

    private String remark;

    private String isdel;

    List<OrgManager> children;

    /** 是否需同步到网管,网管同步过来的时候 需设置为false,防止循环双向同步. */
    @JsonIgnore
    @JSONField(serialize = false)
    private boolean syncFlag = true;
}
