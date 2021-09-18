package com.aspire.mirror.composite.service.desk;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * @projectName: CloudCustomerServiceAPI
 * @description: 接口
 * @author: menglinjie
 * @create: 2020-10-28 10:37
 **/
@Api("ccs")
@RequestMapping("${version}/desk")
public interface CloudCustomerServiceAPI {
	/**
	 *获取云客服系统token
	 * @param
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/ccs/getToken", produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "获取云客服系统token", httpMethod = "GET", notes = "获取云客服系统token",tags = {"cloudCustomerService API"})
	public Object getToken(@RequestParam("module")String module);

	/**
	 *获取客户数据库版本号、字段结构
	 * @param
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/ccs/customer/getTemplate", produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "获取客户数据库版本号、字段结构", httpMethod = "POST", notes = "获取客户数据库版本号、字段结构",tags = {"cloudCustomerService API"})
	public Object getTemplate();

	/**
	 *新增客户资料数据
	 * @param
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/ccs/customer/insert", produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "新增客户资料数据", httpMethod = "POST", notes = "新增客户资料数据",tags = {"cloudCustomerService API"})
	public Object insert();

	@RequestMapping(value="/ccs/knowledge/list", method= RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "查询知识库", httpMethod = "POST", notes = "查询知识库",tags = {"cloudCustomerService API"})
	Object listKnowledgeBase(@RequestBody Map<String,Object> req);

	@RequestMapping(value="/ccs/knowledge/insert", method= RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "添加知识库", httpMethod = "POST", notes = "添加知识库",tags = {"cloudCustomerService API"})
	void insertKnowledgeBase() throws IOException;

	@RequestMapping(value="/ccs/knowledge/{id}", method= RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "查询知识库详情", httpMethod = "GET", notes = "查询知识库详情",tags = {"cloudCustomerService API"})
	Object getKnowledgeDetailById(@PathVariable("id") String id,
								  @RequestParam("kmType") String kmType);
}
