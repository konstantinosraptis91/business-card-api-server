/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.controller;

import gr.bc.api.entity.BusinessCard;
import gr.bc.api.service.BusinessCardService;
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
        
    // Create user business card
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BusinessCard> createBusinessCard(@RequestBody BusinessCard businessCard, UriComponentsBuilder ucBuilder) {
        LOGGER.info("Creating Business Card for user id: " + businessCard.getUserId(), Constants.LOG_DATE_FORMAT.format(new Date()));
//        BusinessCard bc = businessCardService.get
//        if (u.getBusinessCardId() != 0) {
//            LOGGER.info("User with email " + user.getEmail() + " already exists", Constants.LOG_DATE_FORMAT.format(new Date()));
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }
        // reuse object u
        BusinessCard bc = businessCardService.createBusinessCard(businessCard);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/businesscard/{id}").buildAndExpand(bc.getId()).toUri());
        return new ResponseEntity<>(bc, headers, HttpStatus.CREATED);
    }
    
}
