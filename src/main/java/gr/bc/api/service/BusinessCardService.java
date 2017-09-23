package gr.bc.api.service;

import gr.bc.api.model.BusinessCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import gr.bc.api.dao.BusinessCardDao;
import gr.bc.api.model.Company;
import gr.bc.api.model.Profession;
import gr.bc.api.model.request.BusinessCardRequest;
import gr.bc.api.model.response.BusinessCardResponse;
import gr.bc.api.service.exception.BadRequestException;
import gr.bc.api.service.exception.ConflictException;
import gr.bc.api.service.exception.NoContentException;
import gr.bc.api.service.exception.NotFoundException;
import gr.bc.api.service.exception.ServiceException;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 *
 * @author Konstantinos Raptis
 */
@Service
public class BusinessCardService {

    @Autowired
    @Qualifier("MySQLBusinessCard")
    private BusinessCardDao businessCardDao;
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ProfessionService professionService;
    @Autowired
    private WalletEntryService walletEntryService;

    /**
     * Save a business card after authorize user
     *
     * @param id User id
     * @param token User token
     * @param bc The business card to be saved
     * @return
     * @throws ServiceException
     */
    public long saveBusinessCard(long id, String token, BusinessCard bc) throws ServiceException {

        long bcId;

        try {

            // authorize user
            userService.authorizeUser(id, token);
            // save business card
            bcId = businessCardDao.saveBusinessCard(bc);

        } catch (DataAccessException ex) {
            throw new ConflictException(ex.getMessage());
        }

        return bcId;
    }

    /**
     * Save a business card request after authorize user
     *
     * @param id User id
     * @param token User token
     * @param cardRequest The business card request to be saved
     * @return
     * @throws ServiceException
     */
    public long saveBusinessCard(long id, String token, BusinessCardRequest cardRequest) throws ServiceException {

        long bcId;

        try {

            // authorize user
            userService.authorizeUser(id, token);

            Profession p = new Profession();
            p.setName(cardRequest.getProfessionName());
            long profId;

            try {
                // try to save profession here
                profId = professionService.saveProfession(p);
            } catch (ServiceException ex) {
                // cannot save prof cause already exist
                // retrieve it and use its id
                profId = professionService.findByName(p.getName()).getId();
            }

            Company c = new Company();
            c.setName(cardRequest.getCompanyName());
            long compId;

            try {
                // try to save company here        
                compId = companyService.saveCompany(c);
            } catch (ServiceException ex) {
                // cannot save comp cause already exist
                // retrieve it and use its id
                compId = companyService.findByName(c.getName()).getId();
            }

            // set prof id and comp id here
            cardRequest.getBusinessCard().setProfessionId(profId);
            cardRequest.getBusinessCard().setCompanyId(compId);
            // only for testing set template id to 1L
            cardRequest.getBusinessCard().setTemplateId(1L);

            // save business card
            bcId = businessCardDao.saveBusinessCard(cardRequest.getBusinessCard());

        } catch (DataAccessException ex) {
            throw new ConflictException(ex.getMessage());
        }

        return bcId;
    }

