package gr.bc.api.dao;

import gr.bc.api.model.BusinessCard;
import gr.bc.api.util.MySQLHelper;
import java.util.List;
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
public class JdbcWalletDao implements WalletDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcWalletDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean saveBusinessCardToWallet(long userId, long businessCardId) throws DataAccessException {
        
        String insertQuery = " INSERT INTO " + MySQLHelper.USER_BUSINESS_CARD_TABLE + " ("
                + MySQLHelper.USER_TABLE + "_" + MySQLHelper.USER_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "_" + MySQLHelper.BUSINESS_CARD_ID + ")"
                + " VALUES " + "(?,?)";
        
        int rows = jdbcTemplate.update(insertQuery, new Object[]{userId, businessCardId});
        return rows != 0;
    }

    @Override
    public List<BusinessCard> findAllBusinessCardInWalletByUserId(long id) throws DataAccessException {
        
        String selectQuery = "SELECT " + JdbcBusinessCardDao.getAllAttributes()
                + " FROM " + MySQLHelper.BUSINESS_CARD_TABLE
                + " INNER JOIN " + MySQLHelper.USER_BUSINESS_CARD_TABLE
                + " ON " + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_ID + "=" + MySQLHelper.USER_BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_TABLE + "_" + MySQLHelper.BUSINESS_CARD_ID
                + " WHERE " + MySQLHelper.USER_BUSINESS_CARD_TABLE + "." + MySQLHelper.USER_TABLE + "_" + MySQLHelper.USER_ID + "=" + "'" + id + "'";
        
        List<BusinessCard> bcs = jdbcTemplate.query(selectQuery, new JdbcBusinessCardDao.BusinessCardMapper());
        return bcs;
    }

    @Override
    public boolean deleteBusinessCardFromWallet(long userId, long businessCardId) throws DataAccessException {
        
        String deleteQuery = "DELETE FROM " + MySQLHelper.USER_BUSINESS_CARD_TABLE
                + " WHERE " + MySQLHelper.USER_TABLE + "_" + MySQLHelper.USER_ID + " = " + "?"
                + " AND " + MySQLHelper.BUSINESS_CARD_TABLE + "_" + MySQLHelper.BUSINESS_CARD_ID + " = " + "?";
        
        int rows = jdbcTemplate.update(deleteQuery, new Object[]{userId, businessCardId});
        return rows > 0;
    }

    @Override
    public boolean isDuplicate(long userId, long businessCardId) throws DataAccessException {
        
        String selectQuery = "SELECT COUNT(*) FROM " + MySQLHelper.USER_BUSINESS_CARD_TABLE 
                + " WHERE " + MySQLHelper.USER_TABLE + "_" + MySQLHelper.USER_ID + "=?"
                + " AND " + MySQLHelper.BUSINESS_CARD_TABLE + "_" + MySQLHelper.BUSINESS_CARD_ID + "=?";
        
        int rowCount = jdbcTemplate.queryForObject(selectQuery, Integer.class, userId, businessCardId);
        return rowCount > 0;        
    }

}
