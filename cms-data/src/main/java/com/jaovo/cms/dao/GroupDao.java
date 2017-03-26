package com.jaovo.cms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jaovo.cms.model.Group;
import com.jaovo.common.dao.BaseDao;
import com.jaovo.common.model.Pager;

@Repository("groupDao")
public class GroupDao extends BaseDao<Group> implements IGroupDao {

	@Override
	public void add(Group t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Group t) {
		// TODO Auto-generated method stub

	}

	@Override
	public Group load(int id) {
		return super.load(id);
	}

	@Override
	public List<Group> listGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pager<Group> findGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteGroupUsers(int gid) {
		// TODO Auto-generated method stub

	}

}
