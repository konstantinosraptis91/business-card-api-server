package gr.bc.api.dao;

import gr.bc.api.model.Credentials;
import gr.bc.api.model.User;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author Konstantinos Raptis
 */
public interface UserDao {
    
    User saveUser(User user) throws DataAccessException;
        
    User findById(long id) throws DataAccessException;
    
    User findByEmail(String email) throws DataAccessException;
    
    String authenticate(Credentials credentials) throws DataAccessException;
    
    boolean updateUser(long id, User user) throws DataAccessException;
    
    boolean deleteUserById(long id) throws DataAccessException;
    
}
