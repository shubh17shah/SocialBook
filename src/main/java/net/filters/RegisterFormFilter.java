package net.filters;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import net.controller.RegisterController;
import net.helper.Keys;
import net.helper.Validator;
import net.service.UserService;

@WebFilter("/register")
public class RegisterFormFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			String firstNameParam = request.getParameter("first-name");
			String firstName = (firstNameParam != null && !firstNameParam.isEmpty()) ? firstNameParam.trim() : "";

			String lastNameParam = request.getParameter("last-name");
			String lastName = (lastNameParam != null && !lastNameParam.isEmpty()) ? lastNameParam.trim() : "";

			String userNameParam = request.getParameter("user-name");
			String userName = (userNameParam != null && !userNameParam.isEmpty()) ? userNameParam.trim() : "";

			String emailParam = request.getParameter("email");
			String email = (emailParam != null && !emailParam.isEmpty()) ? emailParam.trim() : "";

			RequestDispatcher dis = request.getRequestDispatcher("signup.jsp");

			if (!Validator.checkName(firstName)) {
				request.setAttribute(Keys.ERROR, "First name is invalid");
				dis.forward(request, response);
			} else if (!Validator.checkName(lastName)) {
				request.setAttribute(Keys.ERROR, "Last name is invalid");
				dis.forward(request, response);
			} else if (!Validator.checkUserName(userName)) {
				request.setAttribute(Keys.ERROR, "User name is invalid");
				dis.forward(request, response);
			} else if (UserService.isDuplicateUserName(userName)) {
				request.setAttribute(Keys.ERROR, "User name is already exists");
				dis.forward(request, response);
			} else if (!Validator.checkEmail(email)) {
				request.setAttribute(Keys.ERROR, "Email is invalid");
				dis.forward(request, response);
			} else if (UserService.isDuplicateEmail(email)) {
				request.setAttribute(Keys.ERROR, "Email is already exists");
				dis.forward(request, response);
			} else {
				// Now, check the DOB fields (day, month, and year)
				String dayParam = request.getParameter("day");
				String monthParam = request.getParameter("month");
				String yearParam = request.getParameter("year");

				// Check if dayParam, monthParam, and yearParam are not null and not empty
				if (dayParam == null || dayParam.isEmpty() || monthParam == null || monthParam.isEmpty()
						|| yearParam == null || yearParam.isEmpty()) {
					request.setAttribute(Keys.ERROR, "Invalid date of birth");
					dis.forward(request, response);
					return;
				}

				int birthDay = Integer.parseInt(dayParam);
				int birthMonth = Integer.parseInt(monthParam);
				int birthYear = Integer.parseInt(yearParam);

				LocalDate dob = LocalDate.of(birthYear, birthMonth, birthDay);

				// Use the Validator to check the birthday
				if (!Validator.checkBirthday(dob)) {
					request.setAttribute(Keys.ERROR, "Invalid Date of birth");
					dis.forward(request, response);
					return;
				}

				String passwordParam = request.getParameter("password");
				String password = (passwordParam != null && !passwordParam.isEmpty()) ? passwordParam.trim() : "";

				String confirmPasswordParam = request.getParameter("confirm-password");
				String confirmPassword = (confirmPasswordParam != null && !confirmPasswordParam.isEmpty())
						? confirmPasswordParam.trim()
						: "";

//			String dayParam = request.getParameter("day");
//			String day = (dayParam != null && !dayParam.isEmpty()) ? dayParam.trim() : "";
//
//			String monthParam = request.getParameter("month");
//			String month = (monthParam != null && !monthParam.isEmpty()) ? monthParam.trim() : "";
//
//			String yearParam = request.getParameter("year");
//			String year = (yearParam != null && !yearParam.isEmpty()) ? yearParam.trim() : "";

				String genderParam = request.getParameter("gender");
				String gender = (genderParam != null && !genderParam.isEmpty()) ? genderParam.trim() : "";

				if (!Validator.checkPassword(password)) {
					request.setAttribute(Keys.ERROR, "Password should contain this type of characters Ex@mple1");
					dis.forward(request, response);
				} else if (!password.equals(confirmPassword)) {
					request.setAttribute(Keys.ERROR, "Passwords do not match");
					dis.forward(request, response);
				}else {

					chain.doFilter(request, response);
				}

			}
		} catch (SQLException ex) {
			Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void destroy() {
	}

}