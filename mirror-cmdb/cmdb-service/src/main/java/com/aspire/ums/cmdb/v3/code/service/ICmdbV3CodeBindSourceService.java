package com.aspire.ums.cmdb.v3.code.service;
import java.util.List;
import com.aspire.ums.cmdb.v3.code.payload.CmdbV3CodeBindSource;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:19
*/
public interface ICmdbV3CodeBindSourceService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbV3CodeBindSource> list();

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbV3CodeBindSource get(CmdbV3CodeBindSource entity);

    /**
     * 根据主键ID 获取数据信息
     * @param codeId 码表ID
     * @return 返回实例信息的数据信息
     */
    CmdbV3CodeBindSource getByCodeId(String codeId);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbV3CodeBindSource entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbV3CodeBindSource entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbV3CodeBindSource entity);

    /**
     * 根据码表删除实例
     * @param codeId 码表ID
     */
    void deleteByCodeId(String codeId);
}