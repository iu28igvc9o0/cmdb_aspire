package com.aspire.ums.cmdb.maintenance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName CmdbIncrementStatistRequest
 * @Description 维保增量按照时间维度统计的查询条件的请求类
 * @Author luowenbo
 * @Date 2020/2/19 16:19
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmdbIncrementStatistRequest {

    // 年份
    private String year;

    // 资源池
    private String resourcePool;

    // 业务系统
    private String bizSystem;

    // 设备类型
    private String deviceType;
}
