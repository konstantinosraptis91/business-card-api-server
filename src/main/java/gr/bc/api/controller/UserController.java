package gr.bc.api.controller;

import gr.bc.api.model.BCWResponse;
import gr.bc.api.model.Credentials;
import gr.bc.api.model.User;
import gr.bc.api.service.UserService;
import gr.bc.api.util.Constants;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    // Save user (Create Account)    
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BCWResponse> saveUser(@Valid @RequestBody User user, UriComponentsBuilder ucBuilder) {
        LOGGER.info("Creating " + user, Constants.LOG_DATE_FORMAT.format(new Date()));
        // check if user with the same email already exist
        if (userService.isUserExist(user.getEmail())) {
            LOGGER.info("User with email " + user.getEmail() + " already exists", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        User response = userService.saveUser(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(response.getId()).toUri());
        return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
    }

    // Update user (Update Account)
    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BCWResponse> updateUser(@Valid @RequestBody User user,
            @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
        Credentials crs = Credentials.getCredentials(authToken);
        
        // Check if user who asking for content actually exists
        if (userService.isUserExist(crs.getUsername(), crs.getPassword())) {
                    
            // Check if user who is being updated actually exists
            if (userService.isUserExist(user.getId())) {
                User theUser = userService.findById(user.getId());
                                
                // Check if user who is being updated got the same email with a user in database    
                if (!(userService.isUserExist(user.getEmail()) && 
                        (userService.findByEmail(user.getEmail()).getId() != user.getId()))) {
                    
                    // Check if asking User has the right to update asked user 
                    if (theUser.getEmail().equals(crs.getUsername()) && theUser.getPassword().equals(crs.getPassword())) {
                        boolean result = userService.updateUser(user);
                        return result ? new ResponseEntity<>(new BCWResponse(String.valueOf(result)), HttpStatus.OK)
                                : new ResponseEntity<>(new BCWResponse(String.valueOf(result)), HttpStatus.CONFLICT);
                    } else {
                        return new ResponseEntity<>(new BCWResponse("Forbidden Access."), HttpStatus.FORBIDDEN);
                    }
                             
                } else {
                    LOGGER.info("User with email " + user.getEmail() + " already exists", Constants.LOG_DATE_FORMAT.format(new Date()));
                    return new ResponseEntity<>(new BCWResponse("User with email " + user.getEmail() + " already exists"), HttpStatus.CONFLICT);
                } 
                
                // Check if user who is being updated got the same email with a user in database    
            } else {
                LOGGER.info("Unable to update user with id " + user.getId() + ".User not found", Constants.LOG_DATE_FORMAT.format(new Date()));
                return new ResponseEntity<>(new BCWResponse("Unable to update user with id " + user.getId() + ".User not found"), HttpStatus.NOT_FOUND);
            }
            
        }
        
        return new ResponseEntity<>(new BCWResponse("Bad Credentials. Access Denied") ,HttpStatus.UNAUTHORIZED);
    }

    // Delete user (Delete Account)
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<BCWResponse> deleteUserById(@PathVariable("id") long id, 
            @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
        Credentials crs = Credentials.getCredentials(authToken);
        
        // Check if user who asking for content actually exists
        if (userService.isUserExist(crs.getUsername(), crs.getPassword())) {
            
            // Check if asked for deletion user actually exists 
            if (userService.isUserExist(id)) {
                User theUser = userService.findById(id);
                
                // Check if asking User has the right to delete asked user 
                if (theUser.getEmail().equals(crs.getUsername()) && theUser.getPassword().equals(crs.getPassword())) {
                    LOGGER.info("Deleting user with id " + id, Constants.LOG_DATE_FORMAT.format(new Date()));
                    boolean result = userService.deleteUserById(id);
                    return result ? new ResponseEntity<>(new BCWResponse(String.valueOf(result)), HttpStatus.OK)
                : new ResponseEntity<>(new BCWResponse(String.valueOf(result)), HttpStatus.CONFLICT);
                } else {
                    return new ResponseEntity<>(new BCWResponse("Forbidden Access."), HttpStatus.FORBIDDEN);
                }
                
            } else {
                LOGGER.info("Unable to delete user with id " + id + ". User not found", Constants.LOG_DATE_FORMAT.format(new Date()));
                return new ResponseEntity<>(new BCWResponse("Unable to delete user with id " + id + ". User not found"), HttpStatus.NOT_FOUND);
            }
        
        }
        
        return new ResponseEntity<>(new BCWResponse("Bad Credentials. Access Denied") ,HttpStatus.UNAUTHORIZED);
    }

    // Get users by email or first name and/or last name
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> find(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
        Credentials crs = Credentials.getCredentials(authToken);
        List<User> users = new ArrayList<>();
        if (email != null) {
            if (userService.isUserExist(email)) {
                users.add(userService.findByEmail(email));
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            users = userService.findByName(firstName, lastName);
        }
        return users.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Get user by id
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BCWResponse> findById(@PathVariable("id") long id,
            @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
        Credentials crs = Credentials.getCredentials(authToken);
        
        // Check if user who asking for content actually exists
        if (userService.isUserExist(crs.getUsername(), crs.getPassword())) {
            
            // Check if asked user actually exists 
            if (userService.isUserExist(id)) {
                User theUser = userService.findById(id);
                
                // Check if asking User has the right to claim asked user 
                if (theUser.getEmail().equals(crs.getUsername()) && theUser.getPassword().equals(crs.getPassword())) {
                    return new ResponseEntity<>(theUser, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new BCWResponse("Forbidden Access."), HttpStatus.FORBIDDEN);
                }
                
            } else {
                return new ResponseEntity<>(new BCWResponse("Unable to Find User with Id " + id), HttpStatus.NOT_FOUND);
            }
        }
        
        return new ResponseEntity<>(new BCWResponse("Bad Credentials. Access Denied") ,HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(
            value = "/authenticate",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BCWResponse> authenticate(
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "password", required = true) String password) {
        Boolean result = false;
        if (email != null && password != null) {
            if (userService.isUserExist(email, password)) {
                result = true;
            }
        }
        return new ResponseEntity<>(new BCWResponse(result.toString()), HttpStatus.OK);
    }

}
