package gr.bc.api.dao;

import gr.bc.api.model.BusinessCard;
import gr.bc.api.model.WalletEntry;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
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
public class JdbcWalletEntryDao extends JdbcDao implements WalletEntryDao {
        
    protected static final String TABLE_WALLET_ENTRY = "wallet_entry";
    protected static final String STAMP = "stamp";
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public long saveWalletEntry(WalletEntry entry) throws DataAccessException {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName(TABLE_WALLET_ENTRY).usingGeneratedKeyColumns(ID);
        Map<String, Object> params = new HashMap<>();
        params.put(JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "_" + ID, entry.getBusinessCardId());
        params.put(JdbcUserDao.TABLE_USER + "_" + ID, entry.getUserId());
        params.put(STAMP, entry.getStamp());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        return key.longValue();
    }

    @Override
    public List<BusinessCard> findAllBusinessCardsByUserId(long id) throws DataAccessException {
        
        String selectQuery = "SELECT " + JdbcBusinessCardDao.TABLE_BUSINESS_CARD + ".*"
                + " FROM " + JdbcBusinessCardDao.TABLE_BUSINESS_CARD
                + " INNER JOIN " + TABLE_WALLET_ENTRY
                + " ON " + JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "." + ID + "=" + TABLE_WALLET_ENTRY + "." + JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "_" + ID
                + " WHERE " + TABLE_WALLET_ENTRY + "." + JdbcUserDao.TABLE_USER + "_" + ID + "=" + "'" + id + "'";
        
        List<BusinessCard> bcs = jdbcTemplate.query(selectQuery, new JdbcBusinessCardDao.BusinessCardMapper());
        return bcs;
    }

    @Override
    public boolean deleteWalletEntryById(long id) throws DataAccessException {
        
        String deleteQuery = "DELETE FROM " + TABLE_WALLET_ENTRY
                + " WHERE " + ID + " = " + "?";
                        
        int rows = jdbcTemplate.update(deleteQuery, new Object[]{id});
        return rows > 0;
    }

    @Override
    public WalletEntry findById(long id) throws DataAccessException {
        
        String selectQuery = "SELECT * FROM " + TABLE_WALLET_ENTRY
                + " WHERE " + ID + " = " + "'" + id + "'";

        WalletEntry walletEntry = jdbcTemplate.queryForObject(selectQuery, new JdbcWalletEntryDao.WalletEntryMapper());
        return walletEntry;
    }
    
    public static final class WalletEntryMapper implements RowMapper<WalletEntry> {

        @Override
        public WalletEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
            
            long id = rs.getLong(ID);
            long businessCardId = rs.getLong(JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "_" + ID);
            long userId = rs.getLong(JdbcUserDao.TABLE_USER + "_" + ID);
            Date lastUpdated = rs.getTimestamp(LAST_UPDATED);
            Date createdAt = rs.getTimestamp(CREATED_AT);
            
            WalletEntry entry = new WalletEntry(userId, businessCardId);
            entry.setId(id);
            entry.setLastUpdated(lastUpdated);
            entry.setCreatedAt(createdAt);
            
            return entry;
        }

    }
    
}
