package com.aspire.ums.cmdb.demand.mapper;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.demand.entity.CmdbDemandManager;
import org.apache.ibatis.annotations.Mapper;

/**
* 描述：
* @author
* @date 2019-05-09 16:28:19
*/
@Mapper
public interface CmdbDemandManagerMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbDemandManager> list();

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
    void insert(CmdbDemandManager entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbDemandManager entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbDemandManager entity);

    /**
     * 查询需求信息
     * @param queryMap
     * @return
     */
    List<LinkedHashMap> queryForMap(Map<String, Object> queryMap);

    /**
     * 查询queryForMap方法的数量
     * @param queryMap
     * @return
     */
    Integer queryForMapCount(Map<String, Object> queryMap);
    
    /**
     * 查询需求信息
     * @param queryMap
     * @return
     */
    List<LinkedHashMap> queryExportData(Map<String, Object> queryMap);
    
    /**
     * 查询queryForMap方法的数量
     * @param queryMap
     * @return
     */
    Integer queryExportDataCount(Map<String, Object> queryMap);
}