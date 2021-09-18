/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.rbac.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.migu.tsg.microservice.atomicservice.rbac.dao.RoleParentDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.RoleParent;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.controller <br>
 * 类名称: RecursionTest.java <br>
 * 类描述: 递归测试类<br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年9月15日下午8:59:36 <br>
 * 版本: v1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Ignore
public class RecursionTest {

    @Autowired
    private RoleParentDao roleParentsDao;
    
    
    @Test
    public void test3() {
        System.err.println("recursionRoleChildrenSum:" + recursionRoleChildrenSum("88eb7d5c-2211-4776-9918-925a0fabfc41"));
    }

    /**
     * 递归统计角色被继承最多层数
     * 
     * @param roleUuid
     *            角色UUID
     * 
     * @return 层级数
     */
    private int recursionRoleChildrenSum(final String roleUuid) {
        List<RoleParent> fetchRoleChildrenList = roleParentsDao.fetchRoleChildrenList(roleUuid);
        System.err.println(fetchRoleChildrenList);
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
    
    

    @Test
    public void test2() {
        System.err.println("recursionRoleParentsSum:" + recursionRoleParentsSum("88eb7d5c-2211-4776-9918-925a0fabfc41"));
    }

    /**
     * 递归统计父角色继承最多层数
     * 
     * @param parentRoleUuid
     *            父角色UUID
     * 
     * @return 层级数
     */
    private int recursionRoleParentsSum(final String parentRoleUuid) {
        List<RoleParent> fetchRoleParentList = roleParentsDao.fetchRoleParentList(parentRoleUuid);
        System.err.println(fetchRoleParentList);
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

    @Test
    public void test1() {
        List<String> parentRoleList = Arrays.asList("88eb7d5c-2211-4776-9918-925a0fabfc42",
                "88eb7d5c-2211-4776-9918-925a0fabfc43", "626140d9-2e45-4da0-a204-97c54dcc7cd7");
        parentRoleList.forEach(parentRoleUuid -> {
            System.err.println("hasRecursionRoleParent:"
                    + hasRecursionRoleParent(parentRoleUuid, "88eb7d5c-2211-4776-9918-925a0fabfc41"));
        });
    }

    /**
     * 递归判断当前角色UUID是否被父角色继承过
     * 
     * @param parentRoleUuid
     *            父角色UUID
     * @param roleUuid
     *            角色uuid
     * 
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

}
