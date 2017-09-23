package gr.bc.api.service;

import gr.bc.api.model.authentication.Credentials;
import gr.bc.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import gr.bc.api.dao.UserDao;
import gr.bc.api.model.authentication.AuthToken;
import gr.bc.api.service.exception.ConflictException;
import gr.bc.api.service.exception.NotFoundException;
import gr.bc.api.service.exception.UnauthorizedException;
import gr.bc.api.service.exception.ServiceException;
import gr.bc.api.util.CredentialsUtils;
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

        User u;

        try {
            u = userDao.saveUser(user);
        } catch (DataAccessException ex) {
            throw new ConflictException(ex.getMessage());
        }

        return u;
    }

    public boolean updateUser(AuthToken authToken, User u) throws ServiceException {

        boolean result;

        try {
            // authorize user
            authorizeUser(authToken.getUserId(), authToken.getToken());
            // update user
            result = userDao.updateUser(authToken.getUserId(), u);
            
        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return result;
    }

    public boolean deleteUserById(AuthToken authToken) throws ServiceException {
        
        boolean result;

        try {
            // authorize user
            authorizeUser(authToken.getUserId(), authToken.getToken());
            // delete user
            result = userDao.deleteUserById(authToken.getUserId());
            
        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return result;
    }
    
    /**
     * Find user by the user authentication token
     * 
     * @param authToken User authToken
     * @return The user
     * @throws ServiceException
     */
    public User findById(AuthToken authToken) throws ServiceException {
              
        // authorize user
        authorizeUser(authToken.getUserId(), authToken.getToken());
        // retrieve user
        return userDao.findById(authToken.getUserId());
    }
    
    /**
     * Authorize a user to proceed
     * 
     * @param id User id of the user, for whom 
     * the user, who owns the token want to be authorized 
     * @param token User token
     * @throws NotFoundException User with provided id could't find
     */
    public void authorizeUser(long id, String token) throws ServiceException {
        
        User originalUser;
        
        // find user with id = authTokens's userId
        try {
            originalUser = userDao.findById(id);
        } catch (DataAccessException ex) {
            throw new NotFoundException(ex.getMessage());
        }
        
        // Check if retrieved's user token NOT matching with authToken token then unauthorized access
        if (!originalUser.getToken().equals(token)) {
           throw new UnauthorizedException("Unauthorized");
        }
        
    }
    
    /**
     * By giving a valid credentials token you get back an authentication token
     *
     * @param credentialsToken The credentials (username & password) as a token. Credentials token form is
     * usernameChunk:passwordChunk and uses base64 coding
     * @return authentication token containing token and user id
     * @throws ServiceException
     */
    public AuthToken authenticateByCredentialsToken(String credentialsToken) throws ServiceException {
        return authenticateByCredentials(CredentialsUtils.extractCredentials(credentialsToken));
    }

    /**
     * By giving a valid credentials you get back an authentication token
     *
     * @param crs The credentials object with username and password
     * @return authentication token containing token and user id
     * @throws ServiceException 
     */
    public AuthToken authenticateByCredentials(Credentials crs) throws ServiceException {

        AuthToken at;
        String newToken;

        if ((newToken = userDao.authenticate(crs)) != null) {

            User theUser;
            at = new AuthToken();

            try {
                theUser = userDao.findByEmail(crs.getUsername());
                at.setUserId(theUser.getId());
            } catch (DataAccessException ex) {
                throw new NotFoundException(ex.getMessage());
            }

            at.setToken(newToken);

        } else {
            throw new UnauthorizedException("Unauthorized");
        }

        return at;
    }

}
