package gr.bc.api.dao;

import static gr.bc.api.dao.JdbcBusinessCardDao.TABLE_BUSINESS_CARD;
import gr.bc.api.model.BusinessCard;
import gr.bc.api.model.WalletEntry;
import gr.bc.api.model.response.BusinessCardResponse;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
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
    public void saveWalletEntries(final List<WalletEntry> entries) throws DataAccessException {
        
        String insertQuery = "INSERT INTO " + TABLE_WALLET_ENTRY 
                + " (" + JdbcUserDao.TABLE_USER + "_" + JdbcDao.ID + "," + JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "_" + JdbcDao.ID + ")"
                + " VALUES (?, ?)";
        
        jdbcTemplate.batchUpdate(insertQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                WalletEntry entry = entries.get(i);
                ps.setLong(1, entry.getUserId());
                ps.setLong(2, entry.getBusinessCardId());
            }

            @Override
            public int getBatchSize() {
                return entries.size();
            }
        });
        
    }
    
    @Override
    public List<BusinessCardResponse> findAllBusinessCardsByUserId(long id) throws DataAccessException {
        
        String selectQuery = "SELECT " 
                + JdbcUserDao.TABLE_USER + "." + JdbcUserDao.FIRSTNAME + ","
                + JdbcUserDao.TABLE_USER + "." + JdbcUserDao.LASTNAME + ","
                + JdbcProfessionDao.TABLE_PROFESSION + ".*" + ","
                + JdbcCompanyDao.TABLE_COMPANY + ".*" + ","
                + JdbcTemplateDao.TABLE_TEMPLATE + ".*" + ","
                + TABLE_BUSINESS_CARD + ".*"
                + " FROM " + JdbcBusinessCardDao.TABLE_BUSINESS_CARD 
                
                + " INNER JOIN " + TABLE_WALLET_ENTRY + " ON " + JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "." + JdbcDao.ID + "=" + TABLE_WALLET_ENTRY + "." + JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "_" + JdbcDao.ID
                
                + " INNER JOIN " + JdbcUserDao.TABLE_USER + " ON " + TABLE_BUSINESS_CARD + "." + JdbcUserDao.TABLE_USER + "_" + JdbcDao.ID + "=" + JdbcUserDao.TABLE_USER + "." + JdbcDao.ID
                
                + " INNER JOIN " + JdbcProfessionDao.TABLE_PROFESSION + " ON " + TABLE_BUSINESS_CARD + "." + JdbcProfessionDao.TABLE_PROFESSION + "_" + JdbcDao.ID + "=" + JdbcProfessionDao.TABLE_PROFESSION + "." + JdbcDao.ID
                
                + " INNER JOIN " + JdbcCompanyDao.TABLE_COMPANY + " ON " + TABLE_BUSINESS_CARD + "." + JdbcCompanyDao.TABLE_COMPANY + "_" + JdbcDao.ID + "=" + JdbcCompanyDao.TABLE_COMPANY + "." + JdbcDao.ID
                
                + " INNER JOIN " + JdbcTemplateDao.TABLE_TEMPLATE + " ON " + TABLE_BUSINESS_CARD + "." + JdbcTemplateDao.TABLE_TEMPLATE + "_" + JdbcDao.ID + "=" + JdbcTemplateDao.TABLE_TEMPLATE + "." + JdbcDao.ID
                
                + " WHERE " + TABLE_WALLET_ENTRY + "." + JdbcUserDao.TABLE_USER + "_" + JdbcDao.ID + "=" + "'" + id + "'";
        
        List<BusinessCardResponse> bcs = jdbcTemplate.query(selectQuery, new JdbcBusinessCardDao.BusinessCardResponseMapper());
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
