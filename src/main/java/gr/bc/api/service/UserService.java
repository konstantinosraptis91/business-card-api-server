package gr.bc.api.service;

import gr.bc.api.dao.interfaces.IUserDao;
import gr.bc.api.model.Credentials;
import gr.bc.api.model.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Konstantinos Raptis
 */
@Service
public class UserService {
    
    @Autowired
    @Qualifier("MySQLUser")
    private IUserDao userDao;
        
    public User saveUser(User user) {
        user.init();
        return userDao.saveUser(user);
    }
    
    public boolean updateUser(User user) {
        return userDao.updateUser(user);
    }
    
    public boolean deleteUserById(long id) {
        return userDao.deleteUserById(id);
    }
    
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }
       
    public User findByToken(String token) {
        return userDao.findByToken(token);
    }
    
    public List<User> findByName(String firstName, String lastName) {
        if (firstName == null) {
            return userDao.findByLastName(lastName);
        } else if (lastName == null) {
            return userDao.findByFirstName(firstName);
        } else {
            return userDao.findByFullName(firstName, lastName);
        }
    }

    public User findById(long id) {
        return userDao.findById(id);
    }

    public boolean isUserExistById(long userId) {
        return userDao.isUserExistById(userId);
    }
    
    public boolean isUserExistByEmail(String email) {
        return userDao.isUserExistByEmail(email);
    }
    
    public boolean isUserExistByToken(String token) {
        return userDao.isUserExistByToken(token);
    }
    
    public boolean isUserExistByCredentials(Credentials credentials) {
        return userDao.isUserExistByCredentials(credentials);
    }
    
    public String authenticate(Credentials credentials) {
        return userDao.authenticate(credentials);
    }
    
}
