<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Sign In</title>
</head>
<body>
    <form method="POST" action="user" name="submitForm">
    <div align="center">
        <table>
            <tr>
                <td>User name</td>
                <td><input type="text" name="name" /></td>
            </tr>
            <tr>
                <td>User email</td>
                <td><input type="text" name="email" /></td>
            </tr>
            <tr>
                <td>Password</td>
                <td><input type="password" name="password" /></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Submit" /></td>
            </tr>
        </table>
    </div>
</form>
</body>
</html>