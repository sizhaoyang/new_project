package com.jaovo.cms.dao;

import java.util.List;

import com.jaovo.cms.model.Group;
import com.jaovo.cms.model.Role;
import com.jaovo.cms.model.RoleType;
import com.jaovo.cms.model.User;
import com.jaovo.cms.model.UserGroup;
import com.jaovo.cms.model.UserRole;
import com.jaovo.common.dao.IBaseDao;
import com.jaovo.common.model.Pager;

public interface IUserDao extends IBaseDao<User> {

	/**
	 * 查询某个用户的所有角色信息
	 */
	public List<Role> listUserRoles(int userId);

	/**
	 * 获得用户的所有角色的Id
	 * 
	 * @param userId
	 * @return
	 */
	public List<Integer> listUserRoleIds(int userId);

	/**
	 * 查询出某个用户的所有组信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<Group> listUserGroups(int userId);

	/**
	 * 获得用户的所有组的Id
	 * 
	 * @param userId
	 * @return
	 */
	public List<Integer> listUserGroupIds(int userId);

	/**
	 * 根据用户和角色获得他们的关联对象
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	public UserRole loadUserRole(int userId, int roleId);

	/**
	 * 根据用户和组获得他们的关联的中间对象
	 * 
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public UserGroup loadUserGroup(int userId, int groupId);

	/**
	 * 根据用户名查询某个用户对象
	 * 
	 * @param username
	 * @return
	 */
	public User loadByUsername(String username);

	/**
	 * 根据角色id查询该角色下所有的用户对象
	 * 
	 * @param roleId
	 * @return
	 */
	public List<User> listRoleUsers(int roleId);

	/**
	 * 根据角色类型获取所有的用户对象
	 * 
	 * @param roleTye
	 * @return
	 */
	public List<User> listRoleUsers(RoleType roleType);

	/**
	 * 根据组id,获取组中的所有用户对象
	 * 
	 * @param gid
	 * @return
	 */
	public List<User> listGroupUsers(int gid);

	/**
	 * 给某个用户添加角色
	 * 
	 * @param user
	 * @param role
	 */
	public void addUserRole(User user, Role role);

	/**
	 * 给某个用户添加组信息
	 * 
	 * @param user
	 * @param group
	 */
	public void addUserGroup(User user, Group group);

	/**
	 * 根据用户id 清空该用户的角色信息
	 * 
	 * @param uid
	 */
	public void deleteUserRoles(int uid);

	/**
	 * 根据用户id清空该用户的组信息
	 * 
	 * @param uid
	 */
	public void deleteUserGroups(int uid);

	/**
	 * 查询用户,并分页
	 * 
	 * @return
	 */
	public Pager<User> findUser();

	/**
	 * 删除当前用户的角色对象.删除用户和角色之间的某个映射
	 * 
	 * @param uid
	 * @param rid
	 */
	public void deleteUserRole(int uid, int rid);

	/**
	 * 删除用户和组对象
	 */
	public void deleteUserGroup(int uid, int gid);
}
