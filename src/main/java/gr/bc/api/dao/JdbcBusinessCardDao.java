package gr.bc.api.dao;

import static gr.bc.api.dao.JdbcDao.ID;
import gr.bc.api.model.BusinessCard;
import gr.bc.api.model.Company;
import gr.bc.api.model.Profession;
import gr.bc.api.model.Template;
import gr.bc.api.model.response.BusinessCardResponse;
import gr.bc.api.model.response.BusinessCardResponseImpl;
import gr.bc.api.util.ExtractionBundle;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Konstantinos Raptis
 */
@Repository
@Qualifier("MySQLBusinessCard")
public class JdbcBusinessCardDao extends JdbcDao implements BusinessCardDao {
    
    protected static final String TABLE_BUSINESS_CARD = "business_card";
    protected static final String EMAIL_1 = "email_1";
    protected static final String EMAIL_2 = "email_2";
    protected static final String PHONE_NUMBER_1 = "phone_number_1";
    protected static final String PHONE_NUMBER_2 = "phone_number_2";
    protected static final String LINKED_IN = "linked_in";
    protected static final String WEBSITE = "website";
    protected static final String UNIVERSAL = "universal";
    protected static final String ADDRESS_1 = "address_1";
    protected static final String ADDRESS_2 = "address_2";
    
    private static final String SELECT_QUERY_V2_START = "SELECT " 
                + JdbcUserDao.TABLE_USER + "." + JdbcUserDao.FIRSTNAME + ","
                + JdbcUserDao.TABLE_USER + "." + JdbcUserDao.LASTNAME + ","
                + JdbcProfessionDao.TABLE_PROFESSION + ".*" + ","
                + JdbcCompanyDao.TABLE_COMPANY + ".*" + ","
                + JdbcTemplateDao.TABLE_TEMPLATE + ".*" + ","
                + TABLE_BUSINESS_CARD + ".*"
                + " FROM " + TABLE_BUSINESS_CARD
                
                + " INNER JOIN " + JdbcUserDao.TABLE_USER + " ON " + TABLE_BUSINESS_CARD + "." + JdbcUserDao.TABLE_USER + "_" + ID + "=" + JdbcUserDao.TABLE_USER + "." + ID
                
                + " INNER JOIN " + JdbcProfessionDao.TABLE_PROFESSION + " ON " + TABLE_BUSINESS_CARD + "." + JdbcProfessionDao.TABLE_PROFESSION + "_" + ID + "=" + JdbcProfessionDao.TABLE_PROFESSION + "." + ID
                
                + " INNER JOIN " + JdbcCompanyDao.TABLE_COMPANY + " ON " + TABLE_BUSINESS_CARD + "." + JdbcCompanyDao.TABLE_COMPANY + "_" + ID + "=" + JdbcCompanyDao.TABLE_COMPANY + "." + ID
                
                + " INNER JOIN " + JdbcTemplateDao.TABLE_TEMPLATE + " ON " + TABLE_BUSINESS_CARD + "." + JdbcTemplateDao.TABLE_TEMPLATE + "_" + ID + "=" + JdbcTemplateDao.TABLE_TEMPLATE + "." + ID;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public long saveBusinessCard(BusinessCard businessCard) throws DataAccessException {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName(TABLE_BUSINESS_CARD).usingGeneratedKeyColumns(ID);
        Map<String, Object> params = new HashMap<>();
        params.put(JdbcUserDao.TABLE_USER + "_" + ID, businessCard.getUserId());
        params.put(JdbcTemplateDao.TABLE_TEMPLATE + "_" + ID, businessCard.getTemplateId());
        params.put(JdbcProfessionDao.TABLE_PROFESSION + "_" + ID, businessCard.getProfessionId());
        params.put(JdbcCompanyDao.TABLE_COMPANY + "_" + ID, businessCard.getCompanyId());
        params.put(EMAIL_1, businessCard.getEmail1());
        params.put(EMAIL_2, businessCard.getEmail2());
        params.put(PHONE_NUMBER_1, businessCard.getPhoneNumber1());
        params.put(PHONE_NUMBER_2, businessCard.getPhoneNumber2());
        params.put(LINKED_IN, businessCard.getLinkedIn());
        params.put(WEBSITE, businessCard.getWebsite());
        params.put(UNIVERSAL, businessCard.isUniversal());
        params.put(ADDRESS_1, businessCard.getAddress1());
        params.put(ADDRESS_2, businessCard.getAddress2());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        return key.longValue();
    }

