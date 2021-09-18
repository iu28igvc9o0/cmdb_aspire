package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 敏感指令级别列表查询对象
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api
 * 类名称:    SensitiveLevelQueryModel
 * 类描述:    敏感指令级别查询条件
 * 创建人:    JinSu
 * 创建时间:  2020/9/17 15:47
 * 版本:      v1.0
 */
@Data
public class SensitiveLevelQueryModel {
    @JsonProperty("page_size")
    private Integer	pageSize;		// 如果为null， 则查询全部数据

    @JsonProperty("page_no")
    private Integer	pageNo;			// 从0开始

    private String	nameLike;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date	createTimeEnd;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date	updateTimeStart;

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
