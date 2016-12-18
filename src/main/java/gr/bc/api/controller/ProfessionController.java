/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.controller;

import gr.bc.api.entity.Profession;
import gr.bc.api.service.ProfessionService;
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
 * @author konstantinos
 */
@RestController
@RequestMapping(value = "/api/profession")
public class ProfessionController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfessionController.class);
    @Autowired
    private ProfessionService professionService;
    
    // Create new Profession
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Profession> createProfession(@RequestBody Profession profession, UriComponentsBuilder ucBuilder) {
        LOGGER.info("Creating Profession " + profession.getName(), Constants.LOG_DATE_FORMAT.format(new Date()));
        Profession p = professionService.getProfessionByName(profession.getName());
        if (p.getName() != null) {
            LOGGER.info("Profession " + profession.getName() + " already exists", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        // reuse object p
        p = professionService.createProfession(profession);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/profession/{id}").buildAndExpand(p.getId()).toUri());
        return new ResponseEntity<>(p, headers, HttpStatus.CREATED);
    }
    
}
