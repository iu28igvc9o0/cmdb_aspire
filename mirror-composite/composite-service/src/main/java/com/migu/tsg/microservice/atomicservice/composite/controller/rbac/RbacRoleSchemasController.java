package com.migu.tsg.microservice.atomicservice.composite.controller.rbac;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.ResourceSchemaServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeDefine;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.ICompRbacRoleSchemasService;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.RbacRoleSchemasNameResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.RbacRoleSchemasResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.FetchResourceSchemaDetailResponse;

@RestController
@LogCodeDefine("1050221")
public class RbacRoleSchemasController implements ICompRbacRoleSchemasService {

    @Autowired
    private ResourceSchemaServiceClient resSchemaClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(RbacRoleSchemasController.class);
    
    @Override
    @ResponseStatus(HttpStatus.OK)
    @LogCodeDefine("31")
    public List<RbacRoleSchemasResponse> getRoleSchemas() {
        //直接调用rbac接口
        LOGGER.debug("Welcome into getRoleSchemas1 >>>>>");
        List<FetchResourceSchemaDetailResponse> fetchRoleSchemaList = resSchemaClient.fetchRoleSchemaList();
        LOGGER.debug("The result(List<RbacRoleSchemasResponse>) of rbac 'fetchRoleSchemaList'>>>>>>");
        return PayloadParseUtil.jacksonBaseParse(RbacRoleSchemasResponse.class, fetchRoleSchemaList);
    }

    /**
    * getRoleSchemasByType:(role_schema根据resource_type的权限验证). <br/>
    * 作者： yangshilei
    * @param resourceType
    * @return
    */
    @Override
    @ResponseStatus(HttpStatus.OK)
    @LogCodeDefine("32")
    public RbacRoleSchemasResponse getRoleSchemas(@PathVariable("resource_type") String resType) {
        //直接调用rbac接口
        LOGGER.debug("Welcome into getRoleSchemas >>>>>");
        FetchResourceSchemaDetailResponse fetchRoleSchemaDetail = resSchemaClient.fetchRoleSchemaDetail(resType);
        LOGGER.debug("The result(RbacRoleSchemasResponse) of rbac 'fetchRoleSchemaList'>>>>>>");
        return PayloadParseUtil.jacksonBaseParse(RbacRoleSchemasResponse.class, fetchRoleSchemaDetail);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @LogCodeDefine("31")
    public List<RbacRoleSchemasNameResponse> getSchemasTree(@RequestParam(name="id", required=false, defaultValue="all") String id) {
        //直接调用rbac接口
//        List<FetchResourceSchemaDetailResponse> fetchRoleSchemaList = resSchemaClient.fetchChildrenRoleSchemaList(id);
    	List<FetchResourceSchemaDetailResponse> fetchRoleSchemaList = resSchemaClient.fetchFullChildrenRoleSchemaList(id);
        List<RbacRoleSchemasNameResponse> returnList = PayloadParseUtil.jacksonBaseParse(RbacRoleSchemasNameResponse.class, fetchRoleSchemaList);
//        getTree(returnList);
        linkParentChildSchema(returnList);
		return returnList;
    }
    
    private void linkParentChildSchema(final List<RbacRoleSchemasNameResponse> sourceSchemalList) {
    	List<RbacRoleSchemasNameResponse> referList = new ArrayList<>(sourceSchemalList);
    	
    	for (int i = sourceSchemalList.size() - 1; i >= 0; i--) {
    		RbacRoleSchemasNameResponse item = sourceSchemalList.get(i);
    		Optional<RbacRoleSchemasNameResponse> parentFind 
				= referList.parallelStream().filter(target -> item.getParentResource().equals(target.getResource())).findFirst();
    		if (!parentFind.isPresent()) {
    			continue;
    		}
    		parentFind.get().addChildItem(item);
    		sourceSchemalList.remove(item);
    	}
    }
    
    /**
     * <p>
     * 递归查询树
     * </p>
     * @author 曾祥华
     * @version 0.1 2019年3月14日
     * @param reuqest
     * @param user
     * void
     */
    private void getTree(List<RbacRoleSchemasNameResponse> reuqest){
    	for(RbacRoleSchemasNameResponse req:reuqest) {
    		List<FetchResourceSchemaDetailResponse> childListRoles = resSchemaClient.fetchChildrenRoleSchemaList(req.getResource());
			List<RbacRoleSchemasNameResponse> childList = PayloadParseUtil.jacksonBaseParse(RbacRoleSchemasNameResponse.class,childListRoles);
			req.replaceChildList(childList);
			getTree(childList);
    	}
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @LogCodeDefine("31")
    public List<RbacRoleSchemasNameResponse> getRoleChildrenSchemas(@RequestParam(name = "id", required = false) String id) {
        //直接调用rbac接口
        LOGGER.debug("Welcome into getRoleSchemas1 >>>>>");
        List<FetchResourceSchemaDetailResponse> fetchRoleSchemaList = resSchemaClient.fetchChildrenRoleSchemaList(id);
        LOGGER.debug("The result(List<RbacRoleSchemasResponse>) of rbac 'fetchRoleSchemaList'>>>>>>");
        return PayloadParseUtil.jacksonBaseParse(RbacRoleSchemasNameResponse.class, fetchRoleSchemaList);
    }

}
