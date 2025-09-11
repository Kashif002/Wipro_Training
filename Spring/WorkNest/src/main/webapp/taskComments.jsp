<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Task Comments</title>
    <style>
        body { font-family: Arial, sans-serif; background: linear-gradient(to right, #a445b2, #d41872, #ff0066); min-height: 100vh; padding: 20px; }
        .container { max-width: 800px; margin: 0 auto; background: #fff; padding: 30px; border-radius: 10px; box-shadow: 0px 4px 10px rgba(0,0,0,0.2); }
        h1, h2 { color: #333; }
        .comment { border: 1px solid #ddd; padding: 15px; margin-bottom: 15px; border-radius: 5px; }
        .comment-header { font-weight: bold; color: #ff0066; margin-bottom: 5px; }
        .comment-date { font-size: 12px; color: #777; }
        .comment-content { margin-top: 10px; }
        .btn { padding: 8px 16px; background: #ff0066; color: white; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; }
        .btn:hover { background: #d41872; }
        textarea { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; margin-bottom: 10px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>Comments for: ${task.title}</h1>
        
        <!-- Add Comment Form -->
        <h2>Add Comment</h2>
        <form action="${pageContext.request.contextPath}/user/tasks/comments/add" method="post">
            <input type="hidden" name="taskId" value="${task.id}">
            <textarea name="content" placeholder="Write your comment..." rows="4" required></textarea>
            <button type="submit" class="btn">Post Comment</button>
        </form>
        
        <hr>
        
        <!-- Comments List -->
        <h2>All Comments</h2>
        <c:forEach var="comment" items="${comments}">
            <div class="comment">
                <div class="comment-header">${comment.user.name}</div>
                <div class="comment-date">${comment.createdAt}</div>
                <div class="comment-content">${comment.content}</div>
            </div>
        </c:forEach>
        
        <c:if test="${empty comments}">
            <p>No comments yet.</p>
        </c:if>
        
        <a href="${pageContext.request.contextPath}/user/tasks" class="btn">Back to Tasks</a>
    </div>
</body>
</html>