package com.aspire.ums.cmdb.maintenance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName CmdbComponentInfoQueryRequest
 * @Description TODO
 * @Author luowenbo
 * @Date 2020/2/13 21:54
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmdbComponentInfoQueryRequest {
    // 维保对象
    private String maintenanceId;
    // 页数
    private Integer pageNo;
    // 页面大小
    private Integer pageSize;
}
