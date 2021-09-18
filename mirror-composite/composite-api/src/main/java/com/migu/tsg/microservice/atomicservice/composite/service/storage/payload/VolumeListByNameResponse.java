package com.migu.tsg.microservice.atomicservice.composite.service.storage.payload;

import java.util.Date;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
* 项目名称: composite-api
* 包: com.migu.tsg.microservice.atomicservice.composite.service.storage.payload
* 类名称: SnapshotsCreateResponse.java
* 类描述:
* 创建人: ZhangRiYue
* 创建时间: 2017年9月25日上午12:52:30
* 版本: v1.0 
*/
@NoArgsConstructor
//@AllArgsConstructor
@Data
public class VolumeListByNameResponse {
  
    private String id;
    
    private String name;
    
    private int size;
    
    private String driver_name;
    
    private String snapshot_id;
    
    /** 状态*/
    private String state ;

    /** 挂载计数*/
    private String mountCnt ;

    /** 账号*/
    private String namespace ;

    /** 集群id*/
    private String region_id ;

    /** 错误信息*/
    private String errormsg ;

    /** 创建时间*/
    private Date created_at ;

    /** 更新时间*/
    private Date updated_at ;
    
    public Date getCreated_at() {
        if (this.created_at == null) {
            return null;
        }
        return (Date) this.created_at.clone();
    }
    public void setCreated_at(final Date created_at) {
        if (created_at == null) {
            this.created_at = null;
        } else {
            this.created_at = (Date) created_at.clone();
        }
    }
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
