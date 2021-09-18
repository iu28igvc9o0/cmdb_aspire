package com.migu.tsg.microservice.atomicservice.rbac.biz.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.common.client.LdapServiceClient;
import com.migu.tsg.microservice.atomicservice.ldap.dto.GetLdapUserResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.InsertLdapMemberRequest;
import com.migu.tsg.microservice.atomicservice.ldap.dto.ListPagenationResponse;
import com.migu.tsg.microservice.atomicservice.rbac.biz.DepartmentBiz;
import com.migu.tsg.microservice.atomicservice.rbac.biz.UserBiz;
import com.migu.tsg.microservice.atomicservice.rbac.biz.UserDepmentSyncEipBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.DepartmentUser;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.User;
import com.migu.tsg.microservice.atomicservice.rbac.dto.DepartmentCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserBatchCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.DepartmentDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.UserDTO;
import com.migu.tsg.microservice.atomicservice.rbac.entity.EipDeptDTO;
import com.migu.tsg.microservice.atomicservice.rbac.entity.EipUserDTO;
import com.migu.tsg.microservice.atomicservice.rbac.service.DepartmentService;
import com.migu.tsg.microservice.atomicservice.rbac.service.UserService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class UserDeptSyncEipBizImpl implements UserDepmentSyncEipBiz{
	
	@Value("${eip.url.use-dept}")
	private String urlUseDept;
	@Value("${eip.url.all-user}")
	private String urlAllUser;
	@Value("${eip.sync.env}")
	private String syncEnv;
	@Value("${ldapconfig.namespace:alauda}")
	private String namespace;

	@Autowired
	DepartmentService departmentService;
	@Autowired
	UserService userService;
	@Autowired
	UserBiz userBiz;
	@Autowired
	DepartmentBiz departmentBiz;
	@Autowired
	LdapServiceClient ldapServiceClient;
	
	
	private static final Logger logger = LoggerFactory.getLogger(UserDeptSyncEipBizImpl.class);
	
	/**
	 * OA-卓望数码部门ID
	 */
	private static final String DEPT_SHUMA_ID = "D01500";
	
	/**
	 *  表明从OA同步过来
	 */
	private static final String OA_SYNC = "OA-SYNC";
	
	private static final String CURR_ENV_UMS = "UMS";
	
	

	@Override
	public void userDeptSyncProcess() {
		// 优先读取Config服务的配置，读取不到读取本地
		logger.info("当前环境变量：{}", syncEnv);
		if(!CURR_ENV_UMS.equals(syncEnv)){
			return ;
		}
		RestTemplate restTemplate = new RestTemplate();
		ParameterizedTypeReference<List<EipDeptDTO>> typeRef = new ParameterizedTypeReference<List<EipDeptDTO>>() {
		};
		ResponseEntity<List<EipDeptDTO>> responseEntity = restTemplate.exchange(urlUseDept, HttpMethod.GET, null,
				typeRef);
		List<EipDeptDTO> deptList = responseEntity.getBody();

		// 过滤OA卓望数码下面的组织
		List<EipDeptDTO> smDeptList = deptList.stream().filter(item -> item.getFullId().contains(DEPT_SHUMA_ID))
				.collect(Collectors.toList());
		// 卓望数码组织Map
		Map<String, EipDeptDTO> smDeptMap = smDeptList.stream()
				.collect(Collectors.toMap(EipDeptDTO::getDeptId, EipDeptDTO -> EipDeptDTO));

		List<DepartmentDTO> umsDepartList = departmentBiz.getAll();
		List<String> umsDepartIdList = umsDepartList.stream().map(b -> b.getUuid()).collect(Collectors.toList());
		// EIP同步过来的数码部门ID在组织表不存在，新增
		smDeptList.stream().filter(it -> !umsDepartIdList.contains(it.getDeptId())).forEach(d -> createNewDept(d));
		
		// 同步用户，设置用户组织关系
		initUser(smDeptMap);
	}

	private void initUser(Map<String, EipDeptDTO> smDeptMap) {
		//获取OA用户，eip_user
		RestTemplate restTemplate = new RestTemplate();
		ParameterizedTypeReference<List<EipUserDTO>> typeRef = new ParameterizedTypeReference<List<EipUserDTO>>() {};
		ResponseEntity<List<EipUserDTO>> responseEntity = restTemplate.exchange(urlAllUser, HttpMethod.GET, null,typeRef);
		List<EipUserDTO> eipUserList = responseEntity.getBody();
		
		List<EipUserDTO> smUserList = eipUserList.stream().filter(item -> smDeptMap.containsKey(item.getDeptId())).collect(Collectors.toList());
		//List<EipUserDTO> smUserList = eipUserList.stream().filter(item -> item.getDept().contains("卓望数码")).collect(Collectors.toList());
		logger.info("同步过来的数码用户，数量：{}", smUserList.size());
		Map<String,EipUserDTO> smUserMap = smUserList.stream().collect(Collectors.toMap(EipUserDTO::getUserLogin,EipDeptDTO->EipDeptDTO));
		
		List<UserDTO> userList = userBiz.getAll();
		List<String> allUmsUserAccount = userList.stream().map(b -> b.getLdapId()).collect(Collectors.toList());
		
		// 同步过来的数码用户，在UMS中不存在，删除; UMS是直接物理删除
		List<UserDTO> deletUserList = userList.stream().filter(it -> OA_SYNC.equals(it.getDescr()) && !smUserMap.containsKey(it.getLdapId())).collect(Collectors.toList());
		logger.info("同步EIP账号不存在，需要删的用户{}", JSONArray.fromObject(deletUserList));
		deletUserList.forEach(vo -> userBiz.deleteByPrimaryKey(vo.getUuid()));
		
		//1. 找数码用户存在，UMS不存在的账号。新增用户部门
		smUserList.stream().filter(it -> !allUmsUserAccount.contains(it.getUserLogin())).forEach(v -> createNewSmUser(v));
		//2. UMS用户存在，但是组织不存在，新增用户部门表
		List<UserDTO> eipDeptNotExistUser = userList.stream().filter(
				it -> smUserMap.containsKey(it.getLdapId()) && !StringUtils.isEmpty(it.getDeptId()) && !it.getDeptId().startsWith("D")).collect(Collectors.toList());
		logger.info("UMS用户存在，但是组织不存在，新增用户部门表，数量：{}", eipDeptNotExistUser.size());
		addEipDeptment(smUserMap, eipDeptNotExistUser);
		
		//3. 数码和UMS用户都存在，更新用户组织关系
		userList.stream().filter(it -> smUserMap.containsKey(it.getLdapId())).forEach(v -> updateUserDept(v, smUserMap));
		
		//4. 维护roles_user数据，默认新增用户时，需要增加一条记录。role_uuid  = 'cadcd1e8-3e9c-4dc9-a49f-ddb44ba44f89'	;普通用户菜单
		userBiz.addDefaultRoleForUms();
		//5. 维护t_user_classify_account数据。默认新增用户时需要增加一条记录。user_classify_uuid ='9f05464a-30da-423f-a38a-c7d7f0d36371';首页展示需要
		userBiz.addDefaultUserClassifyAccountForUms();
		
	    //6. 同步ldap用户--比对smUserList，新增
		ldapUserSync(smUserList);
	}

	@SuppressWarnings("unchecked")
	private void ldapUserSync(List<EipUserDTO> smUserList) {
		int pageSize = 200000;
		int currentPage = 1;
		ListPagenationResponse response = ldapServiceClient.listLdapMember(namespace, null, null,null,Collections.EMPTY_LIST, Collections.EMPTY_LIST,
				pageSize, currentPage);
		List<GetLdapUserResponse> results = response.getResults();
		List<String> ldapUserNames = results.stream().map(b -> b.getUsername()).collect(Collectors.toList());
		List<EipUserDTO> needAdds = smUserList.stream().filter(it -> !ldapUserNames.contains(it.getUserLogin())).collect(Collectors.toList());
		List<InsertLdapMemberRequest> request = Lists.newArrayList();
		needAdds.forEach(e -> {
            request.add(convertLdap(e));
        });
		ldapServiceClient.insertLdapMembers(namespace, request);
	}

	private InsertLdapMemberRequest convertLdap(EipUserDTO user) {
		InsertLdapMemberRequest insert = new InsertLdapMemberRequest();
		insert.setMobile(user.getTel());
		insert.setName(user.getUserName());
		insert.setUsername(user.getUserLogin());
		insert.setMail(user.getMail());
		logger.info("新增Ldap用户：{}",JSONObject.fromObject(insert));
		return insert;
	}

	private Object updateUserDept(UserDTO user, Map<String, EipUserDTO> smUserMap) {
		String smDeptId = smUserMap.get(user.getLdapId()).getDeptId();
		List<DepartmentDTO> departmentList = user.getDeptList();
		if(CollectionUtils.isNotEmpty(departmentList) && smDeptId != null){
			Optional<DepartmentDTO> option = departmentList.stream().filter(it -> it.getUuid().startsWith("D")).findAny();
			if(!option.isPresent()){
				List<UserDTO> eipDeptNotExistUser = new ArrayList<>();
				eipDeptNotExistUser.add(user);
				addEipDeptment(smUserMap, eipDeptNotExistUser);
			} else {
				for(DepartmentDTO dept : departmentList){
					if(dept.getUuid().startsWith("D") && !dept.getUuid().equals(smDeptId)){
						userBiz.modifyDeptIdBatchByUserIdArrays(smDeptId, Arrays.asList(dept.getUuid().split(",")));
					}
				}
			}
		}
		return null;
	}

	// UMS用户存在，但是EIP组织不存在，新增用户部门表
	private void addEipDeptment(Map<String, EipUserDTO> smUserMap, List<UserDTO> eipDeptNotExistUser) {
		if(CollectionUtils.isNotEmpty(eipDeptNotExistUser)){
			List<UserCreateRequest> userList2 = new ArrayList<>();
			eipDeptNotExistUser.forEach(user -> {
				UserCreateRequest requ = new UserCreateRequest();
				requ.setCode(user.getCode());
				String deptId = smUserMap.get(user.getLdapId()).getDeptId();
				requ.setDeptId(deptId);
				userList2.add(requ);
				
			});
			UserBatchCreateRequest request3 = new UserBatchCreateRequest();
			request3.setListUser(userList2);
			//userService.batchInsertDepartmentUser(request3);
			
			List<DepartmentUser> departmentUserList = Lists.newArrayList();
            List<UserCreateRequest> list = request3.getListUser();
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(e -> {

                    List<User> userList = userBiz.selectByLdapIdAndNamespace(e.getCode(), "alauda");
                    if (org.apache.commons.collections.CollectionUtils.isEmpty(userList)) {
                        return;
                    }
                    userList.forEach(m -> {
                        DepartmentUser departmentUser = new DepartmentUser();
                        departmentUser.setDeptId(e.getDeptId());
                        departmentUser.setUserId(m.getUuid());
                        departmentUserList.add(departmentUser);
                    });
                });
                userBiz.batchInsertDepartmentUser(departmentUserList);
           }
       }
	}

	//新增用户，会同时插入部门用户表 department_user
	private void createNewSmUser(EipUserDTO eipUser) {
		UserCreateRequest user = new UserCreateRequest();
		user.setDeptId(eipUser.getDeptId());
		user.setLdapId(eipUser.getUserLogin());
		user.setCode(eipUser.getUserLogin());
		user.setMail(eipUser.getMail());
		user.setMobile(eipUser.getTel());
		user.setName(eipUser.getUserName());
		user.setUserType(1);
		user.setNamespace("alauda");
		user.setDescr(OA_SYNC);
		user.setNo(eipUser.getUserCode());
		
		List<String> deptIds = Lists.newArrayList();
		deptIds.add(eipUser.getDeptId());
		user.setDeptIds(deptIds);
		
		logger.info("OA数码新增用户:{}",JSONObject.fromObject(eipUser));
		//userService.createdUser(user);
		
		UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userBiz.insert(userDTO);
	}
	
	// 创建新的部门
	private void createNewDept(EipDeptDTO dept) {
		DepartmentCreateRequest departmentCreateRequest = new DepartmentCreateRequest();
		departmentCreateRequest.setDepartmentId(dept.getDeptId());
		departmentCreateRequest.setName(dept.getDeptName());
		if(DEPT_SHUMA_ID.equals(dept.getParentId())){
			departmentCreateRequest.setParentId("1001");
		}else{
			departmentCreateRequest.setParentId(dept.getParentId());
		}
		departmentCreateRequest.setNo(OA_SYNC);
		departmentCreateRequest.setDescr(dept.getFullName());
		departmentCreateRequest.setNamespace("alauda");
		departmentCreateRequest.setDeptType(1);
		logger.info("OA:{},新增组织--》{}",dept.getFullName(), JSONObject.fromObject(departmentCreateRequest));
		//departmentService.createdDepartment(departmentCreateRequest);
		
		DepartmentDTO departmentDTO = new DepartmentDTO();
        BeanUtils.copyProperties(departmentCreateRequest, departmentDTO);
        if(!StringUtils.isEmpty(departmentCreateRequest.getDepartmentId())){
        	departmentDTO.setUuid(departmentCreateRequest.getDepartmentId());
        }
	    departmentBiz.insert(departmentDTO);
	}
}
