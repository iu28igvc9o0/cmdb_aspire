package com.aspire.ums.cmdb.sync.payload;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import lombok.Data;

/**
 * 业务线
 *
 * @author jiangxuwen
 * @date 2020/5/20 11:16
 */
@Data
public class CmdbBusinessDomain implements Serializable {

    private static final long serialVersionUID = -1848033792519682518L;

    @JsonProperty("name")
    @JSONField(name = "name")
    private String name;

    @JsonProperty("businessId")
    @JSONField(name = "businessId")
    private String businessId;

    @JsonProperty("businessCode")
    @JSONField(name = "businessCode")
    private String businessCode;

    @JsonProperty("parentId")
    @JSONField(name = "parentId")
    private String parentId;

    @JsonProperty("parentCode")
    @JSONField(name = "parentCode")
    private String parentCode;

    @JsonProperty("parentName")
    @JSONField(name = "parentName")
    private String parentName;

    @JsonProperty("remarks")
    @JSONField(name = "remarks")
    private String remarks;

    private List<CmdbBusinessDomain> children = Lists.newArrayList();

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getBusinessCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof CmdbBusinessDomain) {
            CmdbBusinessDomain businessDomain = (CmdbBusinessDomain) obj;
            return Objects.equal(businessDomain.getBusinessCode(), this.getBusinessCode());
        }
        return false;
    }
}
