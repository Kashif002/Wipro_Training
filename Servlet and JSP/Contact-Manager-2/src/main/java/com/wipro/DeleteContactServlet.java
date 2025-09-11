package com.wipro;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/deleteContact")
public class DeleteContactServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        ContactRepository.delete(id);

        HttpSession session = request.getSession();
        session.setAttribute("message", "Contact deleted successfully!");
        session.setAttribute("messageType", "success");

        response.sendRedirect("index.jsp");
    }
}