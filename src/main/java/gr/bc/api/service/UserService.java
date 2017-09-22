package gr.bc.api.service;

import gr.bc.api.model.Credentials;
import gr.bc.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import gr.bc.api.dao.UserDao;
import gr.bc.api.service.exception.ConflictException;
import gr.bc.api.service.exception.NotFoundException;
import gr.bc.api.service.exception.UnauthorizedException;
import gr.bc.api.service.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

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
        
    public User saveUser(User user) throws ConflictException {
        
        User theUser;
        
        try {
            theUser = userDao.saveUser(user);
            // setting those fields to null for security reasons
            theUser.setPassword(null);
            theUser.setEmail(null);           
        } catch (DataAccessException ex) {
            throw new ConflictException(ex.getMessage());
        }
        
        return theUser;
    }
    
    public boolean updateUser(long id, User user, String authToken) throws ServiceException {
        
        boolean response;
        User theUser;

        try {
            // this is the user any update will affect
            theUser = findById(id);

            // if tokens are equal then autorized to proceed with update
            if (theUser.getToken().equals(authToken)) {
                response = userDao.updateUser(id, user);
            } else {
                throw new UnauthorizedException("User's token does not match");
            }

        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }
                
        return response;
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
        
    /**
     * By giving valid a valid token you get back the user with a new token
     * 
     * @param authToken
     * @return user with new token
     * @throws UnauthorizedException 
     */
    public User authenticateByToken(String authToken) throws UnauthorizedException {
        Credentials crs = Credentials.getCredentials(authToken);
        return authenticateByCredentials(crs);
    }
    
    /**
     * By giving valid a valid credentials you get back the user with a new token
     * 
     * @param crs
     * @return user with new token
     * @throws UnauthorizedException 
     */
    public User authenticateByCredentials(Credentials crs) throws UnauthorizedException {
        
        String newToken;
        User theUser;
        
        if ((newToken = userDao.authenticate(crs)) != null) {
            theUser = findByEmail(crs.getUsername());
            theUser.setToken(newToken);
        } else {
            throw new UnauthorizedException("Unauthorized access");
        }
        
        return theUser;
    }
    
}
