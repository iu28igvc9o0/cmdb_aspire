package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    GroupObjectDetail.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/3/13 11:15
 * 版本:      v1.0
 */
@Data
public class GroupObjectDetail {
    @JsonProperty("object_name")
    @ApiModelProperty("对象名称")
    private String objectName;

    @JsonProperty("object_id")
    @ApiModelProperty("对象ID")
    private String objectId;

    @ApiModelProperty("对象类型")
    @JsonProperty("object_type")
    private String objectType;

    @JsonProperty("creater")
    @ApiModelProperty(value = "创建人")
    protected String	creater;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;

    @ApiModelProperty(value = "修改人")
    @JsonProperty("updater")
    protected String	updater;

    @ApiModelProperty(value = "修改时间")
    @JsonProperty("update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date		updateTime;
}
