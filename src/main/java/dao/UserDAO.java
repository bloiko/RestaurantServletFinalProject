package dao;

import entity.*;
import exception.DBException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private DataSource dataSource;
    private static volatile UserDAO instance;

    private UserDAO() throws DBException {
        Context initContext = null;
        try {
            initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            this.dataSource = (DataSource) envContext.lookup("jdbc/restaurant_system");

        } catch (NamingException e) {
            throw new DBException("Cannot connect to the database", e);
        }
    }

    public static UserDAO getInstance() throws DBException {
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

/*    public boolean isCorrectUser(String userName, String password) throws DBException {
        User user = null;
        user = getUserByUserName(userName);
        if (user != null && user.getUserName().equals(userName) && user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }*/

    public boolean isCorrectAdmin(String userName, String password) throws DBException {
        User user = null;
        user = getUserByUserName(userName);
        return user != null && user.getUserName().equals(userName) && user.getPassword().equals(password) && user.getRole().equals("ADMIN");
    }

   /* public List<User> getUsers() throws Exception {
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

    }*/

    private void close(Connection myConn, Statement myStmt, ResultSet myRs) throws DBException {

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
        } catch (SQLException throwables) {
            throw new DBException("Cannot close all conections to the database", throwables);
        }
    }

    public void addUser(User theUser) throws DBException {
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
        } catch (SQLException throwables) {
            throw new DBException("Cannot add user to the database", throwables);
        } finally {
            close(myConn, myStmt, null);
        }
    }

    private int getRoleId(User theUser) throws DBException {
        try {
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
                throw new DBException("Could not find user id: " + roleId);
            }
            return roleId;
        } catch (SQLException throwables) {
            throw new DBException("Cannot get role id from database", throwables);
        }
    }

    public int getUserId(User theUser) throws DBException {
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
            myStmtRole.setString(5, theUser.getPhoneNumber());
            ResultSet resultSet = myStmtRole.executeQuery();
            int userId = -1;
            if (resultSet.next()) {
                userId = resultSet.getInt("id");
            }
            return userId;
        } catch (SQLException throwables) {
            throw new DBException("Cannot get user id from database", throwables);
        } finally {
            close(myConn, myStmt, null);
        }
    }

    public User getUserByUserId(int userId) throws DBException {

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
                return new User(id, firstName, lastName, userName, password, email, address, phoneNumber, role);
            } else {
                throw new DBException("Could not find user userId: " + userId);
            }
        } catch (SQLException throwables) {
            throw new DBException("Cannot get user by user id " + userId + " from database", throwables);
        } finally {
            close(myConn, myStmt, myRs);
        }
    }

    public User getUserByUserName(String theUserName) throws DBException {

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
                return new User(id, firstName, lastName, userName, password, email, address, phoneNumber, role);
            } else {
                throw new DBException("Could not find user userName: " + theUserName);
            }
        } catch (SQLException throwables) {
            throw new DBException("Cannot get user by username " + theUserName + " from database", throwables);
        } finally {
            close(myConn, myStmt, myRs);
        }
    }
}