    /**
     * Update a business card after certify it's ownership
     *
     * @param id The business card id
     * @param token User token
     * @param bc Then business card to be updated
     * @return
     * @throws ServiceException
     */
    public boolean updateBusinessCard(long id, String token, BusinessCard bc) throws ServiceException {

        boolean result;

        try {
            // certify that business card truly belong to the user
            authorizeForBusinessCard(id, token);
            // update business card
            result = businessCardDao.updateBusinessCard(id, bc);

        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return result;
    }

    /**
     * Delete a business card after certify it's ownership
     *
     * @param id The business card id
     * @param token User token
     * @return
     * @throws ServiceException
     */
    public boolean deleteBusinessCardById(long id, String token) throws ServiceException {

        boolean result;

        try {
            // certify that business card truly belong to the user
            authorizeForBusinessCard(id, token);
            // first delete the card from wallet entry table in order to avoid conflicts
            walletEntryService.deleteWalletEntryByBusinessCardId(id);
            // delete business card
            result = businessCardDao.deleteBusinessCardById(id);

        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return result;
    }

    /**
     * Find all business card for a specific user id
     *
     * A non public business card will be returned after user authorized for that card successfully
     *
     * @param id User id
     * @param token User token
     * @return The business cards in a list
     * @throws ServiceException
     */
    public List<BusinessCard> findByUserId(long id, String token) throws ServiceException {

        List<BusinessCard> bcList;

        try {
            // authorize user
            userService.authorizeUser(id, token);
            // find business cards for the specific user id
            bcList = businessCardDao.findByUserId(id);
        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return bcList;
    }

    /**
     * Find all business card responses for a specific user id
     *
     * A non public business card will be returned after user authorized for that card successfully
     *
     * @param id User id
     * @param token User token
     * @return The business card responses in a list
     * @throws ServiceException
     */
    public List<BusinessCardResponse> findByUserIdV2(long id, String token) throws ServiceException {

        List<BusinessCardResponse> cardResponseList;

        try {
            // authorize user
            userService.authorizeUser(id, token);
            // find business cards for the specific user id
            cardResponseList = businessCardDao.findByUserIdV2(id);
        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return cardResponseList;
    }

    /**
     * Find a specific business card
     *
     * A non public business card will be returned after user authorized for that card successfully
     *
     * @param id The business card id
     * @param token User token
     * @return
     * @throws ServiceException
     */
    public BusinessCard findById(long id, String token) throws ServiceException {

        BusinessCard bc;

        try {

            // find the business card
            bc = businessCardDao.findById(id);

            if (!bc.isUniversal()) {
                // certify that business card truly belong to the user
                authorizeForBusinessCard(id, token);
            }

        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return bc;
    }

    /**
     * Find a specific business card response
     *
     * A non public business card will be returned after user authorized for that card successfully
     *
     * @param id The business card id
     * @param token User token
     * @return
     * @throws ServiceException
     */
    public BusinessCardResponse findByIdV2(long id, String token) throws ServiceException {

        BusinessCardResponse cardResponse;

        try {

            // find business card response
            cardResponse = businessCardDao.findByIdV2(id);

            if (!cardResponse.getBusinessCard().isUniversal()) {
                // certify that business card truly belong to the user
                authorizeForBusinessCard(id, token);
            }

        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return cardResponse;
    }
    
    /**
     * Find a specific business card response
     *
     * !!! Warning - This method should never used from a controller
     *
     * @param id The business card id
     * @return
     * @throws ServiceException
     */
    public BusinessCardResponse findByIdV2(long id) throws ServiceException {

        BusinessCardResponse cardResponse;

        try {

            // find business card response
            cardResponse = businessCardDao.findByIdV2(id);
        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return cardResponse;
    }
    
    /**
     * Find all business cards for given ids
     *
     * !!! Non public business cards will not be returned !!!
     *
     * @param idList The business cards ids in a list
     * @return
     * @throws ServiceException
     */
    public List<BusinessCard> findById(List<Long> idList) throws ServiceException {

        List<BusinessCard> bcList;

        try {
            bcList = businessCardDao.findById(idList);
        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return bcList;
    }

    /**
     * Find all business cards for given email
     *
     * !!! Non public business cards will not be returned !!!
     *
     * @param email The user email
     * @return
     * @throws ServiceException
     */
    public List<BusinessCard> findByUserEmail(String email) throws ServiceException {

        List<BusinessCard> bcList;

        try {
            bcList = businessCardDao.findByUserEmail(email);
        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return bcList;
    }

    /**
     * Find all business cards by user full name
     *
     * !!! Non public business cards will not be returned !!!
     *
     * @param firstName User first name
     * @param lastName User last name
     * @return
     * @throws ServiceException
     */
    public List<BusinessCard> findByUserName(String firstName, String lastName) throws ServiceException {

        List<BusinessCard> bcList;

        try {
            bcList = businessCardDao.findByUserName(firstName, lastName);
        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return bcList;
    }

    /**
     * Find all business cards response by user full name
     *
     * !!! Non public business cards will not be returned !!!
     *
     * @param firstName User first name
     * @param lastName User last name
     * @return
     * @throws ServiceException
     */
    public List<BusinessCardResponse> findByUserNameV2(String firstName, String lastName) throws ServiceException {

        List<BusinessCardResponse> cardResponseList;

        try {
            cardResponseList = businessCardDao.findByUserNameV2(firstName, lastName);
        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return cardResponseList;
    }

    /**
     * Find all business cards for a specific list of business cards id's
     *
     * !!! Non public business cards will not be returned !!!
     *
     * @param idList The list, which contrains business cards id's
     * @return
     * @throws ServiceException
     */
    public List<BusinessCardResponse> findByIdV2(List<Long> idList) throws ServiceException {

        List<BusinessCardResponse> cardResponseList;

        try {
            cardResponseList = businessCardDao.findByIdV2(idList);
        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return cardResponseList;
    }

    /**
     * Find all business card responses for a user email
     *
     * !!! Non public business cards will not be returned !!!
     *
     * @param email The user email
     * @return
     * @throws ServiceException
     */
    public List<BusinessCardResponse> findByUserEmailV2(String email) throws ServiceException {

        List<BusinessCardResponse> cardResponseList;

        try {
            cardResponseList = businessCardDao.findByUserEmailV2(email);
        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return cardResponseList;
    }

    /**
     * This method has to cases
     *
     * (1) For a non null email, find all business cards for that user mail
     *
     * (2) For a null email and a non null first name and last name, find all business cards for that user full
     * name
     *
     * !!! Non public business cards will not be returned !!!
     *
     * @param email
     * @param firstName
     * @param lastName
     * @return
     * @throws ServiceException
     */
    public List<BusinessCard> find(String email, String firstName, String lastName) throws ServiceException {

        List<BusinessCard> bcList;

        // (case 1) by user email
        if (email != null) {
            bcList = findByUserEmail(email);
        } // (case 2) by first and last name
        else if (firstName != null && lastName != null) {
            bcList = findByUserName(firstName, lastName);
        } // (case 3) every param is null 
        else {
            throw new BadRequestException("email & firstName & lastName are null");
        }

        if (bcList.isEmpty()) {
            throw new NoContentException("No business cards found");
        }

        return bcList;
    }

    /**
     * This method has to cases
     *
     * (1) For a non null email, find all business card responses for that user mail
     *
     * (2) For a null email and a non null first name and last name, find all business card respones for that user
     * full name
     *
     * !!! Non public business cards will not be returned !!!
     *
     * @param email
     * @param firstName
     * @param lastName
     * @return
     * @throws ServiceException
     */
    public List<BusinessCardResponse> findV2(String email, String firstName, String lastName) throws ServiceException {

        List<BusinessCardResponse> cardResponseList;

        // (case 1) by user email
        if (email != null) {
            cardResponseList = findByUserEmailV2(email);
        } // (case 2) by first and last name
        else if (firstName != null && lastName != null) {
            cardResponseList = findByUserNameV2(firstName, lastName);
        } // (case 3) every param is null 
        else {
            throw new BadRequestException("email & firstName & lastName are null");
        }

        if (cardResponseList.isEmpty()) {
            throw new NoContentException("No business cards found");
        }

        return cardResponseList;
    }

    /**
     * Certify that a card belong to a user
     *
     * @param id Business card id of the business card for which the user, who owns the token want to be authorized
     * @param token User token
     * @throws ServiceException
     */
    public void authorizeForBusinessCard(long id, String token) throws ServiceException {

        BusinessCard originalCard;

        try {
            originalCard = businessCardDao.findById(id);
        } catch (DataAccessException ex) {
            throw new NotFoundException(ex.getMessage());
        }

        userService.authorizeUser(originalCard.getUserId(), token);
    }

}
