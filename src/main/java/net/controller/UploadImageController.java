package net.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import net.helper.Keys;
import net.helper.Validator;
import net.model.User;
import net.service.UserService;

@MultipartConfig(maxFileSize = 16177215) // upload file's size up to 16MB
@WebServlet(urlPatterns = { "/upload-image" })
public class UploadImageController extends HttpServlet {

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.setAttribute(Keys.ERROR, "");
		InputStream inputStream = null;

		// obtains the upload file part in this multipart request
		Part filePart = request.getPart("profilePicture");
		if (filePart != null) {
			// get input stream of the uploaded file
			inputStream = filePart.getInputStream();
			try {
				String username = ((User) session.getAttribute(Keys.USER)).getuserName();
				if (UserService.updateUserAvatar(inputStream, username) == Validator.SUCCESS) {
					User user = UserService.getUser(username);
					session.setAttribute(Keys.USER, user);
					// Cleanup: Close the input stream and delete the temporary file
	                inputStream.close();
	                filePart.delete();

				} else {
					session.setAttribute(Keys.ERROR, "Cannot upload your avatar. Try again later!");
				}
			} catch (SQLException ex) {
				Logger.getLogger(UploadImageController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		response.sendRedirect("editprofile");
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
