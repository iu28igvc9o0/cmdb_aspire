package com.aspire.ums.cmdb.schedule;

import com.aspire.mirror.common.util.DateUtil;
import com.aspire.ums.cmdb.common.StringUtils;
import com.aspire.ums.cmdb.sync.client.DepartmentServiceClient;
import com.aspire.ums.cmdb.vo.KgLdapOrganization;
import com.aspire.ums.cmdb.vo.KgLdapOrganizationMapper;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.rbac.dto.DepartmentCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.DepartmentUpdateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.DepartmentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;
import java.util.List;

/**
 * @Author: baiwenping
 * @Description: 同步科管部组织结构到ums
 * @Date: create in 2020/10/23 18:29
 */
@Component
@Slf4j
@ConditionalOnExpression("${schedule.kgSync.flag:false}")
public class SyncKeGuanOrganizationService {
    @Autowired
    private LdapTemplate ldapTemplate;

    @Autowired
    private DepartmentServiceClient departmentServiceClient;
    @Value("${ldapconfig.namespace:alauda}")
    private String namespace;

    @Async
    @Scheduled(cron = "${kgSync.organization.cron: 0 0 */1 * * ?}")
    public void sync() {
        log.info("研究院组织结构同步开始");
        // get organization data from ldap server
        List<KgLdapOrganization> kgLdapOrganizationList = getOrganizationListFromLdap();
        if (!CollectionUtils.isEmpty(kgLdapOrganizationList)) {
            syncOrganizationToUms(kgLdapOrganizationList);
            log.info("研究院组织结构同步完毕");
        } else {
            log.info("研究院组织结构无用户需同步");
        }
    }

    /**
     * 从科管ldap获取ldap组织结构, 分页查找用户
     * @since 2020-10-26 15:22:22
     * @return
     */
    private List<KgLdapOrganization> getOrganizationListFromLdap() {
        List<KgLdapOrganization> rtnList = Lists.newArrayList();
        try {
            SearchControls schCtrls = new SearchControls();
            //指定检索范围
            /*
             * 0:OBJECT_SCOPE,搜索指定的命名对象。
             * 1:ONELEVEL_SCOPE,只搜索指定命名对象的一个级别，这是缺省值。
             * 2:SUBTREE_SCOPE,搜索以指定命名对象为根结点的整棵树  **/
            schCtrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            int pageSize = 500;
            byte[] cookie = null;
            ContextSource contextSource = ldapTemplate.getContextSource();
            DirContext ctx = contextSource.getReadWriteContext();
            LdapContext lCtx = (LdapContext) ctx;
            //分页
            lCtx.setRequestControls(new Control[] { new PagedResultsControl(  pageSize, Control.CRITICAL) });
            int totalResults = 0;
            KgLdapOrganizationMapper kgLdapOrganizationMapper = new KgLdapOrganizationMapper();
            do {
                //搜索的结果的枚举属性
                NamingEnumeration<SearchResult> results = lCtx.search("ou=organizations,dc=cmri", "(&(objectClass=*))", schCtrls);
                while (null != results && results.hasMoreElements()) {//结果不为空且有值
                    SearchResult sr = results.next();
                    Attributes attrs = sr.getAttributes();
                    KgLdapOrganization kgLdapOrganization = kgLdapOrganizationMapper.mapFromAttributes(attrs);
                    rtnList.add(kgLdapOrganization);
                    totalResults++;
                }
                //cookie是一个字节数组，包含了通过PagedResultsControl下一次调用服务器时所需的信息
                cookie = parseControls(lCtx.getResponseControls());
                lCtx.setRequestControls(new Control[] { new PagedResultsControl(  pageSize, cookie, Control.CRITICAL) });
            } while ((cookie != null) && (cookie.length != 0));
            lCtx.close();
            log.info("Total = " + totalResults);

            log.info("同步研究院ldap用户总数: {}", rtnList.size());
        } catch (Exception e) {
            log.error("获取研究院ldap数据失败", e);
        }
        return rtnList;
    }

    private byte[] parseControls(Control[] controls)
            throws NamingException {
        byte[] cookie = null;
        if (controls != null) {
            for (int i = 0; i < controls.length; i++) {
                if (controls[i] instanceof PagedResultsResponseControl) {
                    PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
                    cookie = prrc.getCookie();
                    System.out.println(">>Next Page \n");
                }
            }
        }
        return (cookie == null) ? new byte[0] : cookie;
    }

