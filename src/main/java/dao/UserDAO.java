package dao;

import entity.*;
import exception.DBException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

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

    /**
     * Returns  data access object. Using Singleton pattern (Double Checked Locking & volatile)
     *
     * @return data access object of the UserDAO class .
     */
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

    /**
     * Check if user with username and password is admin
     *
     * @param userName user name
     * @param password user password.
     * @return boolean if user is admin.
     */
    public boolean isCorrectAdmin(String userName, String password) throws DBException {
        User user = getUserByUserName(userName);
        return user != null && user.getUserName().equals(userName) && user.getPassword().equals(password) 
                && user.getRole().equals("ADMIN");
    }

    /**
     * Close connection, statement and resultSet of the database
     *
     * @param connection connection to be closed
     * @param statement  statement to be closed.
     * @param resultSet  Result Set to be closed.
     */
    private void close(Connection connection, Statement statement, ResultSet resultSet) throws DBException {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException throwables) {
            throw new DBException("Cannot close all conections to the database", throwables);
        }
    }

    /**
     * Add user to the database
     *
     * @param user User to de added.
     */
    public void addUser(User user) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            int roleId = getRoleId(user);
            connection = dataSource.getConnection();
            String sql = "insert into user "
                    + "(first_name,last_name,username,password, email,address,phone_number,role_id) "
                    + "values ( ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getUserName());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getAddress());
            preparedStatement.setString(7, user.getPhoneNumber());
            preparedStatement.setInt(8, roleId);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throw new DBException("Cannot add user to the database", throwables);
        } finally {
            close(connection, preparedStatement, null);
        }
    }

    /**
     * Get user role identifier from the database
     *
     * @param theUser user to be seached.
     * @return user's role identifier.
     */
    private int getRoleId(User theUser) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            String sql = "select role_id from role where name= ? ;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, theUser.getRole());
            resultSet = preparedStatement.executeQuery();
            int roleId = 0;
            if (resultSet.next()) {
                roleId = resultSet.getInt("role_id");
            } else {
                throw new DBException("Could not find user id: " + roleId);
            }
            return roleId;
        } catch (SQLException throwables) {
            throw new DBException("Cannot get role id from database", throwables);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
    }

    /**
     * Get user identifier from the database
     *
     * @param user user to be seached.
     * @return User identifier.
     */
    public int getUserId(User user) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String sql = "select id from user where first_name= ? && last_name= ? && email= ? && address= ? && phone_number= ? ;";
            connection = dataSource.getConnection();
            PreparedStatement myStmtRole = connection.prepareStatement(sql);
            myStmtRole.setString(1, user.getFirstName());
            myStmtRole.setString(2, user.getLastName());
            myStmtRole.setString(3, user.getEmail());
            myStmtRole.setString(4, user.getAddress());
            myStmtRole.setString(5, user.getPhoneNumber());
            resultSet = myStmtRole.executeQuery();
            int userId = -1;
            if (resultSet.next()) {
                userId = resultSet.getInt("id");
            }
            return userId;
        } catch (SQLException throwables) {
            throw new DBException("Cannot get user id from database", throwables);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
    }

    /**
     * Get user by user identifier from the database
     *
     * @param userId user identifier to be seached.
     * @return user that was searched.
     */
    public User getUserByUserId(int userId) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            String sql = "SELECT u.id, u.first_name ,u.last_name,u.username,u.password, u.email,u.address,u.phone_number,role.name as role_name FROM user as u" +
                    "        INNER JOIN role on u.role_id = role.role_id" +
                    "        WHERE u.id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String userName = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");
                String phoneNumber = resultSet.getString("phone_number");
                String role = resultSet.getString("role_name");
                return new User(id, firstName, lastName, userName, password, email, address, phoneNumber, role);
            } else {
                throw new DBException("Could not find user userId: " + userId);
            }
        } catch (SQLException throwables) {
            throw new DBException("Cannot get user by user id " + userId + " from database", throwables);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
    }

    /**
     * Get user by his username from the database
     *
     * @param theUserName user name to be seached.
     * @return user that was searched.
     */
    public User getUserByUserName(String theUserName) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            String sql = "SELECT u.id,u.first_name,u.last_name,u.username,u.password, u.email,u.address,u.phone_number,role.name as role_name FROM user as u" +
                    "        INNER JOIN role on u.role_id = role.role_id" +
                    "        WHERE username=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, theUserName);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String userName = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");
                String phoneNumber = resultSet.getString("phone_number");
                String role = resultSet.getString("role_name");
                return new User(id, firstName, lastName, userName, password, email, address, phoneNumber, role);
            } else {
                throw new DBException("Could not find user userName: " + theUserName);
            }
        } catch (SQLException throwables) {
            throw new DBException("Cannot get user by username " + theUserName + " from database", throwables);
        } finally {
            close(connection, preparedStatement, resultSet);
        }
    }
}















