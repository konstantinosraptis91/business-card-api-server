package gr.bc.api.dao;

import gr.bc.api.model.Profession;
import gr.bc.api.util.Constants;
import gr.bc.api.util.MySQLHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 *
 * @author konstantinos
 */
@Repository
@Qualifier("MySQLProfession")
public class ProfessionDaoMySQLImpl implements ProfessionDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfessionDaoMySQLImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public Profession saveProfession(Profession profession) {
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcInsert.withTableName(MySQLHelper.PROFESSION_TABLE).usingGeneratedKeyColumns(MySQLHelper.PROFESSION_ID);
            Map<String, Object> params = new HashMap<>();
            params.put(MySQLHelper.PROFESSION_NAME, profession.getName());
            params.put(MySQLHelper.PROFESSION_DESCRIPTION, profession.getDescription());
            params.put(MySQLHelper.PROFESSION_LAST_UPDATED, profession.getLastUpdated());
            params.put(MySQLHelper.PROFESSION_CREATED_AT, profession.getCreatedAt());
            Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
            profession.setId(key.intValue());
            return profession;
        } catch (Exception e) {
            LOGGER.error("saveProfession: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return new Profession();
    }

    @Override
    public Profession findByName(String name) {
        Profession profession = new Profession();
        try {
            profession = (Profession) jdbcTemplate.queryForObject("SELECT * FROM "
                    + MySQLHelper.PROFESSION_TABLE + " WHERE " + MySQLHelper.PROFESSION_NAME + " = " + "'" + name + "'",
                    (rs, rowNum) -> {
                        Profession p = new Profession();
                        p.setId(rs.getLong(MySQLHelper.PROFESSION_ID));
                        p.setName(rs.getString(MySQLHelper.PROFESSION_NAME));
                        p.setDescription(rs.getString(MySQLHelper.PROFESSION_DESCRIPTION));
                        return p;
                    });
        } catch (DataAccessException e) {
            LOGGER.error("findByName: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return profession;
    }

    @Override
    public Profession findById(long professionId) {
        Profession profession = new Profession();
        try {
            profession = (Profession) jdbcTemplate.queryForObject("SELECT * FROM "
                    + MySQLHelper.PROFESSION_TABLE + " WHERE " + MySQLHelper.PROFESSION_ID + " = " + "'" + professionId + "'",
                    (rs, rowNum) -> {
                        Profession p = new Profession();
                        p.setId(rs.getLong(MySQLHelper.PROFESSION_ID));
                        p.setName(rs.getString(MySQLHelper.PROFESSION_NAME));
                        p.setDescription(rs.getString(MySQLHelper.PROFESSION_DESCRIPTION));
                        return p;
                    });
        } catch (DataAccessException e) {
            LOGGER.error("findById: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return profession;
    }

    @Override
    public List<Profession> findAllProfessions() {
        List<Profession> professions = new ArrayList<>();
        try {
            professions = jdbcTemplate.query("SELECT * FROM " + MySQLHelper.PROFESSION_TABLE, 
                    (rs, rowNum) -> {
                        Profession p = new Profession();
                        p.setId(rs.getLong(MySQLHelper.PROFESSION_ID));
                        p.setName(rs.getString(MySQLHelper.PROFESSION_NAME));
                        p.setDescription(rs.getString(MySQLHelper.PROFESSION_DESCRIPTION));
                        return p;
                    });
        } catch (DataAccessException e) {
            LOGGER.error("findAllProfessions: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return professions;
    }
    
    @Override
    public boolean deleteProfessionById(long id) {
        Integer rows = null;
        try {
            String deleteQuery = "DELETE FROM " + MySQLHelper.PROFESSION_TABLE
                    + " WHERE " + MySQLHelper.PROFESSION_ID + " = " + "?";
            rows = jdbcTemplate.update(deleteQuery, new Object[]{id});
        } catch (DataAccessException e) {
            LOGGER.error("deleteProfessionById: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return rows != null && rows > 0;
    }

    @Override
    public boolean updateProfession(Profession profession) {
        Integer rows = null;
        try {
            String updateQuery = " UPDATE "
                    + MySQLHelper.PROFESSION_TABLE
                    + " SET "
                    + MySQLHelper.PROFESSION_NAME + "=?,"
                    + MySQLHelper.PROFESSION_DESCRIPTION + "=?"
                    + " WHERE " + MySQLHelper.PROFESSION_ID + "=?";
            rows = jdbcTemplate.update(updateQuery,
                    new Object[]{
                        profession.getName(),
                        profession.getDescription(),
                        profession.getId()
                    });
        } catch (DataAccessException e) {
            LOGGER.error("updateProfession: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return rows != null && rows > 0;
    }
    
    // Check if profession by given id exists
    @Override
    public boolean isProfessionExist(long id) {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM "
                + MySQLHelper.PROFESSION_TABLE + " WHERE "
                + MySQLHelper.PROFESSION_ID + " = " + "?", Integer.class, id);
        return result != null && result > 0;
    }
    
    @Override
    public boolean isProfessionExist(String name) {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM "
                + MySQLHelper.PROFESSION_TABLE + " WHERE "
                + MySQLHelper.PROFESSION_NAME + " = " + "?", Integer.class, name);
        return result != null && result > 0;
    }
    
    
    
}
