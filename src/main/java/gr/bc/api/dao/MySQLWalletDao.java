/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.dao;

import gr.bc.api.dao.interfaces.IWalletDao;
import gr.bc.api.entity.User;
import gr.bc.api.util.Constants;
import gr.bc.api.util.MySQLHelper;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Konstantinos Raptis
 */
@Repository
@Qualifier("MySQLWallet")
public class MySQLWalletDao implements IWalletDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLWalletDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public long addBusinessCardToWallet(long userId, long BusinessCardId) {
        long rows = 0;
        try {
            String updateQuery = " INSERT INTO "
                    + MySQLHelper.USER_BUSINESS_CARD_TABLE
                    + " ("
                    + MySQLHelper.USER_ID + ","
                    + MySQLHelper.BUSINESS_CARD_ID + ")"
                    + " VALUES " + "(?,?)";
            rows = jdbcTemplate.update(updateQuery,
                    new Object[]{
                        userId,
                        BusinessCardId
                    });
        } catch (DataAccessException e) {
            LOGGER.error(e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return rows;
    }

}
