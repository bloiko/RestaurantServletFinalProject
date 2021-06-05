package web.command.admin;

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

/**
 * Command that change order status in admin page.
 *
 * @author B.Loiko
 *
 */
public class AdminChangeOrderStatusCommand extends Command {
    private OrderService orderService;
    private static final Logger log = Logger.getLogger(AdminChangeOrderStatusCommand.class);
    public AdminChangeOrderStatusCommand() throws ServletException {
        init();
    }
    @Override
    public void init() throws ServletException {
        try {
            orderService = new OrderService();
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Command starts");

        String statusName = request.getParameter("status");
        log.trace("Get request parameter: status"+statusName);

        String orderIdString = request.getParameter("orderId");
        log.trace("Get request parameter: orderId"+orderIdString);

        try {
            Order order = orderService.getOrder(orderIdString);
            log.trace("Get from Service by id"+orderIdString+": order --> " + order);
            OrderStatus newStatus = OrderStatus.getOrderStatus(statusName);
            orderService.updateOrder(order.getId(), newStatus);
            log.debug("Order was updated");
        } catch (DBException e) {
            e.printStackTrace();
        }
        log.debug("Command finished");
        return "/controller?command=adminList";
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
