package gr.bc.api.controller;

import gr.bc.api.model.Template;
import gr.bc.api.service.TemplateService;
import gr.bc.api.util.Constants;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
@RequestMapping(value = "/api/template")
public class TemplateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);
    @Autowired
    private TemplateService templateService;

    // Save template
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveTemplate(@RequestBody Template template,
            UriComponentsBuilder ucBuilder) {

        long id;
        
        try {
            id = templateService.saveTemplate(template);
        } catch (DataAccessException ex) {
            LOGGER.error("saveTemplate: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
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
    public ResponseEntity<Template> findById(@PathVariable("id") long id) {
        Template theTemplate;

        try {
            theTemplate = templateService.findById(id);
        } catch (DataAccessException ex) {
            LOGGER.error("findById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            if (ex instanceof EmptyResultDataAccessException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(theTemplate, HttpStatus.OK);
    }

    // find
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Template>> find(@RequestParam(value = "name", required = false) String name) {
        List<Template> templateList;

        // Get all templates
        if (name == null) {
            try {
                templateList = templateService.findAllTemplates();
            } catch (DataAccessException ex) {
                LOGGER.error("findAllTemplates: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            // Get template by name
        } else {
            try {
                templateList = new ArrayList<>();
                templateList.add(templateService.findByName(name));
            } catch (DataAccessException ex) {
                LOGGER.error("findByName: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
                if (ex instanceof EmptyResultDataAccessException) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }

        return templateList.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(templateList, HttpStatus.OK);
    }

    // Get all the templates by given primary color
    @RequestMapping(
            value = "/primarycolor/{primaryColor}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Template>> findByPrimaryColor(@NotNull @PathVariable("primaryColor") String primaryColor) {
        List<Template> templateList;

        try {
            templateList = templateService.findByPrimaryColor(primaryColor);
        } catch (DataAccessException ex) {
            LOGGER.error("findByPrimaryColor: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return templateList.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(templateList, HttpStatus.OK);
    }

    // Get all the templates by given secondary color
    @RequestMapping(
            value = "/secondarycolor/{secondaryColor}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Template>> findBySecondaryColor(@NotNull @PathVariable("secondaryColor") String secondaryColor) {
        List<Template> templateList;

        try {
            templateList = templateService.findBySecondaryColor(secondaryColor);
        } catch (DataAccessException ex) {
            LOGGER.error("findBySecondaryColor: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return templateList.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(templateList, HttpStatus.OK);
    }

    // Delete template
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteTemplateById(@PathVariable("id") long id) {
        boolean response;

        try {
            response = templateService.deleteTemplateById(id);
        } catch (DataAccessException ex) {
            LOGGER.error("deleteTemplateById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (response) {
            LOGGER.info("Template with id " + id + " deleted", Constants.LOG_DATE_FORMAT.format(new Date()));
        }

        return response ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Update Template
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateTemplate(@PathVariable("id") long id,
            @Valid @RequestBody Template template) {
        boolean response;

        try {
            response = templateService.updateTemplate(id, template);
        } catch (DataAccessException ex) {
            LOGGER.error("updateTemplate: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (response) {
            LOGGER.info("Template " + id + " updated", Constants.LOG_DATE_FORMAT.format(new Date()));
        }

        return response ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
