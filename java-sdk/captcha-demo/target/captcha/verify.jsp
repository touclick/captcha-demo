<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.touclick.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body>
<%
			String checkCode = "123";
			String checkKey = request.getParameter("checkAddress");
			String token = request.getParameter("token");
			TouClick touclick = new TouClick();
			Status status = touclick.check(checkCode,checkKey,token);
			if(status != null && status.getCode()==0){
%>
<label>That's ok!</label>
<% }else{ %>
<label>Error</label>
<% } %>
	</body>
</html>