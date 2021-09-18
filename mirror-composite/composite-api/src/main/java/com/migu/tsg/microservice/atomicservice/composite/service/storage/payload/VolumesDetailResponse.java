package com.migu.tsg.microservice.atomicservice.composite.service.storage.payload;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 
* 项目名称: composite-api
* 包: com.migu.tsg.microservice.atomicservice.composite.service.storage.payload
* 类名称: VolumesDetailResponse.java
* 类描述:
* 创建人: ZhangRiYue
* 创建时间: 2017年9月14日上午10:26:30
* 版本: v1.0
 */


@NoArgsConstructor
//@AllArgsConstructor
@Data
public class VolumesDetailResponse {
    /**
     * 主键，记录id
     */
    @ApiModelProperty(value = "主键，记录id")
    private String id ;

    /** 存储卷名称 */
    @ApiModelProperty(value = "pvc名称")
    private String name ;

    /** 存储卷限额 */
    @ApiModelProperty(value = "存储卷限额")
    private Integer size ;

    /** 驱动名称，glusterfs */
    @ApiModelProperty(value = "驱动名称，glusterfs")
    private String driver_name ;

    /** 快照id */
    @ApiModelProperty(value = "快照id")
    private String snapshot_id ;

    /** 状态称*/
    @ApiModelProperty(value = "状态")
    private String state ;

    /** 命名空间 */
    @ApiModelProperty(value = "命名空间")
    private String namespace ;
    
    @ApiModelProperty(value = "k命名空间")
    private String knamespace ;

    /** 集群id */
    @ApiModelProperty(value = "集群id")
    private String region_id ;

    /** 错误信息 */
    @ApiModelProperty(value = "错误信息")
    private String errormsg ;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    private String created_at ;

    /** 更新时间 */
    @ApiModelProperty(value = "更新时间")
    private Date updated_at ;
    
    private List<Map<String, String>> stats;

    private String created_by;

    private String project_name;
    @JsonProperty("resource_actions")
    private List<String> resourceActions;
    
    public Date getUpdated_at() {
        if (this.updated_at == null) {
            return null;
        }
        return (Date) this.updated_at.clone();
    }
    public void setUpdated_at(final Date updated_at) {
        if (updated_at == null) {
            this.updated_at = null;
        } else {
            this.updated_at = (Date) updated_at.clone();
        }
    }
}
