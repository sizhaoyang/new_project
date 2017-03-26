package com.user.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import test.testAddData;

import com.user.dao.UserDao;
import com.user.model.User;
import com.user.util.MyBatisUtil;

public class UserDaoImpl implements  UserDao{

/*	@Test
	public  void  testAddData(){
		User user=new User();
		System.out.println(user.getClass().getName());
	}*/
   
	@Override
	public int add(User user) {
		int  isAddSuccess=0;
		SqlSession  session=null;
          try {
        	  session=MyBatisUtil.getSession();
			  session.insert("com.user.model.User.add", user);
			  session.commit();
			  isAddSuccess=1;
		} catch (Exception e) {
			System.out.println("添加出错");
		}finally{
			session.close();
		}
		return   isAddSuccess;
	}

	@Override
	public int edit(User user) {
		int  isEditSuccess=0;
		SqlSession  session=null;
        try {
			  session=MyBatisUtil.getSession();
			  session.update("com.user.model.User.edit", user);
			  session.commit();
			  isEditSuccess=1;
		} catch (Exception e) {
			System.out.println("编辑出错");
		}finally{
			session.close();
		}
		return isEditSuccess;
	}

	@Override
	public int delete(int id) {
		int  isDeleteSuccess=0;
		SqlSession  session=null;
        try {
        	  session=MyBatisUtil.getSession();
			  session.delete("com.user.model.User.delete", id);
			  session.commit();
			  isDeleteSuccess=1;
		} catch (Exception e) {
			System.out.println("删除出错");
		}finally{
			session.close();
		}
			return  isDeleteSuccess;
		
	}

	@Override
	public List<User> selectAll() {
		List<User>  userList=new  ArrayList<User>();
		SqlSession  session=null;
		 try {
			 session=MyBatisUtil.getSession();	
			userList= session.selectList("com.user.model.User.selectAll");
			 
			} catch (Exception e) {
				System.out.println("查询出错");
			}finally{
				session.close();
			}
		return userList;
	}
    
	

	@Override
	public List<User> selectByUsername(String username) {
		List<User>  userList=new  ArrayList<User>();
		SqlSession  session=null;
		 try {
			 session=MyBatisUtil.getSession();	
			userList= session.selectList("com.user.model.User.selectByUsername","%"+username+"%");
			 
			} catch (Exception e) {
				System.out.println("查询出错");
			}finally{
				session.close();
			}
		return userList;
	}
}
