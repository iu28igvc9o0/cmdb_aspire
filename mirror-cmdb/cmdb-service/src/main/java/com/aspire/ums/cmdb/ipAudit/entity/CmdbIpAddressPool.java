package com.aspire.ums.cmdb.ipAudit.entity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述： 全量存活IP
* @author huanggongrui
* @date 2020-05-21 17:44:58
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbIpAddressPool {

    /**
     * 
     */
    private String id;
    /**
     * 实例id
     */
    private String instanceId;
    /**
     * IP地址
     */
    private String ip;
    /**
     * IP类型
     */
    private String iptype;
    /**
     * MAC地址
     */
    private String mac;
    /**
     * 所属资源池
     */
    private String resource;
    /**
     * 检查时间
     */
    private Date time;
    /**
     * 网关设备IP
     */
    private String gateway;
    /**
     * 云管eventId
     */
    private String eventId;
    /**
     * 云端创建人
     */
    private String srcCreator;
    /**
     * 云管端的创建时间
     */
    private Date srcCreateTime;
    /**
     * 云管端的修改时间
     */
    private Date srcOptTime;
    /**
     * 该实例上次修改的时间
     */
    private Date srcPreOptTime;
    /**
     * 当前实例版本
     */
    private Integer version;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 状态 0-正常,1-删除
     */
    private String delFlag;
}