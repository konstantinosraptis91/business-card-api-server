package gr.bc.api.controller;

import gr.bc.api.model.Credentials;
import gr.bc.api.model.User;
import gr.bc.api.service.UserService;
import gr.bc.api.util.Constants;
import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Konstantinos Raptis
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
//    @Autowired
//    private StorageService storageService;  

    // Save user (Create Account)    
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> saveUser(@Valid @RequestBody User user,
            /*@RequestParam(value = "file", required = false) MultipartFile file,*/
            UriComponentsBuilder ucBuilder) {

        User theUser;
        try {
            theUser = userService.saveUser(user);
        } catch (DataAccessException ex) {
            LOGGER.error("saveUser: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        LOGGER.info("User " + user.toString() + " created", Constants.LOG_DATE_FORMAT.format(new Date()));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(theUser.getId()).toUri());
        
        theUser.setPassword(null);
        theUser.setEmail(null);
        
        return new ResponseEntity<>(theUser.getToken(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/authenticate",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity<User> authenticate(@RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
        Credentials crs = Credentials.getCredentials(authToken);
        String newToken;
        
        if ((newToken = userService.authenticate(crs)) != null) {
            User theUser = userService.findByEmail(crs.getUsername());
            
            theUser.setToken(newToken);
            theUser.setPassword(null);
            theUser.setEmail(null);
            
            return new ResponseEntity<>(theUser, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // Update user (Update Account)
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUser(@PathVariable("id") long id,
            @Valid @RequestBody User user,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
        boolean response;
        User theUser;

        try {
            theUser = userService.findById(id);

            // if tokens are equal then autorized to proceed with update
            if (theUser.getToken().equals(authToken)) {
                response = userService.updateUser(id, user);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (DataAccessException ex) {
            LOGGER.error("updateUser: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            if (ex instanceof EmptyResultDataAccessException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (response) {
            LOGGER.info("User " + id + " updated", Constants.LOG_DATE_FORMAT.format(new Date()));
        }

        return response ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Delete user (Delete Account)
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") long id,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
        boolean response;
        User theUser;

        try {
            theUser = userService.findById(id);

            // if tokens are equal then autorized to proceed with deletion
            if (theUser.getToken().equals(authToken)) {
                response = userService.deleteUserById(id);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (DataAccessException ex) {
            LOGGER.error("deleteUserById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            if (ex instanceof EmptyResultDataAccessException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (response) {
            LOGGER.info("User " + id + " deleted", Constants.LOG_DATE_FORMAT.format(new Date()));
        }

        return response ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Get user by id
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> findById(@PathVariable("id") long id,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
        User theUser;

        try {
            theUser = userService.findById(id);

            // Check if asking User has the right to claim asked user 
            if (theUser.getToken().equals(authToken)) {

                User response = new User();
                response.setId(theUser.getId());
                response.setFirstName(theUser.getFirstName());
                response.setLastName(theUser.getLastName());
                response.setLastUpdated(theUser.getLastUpdated());
                response.setCreatedAt(theUser.getCreatedAt());

                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (DataAccessException ex) {
            LOGGER.error("findById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            if (ex instanceof EmptyResultDataAccessException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

//    @ExceptionHandler(StorageFileNotFoundException.class)
//    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
//        return ResponseEntity.notFound().build();
//    }
}
