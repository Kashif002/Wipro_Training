package com.wipro;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/editContact")
public class EditContactServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        Contact updatedContact = new Contact(id, name, email, phone);
        boolean updated = ContactRepository.update(updatedContact);

        HttpSession session = request.getSession();
        if (updated) {
            session.setAttribute("message", "Contact updated successfully!");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "Error: All fields are required!");
            session.setAttribute("messageType", "error");
        }

        response.sendRedirect("index.jsp");
    }
}