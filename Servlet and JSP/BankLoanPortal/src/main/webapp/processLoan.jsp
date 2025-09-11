<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<jsp:useBean id="loan" class="com.Bank.LoanBean" scope="session" />
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