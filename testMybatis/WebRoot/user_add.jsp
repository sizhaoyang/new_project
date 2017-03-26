<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
  <center>
  <h2>用户添加页面</h2>
  <form action="<%=basePath %>/user/user_add"   >
       
         用户姓名：<input type="text"    name="username" /><br>
         用户密码：<input type="password"  name="password"/><br>
       	 用户地址：<input type="text"   name="address"/><br>
       	 用户邮箱：<input type="text"   name="email"/><br>
       	 用户号码：<input type="text"  name="phone"/><br>
       	 <input  type="submit"   value="提交">
       
      </form>
    </center>
  </body>
</html>
