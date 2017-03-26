package com.jaovo.cms.dao;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.jaovo.cms.model.Group;
import com.jaovo.cms.model.Role;
import com.jaovo.cms.model.RoleType;
import com.jaovo.cms.model.User;
import com.jaovo.cms.model.UserGroup;
import com.jaovo.cms.model.UserRole;
import com.jaovo.cms.testUtils.AbstractDBUnitTestCase;
import com.jaovo.cms.testUtils.AssertHelper;
import com.jaovo.common.model.Pager;
import com.jaovo.common.model.SystemContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
public class TestUserDao extends AbstractDBUnitTestCase {
	@Inject
	private SessionFactory sessionFactory;
	@Inject
	private IUserDao userDao;
	@Inject
	private IRoleDao roleDao;
	@Inject
	private IGroupDao groupDao;

	// 为了Junit3兼容,
	@Before
	public void setUp() {
		Session session = sessionFactory.openSession();
		/**
		 * 测试事务机制,需要和事务机制进行绑定,事务机制同步管理器,,就是事务机制管理器
		 * 事务机制管理器通过sessionFactory持有了session的引用,依次通过session来进事务机制控制 事务机制 也就是两个事
		 * : 要么提交,要么回滚.全部成功就提交,有一个失败就回滚
		 * 
		 * 所以我们在这里把SessionFactory持有所有的session给我们的事务机制管理,这样事务机制提高了一层,到了当前层
		 * 当前层相当于我们的service层
		 */
		/**
		 * 为什么要把事务机制提高Service层?
		 * 
		 * DAO : Data Access Object 数据库连接对象
		 * 
		 * 这个时候,这个里面的方法,对应一个sql语句
		 * 
		 * 一个方法,一个sql语句,一个事务机制,是一一对应的,
		 * 
		 * 但是 事务机制的意义 : 是多个方法语句执行的集合
		 * 
		 * 而DAO的方法又是在Service调用的,而一个Service中可能调用多个DAO方法,可以说是DAO方法的集合,
		 * 那么这些方法是不是应该同时成功或者是同时回滚,所以,应该吧我们的事务机制提升到Service层
		 * TransactionSynchronizationManager 事务同步管理
		 */
		TransactionSynchronizationManager.bindResource(sessionFactory,
				new SessionHolder(session));

		this.backupAllTable();

	}

	@After
	public void tearDown() {
		/**
		 * 事务机制真正执行的,提交或者回滚都靠这里实现和完成
		 */
		SessionHolder holder = (SessionHolder) TransactionSynchronizationManager
				.getResource(sessionFactory);

		Session session = holder.getSession();
		session.flush();
		// 上面是提交或者是回滚
		// 执行完成以后,别管是什么结果,都需要把事务机制解绑
		TransactionSynchronizationManager.unbindResource(sessionFactory);
		// this.resumeDataTable();
	}

	@Test
	public void testAdd() throws DatabaseUnitException, SQLException {
		PrepareDataBase();
		User user = new User(1, "admin1", "123", "admin1", "admin@admin.com",
				"110", 1);
		userDao.add(user);
	}

