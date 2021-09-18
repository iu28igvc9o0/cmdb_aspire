package com.aspire.mirror.composite.service.desk.bpm;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiOperation;

/**
 * @projectName: BpmAnnouncementAPI
 * @description: 获取割接公告
 * @author: tongzhihong
 * @create: 2020-09-14 10:37
 **/
@RequestMapping("${version}/bpm/runTime/announcement/")
public interface BpmAnnouncementAPI {
	
	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "获取割接公告", httpMethod = "GET", notes = "获取割接公告",tags = {"desk API"})
	public Object list() throws Exception;
}
