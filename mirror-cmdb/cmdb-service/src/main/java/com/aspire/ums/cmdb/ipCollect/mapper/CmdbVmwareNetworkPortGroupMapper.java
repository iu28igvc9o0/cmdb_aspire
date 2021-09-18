package com.aspire.ums.cmdb.ipCollect.mapper;

import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbVmwareNetworkPortGroup;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-12-25 14:46
 * @description
 */
@Repository
public interface CmdbVmwareNetworkPortGroupMapper {

    CmdbVmwareNetworkPortGroup findById(String id);

    CmdbVmwareNetworkPortGroup findByInstanceId(String instanceId);

    List<CmdbVmwareNetworkPortGroup> findByInstanceIdList(List<String> instanceIdList);

    int insert(CmdbVmwareNetworkPortGroup entity);

    void batchInsert(List<CmdbVmwareNetworkPortGroup> entityList);

    int update(CmdbVmwareNetworkPortGroup entity);

    void updateByInstanceId(Map<String, Object> params);

    int delete(String id);

    void deleteByInstanceId(String instanceId);

    List<String> getAllInstanceId();

    void deleteByInstanceIdList(List<String> instanceIdList);
}
