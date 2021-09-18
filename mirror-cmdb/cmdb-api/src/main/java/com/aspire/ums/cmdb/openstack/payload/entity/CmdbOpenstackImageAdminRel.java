package com.aspire.ums.cmdb.openstack.payload.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:00:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbOpenstackImageAdminRel {

    /**
     * 
     */
    private String id;

    /**
     * 
     */
    private String imageId;

    /**
     * 
     */
    private String adminId;
}
