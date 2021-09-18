package com.aspire.ums.cmdb.teamwork.service.impl;
import com.aspire.ums.cmdb.teamwork.service.ITeamworkLabelSummaryService;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkLabelSummaryDTO;
import com.aspire.ums.cmdb.teamwork.mapper.TeamworkLabelSummaryMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
* 描述：
* @author
* @date 2021-03-11 14:29:11
*/
@Service
public class TeamworkLabelSummaryServiceImpl implements ITeamworkLabelSummaryService {

    @Autowired
    private TeamworkLabelSummaryMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<TeamworkLabelSummaryDTO> list(TeamworkLabelSummaryDTO entity) {
        return mapper.listByEntity(entity);
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public TeamworkLabelSummaryDTO get(TeamworkLabelSummaryDTO entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(TeamworkLabelSummaryDTO entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(TeamworkLabelSummaryDTO entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(TeamworkLabelSummaryDTO entity) {
        mapper.delete(entity);
    }
}