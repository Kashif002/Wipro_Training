<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<body>
<jsp:include page="header.jsp"></jsp:include>
<h2>Login</h2>
<form action="LoanForm.jsp">
Username:<input type="text" name="username" required><br>
Password:<input type="password" name="password" required><br>
<input type="submit" value="Login">
</form>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>