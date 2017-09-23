package gr.bc.api.controller;

import gr.bc.api.model.User;
import gr.bc.api.model.authentication.TokenProperties;
import gr.bc.api.service.UserService;
import gr.bc.api.service.exception.ConflictException;
import gr.bc.api.service.exception.ServiceException;
import gr.bc.api.util.Constants;
import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> saveUser(@Valid @RequestBody User user, UriComponentsBuilder ucBuilder) {

        User u;

        try {
            u = userService.saveUser(user);
        } catch (ConflictException ex) {
            LOGGER.error("saveUser: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        LOGGER.info("User " + u.toString() + " created", Constants.LOG_DATE_FORMAT.format(new Date()));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(u.getId()).toUri());

        return new ResponseEntity<>(u.getToken(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/authenticate",
            produces = MediaType.TEXT_PLAIN_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(@RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String credentialsToken,
            UriComponentsBuilder ucBuilder) {

        TokenProperties properties;

        try {
            properties = userService.authenticateByCredentialsToken(credentialsToken);
        } catch (ServiceException ex) {
            LOGGER.info(ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(properties.getId()).toUri());
        LOGGER.info("User with id: " + properties.getId() + " authenticated", Constants.LOG_DATE_FORMAT.format(new Date()));

        return new ResponseEntity<>(properties.getToken(), headers, HttpStatus.OK);
    }

    // Update user (Update Account)
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@PathVariable("id") long id,
            @Valid @RequestBody User user,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String token) {
        
        boolean response;

        try {
            response = userService.updateUser(id, token, user);
        } catch (ServiceException ex) {
            return ex.getResponse();
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
    public ResponseEntity<?> deleteUserById(@PathVariable("id") long id,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String token) {
        
        boolean result;
        
        try {
            result = userService.deleteUserById(id, token);
        } catch (ServiceException ex) {
            LOGGER.error("deleteUserById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        if (result) {
            LOGGER.info("User " + id + " deleted", Constants.LOG_DATE_FORMAT.format(new Date()));
        }

        return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Get user by id
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable("id") long id,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String token) {
        
        User u;
        
        try {
            u = userService.findById(id, token);

        } catch (ServiceException ex) {
            LOGGER.error("findById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }
        
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

}
