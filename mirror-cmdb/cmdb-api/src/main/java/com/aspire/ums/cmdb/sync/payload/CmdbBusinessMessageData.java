package com.aspire.ums.cmdb.sync.payload;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import lombok.Data;

/**
 * 网管通过rabbitmq同步过来的业务线.
 *
 * @author jiangxuwen
 * @date 2020/5/20 11:14
 */
@Data
public class CmdbBusinessMessageData implements Serializable {

    private static final long serialVersionUID = 8092874703755050512L;

    @JsonProperty("addList")
    @JSONField(name = "addList")
    private List<CmdbBusinessDomain> addList = Lists.newArrayList();

    @JsonProperty("updateList")
    @JSONField(name = "updateList")
    private List<CmdbBusinessDomain> updateList = Lists.newArrayList();

    @JsonProperty("removeList")
    @JSONField(name = "removeList")
    private List<CmdbBusinessDomain> removeList = Lists.newArrayList();

}
