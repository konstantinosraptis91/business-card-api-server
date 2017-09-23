package gr.bc.api.service;

import gr.bc.api.model.authentication.Credentials;
import gr.bc.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import gr.bc.api.dao.UserDao;
import gr.bc.api.model.authentication.TokenProperties;
import gr.bc.api.service.exception.ConflictException;
import gr.bc.api.service.exception.NotFoundException;
import gr.bc.api.service.exception.UnauthorizedException;
import gr.bc.api.service.exception.ServiceException;
import gr.bc.api.util.CredentialsUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 *
 * @author Konstantinos Raptis
 */
@Service
public class UserService {

    @Autowired
    @Qualifier("MySQLUser")
    private UserDao userDao;

    /**
     * Save a user (create account)
     *
     * @param user The user to be saved
     * @return
     * @throws ConflictException
     */
    public User saveUser(User user) throws ConflictException {

        User u;

        try {
            u = userDao.saveUser(user);
        } catch (DataAccessException ex) {
            throw new ConflictException(ex.getMessage());
        }

        return u;
    }

    /**
     * Update a user (update account)
     *
     * @param id User id of the user to be updated
     * @param token User token of the user to be updated
     * @param u User to be updated
     * @return
     * @throws ServiceException
     */
    public boolean updateUser(long id, String token, User u) throws ServiceException {

        boolean result;

        try {
            // authorize user
            authorizeUser(id, token);
            // update user
            result = userDao.updateUser(id, u);

        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return result;
    }

    /**
     * Delete User (delete account)
     *
     * @param id User id of the user to be deleted
     * @param token User token of the user to be deleted
     * @return
     * @throws ServiceException
     */
    public boolean deleteUserById(long id, String token) throws ServiceException {

        boolean result;

        try {
            // authorize user
            authorizeUser(id, token);
            // delete user
            result = userDao.deleteUserById(id);

        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return result;
    }

    /**
     * Find user
     *
     * @param id User id of the user to be retrieved
     * @param token User token of the user to be retrieved
     * @return
     * @throws ServiceException
     */
    public User findById(long id, String token) throws ServiceException {

        // authorize user
        authorizeUser(id, token);
        // retrieve user
        return userDao.findById(id);
    }

    /**
     * Authorize a user
     *
     * @param id User id of the user, for whom the user, who owns the token want to be authorized
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
     * @return token properties containing token and user id
     * @throws ServiceException
     */
    public TokenProperties authenticateByCredentialsToken(String credentialsToken) throws ServiceException {
        return authenticateByCredentials(CredentialsUtils.extractCredentials(credentialsToken));
    }

    /**
     * By giving a valid credentials you get back an authentication token
     *
     * @param crs The credentials object with username and password
     * @return token properties containing token and user id
     * @throws ServiceException
     */
    public TokenProperties authenticateByCredentials(Credentials crs) throws ServiceException {

        TokenProperties properties;
        String newToken;

        if ((newToken = userDao.authenticate(crs)) != null) {

            User theUser;
            properties = new TokenProperties();

            try {
                theUser = userDao.findByEmail(crs.getUsername());
                properties.setId(theUser.getId());
            } catch (DataAccessException ex) {
                throw new NotFoundException(ex.getMessage());
            }

            properties.setToken(newToken);

        } else {
            throw new UnauthorizedException("Unauthorized");
        }

        return properties;
    }

}
