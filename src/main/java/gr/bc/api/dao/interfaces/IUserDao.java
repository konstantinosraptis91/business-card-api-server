/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.dao.interfaces;

import gr.bc.api.entity.User;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface IUserDao {
    
    // -------------------------------------------------------------------------
    // GET
    // -------------------------------------------------------------------------
    
    User getUserByEmail(String email);
    
    List<User> getUsersByName(String firstName, String lastName);
    
    User getUserById(long userId);
    
    // -------------------------------------------------------------------------
    // CREATE
    // -------------------------------------------------------------------------
    
    User createUser(User user);
    
    // -------------------------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------------------------
    
    User updateUser(long userId, User user);

    // -------------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------------
    
    void deleteUser(long userId);
      
}
