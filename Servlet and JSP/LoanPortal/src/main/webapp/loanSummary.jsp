<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0">
	<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="true"/>
	<jsp:output doctype-root-element="html"
		doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
		omit-xml-declaration="true" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Insert title here</title>
</head>
<body>
<%@ page import="com.bank.LoanBean" %>
<%@ include file="header.jsp" %>

<jsp:useBean id="loan" class="com.bank.LoanBean" scope="session" />

<h3>Loan Application Summary</h3>
<p>Name: <jsp:getProperty name="loan" property="customerName" /></p>
<p>Loan Amount: <jsp:getProperty name="loan" property="loanAmount" /></p>
<p>Tenure: <jsp:getProperty name="loan" property="tenure" /> months</p>
<p>EMI: ₹<jsp:getProperty name="loan" property="emi" /></p>

<%@ include file="footer.jsp" %>
</body>
</html>
</jsp:root>