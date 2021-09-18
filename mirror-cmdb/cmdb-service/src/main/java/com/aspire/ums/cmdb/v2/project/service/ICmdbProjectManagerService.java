package com.aspire.ums.cmdb.v2.project.service;
import java.util.List;
import com.aspire.ums.cmdb.v2.project.entity.CmdbProjectManager;

/**
* 描述：
* @author
* @date 2019-06-17 17:30:43
*/
public interface ICmdbProjectManagerService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbProjectManager> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbProjectManager> listByEntity(CmdbProjectManager entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbProjectManager get(CmdbProjectManager entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbProjectManager entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbProjectManager entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbProjectManager entity);
}