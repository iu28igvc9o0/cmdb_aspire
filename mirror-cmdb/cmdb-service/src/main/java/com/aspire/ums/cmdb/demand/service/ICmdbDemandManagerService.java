package com.aspire.ums.cmdb.demand.service;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.demand.entity.CmdbDemandManager;
import com.aspire.ums.cmdb.demand.entity.CmdbDemandResourceTypeValue;
import com.aspire.ums.cmdb.demand.entity.DemandExcel;
import com.aspire.ums.cmdb.demand.entity.InsertDemandEntity;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-05-09 16:28:19
*/
public interface ICmdbDemandManagerService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbDemandManager> list();

    /**
     * 获取所有实例
     * @return 返回符合实例数据
     */
    JSONObject queryForMap(Map<String, Object> queryMap);
    
    /**
     * 获取所有实例
     * @return 返回符合实例数据
     */
    JSONObject queryExportData(Map<String, Object> queryMap);
    
    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbDemandManager get(CmdbDemandManager entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(InsertDemandEntity entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(InsertDemandEntity entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbDemandManager entity);

    /**
     * 获取表格头
     * @return
     */
    List<Map<String, Object>> getTableHeader();
    
    List<Map<String, Object>> getTypeAndValue();

    /**
     * 获取需求详情
     * @param demandId 需求ID
     * @return
     */
    InsertDemandEntity getDemandInfoId(String demandId);
    
    void insertExcelData(CmdbDemandManager demandManager, List<CmdbDemandResourceTypeValue> valueList);
}