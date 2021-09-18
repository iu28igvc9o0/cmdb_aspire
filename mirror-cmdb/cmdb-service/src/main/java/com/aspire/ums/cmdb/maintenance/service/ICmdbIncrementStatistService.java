package com.aspire.ums.cmdb.maintenance.service;

import com.aspire.ums.cmdb.maintenance.payload.CmdbIncrementStatistRequest;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ICmdbIncrementStatistService
 * @Description 维保增量按照时间维度统计——服务层接口
 * @Author luowenbo
 * @Date 2020/2/19 16:27
 * @Version 1.0
 */
public interface ICmdbIncrementStatistService {
    /*
     *  基于查询条件，按照时间维度统计每个月的设备增量
     * */
    List<Map<String,Object>> statistIncrementByTime(CmdbIncrementStatistRequest req);
}
