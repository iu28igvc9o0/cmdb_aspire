package com.aspire.ums.cmdb.ipCollect.mapper;

import com.aspire.ums.cmdb.ipCollect.payload.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 冲突IP mapper
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/23 13:57
 */
@Repository
public interface CmdbIpClashMapper {

    /**
     * 插入主表
     *
     * @param entity
     * @return
     */
    int insertMain(CmdbIpClashMainEntity entity);

    int batchInsertMain(List<CmdbIpClashMainEntity> entityList);

    /**
     * 获取主表所有冲突IP
     *
     * @return
     */
    List<String> getAllIpForMain();

    /**
     * 统计当前IP的数量
     *
     * @param ip
     * @return
     */
    int countByIpForMain(@Param("ip") String ip);

    /**
     * 根据IP查询
     *
     * @param ip
     * @return
     */
    List<CmdbIpClashMainEntity> getEntityByIpForMain(@Param("ip") String ip);

    List<CmdbIpClashMainEntity> getEntityByIpAndTypeForMain(@Param("ip") String ip, @Param("collectType") String collectType);

    /**
     * 根据采集类型查询所有IP
     *
     * @param colletType
     * @return
     */
    List<String> getIpByTypeForMain(@Param("colletType") String colletType);

    /**
     * 修改主表
     *
     * @param entity
     * @return
     */
    int updateMain(CmdbIpClashMainEntity entity);

    /**
     * 根据IP和采集类型修改
     *
     * @param entity
     * @return
     */
    int updateMainByIpAndType(CmdbIpClashMainEntity entity);

    int updateHandleStatus(CmdbIpClashUpdateRequest entity);

    /**
     * 条件查询数据
     *
     * @param request
     * @return
     */
    List<CmdbIpClashFindPageResponse> findData(CmdbIpClashFindPageRequest request);

    /**
     * Integer 条件查询统计总数
     *
     * @param request
     * @return
     */
    Integer findDataCount(CmdbIpClashFindPageRequest request);

    /**
     * 冲突IP页面头栏统计
     *
     * @param request
     * @return
     */
    CmdbIpClashTopTotalResponse findDataTopTotal(CmdbIpClashFindPageRequest request);

    /**
     * 根据ID批量修改处理状态
     *
     * @param map
     * @return
     */
    int updateStatusByIdsForMain(Map<String, Object> map);

    /**
     * 插入记录表
     *
     * @param entity
     * @return
     */
    int insertRecord(CmdbIpClashRecordEntity entity);

    int batchInsertRecord(List<CmdbIpClashRecordEntity> entityList);

    /**
     * 修改
     *
     * @param entity
     * @return
     */
    int updateRecord(CmdbIpClashRecordEntity entity);

    /**
     * 根据ID批量修改工单号
     *
     * @param map
     * @return
     */
    int updateJobNumberByIdsForRecord(Map<String, Object> map);

    int getClashMain4Now();

    void insertRebuildClash(Map<String,String> map);

    /**
     * 查询 当天状态为待处理的IP
     *
     */
    List<Map<String,Object>> findPendingIpList(CmdbIpClashFindPageRequest cmdbIpClashFindPageRequest);

    /**
     * 插入工单号_当天状态为待处理的IP
     *
     */
    void updateIpClashOrderStatus(Map<String, Object> objectMap);

    /**
     * 获取当天已经入库的冲突IP列表对应的IP检测字段
     */
    List<String> getClashIpTimeKeyList();
}
