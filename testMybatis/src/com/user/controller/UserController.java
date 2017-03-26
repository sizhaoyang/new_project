package com.user.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.user.model.User;
import com.user.util.JsonUtil;
import com.user.util.UserFactory;

public class UserController extends ActionSupport    implements ModelDriven<User>,RequestAware{
     private User  user=new User();
	private Map<String, Object>  request;
	public  String   add(){
		 Boolean   isAddSuccess= UserFactory.getUserService().add(user);
		 if (isAddSuccess) {
			return  SUCCESS;
		}else {
			this.addFieldError("executeError", "添加错误");
			return  ERROR;
		}
	  }
	  public  String   edit(){
		 Boolean   isEditSuccess= UserFactory.getUserService().edit(user);
		 if (isEditSuccess) {
			request.put("user", getModel());
			return  SUCCESS;
		}else {
			this.addFieldError("executeError", "编辑错误");
			return  ERROR;
		}
	  }
	  public  String   delete(){
			 Boolean   isDeleteSuccess= UserFactory.getUserService().delete(user.getId());
			 if (isDeleteSuccess) {
				return  SUCCESS;
			}else {
				this.addFieldError("executeError", "删除错误");
				return  ERROR;
			}
	  }
	  
	  public  String   list(){
			 List<User> userList=  UserFactory.getUserService().selectAll();
			  request.put("userList",userList);
			  return  SUCCESS;
	  }
	  
	  public  String  selectByUsername(){
		  List<User> userList=  UserFactory.getUserService().selectByUsername(user.getUsername());
		  
		  HttpServletRequest  request=ServletActionContext.getRequest();
		  request.setAttribute("userList",userList);
		  return  SUCCESS;
	  }

	@Override
	public User getModel() {
		return user;
	}
	@Override
	public void setRequest(Map<String, Object> request) {
		this.request=request;
	}
	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	public  String  editBefore(){
		request.put("user", user);
		return  SUCCESS;
	}
}
