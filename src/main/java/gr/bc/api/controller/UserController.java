package gr.bc.api.controller;

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
//    @Autowired
//    private StorageService storageService;  
    
    // Save user (Create Account)    
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveUser(@Valid @RequestBody User user, 
                                         /*@RequestParam(value = "file", required = false) MultipartFile file,*/
                                         UriComponentsBuilder ucBuilder) {
        LOGGER.info("Creating " + user, Constants.LOG_DATE_FORMAT.format(new Date()));
        // check if user with the same email already exist
        if (userService.isUserExistByEmail(user.getEmail())) {
            LOGGER.info("User with email " + user.getEmail() + " already exists", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        User response = userService.saveUser(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(response.getId()).toUri());
        // check if profile photo has been provided
//        if (file != null) {
//            storageService.store(file);
//        }
        return new ResponseEntity<>(response.getToken(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/authenticate",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity<String> authenticate(@RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
        Credentials crs = Credentials.getCredentials(authToken);
        String newToken;
        
        if((newToken = userService.authenticate(crs)) != null) {
            return new ResponseEntity<>(newToken, HttpStatus.OK);
        }
        
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);    
    }
    
    // Update user (Update Account)
    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateUser(@Valid @RequestBody User user,
            @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
                
        // Check if user who asking for content actually exists
        if (userService.isUserExistByToken(authToken)) {
                    
            // Check if user who is being updated actually exists
            if (userService.isUserExistById(user.getId())) {
                User theUser = userService.findById(user.getId());
                                
                // Check if user who is being updated got the same email with a user in database    
                if (!(userService.isUserExistByEmail(user.getEmail()) && 
                        (userService.findByEmail(user.getEmail()).getId() != user.getId()))) {
                    
                    // Check if asking User has the right to update asked user 
                    if (theUser.getToken().equals(authToken)) {
                        boolean result = userService.updateUser(user);
                        return result ? new ResponseEntity<>(HttpStatus.OK)
                                : new ResponseEntity<>( HttpStatus.CONFLICT);
                    } else {
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                    }
                             
                } else {
                    LOGGER.info("User with email " + user.getEmail() + " already exists", Constants.LOG_DATE_FORMAT.format(new Date()));
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                } 
                
                // Check if user who is being updated got the same email with a user in database    
            } else {
                LOGGER.info("Unable to update user with id " + user.getId() + ".User not found", Constants.LOG_DATE_FORMAT.format(new Date()));
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
        }
        
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // Delete user (Delete Account)
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteUserById(@PathVariable("id") long id, 
            @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
                
        // Check if user who asking for content actually exists
        if (userService.isUserExistByToken(authToken)) {
            
            // Check if asked for deletion user actually exists 
            if (userService.isUserExistById(id)) {
                User theUser = userService.findById(id);
                
                // Check if asking User has the right to delete asked user 
                if (theUser.getToken().equals(authToken)) {
                    LOGGER.info("Deleting user with id " + id, Constants.LOG_DATE_FORMAT.format(new Date()));
                    boolean result = userService.deleteUserById(id);
                    return result ? new ResponseEntity<>(result, HttpStatus.OK)
                : new ResponseEntity<>(result, HttpStatus.CONFLICT);
                } else {
                    return new ResponseEntity<>(Boolean.FALSE, HttpStatus.FORBIDDEN);
                }
                
            } else {
                LOGGER.info("Unable to delete user with id " + id + ". User not found", Constants.LOG_DATE_FORMAT.format(new Date()));
                return new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND);
            }
        
        }
        
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
        List<User> users = new ArrayList<>();
        
        // Check if user who asking for content actually exists 
        // 1st if
        if (userService.isUserExistByToken(authToken)) {
            
            if (email != null) {
                if (userService.isUserExistByEmail(email)) {
                    users.add(userService.findByEmail(email));
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                if (firstName != null || lastName != null) {
                    users = userService.findByName(firstName, lastName);
                } else {
                    // From 1st if we know that credentials user actualy exist, so if all
                    // request params are null, just return that user (full info)
                    users.add(userService.findByToken(authToken));
                }
            }
            
            return users.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(users, HttpStatus.OK);
            
        }
                
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        
    }

    // Get user by id
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> findById(@PathVariable("id") long id,
            @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
                
        // Check if user who asking for content actually exists
        if (userService.isUserExistByToken(authToken)) {
            
            // Check if asked user actually exists 
            if (userService.isUserExistById(id)) {
                User theUser = userService.findById(id);
                
                // Check if asking User has the right to claim asked user 
                if (theUser.getToken().equals(authToken)) {
                    return new ResponseEntity<>(theUser, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
                
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    
//    @ExceptionHandler(StorageFileNotFoundException.class)
//    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
//        return ResponseEntity.notFound().build();
//    }
    
}
