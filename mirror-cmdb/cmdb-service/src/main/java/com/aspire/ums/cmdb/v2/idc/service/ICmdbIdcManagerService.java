package com.aspire.ums.cmdb.v2.idc.service;
import java.util.List;
import com.aspire.ums.cmdb.v2.idc.entity.CmdbIdcManager;

/**
* 描述：
* @author
* @date 2019-06-17 17:29:30
*/
public interface ICmdbIdcManagerService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbIdcManager> list();

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbIdcManager get(CmdbIdcManager entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbIdcManager entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbIdcManager entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbIdcManager entity);
}