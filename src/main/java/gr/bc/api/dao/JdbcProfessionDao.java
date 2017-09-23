package gr.bc.api.dao;

import gr.bc.api.model.Profession;
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
 * @author konstantinos
 */
@Repository
@Qualifier("MySQLProfession")
public class JdbcProfessionDao extends JdbcDao implements ProfessionDao {
    
    protected static final String TABLE_PROFESSION = "profession";
    protected static final String NAME = "name";
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public long saveProfession(Profession profession) throws DataAccessException {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName(TABLE_PROFESSION).usingGeneratedKeyColumns(ID);
        Map<String, Object> params = new HashMap<>();
        params.put(NAME, profession.getName());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        return key.longValue();
    }

    @Override
    public Profession findByName(String name) throws DataAccessException {
        
        String selectQuery = "SELECT * FROM " + TABLE_PROFESSION 
                + " WHERE " + NAME + " = " + "'" + name + "'";
        
        Profession profession = jdbcTemplate.queryForObject(selectQuery, new JdbcProfessionDao.ProfessionMapper());
        return profession;
    }

    @Override
    public List<Profession> searchByName(String name) throws DataAccessException {
        
        String selectQuery = "SELECT * FROM " + TABLE_PROFESSION 
                + " WHERE " + NAME + " LIKE " + "'% " + name + " %'"
                + " OR " + NAME + " LIKE " + "'% " + name + "'"
                + " OR " + NAME + " LIKE " + "'" + name + " %'"
                + " OR " + NAME + " = " + "'" + name + "'";
                
        
        List<Profession> professionList = jdbcTemplate.query(selectQuery, new JdbcProfessionDao.ProfessionMapper());
        return professionList;
    }
    
    @Override
    public Profession findById(long professionId) throws DataAccessException {
        
        String selectQuery = "SELECT * FROM " + TABLE_PROFESSION 
                + " WHERE " + ID + " = " + "'" + professionId + "'";
        
        Profession profession = jdbcTemplate.queryForObject(selectQuery, new JdbcProfessionDao.ProfessionMapper());
        return profession;
    }

    @Override
    public List<Profession> findAllProfessions() throws DataAccessException {
        
        String selectQuery = "SELECT * FROM " + TABLE_PROFESSION;
        
        List<Profession> professions = jdbcTemplate.query(selectQuery, new JdbcProfessionDao.ProfessionMapper());
        return professions;
    }

    @Override
    public boolean deleteProfessionById(long id) throws DataAccessException {
        
        String deleteQuery = "DELETE FROM " + TABLE_PROFESSION
                + " WHERE " + ID + " = " + "?";
        
        int rows = jdbcTemplate.update(deleteQuery, new Object[]{id});
        return rows > 0;
    }

    @Override
    public boolean updateProfession(long id, Profession profession) throws DataAccessException {
        
        String updateQuery = " UPDATE " + TABLE_PROFESSION
                + " SET " + NAME + "=?"
                + " WHERE " + ID + "=?";
        
        int rows = jdbcTemplate.update(updateQuery, new Object[]{profession.getName(), id});
        return rows > 0;
    }
    
    public static final class ProfessionMapper implements RowMapper<Profession> {

        @Override
        public Profession mapRow(ResultSet rs, int rowNum) throws SQLException {
            Profession p = new Profession();
            p.setId(rs.getLong(ID));
            p.setName(rs.getString(NAME));
            p.setLastUpdated(rs.getTimestamp(LAST_UPDATED));
            p.setCreatedAt(rs.getTimestamp(CREATED_AT));
            return p;
        }
        
    }
    
}
