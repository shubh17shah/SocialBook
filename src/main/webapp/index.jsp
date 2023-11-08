<%@page import="net.helper.Keys"%>
<%@page import="java.util.Objects"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="./css/style.css">
<title>SocialBook Login</title>
</head>
<body>
	<div class="page">
		<img src="images/logo.png" alt="Logo" class="logologin">
		<div class="container">
			<div class="tabs">
				<button id="login-tab" class="tab-btn active">Login</button>
				<button id="signup-tab" class="tab-btn">Sign Up</button>
			</div>

			<div id="login-form" class="form">
				<h2 id="loginhead">Login</h2>
				<p id="login-error" class="error-message">
					<c:if test="${not empty sessionScope[Keys.ERROR]}">
			        ${sessionScope[Keys.ERROR]}
			        <%
			        session.removeAttribute(Keys.ERROR);
			        %>
						<!-- Remove the error message from the session -->
					</c:if>
				</p>
				<form action="login" method="post">
					<input type="text" id="login-email" name="user-login-id"
						placeholder="Email or Username"> <input type="password"
						id="login-password" name="user-login-password"
						placeholder="Password"> 

					<button id="login-button">Log In</button>
				</form>
			</div>
		</div>
	</div>
</body>
<script src="./js/script.js"></script>
</html>
