package com.migu.tsg.microservice.atomicservice.rbac.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.common.util.PageUtil;
import com.migu.tsg.microservice.atomicservice.common.annotation.CacheEvict;
import com.migu.tsg.microservice.atomicservice.common.aspect.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.common.aspect.RequestAuthContext.RequestHeadUser;
import com.migu.tsg.microservice.atomicservice.common.client.LdapServiceClient;
import com.migu.tsg.microservice.atomicservice.common.enums.BadRequestFieldMessageEnum;
import com.migu.tsg.microservice.atomicservice.common.enums.ResultErrorEnum;
import com.migu.tsg.microservice.atomicservice.common.exception.BadRequestFieldException;
import com.migu.tsg.microservice.atomicservice.common.util.InstantUtils;
import com.migu.tsg.microservice.atomicservice.common.util.RegexUtils;
import com.migu.tsg.microservice.atomicservice.rbac.dao.PermissionDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.ResourceSchemaDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.RoleDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.RoleParentDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.RoleUsersDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.Permission;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchema;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.Role;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.RoleParent;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.RoleUsers;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ListRolesResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.GetRoleDetailDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.InsertRoleDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.InsertRolePermissionsDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ListRolesAssignedDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ListRolesDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ParentRoleDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.PermissionDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.RoleParentsDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.RolesAssignedRevokeDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.UpdateRoleDTO;

import net.sf.json.JSONArray;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.biz <br>
 * 类名称: RoleBiz.java <br>
 * 类描述: 【RBAC原子层】角色业务层 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月21日上午11:19:59 <br>
 * 版本: v1.0
 */
@Service
@Transactional
public class RoleBiz {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleBiz.class);

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleParentDao roleParentsDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RoleUsersDao roleUsersDao;
    
    @Autowired
    private ResourceSchemaDao resourceSchemaDao;

    @Autowired
    private LdapServiceClient ldapServiceClient;

    /**
     * 查询对于给定的UUID的角色列表
     * @param uuids 角色UUID集合,必填
     * @param username 用户名称,通过相关的用户名过滤角色列表
     * @return DTO对象
     */
    public List<ListRolesDTO> listRoles(final String[] uuids, final String name, final String namespace, final Integer roleType, final String username) {
        RegexUtils.verifyRegexUuids(uuids);
        List<Role> listRoles = roleDao.listRoles(uuids, name,namespace,roleType,username);
        List<ListRolesDTO> result = new ArrayList<>();
        listRoles.forEach(role -> result
                .add(new ListRolesDTO(role.getUuid(), role.getName(), role.getAdminRole() == 0 ? false : true,
                        role.getNamespace(), InstantUtils.ofEpochMilli(role.getCreatedAt()),
                        InstantUtils.ofEpochMilli(role.getUpdatedAt()), role.getRoleType(), role.getDescribe())));
        LOGGER.info("method[listRoles] Query list of role success");
        return result;
    }

    /**
    * 获取分页列表数据
    * @param pageRequest
    * @return
    */
    public PageResult<ListRolesResponse> pageList(PageRequest pageRequest) {
    	// edit by pgh at 2020-09-18
		RequestHeadUser currUser = RequestAuthContext.currentRequestAuthContext().getUser();
		if (currUser != null && !currUser.isAdmin() && !currUser.isSuperUser()) {
			boolean isAdminRole = false;
			String[] roleUuidArr = getOperRoleIdByUserName(currUser.getNamespace(), currUser.getUsername());
			for (String roleUuid : roleUuidArr) {
				GetRoleDetailDTO roleDetail = getRoleDetail(roleUuid);
				// 如果为管理角色, 返回所有功能点
				if (roleDetail.getAdminRole()) {
					isAdminRole = true;
					break;
				}
			}
			if (!isAdminRole) {
				pageRequest.addFields("adminRole", 0);	// 非管理角色只能管理非管理角色
			}
		}

		Page page = PageUtil.convert(pageRequest);
		int count = roleDao.pageListCount(page);
		PageResult<ListRolesResponse> pageResponse = new PageResult<ListRolesResponse>();
		pageResponse.setCount(count);
		int pageCount = (count - 1) / page.getPageSize() + 1;
		pageResponse.setCurPage(page.getPageNo());
		pageResponse.setPageSize(page.getPageSize());
		pageResponse.setPageCount(pageCount);
		if (count > 0) {
			List<ListRolesResponse> listItem = roleDao.pageList(page);
			pageResponse.setResult(listItem);
		}
		return pageResponse;
    }

    /**
     * 查询子级角色列表
     * @param uuids 角色UUID集合,必填
     * @param username 用户名称,通过相关的用户名过滤角色列表
     * @return DTO对象
     */
    public List<ListRolesDTO> childListRoles(final String[] uuids, final String name, final String namespace, final Integer roleType) {
        RegexUtils.verifyRegexUuids(uuids);
        List<Role> listRoles = roleDao.childListRoles(uuids, name,namespace,roleType);
        List<ListRolesDTO> result = new ArrayList<>();
        listRoles.forEach(role -> result
                .add(new ListRolesDTO(role.getUuid(), role.getName(), role.getAdminRole() == 0 ? false : true,
                        role.getNamespace(), InstantUtils.ofEpochMilli(role.getCreatedAt()),
                        InstantUtils.ofEpochMilli(role.getUpdatedAt()), role.getRoleType(), role.getDescribe())));
        LOGGER.info("method[listRoles] Query list of role success");
        return result;
    }

    /**
     * 新增多个角色以及对应的父角色或对应的权限
     * @param dtoList 新增角色集合
     * @return 新增角色UUID的集合
     */
    public List<InsertRoleDTO> insertRoles(final List<InsertRoleDTO> dtoList) {
        // 封装响应对象集合
        List<InsertRoleDTO> result = new ArrayList<>();
        // 批量新增角色的集合
        List<Role> roleList = new ArrayList<>();
        // 批量新增角色和单个父角色关联关系的集合
        List<RoleParent> roleParentList = new ArrayList<>();
        // 批量新增角色和对应权限关联关系的集合
        List<Permission> permissionList = new ArrayList<>();
        int number = 0;
        Set<String> setName = new HashSet<>();
        for (InsertRoleDTO role : dtoList) {
            validateInsertRoleParameter(number, setName, role);
            String roleUuid = UUID.randomUUID().toString();
            role.setUuid(roleUuid);
            // 添加到批量新增角色的集合中
            roleList.add(new Role(roleUuid, StringUtils.trimToEmpty(role.getName()),
                    StringUtils.trimToEmpty(role.getNamespace()),
                    role.getAdminRole() == null ? 0 : role.getAdminRole() == false ? 0 : 1, null, null,
                    		role.getRoleType(),role.getDescribe()));
            // 如果有关联的父角色,则循环新增与父角色关联信息
            List<RoleParentsDTO> parents = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(role.getParents())) {
                parents = role.getParents().stream().distinct().collect(Collectors.toList());
                int num = 0;
                Set<String> parentRoleSet = new HashSet<>();
                for (RoleParentsDTO roleParents : parents) {
                    validateInsertRoleParentParameter(number, num, parentRoleSet, roleParents, role);
                    num++;
                    roleParentList.add(new RoleParent(roleUuid, roleParents.getUuid(), null, null));
                }

            }
            // 如果有关联的权限,则循环新增与权限关联信息
            if (CollectionUtils.isNotEmpty(role.getPermissions())) {
                int num = 0;
                for (PermissionDTO permission : role.getPermissions()) {
                    validateInsertRolePermissionParameter(number, num, permission);
                    num++;
                    // 新增角色和对应权限关联
                    String permissionUuid = UUID.randomUUID().toString();
                    permission.setUuid(permissionUuid);
                    permission.setRoleUuid(roleUuid);
                    for(String resourceType:permission.getResource()) {
                		ResourceSchema resourceSchema =resourceSchemaDao.fetchResourceSchemaDetail(resourceType,"1");
                		if (resourceSchema!=null && CollectionUtils.isNotEmpty(resourceSchema.getActions())) {
                            Set<String> actions = resourceSchema.getActions().stream().map(dto -> dto.getAction())
                                    .collect(Collectors.toSet());
                            actions.remove(null);
    						permission.getActions().addAll(actions);
    					}
                	}
                    
					permissionList.add(new Permission(permissionUuid, roleUuid,
                            JSONArray.fromObject(permission.getActions()).toString(),
                            JSONArray.fromObject(permission.getResource()).toString(), JSONArray
                                    .fromObject(defaultConstraints(permission.getConstraints())).toString()));
                }

            }
            //自动添加非特殊权限，by 曾祥华
            number++;
            role.setParents(parents);
            if (role.getAdminRole() == null) {
                role.setAdminRole(false);
            } else {
                role.setAdminRole(role.getAdminRole());
            }
            role.setAssignedAt(InstantUtils.now());
            role.setUpdatedAt(InstantUtils.now());
            role.setCreatedAt(InstantUtils.now());
            result.add(role);
        }
        // 批量新增数据
        insertDataBatch(roleList, roleParentList, permissionList);
        LOGGER.info("method[insertRoles] Insert list of role success");
        return result;
    }

    /**
     * 验证新增角色的权限参数
     * @param number 角色索引
     * @param num 权限索引
     * @param permission 权限对象
     * @throws BadRequestFieldException 请求字段不合法的异常
     */
    private void validateInsertRolePermissionParameter(final int number, final int num,
            final PermissionDTO permission) throws BadRequestFieldException {
        // 资源名不能为空
        if (CollectionUtils.isEmpty(permission.getResource())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                    number + ".permissions." + num + ".resource",
                    new String[] { BadRequestFieldMessageEnum.RESOURCE_NAME_CANNOT_BE_EMPTY.getMessage() });
        }
        // 资源操作不能为空
