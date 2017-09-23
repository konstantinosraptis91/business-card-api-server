package gr.bc.api.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;
import gr.bc.api.dao.WalletEntryDao;
import gr.bc.api.model.WalletEntry;
import gr.bc.api.model.response.BusinessCardResponse;
import gr.bc.api.service.exception.BadRequestException;
import gr.bc.api.service.exception.ConflictException;
import gr.bc.api.service.exception.NotFoundException;
import gr.bc.api.service.exception.ServiceException;
import gr.bc.api.service.exception.UnauthorizedException;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 *
 * @author Konstantinos Raptis
 */
@Service
public class WalletEntryService {

    @Autowired
    @Qualifier("MySQLWallet")
    private WalletEntryDao walletDao;
    @Autowired
    private UserService userService;
    @Autowired
    private BusinessCardService businessCardService;

    /**
     * Save a wallet entry to the user's wallet
     *
     * @param id User id of the user who owns the wallet
     * @param token User token of the user who owns the wallet
     * @param entry Wallet entry to be saved in user's wallet
     * @return
     * @throws ServiceException
     */
    public BusinessCardResponse saveWalletEntry(long id, String token, WalletEntry entry) throws ServiceException {

        BusinessCardResponse cardResponse;
        // boolean result;

        try {
            // authorize user for this specific wallet
            authorizeForWallet(id, token);

            cardResponse = businessCardService.findByIdV2(entry.getBusinessCardId());

            /*
            from now on, id belongs for sure to wallet owner
             */
            // Check if user trying to add his own card in wallet
            if (id == cardResponse.getBusinessCard().getUserId()) {
                throw new BadRequestException("It is not allowed for a user to add his own card in his wallet...");
            }

            // Check if business card not public
            if (!cardResponse.getBusinessCard().isUniversal()) {
                throw new BadRequestException("Business card not public...");
            }

            walletDao.saveWalletEntry(entry);

        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return cardResponse;
    }

    /**
     * Find all the entries of the user's wallet
     *
     * @param id User id of the user who owns the wallet
     * @param token User token of the user who owns the wallet
     * @return
     * @throws ServiceException
     */
    public List<BusinessCardResponse> findAllBusinessCardsByUserId(long id, String token) throws ServiceException {

        List<BusinessCardResponse> cardResponseList;

        try {
            // authorize user for this specific wallet
            authorizeForWallet(id, token);

            /*
            from now on, id belongs for sure to wallet owner
             */
            cardResponseList = walletDao.findAllBusinessCardsByUserId(id);

            // (1) remove from wallet business cards which are not public
            cardResponseList = cardResponseList
                    .stream()
                    .filter(bc -> bc.getBusinessCard().isUniversal())
                    .collect(Collectors.toList());

        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return cardResponseList;
    }

    /**
     * Delete a specific wallet entry for the user's wallet
     *
     * @param entry The entry to be deleted from the user's wallet
     * @param token User token of the user who owns the wallet
     * @return
     * @throws ServiceException
     */
    public boolean deleteWalletEntry(WalletEntry entry, String token) throws ServiceException {

        boolean result;

        try {
            // authorize user for this specific wallet
            authorizeForWallet(entry.getUserId(), token);
            // delete wallet entry
            result = walletDao.deleteWalletEntry(entry);
        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return result;
    }

    /**
     * Removes a business card from all wallets, so it can be deleted from business card table without conflicts
     *
     * @param id
     * @return
     * @throws DataAccessException
     */
    public boolean deleteWalletEntryByBusinessCardId(long id) throws DataAccessException {
        return walletDao.deleteWalletEntryByBusinessCardId(id);
    }

    /**
     * Authorize a user for a certain wallet
     *
     * @param id User id of the user, whose wallet, the user, who owns the token want to be authorized
     * @param token User token
     * @throws UnauthorizedException
     */
    public void authorizeForWallet(long id, String token) throws ServiceException {
        userService.authorizeUser(id, token);
    }

}