    @Override
    public List<BusinessCard> findByUserId(long userId) throws DataAccessException {

        String selectQuery = "SELECT * FROM " + TABLE_BUSINESS_CARD
                + " WHERE " + JdbcUserDao.TABLE_USER + "_" + ID + " = " + "'" + userId + "'";

        List<BusinessCard> businessCards = jdbcTemplate.query(selectQuery, new JdbcBusinessCardDao.BusinessCardMapper());
        return businessCards;
    }

    @Override
    public BusinessCard findById(long businessCardId) throws DataAccessException {

        String selectQuery = "SELECT * FROM " + TABLE_BUSINESS_CARD
                + " WHERE " + ID + " = " + "'" + businessCardId + "'";

        BusinessCard businessCard = jdbcTemplate.queryForObject(selectQuery, new JdbcBusinessCardDao.BusinessCardMapper());
        return businessCard;
    }
    
    @Override
    public List<BusinessCard> findById(List<Long> cardIdList) throws DataAccessException {
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", cardIdList);
        
        String selectQuery = "SELECT * FROM " + TABLE_BUSINESS_CARD
                + " WHERE " + ID + " IN (:ids)";

        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        List<BusinessCard> retrievedCardList = template.query(selectQuery, params, new JdbcBusinessCardDao.BusinessCardMapper());
        return retrievedCardList;
    }
    
    @Override
    public List<BusinessCard> findByUserEmail(String email) throws DataAccessException {

        String selectQuery = "SELECT " + TABLE_BUSINESS_CARD + ".*"
                + " FROM " + TABLE_BUSINESS_CARD
                + " INNER JOIN " + JdbcUserDao.TABLE_USER
                + " ON " + TABLE_BUSINESS_CARD + "." + JdbcUserDao.TABLE_USER + "_" + ID + "=" + JdbcUserDao.TABLE_USER + "." + ID
                + " WHERE " + JdbcUserDao.TABLE_USER + "." + JdbcUserDao.EMAIL + "=" + "'" + email + "'"
                + " AND " + UNIVERSAL + "=" + "'" + 1 + "'"
                // case sensitive search for last name
                + " COLLATE utf8_bin";

        List<BusinessCard> businessCardList = jdbcTemplate.query(selectQuery, new JdbcBusinessCardDao.BusinessCardMapper());
        return businessCardList;
    }

    @Override
    public List<BusinessCard> findByUserName(String firstName, String lastName) throws DataAccessException {
        
        String selectQuery = "SELECT " + TABLE_BUSINESS_CARD + ".*"
                + " FROM " + TABLE_BUSINESS_CARD
                + " INNER JOIN " + JdbcUserDao.TABLE_USER
                + " ON " + TABLE_BUSINESS_CARD + "." + JdbcUserDao.TABLE_USER + "_" + ID + "=" + JdbcUserDao.TABLE_USER + "." + ID
                + " WHERE " + JdbcUserDao.TABLE_USER + "." + JdbcUserDao.FIRSTNAME + " LIKE " + "'" + firstName + "'"
                + " AND " + JdbcUserDao.TABLE_USER + "." + JdbcUserDao.LASTNAME + " LIKE " + "'" + lastName + "'"
                + " AND " + UNIVERSAL + "=" + "'" + 1 + "'"
                // case sensitive search for last name
                + " COLLATE utf8_bin";

        List<BusinessCard> businessCardList = jdbcTemplate.query(selectQuery, new JdbcBusinessCardDao.BusinessCardMapper());
        return businessCardList;
    }