//        if (CollectionUtils.isEmpty(permission.getActions())) {
//            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
//                    number + ".permissions." + num + ".actions", new String[] {
//                            BadRequestFieldMessageEnum.RESOURCE_ACTIONS_CANNOT_BE_EMPTY.getMessage() });
//        }
    }

    /**
     * 验证新增角色的父角色参数
     * @param number 角色索引
     * @param num 父角色索引
     * @param parentRoleSet 父角色UUID集合
     * @param roleParents 父角色对象
     * @param role 角色对象
     */
    private void validateInsertRoleParentParameter(final int number, final int num,
            final Set<String> parentRoleSet, final RoleParentsDTO roleParents, final InsertRoleDTO role) {
        // 父角色UUID不能为空
        if (roleParents.getUuid() == null) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                    number + ".parents." + num + ".uuid",
                    new String[] { BadRequestFieldMessageEnum.ROLE_UUID_IS_REQUIRED.getMessage() });
        }
        // 父角色UUID必须合法
        if (!RegexUtils.hasMatchesRegexUuids(roleParents.getUuid())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                    number + ".parents." + num + ".uuid",
                    new String[] { BadRequestFieldMessageEnum.UUID_REQUEST_INVALID.getMessage() });
        }
        // 同一个父角色不能被同一个角色继承多次
        if (parentRoleSet.contains(roleParents.getUuid())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                    number + ".parents." + num + ".uuid", new String[] {
                            BadRequestFieldMessageEnum.PARENT_ROLE_CONNOT_INHERITED_MORE.getMessage() });
        }
        parentRoleSet.add(roleParents.getUuid());
        // 查询父角色是否存在,不存在则抛出父角色不存在异常
        Role roleParent = validateRoleParentAccessDB(roleParents.getUuid(),
                number + ".parents." + num + ".uuid");
        // 角色和父角色不属于同一个命名空间下
        if (!roleParent.getNamespace().equals(role.getNamespace())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                    number + ".parents." + num + ".uuid", new String[] {
                            BadRequestFieldMessageEnum.ROLE_AND_PARENT_NOT_SAME_NAMESPACE.getMessage() });
        }
        // 角色继承层级不能超过4层
        if (recursionRoleParentsSum(roleParents.getUuid()) > 3) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                    number + ".parents." + num + ".uuid", new String[] {
                            BadRequestFieldMessageEnum.ROLES_INHERITED_CANNOT_OUTNUMBER4.getMessage() });
        }
    }

    /**
     * 验证新增角色参数
     * @param number 角色索引
     * @param setName 角色名称集合
     * @param role 角色对象
     * @throws BadRequestFieldException 请求字段不合法的异常
     */
    private void validateInsertRoleParameter(final int number, final Set<String> setName,
            final InsertRoleDTO role) {
        // 角色名不能为空
        if (StringUtils.isBlank(role.getName())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, number + ".name",
                    new String[] { BadRequestFieldMessageEnum.ROLE_NAME_CANNOT_BE_EMPTY.getMessage() });
        }
        // 空间名称不能为空
        if (StringUtils.isBlank(role.getNamespace())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, number + ".namespace",
                    new String[] { BadRequestFieldMessageEnum.NAMESPACE_NAME_CANNOT_BE_EMPTY.getMessage() });
        }
        // 验证角色名是否重复
        if (setName.contains(role.getName())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, number + ".name",
                    new String[] { BadRequestFieldMessageEnum.dynamicMessage(
                            BadRequestFieldMessageEnum.NAME_ALREADY_IN_THE_REQUEST, role.getName()) });
        }
        setName.add(role.getName());
        // 判断新增角色名是否存在,若存在则抛异常
        if (roleDao.hasFetchRole(role.getNamespace(), StringUtils.trimToEmpty(role.getName())) > 0) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, number + ".name",
                    new String[] { BadRequestFieldMessageEnum
                            .dynamicMessage(BadRequestFieldMessageEnum.ROLE_ALREADY_EXIST, role.getName()) });
        }

        // 当前角色必须有父角色或者设置至少一个权限
        if (CollectionUtils.isEmpty(role.getParents()) && CollectionUtils.isEmpty(role.getPermissions())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, number + ".permissions",
                    new String[] { BadRequestFieldMessageEnum.dynamicMessage(
                            BadRequestFieldMessageEnum.ROLE_SHOULD_HAVE_AT_LEAST_ONE_PERMISSION,
                            role.getName()) });
        }
    }

    /**
     * 批量新增数据
     * @param roleList 批量新增角色的集合
     * @param roleParentList 批量新增角色和单个父角色关联关系的集合
     * @param permissionList 批量新增角色和对应权限关联关系的集合
     */
    private void insertDataBatch(List<Role> roleList, List<RoleParent> roleParentList,
            List<Permission> permissionList) {
        int rows = 0;
        // 批量新增角色
        if (CollectionUtils.isNotEmpty(roleList)) {
            rows = roleDao.createRoleBatch(roleList);
            LOGGER.info("method[insertDataBatch] The number of rows by insert role for DB = {}", rows);
        }
        // 批量新增角色和单个父角色关联关系
        if (CollectionUtils.isNotEmpty(roleParentList)) {
            rows = roleParentsDao.createRoleParentBatch(roleParentList);
            LOGGER.info(
                    "method[insertDataBatch] The number of rows by insert mapping relationship between role and parent_role for DB = {}",
                    rows);
        }
        // 批量新增角色和对应权限关联关系
        if (CollectionUtils.isNotEmpty(permissionList)) {
            rows = permissionDao.createPermissionBatch(permissionList);
            LOGGER.info(
                    "method[insertDataBatch] The number of rows by insert role related permssion for DB = {}",
                    rows);
        }
    }

    /**
     * 查询单个角色详细信息
     * @param roleUuid 角色uuid
     * @return dto对象
     */
    @SuppressWarnings("unchecked")
    public GetRoleDetailDTO getRoleDetail(final String roleUuid) {
        RegexUtils.verifyRegexUuids(roleUuid);
        Role role = validateRoleAccessDB(roleUuid);
        GetRoleDetailDTO dto = new GetRoleDetailDTO(role.getUuid(), role.getName(), role.getNamespace(),
                role.getAdminRole() == 0 ? false : true, InstantUtils.ofEpochMilli(role.getCreatedAt()),
                InstantUtils.ofEpochMilli(role.getUpdatedAt()), role.getRoleType(), role.getDescribe(), null, null);

        // 当前角色查询对应的多个权限信息
        List<Permission> fetchPermissionList = permissionDao.fetchPermissionList(role.getUuid());
        // 封装当前角色对应的权限DTO数据
        List<PermissionDTO> permissions = new ArrayList<>();
        for (Permission permission : fetchPermissionList) {
            permissions.add(new PermissionDTO(permission.getUuid(), permission.getRoleUuid(),
                    JSONArray.fromObject(permission.getActions()),
                    JSONArray.fromObject(permission.getResources()),
                    JSONArray.fromObject(permission.getConstraints())));
        }
        dto.setPermissions(permissions);
        // 通过当前角色查询对应的多个父角色信息
        List<RoleParent> fetchRoleParentList = roleParentsDao.fetchRoleParentList(role.getUuid());

        // 封装当前角色对应的父角色DTO数据
        List<ParentRoleDTO> parents = new ArrayList<>();
        for (RoleParent roleParent : fetchRoleParentList) {
            parents.add(new ParentRoleDTO(roleParent.getParentUuid(), roleParent.getParentName(),
                    InstantUtils.ofEpochMilli(roleParent.getAssignedAt())));
        }
        dto.setParents(parents);
        LOGGER.info("method[getRoleDetail] Query role {} detail success", role.getName());
        return dto;
    }

    /**
     * 修改角色以及对应的父角色或对应的权限 无法修改角色名称以及空间名称
     * @param roleUuid 角色UUID
     * @param adminRole 是否为管理员角色
     * @param parents 父角色集合
     * @param permissions 权限集合
     * final UpdateRoleDTO dto
     */
    @CacheEvict
    public void modifyRole(final UpdateRoleDTO udto) {
    	String roleUuid =udto.getUuid();
    	Boolean adminRole=udto.getAdminRole();
    	List<RoleParentsDTO> parents=udto.getParents();
    	List<PermissionDTO> permissions=udto.getPermissions();
        RegexUtils.verifyRegexUuids(roleUuid);
        // 角色权限不能为空
        if (CollectionUtils.isEmpty(permissions)) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "permissions",
                    new String[] { BadRequestFieldMessageEnum.PERMISSIONS_OF_ROLE_NOT_EMPTY.getMessage() });
        }
        Role role = validateRoleAccessDB(roleUuid);
        if (adminRole != null) {
            // 更新adminRole字段
            role.setAdminRole(adminRole == true ? 1 : 0);
        }
        role.setName(udto.getName());
        role.setDescribe(udto.getDescribe());
        int rows = roleDao.modifyRole(role);
        LOGGER.info("method[modifyRole] The number of rows by update role: adminRole for DB = {}", rows);

        // 批量新增角色和单个父角色关联关系的集合
        List<RoleParent> roleParentList = new ArrayList<>();
        // 批量新增角色和对应权限关联关系的集合
        List<Permission> permissionList = new ArrayList<>();

        // 更新当前角色对应父角色关联关系
        // 1.删除当前角色对应的父角色集合
        rows = roleParentsDao.deleteParentRoles(role.getUuid());
        LOGGER.info(
                "method[modifyRole] The number of rows by delete mapping relationship between role and parent_role for DB = {}",
                rows);
        // 2.如果有关联的父角色,则循环新增与父角色关联信息
        if (CollectionUtils.isNotEmpty(parents)) {
            Set<String> parentRoleSet = new HashSet<>();
            int index = 0;
            for (RoleParentsDTO roleParentsDTO : parents) {
                validateModifyRoleParentsParameter(index, roleUuid, parentRoleSet, roleParentsDTO, role);
                roleParentList.add(new RoleParent(roleUuid, roleParentsDTO.getUuid(), null, null));
                index++;
            }
        }

        // 更新当前角色对应的权限集合
        // 1.删除当前角色对应的权限集合
        rows = permissionDao.deletePermissionsByRoleUuid(role.getUuid());
        LOGGER.info("method[modifyRole] The number of rows by delete role related permission for DB = {}",
                rows);
        // 2.如果有关联的权限,则循环新增与权限关联信息
        int num = 0;
        for (PermissionDTO permissionDTO : permissions) {
        	if(CollectionUtils.isEmpty(permissionDTO.getResource())) {
        		permissionDTO.setResource(Arrays.asList(new String[] {"#"}));
            }
            validateModifyRolePermissionsParameter(permissionDTO, num);
            // 新增角色和对应权限关联
            String permissionUuid = UUID.randomUUID().toString();
            for(String resourceType:permissionDTO.getResource()) {
        		ResourceSchema resourceSchema =resourceSchemaDao.fetchResourceSchemaDetail(resourceType,"1");
        		if (resourceSchema!=null) {
					Set<String> actions = resourceSchema.getActions().stream().map(dto -> dto.getAction())
							.collect(Collectors.toSet());
                    actions.remove(null);
					permissionDTO.getActions().addAll(actions);
				}
        	}
            
            permissionList.add(new Permission(permissionUuid, roleUuid,
                    JSONArray.fromObject(permissionDTO.getActions()).toString(),
                    JSONArray.fromObject(permissionDTO.getResource()).toString(),
                    JSONArray.fromObject(defaultConstraints(permissionDTO.getConstraints())).toString()));
            num++;
        }
        
        
        // 批量新增数据
        insertDataBatch(null, roleParentList, permissionList);
        LOGGER.info("method[modifyRole] Modify role {} success", role.getName());
    }

    /**
     * 验证修改角色的权限参数
     * @param permission 角色权限对象
     * @throws BadRequestFieldException 请求字段不合法的异常
     */
    private void validateModifyRolePermissionsParameter(final PermissionDTO permission, final int num)
            throws BadRequestFieldException {
        // 资源名不能为空
        if (CollectionUtils.isEmpty(permission.getResource())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                    "permissions." + num + ".resource",
                    new String[] { BadRequestFieldMessageEnum.RESOURCE_NAME_CANNOT_BE_EMPTY.getMessage() });
        }
        // 资源操作不能为空
