<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Comments</title>
    <style>
        body 
        { 
	        font-family: Arial, sans-serif; 
	        background: linear-gradient(to right, #a445b2, #d41872, #ff0066); 
	        height: 100vh; 
	        display: flex; 
	        justify-content: center; 
	        align-items: center; 
        }
        .container 
        { 
	        background: #fff; 
	        padding: 40px; 
	        border-radius: 10px; 
	        width: 700px; 
	        box-shadow: 0px 4px 10px rgba(0,0,0,0.2); 
        }
        h1 { text-align: center; margin-bottom: 20px; }
        
        textarea 
        { 
	        width: 100%; 
	        height: 100px; 
	        padding: 12px; 
	        border: 1px solid #ddd; 
	        border-radius: 6px; 
	        margin-bottom: 10px; 
        }
        
        button 
        { 
	        width: 100%; 
	        padding: 12px; 
	        background: #ff0066; 
	        border: none; 
	        color: #fff; 
	        border-radius: 6px; 
	        cursor: pointer; 
        }
        
        button:hover { background: #d41872; }
    </style>
</head>
<body>
    <div class="container">
        <h1>Comments</h1>
        <form action="comment" method="post">
            <textarea placeholder="Write your comment..."></textarea>
            <button type="submit">Post Comment</button>
        </form>
    </div>
</body>
</html>
