package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    OpsParamQueryModel.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/6/15 19:26
 * 版本:      v1.0
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class OpsParamQueryModel extends OpsParam{
    @JsonProperty("param_code_arr")
    private String paramCodeArr;

    @JsonProperty("param_id_arr")
    private String paramIdArr;

    @JsonProperty("page_size")
    private Integer	pageSize;		// 如果为null， 则查询全部数据

    @JsonProperty("page_no")
    private Integer	pageNo;			// 从0开始

    @JsonProperty("param_name_like")
    private String paramNameLike;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date	createTimeEnd;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTimeStart;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date	updateTimeEnd;

    public Integer getStartIdx() {
        if (pageSize == null) {
            return null;
        }
        return (pageNo == null || pageNo <= 0 ? 0 : pageNo - 1) * pageSize;
    }
}
