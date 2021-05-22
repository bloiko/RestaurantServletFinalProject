package web.command.login;

import exception.DBException;
import org.apache.log4j.Logger;
import service.UserService;
import web.command.Command;

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
    private static final Logger log = Logger.getLogger(LoginAdminCommand.class);

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
        log.debug("Command starts");

        String username = request.getParameter("username");
        log.trace("Get parameter from the request: username -->" + username);

        String password = request.getParameter("password");
        log.trace("Get parameter from the request: password -->" + password);

        try {
            if (userService.isCorrectAdmin(username, password)) {
                log.info("User with username " + username + "is admin");

                HttpSession session = request.getSession();
                session.setAttribute("username_admin", username);
                log.trace("Set attribute to the session: username -->"+username);

                log.debug("Command finished");
                return "/controller?command=adminList";
            }
        } catch (DBException e) {
            e.printStackTrace();
        }
        request.setAttribute("message", "Account's Invalid");
        log.trace("Set attribute to the request: message -->"+"Account's Invalid");

        log.debug("Command finished");
        return "login-admin.jsp";
    }
}
