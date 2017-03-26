package com.jaovo.cms.dao;

import java.util.List;

import com.jaovo.cms.model.Group;
import com.jaovo.common.dao.IBaseDao;
import com.jaovo.common.model.Pager;

public interface IGroupDao extends IBaseDao<Group> {
	/**
	 * 返回数据集合的方法
	 * 
	 * @return
	 */
	public List<Group> listGroup();

	/**
	 * 返回分页对象的查询
	 * 
	 * @return
	 */
	public Pager<Group> findGroup();

	// 和用户对象相关的方法
	/**
	 * 清空组中的用户
	 * 
	 * @param gid
	 */
	public void deleteGroupUsers(int gid);
}
