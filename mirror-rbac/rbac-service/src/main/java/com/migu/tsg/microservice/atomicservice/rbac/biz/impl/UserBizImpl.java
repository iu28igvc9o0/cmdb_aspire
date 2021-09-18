package com.migu.tsg.microservice.atomicservice.rbac.biz.impl;

import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.common.util.PageUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.rbac.biz.UserBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.*;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.*;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.transform.UserTransformer;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.DepartmentDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.UserDTO;
import org.apache.commons.lang.ArrayUtils;
import org.assertj.core.util.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务层实现类
 * <p>
 * 项目名称: 微服务运维平台 包: com.migu.tsg.microservice.atomicservice.rbac.biz.impl 类名称: UserBizImpl 类描述: 业务层实现类 创建人: 曾祥华 创建时间: 2019-03-07
 * 16:05:29
 */
@Service("userBiz")
public class UserBizImpl implements UserBiz {

    /**
     * 系统日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserBizImpl.class);

    @Value("${department.deep.query:true}")
    private boolean deepQuery;

    /**
     * dao
     */
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleUsersDao roleUsersDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private DepartmentUserDao departmentUserDao;

    @Autowired
    private UserClassifyAccountDao classDao;

    //默认设置为租户
    @Value("${sync.add.classifyId:8929b2ef-8d6d-48ae-b7f5-09c4f27e3b46}")
    private String classifyId;


    /**
     * 新增数据
     *
     * @param userDTO
     *            动作DTO对象
     * @return String 数据ID
     */
    @Override
    @Transactional
    public String insert(final UserDTO userDTO) {
        if (null == userDTO) {
            LOGGER.error("method[insert] param[userDTO] is null");
            throw new RuntimeException("param[userDTO] is null");
        }
        User user = UserTransformer.toPo(userDTO);
        String userId = UUID.randomUUID().toString();
        user.setUuid(userId);
        userDao.insert(user);

        List<String> deptIds = userDTO.getDeptIds();
        if (!CollectionUtils.isEmpty(deptIds)) {
            List<DepartmentUser> listDeptUser = Lists.newArrayList();
            for (String deptId : deptIds) {
                DepartmentUser departmentUser = new DepartmentUser(deptId, userId);
                listDeptUser.add(departmentUser);
            }
            departmentUserDao.insertBatch(listDeptUser);
        }
        return userId;
    }

