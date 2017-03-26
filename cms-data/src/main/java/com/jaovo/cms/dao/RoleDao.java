package com.jaovo.cms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jaovo.cms.model.Role;
import com.jaovo.common.dao.BaseDao;
import com.jaovo.common.model.Pager;

@Repository(value = "roleDao")
public class RoleDao extends BaseDao<Role> implements IRoleDao {
	public RoleDao() {
	}

	@Override
	public List<Role> listRole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role load(int id) {
		return super.load(id);
	}

	@Override
	public Pager<Role> findRole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRoleUsers(int id) {
		// TODO Auto-generated method stub

	}

}
