<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Tasks</title>
    <style>
        body { font-family: Arial, sans-serif; background: linear-gradient(to right, #a445b2, #d41872, #ff0066); min-height: 100vh; padding: 20px; }
        .container { max-width: 1200px; margin: 0 auto; background: #fff; padding: 30px; border-radius: 10px; box-shadow: 0px 4px 10px rgba(0,0,0,0.2); }
        h1 { text-align: center; margin-bottom: 30px; color: #333; }
        table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
        th, td { padding: 12px; border: 1px solid #ddd; text-align: left; }
        th { background: #ff0066; color: #fff; }
        .btn { padding: 8px 16px; background: #ff0066; color: white; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; }
        .btn:hover { background: #d41872; }
        .action-container { display: flex; gap: 5px; flex-wrap: nowrap; }
        .status-dropdown { padding: 6px; border-radius: 4px; border: 1px solid #ddd; }
        .status-badge {
            padding: 4px 8px;
            border-radius: 4px;
            color: white;
            font-weight: bold;
            display: inline-block;
            min-width: 80px;
            text-align: center;
        }
        .status-pending { background: #ffc107; }
        .status-inprogress { background: #17a2b8; }
        .status-completed { background: #28a745; }
        .status-delayed { background: #dc3545; }
    </style>
    <script>
        function updateTaskStatus(taskId, status) {
            window.location.href = '${pageContext.request.contextPath}/user/tasks/updateStatus?taskId=' + taskId + '&status=' + status;
        }
    </script>
</head>
<body>
    <div class="container">
        <h1>My Tasks</h1>
        
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
                            
                            <!-- View Comments Button -->
                            <a href="${pageContext.request.contextPath}/user/tasks/comments?taskId=${task.id}" class="btn">View Comments</a>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </table>
        
        <a href="${pageContext.request.contextPath}/user/dashboard" class="btn">Back to Dashboard</a>
    </div>
</body>
</html>