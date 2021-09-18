package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    SensitiveRuleWhiteQueryModel.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/3/10 9:45
 * 版本:      v1.0
 */
@Data
public class SensitiveRuleWhiteQueryModel extends SensitiveRuleWhite{
    private String objectNameLike;

    @JsonProperty("page_size")
    private Integer	pageSize;		// 如果为null， 则查询全部数据
    @JsonProperty("page_no")
    private Integer	pageNo;			// 从0开始

    public Integer getStartIdx() {
        if (pageSize == null) {
            return null;
        }
        return (pageNo == null || pageNo <= 0 ? 0 : pageNo - 1) * pageSize;
    }
}
