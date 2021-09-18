package com.aspire.ums.cmdb.index.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ItCloudScreenExportRequest
 * @Description 大屏数据导出请求实体
 * @Author luowenbo
 * @Date 2020/5/18 17:51
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItCloudScreenExportRequest {
    /*
    *  导出页面类型
    * */
    private String exportType;
    /*
    *  一级部门
    * */
    private String department1;
    /*
    *  二级部门
    * */
    private String department2;
    /*
    *  资源池
    * */
    private String idcType;
    /*
    *  排除部门
    * */
    private String excludeDep;
    /*
     *  月报时间
     * */
    private String monthlyTime;
}
