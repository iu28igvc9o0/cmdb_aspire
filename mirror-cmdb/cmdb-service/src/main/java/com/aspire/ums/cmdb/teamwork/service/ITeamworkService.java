package com.aspire.ums.cmdb.teamwork.service;
import java.util.List;

import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkDTO;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkVO;

/**
* 描述：
* @author
* @date 2021-03-11 14:29:04
*/
public interface ITeamworkService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
	PageResponse<TeamworkDTO> list(PageRequest pageRequest);
	
	
	List<TeamworkDTO> getListByUser(PageRequest pageRequest);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    TeamworkDTO get(String id);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    TeamworkVO insert(TeamworkVO entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void updateAll(TeamworkVO entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(String id);


	void update(TeamworkVO entity);


	void labelSummary(TeamworkVO teamworkVO);
}