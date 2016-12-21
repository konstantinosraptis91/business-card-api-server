/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.controller;

import gr.bc.api.entity.Profession;
import gr.bc.api.entity.User;
import gr.bc.api.service.ProfessionService;
import gr.bc.api.util.Constants;
import java.util.Date;
import javax.validation.Valid;
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
 * @author konstantinos
 */
@RestController
@RequestMapping(value = "/api/profession")
public class ProfessionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfessionController.class);
    @Autowired
    private ProfessionService professionService;

    // Save new Profession
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Profession> createProfession(@Valid @RequestBody Profession profession,
            UriComponentsBuilder ucBuilder) {
        LOGGER.info("Creating Profession " + profession, Constants.LOG_DATE_FORMAT.format(new Date()));
        if (professionService.isProfessionExist(profession.getName())) {
            LOGGER.info("Profession with name " + profession.getName() + " already exists", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Profession response = professionService.saveProfession(profession);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/profession/{id}").buildAndExpand(response.getId()).toUri());
        return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
    }

    // Update profession
    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateUser(@Valid @RequestBody Profession profession) {
        LOGGER.info("Updating profession with id " + profession.getId(), Constants.LOG_DATE_FORMAT.format(new Date()));
        // Check if profession who is being updated actually exists
        if (!professionService.isProfessionExist(profession.getId())) {
            LOGGER.info("Unable to update profession with id " + profession.getId() + ".Profession not found", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            // Check if profession who is being updated got the same name with a profession in database    
        } else if (professionService.isProfessionExist(profession.getName())
                && professionService.findByName(profession.getName()).getId() != profession.getId()) {
            LOGGER.info("Profession with name " + profession.getName() + " already exists", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(professionService.updateProfession(profession), HttpStatus.OK);
    }

}
