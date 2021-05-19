package service;

import dao.OrderJDBCDAO;
import dao.UserDAO;
import entity.Order;
import entity.User;
import exception.DBException;

import java.util.Comparator;
import java.util.List;

public class UserService {
    private UserDAO userDAO;
    private OrderJDBCDAO orderListDAO;

    public UserService() throws DBException {
        this.userDAO = UserDAO.getInstance();
        orderListDAO = OrderJDBCDAO.getInstance();
    }

    /**
     * Check if user with username and password is admin
     *
     * @param userName user name
     * @param password user password.
     * @return boolean if user is admin.
     */
    public boolean isCorrectAdmin(String userName, String password) throws DBException {
        User user = userDAO.getUserByUserName(userName);
        return user != null && user.getUserName().equals(userName) && user.getPassword().equals(password)
                && user.getRole().equals("ADMIN");
    }

    public List<Order> getUserOrdersSortByOrderDateReversed(User user) throws DBException {
        int userId = userDAO.getUserId(user);
        List<Order> orders = orderListDAO.getOrdersByUserId(userId);
        orders.sort(Comparator.comparing(Order::getOrderDate).reversed());
        return orders;
    }
    public int addUserIfNotExistsAndReturnId(User user) throws DBException {
        int userId = userDAO.getUserId(user);
        if (userId == -1) {
            userDAO.addUser(user);
        }
        return userId;
    }
}
