package gr.bc.api.service;

import gr.bc.api.model.Credentials;
import gr.bc.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import gr.bc.api.dao.UserDao;
import gr.bc.api.service.exception.BadCredentialsException;
import gr.bc.api.service.exception.ServiceException;
import gr.bc.api.util.Constants;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Konstantinos Raptis
 */
@Service
public class UserService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    
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
    
    public User authenticateByToken(String authToken) throws BadCredentialsException {
        Credentials crs = Credentials.getCredentials(authToken);
        return authenticateByCredentials(crs);
    }
    
    public User authenticateByCredentials(Credentials crs) throws BadCredentialsException {
        
        String newToken;
        
        if ((newToken = userDao.authenticate(crs)) != null) {
            User theUser = findByEmail(crs.getUsername());
            theUser.setToken(newToken);
            return theUser;
        } else {
            throw new BadCredentialsException("Unauthorized access");
        }

    }
    
}
