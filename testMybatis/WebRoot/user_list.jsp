<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
	<script type="text/javascript" src="<%=basePath %>/resources/jquery-1.8.3.js"></script>
  </head>
  
  <body  >
      <center><h2>用户列表</h2></center><br>
      
      <input  type="text"  id="username" value=""> <button  id="selectByUsername">搜索</button>
      <table   align="center"   border="1"   width="80%">
        
         
      	<tr>
      		<td>用户姓名</td>
      		<td>用户密码</td>
      		<td>用户地址</td>
      		<td>用户邮箱</td>
      		<td>用户电话</td>
      		<td>操作</td>
      		
      	</tr>
      	<c:forEach  items="${userList}"  var="user">
      	  	<tr>
      		<td>${user.username}</td>
      		<td>${user.password}</td>
      		<td>${user.address}</td>
      		<td>${user.email }</td>
      		<td>${user.phone}</td>
      		<td>
      			<a href="<%= basePath %>/user/add">添加</a>
      			
      			<a href="<%= basePath %>/user/editBefore?id=${user.id}&&username=${user.username}&&password=${user.password}&&address=${user.address}&&email=${user.email}&&phone=${user.phone}">编辑</a>
      			<a href="<%= basePath %>/user/user_delete?id=${user.id}">删除</a>
      		</td>
      	</tr>
      	
      	</c:forEach>
      </table>
      <div  id="content"></div>
       <c:if  test="${empty userList}">
           <a href="<%=basePath %>/user/add">   用户添加 </a>
       </c:if>
       <script type="text/javascript">
       $(function(){
       $("#selectByUsername").click(function(){
           username=  $("#username").val();
          window.location.href="<%=basePath%>user/selectByUsername?username="+username;
       });
          
       })
       </script>
       
  </body>
</html>
