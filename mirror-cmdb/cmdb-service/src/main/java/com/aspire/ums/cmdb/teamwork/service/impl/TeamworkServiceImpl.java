package com.aspire.ums.cmdb.teamwork.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.PageUtil;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkDTO;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkLabelDTO;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkUserManagerDTO;
import com.aspire.ums.cmdb.teamwork.mapper.TeamworkLabelMapper;
import com.aspire.ums.cmdb.teamwork.mapper.TeamworkMapper;
import com.aspire.ums.cmdb.teamwork.mapper.TeamworkUserManagerMapper;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkLabelVO;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkUserManagerVO;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkVO;
import com.aspire.ums.cmdb.teamwork.service.ITeamworkService;
import com.aspire.ums.cmdb.util.DateUtilsNew;
import com.aspire.ums.cmdb.util.PayloadParseUtil;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * 描述：
 * 
 * @author
 * @date 2021-03-11 14:29:04
 */
@Slf4j
@Service
@Transactional
public class TeamworkServiceImpl implements ITeamworkService {

	@Autowired
	private TeamworkMapper mapper;
	

	@Autowired
	private TeamworkLabelMapper labelMapper;
	

	@Autowired
	private TeamworkUserManagerMapper userMapper;

	/**
	 * 获取所有实例
	 * 
	 * @return 返回所有实例数据
	 */
	@Override
	public PageResponse<TeamworkDTO> list(PageRequest pageRequest) {

		Page page = PageUtil.convert(pageRequest);
		PageResponse<TeamworkDTO> pageBean = new PageResponse<TeamworkDTO>();
		int count = mapper.listByEntityCount(page);
		List<TeamworkDTO> list = Lists.newArrayList();
		if (count > 0) {
			list = mapper.listByEntity(page);
		}
		pageBean.setCount(count);
		pageBean.setResult(list);
		return pageBean;
	}

	/**
	 * 根据主键ID 获取数据信息
	 * 
	 * @param entity 实例信息
	 * @return 返回指定ID的数据信息
	 */
	@Override
	public TeamworkDTO get(String id) {
		return mapper.get(id);
	}

