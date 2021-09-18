/**
 * @Title: ListSortUtilsUTTest.java
 * @Package com.migu.tsg.microservice.atomicservice.common.util
 * @Description: 
 * Copyright: Copyright (c) 2018 
 * Company:咪咕文化科技有限公司
 * 
 * @author botao gao
 * @date 2018年2月7日 上午11:31:19
 * @version V1.0
 */

package com.migu.tsg.microservice.atomicservice.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: ListSortUtilsUTTest
 * @Description:
 * @author botao gao
 * @date 2018年2月7日 上午11:31:19
 *
 */
@RunWith(SpringRunner.class)
public class ListSortUtilsUTTest {

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.common.util.ListSortUtils#sort(java.util.List, boolean, java.lang.String[])}.
	 */
	@Test
	public void testSortListOfEBooleanStringArray() {
		// 按userId升序、username降序、birthDate升序排序
		SimpleDateFormat  dateFormate = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		User user1 =null;
		User user2 =null;
		try {
			user1 = new User("a", dateFormate.parse("2018-02-06 12:12:11"), 12);
			user2 = new User("b", dateFormate.parse("2018-02-08 12:12:11"), 14);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		List<User> list = new ArrayList<>();
		list.add(user1);
		list.add(user2);
		String[] sortNameArr = { "birthday" };
		boolean[] isAscArr = { false };
		ListSortUtils.sort(list, sortNameArr, isAscArr);
		list.forEach(System.out::println);

		String[] sortBirthdayArr = { "age" };
		boolean[] isAscArr2 = { true };
		ListSortUtils.sort(list, sortBirthdayArr, isAscArr2);
		list.forEach(System.out::println);
		
		ListSortUtils.sort(list, true, sortNameArr);
		
		list.forEach(System.out::println);

	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	class User {
		public String name;

		public Date birthday;

		public int age;

	}
}
