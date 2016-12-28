/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.controller;

import gr.bc.api.entity.UserRating;
import gr.bc.api.service.BusinessCardService;
import gr.bc.api.service.UserRatingService;
import gr.bc.api.service.UserService;
import gr.bc.api.util.Constants;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Konstantinos Raptis
 */
@RestController
@RequestMapping(value = "/api/userrating")
public class UserRatingController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRatingController.class);
    @Autowired
    private UserRatingService userRatingService;
    @Autowired
    private BusinessCardService businessCardService;
    @Autowired
    private UserService userService;
    
    // Save user rating
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRating> saveUserRating(@Valid @RequestBody UserRating userRating,
            UriComponentsBuilder ucBuilder) {
        LOGGER.info("Creating " + userRating, 
                Constants.LOG_DATE_FORMAT.format(new Date()));
        UserRating response = userRatingService.saveUserRating(userRating);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/userrating/{id}").buildAndExpand(response.getId()).toUri());
        return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
    }
    
    // get user rating by business card id
    @RequestMapping(
            value = "/businesscard/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserRating>> findByBusinessCardId(@PathVariable("id") long id) {
        return businessCardService.isBusinessCardExist(id) ? new ResponseEntity<>(userRatingService.findByBusinessCardId(id), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // get user rating by user id
    @RequestMapping(
            value = "/user/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserRating>> findByUserId(@PathVariable("id") long id) {
        return userService.isUserExist(id) ? new ResponseEntity<>(userRatingService.findByUserId(id), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // update user rating
    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateUserRating(@Valid @RequestBody UserRating userRating) {
        LOGGER.info("Updating user rating with id " + userRating.getId(), Constants.LOG_DATE_FORMAT.format(new Date()));
        // check if user rating which is being updated actually exists
        if (!userRatingService.isUserRatingExixst(userRating.getId())) {
            LOGGER.info("Unable to update user rating with id " + userRating.getId() + ".User rating not found", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userRatingService.updateUserRating(userRating), HttpStatus.OK);
    }
    
    // delete user rating
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteUserRatingById(@PathVariable("id") long id) {
        LOGGER.info("Deleting user rating with id " + id, Constants.LOG_DATE_FORMAT.format(new Date()));
        if (!userRatingService.isUserRatingExixst(id)) {
            LOGGER.info("Unable to delete user rating with id " + id + ".User rating not found", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userRatingService.deleteUserRatingById(id), HttpStatus.NO_CONTENT);
    }
    
}
