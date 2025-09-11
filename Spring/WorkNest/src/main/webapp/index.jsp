<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome to WorkNest</title>
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
            width: 600px;
            box-shadow: 0px 4px 10px rgba(0,0,0,0.2);
            text-align: center;
        }
        h1 
        { 
	        margin-bottom: 20px; 
	        color: #333; 
        }
        input 
        {   
        	width: 100%; 
	        padding: 12px; 
	        margin: 10px 0; 
	        border: 1px solid #ddd; 
	        border-radius: 6px; 
        }
        .btn
        { 
	        width: 100%; 
	        padding: 12px; 
	        background: #ff0066; 
	        border: none; color: #fff; 
	        font-size: 16px; 
	        border-radius: 6px; 
	        cursor: pointer; 
        }
        .btn:hover 
        { 
        	background: #d41872; 
        }
        a 
        { 
	        color: #ff0066; 
	        text-decoration: none; 
        }
    
 
    p {
        font-size: 1.2em;
        color: #34495e;
        line-height: 1.6;
        margin-bottom: 25px;
    }
    .features {
        display: flex;
        justify-content: space-around;
        margin: 20px 0;
    }
    .feature {
        font-size: 1em;
        color: #7f8c8d;
    }
    
</style>
</head>
<body>
    <div class="container">
        <h1>Welcome to WorkNest</h1>
        <p>The ultimate platform for task management and team collaboration.It offers a seamless experience to organize, track, and complete your tasks efficiently.</p>
        <div class="features">
            <div class="feature">Task Management</div>
            <div class="feature">Team Collaboration</div>
            <div class="feature">Real-Time Updates</div>
        </div>
        <a href="login.jsp" class="btn">Get Started Now</a>
    </div>
</body>
</html>