    @Override
    @Transactional
    public int insertBatch(List<UserDTO> userDTOList) {
        int result = 0;
        if (null == userDTOList) {
            LOGGER.error("method[insertBatch] param[userDTOList] is null");
            throw new RuntimeException("param[userDTOList] is null");
        }
        // List<User> userList = UserTransformer.toPo(userDTOList);
        List<UserDTO> addList = Lists.newArrayList();
        List<UserDTO> modifyList = Lists.newArrayList();
        List<RoleUsers> roleUsersList = Lists.newArrayList();
        for (UserDTO user : userDTOList) {
            boolean addFlag = true;
            if (!StringUtils.isEmpty(user.getLdapId()) && !StringUtils.isEmpty(user.getNamespace())) {
                user.setLdapStatus("1");
                List<User> resultUser = userDao.selectByLdapIdAndNamespace(user.getLdapId(), user.getNamespace());
                if (!CollectionUtils.isEmpty(resultUser)) {
                    // 2020.12.25 zhu.juwang 通过云管同步用户时, 需要使用uuid去删除、创建用户及部门关系. 因此在这里赋予user.uuid的值
                    user.setUuid(resultUser.get(0).getUuid());
                    addFlag = false;
                }
            }
            if (addFlag) {
                if (!StringUtils.hasText(user.getUuid())) {
                    user.setUuid(UUID.randomUUID().toString());
                }
                addList.add(user);
            } else {
                modifyList.add(user);
            }
        }
        if (!CollectionUtils.isEmpty(addList)) {
            result += userDao.insertBatch(UserTransformer.toPo(addList));
            List<DepartmentUser> listDeptUser = Lists.newArrayList();
            List<UserClassifyAccount> classList = Lists.newArrayList();
            for (UserDTO userDTO : addList) {
                if (!StringUtils.isEmpty(userDTO.getRoles())) {
                    String[] roleIdArray = userDTO.getRoles().split(",");
                    for (String roleId : roleIdArray) {
                        roleUsersList.add(new RoleUsers(roleId, null, userDTO.getNamespace(), userDTO.getLdapId(), null));
                    }
                }
                if (!StringUtils.isEmpty(userDTO.getDeptId())) {
                    DepartmentUser departmentUser = new DepartmentUser(userDTO.getDeptId(), userDTO.getUuid());
                    listDeptUser.add(departmentUser);
                }
                UserClassifyAccount classifyAccount = new UserClassifyAccount(classifyId,userDTO.getUuid());
                classList.add(classifyAccount);

            }
            if (!CollectionUtils.isEmpty(roleUsersList)) {
                // 新增成员和角色的关联关系
                roleUsersDao.createRoleUsersBatch(roleUsersList);
            }
            if (!CollectionUtils.isEmpty(listDeptUser)) {
                departmentUserDao.insertBatch(listDeptUser);
            }
            if (!CollectionUtils.isEmpty(listDeptUser)) {
                classDao.insertBatch(classList);
            }
        }
        if (!CollectionUtils.isEmpty(modifyList)) {
            List<UserDTO> list = new ArrayList<>();
            for (UserDTO userDTO: modifyList) {
                list.clear();
                list.add(userDTO);
                result += userDao.modifyBatchByLdapIdAndNamespace(UserTransformer.toPo(list));
                if (!CollectionUtils.isEmpty(userDTO.getDeptIds())) {
                    departmentUserDao.deleteByUserId(userDTO.getUuid());
                    List<DepartmentUser> listDeptUser = Lists.newArrayList();
                    for (String deptId : userDTO.getDeptIds()) {
                        String uuid = userDTO.getUuid();
                        if(null==uuid||"".equals(uuid)){
                            uuid = userDao.getUuidBy(userDTO.getLdapId(),userDTO.getNamespace());
                        }
                        DepartmentUser departmentUser = new DepartmentUser(deptId, uuid);
                        listDeptUser.add(departmentUser);
                    }
                    departmentUserDao.insertBatch(listDeptUser);
                }
            }
        }
        return result;
    }

    @Override
    public int modifyDeptIdBatchByUserIdArrays(String deptId, List<String> userIdList) {
        departmentUserDao.deleteByUserIdArrays(userIdList);
        List<DepartmentUser> list = Lists.newArrayList();
        for (String userId : userIdList) {
            list.add(new DepartmentUser(deptId, userId));
        }
        return departmentUserDao.insertBatch(list);
    }

    @Override
    public UserDTO findByLdapId(String ldapId) {
        User user = new User();
        user.setLdapId(ldapId);
        List<User> list = userDao.queryList(user);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        User userResult = list.get(0);
        // 查找所有一级部门
        List<Department> deptList = departmentDao.selectByParentId("1001");
        Set<String> deptIds = deptList.stream().map(Department::getUuid)
                .filter(uuid -> org.apache.commons.lang.StringUtils.isNotBlank(uuid)).collect(Collectors.toSet());
        List<Department> deptList1 = list.get(0).getDeptList();
        if (!CollectionUtils.isEmpty(deptList1)) {
            List<Department> userDepts = list.get(0).getDeptList().stream().filter(department -> department.getDeptType() == 1)
                    .collect(Collectors.toList());
            Department userdept;
            if (CollectionUtils.isEmpty(userDepts)) {
                userdept = list.get(0).getDeptList().get(0);
            } else {
                userdept = userDepts.get(0);
            }
            if (deptIds.contains(userdept.getUuid())) {
                userResult.setDeptId(userdept.getUuid());
            } else {
                get1LevelDept(deptIds, userdept, userResult);
            }
        }
        return CollectionUtils.isEmpty(list) ? null : UserTransformer.fromPo(userResult);
    }

    @Override
    public List<UserDTO> getByDefId(String deptId) {
        List<User> listItem = userDao.getByDefId(deptId);
        List<UserDTO> listDTO = UserTransformer.fromPo(listItem);
        return listDTO;
    }

