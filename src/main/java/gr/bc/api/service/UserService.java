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
 * @author Konstantinos Raptis
 */
@Service
public class UserService {
    
    @Autowired
    @Qualifier("MySQLUser")
    private IUserDao userDao;
    
    public User saveUser(User user) {
        return userDao.saveUser(user);
    }
    
    public boolean updateUser(User user) {
        return userDao.updateUser(user);
    }
    
    public boolean deleteUserById(long id) {
        return userDao.deleteUserById(id);
    }
    
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }
        
    public List<User> findByName(String firstName, String lastName) {
        if (firstName == null) {
            return userDao.findByLastName(lastName);
        } else if (lastName == null) {
            return userDao.findByFirstName(firstName);
        } else {
            return userDao.findByFullName(firstName, lastName);
        }
    }

    public User findById(long id) {
        return userDao.findById(id);
    }

    public boolean isUserExist(long userId) {
        return userDao.isUserExist(userId);
    }
    
    public boolean isUserExist(String email) {
        return userDao.isUserExist(email);
    }
    
}
