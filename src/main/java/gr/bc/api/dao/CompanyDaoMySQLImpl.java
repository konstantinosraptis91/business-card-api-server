package gr.bc.api.dao;

import gr.bc.api.model.Company;
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
 * @author Konstantinos Raptis
 */
@Repository
@Qualifier("MySQLCompany")
public class CompanyDaoMySQLImpl implements CompanyDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDaoMySQLImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Company saveCompany(Company company) throws DataAccessException {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName(MySQLHelper.COMPANY_TABLE).usingGeneratedKeyColumns(MySQLHelper.COMPANY_ID);
        Map<String, Object> params = new HashMap<>();
        params.put(MySQLHelper.COMPANY_NAME, company.getName());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        company.setId(key.intValue());
        return company;
    }

    @Override
    public Company findByName(String name) throws DataAccessException {
        Company company = (Company) jdbcTemplate.queryForObject("SELECT * FROM "
                + MySQLHelper.COMPANY_TABLE + " WHERE " + MySQLHelper.COMPANY_NAME + " = " + "'" + name + "'",
                (rs, rowNum) -> {
                    return extractCompany(rs);
                });
        return company;
    }

    @Override
    public Company findById(long id) throws DataAccessException {
        Company company = (Company) jdbcTemplate.queryForObject("SELECT * FROM "
                + MySQLHelper.COMPANY_TABLE + " WHERE " + MySQLHelper.COMPANY_ID + " = " + "'" + id + "'",
                (rs, rowNum) -> {
                    return extractCompany(rs);
                });
        return company;
    }

    @Override
    public List<Company> findAllCompanies() throws DataAccessException {
        List<Company> companies = jdbcTemplate.query("SELECT * FROM " + MySQLHelper.COMPANY_TABLE,
                (rs, rowNum) -> {
                    return extractCompany(rs);
                });
        return companies;
    }

    @Override
    public boolean deleteCompanyById(long id) throws DataAccessException {
        String deleteQuery = "DELETE FROM " + MySQLHelper.COMPANY_TABLE
                + " WHERE " + MySQLHelper.COMPANY_ID + " = " + "?";
        int rows = jdbcTemplate.update(deleteQuery, new Object[]{id});
        return rows > 0;
    }

    @Override
    public boolean updateCompany(long id, Company company) throws DataAccessException {
        String updateQuery = " UPDATE "
                + MySQLHelper.COMPANY_TABLE
                + " SET "
                + MySQLHelper.COMPANY_NAME + "=?"
                + " WHERE " + MySQLHelper.COMPANY_ID + "=?";
        int rows = jdbcTemplate.update(updateQuery,
                new Object[]{
                    company.getName(),
                    id
                });
        return rows > 0;
    }

    public static Company extractCompany(ResultSet rs) {
        Company c = new Company();
        try {
            c.setId(rs.getLong(MySQLHelper.COMPANY_ID));
            c.setName(rs.getString(MySQLHelper.COMPANY_NAME));
            c.setLastUpdated(rs.getTimestamp(MySQLHelper.COMPANY_LAST_UPDATED));
            c.setCreatedAt(rs.getTimestamp(MySQLHelper.COMPANY_CREATED_AT));
        } catch (SQLException ex) {
            LOGGER.error("extractCompany: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return c;
    }

}
