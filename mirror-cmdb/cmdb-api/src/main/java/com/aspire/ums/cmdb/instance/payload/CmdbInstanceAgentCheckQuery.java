package com.aspire.ums.cmdb.instance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName CmdbInstanceAgentCheckQuery
 * @Description CMDB的Agent部署设备检查请求类
 * @Author luowenbo
 * @Date 2020/5/25 16:46
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbInstanceAgentCheckQuery {
    /*
    *  资源池
    * */
    private String idcType;

    /*
    *  设备IP
    * */
    private String ip;

    /*
    *  扫描时间查询——起始时间
    * */
    private String scanStartTime;

    /*
     *  扫描时间查询——终止时间
     * */
    private String scanEndTime;

    /*
    *  删除的ID
    * */
    private String[] ids;

    /*
    *  页码
    * */
    private Integer pageNo;

    /*
    *  页面大小
    * */
    private Integer pageSize;
}
