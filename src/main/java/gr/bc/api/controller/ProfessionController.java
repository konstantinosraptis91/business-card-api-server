package gr.bc.api.controller;

import gr.bc.api.model.Profession;
import gr.bc.api.service.ProfessionService;
import gr.bc.api.util.Constants;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
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
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveProfession(@Valid @RequestBody Profession profession,
            UriComponentsBuilder ucBuilder) {

        long id;
        
        try {
            id = professionService.saveProfession(profession);
        } catch (DataAccessException ex) {
            LOGGER.error("saveProfession: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        LOGGER.info("Profession with id " + id + " created", Constants.LOG_DATE_FORMAT.format(new Date()));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/profession/{id}").buildAndExpand(id).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    // Update profession
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateProfession(@PathVariable("id") long id, 
            @Valid @RequestBody Profession profession) {
        boolean response;

        try {
            response = professionService.updateProfession(id, profession);
        } catch (DataAccessException ex) {
            LOGGER.error("updateProfession: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (response) {
            LOGGER.info("Profession with id " + profession.getId() + " updated", Constants.LOG_DATE_FORMAT.format(new Date()));
        }

        return response ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete profession
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteProfessionById(@PathVariable("id") long id) {
        boolean response;

        try {
            response = professionService.deleteProfessionById(id);
        } catch (DataAccessException ex) {
            LOGGER.error("deleteProfessionById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (response) {
            LOGGER.info("Profession with id " + id + " deleted", Constants.LOG_DATE_FORMAT.format(new Date()));
        }

        return response ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get profession by id
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Profession> findById(@PathVariable("id") long id) {
        Profession response;

        try {
            response = professionService.findById(id);
        } catch (DataAccessException ex) {
            LOGGER.error("findById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            if (ex instanceof EmptyResultDataAccessException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // find
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Profession>> find(@RequestParam(value = "name", required = false) String name) {
        List<Profession> response;

        // Get all professions
        if (name == null) {
            try {
                response = professionService.findAllProfessions();
            } catch (DataAccessException ex) {
                LOGGER.error("findAllProfessions: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        // Get profession by name
        } else {
            try {
                response = professionService.findByNameV2(name);
            } catch (DataAccessException ex) {
                LOGGER.error("findByName: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
                if (ex instanceof EmptyResultDataAccessException) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }

        return response.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(response, HttpStatus.OK);
    }

}
