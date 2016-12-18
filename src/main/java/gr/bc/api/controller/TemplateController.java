/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.controller;

import gr.bc.api.entity.Template;
import gr.bc.api.entity.User;
import gr.bc.api.service.TemplateService;
import gr.bc.api.util.Constants;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    // Create new Profession
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Template> createTemplate(@RequestBody Template template, UriComponentsBuilder ucBuilder) {
        LOGGER.info("Creating Template " + template.getName(), Constants.LOG_DATE_FORMAT.format(new Date()));
        Template t = templateService.getTemplateByName(template.getName());
        if (t.getName() != null) {
            LOGGER.info("Template " + template.getName() + " already exists", Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        // reuse object t
        t = templateService.createTemplate(template);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/template/{id}").buildAndExpand(t.getId()).toUri());
        return new ResponseEntity<>(t, headers, HttpStatus.CREATED);
    }

    // Get template by id
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Template> getTemplateById(@PathVariable("id") long id) {
        Template t = templateService.getTemplateById(id);
        return t.getName() == null ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(t, HttpStatus.OK);
    }

    // Get template by name or if name is null get all templates
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Template>> getTemplates(
            @RequestParam(value = "name", required = false) String name
            /*@RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken*/) {
        // Credentials crs = Credentials.getCredentials(authToken);
        List<Template> templates = new ArrayList<>();
        if (name != null) {
            Template t = templateService.getTemplateByName(name);
            if (t.getName() != null) {
                templates.add(t);
            }
        } else {
            templates = templateService.getAllTemplates();
        }
        return templates.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(templates, HttpStatus.OK);
    }

}