    @Override
    public List<BusinessCardResponse> findByUserNameV2(String firstName, String lastName) throws DataAccessException {
        
        String selectQuery = SELECT_QUERY_V2_START
                
                + " WHERE " 
                
                + "("
                + JdbcUserDao.TABLE_USER + "." + JdbcUserDao.FIRSTNAME + " LIKE " + "'% " + firstName + " %'"
                + " OR " + JdbcUserDao.TABLE_USER + "." + JdbcUserDao.FIRSTNAME + " LIKE " + "'% " + firstName + "'"
                + " OR " + JdbcUserDao.TABLE_USER + "." + JdbcUserDao.FIRSTNAME + " LIKE " + "'" + firstName + " %'"
                + " OR " + JdbcUserDao.TABLE_USER + "." + JdbcUserDao.FIRSTNAME + " = " + "'" + firstName + "'"
                
                + " OR " + JdbcUserDao.TABLE_USER + "." + JdbcUserDao.LASTNAME + " LIKE " + "'% " + lastName + " %'"
                + " OR " + JdbcUserDao.TABLE_USER + "." + JdbcUserDao.LASTNAME + " LIKE " + "'% " + lastName + "'"
                + " OR " + JdbcUserDao.TABLE_USER + "." + JdbcUserDao.LASTNAME + " LIKE " + "'" + lastName + " %'"
                + " OR " + JdbcUserDao.TABLE_USER + "." + JdbcUserDao.LASTNAME + " = " + "'" + lastName + "'"
                + ")"
                + " AND " 
                + "("
                + JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "." + UNIVERSAL + "=" + "'" + 1 + "'"
                + ")";
        
        List<BusinessCardResponse> businessCardResponseList = jdbcTemplate.query(selectQuery, new JdbcBusinessCardDao.BusinessCardResponseMapper());
        return businessCardResponseList;
    }

    @Override
    public List<BusinessCardResponse> findByIdV2(List<Long> cardIdList) throws DataAccessException {
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", cardIdList);
        
        String selectQuery = SELECT_QUERY_V2_START
                + " WHERE " + JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "." + ID + " IN (:ids)";

        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        List<BusinessCardResponse> retrievedCardList = template.query(selectQuery, params, new JdbcBusinessCardDao.BusinessCardResponseMapper());
        return retrievedCardList;
    }

    @Override
    public List<BusinessCardResponse> findByUserIdV2(long userId) throws DataAccessException {
        
        String selectQuery = SELECT_QUERY_V2_START
                + " WHERE " + JdbcUserDao.TABLE_USER + "_" + ID + " = " + "'" + userId + "'";

        List<BusinessCardResponse> businessCards = jdbcTemplate.query(selectQuery, new JdbcBusinessCardDao.BusinessCardResponseMapper());
        return businessCards;
    }

    @Override
    public List<BusinessCardResponse> findByUserEmailV2(String email) throws DataAccessException {
        
        String selectQuery = SELECT_QUERY_V2_START
                + " WHERE " + JdbcUserDao.TABLE_USER + "." + JdbcUserDao.EMAIL + "=" + "'" + email + "'"
                + " AND " + JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "." + UNIVERSAL + "=" + "'" + 1 + "'"
                // case sensitive search for last name
                + " COLLATE utf8_bin";

        List<BusinessCardResponse> businessCardList = jdbcTemplate.query(selectQuery, new JdbcBusinessCardDao.BusinessCardResponseMapper());
        return businessCardList;
    }

    @Override
    public BusinessCardResponse findByIdV2(long businessCardId) throws DataAccessException {
        
        String selectQuery = SELECT_QUERY_V2_START
                + " WHERE " + JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "." + ID + " = " + "'" + businessCardId + "'";

        BusinessCardResponse businessCard = jdbcTemplate.queryForObject(selectQuery, new JdbcBusinessCardDao.BusinessCardResponseMapper());
        return businessCard;
    }
    
    @Override
    public boolean updateBusinessCard(long id, BusinessCard businessCard) throws DataAccessException {

        ExtractionBundle bundle = extractNotNull(id, businessCard);

        String updateQuery = " UPDATE " + TABLE_BUSINESS_CARD
                + " SET " + bundle.getAttributes()
                + " WHERE " + ID + "=?";
        
        int rows = jdbcTemplate.update(updateQuery, bundle.getValues().toArray());
        return rows > 0;
    }

