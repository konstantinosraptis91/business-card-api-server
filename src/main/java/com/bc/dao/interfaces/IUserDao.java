/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bc.dao.interfaces;

import com.bc.entity.User;

/**
 *
 * @author konstantinos
 */
public interface IUserDao {
    
    int addUser(User user);
    
    User getUserByEmail(String email);
      
}
