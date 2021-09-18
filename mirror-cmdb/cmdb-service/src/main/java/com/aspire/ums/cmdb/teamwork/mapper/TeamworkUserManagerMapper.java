package com.aspire.ums.cmdb.teamwork.mapper;
import java.util.List;

import com.aspire.mirror.common.entity.Page;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkUserManagerDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 描述：
* @author
* @date 2021-03-11 14:29:17
*/
@Mapper
public interface TeamworkUserManagerMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<TeamworkUserManagerDTO> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<TeamworkUserManagerDTO> listByEntity(Page page);
    
    int listByEntityCount(Page page);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    TeamworkUserManagerDTO get(TeamworkUserManagerDTO entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(TeamworkUserManagerDTO entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(TeamworkUserManagerDTO entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(String id);

	void insertBatch(List<TeamworkUserManagerDTO> list);

	void deleteByids(@Param("ids") List<String> ids,@Param("twTeamworkId") String twTeamworkId);
}