    @Override
    public boolean deleteBusinessCardById(long id) throws DataAccessException {
        
        String deleteQuery = "DELETE FROM " + TABLE_BUSINESS_CARD
                + " WHERE " + ID + " = " + "?";
        
        int rows = jdbcTemplate.update(deleteQuery, new Object[]{id});
        return rows > 0;
    }

    public static ExtractionBundle extractNotNull(long id, BusinessCard businessCard) {
        StringBuilder builder = new StringBuilder();
        List<Object> notNullList = new ArrayList<>();

        if (businessCard.getTemplateId() > 0) {
            builder.append(JdbcTemplateDao.TABLE_TEMPLATE + "_" + ID + "=?,");
            notNullList.add(businessCard.getTemplateId());
        }

        if (businessCard.getProfessionId() > 0) {
            builder.append(JdbcProfessionDao.TABLE_PROFESSION + "_" + ID + "=?,");
            notNullList.add(businessCard.getProfessionId());
        }

        if (businessCard.getCompanyId() > 0) {
            builder.append(JdbcCompanyDao.TABLE_COMPANY + "_" + ID + "=?,");
            notNullList.add(businessCard.getCompanyId());
        }

        if (businessCard.getEmail1() != null) {
            builder.append(EMAIL_1 + "=?,");
            notNullList.add(businessCard.getEmail1());
        }

        if (businessCard.getEmail2() != null) {
            builder.append(EMAIL_2 + "=?,");
            notNullList.add(businessCard.getEmail2());
        }

        if (businessCard.getPhoneNumber1() != null) {
            builder.append(PHONE_NUMBER_1 + "=?,");
            notNullList.add(businessCard.getPhoneNumber1());
        }

        if (businessCard.getPhoneNumber2() != null) {
            builder.append(PHONE_NUMBER_2 + "=?,");
            notNullList.add(businessCard.getPhoneNumber2());
        }

        if (businessCard.getLinkedIn() != null) {
            builder.append(LINKED_IN + "=?,");
            notNullList.add(businessCard.getLinkedIn());
        }

        if (businessCard.getWebsite() != null) {
            builder.append(WEBSITE + "=?,");
            notNullList.add(businessCard.getWebsite());
        }

        if (businessCard.isUniversal() != null) {
            builder.append(UNIVERSAL + "=?,");
            notNullList.add(businessCard.isUniversal());
        }

        if (businessCard.getAddress1() != null) {
            builder.append(ADDRESS_1 + "=?,");
            notNullList.add(businessCard.getAddress1());
        }

        if (businessCard.getAddress2() != null) {
            builder.append(ADDRESS_2 + "=?,");
            notNullList.add(businessCard.getAddress2());
        }

        // remove last comma
        String result = builder.toString().substring(0, builder.toString().length() - 1);
        // add id 
        notNullList.add(id);

        return new ExtractionBundle(result, notNullList);
    }

    public static final class BusinessCardMapper implements RowMapper<BusinessCard> {

        @Override
        public BusinessCard mapRow(ResultSet rs, int rowNum) throws SQLException {
            BusinessCard bc = new BusinessCard();
            bc.setId(rs.getLong(ID));
            bc.setUserId(rs.getLong(JdbcUserDao.TABLE_USER + "_" + ID));
            bc.setTemplateId(rs.getLong(JdbcTemplateDao.TABLE_TEMPLATE + "_" + ID));
            bc.setProfessionId(rs.getLong(JdbcProfessionDao.TABLE_PROFESSION + "_" + ID));
            bc.setCompanyId(rs.getLong(JdbcCompanyDao.TABLE_COMPANY + "_" + ID));
            bc.setEmail1(rs.getString(EMAIL_1));
            bc.setEmail2(rs.getString(EMAIL_2));
            bc.setPhoneNumber1(rs.getString(PHONE_NUMBER_1));
            bc.setPhoneNumber2(rs.getString(PHONE_NUMBER_2));
            bc.setLinkedIn(rs.getString(LINKED_IN));
            bc.setWebsite(rs.getString(WEBSITE));
            bc.setUniversal(rs.getBoolean(UNIVERSAL));
            bc.setAddress1(rs.getString(ADDRESS_1));
            bc.setAddress2(rs.getString(ADDRESS_2));
            bc.setLastUpdated(rs.getTimestamp(LAST_UPDATED));
            bc.setCreatedAt(rs.getTimestamp(CREATED_AT));
            return bc;
        }

    }

