package com.jaovo.common.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.expr.NewArray;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.sql.Select;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;

import com.jaovo.common.model.Pager;
import com.jaovo.common.model.SystemContext;

public class BaseDao<T> implements IBaseDao<T> {

	// @Resource
	// @Autowired
	@Inject
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 通过泛型,处理我们的Class对象,泛型是传入的时候才用的,但是我们这里需要先把这些class相关程序先处理
	 * 
	 * 这里实际包含两个对象 : 一个是我们的逻辑类泛型,一个是被处理的实体类的泛型
	 * 
	 * 如果我有一个User 那是不是会有一个UserDao来继承BaseDao,并把User传过来
	 * 
	 * 所以 实体类需要泛型,逻辑来需要泛型
	 * 
	 * 因为我们这个是公共模块,根本不知道继承我们BaseDao是那个XXXDao类是谁,是不确定的
	 * 
	 * 所以 我们就不知道,这个公共的模块生成了具体的业务XXDao以后,要处理的那个对象是谁 XXXModel.java
	 * 
	 * 通过这里需要获得两个泛型,逻辑类对象,可以this
	 * 
	 * 怎么获得继承的那个子类的泛型? 就是实体类.
	 */
	private Class<T> clazz;

	public Class<T> getClazz() {

		// 运行时类 一次就需要一个,所以就是弄了一个单例模式
		// 首先 类 有几种类型 : 数据和逻辑 这的T 是数据类 也就是实体类
		if (clazz == null) {
			// 获得当前逻辑类的泛型,就是谁继承的那个类,这个就获得了正在运行的对象的运行时类泛型,
			// java.lang.reflect.Type
			// this 是子类对象的引用,因为我这个类就是被继承的,这个方法只会在子类被调用
			// getClass() 获得子类的运行时类
			// getGenericSuperclass() 获得当前类(当前类是子类)得到泛型之后的父类
			Type logicType = this.getClass().getGenericSuperclass();
			Type dataType = ((ParameterizedType) logicType)
					.getActualTypeArguments()[0];
			// ParameterizedType 当前类处理对象/参数的泛型,他代表的是BaseDao<T>继承类对象的那个T
			// getActualTypeArguments 获取参数泛型的类型, 比如m1(T t) 获取的就是T的类型,返回的是个数组
			clazz = (Class<T>) dataType;
			// 我们需要把dataType转型为具体的运行的时候的那个运行时类 T.class ---> user,User.class
		}
		return clazz;
	}

	@Override
	public void add(T t) {
		getSession().save(t);

	}

	@Override
	public void delete(int id) {
		getSession().delete(this.load(id));
	}

	@Override
	public void update(T t) {
		getSession().update(t);
	}

	@Override
	public T load(int id) {

		return (T) getSession().load(getClazz(), id);
	}

	// -------------------------------------
	@Override
	public List<T> list(String hql) {

		return this.list(hql, null, null);
	}

	@Override
	public List<T> list(String hql, Object args) {
		return this.list(hql, null, new Object[] { args });
	}

	@Override
	public List<T> list(String hql, Object[] args) {
		return this.list(hql, null, args);
	}

	@Override
	public List<T> list(String hql, Map<String, Object> alias) {
		return this.list(hql, alias, null);
	}

	// 别名 设置的是数据
	// 通配符统计的那个列名
	@Override
	public List<T> list(String hql, Map<String, Object> alias, Object[] args) {
		// 1 初始判断HQL语句除了基础查询以外,有没有额外的需求
		initSort(hql);
		// 2 HQL语句的查询流程
		Query query = this.getSession().createQuery(hql);
		// 2.2 添加参数有三种,通配符,别名,List
		// 通配符List集合 : setParameterList(name,valuesList);
		// 别名参数 : setParameter(name,value);
		// 占位符 : setParameter(position,val); 位置从 0 开始

		// 别名
		setAliasParameter(alias, query);
		// 通配符设置
		setParameter(args, query);
		// 3 执行查询
		return query.list();
	}

