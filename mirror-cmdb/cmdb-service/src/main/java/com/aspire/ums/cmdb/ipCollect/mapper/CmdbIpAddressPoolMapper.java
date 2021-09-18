package com.aspire.ums.cmdb.ipCollect.mapper;

import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpAddressPool;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpAddressPoolEntity;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpArpPoolEntity;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpCollectRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 存活ip Mapper
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 17:51
 */
@Repository
public interface CmdbIpAddressPoolMapper {

    void add(CmdbIpAddressPoolEntity entity);

    void batchAdd(List<CmdbIpAddressPoolEntity> entityList);

    void update(CmdbIpAddressPoolEntity entity);

    void updateByInstanceId(Map<String, Object> params);

    /**
     * 逻辑删除
     *
     * @param instanceIdList
     */
    void batchDeleteByInstanceId(List<String> instanceIdList);

    /**
     * 物理删除
     *
     * @param instanceIdList
     */
    void batchDeleteByInstanceIdw(List<String> instanceIdList);

    /**
     * 查询所有instanceId
     *
     * @return
     */
    List<String> getAllInstanceId();

    /**
     * 根据instanceId集合查询
     *
     * @param instanceIdList
     * @return
     */
    List<CmdbIpAddressPoolEntity> findByInstanceIdList(List<String> instanceIdList);

    /**
     * 根据IP批量查询
     *
     * @param ipList
     * @return
     */
    List<CmdbIpAddressPoolEntity> findDataByIpList(List<String> ipList);

    /**
     * 根据instanceId集合查询
     *
     * @param instanceIds
     * @return
     */
    List<CmdbIpAddressPoolEntity> findByInstanceids(List<String> instanceIds);

}
