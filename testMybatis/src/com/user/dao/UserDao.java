package com.user.dao;

import java.util.List;

import com.user.model.User;

public interface UserDao {
	public  int  add(User  user);
	public  int  edit(User  user);
	public  int  delete(int  id);
	public  List<User>  selectAll();
	public   List<User>  selectByUsername(String username);
	
}
