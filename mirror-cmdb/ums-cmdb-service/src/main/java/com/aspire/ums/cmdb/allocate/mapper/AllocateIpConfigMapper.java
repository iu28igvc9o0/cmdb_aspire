package com.aspire.ums.cmdb.allocate.mapper;

import com.aspire.ums.cmdb.allocate.entity.AllocateIpConfigDetail;
import com.aspire.ums.cmdb.allocate.entity.AllocateIpConfigListReq;
import com.aspire.ums.cmdb.allocate.entity.AllocateIpConfigOperationReq;
import com.aspire.ums.cmdb.allocate.entity.AllocateIpConfigRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AllocateIpConfigMapper {

    /**
     * 获取域名分配的ip数据列表
     */
    List<AllocateIpConfigRes> getAllocateIpConfigData(AllocateIpConfigListReq request);

    /**
     * 总数
     */
    int getAllocateIpConfigDataCount(AllocateIpConfigListReq request);

    /**
     * 添加ip分配管理配置
     */
    void insertAllocateIpConfig(List<AllocateIpConfigDetail> request);

    /**
     *  批量删除
     */
    void deleteAllocateIpConfigById(List<Long> ids);

    /**
     * 导出
     */
    List<Map<String, Object>> exportAllocateIpConfigData(AllocateIpConfigListReq request);

    /**
     * 获取域名
     */
    List<Map<String, Object>> getVpnData();

    /**
     * 获取网段
     */
    List<Map<String, Object>> getNetworkById(@Param("id") long id);

    /**
     * 操作记录
     */
    void insertAllocateIpConfigOperation(AllocateIpConfigOperationReq request);
}
