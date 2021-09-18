package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.teamwork;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.migu.tsg.microservice.atomicservice.composite.common.FtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.cmdb.teamwork.IComTeamworkAPI;
import com.aspire.mirror.composite.service.cmdb.teamwork.payload.ComResultVo;
import com.aspire.mirror.composite.service.cmdb.teamwork.payload.ComTeamworkMessageVO;
import com.aspire.mirror.composite.service.cmdb.teamwork.payload.ComTeamworkReq;
import com.aspire.mirror.composite.service.cmdb.teamwork.payload.ComTeamworkVO;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkMessageVO;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkReq;
import com.aspire.ums.cmdb.teamwork.payload.TeamworkVO;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.teamwork.TeamworkClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.UUIDUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ComTeamworkController implements IComTeamworkAPI {

	@Autowired
	private TeamworkClient teamworkClient;
	
	@Autowired
    private FtpService ftpService;
	
	@Value("${teamwork.ftpPath:/teamwork/}")
	private String ftpPath;

	@Override
	public PageResponse<ComTeamworkVO> pageList(@RequestBody ComTeamworkReq pageRequset) {
		if (pageRequset == null) {
			log.warn("pageList param pageRequset is null");
			return null;
		}
		TeamworkReq req = jacksonBaseParse(TeamworkReq.class, pageRequset);
		//req.setPageNo(pageRequset.getPageNo());
		//req.setPageSize(pageRequset.getPageSize());
		PageResponse<TeamworkVO> page = teamworkClient.pageList(req);

		PageResponse<ComTeamworkVO> result = new PageResponse<ComTeamworkVO>();
		result.setCount(page.getCount());
		result.setResult(jacksonBaseParse(ComTeamworkVO.class, page.getResult()));
		return result;
	}

	@Override
	public ComResultVo deleteTeamwork(@RequestParam(value = "id") String id) {
		if (StringUtils.isEmpty(id)) {
			log.warn("deleteTeamwork id alertId is null");
			return null;
		}
		ResultVo rs = teamworkClient.deleteTeamwork(id);
		
		return jacksonBaseParse(ComResultVo.class, rs);
	}

	@Override
	public ComResultVo createTeamwork(@RequestBody ComTeamworkVO teamwork) {
		teamwork.setTwStatus(TearworkStatusEnum.WORKING.getValue());
		ResultVo rs = teamworkClient.createTeamwork(jacksonBaseParse(TeamworkVO.class, teamwork));

		return jacksonBaseParse(ComResultVo.class, rs);
	}

	@Override
	public ComResultVo updateTeamwork(@RequestBody ComTeamworkVO teamwork) {

		ResultVo rs = teamworkClient.updateTeamwork(jacksonBaseParse(TeamworkVO.class, teamwork));

		return jacksonBaseParse(ComResultVo.class, rs);
	}
	
	@Override
	public ComResultVo closeTeamwork(@RequestBody ComTeamworkVO teamwork) {
		
		Date endTiem = new Date();
		Date startTime = DateUtils.parseDate(teamwork.getTwStartTime(), DateUtils.DATA_PATTERN_FULL_SYMBOL);
		long useTime = (endTiem.getTime()-startTime.getTime())/1000;
		
		TeamworkVO vo = new TeamworkVO();
		vo.setTwUseTime( useTime+"");
		vo.setTwStatus(TearworkStatusEnum.FINISHED.getValue());
		vo.setTwEndTime(DateUtils.getDateStr(endTiem,  DateUtils.DATA_PATTERN_FULL_SYMBOL));
		vo.setId(teamwork.getId());
		ResultVo rs = teamworkClient.updateTeamworkOnly(vo);

		return jacksonBaseParse(ComResultVo.class, rs);
	}
	@Override
  public ComResultVo restartTeamwork(@RequestBody ComTeamworkVO teamwork) {
		
		TeamworkVO vo = new TeamworkVO();
		vo.setId(teamwork.getId());
		vo.setTwStatus(TearworkStatusEnum.WORKING.getValue());
		
		ResultVo rs = teamworkClient.restartTeamwork(vo);

		return jacksonBaseParse(ComResultVo.class, rs);
	}
  
  @Override
  public ComResultVo labelSummary(@RequestBody ComTeamworkVO teamwork) {
	  teamwork.setTwStatus(TearworkStatusEnum.SUMMARIZED.getValue());
		ResultVo rs = teamworkClient.labelSummary(jacksonBaseParse(TeamworkVO.class, teamwork));

		return jacksonBaseParse(ComResultVo.class, rs);
	}
	
	@Override
	public ComResultVo importTeamWorkMsg(ComTeamworkMessageVO msgVO
			, @PathVariable(value = "filename") MultipartFile file) {
		ComResultVo comRsVO = new ComResultVo();
        Map<String, String> ftpMap = new HashMap<>();
        try {
        	String uuid = UUIDUtil.getUUID();
        	msgVO.setId(uuid);
            String[] fileNameArray =file.getOriginalFilename().split("\\.");
            ftpMap = ftpService.uploadImageToFTP2(ftpPath,uuid + "." + fileNameArray[1], file.getInputStream());
            if (!"success".equals(ftpMap.get("code"))) {
                throw new RuntimeException(ftpMap.get("message"));
            }
            String path = ftpMap.get("path");
            msgVO.setContentUrl(path);
            ResultVo rsVO = teamworkClient.createMsg(jacksonBaseParse(TeamworkMessageVO.class, msgVO));
            comRsVO = jacksonBaseParse(ComResultVo.class, rsVO);
        } catch (IOException e) {
        	comRsVO.setSuccess(false);
        	comRsVO.setMsg("上传失败" + e.getMessage());
        	log.error("上传失败",e);
        } catch (Exception e) {
        	comRsVO.setSuccess(false);
        	comRsVO.setMsg(e.getMessage());
        	log.error("上传失败",e);
        }
        return comRsVO;
    }
}
