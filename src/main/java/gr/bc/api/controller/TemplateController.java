/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.controller;

import gr.bc.api.entity.Template;
import gr.bc.api.service.TemplateService;
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
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Template> saveTemplate(@RequestBody Template template, UriComponentsBuilder ucBuilder) {
        LOGGER.info("Creating Template " + template, Constants.LOG_DATE_FORMAT.format(new Date()));
        // check if template with the same name already exist
        if (templateService.isTemplateExist(template.getName())) {
            LOGGER.info("Template with name " + template.getName() + " already exists", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Template response = templateService.saveTemplate(template);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/template/{id}").buildAndExpand(response.getId()).toUri());
        return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
    }

    // Get template by id
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Template> findById(@PathVariable("id") long id) {
        return templateService.isTemplateExist(id) ? new ResponseEntity<>(templateService.findById(id), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get all templates
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Template>> findAllTemplates() {
        List<Template> templates = templateService.findAllTemplates();
        return templates.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(templates, HttpStatus.OK);
    }
    
    // Get all the templates by given color
    @RequestMapping(
            value = "/color/{color}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Template>> findByColor(@PathVariable("color") String color) {
        List<Template> templates = templateService.findByColor(color);
        return templates.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(templates, HttpStatus.OK);
    }
    
    // Get all the templates by given primary color
    @RequestMapping(
            value = "/primarycolor/{primaryColor}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Template>> findByPrimaryColor(@PathVariable("primaryColor") String primaryColor) {
        List<Template> templates = templateService.findByPrimaryColor(primaryColor);
        return templates.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(templates, HttpStatus.OK);
    }
    
    // Get all the templates by given secondary color
    @RequestMapping(
            value = "/secondarycolor/{secondaryColor}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Template>> findBySecondaryColor(@PathVariable("secondaryColor") String secondaryColor) {
        List<Template> templates = templateService.findBySecondaryColor(secondaryColor);
        return templates.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(templates, HttpStatus.OK);
    }
    
    // Delete template
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteTemplateById(@PathVariable("id") long id) {
        LOGGER.info("Deleting Profession with id " + id, Constants.LOG_DATE_FORMAT.format(new Date()));
        if (!templateService.isTemplateExist(id)) {
            LOGGER.info("Unable to delete template with id " + id + ".Template not found", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return templateService.deleteTemplateById(id) ? new ResponseEntity<>(HttpStatus.OK) 
                : new ResponseEntity<>(HttpStatus.CONFLICT); 
    }
    
    // Update Template
    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateProfession(@Valid @RequestBody Template template) {
        LOGGER.info("Updating template with id " + template.getId(), Constants.LOG_DATE_FORMAT.format(new Date()));
        // Check if template who is being updated actually exists
        if (!templateService.isTemplateExist(template.getId())) {
            LOGGER.info("Unable to update template with id " + template.getId() + ".Template not found", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         // Check if template who is being updated got the same name with a template in database    
        } else if (templateService.isTemplateExist(template.getName())
                && templateService.findByName(template.getName()).getId() != template.getId()) {
            LOGGER.info("Template with name " + template.getName() + " already exists", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return templateService.updateTemplate(template) ? new ResponseEntity<>(HttpStatus.OK) 
                : new ResponseEntity<>(HttpStatus.CONFLICT); 
    }
    
}
