<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.wipro.ContactRepository" %>
<%@ page import="com.wipro.Contact" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact Manager</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #f5f7fa 5%, #c3cfe2 50%);
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
            max-width: 900px;
            margin: auto;
        }
        h1 {
            color: #2c3e50;
            text-align: center;
            margin-bottom: 30px;
            font-size: 2.5em;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 2px;
        }
        .btn-primary {
            background: linear-gradient(90deg, #3498db, #2980b9);
            border: none;
            padding: 12px 25px;
            font-size: 1.1em;
            transition: transform 0.3s, box-shadow 0.3s;
        }
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(52, 152, 219, 0.4);
        }
        h2 {
            color: #34495e;
            text-align: center;
            margin-top: 30px;
            font-size: 2em;
            font-weight: 500;
        }
        table {
            background: #fff;
            border-radius: 10px;
            overflow: hidden;
        }
        .table thead th {
            background: #3498db;
            color: #fff;
            font-weight: 600;
        }
        .table-striped tbody tr:nth-of-type(odd) {
            background-color: #f8f9fa;
        }
        .table td, .table th {
            vertical-align: middle;
            padding: 15px;
            text-align: center;
        }
        @media (max-width: 768px) {
            .container {
                padding: 15px;
            }
            h1 {
                font-size: 2em;
            }
            h2 {
                font-size: 1.5em;
            }
            .btn-primary {
                font-size: 1em;
                padding: 10px 20px;
            }
        }
        @media (max-width: 576px) 
        {
		    table {
		        font-size: 0.85em;
		    }
		    .table td, .table th {
		        padding: 8px;
		    }
		}
		        
    </style>
</head>
<body>
    <div class="container">
        <h1>Welcome to Contact Manager</h1>
        <div class="text-center mb-4">
            <a href="addContact.jsp" class="btn btn-primary btn-lg">Add Contact</a>
        </div>
        <h2>My Contacts</h2>
		<div class="table-responsive">
		    <table class="table table-striped table-bordered">
		        <thead class="thead-dark">
		            <tr>
		                <th>Name</th>
		                <th>Email</th>
		                <th>Phone</th>
		            </tr>
		        </thead>
		        <tbody>
		            <% 
		                List<Contact> contacts = ContactRepository.getAll();
		                if (contacts.isEmpty()) {
		            %>
		                <tr>
		                    <td colspan="3" class="text-center">No contacts added yet.</td>
		                </tr>
		            <% 
		                } else {
		                    for (Contact contact : contacts) {
		            %>
		                <tr>
		                    <td><%= contact.getName() %></td>
		                    <td><%= contact.getEmail() %></td>
		                    <td><%= contact.getPhone() %></td>
		                </tr>
		            <% 
		                    }
		                }
		            %>
		        </tbody>
		    </table>
		</div>
    </div>
</body>
</html>