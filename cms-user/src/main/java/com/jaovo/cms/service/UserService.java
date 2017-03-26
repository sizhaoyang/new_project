package com.jaovo.cms.service;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.jaovo.cms.dao.IGroupDao;
import com.jaovo.cms.dao.IRoleDao;
import com.jaovo.cms.dao.IUserDao;
import com.jaovo.cms.model.CmsException;
import com.jaovo.cms.model.Group;
import com.jaovo.cms.model.Role;
import com.jaovo.cms.model.RoleType;
import com.jaovo.cms.model.User;
import com.jaovo.common.model.Pager;

@Service("userService")
public class UserService implements IUserService {

	@Inject
	private IUserDao userDao;
	@Inject
	private IRoleDao roleDao;
	@Inject
	private IGroupDao groupDao;

	public UserService() {
		super();
	}

	public IUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	public IRoleDao getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public IGroupDao getGroupDao() {
		return groupDao;
	}

	public void setGroupDao(IGroupDao groupDao) {
		this.groupDao = groupDao;
	}

	// ---------------------------------------

	@Override
	public void add(User user, Integer[] rids, Integer[] gids) {
		User oldUser = userDao.loadByUsername(user.getUsername());
		if (oldUser != null) {
			throw new CmsException("要添加的用户名已存在");
		}
		user.setCreateDate(new Date());
		// 添加用户
		userDao.add(user);

		// 添加用户组
		for (Integer gid : gids) {
			this.addUserGroup(user, gid);
		}
		// 添加角色
		for (Integer rid : rids) {
			this.addUserRole(user, rid);
		}
	}

	/**
	 * 辅助方法 用来判断要添加的组是否存在,如果不存在就报异常,存在就添加
	 * 
	 * @param user
	 * @param gid
	 */
	private void addUserGroup(User user, Integer gid) {
		Group group = groupDao.load(gid);
		if (group == null) {
			throw new CmsException("要添加的组 :" + gid + " 不存在");
		}
		userDao.addUserGroup(user, group);
	}

	/**
	 * 辅助方法 用来判断要添加的角色是否存在,如果不存在就报异常,存在就添加
	 * 
	 * @param user
	 * @param gid
	 */
	private void addUserRole(User user, Integer rid) {
		Role role = roleDao.load(rid);
		if (role == null) {
			throw new CmsException("要添加的角色 : " + rid + " 不存在");
		}
		userDao.addUserRole(user, role);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		// 删除用户对应的角色对象
		// 删除用户对应的组对象
		// 删除用户
	}

	@Override
	public void update(User user, Integer[] rids, Integer[] gids) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateStatus(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public User load(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Role> listUserRoles(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> listUserRoleIds(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Group> listUserGroups(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> listUserGroupIds(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> listRoleUsers(RoleType roleType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> listGroupUsers(int gid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pager<User> findUser() {
		// TODO Auto-generated method stub
		return null;
	}

}
