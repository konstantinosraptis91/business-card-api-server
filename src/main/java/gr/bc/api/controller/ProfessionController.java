package gr.bc.api.controller;

import gr.bc.api.model.Profession;
import gr.bc.api.service.ProfessionService;
import gr.bc.api.service.exception.ServiceException;
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
    public ResponseEntity<?> saveProfession(@Valid @RequestBody Profession profession,
            UriComponentsBuilder ucBuilder) {

        long id;
        
        try {
            id = professionService.saveProfession(profession);
        } catch (ServiceException ex) {
            LOGGER.error("saveProfession: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
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
    public ResponseEntity<?> updateProfession(@PathVariable("id") long id, 
            @Valid @RequestBody Profession profession) {

        try {
            professionService.updateProfession(id, profession);
        } catch (ServiceException ex) {
            LOGGER.error("updateProfession: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }
        
        LOGGER.info("Profession with id " + id + " updated", Constants.LOG_DATE_FORMAT.format(new Date()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Delete profession
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteProfessionById(@PathVariable("id") long id) {

        try {
            professionService.deleteProfessionById(id);
        } catch (ServiceException ex) {
            LOGGER.error("deleteProfessionById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        LOGGER.info("Profession with id " + id + " deleted", Constants.LOG_DATE_FORMAT.format(new Date()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Get profession by id
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable("id") long id) {
        
        Profession p;

        try {
            p = professionService.findById(id);
        } catch (ServiceException ex) {
            LOGGER.error("findById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }
        
        LOGGER.info("Profession with id " + id + " retrieved", Constants.LOG_DATE_FORMAT.format(new Date()));
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    // find
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> find(@RequestParam(value = "name", required = false) String name) {
        
        List<Profession> pList;
        
        try {
            pList = professionService.find(name);
        } catch (ServiceException ex) {
            LOGGER.error("find: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }
        
        return pList.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(pList, HttpStatus.OK); 
    }

    // search
    @RequestMapping(
            value = "/search",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchByName(@RequestParam(value = "name", required = true) String name) {
        
        List<Profession> pList;
        
        try {
            pList = professionService.searchByName(name);
        } catch (ServiceException ex) {
            LOGGER.error("find: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }
        
        return pList.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(pList, HttpStatus.OK); 
    }
    
}
