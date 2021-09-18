package com.aspire.ums.cmdb.teamwork.mapper;
import java.util.List;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkLabelDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 描述：
* @author
* @date 2021-03-11 14:29:08
*/
@Mapper
public interface TeamworkLabelMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<TeamworkLabelDTO> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<TeamworkLabelDTO> listByEntity(TeamworkLabelDTO entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    TeamworkLabelDTO get(TeamworkLabelDTO entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(TeamworkLabelDTO entity);
    
    void insertBatch(List<TeamworkLabelDTO> list);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(TeamworkLabelDTO entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(TeamworkLabelDTO entity);

	void deleteByids(@Param("ids") List<String> ids,@Param("twTeamworkId") String twTeamworkId);
}