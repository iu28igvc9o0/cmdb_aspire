package com.aspire.ums.cmdb.maintenance.payload;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2019-07-29 22:31:45
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbMaintenanceProjectInstance {

    /**
     * ID
     */
    private String id;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 设备序列号
     */
    private String deviceSn;
    /**
     * 设备ID
     */
    private String instanceId;
    /**
     * 是否删除
     */
    private String isDelete;
}