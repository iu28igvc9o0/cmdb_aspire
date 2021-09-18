package com.aspire.ums.cmdb.maintenance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName CmdbInventoryInfoStatistRequest
 * @Description 维保设备信息与cmdb存量信息比对 请求实体类
 * @Author luowenbo
 * @Date 2020/2/16 15:42
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmdbInventoryInfoStatistRequest {
    // 页数
    private Integer pageNo;
    // 页面大小
    private Integer pageSize;
    // 维保项目名称
    private String projectName;
    // 资源池
    private String resourcePool;
    // 查询类型： 维保项目未关联设备数量（maintenance） || 资产管理未关联设备数量(instance)
    // 点击不同的条件的数量，代入的sql查询不同
    private String searchType;
}
