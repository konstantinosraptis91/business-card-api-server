package gr.bc.api.controller;

import gr.bc.api.model.BusinessCard;
import gr.bc.api.model.request.BusinessCardRequestImpl;
import gr.bc.api.model.response.BusinessCardResponse;
import gr.bc.api.service.BusinessCardService;
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
import org.springframework.web.bind.annotation.RequestHeader;
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
@RequestMapping(value = "/api/businesscard")
public class BusinessCardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessCardController.class);
    @Autowired
    private BusinessCardService businessCardService;
    
    // Create user new business card
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveBusinessCard(@Valid @RequestBody BusinessCard businessCard,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String token,
            UriComponentsBuilder ucBuilder) {
        
        long id;

        try {

            id = businessCardService.saveBusinessCard(businessCard.getUserId(), token, businessCard);

        } catch (ServiceException ex) {
            LOGGER.error("saveBusinessCard: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        LOGGER.info("Business Card " + id + " created", Constants.LOG_DATE_FORMAT.format(new Date()));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/businesscard/{id}").buildAndExpand(id).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    // Create user new business card
    @RequestMapping(
            value = "/v2",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveBusinessCardV2(@RequestBody BusinessCardRequestImpl cardRequest,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String token,
            UriComponentsBuilder ucBuilder) {

        long id;

        try {
            id = businessCardService.saveBusinessCard(cardRequest.getBusinessCard().getUserId(), token, cardRequest);
        } catch (ServiceException ex) {
            LOGGER.error("saveBusinessCard: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        LOGGER.info("Business Card " + id + " created", Constants.LOG_DATE_FORMAT.format(new Date()));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/businesscard/{id}").buildAndExpand(id).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    // Update business card
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBusinessCard(@PathVariable("id") long id,
            @Valid @RequestBody BusinessCard bc,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String token) {

        boolean result;

        try {
            result = businessCardService.updateBusinessCard(id, token, bc);
        } catch (ServiceException ex) {
            LOGGER.error("updateBusinessCard: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        if (result) {
            LOGGER.info("Business Card " + id + " updated", Constants.LOG_DATE_FORMAT.format(new Date()));
        }

        return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Delete business card by id
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBusinessCardById(@PathVariable("id") long id,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String token) {

        boolean result;

        try {
            result = businessCardService.deleteBusinessCardById(id, token);
        } catch (ServiceException ex) {
            LOGGER.error("deleteBusinessCardById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        if (result) {
            LOGGER.info("Business Card " + id + " deleted", Constants.LOG_DATE_FORMAT.format(new Date()));
        }

        return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Get business card id
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable("id") long id,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String token) {

        BusinessCard bc;

        try {
            bc = businessCardService.findById(id, token);
        } catch (ServiceException ex) {
            LOGGER.error("findById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        return new ResponseEntity<>(bc, HttpStatus.OK);
    }

    // Get business card response
    @RequestMapping(
            value = "/v2/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByIdV2(@PathVariable("id") long id,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String token) {

        BusinessCardResponse cardResponse;

        try {
            cardResponse = businessCardService.findByIdV2(id, token);
        } catch (ServiceException ex) {
            LOGGER.error("findByIdV2: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        return new ResponseEntity<>(cardResponse, HttpStatus.OK);
    }

    // Get business card by user id (This method should be used from client to see my business cards)
    @RequestMapping(
            value = "/user/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByUserId(@PathVariable("id") long id,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String token) {
        
        List<BusinessCard> bcList;

        try {
            bcList = businessCardService.findByUserId(id, token);
        } catch (ServiceException ex) {
            LOGGER.error("findByUserId: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        return new ResponseEntity<>(bcList, HttpStatus.OK);
    }

    /**
     * Get business card by user id (This method should be used from client to see my business cards)
     *
     * @param id User id
     * @param token
     * @return
     */
    @RequestMapping(
            value = "/v2/user/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByUserIdV2(@PathVariable("id") long id,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String token) {

        List<BusinessCardResponse> cardResponseList;

        try {
            cardResponseList = businessCardService.findByUserIdV2(id, token);
        } catch (ServiceException ex) {
            LOGGER.error("findByUserIdV2: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        return new ResponseEntity<>(cardResponseList, HttpStatus.OK);
    }

    // Get business card by email (This method should be used from client to find others business cards)
    @RequestMapping(
            value = "/user",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> find(@RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "firstname", required = false) String firstName,
            @RequestParam(value = "lastname", required = false) String lastName) {

        List<BusinessCard> bcList;

        try {
            bcList = businessCardService.find(email, firstName, lastName);
        } catch (ServiceException ex) {
            LOGGER.error("findBusinessCard: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        return new ResponseEntity<>(bcList, HttpStatus.OK);
    }

    // Get business card by email (This method should be used from client to find others business cards)
    @RequestMapping(
            value = "/v2/user",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findV2(@RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "firstname", required = false) String firstName,
            @RequestParam(value = "lastname", required = false) String lastName) {

        List<BusinessCardResponse> cardResponseList;

        try {
            cardResponseList = businessCardService.findV2(email, firstName, lastName);
        } catch (ServiceException ex) {
            LOGGER.error("findBusinessCard: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        return new ResponseEntity<>(cardResponseList, HttpStatus.OK);
    }

}