    private void get1LevelDept(Set<String> deptIds, Department department, User userResult) {
        if (deptIds.contains(department.getParentId())) {
            userResult.setDeptId(department.getParentId());
        } else {
            if (!StringUtils.isEmpty(department.getParentId())) {
                Department subDepartment = departmentDao.selectByPrimaryKey(department.getParentId());
                get1LevelDept(deptIds, subDepartment, userResult);
            } else {
                userResult.setDeptId("");
            }
        }
    }

    @Override
    public UserDTO findByCode(String name) {
        User user = new User();
        user.setCode(name);
        List<User> list = userDao.queryList(user);
        return CollectionUtils.isEmpty(list) ? null : UserTransformer.fromPo(list.get(0));
    }

    /**
     * 根据主键删除数据
     *
     * @param userId
     *            主键
     * @return int 删除数据条数
     */
    @Override
    @Transactional
    public int deleteByPrimaryKey(final String userId) {
        if (StringUtils.isEmpty(userId)) {
            LOGGER.error("method[eleteByPrimaryKey] param[userId] is null");
            throw new RuntimeException("param[userId] is null");
        }
        departmentUserDao.deleteByUserId(userId);
        return userDao.deleteByPrimaryKey(userId);
    }

    /**
     * 根据主键更新数据
     *
     * @param userDTO
     *            动作DTO对象
     * @return int 数据条数
     */
    @Override
    @Transactional
    public int updateByPrimaryKey(final UserDTO userDTO) {
        if (null == userDTO) {
            LOGGER.error("method[updateByPrimaryKey] param[userDTO] is null");
            throw new RuntimeException("param[userDTO] is null");
        }
        if (StringUtils.isEmpty(userDTO.getUuid())) {
            LOGGER.warn("method[updateByPrimaryKey] param[userId] is null");
            throw new RuntimeException("param[userId] is null");
        }
        User user = UserTransformer.toPo(userDTO);
        if (!CollectionUtils.isEmpty(userDTO.getDeptIds())) {
            departmentUserDao.deleteByUserId(userDTO.getUuid());
            List<DepartmentUser> listDeptUser = Lists.newArrayList();
            for (String deptId : userDTO.getDeptIds()) {
                DepartmentUser departmentUser = new DepartmentUser(deptId, userDTO.getUuid());
                listDeptUser.add(departmentUser);
            }
            departmentUserDao.insertBatch(listDeptUser);

        }
        return userDao.updateByPrimaryKey(user);
    }

    /**
     * 根据主键查询
     *
     * @param userId
     *            主键
     * @return UserDTO 返回对象
     */
    @Override
    public UserDTO selectByPrimaryKey(final String userId) {
        User user = userDao.selectByPrimaryKey(userId);
        if (StringUtils.isEmpty(userId)) {
            LOGGER.warn("method[selectByPrimaryKey] param[userId] is null");
            return null;
        }
        return UserTransformer.fromPo(user);
    }

    /**
     * 根据dto实体查询列表
     *
     * @param userDTO
     *            动作DTO对象
     * @return List<User> 返回集合
     */
    @Override
    public List<UserDTO> select(final UserDTO userDTO) {
        if (null == userDTO) {
            LOGGER.warn("select Object userDTO is null");
            return Collections.emptyList();
        }
        User user = UserTransformer.toPo(userDTO);

        if (user.getDeptId() != null) {
            String deptId = user.getDeptId();
            Set<String> deptIdSet = Sets.newHashSet();
            deptIdSet.add("'" + deptId + "'");
            List<Department> departments = departmentDao.selectByParentId(deptId);
            if (!CollectionUtils.isEmpty(departments)) {
                getDeptIdSet(deptIdSet, departments);
            }
            // 设置部门id集合
            // params.put("deptIdString", Joiner.on(",").join(deptIdSet));
            user.setDeptIdString(Joiner.on(",").join(deptIdSet));
//            user.setDeptId(null);
        }
        List<User> listUser = null;
        if (userDTO.isExcel()) {
            listUser = userDao.queryListForExcel(user);
        } else {
            listUser = userDao.queryList(user);
        }
        return UserTransformer.fromPo(listUser);
    }

