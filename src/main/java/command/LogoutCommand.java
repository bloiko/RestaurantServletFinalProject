package command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Logout controller.
 * This controller simply delete all session data about user and simulate logout from the system.
 *
 * @author B.Loiko
 */
public class LogoutCommand extends Command {
    private static final Logger LOGGER = LogManager.getLogger(LogoutCommand.class);
    public LogoutCommand() throws ServletException {
        init();
    }
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOGGER.debug("Controller starts");
        HttpSession session = request.getSession();
        session.removeAttribute("username");
        LOGGER.trace("Session atribute was removed : username");
        session.removeAttribute("cart");
        LOGGER.trace("Session atribute was removed : cart");

        LOGGER.debug("Controller finished");
        return "menuList";
    }

    @Override
    public void init() throws ServletException {
        //empty
    }
}