	/**
	 * 自定义设置通配符方法
	 * 
	 * @param args
	 * @param query
	 */
	// from User where age=19,sex='男' order by age , group by sex , having ?;
	private void setParameter(Object[] args, Query query) {
		if (args != null && args.length > 0) {
			int index = 0;
			for (Object arg : args) {
				query.setParameter(index++, arg);
			}
		}
	}

	/**
	 * 自定义设置别名方法
	 * 
	 * @param alias
	 * @param query
	 */

	private void setAliasParameter(Map<String, Object> alias, Query query) {
		if (alias != null) {
			Set<String> keys = alias.keySet();
			for (String key : keys) {
				Object value = alias.get(key);
				if (value instanceof Collection) {
					// 因为有的时候age的值,是某一个集合中的一个的时候,就怎么怎么样
					// 所以我在这里判断value 如果是集合,就说明肯定是Collection的子集,
					// 就用setParameterList() 这个方法设置
					// 什么情况下用query.setParameterList?
					// 子查询的时候用 : age in :age ---> age in (1,2,3,4,5)
					query.setParameterList(key, (Collection) value);
				} else {
					// age =:age
					query.setParameter(key, value);
				}
			}
		}
	}

	// private void initSort(String hql) {
	// // 一般TradLocal 中 一般会贯穿这个程序的执行流程
	// // 一般数据 起源是在页面中,会根据用户的行为而不同
	// // 然后把数据传入到服务端,一般情况在filter中,filter会在Servlet之前执行,然后获取客户端的数据,装入ThreadLoad
	// // 这样 在整个程序执行的生命周期中,都能访问到,
	// String order = SystemContext.getOrder();// 排序方式 : asc,desc 对应页面中的点击,
	// String sort = SystemContext.getSort();// 排序字段 : age,price,列表页面上面的一排点击按钮
	// // trim() 去除空格
	// if (sort != null && !"".equals(sort.trim())) {
	// hql += " order by " + sort;
	// if ("asc".equals(order.trim())) {
	// hql += " asc";
	// } else {
	// hql += " desc";
	// }
	// }
	// }

	/**
	 * 有返回值的HQL调用
	 * 
	 * @param hql
	 * @return
	 */
	private String initSort(String hql) {
		// 一般TradLocal 中 一般会贯穿这个程序的执行流程
		// 一般数据 起源是在页面中,会根据用户的行为而不同
		// 然后把数据传入到服务端,一般情况在filter中,filter会在Servlet之前执行,然后获取客户端的数据,装入ThreadLoad
		// 这样 在整个程序执行的生命周期中,都能访问到,
		String order = SystemContext.getOrder();// 排序方式 : asc,desc 对应页面中的点击,
		String sort = SystemContext.getSort();// 排序字段 : age,price,列表页面上面的一排点击按钮
		if (sort != null && !"".equals(sort.trim())) {
			hql += " order by " + sort;
			if ("asc".equals(order.trim())) {
				hql += " asc";
			} else {
				hql += " desc";
			}
		}
		return hql;
	}

	// ----------------------------------
	@Override
	public Pager<T> find(String hql) {
		return this.find(hql, null, null);
	}

	@Override
	public Pager<T> find(String hql, Object args) {
		return this.find(hql, new Object[] { args }, null);
	}

	@Override
	public Pager<T> find(String hql, Object[] args) {
		return this.find(hql, args, null);
	}

	@Override
	public Pager<T> find(String hql, Map<String, Object> alias) {

		return this.find(hql, null, alias);
	}

