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
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    // Create user business card
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
    
}
