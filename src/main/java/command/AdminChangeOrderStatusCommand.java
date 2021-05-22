package command;

import dao.OrderDAO;
import entity.Order;
import entity.OrderStatus;
import exception.DBException;
import service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Admin controller.
 *
 * @author B.Loiko
 */
public class AdminChangeOrderStatusCommand extends Command {
    private OrderDAO orderListDAO;

    public AdminChangeOrderStatusCommand() throws ServletException {
        init();
    }

    @Override
    public void init() throws ServletException {
        try {
            orderListDAO = OrderDAO.getInstance();
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String statusName = request.getParameter("status");
        String orderIdString = request.getParameter("orderId");
        Order order;
        try {
            order = orderListDAO.getOrder(orderIdString);
            OrderStatus newStatus = OrderStatus.getOrderStatus(statusName);
            orderListDAO.updateOrder(order.getId(), newStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
       return "/controller?command=adminList";
    }

    public void setOrderListDAO(OrderDAO orderListDAO) {
        this.orderListDAO = orderListDAO;
    }
}
