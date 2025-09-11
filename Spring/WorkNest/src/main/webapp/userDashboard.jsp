<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Dashboard</title>
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
        .welcome-message {
            text-align: center;
            font-size: 18px;
            margin-bottom: 30px;
            color: #666;
        }
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        .stat-card {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            border-left: 4px solid #ff0066;
            text-align: center;
        }
        .stat-card h3 {
            margin-top: 0;
            color: #ff0066;
        }
        .stat-number {
            font-size: 24px;
            font-weight: bold;
            color: #333;
        }
        .nav-menu {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
            margin-bottom: 30px;
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
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        th, td {
            padding: 12px;
            border: 1px solid #ddd;
            text-align: left;
        }
        th {
            background: #ff0066;
            color: #fff;
        }
        .status-badge {
            padding: 4px 8px;
            border-radius: 4px;
            color: white;
            font-weight: bold;
        }
        .status-pending { background: #ffc107; }
        .status-inprogress { background: #17a2b8; }
        .status-completed { background: #28a745; }
        .status-delayed { background: #dc3545; }
        .action-container {
            display: flex;
            gap: 5px;
            flex-wrap: nowrap;
            align-items: center;
        }
        .status-dropdown {
            padding: 6px;
            border-radius: 4px;
            border: 1px solid #ddd;
            margin-right: 5px;
        }
        .action-btn {
            padding: 6px 12px;
            border: none;
            border-radius: 4px;
            color: white;
            text-decoration: none;
            display: inline-block;
            cursor: pointer;
            font-size: 12px;
            white-space: nowrap;
        }
        .btn-comments { background: #6c757d; }
        .btn-comments:hover { background: #5a6268; }
    </style>
    <script>
        function updateTaskStatus(taskId, status) {
            window.location.href = '${pageContext.request.contextPath}/user/tasks/updateStatus?taskId=' + taskId + '&status=' + status;
        }
    </script>
</head>
<body>
    <div class="container">
        <h1>User Dashboard</h1>
        
        <div class="welcome-message">
            Welcome, ${user.name}! Here's your work overview.
        </div>

        <!-- Statistics -->
        <div class="stats-grid">
            <div class="stat-card">
                <h3>Total Tasks</h3>
                <div class="stat-number">${tasks != null ? tasks.size() : 0}</div>
            </div>
            <div class="stat-card">
                <h3>Pending</h3>
                <div class="stat-number">
                    <c:set var="pendingCount" value="0" />
                    <c:forEach var="task" items="${tasks}">
                        <c:if test="${task.status == 'PENDING'}">
                            <c:set var="pendingCount" value="${pendingCount + 1}" />
                        </c:if>
                    </c:forEach>
                    ${pendingCount}
                </div>
            </div>
            <div class="stat-card">
                <h3>In Progress</h3>
                <div class="stat-number">
                    <c:set var="progressCount" value="0" />
                    <c:forEach var="task" items="${tasks}">
                        <c:if test="${task.status == 'IN_PROGRESS'}">
                            <c:set var="progressCount" value="${progressCount + 1}" />
                        </c:if>
                    </c:forEach>
                    ${progressCount}
                </div>
            </div>
            <div class="stat-card">
                <h3>Completed</h3>
                <div class="stat-number">
                    <c:set var="completedCount" value="0" />
                    <c:forEach var="task" items="${tasks}">
                        <c:if test="${task.status == 'COMPLETED'}">
                            <c:set var="completedCount" value="${completedCount + 1}" />
                        </c:if>
                    </c:forEach>
                    ${completedCount}
                </div>
            </div>
        </div>

        <!-- Navigation Menu -->
        <div class="nav-menu">
            <a href="${pageContext.request.contextPath}/user/tasks" class="nav-btn">My Tasks</a>
            <a href="${pageContext.request.contextPath}/logout" class="nav-btn">Logout</a>
        </div>

        <!-- Tasks Table -->
        <h2>Your Tasks</h2>
        <c:choose>
            <c:when test="${not empty tasks}">
                <table>
                    <tr>
                        <th>Title</th>
                        <th>Description</th>
                        <th>Status</th>
                        <th>Start Date</th>
                        <th>Due Date</th>
                        <th>Actions</th>
                    </tr>
                    <c:forEach var="task" items="${tasks}">
                        <tr>
                            <td>${task.title}</td>
                            <td>${task.description}</td>
                            <td>
                                <span class="status-badge 
                                    <c:choose>
                                        <c:when test="${task.status == 'COMPLETED'}">status-completed</c:when>
                                        <c:when test="${task.status == 'IN_PROGRESS'}">status-inprogress</c:when>
                                        <c:when test="${task.status == 'PENDING'}">status-pending</c:when>
                                        <c:otherwise>status-delayed</c:otherwise>
                                    </c:choose>">
                                    ${task.status}
                                </span>
                            </td>
                            <td>${task.startDate}</td>
                            <td>${task.dueDate}</td>
                            <td>
                                <div class="action-container">
                                    <!-- Status Dropdown -->
                                    <select class="status-dropdown" onchange="updateTaskStatus(${task.id}, this.value)">
                                        <option value="PENDING" ${task.status == 'PENDING' ? 'selected' : ''}>Pending</option>
                                        <option value="IN_PROGRESS" ${task.status == 'IN_PROGRESS' ? 'selected' : ''}>In Progress</option>
                                        <option value="COMPLETED" ${task.status == 'COMPLETED' ? 'selected' : ''}>Completed</option>
                                        <option value="DELAYED" ${task.status == 'DELAYED' ? 'selected' : ''}>Delayed</option>
                                    </select>
                                    
                                    <!-- Comments Button -->
                                    <a href="${pageContext.request.contextPath}/user/tasks/comments?taskId=${task.id}" 
                                       class="action-btn btn-comments">Comments</a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:otherwise>
                <p style="text-align: center; color: #666; font-style: italic;">
                    No tasks assigned yet. Please check back later.
                </p>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>