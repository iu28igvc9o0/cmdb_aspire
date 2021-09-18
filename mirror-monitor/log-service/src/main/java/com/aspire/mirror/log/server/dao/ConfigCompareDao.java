package com.aspire.mirror.log.server.dao;

import com.aspire.mirror.log.server.dao.po.ConfigCompare;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author baiwenping
 * @Title: ConfigCompareDao
 * @Package com.aspire.mirror.alert.server.dao
 * @Description: TODO
 * @date 2020/12/16 11:01
 */
@Mapper
public interface ConfigCompareDao {

    /**
     *
     * @return
     */
    List<ConfigCompare> listAll();

    /**
     *
     * @param map
     * @return
     */
    List<ConfigCompare> listByEntity(Map<String, Object> map);

    /**
     *
     * @param id
     * @return
     */
    ConfigCompare get(@Param("id") Integer id);

    /**
     *
     * @param ids
     * @return
     */
    List<ConfigCompare> getByIds(@Param("ids") List<Integer> ids);
    /**
     *
     * @param map
     * @return
     */
    List<ConfigCompare>  findPageWithResult(Map<String, Object> map);

    /**
     *
     * @param map
     * @return
     */
    int findPageWithCount(Map<String, Object> map);

    /**
     *
     * @param configCompare
     */
    void insert (ConfigCompare configCompare);

    /**
     *
     * @param list
     */
    void insertBatch (@Param("list") List<ConfigCompare> list);

    /**
     *
     * @param configCompare
     */
    void update (ConfigCompare configCompare);

    /**
     *
     * @param id
     */
    void delete(@Param("id") Integer id);
}
