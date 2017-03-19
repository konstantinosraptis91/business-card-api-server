package gr.bc.api.controller;

import gr.bc.api.model.BusinessCard;
import gr.bc.api.service.BusinessCardService;
import gr.bc.api.service.UserService;
import gr.bc.api.service.WalletService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Konstantinos Raptis
 */
@RestController
@RequestMapping(value = "/api/wallet")
public class WalletController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WalletController.class);
    @Autowired
    private WalletService walletService;
    @Autowired
    private UserService userService;
    @Autowired
    private BusinessCardService businessCardService;
    
    // Add business card to user wallet (user_business_card)
    @RequestMapping(
            value = "/user/{userId}/businesscard/{businessCardId}",
            method = RequestMethod.POST)           
    public ResponseEntity<Boolean> addBusinessCardToWallet(
            @PathVariable("userId") long userId,
            @PathVariable("businessCardId") long businessCardId) {
        if (businessCardId < 1 || userId < 1) {
            LOGGER.info("id cannot be lesser than 1");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // check if user exist
        if (!userService.isUserExistById(userId)) {
            LOGGER.info("Unable to find user " + userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // check if business card exist
        if (!businessCardService.isBusinessCardExist(businessCardId)) {
            LOGGER.info("Unable to find business card " + businessCardId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Check if user trying to add his own card in wallet
        if (businessCardService.isBusinessCardBelongToUserById(businessCardId, userId)) {
            LOGGER.info("It is not allowed for a user to add his own card in his wallet...");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        // Check if business card which user willing to add in his wallet already exist
        if (walletService.isBusinessCardExistInWallet(userId, businessCardId)) {
            LOGGER.info("User with id " + userId + " already got business card with id " + businessCardId + " in his wallet...");
            return new ResponseEntity<>(HttpStatus.CONFLICT); 
        }
        LOGGER.info("Adding Bussines card with id " + businessCardId + " to users wallet " + userId);
        return new ResponseEntity<>(walletService.addBusinessCardToWallet(userId, businessCardId), HttpStatus.OK);
    }
    
    // Get all business card a user got in his wallet by user id
    @RequestMapping(
            value = "/user/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BusinessCard>> findAllBusinessCardInWalletByUserId(@PathVariable("id") long id) {
        return userService.isUserExistById(id) ? new ResponseEntity<>(walletService.findAllBusinessCardInWalletByUserId(id), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Delete a business card from user wallet
    @RequestMapping(
            value = "/user/{userId}/businesscard/{businessCardId}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteBusinessCardFromWallet(
            @PathVariable("userId") long userId,
            @PathVariable("businessCardId") long businessCardId) {
        // check if user exists
        if (!userService.isUserExistById(userId)) {
            LOGGER.info("Unable to find user " + userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // check if business card exists
        if (!businessCardService.isBusinessCardExist(businessCardId)) {
            LOGGER.info("Unable to find business card " + businessCardId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // check if user got this business card in his wallet
        if (!walletService.isBusinessCardExistInWallet(userId, businessCardId)) {
            LOGGER.info("Unable to find business card " + businessCardId + " in user with id " + userId + " wallet");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return walletService.deleteBusinessCardFromWallet(userId, businessCardId) ? new ResponseEntity<>(HttpStatus.OK) 
                : new ResponseEntity<>(HttpStatus.CONFLICT); 
    }
    
}
