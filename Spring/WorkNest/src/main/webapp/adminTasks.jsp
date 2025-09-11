<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Tasks</title>
    <style>
        body { font-family: Arial, sans-serif; background: linear-gradient(to right, #a445b2, #d41872, #ff0066); min-height: 100vh; padding: 20px; }
        .container { max-width: 1200px; margin: 0 auto; background: #fff; padding: 30px; border-radius: 10px; box-shadow: 0px 4px 10px rgba(0,0,0,0.2); }
        h1 { text-align: center; margin-bottom: 30px; color: #333; }
        table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
        th, td { padding: 12px; border: 1px solid #ddd; text-align: left; }
        th { background: #ff0066; color: #fff; }
        .btn { padding: 8px 16px; background: #ff0066; color: white; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; }
        .btn:hover { background: #d41872; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; }
        .form-group input, .form-group select, .form-group textarea { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>Manage Tasks</h1>
        
        <!-- Add Task Form -->
        <h2>Add New Task</h2>
        <form action="${pageContext.request.contextPath}/admin/tasks/add" method="post">
            <div class="form-group">
                <label>Title:</label>
                <input type="text" name="title" required>
            </div>
            <div class="form-group">
                <label>Description:</label>
                <textarea name="description" required></textarea>
            </div>
            <div class="form-group">
                <label>Status:</label>
                <select name="status" required>
                    <option value="PENDING">Pending</option>
                    <option value="IN_PROGRESS">In Progress</option>
                    <option value="COMPLETED">Completed</option>
                    <option value="DELAYED">Delayed</option>
                </select>
            </div>
            <div class="form-group">
                <label>Start Date:</label>
                <input type="date" name="startDate" required>
            </div>
            <div class="form-group">
                <label>Due Date:</label>
                <input type="date" name="dueDate" required>
            </div>
            <div class="form-group">
                <label>Assign To:</label>
                <select name="assignedTo" required>
                    <c:forEach var="user" items="${users}">
                        <option value="${user.id}">${user.name} (${user.email})</option>
                    </c:forEach>
                </select>
            </div>
            <button type="submit" class="btn">Add Task</button>
        </form>
        
        <hr>
        
        <!-- Tasks List -->
        <h2>All Tasks</h2>
        <table>
            <tr>
                <th>Title</th>
                <th>Description</th>
                <th>Status</th>
                <th>Start Date</th>
                <th>Due Date</th>
                <th>Assigned To</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="task" items="${tasks}">
                <tr>
                    <td>${task.title}</td>
                    <td>${task.description}</td>
                    <td>${task.status}</td>
                    <td>${task.startDate}</td>
                    <td>${task.dueDate}</td>
                    <td>${task.assignedTo.name}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin/tasks/updateStatus?taskId=${task.id}&status=COMPLETED" class="btn">Mark Completed</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
        
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn">Back to Dashboard</a>
    </div>
</body>
</html>