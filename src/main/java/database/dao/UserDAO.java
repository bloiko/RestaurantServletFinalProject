package database.dao;


import database.dao.mapper.EntityMapper;
import database.entity.User;
import exception.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

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
            connection = DBManager.getInstance().getConnection();
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
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot add user to the database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
    }

    /**
     * Get user role identifier from the database
     *
     * @param theUser user to be searched.
     * @return user's role identifier.
     */
    private int getRoleId(User theUser) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DBManager.getInstance().getConnection();
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
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot get role id from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
    }

    /**
     * Get user identifier from the database
     *
     * @param user user to be searched.
     * @return User identifier.
     */
    public int getUserId(User user) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String sql = "select id from user where first_name= ? && last_name= ? && email= ? && address= ? && phone_number= ? ;";
            connection = DBManager.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getAddress());
            preparedStatement.setString(5, user.getPhoneNumber());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                return -1;
            }
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot get user id from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
    }

    /**
     * Get user by user identifier from the database
     *
     * @param userId user identifier to be searched.
     * @return user that was searched.
     */
    public User getUserByUserId(int userId) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DBManager.getInstance().getConnection();
            String sql = "SELECT u.id, u.first_name ,u.last_name,u.username,u.password, u.email,u.address,u.phone_number,role.name as role_name FROM user as u" +
                    "        INNER JOIN role on u.role_id = role.role_id" +
                    "        WHERE u.id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                UserMapper userMapper = new UserMapper();
                return userMapper.mapRow(resultSet);
            } else {
                throw new DBException("Could not find user userId: " + userId);
            }
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot get user by user id " + userId + " from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
    }

    /**
     * Get user by his username from the database
     *
     * @param theUserName user name to be searched.
     * @return user that was searched.
     */
    public User getUserByUserName(String theUserName) throws DBException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DBManager.getInstance().getConnection();
            String sql = "SELECT u.id,u.first_name,u.last_name,u.username,u.password, u.email,u.address,u.phone_number,role.name as role_name FROM user as u" +
                    "        INNER JOIN role on u.role_id = role.role_id" +
                    "        WHERE username=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, theUserName);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                UserMapper userMapper = new UserMapper();
                return userMapper.mapRow(resultSet);
            } else {
                return null;
            }
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot get user by username " + theUserName + " from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
    }

    /**
     * Extracts a user from the result set row.
     */
    public static class UserMapper implements EntityMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet) throws SQLException {
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
        }
    }
}















