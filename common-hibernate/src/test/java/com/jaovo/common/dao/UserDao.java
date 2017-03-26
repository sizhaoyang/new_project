package com.jaovo.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jaovo.common.model.Pager;
import com.jaovo.common.model.User;

@Repository("userDao")
public class UserDao extends BaseDao<User> implements IUserDao {

	@Override
	public List<User> list(String hql, Object[] args, Map<String, Object> alias) {
		return super.list(hql, alias, args);
	}

}
