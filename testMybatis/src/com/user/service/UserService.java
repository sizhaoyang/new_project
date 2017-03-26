package com.user.service;

import java.util.List;

import com.user.model.User;

public interface UserService {
  
	public Boolean  add(User  user);
	
	public  Boolean  edit(User  user);
	
	public  Boolean  delete(int  id);
	
	public  List<User>  selectAll();
	
	public List<User>  selectByUsername(String  username);
	
}
