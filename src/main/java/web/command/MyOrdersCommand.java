package web.command;

import database.entity.Order;
import exception.DBException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Command that shows list of all user's orders.
 *
 * @author B.Loiko
 *
 */
public class MyOrdersCommand extends Command {
    private UserService userService;
    private static final Logger log = LogManager.getLogger(MyOrdersCommand.class);

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    public MyOrdersCommand() throws ServletException {
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
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("Controller starts");
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        log.trace("Session atribute : username" + username);

        List<Order> orders = new LinkedList<>();
        if (username != null) {
            try {
                orders = userService.getUserOrdersSortByOrderDateReversed(username);
            } catch (DBException e) {
                e.printStackTrace();
            }
        }
        request.setAttribute("ORDERS_LIST", orders);
        log.trace("Session atribute : username" + username);

        log.debug("Controller finished");
        return "my-orders.jsp";
    }


}
