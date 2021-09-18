/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.common.aspect;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.migu.tsg.microservice.atomicservice.common.annotation.CacheEvict;
import com.migu.tsg.microservice.atomicservice.common.config.RedisProperties;
import com.migu.tsg.microservice.atomicservice.common.helper.RedisCacheHelper;
import com.migu.tsg.microservice.atomicservice.rbac.dao.RoleUsersDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.RoleUsers;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.RolesAssignedRevokeDTO;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.common.aspect <br>
* 类名称: CacheEvictAspect.java <br>
* 类描述: 删除缓存AOP<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月2日上午9:39:59 <br>
* 版本: v1.0
*/
@Aspect
@Component
public class CacheEvictAspect {

    @Autowired
    private RedisCacheHelper redisCacheHelper;

    @Autowired
    private RoleUsersDao roleUsersDao;

    @Autowired
    private RedisProperties redisProperties;

    private Class<CacheEvict> clazz = CacheEvict.class;

    @Pointcut("execution(public * com.migu.tsg.microservice.atomicservice.rbac.biz.RoleBiz.*(..))")
    public void point() {
    }

    @Pointcut("@annotation(com.migu.tsg.microservice.atomicservice.common.annotation.CacheEvict)")
    public void cacheEvict() {
    }

    /**
     * 环绕通知
     * @param joinPoint AOP对象
     * @throws Throwable 异常
     */
    @SuppressWarnings("unchecked")
    @Around("point() && cacheEvict()")
    public Object around(JoinPoint joinPoint) throws Throwable {
        Object object = ((ProceedingJoinPoint) joinPoint).proceed();
        if (!redisProperties.getUsable()) {
            return object;
        }
        Method[] methods = joinPoint.getTarget().getClass().getMethods();
        String methodName = joinPoint.getSignature().getName();
        int index = 0;
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                index = method.getAnnotation(clazz).value();
                break;
            }
        }
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
//            if (methodName.equals("rolesAssigned") || methodName.equals("rolesRevoke")) {
        	if ("rolesAssigned".equals(methodName) || "rolesRevoke".equals(methodName)) {
                List<RolesAssignedRevokeDTO> dtoList = (List<RolesAssignedRevokeDTO>) args[index];
                for (RolesAssignedRevokeDTO dto : dtoList) {
                    deleteCacheBatch(dto.getUser(), dto.getNamespace());
                }
            } else if ("rolesRevokeAll".equals(methodName)) {
                String namespace = args[index].toString();
                List<String> usernameList = (List<String>) args[index + 1];
                for (String username : usernameList) {
                    deleteCacheBatch(username, namespace);
                }
            } else {
                String roleUuid = args[index].toString();
                List<RoleUsers> fetchRoleUsersList = roleUsersDao.fetchRoleUsersList(roleUuid);
                for (RoleUsers roleUsers : fetchRoleUsersList) {
                    deleteCacheBatch(roleUsers.getUsername(), roleUsers.getNamespace());
                }
            }
        }
        return object;
    }

    /**
     * 批量删除指定KEY的缓存数据
     * @param username 用户名称
     * @param namespace 空间名称
     */
    private void deleteCacheBatch(final String username, final String namespace) {
        String split = redisProperties.getRedisKeySplit();
        String key = redisProperties.getRedisKeyPrefixPermission() + split + namespace + split + username;
        Set<String> keys = redisCacheHelper.keys(key);
        if (CollectionUtils.isNotEmpty(keys)) {
            redisCacheHelper.delete(keys);
        }
    }

}