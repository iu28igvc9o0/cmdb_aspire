package com.aspire.ums.cmdb.openstack.payload.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:00:55
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CmdbOpenstackImage extends BaseOpenStackEntity implements Serializable {

    private static final long serialVersionUID = 287625851425820075L;

    /**
     * 子网ID
     */
    private String imageId;

    /**
     * 云环境
     */
    private String hname;

    /**
     * 
     */
    private String minDisk;

    /**
     * 
     */
    private String minRam;

    /**
     * 
     */
    private String org;

    /**
     * 大小/MB
     */
    private String size;

    /**
     * 
     */
    private String status;

    /**
     * 
     */
    private String sname;

}
