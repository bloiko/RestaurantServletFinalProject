package web.command.login;

import web.command.Command;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Command that log out user.
 *
 * @author B.Loiko
 *
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
        return "/controller?command=menuList";
    }

    @Override
    public void init() throws ServletException {
        //empty
    }
}
