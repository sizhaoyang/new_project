package com.jaovo.common.dao;

import java.util.List;
import java.util.Map;

import com.jaovo.common.model.Pager;
import com.jaovo.common.model.User;

public interface IUserDao extends IBaseDao<User> {

	/**
	 * 添加对象
	 */
	public void add(User user);

	/**
	 * 根据ID删除对象
	 */
	public void delete(int id);

	/**
	 * 更新对象
	 */
	public void update(User user);

	/**
	 * 根据ID查询对象
	 */
	public User load(int id);

	public List<User> list(String hql, Object[] args, Map<String, Object> alias);

	public Pager<User> find(String hql, Object[] args, Map<String, Object> alias);

	public Object loadObject(String hql, Object[] args,
			Map<String, Object> alias);

	public void updateByHql(String hql, Object[] args);

	public <N extends Object> List<N> listBySQL(String sql, Object[] args,
			Map<String, Object> alias, Class<?> claz, boolean hasEntity);

	public <N extends Object> Pager<N> findBySQL(String sql, Object[] args,
			Map<String, Object> alias, Class<?> claz, boolean hasEntity);
}
