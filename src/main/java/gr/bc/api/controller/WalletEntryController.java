package gr.bc.api.controller;

import gr.bc.api.model.WalletEntry;
import gr.bc.api.model.response.BusinessCardResponse;
import gr.bc.api.service.WalletEntryService;
import gr.bc.api.service.exception.ServiceException;
import gr.bc.api.util.Constants;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    // Add business card to user wallet (user_business_card)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveWalletEntry(
            @Valid @RequestBody WalletEntry entry,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String token) {

        BusinessCardResponse cardResponse;
        // boolean result;

        try {
            cardResponse = walletEntryService.saveWalletEntry(entry.getUserId(), token, entry);
        } catch (ServiceException ex) {
            LOGGER.error("addBusinessCardToWallet: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        LOGGER.info(entry.toString() + " added", Constants.LOG_DATE_FORMAT.format(new Date()));

        return new ResponseEntity<>(cardResponse, HttpStatus.CREATED);
    }

    // Get all business card a user got in his wallet by user id
    @RequestMapping(
            value = "/user/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllBusinessCardsByUserId(@PathVariable("id") long id,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String token) {

        List<BusinessCardResponse> cardResponseList;

        try {
            cardResponseList = walletEntryService.findAllBusinessCardsByUserId(id, token);
        } catch (ServiceException ex) {
            LOGGER.error("findAllBusinessCardInWalletByUserId: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            return ex.getResponse();
        }

        LOGGER.info("Returning user " + id + " wallet");

        return cardResponseList.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(cardResponseList, HttpStatus.OK);
    }

    // Delete a business card from user wallet
    @RequestMapping(
            method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteWalletEntry(@NotNull @RequestParam("userId") long userId,
            @NotNull @RequestParam("businessCardId") long businessCardId,
            @NotNull @RequestHeader(Constants.AUTHORIZATION_HEADER_KEY) String token) {

        WalletEntry entry = new WalletEntry();
        entry.setUserId(userId);
        entry.setBusinessCardId(businessCardId);

        try {
            walletEntryService.deleteWalletEntry(entry, token);
        } catch (ServiceException ex) {
            LOGGER.error("deleteBusinessCardFromWallet: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
            ex.getResponse();
        }

        LOGGER.info("Business Card " + entry.getBusinessCardId() + " deleted from User's Wallet " + userId, Constants.LOG_DATE_FORMAT.format(new Date()));

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
