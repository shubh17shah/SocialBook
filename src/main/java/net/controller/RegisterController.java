package net.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.helper.Keys;
import net.helper.Validator;
import net.service.UserService;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String firstName = request.getParameter("first-name").trim();
//			String firstName = (firstNameParam != null && !firstNameParam.isEmpty()) ? firstNameParam.trim() : "";

			String lastName = request.getParameter("last-name").trim();
//			String lastName = (lastNameParam != null && !lastNameParam.isEmpty()) ? lastNameParam.trim() : "";

			String userName = request.getParameter("user-name").trim();
//			String userName = (userNameParam != null && !userNameParam.isEmpty()) ? userNameParam.trim() : "";

			String email = request.getParameter("email").trim();
//			String email = (emailParam != null && !emailParam.isEmpty()) ? emailParam.trim() : "";

			// Now, check the DOB fields (day, month, and year)
			String day = request.getParameter("day");
			String month = request.getParameter("month");
			String year = request.getParameter("year");

			// Check if dayParam, monthParam, and yearParam are not null and not empty
//			if (dayParam == null || dayParam.isEmpty() || monthParam == null || monthParam.isEmpty()
//					|| yearParam == null || yearParam.isEmpty()) {
//				request.setAttribute(Keys.ERROR, "Invalid date of birth");
//				dis.forward(request, response);
//				return;
//			}

			String password = request.getParameter("password").trim();
//			String password = (passwordParam != null && !passwordParam.isEmpty()) ? passwordParam.trim() : "";

			String gender = request.getParameter("gender").trim();
//			String gender = (genderParam != null && !genderParam.isEmpty()) ? genderParam.trim() : "";

			RequestDispatcher dis = request.getRequestDispatcher("signup.jsp");
			String birthday = String.format("%s-%s-%s", day, month, year);
			// Add to database
			if (UserService.addNewUser(firstName, lastName, userName, email, password, birthday,
					gender) == Validator.SUCCESS) {
				response.sendRedirect("index.jsp");
			} else {
				request.setAttribute(Keys.ERROR, "Something wrong! Please, try again!");
				dis.forward(request, response);
			}

		} catch (SQLException ex) {
			Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}
}