	@Override
	public Pager<T> find(String hql, Object[] args, Map<String, Object> alias) {
		Pager<T> pager = new Pager<T>();
		// 1 hql 初始化
		initSort(hql);
		// 查询总记录的HQL语句拼接
		String countHql = getCountQl(hql, true);
		// 查询总记录数
		Query countQuery = getSession().createQuery(countHql);
		setAliasParameter(alias, countQuery);
		setParameter(args, countQuery);
		// uniqueResult 获得唯一的结果
		long totalRecord = (long) countQuery.uniqueResult();

		pager.setTotalReaord(totalRecord);

		// 查询具体在页面中展示的数据
		Query query = getSession().createQuery(hql);
		setAliasParameter(alias, query);
		setParameter(args, query);
		// 3 设置查询数据的起始值,结束值(起始值+pageSize)
		// 把这些参数设置到Pager对象中
		pager = setPagerParameter(pager);
		// 上面只是把分页参数放到pager对象中,
		// 和Query对象没有关系,还要把pager对象中的数据,取出来放到Query对象中
		// 第一个参数是起始值,第二个参数是取得条数,就是每页个数 limit
		query.setFirstResult(pager.getPageOffset()).setMaxResults(
				pager.getPageSize());
		// 上面是准备数据
		List<T> datas = query.list();
		pager.setDatas(datas);
		return pager;
	}

	private Pager setPagerParameter(Pager pager) {
		Integer pageSize = SystemContext.getPageSize();
		Integer pageOffSet = SystemContext.getPageOffset();
		if (pageOffSet == null || pageOffSet <= 0) {
			pageOffSet = 0;
		}
		if (pageSize == null || pageSize <= 0) {
			pageSize = 15;
		}
		pager.setPageOffset(pageOffSet);
		pager.setPageSize(pageSize);
		return pager;
	}

	private String getCountQl(String hql, boolean isHQL) {
		// 就能获取表名
		String table = hql.substring(hql.indexOf("from"));
		// select xxx from 表名
		String countQl = "select count(*) " + table;
		if (isHQL) {
			countQl.replaceAll(" fetch", " ");
		}
		return countQl;
	}

	// ----------------
	@Override
	public Object loadObject(String hql) {
		return loadObject(hql, null, null);
	}

	@Override
	public Object loadObject(String hql, Object args) {
		return loadObject(hql, new Object[] { args }, null);
	}

	@Override
	public Object loadObject(String hql, Object[] args) {
		return loadObject(hql, args, null);
	}

	@Override
	public Object loadObject(String hql, Map<String, Object> alias) {
		return loadObject(hql, null, alias);
	}

	@Override
	public Object loadObject(String hql, Object[] args,
			Map<String, Object> alias) {
		Query query = getSession().createQuery(hql);
		setAliasParameter(alias, query);
		setParameter(args, query);
		return query.uniqueResult();
	}

	// ----------------
	@Override
	public void updateByHql(String hql) {
		updateByHql(hql, null);
	}

	@Override
	public void updateByHql(String hql, Object args) {
		updateByHql(hql, new Object[] { args });
	}

	@Override
	public void updateByHql(String hql, Object[] args) {
		Query query = getSession().createQuery(hql);
		setParameter(args, query);
		query.executeUpdate();

	}

	// -----------
	@Override
	public <N> List<N> listBySQL(String sql, Class<?> claz, boolean hasEntity) {
		return listBySQL(sql, null, null, claz, hasEntity);
	}

	@Override
	public <N> List<N> listBySQL(String sql, Object arg, Class<?> claz,
			boolean hasEntity) {
		return listBySQL(sql, new Object[] { arg }, null, claz, hasEntity);
	}

	@Override
	public <N> List<N> listBySQL(String sql, Object[] args, Class<?> claz,
			boolean hasEntity) {
		return listBySQL(sql, args, null, claz, hasEntity);
	}

	@Override
	public <N> List<N> listBySQL(String sql, Map<String, Object> alias,
			Class<?> claz, boolean hasEntity) {
		return listBySQL(sql, null, alias, claz, hasEntity);
	}

