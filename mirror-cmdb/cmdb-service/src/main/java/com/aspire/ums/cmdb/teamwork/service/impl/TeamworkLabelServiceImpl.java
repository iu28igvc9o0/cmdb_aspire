package com.aspire.ums.cmdb.teamwork.service.impl;
import com.aspire.ums.cmdb.teamwork.service.ITeamworkLabelService;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkLabelDTO;
import com.aspire.ums.cmdb.teamwork.mapper.TeamworkLabelMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
* 描述：
* @author
* @date 2021-03-11 14:29:08
*/
@Service
public class TeamworkLabelServiceImpl implements ITeamworkLabelService {

    @Autowired
    private TeamworkLabelMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<TeamworkLabelDTO> list(TeamworkLabelDTO entity) {
        return mapper.listByEntity(entity);
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public TeamworkLabelDTO get(TeamworkLabelDTO entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(TeamworkLabelDTO entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(TeamworkLabelDTO entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(TeamworkLabelDTO entity) {
        mapper.delete(entity);
    }
}