package com.migu.tsg.microservice.atomicservice.architect.biz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.migu.tsg.microservice.atomicservice.architect.dao.ProjectTemplateDao;
import com.migu.tsg.microservice.atomicservice.architect.dao.ProjectTemplateItemDao;
import com.migu.tsg.microservice.atomicservice.architect.dao.po.ProjectTemplate;
import com.migu.tsg.microservice.atomicservice.architect.dao.po.ProjectTemplateItem;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectTemplateRequest;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectTemplateResponse;
import com.migu.tsg.microservice.atomicservice.architect.dto.FetchProjectTemplateResponse;
import com.migu.tsg.microservice.atomicservice.architect.dto.model.ProjectTemplateResourceDTO;
import com.migu.tsg.microservice.atomicservice.architect.dto.model.ProjectTemplateRoleDTO;
import com.migu.tsg.microservice.atomicservice.architect.dto.model.ProjectTemplateRoleParentDTO;
import com.migu.tsg.microservice.atomicservice.architect.dto.model.ProjectTemplateRolePermissionDTO;
import com.migu.tsg.microservice.atomicservice.common.util.InstantUtils;
import com.migu.tsg.microservice.atomicservice.common.util.RegexUtils;

import net.sf.json.JSONArray;

import com.migu.tsg.microservice.atomicservice.common.enums.BadRequestFieldMessageEnum;
import com.migu.tsg.microservice.atomicservice.common.exception.BadRequestFieldException;
import com.migu.tsg.microservice.atomicservice.common.enums.ResultErrorEnum;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.biz <br>
* 类名称: ProjectTemplateBiz.java <br>
* 类描述: 项目模板业务层<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年9月26日下午5:16:16 <br>
* 版本: v1.0
*/
/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.biz <br>
* 类名称: ProjectTemplateBiz.java <br>
* 类描述: <br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月13日下午12:58:28 <br>
* 版本: v1.0
*/
@Service
@Transactional
public class ProjectTemplateBiz {
    /** 日志  */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectTemplateBiz.class);

    @Autowired
    private ProjectTemplateDao projectTemplateDao;

    @Autowired
    private ProjectTemplateItemDao projectTemplateItemDao;

    private static final String ARCHITECT_TEMPLATE_ITEM_TYPE_ROLE = "role";

    private static final String ARCHITECT_TEMPLATE_ITEM_TYPE_RESOURCE = "resource";

    /**
     * 获取可用模板的列表
     * @param uuids 项目模板UUID集合
     * @return 响应对象
     */
    @SuppressWarnings("unchecked")
    public List<FetchProjectTemplateResponse> fetchProjectTemplateList(final List<String> uuids) {
        if (CollectionUtils.isNotEmpty(uuids)) {
            RegexUtils.verifyRegexUuids(uuids.toArray(new String[0]));
        }
        List<ProjectTemplate> fetchList = projectTemplateDao.listProjectTemplateByUuids(uuids);
        LOGGER.info("method[fetchTemplateList] The list of proejct template for access DB={}", fetchList);
        List<FetchProjectTemplateResponse> result = new ArrayList<>();
        for (ProjectTemplate projectTemplate : fetchList) {
            FetchProjectTemplateResponse response = new FetchProjectTemplateResponse();
            response.setUuid(projectTemplate.getUuid());
            response.setName(projectTemplate.getName());
            response.setUpdatedAt(InstantUtils.ofEpochMilli(projectTemplate.getUpdatedAt()));
            response.setCreatedAt(InstantUtils.ofEpochMilli(projectTemplate.getCreatedAt()));
            List<ProjectTemplateItem> projectTemplateItems = projectTemplate.getProjectTemplateItems();
            List<ProjectTemplateRoleDTO> roles = new ArrayList<>();
            List<Map<String, Object>> resources = new ArrayList<>();
            for (ProjectTemplateItem projectTemplateItem : projectTemplateItems) {
                if (ARCHITECT_TEMPLATE_ITEM_TYPE_ROLE.equals(projectTemplateItem.getItemType())) {
                    roles.add(new Gson().fromJson(projectTemplateItem.getItemData(),
                            ProjectTemplateRoleDTO.class));
                } else if (ARCHITECT_TEMPLATE_ITEM_TYPE_RESOURCE.equals(projectTemplateItem.getItemType())) {
                    resources.add(new Gson().fromJson(projectTemplateItem.getItemData(), Map.class));
                }
            }
            response.setRoles(roles);
            response.setResources(resources);
            result.add(response);
        }
        return result;
    }

    /**
     * 删除单个项目模板
     * @param uuid 项目模板UUID
     */
    public void deleteProjectTemplate(final String uuid) {
        RegexUtils.verifyRegexUuids(uuid);
        projectTemplateDao.deleteProjectTemplateByUuid(uuid);
        projectTemplateItemDao.deleteProjectTemplateItemByTemplateUuid(uuid);
    }

    /**
     * 创建单个项目模板
     * @param request 请求对象
     * @return 响应对象
     */
    public CreateProjectTemplateResponse createProjectTemplate(final CreateProjectTemplateRequest request) {
        if (StringUtils.isBlank(request.getName())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "name", new String[] {
                    BadRequestFieldMessageEnum.PROJECT_TEMPLATE_NAME_CANNOT_BE_EMPTY.getMessage() });
        }
        if (projectTemplateDao.countProjectTemplateByName(request.getName()) > 0) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "name",
                    new String[] { BadRequestFieldMessageEnum.dynamicMessage(
                            BadRequestFieldMessageEnum.PROJECT_TEMPLATE_ALREADY_EXIST, request.getName()) });
        }
        ProjectTemplate projectTemplate = new ProjectTemplate();
        String uuid = UUID.randomUUID().toString();
        projectTemplate.setUuid(uuid);
        projectTemplate.setName(request.getName());
        projectTemplateDao.insertProjectTemplate(projectTemplate);

        List<ProjectTemplateItem> projectTemplateItems = new ArrayList<>();
        Set<Integer> idSet = new HashSet<>();
        if (CollectionUtils.isNotEmpty(request.getRoles())) {
            validateParameterRoles(request.getRoles(), uuid, projectTemplateItems, idSet);
        }
        if (CollectionUtils.isNotEmpty(request.getResources())) {
            validateParameterResources(request.getResources(), uuid, projectTemplateItems, idSet);
        }
        if (CollectionUtils.isNotEmpty(projectTemplateItems)) {
            projectTemplateItemDao.insertProjectTemplateItem(projectTemplateItems);
        }

        CreateProjectTemplateResponse response = new CreateProjectTemplateResponse();
        response.setUuid(uuid);
        response.setName(request.getName());
        response.setRoles(request.getRoles());
        response.setResources(request.getResources());
        response.setUpdatedAt(InstantUtils.now());
        response.setCreatedAt(InstantUtils.now());
        return response;
    }

    /**
     * 验证项目模板声明的角色参数
     * @param roles 项目模板声明的角色集合
     * @param templateUuid 项目模板UUID
     * @param projectTemplateItems 项目模板声明的所有资源集合
     * @param idSet 项目模板声明的所有资源ID集合
     */
    private void validateParameterRoles(final List<ProjectTemplateRoleDTO> roles,
    		final String templateUuid,final List<ProjectTemplateItem> projectTemplateItems,
    		final Set<Integer> idSet) {
        int number = 0;
        Set<String> nameSet = new HashSet<>();
        Map<Integer, String> collect = roles.stream()
                .collect(Collectors.toMap(ProjectTemplateRoleDTO::getId, ProjectTemplateRoleDTO::getName));
        for (ProjectTemplateRoleDTO role : roles) {
            
            validateRolesIdAndName(role, nameSet, idSet, number);

            if (CollectionUtils.isEmpty(role.getParents())
                    && CollectionUtils.isEmpty(role.getPermissions())) {
                throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,"roles." + number + ".permissions",
                        new String[] { BadRequestFieldMessageEnum.dynamicMessage(
                                BadRequestFieldMessageEnum.ROLE_SHOULD_HAVE_AT_LEAST_ONE_PERMISSION,
                                role.getName()) });
            }

            validateRolesDependsOn(role, number, collect);

            validateRolesParents(role, number, collect);

            if (CollectionUtils.isNotEmpty(role.getDependsOn())&& CollectionUtils.isNotEmpty(role.getParents())) {
                List<Integer> dependOnList = role.getDependsOn().stream().distinct().collect(Collectors.toList());
                List<Integer> itemList = role.getParents().stream().map(ProjectTemplateRoleParentDTO::getItem)
                        .distinct().collect(Collectors.toList());
                if (!itemList.equals(dependOnList)) {
                    throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,"roles." + number 
                    		+ ".depends_on&parents",new String[] {
                               BadRequestFieldMessageEnum.PROJECP_TEMPLATE_ROLE_DEPEND_AND_PARENT_NOT_EQUALS
                                  .getMessage() });
                }
            }
            
            validateRolesPermissions(role, number);
            
            number++;
            projectTemplateItems.add(createProjectTemplateItem(role, templateUuid, ARCHITECT_TEMPLATE_ITEM_TYPE_ROLE));
        }
    }

    /**
     * 验证项目模板声明的角色参数
     * @param role 项目模板声明的角色
     * @param nameSet 项目模板UUID
     * @param idSet 项目模板声明的所有资源ID集合
     * @param number 次序
     */
    private void validateRolesIdAndName(ProjectTemplateRoleDTO role, Set<String> nameSet, Set<Integer> idSet, int number) {
    	if (role.getId() == null) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "roles." + number + ".id",
                    new String[] { BadRequestFieldMessageEnum.PROJECP_TEMPLATE_ROLE_ID_CANNOT_BE_EMPTY
                            .getMessage() });
        }
        if (idSet.contains(role.getId())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "roles." + number + ".id",
                    new String[] {
                            BadRequestFieldMessageEnum.PROJECP_TEMPLATE_ROLE_ID_ALREADY_IN_THE_REQUEST
                                    .getMessage() });
        }
        idSet.add(role.getId());
        if (StringUtils.isBlank(role.getName())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "roles." + number + ".name",
                    new String[] { BadRequestFieldMessageEnum.PROJECP_TEMPLATE_ROLE_NAME_CANNOT_BE_EMPTY
                            .getMessage() });
        }
        if (nameSet.contains(role.getName())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "roles." + number + ".name",
                    new String[] {
                            BadRequestFieldMessageEnum.PROJECP_TEMPLATE_ROLE_NAME_ALREADY_IN_THE_REQUEST
                                    .getMessage() });
        }
        nameSet.add(role.getName());
    }

    /**
     * 验证项目模板声明的角色参数
     * @param role 项目模板声明的角色
     * @param number 次序
     * @param collect 项目模板声明的所有资源ID集合
     */
    private void validateRolesDependsOn(ProjectTemplateRoleDTO role, int number, Map<Integer, String> collect) {
    	if (role.getDependsOn() == null) {
            role.setDependsOn(new ArrayList<>());
        } else {
            for (Integer id : role.getDependsOn()) {
                if (!collect.containsKey(id)) {
                    throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                            "roles." + number + ".depends_on",
                            new String[] {
                                    BadRequestFieldMessageEnum.PROJECP_TEMPLATE_ROLE_DEPEND_NOT_EXISTS
                                            .getMessage() });
                }
                if (collect.containsKey(id) && id.equals(role.getId())) {
                    throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                            "roles." + number + ".depends_on",
                            new String[] {
                                    BadRequestFieldMessageEnum.PROJECP_TEMPLATE_ROLE_DEPEND_CONNOT_SELF
                                            .getMessage() });
                }
            }
        }
    }
    /**
     * 验证项目模板声明的角色参数
     * @param role 项目模板声明的角色
     * @param number 次序
     * @param collect 项目模板声明的所有资源ID集合
     */
    private void validateRolesParents(ProjectTemplateRoleDTO role, int number, Map<Integer, String> collect) {
        if (role.getParents() == null) {
            role.setParents(new ArrayList<>());
        } else {
            int num = 0;
            for (ProjectTemplateRoleParentDTO rp : role.getParents()) {
                if (rp.getItem() == null) {
                    throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                            "roles." + number + ".parents." + num + ".item",
                            new String[] {
                                    BadRequestFieldMessageEnum.PROJECP_TEMPLATE_ROLE_PARENT_ITEM_CONNOT_EMPTY
                                            .getMessage() });
                }
                if (StringUtils.isBlank(rp.getName())) {
                    throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                            "roles." + number + ".parents." + num + ".name",
                            new String[] {
                                    BadRequestFieldMessageEnum.PROJECP_TEMPLATE_ROLE_PARENT_NAME_CONNOT_EMPTY
                                            .getMessage() });
                }
                if (collect.containsKey(rp.getItem()) && rp.getItem().equals(role.getId())) {
                    throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                            "roles." + number + ".parents." + num + ".item",
                            new String[] {
                                    BadRequestFieldMessageEnum.PROJECP_TEMPLATE_ROLE_PARENT_ITEM_CONNOT_SELF
                                            .getMessage() });
                }
                if (!collect.containsKey(rp.getItem())) {
                    throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                            "roles." + number + ".parents." + num + ".item",
                            new String[] {
                                    BadRequestFieldMessageEnum.PROJECP_TEMPLATE_ROLE_PARENT_ITEM_NOT_EXISTS
                                            .getMessage() });
                }
                if (!rp.getName().equals(collect.get(rp.getItem()))) {
                    throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                            "roles." + number + ".parents." + num + ".name",
                            new String[] {
                                    BadRequestFieldMessageEnum.PROJECP_TEMPLATE_ROLE_PARENT_NAME_NOT_EXISTS
                                            .getMessage() });
                }
                num++;
            }
        }
    }
    /**
     * 验证项目模板声明的角色参数
     * @param role 项目模板声明的角色
     * @param number 次序
     */
    private void validateRolesPermissions(ProjectTemplateRoleDTO role, int number) {
    	if (role.getPermissions() == null) {
            role.setPermissions(new ArrayList<>());
        } else {
            int num = 0;
            for (ProjectTemplateRolePermissionDTO permission : role.getPermissions()) {
                if (CollectionUtils.isEmpty(permission.getResource())) {
                    throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                            "roles." + number + ".permissions." + num + ".resource",
                            new String[] { BadRequestFieldMessageEnum.RESOURCE_NAME_CANNOT_BE_EMPTY
                                    .getMessage() });
                }
                if (CollectionUtils.isEmpty(permission.getActions())) {
                    throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                            "roles." + number + ".permissions." + num + ".actions",
                            new String[] { BadRequestFieldMessageEnum.RESOURCE_ACTIONS_CANNOT_BE_EMPTY
                                    .getMessage() });
                }
                num++;
            }
        }
    }

    /**
     * 验证项目模板声明的资源参数
     * @param resources 项目模板声明的资源集合
     * @param templateUuid 项目模板UUID
     * @param projectTemplateItems 项目模板声明的所有资源集合
     * @param idSet 项目模板声明的所有资源ID集合
     */
    private void validateParameterResources(final List<ProjectTemplateResourceDTO> resources,
            final String templateUuid, final List<ProjectTemplateItem> projectTemplateItems,
            final Set<Integer> idSet) {
        int number = 0;
        for (ProjectTemplateResourceDTO resource : resources) {
            // 资源ID不能为空
            if (resource.getId() == null) {
                throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                        "resources." + number + ".id",
                        new String[] { BadRequestFieldMessageEnum.PROJECP_TEMPLATE_RESOURCE_ID_CANNOT_BE_EMPTY
                                .getMessage() });
            }
            // 资源ID，必须在同一个项目模版内是唯一的
            if (idSet.contains(resource.getId())) {
                throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                        "resources." + number + ".id",
                        new String[] {
                                BadRequestFieldMessageEnum.PROJECP_TEMPLATE_RESOURCE_ID_ALREADY_IN_THE_REQUEST
                                        .getMessage() });
            }
            idSet.add(resource.getId());

            // 资源NAME不能为空
            if (StringUtils.isBlank(resource.getName())) {
                throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS,
                        "resources." + number + ".name",
                        new String[] {
                                BadRequestFieldMessageEnum.PROJECP_TEMPLATE_RESOURCE_NAME_CANNOT_BE_EMPTY
                                        .getMessage() });
            }
            if (resource.getDependsOn() == null) {
                resource.setDependsOn(new ArrayList<>());
            }
            number++;
            projectTemplateItems.add(
                    createProjectTemplateItem(resource, templateUuid, ARCHITECT_TEMPLATE_ITEM_TYPE_RESOURCE));
        }
    }

    /**
     * 封装创建ProjectTemplateItem对象
     * @param role 项目模板之角色对象
     * @param templateUuid 项目模板UUID
     * @param itemType 资源类型，支持 role或者resource
     * @return ProjectTemplateItem对象
     */
    private ProjectTemplateItem createProjectTemplateItem(final ProjectTemplateRoleDTO role,
            final String templateUuid, final String itemType) {
        ProjectTemplateItem projectTemplateItem = new ProjectTemplateItem();
        projectTemplateItem.setTemplateUuid(templateUuid);
        projectTemplateItem.setItemType(itemType);
        projectTemplateItem.setId(role.getId());
        projectTemplateItem.setName(role.getName());
        projectTemplateItem.setRequired(
                BooleanUtils.toIntegerObject(BooleanUtils.toBooleanDefaultIfNull(role.getRequired(), false)));
        projectTemplateItem.setDependsOn(JSONArray.fromObject(role.getDependsOn()).toString());
        projectTemplateItem.setItemData(new Gson().toJson(role));
        return projectTemplateItem;
    }

    /**
     * 封装创建ProjectTemplateItem对象
     * @param resource 项目模板之资源对象
     * @param templateUuid 项目模板UUID
     * @param itemType 资源类型，支持 role或者resource
     * @return ProjectTemplateItem对象
     */
    private ProjectTemplateItem createProjectTemplateItem(final ProjectTemplateResourceDTO resource,
            final String templateUuid, final String itemType) {
        ProjectTemplateItem projectTemplateItem = new ProjectTemplateItem();
        projectTemplateItem.setTemplateUuid(templateUuid);
        projectTemplateItem.setItemType(itemType);
        projectTemplateItem.setId(resource.getId());
        projectTemplateItem.setName(resource.getName());
        projectTemplateItem.setRequired(BooleanUtils
                .toIntegerObject(BooleanUtils.toBooleanDefaultIfNull(resource.getRequired(), false)));
        projectTemplateItem.setDependsOn(JSONArray.fromObject(resource.getDependsOn()).toString());
        projectTemplateItem.setItemData(new Gson().toJson(resource));
        return projectTemplateItem;
    }
}