	@Override
	public <N> List<N> listBySQL(String sql, Object[] args,
			Map<String, Object> alias, Class<?> claz, boolean hasEntity) {
		// 1 初始化SQL语句
		initSort(sql);
		// 准备查询语句 , hibernate用SQLQuery来进行原生的SQL语句的查询
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		setAliasParameter(alias, sqlQuery);
		setParameter(args, sqlQuery);
		if (hasEntity) {
			sqlQuery.addEntity(claz);
		} else {
			/*
			 * 如果 没有传入Class实体,实体概念 : 包括实体类和实体类对应的Entity
			 * 有时候我们查询的数据,不是和我们实体类一一对应的表,多表联查,返回的就是一个VO值对象 什么是VO?
			 * 我单表查询的时候,是不是一个表对应的一个实体类, 这个时候,我传入的这个Class就是那个值对象,他就对应了多张表
			 * hibernate 对这个样的数据做了一些处理 Transformers
			 * 我们Transformers这个对象,把结果集转换成一个VO的bean对象,
			 * 实体对象不是实体类对象,实体对象用于直接封装数据的那个对象,但是我们还是要传入claz
			 * 
			 * 因为VO是没有实体的,只有实体的一部分,只有类的部分.所以类和表之间没有一一对应的关系,,所以没有办法直接转换
			 * hibernate专门为这种情况创建了一个转换器
			 * ,这个转换器可以把我们表的数据直接封装到对应的对象中,那样的话,class可以传入多个,
			 * 但是用的最多的还是把我们的数据按照列名的方式分别对应到VO对象中,相当于一个类的集合,只不过把属性写在了一起
			 */
			sqlQuery.setResultTransformer(Transformers.aliasToBean(claz));
		}
		return sqlQuery.list();
	}

	// ------------
	@Override
	public <N> Pager<N> findBySQL(String sql, Class<?> claz, boolean hasEntity) {
		return findBySQL(sql, null, null, claz, hasEntity);
	}

	@Override
	public <N> Pager<N> findBySQL(String sql, Object arg, Class<?> claz,
			boolean hasEntity) {
		return findBySQL(sql, new Object[] { arg }, null, claz, hasEntity);
	}

	@Override
	public <N> Pager<N> findBySQL(String sql, Object[] args, Class<?> claz,
			boolean hasEntity) {
		return findBySQL(sql, args, null, claz, hasEntity);
	}

	@Override
	public <N> Pager<N> findBySQL(String sql, Map<String, Object> alias,
			Class<?> claz, boolean hasEntity) {
		return findBySQL(sql, null, alias, claz, hasEntity);
	}

	@Override
	public <N> Pager<N> findBySQL(String sql, Object[] args,
			Map<String, Object> alias, Class<?> claz, boolean hasEntity) {
		Pager<N> pager = new Pager<N>();
		// 1
		initSort(sql);
		// 查询总记录数
		String countSql = getCountQl(sql, true);
		SQLQuery countQuery = getSession().createSQLQuery(countSql);
		setAliasParameter(alias, countQuery);
		setParameter(args, countQuery);
		// BigInteger :
		// 更长长度的整型,可以理解为long,但是他是引用数据类型,和本机数据类型一样,只不过是可变的
		long totalRecord = ((BigInteger) countQuery.uniqueResult()).longValue();
		pager.setTotalReaord(totalRecord);
		// 查询具体在页面中显示的数据
		SQLQuery query = getSession().createSQLQuery(sql);
		setAliasParameter(alias, query);
		setParameter(args, query);
		pager = setPagerParameter(pager);
		if (hasEntity) {
			query.addEntity(claz);
		} else {
			query.setResultTransformer(Transformers.aliasToBean(claz));
		}
		query.setFirstResult(pager.getPageOffset()).setMaxResults(
				pager.getPageSize());
		// 上面都是在准备数据
		List<N> datas = query.list();
		pager.setDatas(datas);
		return pager;
	}

}
