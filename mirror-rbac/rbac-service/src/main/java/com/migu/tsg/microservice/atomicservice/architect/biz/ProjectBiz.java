package com.migu.tsg.microservice.atomicservice.architect.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.migu.tsg.microservice.atomicservice.architect.dao.ProjectDao;
import com.migu.tsg.microservice.atomicservice.architect.dao.ProjectTemplateDao;
import com.migu.tsg.microservice.atomicservice.architect.dao.po.Project;
import com.migu.tsg.microservice.atomicservice.architect.dao.po.ProjectTemplate;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectRequest;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectResponse;
import com.migu.tsg.microservice.atomicservice.architect.dto.FetchProjectResponse;
import com.migu.tsg.microservice.atomicservice.common.util.InstantUtils;
import com.migu.tsg.microservice.atomicservice.common.util.RegexUtils;
import com.migu.tsg.microservice.atomicservice.common.enums.BadRequestFieldMessageEnum;
import com.migu.tsg.microservice.atomicservice.common.exception.BadRequestFieldException;
import com.migu.tsg.microservice.atomicservice.common.enums.ResultErrorEnum;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.biz <br>
* 类名称: ProjectBiz.java <br>
* 类描述: 项目业务层<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年9月26日下午5:16:16 <br>
* 版本: v1.0
*/
@Service
@Transactional
public class ProjectBiz {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectBiz.class);

    @Autowired
    private ProjectTemplateDao projectTemplateDao;

    @Autowired
    private ProjectDao projectDao;

    /**
     * 获取项目列表
     * @param uuids 项目UUID集合
     * @return 响应对象
     */
    public List<FetchProjectResponse> fetchProjectList(final List<String> uuids) {
        if (CollectionUtils.isNotEmpty(uuids)) {
            RegexUtils.verifyRegexUuids(uuids.toArray(new String[0]));
        }
        List<Project> listProject = projectDao.listProjectByUuids(uuids);
        LOGGER.info("method[fetchProjectList] The list of project for access DB={}", listProject);
        List<FetchProjectResponse> result = new ArrayList<>();
        for (Project project : listProject) {
            FetchProjectResponse response = new FetchProjectResponse();
            response.setUuid(project.getUuid());
            response.setName(project.getName());
            response.setNamespace(project.getNamespace());
            response.setStatus(project.getStatus());
            response.setUpdatedAt(InstantUtils.ofEpochMilli(project.getUpdatedAt()));
            response.setCreatedAt(InstantUtils.ofEpochMilli(project.getCreatedAt()));
            result.add(response);
        }
        return result;
    }

    /**
     * 查询单个项目详情
     * @param uuid 项目UUID
     * @return 响应对象
     */
    public FetchProjectResponse fetchProjectByUuid(final String uuid) {
        RegexUtils.verifyRegexUuids(uuid);
        Project project = projectDao.getProjectByUuid(uuid);
        LOGGER.info("method[fetchProjectByUuid] The project for access DB={}", project);
        if (project == null) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "uuid",
                    new String[] { BadRequestFieldMessageEnum
                            .dynamicMessage(BadRequestFieldMessageEnum.PROJECT_NOT_EXIST, uuid) });
        }
        FetchProjectResponse response = new FetchProjectResponse();
        response.setUuid(project.getUuid());
        response.setName(project.getName());
        response.setNamespace(project.getNamespace());
        response.setStatus(project.getStatus());
        response.setUpdatedAt(InstantUtils.ofEpochMilli(project.getUpdatedAt()));
        response.setCreatedAt(InstantUtils.ofEpochMilli(project.getCreatedAt()));
        return response;
    }

    /**
     * 创建单个项目
     * @param request 请求对象
     * @return 响应对象
     */
    public CreateProjectResponse createProject(final CreateProjectRequest request) {
        validateParameter(request);
        ProjectTemplate projectTemplate = projectTemplateDao.getProjectTemplateByName(request.getTemplate());
        LOGGER.info("method[createProject] The project template for access DB={}", projectTemplate);
        if (projectTemplate == null) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "template",
                    new String[] { BadRequestFieldMessageEnum.dynamicMessage(
                            BadRequestFieldMessageEnum.PROJECT_TEMPLATE_NOT_EXIST, request.getTemplate()) });
        }
        Project project = new Project();
        String uuid = UUID.randomUUID().toString();
        project.setUuid(uuid);
        project.setName(request.getName());
        project.setNamespace(request.getNamespace());
        project.setTemplate(projectTemplate.getName());
        project.setTemplateUuid(projectTemplate.getUuid());
        project.setStatus("success");// TODO status 项目状态暂时不明白
        projectDao.insertProject(project);

        CreateProjectResponse response = new CreateProjectResponse();
        response.setUuid(uuid);
        response.setName(request.getName());
        response.setNamespace(request.getNamespace());
        response.setUpdatedAt(InstantUtils.now());
        response.setCreatedAt(InstantUtils.now());
        return response;
    }

    /**
     * 删除单个项目
     * @param uuid 项目UUID
     */
    public void deleteProject(final String uuid) {
        RegexUtils.verifyRegexUuids(uuid);
        projectDao.deleteProjectByUuid(uuid);
    }

    /**
     * 修改单个项目(只更新项目修改时间)
     * @param uuid 项目UUID
     */
    public void updateProject(final String uuid) {
        RegexUtils.verifyRegexUuids(uuid);
        projectDao.updateProjectByUuid(uuid);
    }

    /**
     * 验证参数
     * @param request 参数对象
     * @throws BadRequestFieldException 请求字段不合法的异常 
     */
    private void validateParameter(final CreateProjectRequest request)
            throws BadRequestFieldException {
        if (StringUtils.isBlank(request.getName())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "name",
                    new String[] { BadRequestFieldMessageEnum.PROJECT_NAME_CANNOT_BE_EMPTY.getMessage() });
        }
        if (StringUtils.isBlank(request.getTemplate())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "template", new String[] {
                    BadRequestFieldMessageEnum.PROJECT_TEMPLATE_NAME_CANNOT_BE_EMPTY.getMessage() });
        }
        if (StringUtils.isBlank(request.getNamespace())) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "namespace",
                    new String[] { BadRequestFieldMessageEnum.NAMESPACE_NAME_CANNOT_BE_EMPTY.getMessage() });
        }
        if (projectDao.countProjectByName(request.getNamespace(), request.getName()) > 0) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "name",
                    new String[] { BadRequestFieldMessageEnum.dynamicMessage(
                            BadRequestFieldMessageEnum.PROJECT_ALREADY_EXIST, request.getName()) });
        }
    }

}