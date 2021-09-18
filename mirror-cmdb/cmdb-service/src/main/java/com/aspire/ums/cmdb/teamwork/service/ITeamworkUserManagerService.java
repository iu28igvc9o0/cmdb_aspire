package com.aspire.ums.cmdb.teamwork.service;
import java.util.List;

import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkUserManagerDTO;

/**
* 描述：
* @author
* @date 2021-03-11 14:29:17
*/
public interface ITeamworkUserManagerService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
	 PageBean<TeamworkUserManagerDTO> pageList(PageRequest pageRequest);
	 
	 List<TeamworkUserManagerDTO> list(TeamworkUserManagerDTO entity);

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
}