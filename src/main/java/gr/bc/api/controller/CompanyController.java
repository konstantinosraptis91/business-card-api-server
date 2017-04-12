package gr.bc.api.controller;

import gr.bc.api.model.Company;
import gr.bc.api.service.CompanyService;
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
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveCompany(@Valid @RequestBody Company company,
            UriComponentsBuilder ucBuilder) {

        long id;
        
        try {
            id = companyService.saveCompany(company);
        } catch (DataAccessException ex) {
            LOGGER.error("saveCompany: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
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
    public ResponseEntity<Void> updateCompany(@PathVariable("id") long id, 
            @Valid @RequestBody Company company) {
        boolean response;

        try {
            response = companyService.updateCompany(id, company);
        } catch (DataAccessException ex) {
            LOGGER.error("updateCompany: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (response) {
            LOGGER.info("Company with id " + id + " updated", Constants.LOG_DATE_FORMAT.format(new Date()));
        }

        return response ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete company
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteCompanyById(@PathVariable("id") long id) {
        boolean response;

        try {
            response = companyService.deleteCompanyById(id);
        } catch (DataAccessException ex) {
            LOGGER.error("deleteCompanyById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (response) {
            LOGGER.info("Company with id " + id + " deleted", Constants.LOG_DATE_FORMAT.format(new Date()));
        }

        return response ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get company by id
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Company> findById(@PathVariable("id") long id) {
        Company theCompany;

        try {
            theCompany = companyService.findById(id);
        } catch (DataAccessException ex) {
            LOGGER.error("findById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            if (ex instanceof EmptyResultDataAccessException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(theCompany, HttpStatus.OK);
    }

    // find
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Company>> find(@RequestParam(value = "name", required = false) String name) {
        List<Company> companyList;

        // Get all companies
        if (name == null) {
            try {
                companyList = companyService.findAllCompanies();
            } catch (DataAccessException ex) {
                LOGGER.error("findAllCompanies: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        // Get company by name
        } else {
            try {
                companyList = new ArrayList<>();
                companyList.add(companyService.findByName(name));
            } catch (DataAccessException ex) {
                LOGGER.error("findByName: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
                if (ex instanceof EmptyResultDataAccessException) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }

        return companyList.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(companyList, HttpStatus.OK);
    }

}
