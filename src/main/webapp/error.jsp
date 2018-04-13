<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="util.Errors"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Craigslist Watcher</title>
<link
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet">
</head>

<%
  Errors error = (Errors) request.getAttribute("error");
  if (error == null) {
    error = Errors.UNKNOWN_ERROR;
  }
  String errorMessage = error.getMessage();
%>
<body>
	<div><%=errorMessage%></div>
</body>
</html>