package gr.bc.api.dao;

import gr.bc.api.model.Profession;
import gr.bc.api.util.Constants;
import gr.bc.api.util.MySQLHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public Profession saveProfession(Profession profession) throws DataAccessException {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName(MySQLHelper.PROFESSION_TABLE).usingGeneratedKeyColumns(MySQLHelper.PROFESSION_ID);
        Map<String, Object> params = new HashMap<>();
        params.put(MySQLHelper.PROFESSION_NAME, profession.getName());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        profession.setId(key.intValue());
        return profession;
    }

    @Override
    public Profession findByName(String name) throws DataAccessException {
        Profession profession = (Profession) jdbcTemplate.queryForObject("SELECT * FROM "
                + MySQLHelper.PROFESSION_TABLE + " WHERE " + MySQLHelper.PROFESSION_NAME + " = " + "'" + name + "'",
                (rs, rowNum) -> {
                    return extractProfession(rs);
                });
        return profession;
    }

    @Override
    public Profession findById(long professionId) throws DataAccessException {
        Profession profession = (Profession) jdbcTemplate.queryForObject("SELECT * FROM "
                + MySQLHelper.PROFESSION_TABLE + " WHERE " + MySQLHelper.PROFESSION_ID + " = " + "'" + professionId + "'",
                (rs, rowNum) -> {
                    return extractProfession(rs);
                });
        return profession;
    }

    @Override
    public List<Profession> findAllProfessions() throws DataAccessException {
        List<Profession> professions = jdbcTemplate.query("SELECT * FROM " + MySQLHelper.PROFESSION_TABLE,
                (rs, rowNum) -> {
                    return extractProfession(rs);
                });
        return professions;
    }

    @Override
    public boolean deleteProfessionById(long id) throws DataAccessException {
        String deleteQuery = "DELETE FROM " + MySQLHelper.PROFESSION_TABLE
                + " WHERE " + MySQLHelper.PROFESSION_ID + " = " + "?";
        int rows = jdbcTemplate.update(deleteQuery, new Object[]{id});
        return rows > 0;
    }

    @Override
    public boolean updateProfession(long id, Profession profession) throws DataAccessException {
        String updateQuery = " UPDATE "
                + MySQLHelper.PROFESSION_TABLE
                + " SET "
                + MySQLHelper.PROFESSION_NAME + "=?,"
                + " WHERE " + MySQLHelper.PROFESSION_ID + "=?";
        int rows = jdbcTemplate.update(updateQuery,
                new Object[]{
                    profession.getName(),
                    id
                });
        return rows > 0;
    }

    public static Profession extractProfession(ResultSet rs) throws DataAccessException {
        Profession p = new Profession();
        try {
            p.setId(rs.getLong(MySQLHelper.PROFESSION_ID));
            p.setName(rs.getString(MySQLHelper.PROFESSION_NAME));
            p.setLastUpdated(rs.getTimestamp(MySQLHelper.PROFESSION_LAST_UPDATED));
            p.setCreatedAt(rs.getTimestamp(MySQLHelper.PROFESSION_CREATED_AT));
        } catch (SQLException ex) {
            LOGGER.error("extractProfession: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return p;
    }

}
