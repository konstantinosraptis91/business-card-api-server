package gr.bc.api.controller;

import gr.bc.api.model.Company;
import gr.bc.api.service.CompanyService;
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
 * @author Konstantinos Raptis
 */
@RestController
@RequestMapping(value = "/api/company")
public class CompanyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);
    @Autowired
    private CompanyService companyService;

    // Save new company
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveCompany(@Valid @RequestBody Company company,
            UriComponentsBuilder ucBuilder) {

        long id;
        
        try {
            id = companyService.saveCompany(company);
        } catch (ServiceException ex) {
            LOGGER.error("saveCompany: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }
        
        LOGGER.info("Company with id " + id + " created", Constants.LOG_DATE_FORMAT.format(new Date()));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/company/{id}").buildAndExpand(id).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    // Update company
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCompany(@PathVariable("id") long id, 
            @Valid @RequestBody Company company) {
        
        try {
            companyService.updateCompany(id, company);
        } catch (ServiceException ex) {
            LOGGER.error("updateCompany: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }
        
        LOGGER.info("Company with id " + id + " updated", Constants.LOG_DATE_FORMAT.format(new Date()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Delete company
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCompanyById(@PathVariable("id") long id) {
        
        try {
            companyService.deleteCompanyById(id);
        } catch (ServiceException ex) {
            LOGGER.error("deleteCompanyById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Get company by id
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable("id") long id) {
        
        Company c;

        try {
            c = companyService.findById(id);
        } catch (ServiceException ex) {
            LOGGER.error("findById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }
        
        return new ResponseEntity<>(c, HttpStatus.OK);
    }

    // find
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> find(@RequestParam(value = "name", required = false) String name) {
        
        List<Company> cList;
        
        try {
            cList = companyService.find(name);
        } catch (ServiceException ex) {
            LOGGER.error("find: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }
        
        return cList.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(cList, HttpStatus.OK);
    }
    
    // search
    @RequestMapping(
            value = "/search",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchByName(@RequestParam(value = "name", required = true) String name) {
        
        List<Company> cList;
        
        try {
            cList = companyService.searchByName(name);
        } catch (ServiceException ex) {
            LOGGER.error("search: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }
        
        return cList.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(cList, HttpStatus.OK); 
    }
    
}
