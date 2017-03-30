package gr.bc.api.dao;

import gr.bc.api.model.BusinessCard;
import gr.bc.api.util.Constants;
import gr.bc.api.util.ExtractionBundle;
import gr.bc.api.util.MySQLHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
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
 * @author Konstantinos Raptis
 */
@Repository
@Qualifier("MySQLBusinessCard")
public class BusinessCardDaoMySQLImpl implements BusinessCardDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessCardDaoMySQLImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BusinessCard saveBusinessCard(BusinessCard businessCard) throws DataAccessException {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName(MySQLHelper.BUSINESS_CARD_TABLE).usingGeneratedKeyColumns(MySQLHelper.BUSINESS_CARD_ID);
        Map<String, Object> params = new HashMap<>();
        params.put(MySQLHelper.USER_TABLE + "_" + MySQLHelper.USER_ID, businessCard.getUserId());
        params.put(MySQLHelper.TEMPLATE_TABLE + "_" + MySQLHelper.TEMPLATE_ID, businessCard.getTemplateId());
        params.put(MySQLHelper.PROFESSION_TABLE + "_" + MySQLHelper.PROFESSION_ID, businessCard.getProfessionId());
        params.put(MySQLHelper.COMPANY_TABLE + "_" + MySQLHelper.COMPANY_ID, businessCard.getCompanyId());
        params.put(MySQLHelper.BUSINESS_CARD_EMAIL_1, businessCard.getEmail1());
        params.put(MySQLHelper.BUSINESS_CARD_EMAIL_2, businessCard.getEmail2());
        params.put(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1, businessCard.getPhoneNumber1());
        params.put(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2, businessCard.getPhoneNumber2());
        params.put(MySQLHelper.BUSINESS_CARD_LINKEDIN, businessCard.getLinkedIn());
        params.put(MySQLHelper.BUSINESS_CARD_WEBSITE, businessCard.getWebsite());
        params.put(MySQLHelper.BUSINESS_CARD_UNIVERSAL, businessCard.isUniversal());
        params.put(MySQLHelper.BUSINESS_CARD_ADDRESS_1, businessCard.getAddress1());
        params.put(MySQLHelper.BUSINESS_CARD_ADDRESS_2, businessCard.getAddress2());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        businessCard.setId(key.intValue());
        return businessCard;
    }

    @Override
    public BusinessCard findByUserId(long userId) throws DataAccessException {
        BusinessCard businessCard = (BusinessCard) jdbcTemplate.queryForObject("SELECT * FROM "
                + MySQLHelper.BUSINESS_CARD_TABLE + " WHERE " + MySQLHelper.USER_TABLE + "_" + MySQLHelper.USER_ID + " = " + "'" + userId + "'",
                (rs, rowNum) -> {
                    return extractBusinessCard(rs);
                });
        return businessCard;
    }

    @Override
    public BusinessCard findById(long businessCardId) throws DataAccessException {
        BusinessCard businessCard = (BusinessCard) jdbcTemplate.queryForObject("SELECT * FROM "
                + MySQLHelper.BUSINESS_CARD_TABLE + " WHERE " + MySQLHelper.BUSINESS_CARD_ID + " = " + "'" + businessCardId + "'",
                (rs, rowNum) -> {
                    return extractBusinessCard(rs);
                });
        return businessCard;
    }

    @Override
    public BusinessCard findByUserEmail(String email) throws DataAccessException {
        String selectQuery = "SELECT "
                + getAllAttributes()
                + " FROM " + MySQLHelper.BUSINESS_CARD_TABLE
                + " INNER JOIN " + MySQLHelper.USER_TABLE
                + " ON "
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.USER_TABLE + "_" + MySQLHelper.USER_ID
                + "="
                + MySQLHelper.USER_TABLE + "." + MySQLHelper.USER_ID
                + " WHERE "
                + MySQLHelper.USER_TABLE + "." + MySQLHelper.USER_EMAIL + "=" + "'" + email + "'"
                // case sensitive search for last name
                + " COLLATE utf8_bin";
        BusinessCard businessCard = (BusinessCard) jdbcTemplate.queryForObject(selectQuery, (rs, rowNum) -> {
            return extractBusinessCard(rs);
        });
        return businessCard;
    }

    @Override
    public boolean updateBusinessCard(long id, BusinessCard businessCard) throws DataAccessException {
        ExtractionBundle bundle = extractNotNull(id, businessCard);
            String updateQuery = " UPDATE "
                    + MySQLHelper.BUSINESS_CARD_TABLE
                    + " SET "
                    + bundle.getAttributes()
                    + " WHERE " + MySQLHelper.BUSINESS_CARD_ID + "=?";
            int rows = jdbcTemplate.update(updateQuery, bundle.getValues().toArray());
        
        return rows > 0;
    }

    @Override
    public boolean deleteBusinessCardById(long id) throws DataAccessException {
        String deleteQuery = "DELETE FROM " + MySQLHelper.BUSINESS_CARD_TABLE
                + " WHERE " + MySQLHelper.BUSINESS_CARD_ID + " = " + "?";
        int rows = jdbcTemplate.update(deleteQuery, new Object[]{id});
        return rows > 0;
    }

    public static String getAllAttributes() {
        return MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.USER_TABLE + "_" + MySQLHelper.USER_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.TEMPLATE_TABLE + "_" + MySQLHelper.TEMPLATE_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.PROFESSION_TABLE + "_" + MySQLHelper.PROFESSION_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.COMPANY_TABLE + "_" + MySQLHelper.COMPANY_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_EMAIL_1 + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_EMAIL_2 + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1 + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2 + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_LINKEDIN + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_WEBSITE + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_UNIVERSAL + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_ADDRESS_1 + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_ADDRESS_2 + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_LAST_UPDATED + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_CREATED_AT;
    }

    public static BusinessCard extractBusinessCard(ResultSet rs) {
        BusinessCard bc = new BusinessCard();
        try {
            bc.setId(rs.getLong(MySQLHelper.BUSINESS_CARD_ID));
            bc.setUserId(rs.getLong(MySQLHelper.USER_TABLE + "_" + MySQLHelper.USER_ID));
            bc.setTemplateId(rs.getLong(MySQLHelper.TEMPLATE_TABLE + "_" + MySQLHelper.TEMPLATE_ID));
            bc.setProfessionId(rs.getLong(MySQLHelper.PROFESSION_TABLE + "_" + MySQLHelper.PROFESSION_ID));
            bc.setCompanyId(rs.getLong(MySQLHelper.COMPANY_TABLE + "_" + MySQLHelper.COMPANY_ID));
            bc.setEmail1(rs.getString(MySQLHelper.BUSINESS_CARD_EMAIL_1));
            bc.setEmail2(rs.getString(MySQLHelper.BUSINESS_CARD_EMAIL_2));
            bc.setPhoneNumber1(rs.getString(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1));
            bc.setPhoneNumber2(rs.getString(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2));
            bc.setLinkedIn(rs.getString(MySQLHelper.BUSINESS_CARD_LINKEDIN));
            bc.setWebsite(rs.getString(MySQLHelper.BUSINESS_CARD_WEBSITE));
            bc.setUniversal(rs.getBoolean(MySQLHelper.BUSINESS_CARD_UNIVERSAL));
            bc.setAddress1(rs.getString(MySQLHelper.BUSINESS_CARD_ADDRESS_1));
            bc.setAddress2(rs.getString(MySQLHelper.BUSINESS_CARD_ADDRESS_2));
            bc.setLastUpdated(rs.getTimestamp(MySQLHelper.BUSINESS_CARD_LAST_UPDATED));
            bc.setCreatedAt(rs.getTimestamp(MySQLHelper.BUSINESS_CARD_CREATED_AT));
        } catch (SQLException ex) {
            LOGGER.error("extractBusinessCard: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return bc;
    }
    
    public static ExtractionBundle extractNotNull(long id, BusinessCard businessCard) {
        StringBuilder builder = new StringBuilder();
        List<Object> notNullList = new ArrayList<>();
        
        if (businessCard.getTemplateId() > 0) {
            builder.append(MySQLHelper.TEMPLATE_TABLE + "_" + MySQLHelper.TEMPLATE_ID + "=?,");
            notNullList.add(businessCard.getTemplateId());
        }
        
        if (businessCard.getProfessionId() > 0)  {
            builder.append(MySQLHelper.PROFESSION_TABLE + "_" + MySQLHelper.PROFESSION_ID + "=?,");
            notNullList.add(businessCard.getProfessionId());
        }
               
        if (businessCard.getCompanyId() > 0) {
            builder.append(MySQLHelper.COMPANY_TABLE + "_" + MySQLHelper.COMPANY_ID + "=?,");
            notNullList.add(businessCard.getCompanyId());
        }
        
        if (businessCard.getEmail1() != null) {
            builder.append(MySQLHelper.BUSINESS_CARD_EMAIL_1 + "=?,");
            notNullList.add(businessCard.getEmail1());
        }
        
        if (businessCard.getEmail2() != null)  {
            builder.append(MySQLHelper.BUSINESS_CARD_EMAIL_2 + "=?,");
            notNullList.add(businessCard.getEmail2());
        }
        
        if (businessCard.getPhoneNumber1() != null)  {
            builder.append(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1 + "=?,");
            notNullList.add(businessCard.getPhoneNumber1());
        }
        
        if (businessCard.getPhoneNumber2() != null) {
            builder.append(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2 + "=?,");
            notNullList.add(businessCard.getPhoneNumber2());
        }
        
        if (businessCard.getLinkedIn() != null) {
            builder.append(MySQLHelper.BUSINESS_CARD_LINKEDIN + "=?,");
            notNullList.add(businessCard.getLinkedIn());
        }
        
        if (businessCard.getWebsite() != null) {
            builder.append(MySQLHelper.BUSINESS_CARD_WEBSITE + "=?,");
            notNullList.add(businessCard.getWebsite());
        }
        
        if (businessCard.isUniversal() != null)  {
            builder.append(MySQLHelper.BUSINESS_CARD_UNIVERSAL + "=?,");
            notNullList.add(businessCard.isUniversal());
        }
        
        if (businessCard.getAddress1() != null)  {
            builder.append(MySQLHelper.BUSINESS_CARD_ADDRESS_1 + "=?,");
            notNullList.add(businessCard.getAddress1());
        }
        
        if (businessCard.getAddress2() != null) {
            builder.append(MySQLHelper.BUSINESS_CARD_ADDRESS_2 + "=?,");
            notNullList.add(businessCard.getAddress2());
        }
        
        // remove last comma
        String result = builder.toString().substring(0, builder.toString().length() - 1);
        // add id 
        notNullList.add(id);
        
        return new ExtractionBundle(result, notNullList);
    }
       
}
