package com.aspire.ums.cmdb.v2.instance.mapper;

import com.aspire.ums.cmdb.instance.payload.CmdbInstanceAgentCheck;
import com.aspire.ums.cmdb.instance.payload.CmdbInstanceAgentCheckQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName CmdbInstanceAgentCheckMapper
 * @Description
 * @Author luowenbo
 * @Date 2020/5/25 16:17
 * @Version 1.0
 */
@Mapper
public interface CmdbInstanceAgentCheckMapper {

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
    void batchDelete(String[] ids);

    /*
     * 查询展示列表
     * */
    List<CmdbInstanceAgentCheck> list(CmdbInstanceAgentCheckQuery query);

    /*
     * 查询列表总数
     * */
    int listByCount(CmdbInstanceAgentCheckQuery query);

    /*
    *  获取CI中已经安装Agent的设备
    * */
    List<CmdbInstanceAgentCheck> getCIWithAgent(@Param("idcType") String idcType);
}
