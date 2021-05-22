package web.command.admin;

import database.dao.OrderDAO;
import database.entity.Order;
import database.entity.OrderStatus;
import exception.DBException;
import service.OrderService;
import web.command.Command;

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
public class AdminListCommand extends Command {
    private OrderDAO orderListDAO;
    private OrderService orderService;
    public AdminListCommand() throws ServletException {
        init();
    }
    @Override
    public void init() throws ServletException {
        try {
            orderService = new OrderService();
            orderListDAO = OrderDAO.getInstance();
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<OrderStatus> orderStatuses = orderListDAO.getStatuses();
            List<Order> notDoneOrders = orderService.getNotDoneOrdersSortById();
            List<Order> doneOrders = orderService.getDoneOrders();

            request.setAttribute("statusList", orderStatuses);
            request.setAttribute("NOT_DONE_ORDERS_LIST", notDoneOrders);
            request.setAttribute("DONE_ORDERS_LIST", doneOrders);
        } catch (DBException e) {
            e.printStackTrace();
        }
        return  "admin.jsp";
    }

    public void setOrderListDAO(OrderDAO orderListDAO) {
        this.orderListDAO = orderListDAO;
    }
}
