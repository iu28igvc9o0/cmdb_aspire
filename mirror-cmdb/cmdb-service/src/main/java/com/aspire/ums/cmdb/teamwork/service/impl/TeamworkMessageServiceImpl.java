package com.aspire.ums.cmdb.teamwork.service.impl;
import com.aspire.ums.cmdb.teamwork.service.ITeamworkMessageService;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkMessageDTO;
import com.aspire.ums.cmdb.teamwork.mapper.TeamworkMessageMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
* 描述：
* @author
* @date 2021-03-11 14:29:13
*/
@Service
public class TeamworkMessageServiceImpl implements ITeamworkMessageService {

    @Autowired
    private TeamworkMessageMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<TeamworkMessageDTO> list(TeamworkMessageDTO entity) {
        return mapper.listByEntity(entity);
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public TeamworkMessageDTO get(TeamworkMessageDTO entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(TeamworkMessageDTO entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(TeamworkMessageDTO entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(TeamworkMessageDTO entity) {
        mapper.delete(entity);
    }
}