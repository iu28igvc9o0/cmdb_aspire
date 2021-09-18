package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 敏感指令审核查询对象
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    SensitiveReviewQueryModel.java
 * 类描述:    敏感指令审核查询对象
 * 创建人:    JinSu
 * 创建时间:  2020/2/18 10:01
 * 版本:      v1.0
 */
@Data
public class SensitiveReviewQueryModel extends SensitiveReview{
    private String commandLike;

    private String ruleNameLike;

    private Integer contentType;

    private String reviewStatusString;

    private Integer instanceClassify;

    private String instanceNameLike;

    private List<String> roleIdList;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTimeStart;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTimeEnd;

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