    /**
     * 根据主键数组查询
     *
     * @param userIdArrays
     *            主键数组
     * @return List<UserDTO> 返回集合对象
     */
    @Override
    public List<UserDTO> selectByPrimaryKeyArrays(final String[] userIdArrays) {
        if (ArrayUtils.isEmpty(userIdArrays)) {
            LOGGER.warn("method[selectByPrimaryKeyArrays] param[userIdArrays] is null");
            return Collections.emptyList();
        }
        List<User> listUser = userDao.selectByPrimaryKeyArrays(userIdArrays);
        return UserTransformer.fromPo(listUser);
    }

    /**
     * 获取分页列表数据
     *
     * @param pageRequest
     * @return
     */
    @Override
    public PageResult<UserDTO> pageList(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        Map<String, Object> params = page.getParams();
        if (deepQuery) {
            if (params.get("deptId") != null) {
                String deptId = (String) params.get("deptId");
                Set<String> deptIdSet = Sets.newHashSet();
                deptIdSet.add("'" + deptId + "'");
                List<Department> departments = departmentDao.selectByParentId(deptId);
                if (!CollectionUtils.isEmpty(departments)) {
                    getDeptIdSet(deptIdSet, departments);
                }
                // 设置部门id集合
                params.put("deptIdString", Joiner.on(",").join(deptIdSet));
//            params.remove("deptId");
            }
        }
        int count = userDao.pageListCount(page);
        PageResult<UserDTO> pageResponse = new PageResult<UserDTO>();
        pageResponse.setCount(count);
        int pageCount = (count - 1) / page.getPageSize() + 1;
        pageResponse.setCurPage(page.getPageNo());
        pageResponse.setPageSize(page.getPageSize());
        pageResponse.setPageCount(pageCount);
        if (count > 0) {
            List<User> listItem = userDao.pageList(page);
            List<UserDTO> listDTO = UserTransformer.fromPo(listItem);
            pageResponse.setResult(listDTO);
        }
        return pageResponse;
    }

    private void getDeptIdSet(Set<String> deptIdSet, List<Department> departments) {

        for (Department department : departments) {
            deptIdSet.add("'" + department.getUuid() + "'");
            List<Department> subDepartemnt = departmentDao.selectByParentId(department.getUuid());
            if (!CollectionUtils.isEmpty(subDepartemnt)) {
                getDeptIdSet(deptIdSet, subDepartemnt);
            }
        }
    }

    @Override
    public void batchInsertDepartmentUser(List<DepartmentUser> departmentUserList) {
        if (CollectionUtils.isEmpty(departmentUserList)) {
            return;
        }
        departmentUserList.forEach(e -> departmentUserDao.deleteByUserId(e.getUserId()));
        List<List<DepartmentUser>> partitionList = Lists.partition(departmentUserList, 200);
        partitionList.forEach(e -> departmentUserDao.insertBatch(e));
    }

    @Override
    public List<User> selectByLdapIdAndNamespace(String ldapId, String namespace) {
        return userDao.selectByLdapIdAndNamespace(ldapId, namespace);
    }

	@Override
	public List<UserDTO> getAll() {
		List<User> userList = userDao.getAll();
		List<UserDTO> listDTO = UserTransformer.fromPo(userList);
		return listDTO;
	}

    @Override
    public List<String> findSameDeptUserIdByLdapId(String ldapId) {
        List<String> deptIdList =Lists.newLinkedList();
        if (!StringUtils.isEmpty(ldapId)) {
            User user = new User();
            user.setLdapId(ldapId);
            List<User> list = userDao.queryList(user);
            getSubDept(deptIdList, list.get(0).getDeptList());
        }
        List<User> userDTOS = userDao.getByDefId(Joiner.on(",").join(deptIdList));
        List<String> ldapAccountList = userDTOS.stream().map(User::getLdapId).collect(Collectors.toList());
        return ldapAccountList;
    }
    private void getSubDept(List<String> deptIdList, List<Department> deptList) {
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(deptList)) {
            deptIdList.addAll(deptList.stream().map(Department::getUuid).collect(Collectors.toList()));
            deptList.stream().forEach(item -> {
                List<Department> deptSubList = departmentDao.queryByDeptId(item.getUuid());
                getSubDept(deptIdList, deptSubList);
            });
        }
    }
    

	@Override
	public void addDefaultRoleForUms() {
		userDao.addDefaultRoleForUms();
	}

	@Override
	public void addDefaultUserClassifyAccountForUms() {
		userDao.addDefaultUserClassifyAccountForUms();
	}
}
