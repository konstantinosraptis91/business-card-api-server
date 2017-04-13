package gr.bc.api.controller;

import gr.bc.api.model.BusinessCard;
import gr.bc.api.model.User;
import gr.bc.api.model.WalletEntry;
import gr.bc.api.service.BusinessCardService;
import gr.bc.api.service.UserService;
import gr.bc.api.service.WalletEntryService;
import gr.bc.api.util.Constants;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Konstantinos Raptis
 */
@RestController
@RequestMapping(value = "/api/walletentry")
public class WalletEntryController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WalletEntryController.class);
    @Autowired
    private WalletEntryService walletEntryService;
    @Autowired
    private UserService userService;
    @Autowired
    private BusinessCardService businessCardService;

    // Add business card to user wallet (user_business_card)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveWalletEntry(
            @Valid @RequestBody WalletEntry entry,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
        User walletOwner;
        BusinessCard theBusinessCard;
        boolean response;

        try {
            walletOwner = userService.findById(entry.getUserId());

            // if tokens are equal then autorized to proceed
            if (walletOwner.getToken().equals(authToken)) {

                theBusinessCard = businessCardService.findById(entry.getBusinessCardId());
                // Check if user trying to add his own card in wallet
                if (walletOwner.getId() == theBusinessCard.getId()) {
                    LOGGER.info("It is not allowed for a user to add his own card in his wallet...");
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
                
                // Check if business card not public
                if (!theBusinessCard.isUniversal()) {
                    LOGGER.info("Business card not public...");
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
                
                response = walletEntryService.saveWalletEntry(entry);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (DataAccessException ex) {
            LOGGER.error("addBusinessCardToWallet: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            if (ex instanceof EmptyResultDataAccessException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (response) {
            LOGGER.info(entry.toString() + " added", Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        
        
        return response? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Get all business card a user got in his wallet by user id
    @RequestMapping(
            value = "/user/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BusinessCard>> findAllBusinessCardsByUserId(@PathVariable("id") long id,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
        User walletOwner;
        List<BusinessCard> businessCardList;

        try {
            walletOwner = userService.findById(id);

            // if tokens are equal then autorized to proceed
            if (walletOwner.getToken().equals(authToken)) {
                businessCardList = walletEntryService.findAllBusinessCardsByUserId(walletOwner.getId());
                
                // (1) remove from wallet business cards which are not public
                businessCardList = businessCardList
                        .stream()
                        .filter(bc -> bc.isUniversal())
                        .collect(Collectors.toList());
                
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (DataAccessException ex) {
            LOGGER.error("findAllBusinessCardInWalletByUserId: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            if (ex instanceof EmptyResultDataAccessException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return businessCardList.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(businessCardList, HttpStatus.OK);
    }

    // Delete a business card from user wallet
    @RequestMapping(
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteWalletEntryById(@Valid @RequestBody WalletEntry entry,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String authToken) {
        User walletOwner;
        boolean response;

        try {
            walletOwner = userService.findById(entry.getUserId());
                        
            // if tokens are equal then autorized to proceed
            if (walletOwner.getToken().equals(authToken)) {
                
                response = walletEntryService.deleteWalletEntry(entry);

            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (DataAccessException ex) {
            LOGGER.error("deleteBusinessCardFromWallet: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            if (ex instanceof EmptyResultDataAccessException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (response) {
            LOGGER.info("Business Card " + entry.getBusinessCardId() + " deleted from User's Wallet " + walletOwner.getId(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }

        return response ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
