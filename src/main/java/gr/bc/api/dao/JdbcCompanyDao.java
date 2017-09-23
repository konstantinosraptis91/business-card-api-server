package gr.bc.api.dao;

import gr.bc.api.model.Company;
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
@Qualifier("MySQLCompany")
public class JdbcCompanyDao extends JdbcDao implements CompanyDao {
    
    protected static final String TABLE_COMPANY = "company";
    protected static final String NAME = "name";
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public long saveCompany(Company company) throws DataAccessException {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName(TABLE_COMPANY).usingGeneratedKeyColumns(ID);
        Map<String, Object> params = new HashMap<>();
        params.put(NAME, company.getName());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        return key.longValue();
    }

    @Override
    public Company findByName(String name) throws DataAccessException {

        String selectQuery = "SELECT * FROM " + TABLE_COMPANY
                + " WHERE " + NAME + " = " + "'" + name + "'";

        Company company = jdbcTemplate.queryForObject(selectQuery, new JdbcCompanyDao.CompanyMapper());
        return company;
    }

    @Override
    public List<Company> searchByName(String name) throws DataAccessException {

        String selectQuery = "SELECT * FROM " + TABLE_COMPANY
                + " WHERE " + NAME + " LIKE " + "'% " + name + " %'"
                + " OR " + NAME + " LIKE " + "'% " + name + "'"
                + " OR " + NAME + " LIKE " + "'" + name + " %'"
                + " OR " + NAME + " = " + "'" + name + "'";
                

        List<Company> companyList = jdbcTemplate.query(selectQuery, new JdbcCompanyDao.CompanyMapper());
        return companyList;
    }
    
    @Override
    public Company findById(long id) throws DataAccessException {

        String selectQuery = "SELECT * FROM " + TABLE_COMPANY
                + " WHERE " + ID + " = " + "'" + id + "'";

        Company company = jdbcTemplate.queryForObject(selectQuery, new JdbcCompanyDao.CompanyMapper());
        return company;
    }

    @Override
    public List<Company> findAllCompanies() throws DataAccessException {

        String selectQuery = "SELECT * FROM " + TABLE_COMPANY;

        List<Company> companies = jdbcTemplate.query(selectQuery, new JdbcCompanyDao.CompanyMapper());

        return companies;
    }

    @Override
    public boolean deleteCompanyById(long id) throws DataAccessException {

        String deleteQuery = "DELETE FROM " + TABLE_COMPANY
                + " WHERE " + ID + " = " + "?";

        int rows = jdbcTemplate.update(deleteQuery, new Object[]{id});
        return rows > 0;
    }

    @Override
    public boolean updateCompany(long id, Company company) throws DataAccessException {

        String updateQuery = " UPDATE " + TABLE_COMPANY
                + " SET " + NAME + "=?"
                + " WHERE " + ID + "=?";

        int rows = jdbcTemplate.update(updateQuery, new Object[]{company.getName(), id});
        return rows > 0;
    }

    public static final class CompanyMapper implements RowMapper<Company> {

        @Override
        public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
            Company c = new Company();
            c.setId(rs.getLong(ID));
            c.setName(rs.getString(NAME));
            c.setLastUpdated(rs.getTimestamp(LAST_UPDATED));
            c.setCreatedAt(rs.getTimestamp(CREATED_AT));
            return c;
        }

    }

}
