package com.aspire.ums.cmdb.teamwork.service;
import java.util.List;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkLabelDTO;

/**
* 描述：
* @author
* @date 2021-03-11 14:29:08
*/
public interface ITeamworkLabelService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<TeamworkLabelDTO> list(TeamworkLabelDTO entity);

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
}