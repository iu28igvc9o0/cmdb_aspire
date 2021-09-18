package com.aspire.ums.cmdb.teamwork.mapper;
import java.util.List;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkMessageDTO;
import org.apache.ibatis.annotations.Mapper;

/**
* 描述：
* @author
* @date 2021-03-11 14:29:13
*/
@Mapper
public interface TeamworkMessageMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<TeamworkMessageDTO> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<TeamworkMessageDTO> listByEntity(TeamworkMessageDTO entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    TeamworkMessageDTO get(TeamworkMessageDTO entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(TeamworkMessageDTO entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(TeamworkMessageDTO entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(TeamworkMessageDTO entity);
}