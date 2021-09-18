package com.migu.tsg.microservice.atomicservice.ldap.biz;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.naming.NameAlreadyBoundException;
import javax.naming.directory.SearchControls;
import javax.servlet.http.HttpServletResponse;

import com.migu.tsg.microservice.atomicservice.ldap.biz.bo.ValidCode;
import com.migu.tsg.microservice.atomicservice.ldap.config.RedisProperties;
import com.migu.tsg.microservice.atomicservice.ldap.enums.LdapValidCodeEnum;
import com.migu.tsg.microservice.atomicservice.ldap.helper.RedisCacheHelper;
import com.migu.tsg.microservice.atomicservice.ldap.util.DateUtil;
import com.migu.tsg.microservice.atomicservice.ldap.util.VerifyCodeUtils;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import com.migu.tsg.microservice.atomicservice.ldap.biz.bo.LdapUser;
import static com.migu.tsg.microservice.atomicservice.ldap.biz.mapper.LdapUserMapper.*;
import static com.migu.tsg.microservice.atomicservice.ldap.enums.BadRequestFieldMessageEnum.*;
import static com.migu.tsg.microservice.atomicservice.ldap.enums.ResultErrorEnum.*;
import static com.migu.tsg.microservice.atomicservice.ldap.util.RegexUtils.*;

import com.migu.tsg.microservice.atomicservice.ldap.biz.mapper.LdapUserMapper;
import com.migu.tsg.microservice.atomicservice.ldap.dto.GetLdapUserResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.InsertLdapMemberRequest;
import com.migu.tsg.microservice.atomicservice.ldap.dto.InsertLdapMemberResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.ListPagenationResponse;
import com.migu.tsg.microservice.atomicservice.ldap.enums.LdapUserTypeEnum;
import com.migu.tsg.microservice.atomicservice.ldap.exception.BadRequestFieldException;
import com.migu.tsg.microservice.atomicservice.ldap.helper.SendMailHelper;
import com.migu.tsg.microservice.atomicservice.ldap.util.InstantUtils;
import com.migu.tsg.microservice.atomicservice.ldap.util.ListSortUtils;
import com.migu.tsg.microservice.atomicservice.ldap.util.Pagenation;