    private void syncOrganizationToUms(List<KgLdapOrganization> kgLdapOrganizationList) {
        List<DepartmentDTO> departmentDTOList = departmentServiceClient.getAll();
        List<DepartmentCreateRequest> departmentCreateRequestList = Lists.newArrayList();

        if (!CollectionUtils.isEmpty(kgLdapOrganizationList)) {
            boolean flag;
            for (KgLdapOrganization kgLdapOrganization: kgLdapOrganizationList) {
                flag = false;
                String o = kgLdapOrganization.getO();
                for (DepartmentDTO departmentDTO: departmentDTOList) {
                    if (!StringUtils.isEmpty(o) && o.equals(departmentDTO.getUuid())) {
                        flag = true;
                        String parentOrgId = kgLdapOrganization.getParentOrgId();
                        String displayName = kgLdapOrganization.getDisplayName();
                        if ((!StringUtils.isEmpty(parentOrgId) && !parentOrgId.equals(departmentDTO.getParentId())) ||
                                (!StringUtils.isEmpty(displayName) && !displayName.equals(departmentDTO.getName()))) {
                            DepartmentUpdateRequest departmentUpdateRequest = new DepartmentUpdateRequest();
                            departmentUpdateRequest.setName(displayName);
                            departmentUpdateRequest.setDeptType(1);
                            departmentUpdateRequest.setParentId(parentOrgId);
                            departmentUpdateRequest.setDescr("uim " + kgLdapOrganization.getDescription()  + " at " + DateUtil.formatDate(DateUtil.DATE_TIME_CH_FORMAT));
                            log.info("update 研究院组织结构, displayName is : {}", displayName);
                            // 修改
                            try {
                                departmentServiceClient.modifyByPrimaryKey(o, departmentUpdateRequest);
                            } catch (Exception e) {
                                log.error("更新研究院组织结构异常，{}", e);
                            }
                        }
                        break;
                    }
                }
                if (!flag) {
                    departmentCreateRequestList.add(convertOrganization(kgLdapOrganization));
                }
            }
        } else {
            for (KgLdapOrganization kgLdapOrganization: kgLdapOrganizationList) {
                departmentCreateRequestList.add(convertOrganization(kgLdapOrganization));
            }
        }

        // 新增
        try {
            if (!departmentCreateRequestList.isEmpty()) {
                departmentServiceClient.batchCreatedDepartment(departmentCreateRequestList);
            }

        } catch (Exception e) {
            log.error("批量新增研究院组织结构失败， {}", e);
        }

        // 删除
        boolean flag;
        List<String> idList = Lists.newArrayList();
        for (DepartmentDTO departmentDTO : departmentDTOList) {
            flag = false;
            String uuid = departmentDTO.getUuid();
            String descr = departmentDTO.getDescr();
            for (KgLdapOrganization kgLdapOrganization: kgLdapOrganizationList) {
                String o = kgLdapOrganization.getO();
                if (!StringUtils.isEmpty(uuid) && uuid.equals(o)) {
                    flag = true;
                    break;
                }
            }
            if (!flag && (!StringUtils.isEmpty(descr) && descr.startsWith("uim"))) {
                idList.add(uuid);
            }
        }
        if (!idList.isEmpty()) {
            departmentServiceClient.batchDeleteByPrimaryKey(idList);
        }

    }

    private DepartmentCreateRequest convertOrganization (KgLdapOrganization kgLdapOrganization) {
        DepartmentCreateRequest departmentCreateRequest = new DepartmentCreateRequest();
        departmentCreateRequest.setDepartmentId(kgLdapOrganization.getO());
        departmentCreateRequest.setName(kgLdapOrganization.getDisplayName());
        departmentCreateRequest.setDeptType(1);
        departmentCreateRequest.setNamespace(namespace);
        String parentOrgId = kgLdapOrganization.getParentOrgId();
        departmentCreateRequest.setParentId(StringUtils.isEmpty(parentOrgId) ? "1001": parentOrgId);
        departmentCreateRequest.setDescr("uim " + kgLdapOrganization.getDescription()  + " at " + DateUtil.formatDate(DateUtil.DATE_TIME_CH_FORMAT));
        return departmentCreateRequest;
    }
}
