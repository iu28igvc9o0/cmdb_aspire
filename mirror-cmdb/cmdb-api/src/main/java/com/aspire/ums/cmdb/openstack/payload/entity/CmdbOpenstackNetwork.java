package com.aspire.ums.cmdb.openstack.payload.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:00:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CmdbOpenstackNetwork extends BaseOpenStackEntity implements Serializable {

    private static final long serialVersionUID = -5190416814996981047L;

    /**
     * 
     */
    private String networkId;

    /**
     * 
     */
    private String hname;

    /**
     * 
     */
    private String sname;

    /**
     * 
     */
    private String isAdminStateUp;

    /**
     * 
     */
    private String isRouterExternal;

    /**
     * 
     */
    private String isShared;

    /**
     * 
     */
    private String mtu;

    /**
     * 
     */
    private String org;

    /**
     * 
     */
    private String providerNetworkType;

    /**
     * 
     */
    private String providerSegmentationId;

    /**
     * 
     */
    private String status;

}
