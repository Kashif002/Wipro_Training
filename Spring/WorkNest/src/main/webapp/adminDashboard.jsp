<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            background: linear-gradient(to right, #a445b2, #d41872, #ff0066); 
            min-height: 100vh; 
            padding: 20px; 
        }
        .container { 
            max-width: 1200px; 
            margin: 0 auto; 
            background: #fff; 
            padding: 30px; 
            border-radius: 10px; 
            box-shadow: 0px 4px 10px rgba(0,0,0,0.2); 
        }
        h1 { 
            text-align: center; 
            margin-bottom: 30px; 
            color: #333; 
        }
        .dashboard-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        .dashboard-card {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            border-left: 4px solid #ff0066;
        }
        .dashboard-card h3 {
            margin-top: 0;
            color: #ff0066;
        }
        .stats {
            font-size: 24px;
            font-weight: bold;
            color: #333;
        }
        .nav-menu {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
            margin-top: 30px;
        }
        .nav-btn {
            display: block;
            padding: 15px;
            background: #ff0066;
            color: white;
            text-align: center;
            text-decoration: none;
            border-radius: 6px;
            font-weight: bold;
            transition: background 0.3s;
        }
        .nav-btn:hover {
            background: #d41872;
            transform: translateY(-2px);
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Admin Dashboard</h1>
        
        <!-- Statistics Cards -->
        <div class="dashboard-grid">
            <div class="dashboard-card">
                <h3>Pending Tasks</h3>
                <div class="stats">${pendingTasks != null ? pendingTasks.size() : 0}</div>
            </div>
            <div class="dashboard-card">
                <h3>In Progress</h3>
                <div class="stats">${inProgressTasks != null ? inProgressTasks.size() : 0}</div>
            </div>
            <div class="dashboard-card">
                <h3>Completed</h3>
                <div class="stats">${completedTasks != null ? completedTasks.size() : 0}</div>
            </div>
            <div class="dashboard-card">
                <h3>Delayed</h3>
                <div class="stats">${delayedTasks != null ? delayedTasks.size() : 0}</div>
            </div>
        </div>

        <!-- Navigation Menu -->
        <div class="nav-menu">
            <a href="${pageContext.request.contextPath}/admin/tasks" class="nav-btn">Manage Tasks</a>
            <a href="${pageContext.request.contextPath}/admin/comments" class="nav-btn">View All Comments</a>
            <a href="${pageContext.request.contextPath}/logout" class="nav-btn">Logout</a>
        </div>

        <!-- Task Status Sections -->
        <c:if test="${not empty pendingTasks}">
            <h2>Pending Tasks</h2>
            <table style="width: 100%; border-collapse: collapse; margin-bottom: 20px;">
                <tr style="background: #ff0066; color: white;">
                    <th style="padding: 12px; text-align: left;">Title</th>
                    <th style="padding: 12px; text-align: left;">Description</th>
                    <th style="padding: 12px; text-align: left;">Assigned To</th>
                    <th style="padding: 12px; text-align: left;">Due Date</th>
                </tr>
                <c:forEach var="task" items="${pendingTasks}">
                    <tr style="border-bottom: 1px solid #ddd;">
                        <td style="padding: 12px;">${task.title}</td>
                        <td style="padding: 12px;">${task.description}</td>
                        <td style="padding: 12px;">${task.assignedTo.name}</td>
                        <td style="padding: 12px;">${task.dueDate}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>

        <c:if test="${not empty inProgressTasks}">
            <h2>Tasks In Progress</h2>
            <table style="width: 100%; border-collapse: collapse; margin-bottom: 20px;">
                <tr style="background: #ff0066; color: white;">
                    <th style="padding: 12px; text-align: left;">Title</th>
                    <th style="padding: 12px; text-align: left;">Assigned To</th>
                    <th style="padding: 12px; text-align: left;">Due Date</th>
                </tr>
                <c:forEach var="task" items="${inProgressTasks}">
                    <tr style="border-bottom: 1px solid #ddd;">
                        <td style="padding: 12px;">${task.title}</td>
                        <td style="padding: 12px;">${task.assignedTo.name}</td>
                        <td style="padding: 12px;">${task.dueDate}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </div>
</body>
</html>