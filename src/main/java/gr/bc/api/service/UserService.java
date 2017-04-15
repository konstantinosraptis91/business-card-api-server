package gr.bc.api.service;

import gr.bc.api.model.Credentials;
import gr.bc.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import gr.bc.api.dao.UserDao;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author Konstantinos Raptis
 */
@Service
public class UserService {
    
    @Autowired
    @Qualifier("MySQLUser")
    private UserDao userDao;
        
    public User saveUser(User user) throws DataAccessException {
        return userDao.saveUser(user);
    }
    
    public boolean updateUser(long id, User user) throws DataAccessException {
        return userDao.updateUser(id, user);
    }
    
    public boolean deleteUserById(long id) throws DataAccessException {
        return userDao.deleteUserById(id);
    }
    
    public User findByEmail(String email) throws DataAccessException {
        return userDao.findByEmail(email);
    }

    public User findById(long id) throws DataAccessException {
        return userDao.findById(id);
    }
    
    public String authenticate(Credentials credentials) throws DataAccessException {
        return userDao.authenticate(credentials);
    }
    
}
