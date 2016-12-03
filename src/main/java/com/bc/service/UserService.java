/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bc.service;

import com.bc.dao.interfaces.IUserDao;
import com.bc.entity.User;
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
    
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }
    
}
