<?xml version="1.0" encoding="UTF-8" ?>
<%@ page import="com.bank.LoanBean" %>
<%@ page errorPage="errorPage.jsp" %>
<%@ include file="header.jsp" %>

<html>
<head>
<title>Insert title here</title>
</head>
<body>

<jsp:useBean id="loan" class="com.bank.LoanBean" scope="session" />
<jsp:setProperty name="loan" property="customerName" param="name" />
<jsp:setProperty name="loan" property="loanAmount" param="amount" />
<jsp:setProperty name="loan" property="tenure" param="tenure" />

<%! 
    double interestRate = 8.5; // Declaration tag
    double calculateEMI(double amount, double rate, int tenure) {
        double monthlyRate = rate / (12 * 100);
        return (amount * monthlyRate * Math.pow(1 + monthlyRate, tenure)) /
               (Math.pow(1 + monthlyRate, tenure) - 1);
    }
%>

<%
    double emi = calculateEMI(loan.getLoanAmount(), interestRate, loan.getTenure());
    loan.setEmi(emi);
%>

<jsp:forward page="loanSummary.jsp" />

<%@ include file="footer.jsp" %>

</body>
</html>