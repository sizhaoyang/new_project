package com.jaovo.common.test;

// 静态导入 ,导入的是所有的静态方法,这个时候里面的静态方法的调用就可以不加类名前缀
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.jaovo.common.dao.IUserDao;
import com.jaovo.common.model.Pager;
import com.jaovo.common.model.SystemContext;
import com.jaovo.common.model.User;
import com.jaovo.common.testUtils.AbstractDBUnitTestCase;
import com.jaovo.common.testUtils.AssertHelper;

/**
 * 我们当前的测试类,相当于我们的Service层的方法,调用我们的Dao方法
 * 
 * 需要SessionFactory 和userDao引用
 * 
 * @RunWith(SpringJUnit4ClassRunner.class)
 * 
 *                                         spring的测试包,支持事务机制的测试
 * 
 * @ContextConfiguration("./beans.xml")
 * 
 *                                      导入spring配置,如依赖注入
 * @author jaovo
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
public class TestUserDao extends AbstractDBUnitTestCase {

	@Inject
	private SessionFactory sessionFactory;
	@Inject
	private IUserDao userDao;

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

	/**
	 * 测试添加
	 * 
	 * @throws DatabaseUnitException
	 * @throws SQLException
	 */
	@Test
	public void add() throws DatabaseUnitException, SQLException {
		// 1 在测试前获取数据集合
		IDataSet dataSet = createDataSet("t_user");
		// 2
		DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
		User user = new User(1, "测试");
		userDao.add(user);
	}

	/**
	 * 测试删除
	 * 
	 * 
	 * hibernate 中 load和get的区别? 第一 : 执行的时机不同
	 * 如果是get的话,hibernate会直接立即把sql语句发送到数据库,然后执行把结果返回回来
	 * 如果是load的话,.sql语句不会立即执行,是sql语句查询出来的某个代理对象
	 * (代理对象里面只有id),访问代理对象对应的属性的时候,才发送sql语句去执行查询属性,再把结果返回回来
	 * 
	 * 第二 : 如果返回的结果返回为空的话,报两个不同的错误 get 会报空指针异常 load 会报 ObjecNotFoundExctption
	 * 
	 * @throws SQLException
	 * @throws DatabaseUnitException
	 */
	@Test(expected = org.hibernate.ObjectNotFoundException.class)
	public void testDelete() throws DatabaseUnitException, SQLException {
		IDataSet dataSet = createDataSet("t_user");
		DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
		userDao.delete(1);
		User user = userDao.load(1);
		System.out.println(user.getId());
	}

	/**
	 * update
	 * 
	 * @throws SQLException
	 * @throws DatabaseUnitException
	 */
	@Test
	public void testUpdate() throws DatabaseUnitException, SQLException {
		IDataSet dataSet = createDataSet("t_user");
		DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
		User u1 = new User(1, "aaaa");
		User u2 = userDao.load(1);
		u2.setUsername(u1.getUsername());
		userDao.update(u2);
	}

	/**
	 * 测试load
	 * 
	 * @throws SQLException
	 * @throws DatabaseUnitException
	 */
	@Test
	public void testLoad() throws DatabaseUnitException, SQLException {
		IDataSet dataSet = createDataSet("t_user");
		DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
		AssertHelper.asserUser(userDao.load(1));
	}

	private void DBTestBasic(String order) {
		IDataSet dataSet = createDataSet("t_user");
		try {
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
		} catch (DatabaseUnitException | SQLException e) {
			e.printStackTrace();
		}
		SystemContext.setOrder(order);
		SystemContext.setSort("id");
	}

	@Test
	public void testListByArgs() {
		DBTestBasic("desc");
		String hql = "from User where id>? and id<?";
		Object[] args = new Object[] { 1, 4 };
		List<User> actuals = userDao.list(hql, args);
		List<User> excepted = Arrays.asList(new User(2, "admin2"), new User(3,
				"admin3"));
		AssertHelper.assertUsers(excepted, actuals);
	}

	@Test
	public void testListByArgsAndAlias() {
		DBTestBasic("desc");
		String hql = "from User where id>? and id<? and id in(:ids)";
		Object[] args = new Object[] { 1, 5 };
		Map<String, Object> alias = new HashMap<String, Object>();
		alias.put("ids", Arrays.asList(1, 2, 3, 5, 6, 7, 8, 9, 10));
		List<User> actuals = userDao.list(hql, args, alias);
		List<User> excepted = Arrays.asList(new User(2, "admin2"), new User(3,
				"admin3"));
		AssertHelper.assertUsers(excepted, actuals);
	}

	/**
	 * 测试分页
	 */
	@Test
	public void testFindByArgsAandAlias() {
		DBTestBasic("desc");
		SystemContext.removeOrder();
		SystemContext.removeSort();

		SystemContext.setPageOffset(0);
		SystemContext.setPageSize(2);
		String hql = "from User where id>= ? and id<= ? and id in(:ids)";
		Object[] args = new Object[] { 1, 10 };
		Map<String, Object> alias = new HashMap<String, Object>();
		alias.put("ids", Arrays.asList(2, 3, 5, 6, 7, 8, 9, 10));
		Pager<User> actuals = userDao.find(hql, args, alias);

		List<User> excepted = Arrays.asList(new User(2, "admin2"), new User(3,
				"admin3"));
		AssertHelper.assertUsers(excepted, actuals.getDatas());
	}

	/**
	 * 测试SQL
	 */
	@Test
	public void testListBySQLArgsAndAlias() {
		DBTestBasic("desc");
		String sql = "select * from t_user where id> ? and id<? and id in(:ids)";
		Object[] args = new Object[] { 1, 4 };
		Map<String, Object> alias = new HashMap<String, Object>();
		alias.put("ids", Arrays.asList(2, 3, 5, 6, 7, 8, 9, 10));
		List<User> actuals = userDao.listBySQL(sql, args, alias, User.class,
				true);
		List<User> excepted = Arrays.asList(new User(2, "admin2"), new User(3,
				"admin3"));
		AssertHelper.assertUsers(excepted, actuals);
	}

	/**
	 * 测试SQL分页
	 */
	@Test
	public void testFindBySQLArgsAndAlias() {
		DBTestBasic("desc");

		// 准备分行业参数
		SystemContext.removeOrder();
		SystemContext.removeSort();

		SystemContext.setPageSize(3);
		SystemContext.setPageOffset(1);

		String sql = "select * from t_user where id>=? and id <= ? and id in(:ids)";
		Object[] args = new Object[] { 1, 10 };
		// 添加一个id的集合,所查出的对象必须是这个id集合的对象才可以
		Map<String, Object> alias = new HashMap<String, Object>();
		alias.put("ids", Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10));

		// 从数据查出来的
		Pager<User> actuals = userDao.findBySQL(sql, args, alias, User.class,
				true);
		// 我们自己准备的,应该查出来的结果
		List<User> excepted = Arrays.asList(new User(4, "admin4"), new User(5,
				"admin5"), new User(6, "admin6"));

		AssertHelper.assertUsers(excepted, actuals.getDatas());
	}
}
