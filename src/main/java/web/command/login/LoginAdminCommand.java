package web.command.login;

import web.command.Command;
import exception.DBException;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Login Admin controller.
 * This login is used in AdminFilter that goes before showing admin.jsp
 *
 * @author B.Loiko
 */
public class LoginAdminCommand extends Command {
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    public LoginAdminCommand() throws ServletException {
        init();
    }
    @Override
    public void init() throws ServletException {
        try {
            userService = new UserService();
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            if (userService.isCorrectAdmin(username, password)) {
                HttpSession session = request.getSession();
                session.setAttribute("username_admin", username);
                return "/controller?command=adminList";
            }

        } catch (DBException e) {
            e.printStackTrace();
        }
        request.setAttribute("message", "Account's Invalid");
        return "login-admin.jsp";
    }
}
