package com.jaovo.cms.service;

import java.util.List;

import com.jaovo.cms.model.Group;
import com.jaovo.cms.model.Role;
import com.jaovo.cms.model.RoleType;
import com.jaovo.cms.model.User;
import com.jaovo.common.model.Pager;

public interface IUserService {

	/**
	 * 添加用户,需要判断用户是否存在,存在的话抛异常
	 * 
	 * @param user
	 * @param rids
	 * @param gids
	 */
	public void add(User user, Integer[] rids, Integer[] gids);

	/**
	 * 删除用户 : 删除的时候,需要删除和角色和组的映射关系一起删除
	 * 
	 * 用户有相应的文章,就不能删除
	 * 
	 * @param id
	 */
	public void delete(int id);

	/**
	 * 用户更新,如果需要更新的对象的组合角色和原有的一样,就不做操作,比原有的多就添加,如果比原有的少,就删除
	 * 
	 * @param user
	 * @param rids
	 * @param gids
	 */
	public void update(User user, Integer[] rids, Integer[] gids);

	/**
	 * 更新用户状态
	 * 
	 * @param id
	 */
	public void updateStatus(int id);

	/**
	 * 获取用户的信息
	 * 
	 * @param id
	 * @return
	 */
	public User load(int id);

	/**
	 * 查询出某个用户的所有角色信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<Role> listUserRoles(int userId);

	/**
	 * 获取用户的所有角色的Id
	 * 
	 * @param userId
	 * @return
	 */
	public List<Integer> listUserRoleIds(int userId);

	/**
	 * 查询某个用户的所有组信息
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
	 * 根据角色类型获取所有的用户对象
	 * 
	 * @param roleType
	 * @return
	 */
	public List<User> listRoleUsers(RoleType roleType);

	/**
	 * 获取某个组中所有的用户对象
	 * 
	 * @param gid
	 * @return
	 */
	public List<User> listGroupUsers(int gid);

	/**
	 * 查询用户,并分页
	 * 
	 * @return
	 */
	public Pager<User> findUser();
}
