package dao;

import entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserListDAO {
    private List<User> userList;
    private static UserListDAO instance;

    private UserListDAO() {
        userList = new ArrayList<>();

    }

    public static UserListDAO getInstance() {
        if (instance == null) {
            return instance = new UserListDAO();
        } else return instance;
    }

    public boolean isCorrectUser(String userName,String password){
        try {
            List<User> userList = getUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (User user : userList){
            if(user.getUserName().equals(userName) && user.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }
    public List<User> getUsers() throws Exception {
        return userList;

    }

    public void addUser(User theUser) throws Exception {
        userList.add(theUser);
    }

    public User getUser(String theUserId) throws Exception {
        long userId = Integer.parseInt(theUserId);
        for (User User : userList) {
            if (User.getId() == userId) {
                return User;
            }
        }
        return null;
    }
    public User getUserByUserName(String userName) throws Exception {
        for (User User : userList) {
            if (User.getUserName().equals(userName)) {
                return User;
            }
        }
        return null;
    }
    public void updateUser(User theUser) throws Exception {
        for (User User : userList) {
            if (User.getId() == theUser.getId()) {
                User = theUser;
            }
        }

    }

    public void deleteUser(String theUserId) throws Exception {
        int id = Integer.parseInt(theUserId);
        User userToDelete = null;
        for (User user : userList) {
            if (user.getId() == id) {
                userToDelete = user;
            }
        }
        if (userToDelete != null) {
            userList.remove(userToDelete);
        }
    }
}















