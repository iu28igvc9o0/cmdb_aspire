package com.migu.tsg.microservice.atomicservice.rbac.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.DepartmentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResult;
import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.rbac.biz.UserBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.DepartmentUser;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.User;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserBatchCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserCreateResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserPageRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserUpdateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserUpdateResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.UserDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.UserVO;
import com.migu.tsg.microservice.atomicservice.rbac.service.UserService;

/**
 * 控制层实现类
 * <p>
 * 项目名称: mirror平台 包: com.migu.tsg.microservice.atomicservice.rbac.controller 类名称: UserController 类描述: 控制层实现类 创建人: 曾祥华 创建时间:
 * 2019-03-07 16:05:29
 */
@RestController
// @CacheConfig(cacheNames = "UserCache")
public class UserController implements UserService {

    /**
     * 系统日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    /**
     * service
     */
    @Autowired
    private UserBiz userBiz;

    /**
     * 创建动作信息
     *
     * @param UserCreateRequest
     *            动作创建请求对象
     * @return UserCreateResponse 动作创建响应对象
     */

    @Override
    public UserCreateResponse createdUser(@RequestBody final UserCreateRequest userCreateRequest) {
        if (null == userCreateRequest) {
            LOGGER.error("created param userCreateRequest is null");
            throw new RuntimeException("userCreateRequest is null");
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userCreateRequest, userDTO);
        String userId = userBiz.insert(userDTO);
        UserCreateResponse userCreateResponse = new UserCreateResponse();
        BeanUtils.copyProperties(userDTO, userCreateResponse);
        userCreateResponse.setUuid(userId);
        return userCreateResponse;
    }

