package dao;

import entity.*;

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
    private DataSource dataSource;
    private static volatile UserDAO instance;

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
        UserDAO localInstance = instance;
        if (localInstance == null) {
            synchronized (UserDAO.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UserDAO();
                }
            }
        }
        return localInstance;
    }

    public boolean isCorrectUser(String userName, String password) {
        User user = null;
        try {
            user = getUserByUserName(userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null && user.getUserName().equals(userName) && user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public boolean isCorrectAdmin(String userName, String password) {
        User user = null;
        try {
            user = getUserByUserName(userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null && user.getUserName().equals(userName) && user.getPassword().equals(password) && user.getRole().equals("ADMIN")) {
            return true;
        }
        return false;
    }

    public List<User> getUsers() throws Exception {
        List<User> list = new ArrayList<>();
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "SELECT u.id,u.first_name,u.last_name,u.username,u.password, u.email,u.address,u.phone_number,role.name as role_name FROM user as u" +
                    "   INNER JOIN role on u.id = role.role_id;";
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
                String phoneNumber = myRs.getString("phone_number");
                String role = myRs.getString("role_name");


                User user = new User(id, firstName, lastName, userName, password, email, address, phoneNumber, role);
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
            int roleId = getRoleId(theUser);
            myConn = dataSource.getConnection();
            String sql = "insert into user "
                    + "(first_name,last_name,username,password, email,address,phone_number,role_id) "
                    + "values ( ?, ?, ?, ?, ?, ?, ?, ?)";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setString(1, theUser.getFirstName());
            myStmt.setString(2, theUser.getLastName());
            myStmt.setString(3, theUser.getUserName());
            myStmt.setString(4, theUser.getPassword());
            myStmt.setString(5, theUser.getEmail());
            myStmt.setString(6, theUser.getAddress());
            myStmt.setString(7, theUser.getPhoneNumber());
            myStmt.setInt(8, roleId);
            myStmt.execute();
        } finally {
            close(myConn, myStmt, null);
        }
    }

    private int getRoleId(User theUser) throws Exception {
        Connection myConn = dataSource.getConnection();
        String sql;
        ResultSet resultSet;
        PreparedStatement myStmtRole;
        sql = "select role_id from role where name= ? ;";
        myStmtRole = myConn.prepareStatement(sql);
        myStmtRole.setString(1, theUser.getRole());
        resultSet = myStmtRole.executeQuery();
        int roleId = 0;
        if (resultSet.next()) {
            roleId = resultSet.getInt("role_id");
        } else {
            throw new Exception("Could not find user id: " + roleId);
        }
        return roleId;
    }

    public int getUserId(User theUser) throws Exception {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            String sql = "select id from user where first_name= ? && last_name= ? && email= ?&& address= ?&& phone_number= ? ;";
            myConn = dataSource.getConnection();
            PreparedStatement myStmtRole = myConn.prepareStatement(sql);
            myStmtRole.setString(1, theUser.getFirstName());
            myStmtRole.setString(2, theUser.getLastName());
            myStmtRole.setString(3, theUser.getEmail());
            myStmtRole.setString(4, theUser.getAddress());
            myStmtRole.setString(5 , theUser.getPhoneNumber());
            ResultSet resultSet = myStmtRole.executeQuery();
            int userId = -1;
            if (resultSet.next()) {
                userId = resultSet.getInt("id");
            }
            return userId;
        } finally {
            close(myConn, myStmt, null);
        }
    }
    public User getUserByUserId(int userId) throws Exception {

        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "SELECT u.id,u.first_name,u.last_name,u.username,u.password, u.email,u.address,u.phone_number,role.name as role_name FROM user as u" +
                    "        INNER JOIN role on u.role_id = role.role_id" +
                    "        WHERE u.id=?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, userId);
            myRs = myStmt.executeQuery();
            if (myRs.next()) {
                int id = myRs.getInt("id");
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String userName = myRs.getString("username");
                String password = myRs.getString("password");
                String email = myRs.getString("email");
                String address = myRs.getString("address");
                String phoneNumber = myRs.getString("phone_number");
                String role = myRs.getString("role_name");
                User user = new User(id, firstName, lastName, userName, password, email, address, phoneNumber, role);
                return user;
            } else {
                throw new Exception("Could not find user userId: " + userId);
            }
        } finally {
            close(myConn, myStmt, myRs);
        }
    }
    public User getUserByUserName(String theUserName) throws Exception {

        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "SELECT u.id,u.first_name,u.last_name,u.username,u.password, u.email,u.address,u.phone_number,role.name as role_name FROM user as u" +
                    "        INNER JOIN role on u.role_id = role.role_id" +
                    "        WHERE username=?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setString(1, theUserName);
            myRs = myStmt.executeQuery();
            if (myRs.next()) {
                int id = myRs.getInt("id");
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String userName = myRs.getString("username");
                String password = myRs.getString("password");
                String email = myRs.getString("email");
                String address = myRs.getString("address");
                String phoneNumber = myRs.getString("phone_number");
                String role = myRs.getString("role_name");
                User user = new User(id, firstName, lastName, userName, password, email, address, phoneNumber, role);
                return user;
            } else {
                throw new Exception("Could not find user userName: " + theUserName);
            }
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















