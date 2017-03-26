package com.user.util;

import com.user.dao.UserDao;
import com.user.dao.impl.UserDaoImpl;
import com.user.service.UserService;
import com.user.service.impl.UserServiceImpl;

public class UserFactory {
   
	 public  static UserDao  getUserDao(){
		 return   new  UserDaoImpl();
	 }
	 
	 public  static  UserService  getUserService(){
		 return   new  UserServiceImpl();
	 }
}
