package com.aspire.ums.cmdb.openstack.payload.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:00:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CmdbOpenstackAdmin extends BaseOpenStackEntity implements Serializable {

    private static final long serialVersionUID = 4142239227681371604L;

    /**
     * 
     */
    private String org;

    /**
     * 
     */
    private String controllerIp;

    /**
     * 
     */
    private String orderIpController;
}
