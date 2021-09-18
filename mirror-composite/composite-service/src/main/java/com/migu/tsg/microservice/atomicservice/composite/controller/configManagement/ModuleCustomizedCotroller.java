package com.migu.tsg.microservice.atomicservice.composite.controller.configManagement;

import com.aspire.mirror.composite.service.configManagement.IModuleCustomizedService;
import com.aspire.mirror.composite.service.configManagement.payload.ModuleCustomizedPayload;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.ModuleCustomizedServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.Authentication;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedUpdateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ModuleCustomizedDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.ModuleCustomizedVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**   
 * <p>
 * 页面定制
 * </p>
 * @title ModuleCustomizedCotroller.java
 * @package com.migu.tsg.microservice.atomicservice.composite.controller.configManagement 
 * @author 曾祥华
 * @version 0.1 2019年7月17日
 */
@RestController
public class ModuleCustomizedCotroller extends CommonResourceController implements IModuleCustomizedService {

	@Autowired
	private ModuleCustomizedServiceClient moduleCustomizedServiceClient;
	
	@Override
//	@Authentication(anonymous = true)
	public ModuleCustomizedPayload saveModuleCustomized(ModuleCustomizedPayload m) {
		ModuleCustomizedCreateRequest dto=new ModuleCustomizedCreateRequest();
		dto.setUserId(m.getUserId());
		dto.setModuleId(m.getModuleId());
		List<ModuleCustomizedDTO> pay=moduleCustomizedServiceClient.select(dto);
		Object returnO=null;
		//存在则修改
		if(pay!=null&&pay.size()>0) {
			returnO =moduleCustomizedServiceClient.modifyByPrimaryKey(PayloadParseUtil.jacksonBaseParse(ModuleCustomizedUpdateRequest.class,m));
		}
		//否则为新增
		else {
			returnO=moduleCustomizedServiceClient.createdModuleCustomized(PayloadParseUtil.jacksonBaseParse(ModuleCustomizedCreateRequest.class,m));
		}
		return PayloadParseUtil.jacksonBaseParse(ModuleCustomizedPayload.class,returnO);
	}

	@Override
	public ResponseEntity<String> deleteByPrimaryKey(String moduleCustomizedId) {
		return moduleCustomizedServiceClient.deleteByPrimaryKey(moduleCustomizedId);
	}

	@Override
	public ModuleCustomizedPayload findByPrimaryKey(String moduleCustomizedId) {
		ModuleCustomizedVO findByPrimaryKey = moduleCustomizedServiceClient.findByPrimaryKey(moduleCustomizedId);
		return PayloadParseUtil.jacksonBaseParse(ModuleCustomizedPayload.class,findByPrimaryKey);
	}

	@Override
	public List<ModuleCustomizedPayload> select(ModuleCustomizedPayload moduleCustomized) {
		ModuleCustomizedCreateRequest mr=PayloadParseUtil.jacksonBaseParse(ModuleCustomizedCreateRequest.class,moduleCustomized);
		List<ModuleCustomizedDTO> list=moduleCustomizedServiceClient.select(mr);
		return PayloadParseUtil.jacksonBaseParse(ModuleCustomizedPayload.class,list);
	}

}
