package com.aspire.ums.cmdb.collectApproval.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
* 描述：
* @author
* @date 2019-06-18 20:55:57
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbCollectResource {

    /**
     * ID
     */
    private String id;
    /**
     * 实例ID
     */
    private String instanceId;
    /**
     * 采集字段
     */
    private String codeId;
    /**
     * 采集字段值
     */
    private String value;
    /**
     * 创建时间
     */
    private Date createTime;
}