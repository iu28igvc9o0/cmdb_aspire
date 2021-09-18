package com.migu.tsg.microservice.atomicservice.rbac.dao.po.transform;

import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.User;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.UserDTO;

/**
 * 对象转换类
 * <p>
 * 项目名称:  mirror平台
 * 包:     com.migu.tsg.microservice.atomicservice.rbac.entity   
 * 类名称:     User
 * 类描述:    对应的PO与DTO的转换类
 * 创建人:     曾祥华
 * 创建时间:     2019-03-07 16:05:29
 */
public final class UserTransformer {
	private UserTransformer() {
    }
    
    /**
     * 将PO实体转换为动作DTO实体
     *
     * @param user 动作PO实体
     * @return UserDTO 动作DTO实体
     */
    public static UserDTO fromPo(final User user) {
        if (null == user) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        
			userDTO.setUuid(user.getUuid());
			userDTO.setName(user.getName());
			userDTO.setUserType(user.getUserType());
			userDTO.setDeptId(user.getDeptId());
			userDTO.setNo(user.getNo());
			userDTO.setSex(user.getSex());
			userDTO.setMail(user.getMail());
			userDTO.setAddress(user.getAddress());
			userDTO.setPhone(user.getPhone());
			userDTO.setMobile(user.getMobile());
			userDTO.setLdapId(user.getLdapId());
			userDTO.setNamespace(user.getNamespace());
			userDTO.setPicture(user.getPicture());
			userDTO.setCode(user.getCode());
			userDTO.setDescr(user.getDescr());
			userDTO.setFax(user.getFax());
			userDTO.setRelationPerson(user.getRelationPerson());
			userDTO.setPost(user.getPost());
			userDTO.setLdapPasswordUpdatetime(user.getLdapPasswordUpdatetime());
			userDTO.setLdapStatus(user.getLdapStatus());
            userDTO.setDeptList(DepartmentTransformer.fromPo(user.getDeptList()));
        return userDTO;
    }

    /**
     * 将业务实体对象集合转换为动作持久化对象集合
     *
     * @param listUser 业务实体对象集合
     * @return List<UserDTO> 持久化对象集合
     */
    public static List<UserDTO> fromPo(final List<User> listUser) {
        if (CollectionUtils.isEmpty(listUser)) {
            return Lists.newArrayList();
        }
        List<UserDTO> listUserDTO = Lists.newArrayList();

        for (User user : listUser) {
            listUserDTO.add(UserTransformer.fromPo(user));
        }
        return listUserDTO;
    }

    /**
     * 将DTO实体转换为动作PO实体
     *
     * @param userDTO DTO实体类
     * @return User PO实体
     */
    public static User toPo(final UserDTO userDTO) {
        if (null == userDTO) {
            return null;
        }

        User user = new User();
        
			user.setUuid(userDTO.getUuid());
			user.setName(userDTO.getName());
			user.setUserType(userDTO.getUserType());
			user.setDeptId(userDTO.getDeptId());
			user.setNo(userDTO.getNo());
			user.setSex(userDTO.getSex());
			user.setMail(userDTO.getMail());
			user.setAddress(userDTO.getAddress());
			user.setPhone(userDTO.getPhone());
			user.setMobile(userDTO.getMobile());
			user.setLdapId(userDTO.getLdapId());
			user.setNamespace(userDTO.getNamespace());
			user.setPicture(userDTO.getPicture());
			user.setCode(userDTO.getCode());
			user.setDescr(userDTO.getDescr());
			user.setFax(userDTO.getFax());
			user.setRelationPerson(userDTO.getRelationPerson());
			user.setPost(userDTO.getPost());
			user.setLdapStatus(userDTO.getLdapStatus());
			user.setLdapPasswordUpdatetime(userDTO.getLdapPasswordUpdatetime());
            user.setDeptIds(userDTO.getDeptIds());
        return user;
    }

    /**
     * 将业务实体对象集合转换为动作持久化对象集合
     *
     * @param listUserDTO 业务实体对象集合
     * @return List<User> 持久化对象集合
     */
    public static List<User> toPo(final List<UserDTO> listUserDTO) {
        if (CollectionUtils.isEmpty(listUserDTO)) {
            return Lists.newArrayList();
        }
        List<User> listUser = Lists.newArrayList();

        for (UserDTO userdTO : listUserDTO) {
            listUser.add(UserTransformer.toPo(userdTO));
        }
        return listUser;
    }

    /**
     * 将业务实体对象集合转换为Map
     *
     * @param listUserDTO 业务实体对象集合
     * @return Map<String,UserDTO> Map[key=String|value=UserDTO]
     */
    public static Map<String, UserDTO> toDTOMap(final List<UserDTO> listUserDTO) {
        if (CollectionUtils.isEmpty(listUserDTO)) {
            return Maps.newHashMap();
        }
        Map<String, UserDTO> userDTOMaps = Maps.newHashMap();

        for (UserDTO userDTO : listUserDTO) {
            userDTOMaps.put(userDTO.getUuid(), userDTO);
        }
        return userDTOMaps;
    }

    /**
     * 将业务实体对象集合中的主键进行提取并封装成数组
     *
     * @param listUserDTO 业务实体对象集合
     * @return String[] 主键数组
     */
    public static String[] toDTOPrimaryKeyArrays(final List<UserDTO> listUserDTO) {
        if (CollectionUtils.isEmpty(listUserDTO)) {
            return null;
        }
        int size = listUserDTO.size();
        String[] userIdArrays = new String[size];
        for (int i = 0; i < size; i++) {
            userIdArrays[i] = listUserDTO.get(i).getUuid();
        }
        return userIdArrays;
    }

}
