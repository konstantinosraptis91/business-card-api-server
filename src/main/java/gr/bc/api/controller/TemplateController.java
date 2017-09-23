package gr.bc.api.controller;

import gr.bc.api.model.Template;
import gr.bc.api.service.TemplateService;
import gr.bc.api.service.exception.ServiceException;
import gr.bc.api.util.Constants;
import java.util.Date;
import java.util.List;
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
@RequestMapping(value = "/api/template")
public class TemplateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);
    @Autowired
    private TemplateService templateService;

    // Save template
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveTemplate(@RequestBody Template template,
            UriComponentsBuilder ucBuilder) {

        long id;

        try {
            id = templateService.saveTemplate(template);
        } catch (ServiceException ex) {
            LOGGER.error("saveTemplate: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        LOGGER.info("Template " + id + " created", Constants.LOG_DATE_FORMAT.format(new Date()));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/template/{id}").buildAndExpand(id).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    // Get template by id
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable("id") long id) {

        Template t;

        try {
            t = templateService.findById(id);
        } catch (ServiceException ex) {
            LOGGER.error("findById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        return new ResponseEntity<>(t, HttpStatus.OK);
    }

    // find
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> find(@RequestParam(value = "name", required = false) String name) {

        List<Template> tList;

        try {
            tList = templateService.find(name);
        } catch (ServiceException ex) {
            LOGGER.error("find: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        return tList.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(tList, HttpStatus.OK);
    }

    // Get all the templates by given primary color
    @RequestMapping(
            value = "/primarycolor/{primaryColor}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByPrimaryColor(@NotNull @PathVariable("primaryColor") String primaryColor) {
        
        List<Template> tList;

        try {
            tList = templateService.findByPrimaryColor(primaryColor);
        } catch (ServiceException ex) {
            LOGGER.error("findByPrimaryColor: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        return tList.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(tList, HttpStatus.OK);
    }

    // Get all the templates by given secondary color
    @RequestMapping(
            value = "/secondarycolor/{secondaryColor}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findBySecondaryColor(@NotNull @PathVariable("secondaryColor") String secondaryColor) {
        
        List<Template> tList;

        try {
            tList = templateService.findBySecondaryColor(secondaryColor);
        } catch (ServiceException ex) {
            LOGGER.error("findBySecondaryColor: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        return tList.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(tList, HttpStatus.OK);
    }

    // Delete template
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTemplateById(@PathVariable("id") long id) {

        try {
            templateService.deleteTemplateById(id);
        } catch (ServiceException ex) {
            LOGGER.error("deleteTemplateById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Update Template
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTemplate(@PathVariable("id") long id,
            @Valid @RequestBody Template template) {

        try {
            templateService.updateTemplate(id, template);
        } catch (ServiceException ex) {
            LOGGER.error("updateTemplate: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
