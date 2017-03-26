package com.jaovo.cms.testUtils;

import java.util.List;

import static org.junit.Assert.*;

import javax.validation.constraints.AssertTrue;

import org.junit.Assert;

import com.jaovo.cms.model.Group;
import com.jaovo.cms.model.Role;
import com.jaovo.cms.model.User;

import static java.util.List.*;

/**
 * 断言
 * 
 * @author jaovo
 *
 */
public class AssertHelper {

	// 基准对象
	private static User baseUser = new User(1, "admin1", "123", "admin1",
			"admin1@admin.com", "110", 1);

	public static User getBaseUser() {
		return baseUser;
	}

	/**
	 * 单个对象和单个对象间的比较
	 */
	public static void assertUser(User excepted, User actual) {
		Assert.assertNotNull("从数据库中取得的数据位空", actual);

		Assert.assertEquals(excepted.getId(), actual.getId());
		Assert.assertEquals(excepted.getUsername(), actual.getUsername());

		Assert.assertEquals(excepted.getPassword(), actual.getPassword());
		Assert.assertEquals(excepted.getNickname(), actual.getNickname());
		Assert.assertEquals(excepted.getEmail(), actual.getEmail());
		Assert.assertEquals(excepted.getPhone(), actual.getPhone());
		Assert.assertEquals(excepted.getStatus(), actual.getStatus());
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

	// 2 对比Role
	public static void assertRole(Role excepted, Role actual) {
		Assert.assertNotNull("没有从数据取回数据", actual);
		Assert.assertEquals(excepted.getId(), actual.getId());
		Assert.assertEquals(excepted.getName(), actual.getName());
		Assert.assertEquals(excepted.getRoleType(), actual.getRoleType());

	}

	public static void assertRoles(List<Role> excepted, List<Role> actuals) {
		Assert.assertNotNull("没有从数据取回数据", actuals);
		for (int i = 0; i < excepted.size(); i++) {
			Role role1 = excepted.get(i);
			Role role2 = actuals.get(i);
			assertRole(role1, role2);
		}
	}

	// 2 对比Group
	public static void assertGroup(Group excepted, Group actual) {
		Assert.assertNotNull("没有从数据取回数据", actual);
		Assert.assertEquals(excepted.getId(), actual.getId());
		Assert.assertEquals(excepted.getName(), actual.getName());
	}

	public static void assertGroups(List<Group> excepted, List<Group> actuals) {
		Assert.assertNotNull("没有从数据取回数据", actuals);
		for (int i = 0; i < excepted.size(); i++) {
			Group Group1 = excepted.get(i);
			Group Group2 = actuals.get(i);
			assertGroup(Group1, Group2);
		}
	}

	public static void assertObjects(List<?> excepted, List<?> actuals) {
		for (int i = 0; i < excepted.size(); i++) {
			Object object1 = excepted.get(i);
			Object object2 = actuals.get(i);
			Assert.assertEquals(object1, object2);//
		}

	}
}
