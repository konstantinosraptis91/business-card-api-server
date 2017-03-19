package gr.bc.api.dao.interfaces;

import gr.bc.api.model.Credentials;
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

    User findByToken(String token);
    
    User saveUser(User user);
    
    String authenticate(Credentials credentials);
    
    boolean updateUser(User user);
    
    boolean deleteUserById(long id);
    
    boolean isUserExistById(long id);
    
    boolean isUserExistByEmail(String email);
    
    boolean isUserExistByToken(String token);
    
    boolean isUserExistByCredentials(Credentials credentials);
    
}
