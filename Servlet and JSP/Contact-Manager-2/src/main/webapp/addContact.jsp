<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Contact</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            min-height: 100vh;
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 50px;
        }
        .container {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            max-width: 500px;
            margin: auto;
        }
        h1 {
            color: black;
            text-align: center;
            margin-bottom: 30px;
            font-size: 2.5em;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 2px;
        }
        .form-group label {
            color: #34495e;
            font-weight: 500;
        }
        .form-control {
            border-radius: 8px;
            padding: 12px;
            font-size: 1.1em;
            border: 2px solid #ddd;
            transition: border-color 0.3s;
        }
        .form-control:focus {
            border-color: #3498db;
            box-shadow: 0 0 5px rgba(52, 152, 219, 0.3);
        }
        .btn-success {
            background: linear-gradient(45deg, #2ecc71, #27ae60);
            border: none;
            padding: 12px 25px;
            font-size: 1.1em;
            width: 100%;
            transition: transform 0.3s, box-shadow 0.3s;
        }
        .btn-success:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(46, 204, 113, 0.4);
        }
        .btn-secondary {
            background: #95a5a6;
            border: none;
            padding: 10px 20px;
            font-size: 1em;
            width: 100%;
            transition: transform 0.3s, box-shadow 0.3s;
        }
        .btn-secondary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(149, 165, 166, 0.4);
        }
        .alert {
            margin-bottom: 20px;
        }
        @media (max-width: 768px) {
            .container {
                padding: 15px;
            }
            h1 {
                font-size: 2em;
            }
            .form-control {
                font-size: 1em;
            }
            .btn-success, .btn-secondary {
                font-size: 1em;
                padding: 10px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Add New Contact</h1>
        <% 
            String message = (String) session.getAttribute("message");
            String messageType = (String) session.getAttribute("messageType");
            if (message != null) {
        %>
            <div class="alert <%= messageType.equals("success") ? "alert-success" : "alert-danger" %> mt-3">
                <%= message %>
            </div>
        <% 
                session.removeAttribute("message");
                session.removeAttribute("messageType");
            }
        %>
        <form action="addContact" method="post">
            <div class="form-group">
                <label for="name">Name</label>
                <input type="text" class="form-control" id="name" name="name" required>
            </div>
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" class="form-control" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="phone">Phone</label>
                <input type="tel" class="form-control" id="phone" name="phone" required>
            </div>
            <div class="text-center">
                <button type="submit" class="btn btn-success btn-lg">Save Contact</button>
            </div>
        </form>
        <div class="text-center mt-3">
            <a href="index.jsp" class="btn btn-secondary">Back to Home</a>
        </div>
    </div>
</body>
</html>