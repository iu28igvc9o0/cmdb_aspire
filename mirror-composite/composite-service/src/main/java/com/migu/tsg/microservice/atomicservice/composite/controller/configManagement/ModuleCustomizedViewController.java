package com.migu.tsg.microservice.atomicservice.composite.controller.configManagement;

import com.aspire.mirror.composite.service.configManagement.IModuleCustomizedViewService;
import com.aspire.mirror.composite.service.configManagement.payload.ModuleCustomizedViewPayload;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.ModuleCustomizedViewServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedViewRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedViewUpdateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ModuleCustomizedViewDTO;

import tk.mybatis.mapper.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**   
 * <p>
 * 页面定制
 * </p>
 * @title ModuleCustomizedCotroller.java
 * @package com.migu.tsg.microservice.atomicservice.composite.controller.configManagement 
 * @author 
 * @version 
 */
@RestController
public class ModuleCustomizedViewController extends CommonResourceController implements IModuleCustomizedViewService {


	
	@Autowired
	private ModuleCustomizedViewServiceClient moduleCustomizedViewServiceClient;
	
	
	 protected String modelId = "index";
	
//	@Override
//	@Authentication(anonymous = true)
//	@ResAction(resType = "ModuleCustomizedView", action = "insert")

	
//	@Override
//	public ModuleCustomizedViewPayload saveModuleCustomizedView(ModuleCustomizedViewPayload m) {
//		ModuleCustomizedViewRequest dto=new ModuleCustomizedViewRequest();
//		dto.setUserId(m.getUserId());
//		dto.setModuleId(m.getModuleId());
//		List<ModuleCustomizedViewDTO> pay=moduleCustomizedViewServiceClient.select(dto);
//		Object returnO=null;
//		//存在则修改
//		if(pay!=null&&pay.size()>0) {
//			returnO =moduleCustomizedViewServiceClient.modifyByPrimaryKey(PayloadParseUtil.jacksonBaseParse(ModuleCustomizedUpdateRequest.class,m));
//		}
//		//否则为新增
//		else {
//			returnO=moduleCustomizedViewServiceClient.createdModuleCustomized(PayloadParseUtil.jacksonBaseParse(ModuleCustomizedCreateRequest.class,m));
//		}
//		return PayloadParseUtil.jacksonBaseParse(ModuleCustomizedViewPayload.class,returnO);
//	}
	
	
	@Override
	@ResAction(resType = "ModuleCustomizedView", action = "create")
	public ResponseEntity<String> saveModuleCustomizedView(@RequestBody ModuleCustomizedViewPayload m) {
		RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
	    resAuthHelper.resourceActionVerify(reqCtx.getUser(), new RbacResource(), reqCtx.getResAction(), reqCtx
                .getFlattenConstraints());
		ModuleCustomizedViewRequest dto=new ModuleCustomizedViewRequest();
		dto.setUserId(m.getUserId());
		if(m.getName()!=null) {
			dto.setName(m.getName());
		}
		List<ModuleCustomizedViewDTO> pay=moduleCustomizedViewServiceClient.select(dto);
		Object returnO=null;
		//存在则修改
		if(pay!=null&&pay.size()>0) {
			throw new RuntimeException("the view  name is exist");
		}
		//否则为新增
		else {
			if(StringUtil.isEmpty(m.getModuleId())) {
				m.setModuleId(modelId);
			}
			if(m.getCreateTime() == null) {
				m.setCreateTime(new Date());
			}
			return moduleCustomizedViewServiceClient.createdModuleCustomizedView(PayloadParseUtil.jacksonBaseParse(ModuleCustomizedViewRequest.class,m));
		}
		
	}
	
	
	
	@Override
	@ResAction(resType = "ModuleCustomizedView", action = "update")
	public ResponseEntity<String> designView(@RequestBody  ModuleCustomizedViewPayload m) {
		RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
	    resAuthHelper.resourceActionVerify(reqCtx.getUser(), new RbacResource(), reqCtx.getResAction(), reqCtx
                .getFlattenConstraints());
		 moduleCustomizedViewServiceClient.modifyByPrimaryKey(PayloadParseUtil.jacksonBaseParse(ModuleCustomizedViewUpdateRequest.class,m));
		 return new ResponseEntity<String>("success",HttpStatus.OK);
		
	}


	@Override
	@ResAction(resType = "ModuleCustomizedView", action = "delete")
	public ResponseEntity<String> deleteByPrimaryKey(@PathVariable("id") String id) {
		RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
	    resAuthHelper.resourceActionVerify(reqCtx.getUser(), new RbacResource(), reqCtx.getResAction(), reqCtx
                .getFlattenConstraints());
		moduleCustomizedViewServiceClient.deleteByPrimaryKey(id);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@Override
	@ResAction(resType = "ModuleCustomizedView", action = "update")
	public  ResponseEntity<String> updateModuleCustomizedView(@RequestBody 
			ModuleCustomizedViewPayload m) {
		RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
	    resAuthHelper.resourceActionVerify(reqCtx.getUser(), new RbacResource(), reqCtx.getResAction(), reqCtx
                .getFlattenConstraints());
		 moduleCustomizedViewServiceClient.modifyByPrimaryKey(PayloadParseUtil.jacksonBaseParse(ModuleCustomizedViewUpdateRequest.class,m));
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@Override
	 @ResAction(action = "view", resType = "ModuleCustomizedView")
	public List<ModuleCustomizedViewPayload> select(@RequestBody ModuleCustomizedViewPayload m) {
		RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
	    resAuthHelper.resourceActionVerify(reqCtx.getUser(), new RbacResource(), reqCtx.getResAction(), reqCtx
                .getFlattenConstraints());
		ModuleCustomizedViewRequest dto=new ModuleCustomizedViewRequest();
		if(StringUtil.isNotEmpty(m.getId())) {
			dto.setId(m.getId());
		}
		if(!reqCtx.getUser().isAdmin() && !reqCtx.getUser().isSuperUser()) {
			if(StringUtil.isNotEmpty(m.getUserId())){
				dto.setUserId(m.getUserId());
			}
		}
		if(StringUtil.isNotEmpty(m.getSystemId())){
			dto.setUserId(m.getSystemId());
		}
		
		if(StringUtil.isNotEmpty(m.getName())){
			dto.setUserId(m.getName());
		}
		
		List<ModuleCustomizedViewDTO> pay=moduleCustomizedViewServiceClient.select(dto);
		return PayloadParseUtil.jacksonBaseParse(ModuleCustomizedViewPayload.class,pay);
	}






}
