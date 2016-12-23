/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.controller;

import gr.bc.api.entity.BusinessCard;
import gr.bc.api.service.BusinessCardService;
import gr.bc.api.service.ProfessionService;
import gr.bc.api.service.TemplateService;
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
@RequestMapping(value = "/api/businesscard")
public class BusinessCardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessCardController.class);
    @Autowired
    private BusinessCardService businessCardService;
    @Autowired
    private UserService userService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private ProfessionService professionService;

    // Create user new business card
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessCard> saveBusinessCard(@RequestBody BusinessCard businessCard, UriComponentsBuilder ucBuilder) {
        LOGGER.info("Creating Business Card for user id: " + businessCard.getUserId(), Constants.LOG_DATE_FORMAT.format(new Date()));
        // user not found
        if (!userService.isUserExist(businessCard.getUserId())) {
            LOGGER.info("Unable to find user with id " + businessCard.getUserId() + ". User does not exist.", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // template not found
        if (!templateService.isTemplateExist(businessCard.getTemplateId())) {
            LOGGER.info("Unable to find template with id " + businessCard.getTemplateId() + ". Template does not exist.", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // profession not found
        if (!professionService.isProfessionExist(businessCard.getProfessionId())) {
            LOGGER.info("Unable to find profession with id " + businessCard.getProfessionId() + ". Template does not exist.", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // user already owns a business card
        if (businessCardService.isBusinessCardExistByUserId(businessCard.getUserId())) {
            LOGGER.info("User with id " + businessCard.getUserId() + " already owns a Business Card", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        BusinessCard response = businessCardService.saveBusinessCard(businessCard);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/businesscard/{id}").buildAndExpand(response.getId()).toUri());
        return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
    }
    
    // Get business card id
    @RequestMapping(
            value="/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessCard> findById(@PathVariable("id") long id) {
        return businessCardService.isBusinessCardExist(id) ? new ResponseEntity<>(businessCardService.findById(id), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // Get business card by user id
    @RequestMapping(
            value="/user/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessCard> findByUserId(@PathVariable("id") long id) {
        return businessCardService.isBusinessCardExistByUserId(id) ? new ResponseEntity<>(businessCardService.findByUserId(id), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // Get business card by profession id
    @RequestMapping(
            value="/profession/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BusinessCard>> findByProfessionId(@PathVariable("id") long id) {
        List<BusinessCard> bcs = businessCardService.findByProfessionId(id);
        return !bcs.isEmpty() ? new ResponseEntity<>(bcs, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // Get business card by email or first name and/or last name
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BusinessCard>> find(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName) {
        List<BusinessCard> bcs = new ArrayList<>();
        if (email != null) {
            if (businessCardService.isBusinessCardExistByUserEmail(email)) {
                bcs.add(businessCardService.findByUserEmail(email));
            }
        } else {
            bcs = businessCardService.findByUserName(firstName, lastName);
        }
        return bcs.isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(bcs, HttpStatus.OK);
    }
    
    // Update business card
    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateBusinessCard(@Valid @RequestBody BusinessCard businessCard) {
        LOGGER.info("Updating business card with id " + businessCard.getId(), Constants.LOG_DATE_FORMAT.format(new Date()));
        // Check if business card which actually updated exists
        if (!businessCardService.isBusinessCardExist(businessCard.getId())) {
            LOGGER.info("Unable to update business card with id " + businessCard.getId() + ".Business card not found", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(businessCardService.updateBusinessCard(businessCard), HttpStatus.OK);
    }
    
    // Delete business card by id
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteBusinessCardById(@PathVariable("id") long id) {
        LOGGER.info("Deleting business card with id " + id, Constants.LOG_DATE_FORMAT.format(new Date()));
        if (!businessCardService.isBusinessCardExist(id)) {
            LOGGER.info("Unable to delete business card with id " + id + ".Business card not found", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(businessCardService.deleteBusinessCardById(id), HttpStatus.NO_CONTENT);
    }
    
    // Delete business card by user id
    @RequestMapping(
            value = "/user/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteBusinessCardByUserId(@PathVariable("id") long id) {
        LOGGER.info("Deleting business card with user id " + id, Constants.LOG_DATE_FORMAT.format(new Date()));
        if (!businessCardService.isBusinessCardExistByUserId(id)) {
            LOGGER.info("Unable to delete business card with user id " + id + ".Business card not found", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(businessCardService.deleteBusinessCardByUserId(id), HttpStatus.NO_CONTENT);
    }
    
}
