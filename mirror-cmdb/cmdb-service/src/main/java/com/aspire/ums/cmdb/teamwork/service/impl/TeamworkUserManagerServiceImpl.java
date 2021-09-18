package com.aspire.ums.cmdb.teamwork.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.util.PageUtil;
import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkUserManagerDTO;
import com.aspire.ums.cmdb.teamwork.mapper.TeamworkUserManagerMapper;
import com.aspire.ums.cmdb.teamwork.service.ITeamworkUserManagerService;
import com.google.common.collect.Lists;

/**
* 描述：
* @author
* @date 2021-03-11 14:29:17
*/
@Service
public class TeamworkUserManagerServiceImpl implements ITeamworkUserManagerService {

    @Autowired
    private TeamworkUserManagerMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public PageBean<TeamworkUserManagerDTO> pageList(PageRequest pageRequest) {
		Page page = PageUtil.convert(pageRequest);
		PageBean<TeamworkUserManagerDTO> pageBean = new PageBean<TeamworkUserManagerDTO>();
		int count = mapper.listByEntityCount(page);
		List<TeamworkUserManagerDTO> list = Lists.newArrayList();
		if (count > 0) {
			list = mapper.listByEntity(page);
		}
		pageBean.setCount(count);
		pageBean.setResult(list);
		return pageBean;
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public TeamworkUserManagerDTO get(TeamworkUserManagerDTO entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(TeamworkUserManagerDTO entity) {
        mapper.insert(entity);
    }
    
    /**
     * 批量新增实例
     * @param entity 实例数据
     * @return
     */
    public void insertBatch(List<TeamworkUserManagerDTO> entityList) {
        mapper.insertBatch(entityList);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(TeamworkUserManagerDTO entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(String id) {
        mapper.delete(id);
    }

	@Override
	public List<TeamworkUserManagerDTO> list(TeamworkUserManagerDTO entity) {
		Page page = new Page();
		page.getParams().put("twTeamworkId", entity.getTwTeamworkId());
		page.setPageSize(null);
		return mapper.listByEntity(page);
	}
}