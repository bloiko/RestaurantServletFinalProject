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
 * Login Main controller.
 * This login is used main part of the program. Also, after registration.
 *
 * @author B.Loiko
 */
public class LoginMainCommand extends Command {
    public static final String COMMAND = "web/command";
    private UserService userService;
    public LoginMainCommand() throws ServletException {
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

        HttpSession session = request.getSession();
        removePastErrorMessagesIfExist(session);
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            if (userService.isCorrectUser(username, password)) {
                session.setAttribute("username", username);
                if ("ORDER_IN_CART".equals(session.getAttribute(COMMAND))) {
                    session.removeAttribute(COMMAND);
                    return "cart.jsp";
                } else {
                    return "/controller?command=menuList";
                }
            }
        } catch (DBException e) {
            e.printStackTrace();
        }
        session.setAttribute("message", "Account's Invalid");
        return "/controller?command=loginMain";
    }

    private void removePastErrorMessagesIfExist(HttpSession session) {
        String pastMessage = (String) session.getAttribute("message");
        if (pastMessage != null) {
            session.removeAttribute("message");
        }
    }
}
