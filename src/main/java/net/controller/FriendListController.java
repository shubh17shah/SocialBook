package net.controller;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
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
import net.model.User;
import net.service.UserService;

@WebServlet(urlPatterns = {"/friend-list"})
public class FriendListController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession sess = request.getSession();
            User me = (User) sess.getAttribute(Keys.USER);
            List<User> list = UserService.getFriendList(me.getId());
            sess.setAttribute(Keys.FRIEND_LIST, list);
            RequestDispatcher dis = request.getRequestDispatcher("WEB-INF/friend-list.jsp");
            dis.forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(FriendListController.class.getName()).log(Level.SEVERE, null, ex);
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
