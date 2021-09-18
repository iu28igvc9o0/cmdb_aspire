/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.common.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.common.util <br>
* 类名称: ListSortUtilsTest.java <br>
* 类描述: <br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月25日下午3:22:43 <br>
* 版本: v1.0
*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Ignore
public class ListSortUtilsTest {

    private List<User> list = new ArrayList<>();

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.common.util.ListSortUtils#sort(java.util.List, boolean, java.lang.String[])}.
     */
    @Test
    public final void testSortListOfEBooleanStringArray() {
        // 按userId升序、username降序、birthDate升序排序
        String[] sortNameArr = { "userId", "username", "birthDate" };
        boolean[] isAscArr = { true, false, true };
        ListSortUtils.sort(list, sortNameArr, isAscArr);
        System.err.println(
                "按userId升序、username降序、birthDate升序排序(如果userId相同，则按照username降序,如果username相同，则按照birthDate升序)");
        list.forEach(System.err::print);
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.common.util.ListSortUtils#sort(java.util.List, java.lang.String[], boolean[])}.
     */
    @Test
    public final void testSortListOfEStringArrayBooleanArray() {
        // 按userId、username、birthDate都升序排序
        ListSortUtils.sort(list, true, "userId", "username", "birthDate");
        System.err.println(
                "按userId、username、birthDate排序(如果userId相同，则按照username升序,如果username相同，则按照birthDate升序)");
        list.forEach(System.err::print);

        // 按userId、username都倒序排序
        ListSortUtils.sort(list, false, "userId", "username");
        System.err.println("按userId和username倒序(如果userId相同，则按照username倒序)");
        list.forEach(System.err::print);
    }

    @Before
    public void initData() {
        list = Arrays.asList(
                new User(3, "bbb",
                        Date.from(LocalDate.parse("1980-12-01").atStartOfDay().atZone(ZoneId.systemDefault())
                                .toInstant())),
                new User(0, "126",
                        Date.from(LocalDate.parse("1900-10-11").atStartOfDay().atZone(ZoneId.systemDefault())
                                .toInstant())),
                new User(12, "5",
                        Date.from(LocalDate.parse("1973-08-21").atStartOfDay().atZone(ZoneId.systemDefault())
                                .toInstant())),
                new User(465, "1567",
                        Date.from(LocalDate.parse("2012-01-26").atStartOfDay().atZone(ZoneId.systemDefault())
                                .toInstant())),
                new User(2006, "&4m",
                        Date.from(LocalDate.parse("2010-05-08").atStartOfDay().atZone(ZoneId.systemDefault())
                                .toInstant())),
                new User(5487, "hf67",
                        Date.from(LocalDate.parse("2016-12-30").atStartOfDay().atZone(ZoneId.systemDefault())
                                .toInstant())),
                new User(5487, "jigg",
                        Date.from(LocalDate.parse("2000-10-16").atStartOfDay().atZone(ZoneId.systemDefault())
                                .toInstant())),
                new User(5487, "jigg", Date.from(LocalDate.parse("1987-07-25").atStartOfDay()
                        .atZone(ZoneId.systemDefault()).toInstant())));

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class User {
        private Integer userId;
        private String username;
        private Date birthDate;

        @Override
        public String toString() {
            return "UserInfo [userId=" + userId + ", username=" + username + ", birthDate="
                    + birthDate.toInstant() + "]\n";
        }
    }

}
