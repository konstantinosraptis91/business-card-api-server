package gr.bc.api.dao;

import gr.bc.api.model.BusinessCard;
import gr.bc.api.model.WalletEntry;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Konstantinos Raptis
 */
@Repository
@Qualifier("MySQLWallet")
public class JdbcWalletEntryDao implements WalletEntryDao {
        
    protected static final String TABLE_WALLET_ENTRY = "wallet_entry";
        
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean saveWalletEntry(WalletEntry entry) throws DataAccessException {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName(TABLE_WALLET_ENTRY);
        Map<String, Object> params = new HashMap<>();
        params.put(JdbcUserDao.TABLE_USER + "_" + JdbcDao.ID, entry.getUserId());
        params.put(JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "_" + JdbcDao.ID, entry.getBusinessCardId());
        int rows = jdbcInsert.execute(new MapSqlParameterSource(params));
        return rows > 0;
    }

    @Override
    public List<BusinessCard> findAllBusinessCardsByUserId(long id) throws DataAccessException {
        
        String selectQuery = "SELECT " + JdbcBusinessCardDao.TABLE_BUSINESS_CARD + ".*"
                + " FROM " + JdbcBusinessCardDao.TABLE_BUSINESS_CARD
                + " INNER JOIN " + TABLE_WALLET_ENTRY
                + " ON " + JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "." + JdbcDao.ID + "=" + TABLE_WALLET_ENTRY + "." + JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "_" + JdbcDao.ID
                + " WHERE " + TABLE_WALLET_ENTRY + "." + JdbcUserDao.TABLE_USER + "_" + JdbcDao.ID + "=" + "'" + id + "'";
        
        List<BusinessCard> bcs = jdbcTemplate.query(selectQuery, new JdbcBusinessCardDao.BusinessCardMapper());
        return bcs;
    }

    @Override
    public boolean deleteWalletEntry(WalletEntry entry) throws DataAccessException {
        
        String deleteQuery = "DELETE FROM " + TABLE_WALLET_ENTRY
                + " WHERE " + JdbcUserDao.TABLE_USER + "_" + JdbcDao.ID + " = " + "?"
                + " AND " + JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "_" + JdbcDao.ID + " = " + "?";
                        
        int rows = jdbcTemplate.update(deleteQuery, new Object[]{entry.getUserId(), entry.getBusinessCardId()});
        return rows > 0;
    }

    @Override
    public WalletEntry find(WalletEntry entry) throws DataAccessException {
        
        String selectQuery = "SELECT * FROM " + TABLE_WALLET_ENTRY
                + " WHERE " + JdbcUserDao.TABLE_USER + "_" + JdbcDao.ID + " = " + "'" + entry.getUserId() + "'"
                + " AND " + JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "_" + JdbcDao.ID + " = " + "'" + entry.getBusinessCardId() + "'";

        WalletEntry walletEntry = jdbcTemplate.queryForObject(selectQuery, new JdbcWalletEntryDao.WalletEntryMapper());
        return walletEntry;
    }

    @Override
    public boolean deleteWalletEntryByBusinessCardId(long id) throws DataAccessException {
        
        String deleteQuery = "DELETE FROM " + TABLE_WALLET_ENTRY
                + " WHERE " + JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "_" + JdbcDao.ID + " = " + "?";
        
        int rows = jdbcTemplate.update(deleteQuery, new Object[]{id});
        return rows > 0;
    }
    
    public static final class WalletEntryMapper implements RowMapper<WalletEntry> {

        @Override
        public WalletEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
            WalletEntry entry = new WalletEntry();
            entry.setUserId(rs.getLong(JdbcUserDao.TABLE_USER + "_" + JdbcDao.ID));
            entry.setBusinessCardId(rs.getLong(JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "_" + JdbcDao.ID));
            return entry;
        }

    }
    
}
