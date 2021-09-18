package com.aspire.ums.cmdb.openstack.payload.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:00:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbOpenstackAllocationPool {

    /**
     * 
     */
    private String id;

    /**
     * 
     */
    private String subnetId;

    /**
     * 
     */
    private String start;

    /**
     * 
     */
    private String end;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;
}