    @Override
    public int batchCreatedUser(@RequestBody UserBatchCreateRequest userBatchCreateRequest) {
        if (null == userBatchCreateRequest || CollectionUtils.isEmpty(userBatchCreateRequest.getListUser())) {
            LOGGER.error("created param userBatchCreateRequest is empty");
            throw new RuntimeException("userBatchCreateRequest is empty");
        }
        List<UserDTO> userList = Lists.newArrayList();
        for (UserCreateRequest userCreateRequest : userBatchCreateRequest.getListUser()) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUuid(userCreateRequest.getUserId());
            BeanUtils.copyProperties(userCreateRequest, userDTO);
            userList.add(userDTO);
        }
        return userBiz.insertBatch(userList);
    }

    @Override
    public int batchModifyDeptIdByUserIdArrays(@RequestParam("dept_id") String deptId, @RequestParam("user_ids") String userIds) {
        if (StringUtils.isEmpty(deptId)) {
            LOGGER.error("batchModifyDeptIdByUserIdArrays deptId is empty");
            throw new RuntimeException("batchModifyDeptIdByUserIdArrays deptId is empty");
        }
        if (StringUtils.isEmpty(userIds)) {
            LOGGER.error("batchModifyDeptIdByUserIdArrays userIds is empty");
            throw new RuntimeException("batchModifyDeptIdByUserIdArrays userIds is empty");
        }
        return userBiz.modifyDeptIdBatchByUserIdArrays(deptId, Arrays.asList(userIds.split(",")));
    }

    @Override
    public List<UserDTO> getByDefId(String deptId) {
        if (StringUtils.isEmpty(deptId)) {
            LOGGER.warn("deptId param templatePageRequest is null");
            return null;
        }
        List<UserDTO> dtoList = userBiz.getByDefId(deptId);
        return dtoList;
    }

    /**
     * 根据主键删除单条动作信息
     *
     * @param userId
     *            主键
     * @@return Result 返回结果
     */
    @Override
    public ResponseEntity<String> deleteByPrimaryKey(@PathVariable("user_id") final String userId) {
        try {
            userBiz.deleteByPrimaryKey(userId);
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOGGER.error("deleteByProjectId error:" + e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据主键修改信息
     *
     * @param UserCreateRequest
     *            动作创建请求对象
     * @return UserCreateResponse 动作创建响应对象
     */
    @Override
    public UserUpdateResponse modifyByPrimaryKey(@PathVariable("user_id") final String userId,
            @RequestBody final UserUpdateRequest userUpdateRequest) {
        UserDTO userdTO = new UserDTO();
        BeanUtils.copyProperties(userUpdateRequest, userdTO);
        userdTO.setUuid(userId);
        userBiz.updateByPrimaryKey(userdTO);
        UserDTO findUserDTO = userBiz.selectByPrimaryKey(userId);
        UserUpdateResponse userUpdateResponse = new UserUpdateResponse();
        if (findUserDTO != null) {
            BeanUtils.copyProperties(findUserDTO, userUpdateResponse);
        }

        return userUpdateResponse;
    }

    /**
     * 根据主键查找动作详情信息
     *
     * @param userId
     *            主键
     * @return UserVO 详情响应对象
     */
    @Override
    public UserVO findByPrimaryKey(@PathVariable("user_id") final String userId) {
        if (StringUtils.isEmpty(userId)) {
            LOGGER.warn("findByPrimaryKey param userId is null");
            return null;
        }
        UserDTO userDTO = userBiz.selectByPrimaryKey(userId);
        UserVO userVO = new UserVO();
        if (null != userDTO) {
            BeanUtils.copyProperties(userDTO, userVO);
        }

        return userVO;
    }

    @Override
    public UserVO findByLdapId(@RequestParam("ldap_id") String ldapId) {
        if (StringUtils.isEmpty(ldapId)) {
            LOGGER.warn("findByLdapId param ldapId is null");
            return null;
        }
        UserVO userVO = new UserVO();
        UserDTO userDTO = userBiz.findByLdapId(ldapId);
        if (null != userDTO) {
            BeanUtils.copyProperties(userDTO, userVO);
        }
        return userVO;
    }

    /**
     * 根据主键查询动作集合信息
     *
     * @param userIds
     *            动作主键
     * @return UserVO 动作查询响应对象
     */
    @Override
    public List<UserVO> listByPrimaryKeyArrays(@PathVariable("user_id") final String userIds) {
        if (StringUtils.isEmpty(userIds)) {
            LOGGER.error("listByPrimaryKeyArrays param auserIds is null");
            return Lists.newArrayList();
        }
        String[] userIdArrays = userIds.split(",");
        List<UserDTO> listUserDTO = userBiz.selectByPrimaryKeyArrays(userIdArrays);
        List<UserVO> listUserVO = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(listUserDTO)) {
            for (UserDTO userDTO : listUserDTO) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(userDTO, userVO);
                listUserVO.add(userVO);
            }
        }
        return listUserVO;
    }

    @Override
    public List<UserDTO> queryList(@RequestBody UserCreateRequest userCreateRequest) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userCreateRequest, userDTO);
        return userBiz.select(userDTO);
    }

    /**
     * 根据查询条件获取分页列表获取分页列表
     * 
     * @param request
     *            分页查询监控对象
     * @return
     */
    @Override
    public PageResult<UserDTO> pageList(@RequestBody UserPageRequest request) {
        if (null == request) {
            LOGGER.warn("pageList param templatePageRequest is null");
            return null;
        }
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageSize(request.getPageSize());
        pageRequest.setPageNo(request.getPageNo());
        // 查询条件，可自己增加PageRequest中参数
        try {
            pageRequest.getDynamicQueryFields().putAll(org.apache.commons.beanutils.BeanUtils.describe(request));
        } catch (Exception e) {
        }
        /**
         * pageRequest.addFields("precinctId", request.getPrecinctId()); pageRequest.addFields("precinctName",
         * request.getPrecinctName()); pageRequest.addFields("precinctKind", request.getPrecinctKind());
         * pageRequest.addFields("lscId", request.getLscId()); pageRequest.addFields("areaCode", request.getAreaCode());
         */
        PageResult<UserDTO> UserDTOPageResponse = userBiz.pageList(pageRequest);
        return UserDTOPageResponse;
    }

    @Override
    public UserVO findByCode(@RequestParam("code") String name) {
        if (StringUtils.isEmpty(name)) {
            LOGGER.warn("findByCode param name is null");
            return null;
        }
        UserDTO userDTO = userBiz.findByCode(name);

        if (null == userDTO) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDTO, userVO);
        return userVO;
    }

    @Override
    public ResponseEntity<String> batchInsertDepartmentUser(@RequestBody UserBatchCreateRequest userBatchCreateRequest) {
        List<DepartmentUser> departmentUserList = Lists.newArrayList();
        try {
            List<UserCreateRequest> list = userBatchCreateRequest.getListUser();
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(e -> {

                    List<User> userList = userBiz.selectByLdapIdAndNamespace(e.getCode(), "alauda");
                    if (org.apache.commons.collections.CollectionUtils.isEmpty(userList)) {
                        LOGGER.warn("未找到用户[{}]对应的记录!!!", e.getCode());
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
            return new ResponseEntity<>("success", HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("新增部门-人员失败.", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@Override
	public List<UserDTO> getAll() {
		return userBiz.getAll();
	}

    @Override
    public List<String> findSameDeptUserIdByLdapId(@PathVariable("ldapId") String ldapId) {
        return userBiz.findSameDeptUserIdByLdapId(ldapId);
    }

}
