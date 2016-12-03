/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.service;

import gr.bc.api.dao.interfaces.IUserDao;
import gr.bc.api.entity.User;
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
    
    public int addUser(User user) {
        return userDao.addUser(user);
    }
    
    public int updateUser(User user) {
        return userDao.updateUser(user);
    }
    
    public int deleteUser(String email) {
        return userDao.deleteUser(email);
    }
    
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }
        
    public User getUserByName(String firstName, String lastName) {
        return userDao.getUserByName(firstName, lastName);
    }
    
}
