package com.aspire.ums.cmdb.v3.screen.service;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenProblemInfo;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenProblemInfoRequest;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2020-07-03 10:17:19
*/
public interface ICmdbScreenProblemInfoService {

    /*
    *   查询列表，分页或者不分页
    * */
    Result<CmdbScreenProblemInfo> listByPage(CmdbScreenProblemInfoRequest req);

     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbScreenProblemInfo> list();

    /**
     * 根据主键ID 获取数据信息
     * @param id 实例ID
     * @return 返回实例信息的数据信息
     */
    CmdbScreenProblemInfo get(String id);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    Map<String,Object> insert(CmdbScreenProblemInfo entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    Map<String,Object> update(CmdbScreenProblemInfo entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbScreenProblemInfo entity);
}