//        if (CollectionUtils.isEmpty(permission.getActions())) {
//            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
//                    "permissions." + num + ".actions", new String[] {
//                            BadRequestFieldMessageEnum.RESOURCE_ACTIONS_CANNOT_BE_EMPTY.getMessage() });
//        }
    }

    /**
     * 验证修改角色的父角色参数
     * @param index 索引
     * @param roleUuid 角色UUID
     * @param parentRoleSet 父角色UUID集合
     * @param roleParentsDTO 父角色对象
     * @param role 角色对象
     */
    private void validateModifyRoleParentsParameter(final int index, final String roleUuid,
            final Set<String> parentRoleSet, final RoleParentsDTO roleParentsDTO, final Role role) {
        // 角色的父角色不允许为自己
        if (roleUuid.equals(roleParentsDTO.getUuid())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "parents." + index + ".uuid",
                    new String[] { BadRequestFieldMessageEnum.PARENT_ROLE_NOT_SELF.getMessage() });
        }
        // 查询父角色是否存在,不存在则抛出父角色不存在异常
        Role roleParent = validateRoleParentAccessDB(roleParentsDTO.getUuid(), "parents." + index + ".uuid");
        // 角色和父角色不属于同一个命名空间下
        if (!roleParent.getNamespace().equals(role.getNamespace())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "parents." + index + ".uuid",
                    new String[] {
                            BadRequestFieldMessageEnum.ROLE_AND_PARENT_NOT_SAME_NAMESPACE.getMessage() });
        }
        // 角色不能相互继承
        if (hasRecursionRoleParent(roleParentsDTO.getUuid(), roleUuid)) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "parents." + index + ".uuid",
                    new String[] { BadRequestFieldMessageEnum.ROLES_CANNOT_MUTUAL_INHERITED.getMessage() });
        }
        // 角色继承层级不能超过4层
        if (recursionRoleParentsSum(roleParentsDTO.getUuid()) + recursionRoleChildrenSum(roleUuid) > 4) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "parents." + index + ".uuid",
                    new String[] {
                            BadRequestFieldMessageEnum.ROLES_INHERITED_CANNOT_OUTNUMBER4.getMessage() });
        }
        // 同一个父角色不能被同一个角色继承多次
        if (parentRoleSet.contains(roleParentsDTO.getUuid())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "parents." + index + ".uuid",
                    new String[] {
                            BadRequestFieldMessageEnum.PARENT_ROLE_CONNOT_INHERITED_MORE.getMessage() });
        }
        parentRoleSet.add(roleParentsDTO.getUuid());
    }

    /**
     * 递归判断当前角色UUID是否被父角色继承过
     * @param parentRoleUuid 父角色UUID
     * @param roleUuid 角色uuid
     * @return true 表示被父角色继承 false表示没有被父角色继承
     */
    private boolean hasRecursionRoleParent(final String parentRoleUuid, final String roleUuid) {
        List<RoleParent> fetchRoleParentList = roleParentsDao.fetchRoleParentList(parentRoleUuid);
        if (CollectionUtils.isNotEmpty(fetchRoleParentList)) {
            for (RoleParent roleParent : fetchRoleParentList) {
                if (roleUuid.equals(roleParent.getParentUuid())
                        || hasRecursionRoleParent(roleParent.getParentUuid(), roleUuid)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 删除单个角色以及关联的成员,权限,父角色
     * @param roleUuid 角色UUID
     */
    @CacheEvict
    public void deleteRole(final String roleUuid) {
        RegexUtils.verifyRegexUuids(roleUuid);
        Role role = validateRoleAccessDB(roleUuid);
        // 查询是否是父角色,若是则禁止删除父角色
        if (roleParentsDao.isParentRole(role.getUuid()) > 0) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "role_uuid",
                    new String[] { BadRequestFieldMessageEnum.ROLE_INHERITED_NOT_REMOVED.getMessage() });
        }
        if (CollectionUtils.isNotEmpty(roleUsersDao.fetchRoleUsersList(roleUuid))) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "role_uuid",
                    new String[] { BadRequestFieldMessageEnum.IS_CONNECTED_USER_ROLE_NOT_REMOVED.getMessage() });
        }
        // 删除当前角色信息
        int rows = roleDao.deleteRoleByUuid(roleUuid);
        LOGGER.info("method[deleteRole] The number of rows by delete role for DB = {}", rows);
        // 删除当前角色和父角色的关联关系
        rows = roleParentsDao.deleteParentRoles(roleUuid);
        LOGGER.info(
                "method[deleteRole] The number of rows by delete mapping relationship between role and parent_role for DB = {}",
                rows);
        // 删除当前角色对应的权限
        rows = permissionDao.deletePermissionsByRoleUuid(role.getUuid());
        LOGGER.info("method[deleteRole] The number of rows by delete role related permission for DB = {}",
                rows);
//        // 删除当前角色对应成员的关联关系
//        rows = roleUsersDao.deleteRoleUsersByRoleUuid(role.getUuid());
        LOGGER.info(
                "method[deleteRole] The number of rows by delete mapping relationship between role and user for DB = {}",
                rows);
        LOGGER.info("method[deleteRole] Delete role {} success", role.getName());
    }

    /**
     * 关联父角色
     * @param roleUuid 角色UUID
     * @param parentUuid 父角色UUID
     * @param parentName 父角色名称
     */
    @CacheEvict
    public void insertParentRole(final String roleUuid, final String parentUuid, final String parentName) {
        RegexUtils.verifyRegexUuids(roleUuid, parentUuid);
        // 角色的父角色不允许为自己
        if (roleUuid.equals(parentUuid)) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "uuid",
                    new String[] { BadRequestFieldMessageEnum.PARENT_ROLE_NOT_SELF.getMessage() });
        }
        Role role = validateRoleAccessDB(roleUuid);
        // 查询父角色是否存在,不存在则抛出父角色不存在异常
        Role roleParent = validateRoleParentAccessDB(parentUuid, "uuid");
        // 角色和父角色不属于同一个命名空间下
        if (!role.getNamespace().equals(roleParent.getNamespace())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "uuid", new String[] {
                    BadRequestFieldMessageEnum.ROLE_AND_PARENT_NOT_SAME_NAMESPACE.getMessage() });
        }
        // 查询当前角色的父角色是否存在,大于0则存在,小于等于0则不存在
        if (roleParentsDao.hasFetchRoleParent(role.getUuid(), parentUuid) > 0) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "uuid",
                    new String[] { BadRequestFieldMessageEnum.PARENT_ALREADY_ADDED.getMessage() });
        }
        // 角色不能相互继承
        if (hasRecursionRoleParent(parentUuid, roleUuid)) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "uuid",
                    new String[] { BadRequestFieldMessageEnum.ROLES_CANNOT_MUTUAL_INHERITED.getMessage() });
        }
        // 角色继承层级不能超过4层
        if (recursionRoleParentsSum(parentUuid) + recursionRoleChildrenSum(roleUuid) > 4) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "uuid", new String[] {
                    BadRequestFieldMessageEnum.ROLES_INHERITED_CANNOT_OUTNUMBER4.getMessage() });
        }
        // 新增当前角色和父角色的关联关系
        int rows = roleParentsDao.createRoleParent(new RoleParent(role.getUuid(), parentUuid, null, null));
        LOGGER.info(
                "method[insertParentRole] The number of rows by insert mapping relationship between role and parent_role for DB = {}",
                rows);
        LOGGER.info("method[insertParentRole] Insert parent role {} success", role.getName());
    }

    /**
     * 递归统计父角色继承最多层数
     * @param parentRoleUuid 父角色UUID
     * @return 层级数
     */
    private int recursionRoleParentsSum(final String parentRoleUuid) {
        List<RoleParent> fetchRoleParentList = roleParentsDao.fetchRoleParentList(parentRoleUuid);
        int sum = 1;
        if (CollectionUtils.isNotEmpty(fetchRoleParentList)) {
            int max = 0;
            for (RoleParent roleParent : fetchRoleParentList) {
                sum = 1 + recursionRoleParentsSum(roleParent.getParentUuid());
                max = Integer.compare(max, sum) > 0 ? max : sum;
            }
            sum = max;
        }
        return sum;
    }

    /**
     * 递归统计角色被继承最多层数
     * @param roleUuid 角色UUID
     * @return 层级数
     */
    private int recursionRoleChildrenSum(final String roleUuid) {
        List<RoleParent> fetchRoleChildrenList = roleParentsDao.fetchRoleChildrenList(roleUuid);
        int sum = 1;
        if (CollectionUtils.isNotEmpty(fetchRoleChildrenList)) {
            int max = 0;
            for (RoleParent roleParent : fetchRoleChildrenList) {
                sum = 1 + recursionRoleChildrenSum(roleParent.getRoleUuid());
                max = Integer.compare(max, sum) > 0 ? max : sum;
            }
            sum = max;
        }
        return sum;
    }

    /**
     * 删除单个父角色
     * @param roleUuid 角色UUID
     * @param parentUuid 父角色UUID
     */
    @CacheEvict
    public void deleteParentRole(final String roleUuid, final String parentUuid) {
        RegexUtils.verifyRegexUuids(roleUuid, parentUuid);
        Role role = validateRoleAccessDB(roleUuid);
        // 查询当前角色的父角色是否存在,大于0则存在,小于等于0则不存在
        if (roleParentsDao.hasFetchRoleParent(role.getUuid(), parentUuid) <= 0) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "parent_uuid",
                    new String[] { BadRequestFieldMessageEnum.ROLE_NOT_INHERIT_PARENT_ROLE.getMessage() });
        }
        // 删除子角色和父角色关联关系
        int rows = roleParentsDao.deleteParentRole(role.getUuid(), parentUuid);
        LOGGER.info(
                "method[deleteParentRole] The number of rows by delete mapping relationship between role and parent_role for DB = {}",
                rows);
        LOGGER.info("method[deleteParentRole] Delete role {} parent success", role.getName());
    }

    /**
     * 验证角色是否存在,并且用到角色对象信息
     * @param roleUuid 角色UUID
     * @return 角色对象
     */
    private Role validateRoleAccessDB(final String roleUuid) {
        // 查询角色信息
        Role role = roleDao.getRoleByUUID(roleUuid);
        // 若查询结果为空,则抛出资源不存在异常
        if (role == null) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "role_uuid",
                    new String[] { BadRequestFieldMessageEnum.ROLE_NOT_EXIST.getMessage() });
        }
        LOGGER.info("method[validateRoleAccessDB] Query role {} success", role.getName());
        return role;
    }

    /**
     * 验证父角色是否存在,并且用到父角色对象信息
     * @param roleParentUuid 父角色UUID
     * @param fileName 无效的字段名
     * @return 父角色对象
     */
    private Role validateRoleParentAccessDB(final String roleParentUuid, final String fileName) {
        // 查询角色信息
        Role roleParent = roleDao.getRoleByUUID(roleParentUuid);
        // 若查询结果为空,则抛出资源不存在异常
        if (roleParent == null) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, fileName,
                    new String[] { BadRequestFieldMessageEnum.PARENT_NOT_EXIST.getMessage() });
        }
        LOGGER.info("method[validateRoleParentAccessDB] Query parent role {} success", roleParent.getName());
        return roleParent;
    }

    /**
     * 新增角色单个权限
     * @param roleUuid 角色UUID
     * @param actions 资源操作集合
     * @param resource 资源名称集合
     * @param constraints 资源约束集合
     * @return dto对象
     */
    @CacheEvict
    public InsertRolePermissionsDTO insertRolePermission(final String roleUuid, final List<String> actions,
            final List<String> resource, final List<Map<String, String>> constraints) {
        RegexUtils.verifyRegexUuids(roleUuid);
        validateResourceParameter(actions, resource);
        // 查询角色是否存在
        Role role = roleDao.getRoleByUUID(roleUuid);
        if (role == null) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "role_uuid",
                    new String[] { BadRequestFieldMessageEnum.ROLE_NOT_EXIST.getMessage() });
        }
        String permissionUuid = UUID.randomUUID().toString();
        // 新增角色权限
        int rows = permissionDao.createPermission(new Permission(permissionUuid, role.getUuid(),
                JSONArray.fromObject(actions).toString(), JSONArray.fromObject(resource).toString(),
                JSONArray.fromObject(defaultConstraints(constraints)).toString()));
        LOGGER.info(
                "method[InsertRolePermissionsDTO] The number of rows by insert role related permission for DB = {}",
                rows);
        InsertRolePermissionsDTO insertRolePermissionsDTO = new InsertRolePermissionsDTO(permissionUuid,
                actions, resource, constraints);
        LOGGER.info("method[InsertRolePermissionsDTO] Insert role {} permission success", role.getName());
        return insertRolePermissionsDTO;
    }

    /**
     * 处理约束为null或者empty
     * @param constraints 资源约束集合
     * @return 资源约束集合
     */
    private List<Map<String, String>> defaultConstraints(final List<Map<String, String>> constraints) {
        List<Map<String, String>> newConstraints = null;
        if (CollectionUtils.isEmpty(constraints)) {
            newConstraints = new ArrayList<>();
            newConstraints.add(new HashMap<>());
        } else {
            newConstraints = constraints;
        }
        return newConstraints;
    }

    /**
     * 修改角色单个权限
     * @param roleUuid 角色UUID
     * @param permissionUuid 权限UUID
     * @param actions 资源操作集合
     * @param resource 资源名称集合
     * @param constraints 资源约束集合
     */
    @CacheEvict
    public void modifyRolePermission(final String roleUuid, final String permissionUuid,
            final List<String> actions, final List<String> resource,
            final List<Map<String, String>> constraints) {
        RegexUtils.verifyRegexUuids(roleUuid, permissionUuid);
        validateResourceParameter(actions, resource);
        validateRoleAndPermissionAccessDB(roleUuid, permissionUuid);
        // 修改角色单个权限
        int rows = permissionDao.updatePermissionByUuid(new Permission(permissionUuid, roleUuid,
                JSONArray.fromObject(actions).toString(), JSONArray.fromObject(resource).toString(),
                JSONArray.fromObject(defaultConstraints(constraints)).toString()));
        LOGGER.info(
                "method[modifyRolePermission] The number of rows by update role related permission for DB = {}",
                rows);
        LOGGER.info("method[modifyRolePermission] Modify role {} permission {} success", roleUuid,
                permissionUuid);
    }

    /**
     * 验证资源名和资源操作
     * @param actions 资源操作
     * @param resource 资源名称
     * @throws BadRequestFieldException 无效字段异常
     */
    private void validateResourceParameter(final List<String> actions, final List<String> resource)
            throws BadRequestFieldException {
        // 资源名不能为空
        if (CollectionUtils.isEmpty(resource)) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "rolePermissions.resource",
                    new String[] { BadRequestFieldMessageEnum.RESOURCE_NAME_CANNOT_BE_EMPTY.getMessage() });
        }
        // 资源操作不能为空
        if (CollectionUtils.isEmpty(actions)) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "rolePermissions.actions",
                    new String[] {
                            BadRequestFieldMessageEnum.RESOURCE_ACTIONS_CANNOT_BE_EMPTY.getMessage() });
        }
    }

    /**
     * 删除角色单个权限
     * @param roleUuid 角色UUID
     * @param permissionUuid 权限UUID
     */
    @CacheEvict
    public void deleteRolePermission(final String roleUuid, final String permissionUuid) {
        RegexUtils.verifyRegexUuids(roleUuid, permissionUuid);
        validateRoleAndPermissionAccessDB(roleUuid, permissionUuid);
        // 删除角色单个权限
        int rows = permissionDao.deletePermissionsByUuid(permissionUuid, roleUuid);
        LOGGER.info(
                "method[deleteRolePermission] The number of rows by delete role related permission for DB = {}",
                rows);
        LOGGER.info("method[deleteRolePermission] Delete role {} permission {} success", roleUuid,
                permissionUuid);
    }

    /**
     * 访问数据库验证角色和权限是否存在
     * @param roleUuid 角色UUID
     * @param permissionUuid 权限UUID
     */
    private void validateRoleAndPermissionAccessDB(final String roleUuid, final String permissionUuid) {
        // 查询角色是否存在
        if (roleDao.hasGetRoleByUUID(roleUuid) <= 0) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "role_uuid",
                    new String[] { BadRequestFieldMessageEnum.ROLE_NOT_EXIST.getMessage() });
        }
        // 查询即将要删除的权限是否存在
        if (permissionDao.hasFetchPermissionByUuid(permissionUuid) <= 0) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "permission_uuid",
                    new String[] { BadRequestFieldMessageEnum.PERMISSION_NOT_EXIST.getMessage() });
        }
    }

    /**
     * 查询已经分配给用户的角色列表OR已经分配给角色的用户列表
     * @param namespace 空间名称
     * @param roleUuid 角色UUID
     * @param user 用户名称
     * @return 已经分配给用户的角色列表
     */
    public List<ListRolesAssignedDTO> listRolesAssigned(final String namespace, final String[] roleUuid,
            final String user) {
        // 查询角色成员集合
        List<RoleUsers> fetchRoleUsersList = roleUsersDao.fetchRolesAssignedList(namespace, user, roleUuid);

        // 封装DTO数据
        List<ListRolesAssignedDTO> lraList = new ArrayList<>();
        for (RoleUsers roleUsers : fetchRoleUsersList) {
            lraList.add(new ListRolesAssignedDTO(roleUsers.getRoleUuid(), roleUsers.getRoleName(),
                    roleUsers.getNamespace(), roleUsers.getUsername(),
                    InstantUtils.ofEpochMilli(roleUsers.getAssignedAt())));
        }
        LOGGER.info("method[listRolesAssigned] The query has been allocated to the user's role list "
                + "OR already allocated to the user list of the roles success");
        return lraList;
    }

    /**
     * 分配一个或多个角色给一个或者多个用户
     * @param dtoList 分配数据集合
     * @return 已分配的数据集合
     */
    @CacheEvict
    public List<RolesAssignedRevokeDTO> rolesAssigned(final List<RolesAssignedRevokeDTO> dtoList) {
        // 分配角色集合为空,抛异常
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "request", new String[] {
                    BadRequestFieldMessageEnum.AT_LEAST_ONE_ROLE_ASSIGN_TO_USER.getMessage() });
        }
        validateRolesAssignedParameter(dtoList);
        // 验证被分配角色的用户是否存在于LDAP中
        for (RolesAssignedRevokeDTO radto : dtoList) {
            ldapServiceClient.getLdapMember(radto.getNamespace(), radto.getUser());
        }
        List<String> roleUuidsList = validateRolesAccessDB(dtoList, 1);
        int index = 0;
        for (RolesAssignedRevokeDTO radto : dtoList) {
            int rows = roleUsersDao.createRoleUsers(new RoleUsers(roleUuidsList.get(index),
                    radto.getRoleName(), radto.getNamespace(), radto.getUser(), null));
            LOGGER.info(
                    "method[rolesAssigned] The number of rows by insert mapping relationship between role and user success for DB = {}",
                    rows);
            index++;
        }
        return dtoList;
    }

    /**
     * 撤销一个或多个用户的一个或多个角色
     * @param dtoList 撤销数据集合
     */
    @CacheEvict
    public void rolesRevoke(final List<RolesAssignedRevokeDTO> dtoList) {
        // 分配角色集合为空,抛异常
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "request",
                    new String[] { BadRequestFieldMessageEnum.AT_LEAST_REVOKE_USER_ONE_ROLE.getMessage() });
        }
        validateRolesAssignedParameter(dtoList);
        List<String> roleUuidsList = validateRolesAccessDB(dtoList, 0);
        int index = 0;
        String roleid=CollectionUtils.isNotEmpty(roleUuidsList)?roleUuidsList.get(index):null;
        for (RolesAssignedRevokeDTO radto : dtoList) {
            if (StringUtils.isNotBlank(radto.getRoleUuid())) {
                roleid = radto.getRoleUuid();
            }
            int rows = roleUsersDao.deleteRoleUsers(roleid, radto.getNamespace(),
                    radto.getUser());
            LOGGER.info(
                    "method[rolesRevoke] The number of rows by delete mapping relationship between role and user success for DB = {}",
                    rows);
            index++;
        }
    }

    /**
     * 访问数据库验证角色是否存在,判断用户是否已经关联角色
     * @param dtoList 参数集合
     * @param type 0为撤销,1为分配
     * @return 角色UUID集合
     */
    private List<String> validateRolesAccessDB(final List<RolesAssignedRevokeDTO> dtoList, final int type) {
        List<String> roleUuidsList = new ArrayList<>();
        int index = 0;
        for (RolesAssignedRevokeDTO radto : dtoList) {
        	if(type==0&&StringUtils.isNotEmpty(radto.getUser())) {
        		continue;
        	}
            Role role = roleDao.fetchRoleDetail(radto.getNamespace(),radto.getRoleUuid(), radto.getRoleName());
            if (role == null) {
                throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, index + ".role_name",
                        new String[] { BadRequestFieldMessageEnum.ROLE_NOT_EXIST.getMessage() });
            } else {
                roleUuidsList.add(role.getUuid());
                int row = roleUsersDao.hasFetchRoleUsersList(role.getUuid(), radto.getNamespace(),
                        radto.getUser());
                // 判断是否已经关联,已关联则抛异常
                if (row > 0 && type == 1) {
                    throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, index + ".role_name",
                            new String[] { BadRequestFieldMessageEnum.dynamicMessage(
                                    BadRequestFieldMessageEnum.USER_HAVE_ROLE, radto.getUser(),
                                    radto.getRoleName()) });
                }
                // 判断是否已经关联,没有关联则抛异常
                if (row <= 0 && type == 0) {
                    throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, index + ".role_name",
                            new String[] { BadRequestFieldMessageEnum.dynamicMessage(
                                    BadRequestFieldMessageEnum.USER_DONT_HAVE_ROLE, radto.getUser(),
                                    radto.getRoleName()) });
                }
            }
        }
        return roleUuidsList;
    }

    /**
     * 静态验证用户分配角色的参数
     * @param dtoList 参数集合
     * @throws BadRequestFieldException 请求字段不合法的异常
     */
    private void validateRolesAssignedParameter(final List<RolesAssignedRevokeDTO> dtoList)
            throws BadRequestFieldException {
        int number = 0;
        for (RolesAssignedRevokeDTO radto : dtoList) {
            if (StringUtils.isBlank(radto.getNamespace())) {
                throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                        "RolesUsers." + number + ".namespace", new String[] {
                                BadRequestFieldMessageEnum.NAMESPACE_NAME_CANNOT_BE_EMPTY.getMessage() });
            }
//            if (StringUtils.isBlank(radto.getRoleName())) {
//                throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
//                        "RolesUsers." + number + ".role_name",
//                        new String[] { BadRequestFieldMessageEnum.ROLE_NAME_CANNOT_BE_EMPTY.getMessage() });
//            }
            if (StringUtils.isBlank(radto.getUser())) {
                throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                        "RolesUsers." + number + ".user",
                        new String[] { BadRequestFieldMessageEnum.USER_NAME_CANNOT_BE_EMPTY.getMessage() });
            }
            number++;
        }
    }

    /**
     * 撤销用户组的所有角色
     * @param namespace 空间名称/根账号
     * @param usernameList usernameList 撤销数据集合
     */
    @CacheEvict
    public void rolesRevokeAll(final String namespace, final List<String> usernameList) {
        // 用户名称集合为空,抛异常
        if (CollectionUtils.isEmpty(usernameList)) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "request", new String[] {
                    BadRequestFieldMessageEnum.AT_LEAST_REVOKE_ONE_USER_ALL_ROLE.getMessage() });
        }
        int rows = roleUsersDao.deleteRoleUsersBatch(namespace, usernameList);
        LOGGER.info(
                "method[rolesRevokeAll] The number of rows by delete mapping relationship between role and user success for DB = {}",
                rows);
    }

    public String[] getOperRoleIdByUserName(String namespace, String userName) {
        return roleUsersDao.getOperRoleIdByUserName(namespace, userName);
    }
}