    public static final class BusinessCardResponseMapper implements RowMapper<BusinessCardResponse> {

        @Override
        public BusinessCardResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            // fristname, lastname
            String firstName = rs.getString(JdbcUserDao.FIRSTNAME);
            String lastName = rs.getString(JdbcUserDao.LASTNAME);
            
            // profession
            Profession p = new Profession();
            p.setId(rs.getLong(JdbcProfessionDao.TABLE_PROFESSION + "." + ID));
            p.setName(rs.getString(JdbcProfessionDao.TABLE_PROFESSION + "." + JdbcProfessionDao.NAME));
            p.setLastUpdated(rs.getTimestamp(JdbcProfessionDao.TABLE_PROFESSION + "." + LAST_UPDATED));
            p.setCreatedAt(rs.getTimestamp(JdbcProfessionDao.TABLE_PROFESSION + "." +  CREATED_AT));
            
            // company
            Company c = new Company();
            c.setId(rs.getLong(JdbcCompanyDao.TABLE_COMPANY + "." + ID));
            c.setName(rs.getString(JdbcCompanyDao.TABLE_COMPANY + "." + JdbcCompanyDao.NAME));
            c.setLastUpdated(rs.getTimestamp(JdbcCompanyDao.TABLE_COMPANY + "." + LAST_UPDATED));
            c.setCreatedAt(rs.getTimestamp(JdbcCompanyDao.TABLE_COMPANY + "." +  CREATED_AT));
            
            // template
            Template t = new Template();
            t.setId(rs.getLong(JdbcTemplateDao.TABLE_TEMPLATE + "." + ID));
            t.setName(rs.getString(JdbcTemplateDao.TABLE_TEMPLATE + "." + JdbcTemplateDao.NAME));
            t.setPrimaryColor(rs.getString(JdbcTemplateDao.PRIMARY_COLOR));
            t.setSecondaryColor(rs.getString(JdbcTemplateDao.SECONDARY_COLOR));
            t.setLastUpdated(rs.getTimestamp(JdbcTemplateDao.TABLE_TEMPLATE + "." + LAST_UPDATED));
            t.setCreatedAt(rs.getTimestamp(JdbcTemplateDao.TABLE_TEMPLATE + "." +  CREATED_AT));
            
            // business card
            BusinessCard bc = new BusinessCard();
            bc.setId(rs.getLong(JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "." + ID));
            bc.setUserId(rs.getLong(JdbcUserDao.TABLE_USER + "_" + ID));
            bc.setTemplateId(rs.getLong(JdbcTemplateDao.TABLE_TEMPLATE + "_" + ID));
            bc.setProfessionId(rs.getLong(JdbcProfessionDao.TABLE_PROFESSION + "_" + ID));
            bc.setCompanyId(rs.getLong(JdbcCompanyDao.TABLE_COMPANY + "_" + ID));
            bc.setEmail1(rs.getString(EMAIL_1));
            bc.setEmail2(rs.getString(EMAIL_2));
            bc.setPhoneNumber1(rs.getString(PHONE_NUMBER_1));
            bc.setPhoneNumber2(rs.getString(PHONE_NUMBER_2));
            bc.setLinkedIn(rs.getString(LINKED_IN));
            bc.setWebsite(rs.getString(WEBSITE));
            bc.setUniversal(rs.getBoolean(UNIVERSAL));
            bc.setAddress1(rs.getString(ADDRESS_1));
            bc.setAddress2(rs.getString(ADDRESS_2));
            bc.setLastUpdated(rs.getTimestamp(JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "." + LAST_UPDATED));
            bc.setCreatedAt(rs.getTimestamp(JdbcBusinessCardDao.TABLE_BUSINESS_CARD + "." + CREATED_AT));
            
            // set up BusinessCardResponse here
            BusinessCardResponse bcr = new BusinessCardResponseImpl();
            bcr.setUserFullName(firstName, lastName);
            bcr.setProfession(p);
            bcr.setCompany(c);
            bcr.setTemplate(t);
            bcr.setBusinessCard(bc);
            
            return bcr;
        }

    }
    
}
