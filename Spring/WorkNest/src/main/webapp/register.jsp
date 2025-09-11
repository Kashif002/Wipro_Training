<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
    <style>
        body { 
            font-family: Arial, sans-serif;
            background: linear-gradient(to right, #a445b2, #d41872, #ff0066); 
            height: 100vh; 
            display: flex; 
            justify-content: center; 
            align-items: center; 
        }
        .container { 
            background: #fff; 
            padding: 40px;
            border-radius: 10px; 
            width: 400px; 
            box-shadow: 0px 4px 10px rgba(0,0,0,0.2); 
            text-align: center; 
        }
        h1 { 
            margin-bottom: 20px; 
            color: #333; 
        }
        input, select { 
            width: 100%; 
            padding: 12px; 
            margin: 10px 0; 
            border: 1px solid #ddd; 
            border-radius: 6px; 
        }
        button { 
            width: 100%; 
            padding: 12px; 
            background: #ff0066; 
            border: none; 
            color: #fff; 
            font-size: 16px; 
            border-radius: 6px; 
            cursor: pointer; 
        }
        button:hover { 
            background: #d41872; 
        }
        a { 
            color: #ff0066; 
            text-decoration: none; 
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>REGISTER</h1>
        <form action="register" method="post">
            <input type="text" name="username" placeholder="Username" required>
            <input type="email" name="email" placeholder="Email" required>
            <input type="password" name="password" placeholder="Password" required>

            <!-- Role Dropdown -->
            <select name="role" required>
                <option value="">Select Role</option>
                <option value="USER">User</option>
                <option value="ADMIN">Admin</option>
            </select>

            <button type="submit">REGISTER</button>
        </form>
        <p>Already have an account? <a href="login">Login</a></p>
    </div>
</body>
</html>
