package com.jaovo.common.testUtils;

import java.util.List;

import static org.junit.Assert.*;

import javax.validation.constraints.AssertTrue;

import org.junit.Assert;

import com.jaovo.common.model.User;

import static java.util.List.*;

/**
 * 断言
 * 
 * @author jaovo
 *
 */
public class AssertHelper {

	// 基准对象
	private static User baseUser = new User(1, "admin1");

	/**
	 * 单个对象和单个对象间的比较
	 */
	public static void assertUser(User excepted, User actual) {
		Assert.assertNotNull("从数据库中取得的数据位空", actual);

		Assert.assertEquals(excepted.getId(), actual.getId());
		Assert.assertEquals(excepted.getUsername(), actual.getUsername());
	}

	/**
	 * 多个对象比较
	 */
	public static void assertUsers(List<User> excepted, List<User> actual) {
		Assert.assertNotNull("从数据库中取得的数据位空", actual);
		Assert.assertEquals(excepted.size(), actual.size());
		assertTrue(excepted.size() == actual.size());
		for (int i = 0; i < excepted.size(); i++) {
			assertUser(excepted.get(i), actual.get(i));
		}
	}

	/**
	 * 单独访问一个user对象 如果只传了数据库对象,那么就和我们的基准对象进行比较
	 */
	public static void asserUser(User actual) {
		assertUser(baseUser, actual);
	}
}
