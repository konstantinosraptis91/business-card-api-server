/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.controller;

import gr.bc.api.service.BusinessCardService;
import gr.bc.api.service.UserService;
import gr.bc.api.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @RequestMapping(value = "/user/{userId}/businesscard/{businessCardId}",
            method = RequestMethod.POST)           
    public ResponseEntity<Long> addBusinessCardToWallet(
            @PathVariable("userId") long userId,
            @PathVariable("businessCardId") long businessCardId) {
        if (businessCardId < 1 || userId < 1) {
            LOGGER.info("id cannot be lesser than 1");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // check if user exist
        if (!userService.isUserExist(userId)) {
            LOGGER.info("Unable to find user " + userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // check if business card exist
        if (!businessCardService.isBusinessCardExist(businessCardId)) {
            LOGGER.info("Unable to find business card " + businessCardId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        LOGGER.info("Adding Bussines card " + businessCardId + " to users wallet " + userId);
        return new ResponseEntity<>(walletService.addBusinessCardToWallet(userId, businessCardId), HttpStatus.OK);
    }
    
}
