/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.service;

import gr.bc.api.dao.interfaces.IUserDao;
import gr.bc.api.entity.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author konstantinos
 */
@Service
public class UserService {
    
    @Autowired
    @Qualifier("MySQLUser")
    private IUserDao userDao;
    
    public User addUser(User user) {
        return userDao.addUser(user);
    }
    
    public User updateUser(long id, User user) {
        return userDao.updateUser(id, user);
    }
    
    public void deleteUser(long id) {
        userDao.deleteUser(id);
    }
    
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }
        
    public List<User> getUsersByName(String firstName, String lastName) {
        return userDao.getUsersByName(firstName, lastName);
    }

    public User getUserById(long id) {
        return userDao.getUserById(id);
    }
    
}
