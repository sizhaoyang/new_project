package com.user.service.impl;

import java.util.List;

import com.user.model.User;
import com.user.service.UserService;
import com.user.util.UserFactory;

public class UserServiceImpl implements UserService {
	@Override
	public Boolean add(User user) {
	    if(validate(user)){
	    	
	     return  UserFactory.getUserDao().add(user)==1?true:false;
	    }else {
		 return  false;
		}
	}

	@Override
	public Boolean edit(User user) {
		 if(validate(user)){
		     return  UserFactory.getUserDao().edit(user)==1?true:false;
		    }else {
			 return  false;
			}
	}

	@Override
	public Boolean delete(int id) {
	
		return  UserFactory.getUserDao().delete(id)==1?true:false;
	}


	@Override
	public List<User> selectAll() {
		return UserFactory.getUserDao().selectAll();
	}

	public  Boolean   validate(User  user){
	     if (user.getUsername()==null||"".equals(user.getUsername().trim())) {
			return  false;
		}else if(user.getPassword()==null||"".equals(user.getPassword().trim())){
			return  false;
		}else {
			return  true;
		}
	}

	@Override
	public List<User> selectByUsername(String username) {
		return UserFactory.getUserDao().selectByUsername(username);
	}
}
