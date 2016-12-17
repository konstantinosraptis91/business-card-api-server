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

    // Add user (Create Account)    
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
        LOGGER.info("Creating User with email " + user.getEmail(), Constants.LOG_DATE_FORMAT.format(new Date()));
        User u = userService.getUserByEmail(user.getEmail());
        if (u.getEmail() != null) {
            LOGGER.info("User with email " + user.getEmail() + " already exists", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        // reuse object u
        u = userService.createUser(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(u.getId()).toUri());
        return new ResponseEntity<>(u, headers, HttpStatus.CREATED);
    }

    // Update user (Update Account)
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        LOGGER.info("Updating User with id " + id, Constants.LOG_DATE_FORMAT.format(new Date()));
        User u1 = userService.getUserById(id);
        User u2 = userService.getUserByEmail(user.getEmail());
        if (u1.getEmail() == null) {
            LOGGER.info("Unable to update user with id " + id + ".User not found", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (u2.getEmail() != null && u2.getId() != id) {
            LOGGER.info("User with email " + user.getEmail() + " already exists", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(userService.updateUser(id, user), HttpStatus.OK);
    }

    // Delete user (Delete Account)
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteUser(@PathVariable("id") long id) {
        LOGGER.info("Deleting User with id " + id, Constants.LOG_DATE_FORMAT.format(new Date()));
        User u = userService.getUserById(id);
        if (u.getEmail() == null) {
            LOGGER.info("Unable to delete user with id " + id + ".User not found", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get users by email or first name and/or last name
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName
            /*@RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken*/) {
        // Credentials crs = Credentials.getCredentials(authToken);
        List<User> users = new ArrayList<>();
        if (email != null) {
            User u = userService.getUserByEmail(email);
            if (u.getEmail() != null) {
                users.add(u); 
            }
        } else {
            users = userService.getUsersByName(firstName, lastName);
        }
        return users.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) 
                    : new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Get user by id
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        User u = userService.getUserById(id);
        return u.getEmail() == null ? new ResponseEntity<>(HttpStatus.NO_CONTENT) 
                : new ResponseEntity<>(u, HttpStatus.OK);
    }

}
