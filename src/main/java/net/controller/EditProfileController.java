package net.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.helper.Keys;
import net.helper.Validator;
import net.model.User;
import net.service.UserService;

@WebServlet("/editprofile")
public class EditProfileController extends HttpServlet {
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.setAttribute(Keys.ERROR, "");
		session.setAttribute(Keys.SUCCESS, "");

		String action = request.getParameter("action");

		if (action != null && action.equals("update-profile")) {

			try {
				User user = (User) session.getAttribute(Keys.USER);
//				String currentEmail = user.getEmail();
				String currentUserName = user.getuserName();
//				String oldEmail = user.getEmail();

				String firstName = request.getParameter("first-name").trim();
				String lastName = request.getParameter("last-name").trim();
				String newUserName = request.getParameter("user-name").trim();
//				String newEmail = request.getParameter("email").trim();
//				String password = request.getParameter("password").trim();
//				String confirmPassword = request.getParameter("confirm-password").trim();
//				String dayParam = request.getParameter("day");
//				String monthParam = request.getParameter("month");
//				String yearParam = request.getParameter("year");
//				int birthDay = Integer.parseInt(dayParam);
//				int birthMonth = Integer.parseInt(monthParam);
//				int birthYear = Integer.parseInt(yearParam);
//				LocalDate dob = LocalDate.of(birthYear, birthMonth, birthDay);
				String gender = request.getParameter("gender").trim();

				// Check if username or email has changed
				boolean isUserNameChanged = !currentUserName.equals(newUserName);
//				boolean isEmailChanged = !oldEmail.equals(newEmail);

				// Check if username and email are valid
				boolean isValid = true;

				if (isUserNameChanged) {
					if (!Validator.checkUserName(newUserName)) {
						session.setAttribute(Keys.ERROR, "User name is invalid");
						isValid = false;
					} else if (UserService.isDuplicateUserName(newUserName)) {
						session.setAttribute(Keys.ERROR, "User name is already in use");
						isValid = false;
					}
				}

//				if (isEmailChanged) {
//					if (!Validator.checkEmail(newEmail)) {
//						session.setAttribute(Keys.ERROR, "Email is invalid");
//						isValid = false;
//					} else if (UserService.isDuplicateEmail(newEmail)) {
//						session.setAttribute(Keys.ERROR, "Email is already in use");
//						isValid = false;
//					}
//				}

				// Check if birthday is valid
//				if (!Validator.checkBirthday(dob)) {
//					session.setAttribute(Keys.ERROR, "Date of birth cannot be in the future");
//					isValid = false;
//				}

				// Check if password is valid
//				if (!Validator.checkPassword(password)) {
//					session.setAttribute(Keys.ERROR, "Password should contain valid characters");
//					isValid = false;
//				} else if (!password.equals(confirmPassword)) {
//					session.setAttribute(Keys.ERROR, "Passwords do not match");
//					isValid = false;
//				}

				if (isValid) {
//					String birthday = String.format("%s-%s-%s", dayParam, monthParam, yearParam);
					if (UserService.updateUser(firstName, lastName, newUserName, gender,
							currentUserName) == Validator.SUCCESS) {
//	for logout after username change
//						User updatedUser = UserService.getUser(currentUserName);
						User updatedUser = UserService.getUser(newUserName);
						session.setAttribute(Keys.USER, updatedUser);
						session.setAttribute(Keys.SUCCESS, "Data has been updated successfully!");
					} else {
						session.setAttribute(Keys.ERROR, "Something went wrong. Please try again.");
					}
				}

			} catch (SQLException ex) {
				Logger.getLogger(EditProfileController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		RequestDispatcher dis = request.getRequestDispatcher("WEB-INF/editprofile.jsp");
		dis.forward(request, response);

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