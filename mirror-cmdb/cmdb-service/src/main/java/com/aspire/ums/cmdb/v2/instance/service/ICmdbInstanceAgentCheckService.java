package com.aspire.ums.cmdb.v2.instance.service;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbInstanceAgentCheck;
import com.aspire.ums.cmdb.instance.payload.CmdbInstanceAgentCheckQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName ICmdbInstanceAgentCheckService
 * @Description
 * @Author luowenbo
 * @Date 2020/5/25 17:40
 * @Version 1.0
 */
public interface ICmdbInstanceAgentCheckService {
    /*
     * 新增
     * */
    void insert(CmdbInstanceAgentCheck obj);

    /*
     * 批量新增
     * */
    void batchInsert(List<CmdbInstanceAgentCheck> list);

    /*
     *  批量删除
     * */
    void batchDelete(CmdbInstanceAgentCheckQuery query);

    /*
     * 查询展示列表
     * */
    Result<CmdbInstanceAgentCheck> list(CmdbInstanceAgentCheckQuery query);

    /*
     *  获取CI中已经安装Agent的设备
     * */
    List<CmdbInstanceAgentCheck> getCIWithAgent(@Param("idcType") String idcType);
}
