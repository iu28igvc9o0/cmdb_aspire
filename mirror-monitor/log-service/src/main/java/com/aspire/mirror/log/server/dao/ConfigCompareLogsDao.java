package com.aspire.mirror.log.server.dao;

import com.aspire.mirror.log.server.dao.po.ConfigCompareLogs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author baiwenping
 * @Title: ConfigCompareLogsDao
 * @Package com.aspire.mirror.alert.server.dao
 * @Description: TODO
 * @date 2020/12/16 11:01
 */
@Mapper
public interface ConfigCompareLogsDao {

    /**
     *
     * @param log
     */
    void insert (ConfigCompareLogs log);

    /**
     *
     * @param compareId
     * @return
     */
    List<ConfigCompareLogs> listByCompareId(@Param("compareId") Integer compareId);
}
