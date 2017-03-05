/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.dao.interfaces;

import gr.bc.api.model.User;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface IUserDao {
        
    User findByEmail(String email);
    
    List<User> findByFullName(String firstName, String lastName);
    
    List<User> findByFirstName(String firstName);
    
    List<User> findByLastName(String lastName);
    
    User findById(long id);
        
    User saveUser(User user);
        
    boolean updateUser(User user);
    
    boolean deleteUserById(long id);
    
    boolean isUserExist(long id);
    
    boolean isUserExist(String email);
    
    boolean isUserExist(String email, String password);
    
}
