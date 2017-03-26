package com.jaovo.cms.service;

import java.util.List;

import com.jaovo.cms.model.Group;
import com.jaovo.common.model.Pager;

public interface IGroupService {
	public void add(Group group);

	public void delete(int id);

	public Group load(int id);

	public void update(Group group);

	public List<Group> listGroup();

	public Pager<Group> findGroup();

	public void deleteGroupUsers(int gid);
}