	/**
	 * 新增实例
	 * 
	 * @param entity 实例数据
	 * @return
	 */
	@Override
	public TeamworkVO insert(TeamworkVO entity) {
		TeamworkDTO tw = new TeamworkDTO();
		BeanUtils.copyProperties(entity, tw);
		String twId =entity.getId();
		String twCode = UUIDUtil.getDateTimeUniqueId();
		tw.setId(twId);
		tw.setTwCode(twCode);
		
		//tw.setTwStatus("1");
		String date = DateUtilsNew.format(new Date(), DateUtilsNew.DATE_TIME_FORMAT2);
		tw.setTwStartTime(date);
		//tw.setTwStartDateTime();
		
		entity.setTwCode(twCode);
		entity.setId(twId);
		entity.setTwStartTime(date);
		//entity.setTwStatus("协同中");
		mapper.insert(tw);
		
		//插入标签
		List<TeamworkLabelVO>  labelList = entity.getLabelList();
		if(null!=labelList) {
			List<TeamworkLabelDTO>  labelDtoList = Lists.newArrayList();
			for(TeamworkLabelVO label:labelList) {
				TeamworkLabelDTO labelDto = new TeamworkLabelDTO();
				BeanUtils.copyProperties(label, labelDto);
				labelDto.setId(UUIDUtil.getUUID());
				labelDto.setTwTeamworkId(twId);
				labelDto.setSummaryContent(null);
				
				label.setId(UUIDUtil.getUUID());
				label.setTwTeamworkId(twId);
				label.setSummaryContent(null);
				labelDtoList.add(labelDto);
				
		    }
			labelMapper.insertBatch(labelDtoList);
		}
		//插入用户    
		List<TeamworkUserManagerVO>  userList = entity.getUserList();
		if(null!=userList) {
			List<TeamworkUserManagerDTO>  userDtoList = Lists.newArrayList();
			for(TeamworkUserManagerVO user:userList) {
				TeamworkUserManagerDTO userDto = new TeamworkUserManagerDTO();
				BeanUtils.copyProperties(user, userDto);
				userDto.setId(UUIDUtil.getUUID());
				userDto.setTwTeamworkId(twId);
				
				user.setId(UUIDUtil.getUUID());
				user.setTwTeamworkId(twId);
				userDtoList.add(userDto);
		    }
			userMapper.insertBatch(userDtoList);
		}
		
		//entity.setUserList(userList);
		//entity.setLabelList(labelList);
		return entity;
	}
	@Override
	public void update(TeamworkVO entity) {
		TeamworkDTO tw = new TeamworkDTO();
		BeanUtils.copyProperties(entity, tw);
		mapper.update(tw);
	}
	/**
	 * 修改实例
	 * 
	 * @param entity 实例数据
	 * @return
	 */
	@Override
	public void updateAll(TeamworkVO entity) {
		update(entity);//更新协同作战
		String twId = entity.getId();
		//插入标签
		List<TeamworkLabelVO>  labelList = entity.getLabelList();
		if(null!=labelList) {
			List<TeamworkLabelDTO>  labelDtoList = Lists.newArrayList();
			List<String> ids = Lists.newArrayList();
			for(TeamworkLabelVO label:labelList) {
				String labelId = label.getId();
				TeamworkLabelDTO labelDto = new TeamworkLabelDTO();
				BeanUtils.copyProperties(label, labelDto);
				if(StringUtils.isEmpty(labelId)) {
					labelDto.setTwTeamworkId(twId);
					labelDto.setId(UUIDUtil.getUUID());
					labelDtoList.add(labelDto);
				}else {
					ids.add(labelId);
				}
				
		    }
			//删除标签
			if(ids.size()==0) {
				ids = null;
			}
			labelMapper.deleteByids(ids,twId);
			if(labelDtoList.size()>0) {
				labelMapper.insertBatch(labelDtoList);
			}
			
		}
		//插入用户    
		List<TeamworkUserManagerVO>  userList = entity.getUserList();
		if(null!=userList) {
			List<TeamworkUserManagerDTO>  userDtoList = Lists.newArrayList();
			List<String> ids = Lists.newArrayList();
			for(TeamworkUserManagerVO user:userList) {
				TeamworkUserManagerDTO userDto = new TeamworkUserManagerDTO();
				String id = user.getId();
				BeanUtils.copyProperties(user, userDto);
				if(StringUtils.isEmpty(id)) {
					userDto.setId(UUIDUtil.getUUID());
					userDto.setTwTeamworkId(twId);
				userDtoList.add(userDto);
				}else {
					ids.add(id);
				}
		    }
			//删除用户
			if(ids.size()==0) {
				ids = null;
			}
			userMapper.deleteByids(ids,twId);
			if(userDtoList.size()>0) {
				userMapper.insertBatch(userDtoList);
			}
		}
		
	}

	/**
	 * 删除实例
	 * 
	 * @param entity 实例数据
	 * @return
	 */
	@Override
	public void delete(String id) {
		mapper.delete(id);
		
		userMapper.deleteByids(null, id);
		
		labelMapper.deleteByids(null, id);
		
	}

	@Override
	public List<TeamworkDTO> getListByUser(PageRequest pageRequest) {
		Page page = PageUtil.convert(pageRequest);
		return mapper.getListByUser(page);
	}

	@Override
	public void labelSummary(TeamworkVO teamworkVO) {
		update(teamworkVO);
		List<TeamworkLabelVO>  labelList =  teamworkVO.getLabelList();
		for(TeamworkLabelVO vo:labelList) {
			labelMapper.update(PayloadParseUtil.jacksonBaseParse(TeamworkLabelDTO.class, vo));
		}
		
		
	}
}