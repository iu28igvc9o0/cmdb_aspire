package com.aspire.ums.cmdb.teamwork.web;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.FieldUtil;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.teamwork.ITeamworkAPI;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkDTO;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkLabelDTO;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkMessageDTO;
import com.aspire.ums.cmdb.teamwork.entity.TeamworkUserManagerDTO;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkLabelReq;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkLabelVO;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkMessageVO;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkReq;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkUserManagerVO;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkUserReq;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkVO;
import com.aspire.ums.cmdb.teamwork.service.ITeamworkLabelService;
import com.aspire.ums.cmdb.teamwork.service.ITeamworkMessageService;
import com.aspire.ums.cmdb.teamwork.service.ITeamworkService;
import com.aspire.ums.cmdb.teamwork.service.ITeamworkUserManagerService;
import com.aspire.ums.cmdb.util.DateUtilsNew;
import com.aspire.ums.cmdb.util.PayloadParseUtil;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TeamworkController implements ITeamworkAPI {

	@Autowired
	private ITeamworkService iTeamworkService;
	
	@Autowired
	private ITeamworkLabelService labelService;
	
	@Autowired
	private ITeamworkUserManagerService userManagerService;
	
	@Autowired
	private ITeamworkMessageService messageService;

	/**
	 * 查询作战列表
	 */
	@Override
	public PageResponse<TeamworkVO> pageList(@RequestBody TeamworkReq pageRequset) {
		if (pageRequset == null) {
			log.warn("pageList param pageRequset is null");
			return null;
		}
		PageRequest page = new PageRequest();
		BeanUtils.copyProperties(pageRequset, page);
		Map<String, Object> map = FieldUtil.getFiledMap(pageRequset);
		for (String key : map.keySet()) {
			page.addFields(key, map.get(key));
		}
		PageResponse<TeamworkDTO> pageResult = iTeamworkService.list(page);
		List<TeamworkVO> list = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(pageResult.getResult())) {
			for (TeamworkDTO dto : pageResult.getResult()) {
				TeamworkVO res = new TeamworkVO();
				BeanUtils.copyProperties(dto, res);
				list.add(res);
			}
		}
		PageResponse<TeamworkVO> result = new PageResponse<TeamworkVO>();
		result.setCount(pageResult.getCount());
		result.setResult(list);
		return result;
	}

	/**
	 * 删除作战
	 */
	@Override
	public ResultVo deleteTeamwork(@RequestParam(value = "id") String id) {
		if (StringUtils.isEmpty(id)) {
			log.warn("deleteTeamwork id alertId is null");
			return null;
		}
		ResultVo rs = new ResultVo();
		iTeamworkService.delete(id);

		return rs;
	}

	/**
	 * 创建作战
	 */
	@Override
	public ResultVo createTeamwork(@RequestBody TeamworkVO teamwork) {
		ResultVo rs = new ResultVo();
		String uuid = UUIDUtil.getUUID();
		teamwork.setId(uuid);
		teamwork.setIdJoin(uuid);
		TeamworkVO tw = iTeamworkService.insert(teamwork);
		rs.setData(tw);
		return rs;
	}

	/**
	 * 更新作战
	 */
	@Override
	public ResultVo updateTeamwork(@RequestBody TeamworkVO teamwork) {

		ResultVo rs = new ResultVo();
		iTeamworkService.updateAll(teamwork);

		return rs;
	}
	
	@Override
	public ResultVo updateTeamworkOnly(@RequestBody TeamworkVO teamwork) {
		ResultVo rs = new ResultVo();
		iTeamworkService.update(teamwork);
		return rs;
	}
	
	/**
	 * 根据账户查询作战列表，返回作战信息、标签、参与人
	 */
	@Override
	public List<TeamworkVO> getListByUser(@RequestBody TeamworkUserReq req) {
		PageRequest page = new PageRequest();
		BeanUtils.copyProperties(req, page);
		Map<String, Object> map = FieldUtil.getFiledMap(req);
		for (String key : map.keySet()) {
			page.addFields(key, map.get(key));
		}
		List<TeamworkDTO> twList = iTeamworkService.getListByUser(page);
		List<TeamworkVO> twVOList = Lists.newArrayList();
		for(TeamworkDTO tw:twList) {
			TeamworkVO twVo = new TeamworkVO();
			BeanUtils.copyProperties(tw, twVo);
			
			String twID= tw.getId();
			TeamworkLabelDTO entity = new TeamworkLabelDTO();
			entity.setTwTeamworkId(twID);
			List<TeamworkLabelDTO>  labelList = labelService.list(entity);
			
			List<TeamworkLabelVO> labelVOList =  PayloadParseUtil.jacksonBaseParse(TeamworkLabelVO.class, labelList);
			
			TeamworkUserManagerDTO userEntity = new TeamworkUserManagerDTO();
			userEntity.setTwTeamworkId(twID);
			List<TeamworkUserManagerDTO> userList = userManagerService.list(userEntity);
			List<TeamworkUserManagerVO> userVOList =  PayloadParseUtil.jacksonBaseParse(TeamworkUserManagerVO.class, userList);
			
			twVo.setLabelList(labelVOList);
			twVo.setUserList(userVOList);
			twVOList.add(twVo);
		}
		return twVOList;
	}
	
	/**
	 * 根据作战id、标签id查询聊天信息
	 */
	@Override
	public List<TeamworkMessageVO> getMsg(@RequestBody TeamworkLabelReq req){
		List<TeamworkMessageDTO> msgDto = messageService.list( PayloadParseUtil.jacksonBaseParse(TeamworkMessageDTO.class, req));
		return  PayloadParseUtil.jacksonBaseParse(TeamworkMessageVO.class, msgDto);
	}
	
	/**
	 * 根据作战id、标签id查询标签汇总内容
	 */
	@Override
	public List<TeamworkLabelVO> getMsgSummary(@RequestBody TeamworkLabelReq req){
		TeamworkLabelDTO dto =  PayloadParseUtil.jacksonBaseParse(TeamworkLabelDTO.class, req);
		dto.setId(req.getTwLabelId());
		List<TeamworkLabelDTO> dtoList= labelService.list(dto);
		return  PayloadParseUtil.jacksonBaseParse(TeamworkLabelVO.class, dtoList);
	}
	
	@Override
	public ResultVo createMsg(@RequestBody TeamworkMessageVO msgVO){
		ResultVo rs = new ResultVo();
		msgVO.setId(UUIDUtil.getUUID());
		msgVO.setTwSendTime( DateUtilsNew.format(new Date(), DateUtilsNew.DATE_TIME_FORMAT2));
		messageService.insert(PayloadParseUtil.jacksonBaseParse(TeamworkMessageDTO.class, msgVO));
		return  rs;
	}

	@Override
	public ResultVo labelSummary(@RequestBody TeamworkVO teamworkVO) {
		ResultVo rs = new ResultVo();
		iTeamworkService.labelSummary(teamworkVO);
		return rs;
	}

	@Override
	public TeamworkVO detailTeamwor(@RequestParam(value="id", required = true)String id, int type) {
		TeamworkDTO tw = iTeamworkService.get(id);
		TeamworkVO vo = PayloadParseUtil.jacksonBaseParse(TeamworkVO.class, tw);
		String twId = tw.getId();
		if(type==0) {
			//查询标签列表
			TeamworkLabelDTO label = new TeamworkLabelDTO();
			label.setTwTeamworkId(twId);
			List<TeamworkLabelDTO> labelList = labelService.list(label);
			vo.setLabelList( PayloadParseUtil.jacksonBaseParse(TeamworkLabelVO.class, labelList));
			
			//查询人员列表
			TeamworkUserManagerDTO user = new TeamworkUserManagerDTO();
			user.setTwTeamworkId(twId);
			List<TeamworkUserManagerDTO> userList = userManagerService.list(user);
			vo.setUserList( PayloadParseUtil.jacksonBaseParse(TeamworkUserManagerVO.class, userList));
		}
		return vo;
	}
	
	@Override
	 public ResultVo restartTeamwork(@RequestBody TeamworkVO teamwork) {
		 	ResultVo rs = new ResultVo();
			String id = teamwork.getId();
			TeamworkVO pre = detailTeamwor(id, 0);
			pre.setId(UUIDUtil.getUUID());
			pre.setTwEndTime(null);
			pre.setTwUseTime(null);
			
			TeamworkVO vo = new TeamworkVO();
			String uuid = UUIDUtil.getUUID();
			vo.setId(uuid);
			vo.setIdJoin(teamwork.getId());
			//vo.setTwUseTime( useTime+"");
			//vo.setTwEndTime(DateUtils.getDateStr(endTiem,  DateUtils.DATA_PATTERN_FULL_SYMBOL));
			//vo.setId(teamwork.getId());
			
			iTeamworkService.insert(pre);

			return rs;
		}
}
