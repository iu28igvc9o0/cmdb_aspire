package com.aspire.ums.cmdb.ipCollect.service;

import com.aspire.ums.cmdb.ipCollect.payload.entity.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/23 13:57
 */
public interface CmdbIpClashService {

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
     * 冲突IP处理
     *
     * @param oldEntity
     */
    <T extends BaseIpCollectEntity> void ipClashSave(BaseIpCollectEntity oldEntity, Class<T> clazz);

    /**
     * 推送冲突IP处理
     * @param requestList
     */
    void ipClashSaveList(List<CmdbIpClashCreateRequest> requestList);

    /**
     * 插入主表
     *
     * @param entity
     * @return
     */
    void insertMain(CmdbIpClashMainEntity entity);

    void batchInsertMain(List<CmdbIpClashMainEntity> entities);

    /**
     * 修改处理状态
     *
     * @param request
     */
    void updateHandleStatus(CmdbIpClashUpdateRequest request);

    /**
     * 获取主表所有冲突IP
     *
     * @return
     */
    List<String> getAllIpForMain();

    /**
     * 判断当前IP是否存在
     *
     * @param ip
     * @return
     */
    Boolean isExistByIpForMain(String ip);

    /**
     * 根据IP查询
     *
     * @param ip
     * @return
     */
    List<CmdbIpClashMainEntity> getEntityByIpForMain(String ip);

    /**
     * 根据IP和采集类型查询
     *
     * @param ip
     * @param collectType
     * @return
     */
    List<CmdbIpClashMainEntity> getEntityByIpAndTypeForMain(String ip, String collectType);

    /**
     * 根据采集类型查询所有IP
     *
     * @param colletType
     * @return
     */
    List<String> getIpByTypeForMain(String colletType);

    /**
     * 更新主表数据
     *
     * @param entity
     * @return
     */
    void updateMain(CmdbIpClashMainEntity entity);

    /**
     * 插入记录表
     *
     * @param entity
     * @return
     */
    void insertRecord(CmdbIpClashRecordEntity entity);

    void batchInsertRecord(List<CmdbIpClashRecordEntity> entities);

    /**
     * 更新记录表数据
     *
     * @param entity
     */
    void updateRecord(CmdbIpClashRecordEntity entity);

    void saveIpClashList4Now(Map<String, List<CmdbIpCollectResponse>> ipClashList4Now);

    /**
     * 查询当天的冲突IP的入库记录的数量
     */
    int getClashMain4Now();

    void rebuildClashList(List<CmdbIpClashRebuildRequest> request);

    /**
     * 当天,状态为待处理的IP，发送通用任务流程工单
     */
    void buidThatDayPendingIpAndSendOrder();
}
