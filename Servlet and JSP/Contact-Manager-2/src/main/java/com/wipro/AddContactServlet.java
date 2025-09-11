package com.wipro;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/addContact")
public class AddContactServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        Contact contact = new Contact(0, name, email, phone); // ID will be set in repository
        boolean added = ContactRepository.add(contact);

        HttpSession session = request.getSession();
        if (added) {
            session.setAttribute("message", "Contact added successfully!");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Error: All fields are required!");
            session.setAttribute("messageType", "error");
        }

        response.sendRedirect("index.jsp");
    }
}