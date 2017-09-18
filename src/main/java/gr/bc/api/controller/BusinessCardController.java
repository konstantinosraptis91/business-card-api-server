package gr.bc.api.controller;

import gr.bc.api.model.BusinessCard;
import gr.bc.api.model.User;
import gr.bc.api.model.response.BusinessCardResponse;
import gr.bc.api.service.BusinessCardService;
import gr.bc.api.service.UserService;
import gr.bc.api.service.WalletEntryService;
import gr.bc.api.util.Constants;
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
    @Autowired
    private UserService userService;
    @Autowired
    private WalletEntryService walletEntryService;

    // Create user new business card
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveBusinessCard(@Valid @RequestBody BusinessCard businessCard,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken,
            UriComponentsBuilder ucBuilder) {

        User owner;
        long id;

        try {

            owner = userService.findById(businessCard.getUserId());

            // if tokens are equal then autorized to proceed and save given business card for owner user
            if (owner.getToken().equals(authToken)) {
                id = businessCardService.saveBusinessCard(businessCard);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (DataAccessException ex) {
            LOGGER.error("saveBusinessCard: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            if (ex instanceof EmptyResultDataAccessException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        LOGGER.info("Business Card " + id + " created", Constants.LOG_DATE_FORMAT.format(new Date()));
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/businesscard/{id}").buildAndExpand(id).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    // Get business card id
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessCard> findById(@PathVariable("id") long id,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
        BusinessCard theBusinessCard;

        try {
            theBusinessCard = businessCardService.findById(id);
            User owner = userService.findById(theBusinessCard.getUserId());

            // Check if asking User has the right to claim this business card
            if (!owner.getToken().equals(authToken)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (DataAccessException ex) {
            LOGGER.error("findById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            if (ex instanceof EmptyResultDataAccessException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(theBusinessCard, HttpStatus.OK);
    }

    // Get business card id
    @RequestMapping(
            value = "/v2/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessCardResponse> findByIdV2(@PathVariable("id") long id,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
        BusinessCardResponse theBusinessCard;

        try {
            theBusinessCard = businessCardService.findByIdV2(id);
            User owner = userService.findById(theBusinessCard.getBusinessCard().getUserId());

            // Check if asking User has the right to claim this business card
            if (!owner.getToken().equals(authToken)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (DataAccessException ex) {
            LOGGER.error("findByIdV2: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            if (ex instanceof EmptyResultDataAccessException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(theBusinessCard, HttpStatus.OK);
    }
    
    // Get business card by user id (This method should be used from client to see my business cards)
    @RequestMapping(
            value = "/user/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BusinessCard>> findByUserId(@PathVariable("id") long id,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
        List<BusinessCard> businessCardList;

        try {
            User owner = userService.findById(id);
            businessCardList = businessCardService.findByUserId(id);

            if (businessCardList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            // Check if asking User has the right to claim this business card
            if (!owner.getToken().equals(authToken)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (DataAccessException ex) {
            LOGGER.error("findByUserId: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            if (ex instanceof EmptyResultDataAccessException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(businessCardList, HttpStatus.OK);
    }

    // Get business card by user id (This method should be used from client to see my business cards)
    @RequestMapping(
            value = "/v2/user/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BusinessCardResponse>> findByUserIdV2(@PathVariable("id") long id,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
        List<BusinessCardResponse> businessCardList;

        try {
            User owner = userService.findById(id);
            businessCardList = businessCardService.findByUserIdV2(id);

            if (businessCardList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            // Check if asking User has the right to claim this business card
            if (!owner.getToken().equals(authToken)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (DataAccessException ex) {
            LOGGER.error("findByUserIdV2: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            if (ex instanceof EmptyResultDataAccessException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(businessCardList, HttpStatus.OK);
    }
    
    // Get business card by email (This method should be used from client to find others business cards)
    @RequestMapping(
            value = "/user",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BusinessCard>> findBusinessCard(@RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "firstname", required = false) String firstName,
            @RequestParam(value = "lastname", required = false) String lastName) {

        List<BusinessCard> businessCardList;

        try {

            // (case 1) by user email
            if (email != null) {
                businessCardList = businessCardService.findByUserEmail(email);
            } // (case 2) by first and last name
            else if (firstName != null && lastName != null) {
                businessCardList = businessCardService.findByUserName(firstName, lastName);
            } // (case 3) every param is null 
            else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (businessCardList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

        } catch (DataAccessException ex) {
            LOGGER.error("findBusinessCard: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            if (ex instanceof EmptyResultDataAccessException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(businessCardList, HttpStatus.OK);
    }

    // Get business card by email (This method should be used from client to find others business cards)
    @RequestMapping(
            value = "/v2/user",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BusinessCardResponse>> findBusinessCardV2(@RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "firstname", required = false) String firstName,
            @RequestParam(value = "lastname", required = false) String lastName) {

        List<BusinessCardResponse> businessCardList;

        try {

            // (case 1) by user email
            if (email != null) {
                businessCardList = businessCardService.findByUserEmailV2(email);
            } // (case 2) by first and last name
            else if (firstName != null && lastName != null) {
                businessCardList = businessCardService.findByUserNameV2(firstName, lastName);
            } // (case 3) every param is null 
            else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (businessCardList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

        } catch (DataAccessException ex) {
            LOGGER.error("findBusinessCardV2: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            if (ex instanceof EmptyResultDataAccessException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(businessCardList, HttpStatus.OK);
    }
    
    // Update business card
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateBusinessCard(@PathVariable("id") long id,
            @Valid @RequestBody BusinessCard businessCard,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
        boolean response;
        BusinessCard theBusinessCard;

        try {
            theBusinessCard = businessCardService.findById(id);
            User owner = userService.findById(theBusinessCard.getUserId());

            // if tokens are equal then autorized to proceed with update
            if (owner.getToken().equals(authToken)) {
                response = businessCardService.updateBusinessCard(theBusinessCard.getId(), businessCard);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (DataAccessException ex) {
            LOGGER.error("updateBusinessCard: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            if (ex instanceof EmptyResultDataAccessException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (response) {
            LOGGER.info("Business Card " + id + " updated", Constants.LOG_DATE_FORMAT.format(new Date()));
        }

        return response ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Delete business card by id
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteBusinessCardById(@PathVariable("id") long id,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
        boolean response;
        BusinessCard theBusinessCard;

        try {
            theBusinessCard = businessCardService.findById(id);
            User owner = userService.findById(theBusinessCard.getUserId());

            // if tokens are equal then autorized to proceed with deletion
            if (owner.getToken().equals(authToken)) {
                // first delete the card from wallet entry table in order to avoid conflicts
                walletEntryService.deleteWalletEntryByBusinessCardId(id);
                response = businessCardService.deleteBusinessCardById(id);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (DataAccessException ex) {
            LOGGER.error("deleteBusinessCardById: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            if (ex instanceof EmptyResultDataAccessException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (response) {
            LOGGER.info("Business Card " + id + " deleted", Constants.LOG_DATE_FORMAT.format(new Date()));
        }

        return response ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
