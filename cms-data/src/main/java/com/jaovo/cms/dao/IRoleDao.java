package com.jaovo.cms.dao;

import java.util.List;

import com.jaovo.cms.model.Role;
import com.jaovo.common.dao.IBaseDao;
import com.jaovo.common.model.Pager;

public interface IRoleDao extends IBaseDao<Role> {

	/**
	 * 返回数据集合的方法 查询有多少个角色
	 * 
	 * @return
	 */
	public List<Role> listRole();

	/**
	 * 返回分页对象的查询 查询有多少个角色并以分页显示
	 * 
	 * @return
	 */
	public Pager<Role> findRole();

	/**
	 * 根据RoleId 删除某个用户
	 * 
	 * @param id
	 */
	public void deleteRoleUsers(int id);
}
