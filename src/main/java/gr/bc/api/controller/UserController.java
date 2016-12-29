/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.controller;

//import gr.bc.api.entity.Credentials;
import gr.bc.api.entity.User;
import gr.bc.api.service.UserService;
import gr.bc.api.util.Constants;
//import gr.bc.api.util.Constants;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
//import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
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
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user, UriComponentsBuilder ucBuilder) {
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
    public ResponseEntity<Void> updateUser(@Valid @RequestBody User user) {
        LOGGER.info("Updating user with id " + user.getId(), Constants.LOG_DATE_FORMAT.format(new Date()));
        // Check if user who is being updated actually exists
        if (!userService.isUserExist(user.getId())) {
            LOGGER.info("Unable to update user with id " + user.getId() + ".User not found", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            // Check if user who is being updated got the same email with a user in database    
        } else if (userService.isUserExist(user.getEmail())
                && userService.findByEmail(user.getEmail()).getId() != user.getId()) {
            LOGGER.info("User with email " + user.getEmail() + " already exists", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return userService.updateUser(user) ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CONFLICT); 
    }

    // Delete user (Delete Account)
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") long id) {
        LOGGER.info("Deleting user with id " + id, Constants.LOG_DATE_FORMAT.format(new Date()));
        if (!userService.isUserExist(id)) {
            LOGGER.info("Unable to delete user with id " + id + ".User not found", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return userService.deleteUserById(id) ? new ResponseEntity<>(HttpStatus.OK) 
                : new ResponseEntity<>(HttpStatus.CONFLICT); 
    }

    // Get users by email or first name and/or last name
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> find(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName
    /*@RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken*/) {
        // Credentials crs = Credentials.getCredentials(authToken);
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
    public ResponseEntity<User> findById(@PathVariable("id") long id) {
        return userService.isUserExist(id) ? new ResponseEntity<>(userService.findById(id), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
