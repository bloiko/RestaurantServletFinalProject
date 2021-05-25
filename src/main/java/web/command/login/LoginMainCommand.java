package web.command.login;

import org.apache.log4j.Logger;
import service.UserService;
import web.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Command that process user data from the main login page.
 *
 * @author B.Loiko
 *
 */
public class LoginMainCommand extends Command {
    public static final String COMMAND = "web/command";
    private UserService userService;
    private static final Logger log = Logger.getLogger(LoginMainCommand.class);

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
        log.debug("Command starts");
        HttpSession session = request.getSession();
        removePastErrorMessagesIfExist(session);
        String username = request.getParameter("username");
        log.trace("Get parameter from the request: username --> "+ username);

        String password = request.getParameter("password");
        log.trace("Get parameter from the request: password --> "+ password);
            if (userService.isCorrectUser(username, password)) {
                log.info("User "+username+" is correct user");

                session.setAttribute("username", username);
                log.trace("Set attribute to the session: username --> "+ username);

                if ("ORDER_IN_CART".equals(session.getAttribute(COMMAND))) {
                    session.removeAttribute(COMMAND);
                    log.trace("Remove attribute from the session: "+COMMAND);

                    log.debug("Command finished");
                    return "cart.jsp";
                } else {
                    log.debug("Command finished");
                    return "/controller?command=menuList";
                }
            }
        session.setAttribute("message", "Account's Invalid");
        log.trace("Set attribute to the session: message --> "+ "Account's Invalid");

        log.debug("Command finished");
        //return "/controller?command=loginMain";
        return "login-main.jsp";
    }

    private void removePastErrorMessagesIfExist(HttpSession session) {
        String pastMessage = (String) session.getAttribute("message");
        if (pastMessage != null) {
            session.removeAttribute("message");
        }
        log.trace("Remove attribute from the session: message");

    }
}
