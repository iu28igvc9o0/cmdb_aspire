package com.aspire.ums.cmdb.openstack.payload.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:01:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbOpenstackSubnetAdminRel {

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
    private String adminId;
}
