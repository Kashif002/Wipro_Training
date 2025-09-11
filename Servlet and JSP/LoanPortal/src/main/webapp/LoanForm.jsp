<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session= "true"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%@ include file="header.jsp" %>
<%
    String username = request.getParameter("username");
    session.setAttribute("user", username);
    String password = request.getParameter("password");
    
%>
<h3>Welcome, <%= session.getAttribute("user") %></h3>

<form action="processLoan.jsp" method="post">
    Name: <input type="text" name="name" required><br>
    Loan Amount: <input type="number" name="amount" required><br>
    Tenure (in months): <input type="number" name="tenure" required><br>
    <input type="submit" value="Apply Loan">
</form>
<%@ include file="footer.jsp" %>

</body>
</html>