/**
* 项目名称: ldap-service <br>
* 包: com.migu.tsg.microservice.atomicservice.ldap.biz <br>
* 类名称: LdapUserBiz.java <br>
* 类描述: LDAP用户业务层<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年11月6日下午4:10:11 <br>
* 版本: v1.0
*/
@Service
public class LdapUserBiz {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LdapUserBiz.class);

    @Autowired
    private LdapTemplate ldapTemplate;

    @Autowired
    private SendMailHelper sendMailHelper;

    @Autowired
    private RedisCacheHelper redisCacheHelper;

    @Autowired
    private RedisProperties redisProperties;

    /**
     * 默认超时间
     */
    private static final int TIME_OUT = 5;
    /**
     * 默认组织
     */
    @Value("${ldap.defaultOu}")
    private String defaultOu;

    /**
     * LDAP唯一标识串分隔符
     */
    public static final String DN_SEPARATOR = ",";

    /**
     * 过滤LDAP的对象类(objectClass)条件
     */
    private static final String FILTER_OBJECTCLASS = "&(objectclass=inetOrgPerson)";

    /**
     * 获取命名空间(根账号)信息
     * @param namespace 命名空间(根账号)
     * @return 响应对象
     */
    public GetLdapUserResponse getLdapAdmin(final String namespace) {
        LdapUser ldapAdmin = lookup("namesapce", namespace);
        GetLdapUserResponse response = map(ldapAdmin);
        response.setAllProjects(ldapAdmin.getProjects());
        LOGGER.info("Query one ldap user {} is successful", namespace);
        return response;
    }

    /**
     * 修改单个根账号信息(比如:公司名称,公司头像)
     * @param namespace 命名空间(根账号)
     * @param company 公司名称
     * @param mail 公司邮箱
     * @param jpegPhoto 公司头像(Base64字符串)
     * @param newPassword 新密码
     * @param oldPassword 旧密码
     */
    public void updateLdapAdmin(final String namespace, final String company, final String mail,
            final String jpegPhoto, final String newPassword, final String oldPassword) {
        DirContextOperations context = null;
        try {
            context = ldapTemplate.lookupContext(buildDn(namespace));
        } catch (Exception e) { // NameNotFoundException
            throw new BadRequestFieldException(INVALID_ARGS, "namespace",
                    new String[] { dynamicMessage(LDAP_USER_NOT_EXIST, namespace, defaultOu) }, e);
        }
        if (StringUtils.isNotBlank(company)) {
            context.setAttributeValue(O, company);
        }
        if (StringUtils.isNotBlank(mail)) {
            if (!hasMatchesRegexEmails(mail)) {
                throw new BadRequestFieldException(INVALID_ARGS, "mail",
                        new String[] { dynamicMessage(USER_EMAIL_INVALID, mail) });
            }
            context.setAttributeValue(MAIL, mail);
        }
        if (StringUtils.isNotBlank(jpegPhoto)) {
            context.setAttributeValue(JPEGPHOTO, jpegPhoto);
        }
        if (StringUtils.isNotBlank(newPassword)) {
            if (!hasMatchesRegexPasswords(newPassword)) {
                throw new BadRequestFieldException(INVALID_ARGS, "newPassword",
                        new String[] { dynamicMessage(USER_PASSWORD_INVALID, newPassword) });
            }
            authenticate(namespace, null, oldPassword);
            context.setAttributeValue(USERPASSWORD, newPassword);
        }
        try {
            ldapTemplate.modifyAttributes(context);
        } catch (Exception e) {
            throw new BadRequestFieldException(INVALID_ARGS, "namespace",
                    new String[] { dynamicMessage(UPDATE_LDAP_USER_FAILED, namespace) }, e);
        }
        LOGGER.info("Modify ldap user {} success", namespace);
    }

    /**
     * 获取命名空间(根账号)中的成员信息
     * @param namespace 命名空间(根账号)
     * @param username 用户名称
     * @return 响应对象
     */
    public GetLdapUserResponse getLdapMember(final String namespace, final String username) {
        LdapUser ldapAdmin = lookup("namesapce", namespace);
        LdapUser ldapUser = lookup("username", username, namespace);
        ldapUser.setCompany(ldapAdmin.getCompany());
        if (StringUtils.isBlank(ldapUser.getJpegPhoto())) {
            ldapUser.setJpegPhoto(ldapAdmin.getJpegPhoto());
        }
        if (StringUtils.isBlank(ldapUser.getMail())) {
            ldapUser.setMail(ldapAdmin.getMail());
        }
        GetLdapUserResponse response = map(ldapUser);
        response.setAllProjects(ldapAdmin.getProjects());
        LOGGER.info("Query one ldap user {} in {} is successful", username, namespace);
        return response;
    }

    /**
     * 获取命名空间中(根账号)的成员信息列表
     * @param namespace 命名空间(根账号)
     * @param usernames 成员名称集合,按成员名称使用“包含”算法搜索
     * @param projects 所属项目集合,按项目名称使用“包含”算法搜索
     * @param orderBy 来改变默认的列表排序。选项："createTime"和"username"做降序排序就加一个“-”符号的开始。例如-username
     * @param pageSize 在一个页面成功的项目数，默认为20,如果该值为非正数,则表示返回查询结果的全部数据
     * @param currentPage 选择所需页，默认为1
     * @return 响应对象
     */
    public ListPagenationResponse listLdapMember(final String namespace, final List<String> uuids, final List<String> usernames,final List<String> names,
            final List<String> projects, final List<String> orderBy, final int pageSize,
            final int currentPage) {
        StringBuffer filter = new StringBuffer("(" + FILTER_OBJECTCLASS + "(&");
        if (CollectionUtils.isNotEmpty(usernames)) {
            filter.append("(|");
            for (String username : usernames) {
                if (StringUtils.isNotBlank(username)) {
                    filter.append("(uid=*" + username + "*)");
                }
            }
            filter.append(")");
        }
        if (CollectionUtils.isNotEmpty(uuids)) {
            filter.append("(|");
            for (String uuid : uuids) {
                if (StringUtils.isNotBlank(uuid)) {
                    filter.append("(uid=" + uuid + ")");
                }
            }
            filter.append(")");
        }
        if (CollectionUtils.isNotEmpty(names)) {
            filter.append("(|");
            for (String name : names) {
                if (StringUtils.isNotBlank(name)) {
                    filter.append("(sn=" + name + ")");
                }
            }
            filter.append(")");
        }
        if (CollectionUtils.isNotEmpty(projects)) {
            filter.append("(|");
            for (String project : projects) {
                if (StringUtils.isNotBlank(project)) {
                    filter.append("(project=*" + project + "*)");
                }
            }
            filter.append(")");
        }
        filter.append("))");
        List<LdapUser> result = ldapTemplate.search(buildDn(namespace), filter.toString(),
                SearchControls.ONELEVEL_SCOPE, new LdapUserMapper());
        // 排序
        sort(result, orderBy);
        //        result.stream()
        //                .sorted(Comparator.comparing(LdapUser::getCreateTime, Comparator.reverseOrder())
        //                        .thenComparing(LdapUser::getUsername, Comparator.reverseOrder()))
        //                .collect(Collectors.toList());

        // 分页获取集合中的数据
        Pagenation pagenation = null;
        if (pageSize > 0) {
            pagenation = new Pagenation(pageSize, currentPage, result.size());
            result = result.stream().skip(pagenation.getStartRow()).limit(pagenation.getPageSize())
                    .collect(Collectors.toList());
        }
        ListPagenationResponse response = new ListPagenationResponse();
        if (pagenation != null) {
            BeanUtils.copyProperties(pagenation, response);
        }
        response.setResults(map(result, lookup("namesapce", namespace)));
        LOGGER.info("Query ldap user list is successful");
        return response;
    }

    /**
     * 新增命名空间(根账号)中的单个成员信息
     * @param namespace 命名空间
     * @param requests 请求对象集合
     * @return 响应对象集合
     */
    public List<InsertLdapMemberResponse> insertLdapMember(final String namespace,
            final List<InsertLdapMemberRequest> requests) {
        List<InsertLdapMemberResponse> response = new ArrayList<>();
        if (CollectionUtils.isEmpty(requests)) {
            throw new BadRequestFieldException(INVALID_ARGS, "request",
                    new String[] { dynamicMessage(INSERT_LEAST_ONE_LDAP_USER, namespace) });
        }
        LdapUser ldapAdmin = lookup("namesapce", namespace);
        List<String> allProjects = ldapAdmin.getProjects();
        int index = 0;
        List<DirContextOperations> contextList = new ArrayList<>();
        List<Map<String, String>> user = new ArrayList<>();
        Set<String> setUsername = new HashSet<>();
        for (InsertLdapMemberRequest request : requests) {
            validateInsertLdapUserParameter(index, setUsername, request, allProjects);
            // 若组织名称无项目,则清空新增用户的所属项目集合
            List<String> projectList = CollectionUtils.isEmpty(allProjects) ? new ArrayList<>()
                    : request.getProjects();
            String newPassword = getPassword(request.getPassword());
            contextList.add(getContext(namespace, request.getUsername(), request.getMobile(),
                    request.getMail(), newPassword, request.getName(), request.getDept(), projectList,
                    request.getType(),request.getDescription()));
            response.add(getInsertLdapMemberResponse(namespace, request.getUsername(), request.getMobile(),
                    request.getMail(), newPassword, request.getName(), request.getDept(), projectList));
            index++;
            Map<String, String> map = new HashMap<>();
            map.put("username", request.getUsername());
            map.put("password", newPassword);
            user.add(map);
        }
        bind(namespace, contextList);
        // 发送邮箱
        sendMailHelper.submit(ldapAdmin.getMail(), user);
        return response;
    }

    /**
     * 批量新增用户
     * @param namespace 空间名称
     * @param contextList 用户数据集合
     */
    private void bind(final String namespace, List<DirContextOperations> contextList) {
        int index = 0;
        List<String> usernameList = new ArrayList<>();
        for (DirContextOperations context : contextList) {
            String username = context.getStringAttribute(UID);
            try {
                // LDAP用户之UID全组织唯一,验证USERNAME是否已经存在
                if (CollectionUtils.isNotEmpty(ldapTemplate.search(buildDn(),
                        "(" + FILTER_OBJECTCLASS + "(" + UID + "=" + username + "))",
                        new LdapUserMapper()))) {
                    LOGGER.info("Add ldap user {} in {} fail, Ldap user {} already exist", username,
                            namespace, username);
                    throw new NameAlreadyBoundException();
                }
                LOGGER.info("ldap context is {}", context);
                ldapTemplate.bind(context);
            } catch (Exception e) {
                // 新增出现异常,删除本次请求已经新增的用户(模拟回滚事务)
                for (String userName : usernameList) {
                    try {
                        ldapTemplate.unbind(buildDn(userName, namespace));
                        LOGGER.info(
                                "Delete the user {} in {} that has been added, Batch add transaction rollback for success",
                                userName, namespace);
                    } catch (Exception ex) {
                        LOGGER.warn(
                                "Delete the user {} in {} that has been added, Batch add transaction rollback for fail",
                                userName, namespace, ex);
                    }
                }
                if (e instanceof NameAlreadyBoundException) {
                    LOGGER.info("Ldap user {} already exist", username);
                    throw new BadRequestFieldException(INVALID_ARGS, index + ".username",
                            new String[] { dynamicMessage(USER_NAME_ALREADY_EXIST, username) });
                }
                LOGGER.info("Add ldap user {} in {} fail", username, namespace);
                LOGGER.error("Add ldap user is failed", e);
                throw new BadRequestFieldException(INVALID_ARGS, "request",
                        new String[] { dynamicMessage(INSERT_LDAP_USER_FAILED, username, namespace) });
            }
            index++;
            usernameList.add(username);
            LOGGER.info("Add ldap user {} in {} success", username, namespace);
        }
    }

    /**
     * 若密码为空,则随机生成新密码
     * @param password 请求参数密码
     * @return 新密码
     */
    private String getPassword(final String password) {
        String newPassword = password;
        if (StringUtils.isBlank(password)) {
            newPassword = RandomStringUtils.randomAlphanumeric(6);
        }
        return newPassword;
    }

    /**
     * 验证批量新增参数
     * @param index 索引
     * @param setUsername 新增用户集合,用于判断PAYLOAD中用户名是否重复
     * @param request 请求对象
     * @param allProjects 组织名称的项目集合
     */
    private void validateInsertLdapUserParameter(final int index, final Set<String> setUsername,
            final InsertLdapMemberRequest request, final List<String> allProjects) {
        if (StringUtils.isBlank(request.getUsername())) {
            throw new BadRequestFieldException(INVALID_ARGS, index + ".username",
                    new String[] { USER_NAME_CANNOT_BE_EMPTY.getMessage() });
        }
//        if (!hasMatchesRegexUsernames(request.getUsername())) {
//            throw new BadRequestFieldException(INVALID_ARGS, index + ".username",
//                    new String[] { dynamicMessage(USER_NAME_INVALID, request.getUsername()) });
//        }
        if (setUsername.contains(request.getUsername())) {
            throw new BadRequestFieldException(INVALID_ARGS, index + ".username", new String[] {
                    dynamicMessage(USER_NAME_ALREADY_EXIST_IN_PAYLOAD, request.getUsername()) });
        }
        setUsername.add(request.getUsername());
        //        if (StringUtils.isBlank(request.getMail())) {
        //            throw new BadRequestFieldException(INVALID_ARGS, index + ".mail",
        //                    new String[] { USER_MAIL_CANNOT_BE_EMPTY.getMessage() });
        //        }
//        if (StringUtils.isNotBlank(request.getMail()) && !hasMatchesRegexEmails(request.getMail())) {
//            throw new BadRequestFieldException(INVALID_ARGS, index + ".mail",
//                    new String[] { dynamicMessage(USER_EMAIL_INVALID, request.getMail()) });
//        }
        if (StringUtils.isNotBlank(request.getPassword())
                && !hasMatchesRegexPasswords(request.getPassword())) {
            throw new BadRequestFieldException(INVALID_ARGS, index + ".password",
                    new String[] { dynamicMessage(USER_PASSWORD_INVALID, request.getPassword()) });
        }
//        if (StringUtils.isNotBlank(request.getMobile()) && !hasMatchesRegexMobiles(request.getMobile())) {
//            throw new BadRequestFieldException(INVALID_ARGS, index + ".mobile",
//                    new String[] { dynamicMessage(USER_MOBILE_INVALID, request.getMobile()) });
//        }
        // 用户所属项目必须存在于组织名称的项目中
        if (CollectionUtils.isNotEmpty(allProjects) && CollectionUtils.isNotEmpty(request.getProjects())) {
            int index2 = 0;
            for (String project : request.getProjects()) {
                if (!allProjects.contains(project)) {
                    throw new BadRequestFieldException(INVALID_ARGS, index + ".projects." + index2,
                            new String[] { dynamicMessage(USER_PROJECT_NOT_EXIST, project) });
                }
                index2++;
            }
        }
    }

    /**
     * 修改命名空间(根账号)中的单个成员信息
     * @param namespace 命名空间
     * @param username 用户账号(用户用户名,LDAP全组织唯一)
     * @param mobile 用户手机号码
     * @param mail 用户邮箱
     * @param oldPassword 用户登录旧密码
     * @param newPassword 用户登录新密码
     * @param name 用户真实姓名
     * @param dept 用户所属部门
     * @param projects 用户所属项目
     * @return 响应对象
     */
    public void updateLdapMember(final String namespace, final String username, final String mobile,
            final String mail, final String oldPassword, final String newPassword, final String name,
            final String dept, final List<String> projects,final boolean isUpdateTime,
             final String employeeType, final String description) {
        DirContextOperations context = null;
        try {
            context = ldapTemplate.lookupContext(buildDn(username, namespace));
        } catch (Exception e) { // NameNotFoundException
            throw new BadRequestFieldException(INVALID_ARGS, "username",
                    new String[] { dynamicMessage(LDAP_USER_NOT_EXIST, username, namespace) });
        }
        context = getContext(context, namespace, username, mobile, mail, oldPassword, newPassword, name, dept,
                projects, lookup("namesapce", namespace).getProjects(),isUpdateTime,employeeType,description);
        try {
            ldapTemplate.modifyAttributes(context);
        } catch (Exception e) {
            throw new BadRequestFieldException(INVALID_ARGS, "username",
                    new String[] { dynamicMessage(UPDATE_LDAP_USER_FAILED, username, namespace) });
        }
        LOGGER.info("Modify ldap user {} in {} success", username, namespace);
    }

    /**
     * 验证修改用户参数以及封装修改用户数据对象
     * @param context 用户数据对象
     * @param namespace 命名空间
     * @param username 用户名称
     * @param mobile 用户手机号码
     * @param mail 用户邮箱
     * @param oldPassword 用户旧密码
     * @param newPassword 用户新密码
     * @param name 用户真实姓名
     * @param dept 用户所属部门
     * @param projects 用户所属项目集合
     * @param allProjects 根账号的项目集合
     * @return 修改用户数据对象
     */
    private DirContextOperations getContext(final DirContextOperations context, final String namespace,
            final String username, final String mobile, final String mail, final String oldPassword,
            final String newPassword, final String name, final String dept, final List<String> projects,
            final List<String> allProjects,final boolean isUpdateTime,
            final String employeeType, final String description) {
        if (StringUtils.isNotBlank(name)) {
            context.setAttributeValue(SN, name);
        }
        if (StringUtils.isNotBlank(newPassword)) {
//            if (!hasMatchesRegexPasswords(newPassword)) {
//                throw new BadRequestFieldException(INVALID_ARGS, "newPassword",
//                        new String[] { dynamicMessage(USER_PASSWORD_INVALID, newPassword) });
//            }
//            authenticate(namespace, username, oldPassword);
            context.setAttributeValue(USERPASSWORD, newPassword);
        }

        if (StringUtils.isNotBlank(mobile)) {
//            if (!hasMatchesRegexMobiles(mobile)) {
//                throw new BadRequestFieldException(INVALID_ARGS, "mobile",
//                        new String[] { dynamicMessage(USER_MOBILE_INVALID, mobile) });
//            }
            context.setAttributeValue(MOBILE, mobile);
        }
        if (StringUtils.isNotBlank(mail)) {
//            if (!hasMatchesRegexEmails(mail)) {
//                throw new BadRequestFieldException(INVALID_ARGS, "mail",
//                        new String[] { dynamicMessage(USER_EMAIL_INVALID, mail) });
//            }
            context.setAttributeValue(MAIL, mail);
        }
        if (StringUtils.isNotBlank(dept)) {
            context.setAttributeValue(DEPARTMENTNUMBER, dept);
        }
        if (isUpdateTime) {
            // 创建时间
            context.setAttributeValue(CREATETIME, InstantUtils.now());
        }
        if (StringUtils.isNotBlank(employeeType)) {
            context.setAttributeValue(EMPLOYEETYPE, employeeType);
        }
        if (StringUtils.isNotBlank(description)) {
            context.setAttributeValue(DESCRIPTION, description);
        }
        if (CollectionUtils.isNotEmpty(projects)) {
            // 用户所属项目必须存在于组织名称的项目中
            if (CollectionUtils.isNotEmpty(allProjects)) {
                int index = 0;
                for (String project : projects) {
                    if (!allProjects.contains(project)) {
                        throw new BadRequestFieldException(INVALID_ARGS, "projects." + index,
                                new String[] { dynamicMessage(USER_PROJECT_NOT_EXIST, project) });
                    }
                    index++;
                }
            }
            context.setAttributeValues(PROJECT,
                    CollectionUtils.isEmpty(allProjects) ? null : projects.toArray());
        }
//        context.setAttributeValue(UPDATETIME, InstantUtils.now());
        return context;
    }

    /**
     * 校验用户旧密码
     * @param namespace 空间名称
     * @param username 用户名称
     * @param oldPassword 用户旧密码
     */
    private void authenticate(final String namespace, final String username, final String oldPassword) {
        if (StringUtils.isNotBlank(oldPassword)) {
            boolean bl = false;
            if (StringUtils.isNotBlank(username)) {
                try {
                    bl = ldapTemplate.authenticate(buildDn(namespace),
                            "(" + FILTER_OBJECTCLASS + "(" + UID + "=" + username + "))", oldPassword);
                } catch (Exception e) {
                    throw new BadRequestFieldException(INVALID_ARGS, "old_password",
                            new String[] { dynamicMessage(VERIFY_LDAP_USER_PASSWORD_FAILED, username,
                                    namespace, oldPassword) },
                            e);
                }
                if (!bl) {
                    throw new BadRequestFieldException(INVALID_ARGS, "old_password", new String[] {
                            dynamicMessage(USER_OLD_PASSWORD_INCORRECT, username, namespace, oldPassword) });
                }
                LOGGER.info("Verify ldap user {} in {} old password {} success", username, namespace,
                        oldPassword);

            } else {
                try {
                    bl = ldapTemplate.authenticate(buildDn(),
                            "(" + FILTER_OBJECTCLASS + "(" + UID + "=" + namespace + "))", oldPassword);
                } catch (Exception e) {
                    throw new BadRequestFieldException(INVALID_ARGS, "old_password",
                            new String[] { dynamicMessage(VERIFY_LDAP_USER_PASSWORD_FAILED, namespace,
                                    defaultOu, oldPassword) },
                            e);
                }
                if (!bl) {
                    throw new BadRequestFieldException(INVALID_ARGS, "old_password", new String[] {
                            dynamicMessage(USER_OLD_PASSWORD_INCORRECT, namespace, defaultOu, oldPassword) });
                }
                LOGGER.info("Verify ldap user {} in {} old password {} success", namespace, defaultOu,
                        oldPassword);

            }
        }
    }

    /**
     * 删除命名空间(根账号)中的单个成员信息
     * @param namespace 命名空间
     * @param username 成员名称
     */
    public void deleteLdapMember(final String namespace, final String username) {
        try {
            ldapTemplate.unbind(buildDn(username, namespace));
        } catch (Exception e) { // NameNotFoundException
            throw new BadRequestFieldException(INVALID_ARGS, "username",
                    new String[] { dynamicMessage(DELETE_LDAP_USER_FAILED, username, namespace) }, e);
        }
        LOGGER.info("Remove ldap user {} in {} success", username, namespace);
    }

    /**
     * 封装响应数据
     * @param namespace 命名空间
     * @param username 成员名称
     * @param mobile 手机号码
     * @param mail 邮箱
     * @param password 密码
     * @param name 真实姓名
     * @param dept 所属部门
     * @param projects 所属项目集合
     * @return 响应对象
     */
    private InsertLdapMemberResponse getInsertLdapMemberResponse(final String namespace,
            final String username, final String mobile, final String mail, final String password,
            final String name, final String dept, final List<String> projects) {
        InsertLdapMemberResponse response = new InsertLdapMemberResponse();
        response.setUsername(username);
        response.setUserType(LdapUserTypeEnum.user);
        response.setNamespace(lookup("namesapce", namespace).getUsername());
        // 若NAME为空,则USERNAME填充(LDAP姓名SN字段必填)
        response.setName(StringUtils.isBlank(name) ? username : name);
        response.setPassword(password);
        response.setMobile(mobile);
        response.setMail(mail);
        response.setDept(dept);
        response.setProjects(projects);
        response.setCreateTime(InstantUtils.now());
        return response;
    }

    /**
     * 封装新增LDAP用户数据
     * @param namespace 命名空间
     * @param username 成员名称
     * @param mobile 手机号码
     * @param mail 邮箱
     * @param password 密码
     * @param name 真实姓名
     * @param dept 所属部门
     * @param projects 所属项目集合
     * @return 新增LDAP用户数据对象
     */
    private DirContextOperations getContext(final String namespace, final String username,
            final String mobile, final String mail, final String password, final String name,
            final String dept, final List<String> projects, final String type, final String description) {
        DirContextOperations context = new DirContextAdapter(buildDn(username, namespace));
        // 对象类型
        context.setAttributeValue(OBJECTCLASS, INETORGPERSON);
        // 用户名称(LDAP之UID字段必填)
        context.setAttributeValue(UID, username);
        // Common Name为用户名(LDAP之CN字段必填)
        context.setAttributeValue(CN, username);
        // 密码
        context.setAttributeValue(USERPASSWORD, password);
        // 用户类型.命名空间
        context.setAttributeValue(EMPLOYEETYPE, LdapUserTypeEnum.user.toString() + "." + namespace);
        // 真实姓名,若NAME为空,则USERNAME填充(LDAP姓名SN字段必填)
        context.setAttributeValue(SN, StringUtils.isBlank(name) ? username : name);
        // 手机号码
        context.setAttributeValue(MOBILE, mobile);
        // 邮箱
        context.setAttributeValue(MAIL, mail);
        // 部门
        context.setAttributeValue(DEPARTMENTNUMBER, dept);
        // 所属项目
        if (CollectionUtils.isNotEmpty(projects)) {
            context.setAttributeValues(PROJECT,
                    projects.stream().filter(e -> StringUtils.isNotBlank(e)).toArray());
        }
        // 创建时间
        context.setAttributeValue(CREATETIME, InstantUtils.now());

        context.setAttributeValue(DESCRIPTION, description);
        context.setAttributeValue(EMPLOYEETYPE, type);
        // 更新时间
//        context.setAttributeValue(UPDATETIME, InstantUtils.now());
        return context;
    }

    /**
     * 排序
     * @param list 集合对象
     * @param orderBy 排序字段集合
     */
    private void sort(final List<LdapUser> list, final List<String> orderBy) {
        if (CollectionUtils.isEmpty(orderBy)) {
            return;
        }
        String[] sortNameArr = new String[orderBy.size()];
        boolean[] isAscArr = new boolean[orderBy.size()];
        for (int i = 0; i < orderBy.size(); i++) {
            String field = StringUtils.trimToEmpty(orderBy.get(i)).replace("-", "");
            boolean type = Pattern.compile("-").matcher(StringUtils.trimToEmpty(orderBy.get(i))).find();
            sortNameArr[i] = field;
            isAscArr[i] = !type;
        }
        ListSortUtils.sort(list, sortNameArr, isAscArr);
    }

    /**
     * 获取LDAP User对象
     * @param uids LDAP User Id集合
     * @return LDAP User对象
     */
    private LdapUser lookup(final String fileName, final String... uids) {
        LdapUser ldapUser = null;
        try {
            ldapUser = ldapTemplate.lookup(buildDn(uids), new LdapUserMapper());
        } catch (NameNotFoundException e) {
            if (uids.length == 1) {
                throw new BadRequestFieldException(INVALID_ARGS, fileName,
                        new String[] { dynamicMessage(LDAP_USER_NOT_EXIST, uids[0], defaultOu) }, e);
            } else {
                throw new BadRequestFieldException(INVALID_ARGS, fileName,
                        new String[] { dynamicMessage(LDAP_USER_NOT_EXIST, uids) }, e);
            }
        }
        return ldapUser;
    }

    /**
     * 获取LDAP User DN
     * Distinguished Name，LDAP中entry的唯一辨别名，一条完整的DN写法：uid=zhang3,ou=People,dc=163,dc=com
     * LDAP中的entry只有DN是由LDAP Server来保证唯一的
     * @param uids LDAP User Id集合
     * @return LDAP User对象
     */
    private String buildDn(final String... uids) {
        StringBuffer dn = new StringBuffer();
        if (uids != null && uids.length > 0) {
            for (String uid : uids) {
                if (StringUtils.isNotBlank(uid)) {
                    dn.append(getUidDn(uid)).append(DN_SEPARATOR);
                }
            }
        }
        dn.append(getOuDn(defaultOu));
        return dn.toString();
    }

    /**映射封装响应对象集合
     * @param result LDAP用户对象集合
     * @param namespace 根账号
     * @return 响应对象集合
     */
    private List<GetLdapUserResponse> map(final List<LdapUser> result, final LdapUser namespace) {
        List<GetLdapUserResponse> responseList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(result)) {
            responseList = result.stream().filter(ldapUser -> LdapUserTypeEnum.user == ldapUser.getUserType())
                    .map(ldapUser -> {
                        GetLdapUserResponse response = map(ldapUser);
                        response.setAllProjects(namespace.getProjects());
                        response.setCompany(namespace.getCompany());
                        if (StringUtils.isBlank(response.getMail())) {
                            response.setMail(namespace.getMail());
                        }
                        return response;
                    }).collect(Collectors.toList());
        }
        return responseList;
    }

    /**
     * 映射封装响应对象
     * @param ldapUser LDAP用户对象
     * @return 响应对象
     */
    private GetLdapUserResponse map(final LdapUser ldapUser) {
        GetLdapUserResponse response = new GetLdapUserResponse();
        BeanUtils.copyProperties(ldapUser, response);
        return response;
    }

    /**
     * 获取uid的LDAP唯一标识串
     * @param uid 用户ID
     * @return 标识串,如:"uid=user"
     */
    private String getUidDn(String uid) {
        return "uid=" + uid;
    }

    /**
     * 获取组织的LDAP唯一标识串
     * @param ou 组织
     * @return 标识串,如:"ou=migu"
     */
    private String getOuDn(String ou) {
        return "ou=" + ou;
    }


    /**
     * 检验验证码
     * @param inputCode
     * @param validCodeKey
     * @param response
     */
    public void checkValidCode(String inputCode, String validCodeKey, HttpServletResponse response) {
        String result = checkCode(inputCode, validCodeKey);
        try {
            PrintWriter out = response.getWriter();
            out.write(result);
            out.flush();
            out.close();
        }catch (IOException e) {
            LOGGER.error("fail to save result to response");
            throw new RuntimeException("fail to save check code result to response.", e);
        }
    }

    public String checkCode(String inputCode, String validCodeKey) {
        String currentValidKey = "";
        if (StringUtils.isEmpty(validCodeKey)) {
            LOGGER.info("method [checkValidCode] valid code key is empty");
            return LdapValidCodeEnum.VALID_CODE_CHECK_ERROR.getCallbackCode();
        }
        if (StringUtils.isEmpty(inputCode)) {
            LOGGER.info("method [checkValidCode] input code is empty");
            return LdapValidCodeEnum.VALID_CODE_CHECK_ERROR.getCallbackCode();
        }
        if (!redisCacheHelper.hasKey(redisProperties.getRedisKeyPrefixValidCodeList())) {
            LOGGER.info("method [checkValidCode] valid code list is no exist in redis");
            return LdapValidCodeEnum.VALID_CODE_CHECK_ERROR.getCallbackCode();
        }
        ValidCode validCode = (ValidCode) redisCacheHelper.get(redisProperties.getRedisKeyPrefixValidCodeList(), validCodeKey);

        if (null == validCode) {
            LOGGER.info("method [checkValidCode] valid code key is no exist in redis");
            return LdapValidCodeEnum.VALID_CODE_CHECK_ERROR.getCallbackCode();
        }
        String beCheckCode = validCode.getCode();
        Timestamp createDate = validCode.getDate();

        if (beCheckCode.equalsIgnoreCase(inputCode)) {
            /**
             * 判断验证码是否超时
             * */
            long seconds = TIME_OUT * DateUtil.DATE_MINUTE_TO_SECONDS;
            Date timeOutDate = DateUtil.getIntevalOfDate(createDate, seconds);
            if (1 != DateUtil.compare(timeOutDate, new Date())) {
                LOGGER.info("method [checkValidCode] valid code key is time out");
                return LdapValidCodeEnum.VALID_CODE_CHECK_TIME_OUT.getCallbackCode();
            }
            redisCacheHelper.delete(currentValidKey);
            return LdapValidCodeEnum.VALID_CODE_CHECK_CORRECT.getCallbackCode();
        }
        return LdapValidCodeEnum.VALID_CODE_CHECK_ERROR.getCallbackCode();
    }
    /**
     * 获取验证码图片
     * @param callback callback
     * @param response response
     */
    public void getCodeImage(String callback, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Set-Cookie","cookiename=cookievalue; path=/; Domain=domainvaule; Max-age=seconds; HttpOnly");

        /** 生成随机字串 */
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        ValidCode validCode = new ValidCode(verifyCode, new Timestamp(System.currentTimeMillis()));
        /** 生成验证码KEY字符串 */
        String verifyCodeKey = VerifyCodeUtils.getValidCodeKey(5, 5);
        /** 把验证码KEY存入REDIS */
        redisCacheHelper.put(redisProperties.getRedisKeyPrefixValidCodeList(),verifyCodeKey, validCode);
        /** 生成图片 */
        int w = 100, h = 40;
        String imgSrc = "";
        try {
            imgSrc = VerifyCodeUtils.outputImage(w, h, verifyCode);
            Map<String,String> map = new HashMap<>();
            map.put("imgSrc",imgSrc);
            map.put("validCodeKey", verifyCodeKey);
            JSONObject jsonObject = JSONObject.fromObject(map);
            String result = jsonObject.toString();
            //前端传过来的回调函数名称
//            String callback = request.getParameter("callback");
//            if (callback.isEmpty()) {
//                LOGGER.error("there is no callback from ajax");
//                throw new RuntimeException("there is no callback from ajax");
//            }
            //用回调函数名称包裹返回数据，这样，返回数据就作为回调函数的参数传回去了
            result = callback + "(" + result + ")";
            response.getWriter().write(result);
        } catch (IOException e) {
            LOGGER.error("create valid code image has error {}" , e);
        }
    }

    public Map<String,String> getValidCode () {
        /** 生成随机字串 */
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        ValidCode validCode = new ValidCode(verifyCode, new Timestamp(System.currentTimeMillis()));
        /** 生成验证码KEY字符串 */
        String verifyCodeKey = VerifyCodeUtils.getValidCodeKey(5, 5);

        Map<String,String> map = new HashMap<>();
        map.put("inputCode",verifyCode);
        map.put("validCodeKey", verifyCodeKey);
        /** 把验证码KEY存入REDIS */
        redisCacheHelper.put(redisProperties.getRedisKeyPrefixValidCodeList(),verifyCodeKey, validCode);
        return map;
    }
}
