<%@page import="net.helper.Keys"%>
<%@page import="java.util.Objects"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Forgot Password</title>
</head>
<body>
	<h1>Forgot Password</h1>
	<form action="forgotpassword" method="post">
		<p id="signup-error" class="error-message">
			<%=Objects.toString(request.getAttribute(Keys.ERROR), "")%>
		</p>
		Enter your email address: <input type="email" name="email" required>
		<input type="submit" value="Submit">
	</form>
</body>
</html>
