package com.jaovo.cms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jaovo.cms.model.Group;
import com.jaovo.cms.model.Role;
import com.jaovo.cms.model.RoleType;
import com.jaovo.cms.model.User;
import com.jaovo.cms.model.UserGroup;
import com.jaovo.cms.model.UserRole;
import com.jaovo.common.dao.BaseDao;
import com.jaovo.common.model.Pager;

@Repository("userDao")
public class UserDao extends BaseDao<User> implements IUserDao {

	public UserDao() {
	}

	@Override
	public List<Role> listUserRoles(int userId) {
		String hql = "select userRole.role from UserRole userRole where userRole.user.id=?";
		return this.getSession().createQuery(hql).setParameter(0, userId)
				.list();
	}

	@Override
	public List<Integer> listUserRoleIds(int userId) {
		String hql = "select userRole.role.id from UserRole userRole where userRole.user.id=?";
		return this.getSession().createQuery(hql).setParameter(0, userId)
				.list();
	}

	@Override
	public List<Group> listUserGroups(int userId) {
		String hql = "select userGroup.group from UserGroup userGroup where userGroup.user.id=?";
		return this.getSession().createQuery(hql).setParameter(0, userId)
				.list();
	}

	@Override
	public List<Integer> listUserGroupIds(int userId) {
		String hql = "select userGroup.group.id from UserGroup userGroup where userGroup.user.id=?";
		return this.getSession().createQuery(hql).setParameter(0, userId)
				.list();
	}

	@Override
	public UserRole loadUserRole(int userId, int roleId) {
		String hql = "select userRole from UserRole userRole  left join fetch userRole.user user left join fetch userRole.role role where user.id=? and role.id=?";
		return (UserRole) this.getSession().createQuery(hql)
				.setParameter(0, userId).setParameter(1, roleId).uniqueResult();
	}

	@Override
	public UserGroup loadUserGroup(int userId, int groupId) {
		String hql = "select userGroup from UserGroup userGroup left join fetch userGroup.user user left join fetch userGroup.group gp where user.id=? and gp.id=?";

		return (UserGroup) this.getSession().createQuery(hql)
				.setParameter(0, userId).setParameter(1, groupId)
				.uniqueResult();
	}

	@Override
	public User loadByUsername(String username) {
		String hql = "from User where username=?";
		return (User) this.getSession().createQuery(hql)
				.setParameter(0, username).uniqueResult();
	}

	@Override
	public List<User> listRoleUsers(int roleId) {
		String hql = "select userRole.user from UserRole userRole where userRole.role.id=?";
		// return this.getSession().createQuery(hql).setParameter(0, roleId)
		// .list();
		return this.list(hql, roleId);
	}

	@Override
	public List<User> listRoleUsers(RoleType roleType) {
		String hql = "select userRole.user from UserRole userRole where userRole.role.roleType=?";
		return this.list(hql, roleType);
	}

	@Override
	public List<User> listGroupUsers(int gid) {
		String hql = "select userGroup.user from UserGroup userGroup where userGroup.group.id=?";
		return this.list(hql, gid);
	}

	@Override
	public void addUserRole(User user, Role role) {
		UserRole userRole = this.loadUserRole(user.getId(), role.getId());
		if (userRole != null) {
			return;
		}
		userRole = new UserRole();
		userRole.setUser(user);
		userRole.setRole(role);
		this.getSession().save(userRole);
	}

	@Override
	public void addUserGroup(User user, Group group) {
		UserGroup userGroup = this.loadUserGroup(user.getId(), group.getId());
		if (userGroup != null) {
			return;
		}
		userGroup = new UserGroup();
		userGroup.setUser(user);
		userGroup.setGroup(group);
		this.getSession().save(userGroup);
	}

	@Override
	public void deleteUserRole(int uid, int rid) {
		String hql = "delete UserRole userRole where userRole.user.id=? and userRole.role.id=?";
		this.updateByHql(hql, new Object[] { uid, rid });
	}

	// 清空某个用户的角色
	@Override
	public void deleteUserRoles(int uid) {
		String hql = "delete UserRole userRole where userRole.user.id=?";
		this.updateByHql(hql, uid);
	}

	// 清空某个用户的组
	@Override
	public void deleteUserGroups(int uid) {
		String hql = "delete UserGroup userGroup where userGroup.user.id=?";
		this.updateByHql(hql, uid);

	}

	@Override
	public void deleteUserGroup(int uid, int gid) {
		String hql = "delete UserGroup userGroup where userGroup.user.id=? and userGroup.group.id=?";
		this.updateByHql(hql, new Object[] { uid, gid });

	}

	@Override
	public Pager<User> findUser() {
		return this.find("from User");
	}

}