	/**
	 * 准备数据库测试数据
	 */
	private void PrepareDataBase() {
		// 1 在测试前获取数据集合
		IDataSet dataSet = createDataSet("t_user");
		// 2
		try {
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
		} catch (DatabaseUnitException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据用户Id 查询 出 该用户的所有角色信息
	 */
	@Test
	public void testListUserRoles() {
		PrepareDataBase();
		List<Role> excepted = Arrays.asList(new Role(2, "文章发布人员",
				RoleType.ROLE_PUBLISH), new Role(3, "文章审核人员",
				RoleType.ROLE_AUDIT));
		List<Role> roles = userDao.listUserRoles(2);
		AssertHelper.assertRoles(excepted, roles);
	}

	/**
	 * 根据用户Id 查询 出 该用户的所有角色的 Id
	 */
	@Test
	public void testListUserRoleIds() {
		PrepareDataBase();
		List<Integer> excepted = Arrays.asList(2, 3);
		List<Integer> roles = userDao.listUserRoleIds(2);
		AssertHelper.assertObjects(excepted, roles);
	}

	/**
	 * 根据用户ID,查询出该用户的所有组信息
	 */
	@Test
	public void testListUserGroups() {
		PrepareDataBase();
		List<Group> excepted = Arrays.asList(new Group(1, "教学部"), new Group(3,
				"招生部"));
		List<Group> groups = userDao.listUserGroups(3);
		AssertHelper.assertGroups(excepted, groups);
	}

	/**
	 * 根据用户ID,查询出该用户的所有组的Id
	 */
	@Test
	public void testListUserGroupIds() {

		// 1 数据库准备测试数据
		PrepareDataBase();

		List<Integer> actuals = Arrays.asList(1, 3);
		List<Integer> excepted = userDao.listUserGroupIds(3);
		AssertHelper.assertObjects(excepted, actuals);
	}

	/**
	 * 根据用户Id和组Id获取他们的关联的中间对象
	 */
	@Test
	public void testLoadUserGroup() {
		// 1 数据库准备测试数据
		PrepareDataBase();

		int uid = 2;
		int gid = 1;
		UserGroup ug = userDao.loadUserGroup(uid, gid);
		User au = new User(2, "admin2", "123", "admin2", "admin2@admin.com",
				"110", 1);
		Group ag = new Group(1, "教学部");
		// System.out.println(ug.getId()+"---"+ug.getUser().getId()+"---"+ug.getGroup().getId());
		AssertHelper.assertUser(ug.getUser(), au);
		AssertHelper.assertGroup(ug.getGroup(), ag);
	}

	/**
	 * 根据用户名查询某个用户对象
	 */
	@Test
	public void testLoadUserName() {

		// 1 数据库准备测试数据
		PrepareDataBase();

		User au = AssertHelper.getBaseUser();
		String username = "admin1";
		User eu = userDao.loadByUsername(username);
		AssertHelper.assertUser(eu, au);
	}

	/**
	 * 根据角色id查询该角色下所有的用户对象
	 */
	@Test
	public void testListRoleUsers() {

		// 1 数据库准备测试数据
		PrepareDataBase();

		int rid = 2;
		List<User> aus = Arrays.asList(new User(2, "admin2", "123", "admin2",
				"admin2@admin.com", "110", 1), new User(3, "admin3", "123",
				"admin3", "admin3@admin.com", "110", 1));
		List<User> eus = userDao.listRoleUsers(rid);
		AssertHelper.assertUsers(eus, aus);
	}

	/**
	 * 根据角色类型获取所有的用户对象
	 */
	@Test
	public void testListRoleUsersByRoleType() {

		// 1 数据库准备测试数据
		PrepareDataBase();

		List<User> aus = Arrays.asList(new User(2, "admin2", "123", "admin2",
				"admin2@admin.com", "110", 1), new User(3, "admin3", "123",
				"admin3", "admin3@admin.com", "110", 1));
		List<User> eus = userDao.listRoleUsers(RoleType.ROLE_PUBLISH);
		AssertHelper.assertUsers(eus, aus);
	}

	/**
	 * 根据组id,获取组中的所有用户对象
	 */
	@Test
	public void testListGroupUsers() {
		// 1 数据库准备测试数据
		PrepareDataBase();
		List<User> aus = Arrays.asList(new User(2, "admin2", "123", "admin2",
				"admin2@admin.com", "110", 1), new User(3, "admin3", "123",
				"admin3", "admin3@admin.com", "110", 1));
		List<User> eus = userDao.listGroupUsers(1);
		AssertHelper.assertUsers(eus, aus);
	}

	/**
	 * 给某个用户添加组信息
	 */
	@Test
	public void testAddUserGroup() {
		PrepareDataBase();
		int uid = 1;
		int gid = 1;
		Group group = groupDao.load(gid);
		User user = userDao.load(uid);
		userDao.addUserGroup(user, group);
		UserGroup ur = userDao.loadUserGroup(uid, gid);
		assertNotNull(ur);
		assertEquals(ur.getGroup().getId(), gid);
		assertEquals(ur.getUser().getId(), uid);
	}

	/**
	 * 添加用户角色
	 */
	@Test
	public void testAddUserRole() {
		PrepareDataBase();
		int uid = 1;
		int rid = 3;
		User user = userDao.load(uid);
		Role role = roleDao.load(rid);
		userDao.addUserRole(user, role);
		UserRole ur = userDao.loadUserRole(uid, rid);
		assertNotNull(ur);
		assertEquals(ur.getRole().getId(), rid);
		assertEquals(ur.getUser().getId(), uid);
	}

	/**
	 * 根据用户id 清空该用户的角色信息
	 */
	@Test
	public void testDeleteUserRoles() {
		PrepareDataBase();
		int uid = 2;
		userDao.deleteUserRoles(uid);
		List<Role> urs = userDao.listUserRoles(uid);
		assertTrue(urs.size() <= 0);
	}

	/**
	 * 根据用户id清空该用户的组信息
	 */
	@Test
	public void testDeleteUserGroups() {
		PrepareDataBase();
		int uid = 2;
		userDao.deleteUserGroups(uid);
		List<Group> ugs = userDao.listUserGroups(uid);
		assertTrue(ugs.size() <= 0);
	}

	/**
	 * 删除当前用户的角色对象.删除用户和角色之间的某个映射
	 */
	@Test
	public void testDeleteUserRole() {
		PrepareDataBase();
		int uid = 1;
		int rid = 1;
		userDao.deleteUserRole(uid, rid);
		assertNull(userDao.loadUserRole(uid, rid));
	}

	/**
	 * 删除当前用户的组对象.删除用户和组之间的某个映射
	 */
	@Test
	public void testDeleteUserGroup() {
		PrepareDataBase();
		int uid = 2;
		int gid = 1;
		userDao.deleteUserGroup(uid, gid);
		assertNull(userDao.loadUserGroup(uid, gid));
	}

	/**
	 * 查询所有用户并分页
	 */
	@Test
	public void testFindUser() {
		PrepareDataBase();
		SystemContext.setPageOffset(0);
		SystemContext.setPageSize(15);

		List<User> excepted = Arrays.asList(new User(1, "admin1", "123",
				"admin1", "admin1@admin.com", "110", 1), new User(2, "admin2",
				"123", "admin2", "admin2@admin.com", "110", 1), new User(3,
				"admin3", "123", "admin3", "admin3@admin.com", "110", 1));
		Pager<User> actual = userDao.findUser();
		assertNotNull(actual);
		assertEquals(actual.getTotalReaord(), excepted.size());
		AssertHelper.assertUsers(excepted, actual.getDatas());
	}
}
