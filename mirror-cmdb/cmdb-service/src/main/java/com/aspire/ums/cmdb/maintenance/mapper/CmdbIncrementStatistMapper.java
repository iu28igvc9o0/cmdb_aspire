package com.aspire.ums.cmdb.maintenance.mapper;

import com.aspire.ums.cmdb.maintenance.payload.CmdbIncrementStatistRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @ClassName CmdbIncrementStatist
 * @Description 维保增量按照时间维度统计
 * @Author luowenbo
 * @Date 2020/2/19 16:16
 * @Version 1.0
 */
@Mapper
public interface CmdbIncrementStatistMapper {

    /*
    *  基于查询条件，按照时间维度统计每个月的设备增量
    * */
    List<Map<String,Object>> statistIncrementByTime(CmdbIncrementStatistRequest req);
}
