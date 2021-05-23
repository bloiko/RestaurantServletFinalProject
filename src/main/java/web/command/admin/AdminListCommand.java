package web.command.admin;

import database.dao.OrderDAO;
import database.entity.Order;
import database.entity.OrderStatus;
import exception.DBException;
import org.apache.log4j.Logger;
import service.OrderService;
import web.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Command that shows list of orders in admin page.
 *
 * @author B.Loiko
 *
 */
public class AdminListCommand extends Command {
    private OrderDAO orderListDAO;
    private OrderService orderService;
    private static final Logger log = Logger.getLogger(AdminListCommand.class);

    public AdminListCommand() throws ServletException {
        init();
    }
    @Override
    public void init() throws ServletException {
        try {
            orderService = new OrderService();
            orderListDAO = new OrderDAO();
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Command starts");
        try {
            List<OrderStatus> orderStatuses = orderListDAO.getStatuses();
            log.trace("Get statuses from Service : satuses --> " + orderStatuses);

            List<Order> notDoneOrders = orderService.getNotDoneOrdersSortById();
            log.trace("Get not done orders from Service : notDoneOrders --> " + notDoneOrders);

            List<Order> doneOrders = orderService.getDoneOrders();
            log.trace("Get done orders from Service : doneOrders --> " + doneOrders);

            request.setAttribute("statusList", orderStatuses);
            log.trace("Set request parameter: statusList"+orderStatuses);

            request.setAttribute("NOT_DONE_ORDERS_LIST", notDoneOrders);
            log.trace("Set request parameter: NOT_DONE_ORDERS_LIST"+notDoneOrders);

            request.setAttribute("DONE_ORDERS_LIST", doneOrders);
            log.trace("Set request parameter: DONE_ORDERS_LIST"+doneOrders);

        } catch (DBException e) {
            e.printStackTrace();
        }
        log.debug("Command finished");
        return  "admin.jsp";
    }

    public void setOrderListDAO(OrderDAO orderListDAO) {
        this.orderListDAO = orderListDAO;
    }
}
