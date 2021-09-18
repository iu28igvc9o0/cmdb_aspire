package com.aspire.ums.cmdb.teamwork.service.impl;
import com.aspire.ums.cmdb.teamwork.service.ITeamworkMessageReadManagerService;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkMessageReadManagerDTO;
import com.aspire.ums.cmdb.teamwork.mapper.TeamworkMessageReadManagerMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
* 描述：
* @author
* @date 2021-03-11 14:29:15
*/
@Service
public class TeamworkMessageReadManagerServiceImpl implements ITeamworkMessageReadManagerService {

    @Autowired
    private TeamworkMessageReadManagerMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<TeamworkMessageReadManagerDTO> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public TeamworkMessageReadManagerDTO get(TeamworkMessageReadManagerDTO entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(TeamworkMessageReadManagerDTO entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(TeamworkMessageReadManagerDTO entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(TeamworkMessageReadManagerDTO entity) {
        mapper.delete(entity);
    }
}