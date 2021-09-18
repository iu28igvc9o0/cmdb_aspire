package com.aspire.mirror.composite.service.opsmanage.payload;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.opsmanage.payload
 * 类名称:    GroupRelationExport.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/3/13 16:34
 * 版本:      v1.0
 */
@Data
public class GroupRelationExport {
    @JsonProperty("group_name")
    @ApiModelProperty("分组名称")
    @Excel(name = "分组名称", width = 20)
    private String groupName;

    @ApiModelProperty("对象类型")
    @JsonProperty("object_type")
    @Excel(name = "对象类型", replace = {"作业_pipeline", "脚本_script", "YUM文件_YUM"}, width = 20)
    private String objectType;

    @JsonProperty("object_name")
    @ApiModelProperty("对象名称")
    @Excel(name = "对象名称", width = 20)
    private String objectName;

    @JsonProperty("creater")
    @ApiModelProperty(value = "创建人")
    @Excel(name = "创建人", width = 20)
    protected String	creater;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", format = "yyyy-MM-dd HH:mm:ss", width = 30)
    protected Date createTime;

    @ApiModelProperty(value = "修改人")
    @JsonProperty("updater")
    @Excel(name = "修改人", width = 20)
    protected String	updater;

    @ApiModelProperty(value = "修改时间")
    @JsonProperty("update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "修改时间", format = "yyyy-MM-dd HH:mm:ss", width = 30)
    protected Date		updateTime;
}
