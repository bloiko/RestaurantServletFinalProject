package dao;

import entity.FoodItem;
import entity.User;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private String sqlTable = "jdbc/restaurant_system";
    //@Resource(name = "jdbc/restaurant_system")
    private DataSource dataSource;
    private static UserDAO instance;

    private UserDAO() {
        Context initContext = null;
        try {
            initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            this.dataSource = (DataSource) envContext.lookup("jdbc/restaurant_system");

        } catch (
                NamingException e) {
            e.printStackTrace();
        }
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            return instance = new UserDAO();
        } else return instance;
    }

    public boolean isCorrectUser(String userName, String password) {
        List<User> userList = new ArrayList<>();
        try {
            userList = getUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (User user : userList) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public List<User> getUsers() throws Exception {
        List<User> list = new ArrayList<>();
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;
        String myConnectionString = "jdbc:mysql://localhost:3306/restaurant_system?useSSL=false&serverTimezone=UTC";
        try {
            myConn = dataSource.getConnection();
            String sql = "SELECT u.id,u.first_name,u.last_name,u.username,u.password, u.email,u.address,u.phoneNumber,role.name as role_name FROM user as u\n" +
                    "   INNER JOIN user_role on u.id=user_role.user_id\n" +
                    "   INNER JOIN role on user_role.role_id = role.role_id;";
            myStmt = myConn.createStatement();
            myRs = myStmt.executeQuery(sql);
            while (myRs.next()) {
                int id = myRs.getInt("id");
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String userName = myRs.getString("username");
                String password = myRs.getString("password");
                String email = myRs.getString("email");
                String address = myRs.getString("address");
                String phoneNumber = myRs.getString("phoneNumber");
                String role = myRs.getString("role_name");

                List<String> roles = new ArrayList<>();
                roles.add(role);
                User user = new User(id, firstName, lastName, userName, password, email, address, phoneNumber, roles);
                list.add(user);
            }
            return list;
        } finally {
            close(myConn, myStmt, myRs);
        }

    }

    private void close(Connection myConn, Statement myStmt, ResultSet myRs) {

        try {
            if (myRs != null) {
                myRs.close();
            }

            if (myStmt != null) {
                myStmt.close();
            }

            if (myConn != null) {
                myConn.close();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public void addUser(User theUser) throws Exception {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "insert into user "
                    + "(id,first_name,last_name,username,password, email,address,phoneNumber) "
                    + "values (?, ?, ?, ?,?,?,?,?)";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, theUser.getId());
            myStmt.setString(2, theUser.getFirstName());
            myStmt.setString(3, theUser.getLastName());
            myStmt.setString(4, theUser.getUserName());
            myStmt.setString(5, theUser.getPassword());
            myStmt.setString(6, theUser.getEmail());
            myStmt.setString(7, theUser.getAddress());
            myStmt.setString(8, theUser.getPhoneNumber());
            myStmt.execute();
        } finally {
            close(myConn, myStmt, null);
        }
    }
/*
    public User getUser(String theUserId) throws Exception {
        long userId = Integer.parseInt(theUserId);
        for (User User : userList) {
            if (User.getId() == userId) {
                return User;
            }
        }
        return null;
    }*/


    public User getUserByUserName(String theUserName) throws Exception {

        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "SELECT u.id,u.first_name,u.last_name,u.username,u.password, u.email,u.address,u.phoneNumber,role.name as role_name FROM user as u" +
                    "        INNER JOIN user_role on u.id=user_role.user_id" +
                    "        INNER JOIN role on user_role.role_id = role.role_id" +
                    "        WHERE username=?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setString(1, theUserName);
            myRs = myStmt.executeQuery(sql);
            int id = myRs.getInt("id");
            String firstName = myRs.getString("first_name");
            String lastName = myRs.getString("last_name");
            String userName = myRs.getString("username");
            String password = myRs.getString("password");
            String email = myRs.getString("email");
            String address = myRs.getString("address");
            String phoneNumber = myRs.getString("phoneNumber");
            String role = myRs.getString("role_name");

            List<String> roles = new ArrayList<>();
            roles.add(role);
            User user = new User(id, firstName, lastName, userName, password, email, address, phoneNumber, roles);
            return user;
        } finally {
            close(myConn, myStmt, myRs);
        }
    }

//    public void updateUser(User theUser) throws Exception {
//        for (User User : userList) {
//            if (User.getId() == theUser.getId()) {
//                User = theUser;
//            }
//        }
//
//    }
//
//    public void deleteUser(String theUserId) throws Exception {
//        int id = Integer.parseInt(theUserId);
//        User userToDelete = null;
//        for (User user : userList) {
//            if (user.getId() == id) {
//                userToDelete = user;
//            }
//        }
//        if (userToDelete != null) {
//            userList.remove(userToDelete);
//        }
//    }
}















