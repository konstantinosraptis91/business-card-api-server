/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.dao.interfaces;

import gr.bc.api.entity.User;

/**
 *
 * @author konstantinos
 */
public interface IUserDao {
    
    int addUser(User user);
    
    User getUserByEmail(String email);

    int updateUser(User user);

    int deleteUser(String email);

    User getUserByName(String firstName, String lastName);
      
}
