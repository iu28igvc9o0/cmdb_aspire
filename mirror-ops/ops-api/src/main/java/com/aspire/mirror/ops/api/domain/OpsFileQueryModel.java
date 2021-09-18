package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    OpsFileQueryModel.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/6/9 20:48
 * 版本:      v1.0
 */
@Data
public class OpsFileQueryModel extends OpsFile{

    @JsonProperty("file_name_like")
    private String fileNameLike;

    @JsonProperty("file_version_like")
    private String fileVersionLike;


    @JsonProperty("page_size")
    private Integer	pageSize;		// 如果为null， 则查询全部数据
    @JsonProperty("page_no")
    private Integer	pageNo;			// 从0开始

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("update_time_start")
    private Date updateTimeStart;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("update_time_end")
    private Date	updateTimeEnd;

    @JsonProperty("group_ids")
    private String groupIds;

    public Integer getStartIdx() {
        if (pageSize == null) {
            return null;
        }
        return (pageNo == null || pageNo <= 0 ? 0 : pageNo - 1) * pageSize;
    }
}
