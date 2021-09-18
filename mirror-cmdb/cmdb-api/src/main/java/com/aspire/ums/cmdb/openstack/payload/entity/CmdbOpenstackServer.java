package com.aspire.ums.cmdb.openstack.payload.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:00:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CmdbOpenstackServer extends BaseOpenStackEntity implements Serializable {

    private static final long serialVersionUID = -6280417791440113664L;

    /**
     * 子网ID
     */
    private String serverId;

    /**
     * 云环境
     */
    private String hname;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private String launchedAt;

    /**
     * 
     */
    private String org;

    /**
     * 
     */
    private String powerState;

    /**
     * 
     */
    private String rootDeviceName;

    /**
     * 
     */
    private String status;

    /**
     * 
     */
    private String sname;

}
