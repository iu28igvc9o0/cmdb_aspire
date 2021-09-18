package com.aspire.ums.cmdb.teamwork.mapper;
import java.util.List;

import com.aspire.mirror.common.entity.Page;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 描述：
* @author
* @date 2021-03-11 14:29:04
*/
@Mapper
public interface TeamworkMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<TeamworkDTO> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<TeamworkDTO> listByEntity(Page page);
    
    List<TeamworkDTO> getListByUser(Page page);

    int listByEntityCount(Page page);
    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    TeamworkDTO get(@Param("id") String id);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(TeamworkDTO entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(TeamworkDTO entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(String id);
}