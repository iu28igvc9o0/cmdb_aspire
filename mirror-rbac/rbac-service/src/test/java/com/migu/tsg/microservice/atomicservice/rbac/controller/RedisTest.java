/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.rbac.controller;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.migu.tsg.microservice.atomicservice.rbac.biz.AuthBiz;
import com.migu.tsg.microservice.atomicservice.rbac.cache.CacheBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.ResourceSchemaDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.Permission;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchema;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchemaActions;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.controller <br>
 * 类名称: RedisTest.java <br>
 * 类描述: http://redisdoc.com/;http://doc.redisfans.com/<br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年9月16日下午2:58:25 <br>
 * 版本: v1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Ignore
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private AuthBiz authBiz;

    @Autowired
    private ResourceSchemaDao resourceSchemaDao;

    @Autowired
    private CacheBiz cacheBiz;

    @Test
    public void test2() throws Exception {
        //        long start = System.currentTimeMillis();
        //        resourceSchemaDao.fetchResourceSchemaList();
        //        long end = System.currentTimeMillis();
        //
        //        System.err.println("DB:" + (end - start));
        for (int i = 0; i < 5; i++) {

            long start = System.currentTimeMillis();
            redisTemplate.opsForValue().get("rbac-service-resource-schema3");
            long end = System.currentTimeMillis();

            System.err.println("Redis-String:" + (end - start));

            start = System.currentTimeMillis();
            redisTemplate.opsForList().range("rbac-service-resource-schema4", 0, -1);
            end = System.currentTimeMillis();

            System.err.println("Redis-List:" + (end - start));

            start = System.currentTimeMillis();
            redisTemplate.opsForHash().values("rbac-service-resource-schema7");
            end = System.currentTimeMillis();
            System.err.println("Redis-Hash:" + (end - start));
        }
    }

    @Test
    public void test() throws Exception {
        //        stringRedisTemplate.opsForValue().set("aaa", "111");
        //        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));

        List<ResourceSchema> fetchResourceSchemaList = resourceSchemaDao.fetchResourceSchemaList();
        System.err.println(fetchResourceSchemaList);
        for (ResourceSchema resourceSchema : fetchResourceSchemaList) {
            //            redisTemplate.opsForList().rightPush("rbac-service-resource-schema7", resourceSchema);
            redisTemplate.opsForHash().put("rbac-service-resource-schema7", resourceSchema.getResource(),
                    resourceSchema);
        }
        //        redisTemplate.opsForValue().set("rbac-service-resource-schema3", fetchResourceSchemaList);
    }

    @Test
    public void test4() throws Exception {
        List<Permission> permissionList = cacheBiz.listOfPermissionForParentRoleUuid("ws1007", "migu");
        System.err.println(permissionList);
        for (Permission permission : permissionList) {
            redisTemplate.opsForHash().put("rbac-service-permission~ws1007~migu", permission.getUuid(),
                    permission);
        }
    }

    //@Test
    public void testObj() throws Exception {
        ResourceSchemaActions action = new ResourceSchemaActions("role", "role:updated", null, null);
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set("action", action); // 写入缓存
        ResourceSchemaActions resourceSchemaActions = (ResourceSchemaActions) operations.get("action");// 读取缓存数据
        System.err.println("resourceSchemaActions:" + resourceSchemaActions);
        // operations.set("com.neo.f", action, 1, TimeUnit.SECONDS);
        // Thread.sleep(1000);
        // redisTemplate.delete("com.neo.f");
        // boolean exists = redisTemplate.hasKey("com.neo.f");
        // if (exists) {
        // System.out.println("exists is true");
        // } else {
        // System.out.println("exists is false");
        // }
        // Assert.assertEquals("aa", operations.get("com.neo.f").getUserName());
    }

    //@Test
    public void test5() {
        //        List<Permission> listOfPermissionForParentRoleUuid = authBiz.listOfPermissionForParentRoleUuid("user",
        //                "alaudademo07");
        //        System.err.println("listOfPermissionForParentRoleUuid:" + listOfPermissionForParentRoleUuid);
    }

}
