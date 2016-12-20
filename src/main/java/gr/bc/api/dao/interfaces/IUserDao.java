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
        
    User getUserByEmail(String email);
    
    List<User> getUsersByName(String firstName, String lastName);
    
    User getUserById(long id);
        
    User createUser(User user);
        
    User updateUser(long id, User user);
    
    void deleteUser(long id);
    
    boolean isUserExist(long id);
    
    boolean isUserExist(String email);
    
}
