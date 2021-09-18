package com.aspire.ums.cmdb.teamwork.service;
import java.util.List;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkLabelSummaryDTO;

/**
* 描述：
* @author
* @date 2021-03-11 14:29:11
*/
public interface ITeamworkLabelSummaryService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<TeamworkLabelSummaryDTO> list(TeamworkLabelSummaryDTO entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    TeamworkLabelSummaryDTO get(TeamworkLabelSummaryDTO entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(TeamworkLabelSummaryDTO entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(TeamworkLabelSummaryDTO entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(TeamworkLabelSummaryDTO entity);
}