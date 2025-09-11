// AddContactServlet.java
package com.wipro;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/addContact")
public class AddContactServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");

		if (name != null && email != null && phone != null) {
			Contact contact = new Contact(name, email, phone);
			ContactRepository.add(contact);
		}

		response.sendRedirect("index.jsp");
	}
}