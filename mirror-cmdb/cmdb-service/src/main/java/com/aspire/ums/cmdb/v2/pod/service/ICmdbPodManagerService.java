package com.aspire.ums.cmdb.v2.pod.service;
import java.util.List;
import com.aspire.ums.cmdb.v2.pod.entity.CmdbPodManager;

/**
* 描述：
* @author
* @date 2019-06-17 17:29:54
*/
public interface ICmdbPodManagerService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbPodManager> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbPodManager> listByEntity(CmdbPodManager entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbPodManager get(CmdbPodManager entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbPodManager entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbPodManager entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbPodManager entity);
}