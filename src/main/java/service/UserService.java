package service;


import database.dao.OrderDAO;
import database.dao.UserDAO;
import database.entity.Order;
import database.entity.User;
import exception.DBException;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService {
    private UserDAO userDAO;
    private OrderDAO orderListDAO;

    public UserService(UserDAO userDAO, OrderDAO orderListDAO) {
        this.userDAO = userDAO;
        this.orderListDAO = orderListDAO;
    }

    public UserService() throws DBException {
        this.userDAO = new UserDAO();
        orderListDAO = new OrderDAO();
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

    public List<Order> getUserOrdersSortByOrderDateReversed(String username) throws DBException {
        int userId = userDAO.getUserByUserName(username).getId();
        List<Order> orders = orderListDAO.getOrdersByUserId(userId);
        orders.sort(Comparator.comparing(Order::getOrderDate).reversed());
        return orders;
    }
    public int addUserAndReturnId(User user) throws DBException {
        int userId = userDAO.getUserId(user);
        if (userId == -1) {
            userDAO.addUser(user);
        }
        return userDAO.getUserId(user);

    }
    public boolean isCorrectPhoneNumber(String phoneNumber){
        String patterns = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
    public boolean isCorrectEmail(String email){
        String patterns = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isCorrectUser(String userName, String password) throws DBException {
        User user = userDAO.getUserByUserName(userName);
        return user != null && user.getUserName().equals(userName) && user.getPassword().equals(password)
                && (user.getRole().equals("USER")||user.getRole().equals("ADMIN"));
    }
    public User getUserByUserName(String username) throws DBException {
        return userDAO.getUserByUserName(username);
    }
}
