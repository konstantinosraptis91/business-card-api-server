package gr.bc.api.dao;

import gr.bc.api.model.BusinessCard;
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
 * @author Konstantinos Raptis
 */
@Repository
@Qualifier("MySQLBusinessCard")
public class BusinessCardDaoMySQLImpl implements BusinessCardDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessCardDaoMySQLImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BusinessCard saveBusinessCard(BusinessCard businessCard) {
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcInsert.withTableName(MySQLHelper.BUSINESS_CARD_TABLE).usingGeneratedKeyColumns(MySQLHelper.BUSINESS_CARD_ID);
            Map<String, Object> params = new HashMap<>();
            params.put(MySQLHelper.PROFESSION_ID, businessCard.getProfessionId());
            params.put(MySQLHelper.TEMPLATE_ID, businessCard.getTemplateId());
            params.put(MySQLHelper.USER_ID, businessCard.getUserId());
            params.put(MySQLHelper.BUSINESS_CARD_TITLE, businessCard.getTitle());
            params.put(MySQLHelper.BUSINESS_CARD_DESCRIPTION, businessCard.getDescription());
            params.put(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1, businessCard.getPhoneNumber1());
            params.put(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2, businessCard.getPhoneNumber2());
            params.put(MySQLHelper.BUSINESS_CARD_LINKEDIN, businessCard.getLinkedIn());
            params.put(MySQLHelper.BUSINESS_CARD_WEBSITE, businessCard.getWebsite());
            params.put(MySQLHelper.BUSINESS_CARD_UNIVERSAL, businessCard.isUniversal());
            params.put(MySQLHelper.BUSINESS_CARD_EMAIL, businessCard.getEmail());
            params.put(MySQLHelper.BUSINESS_CARD_LAST_UPDATED, businessCard.getLastUpdated());
            params.put(MySQLHelper.BUSINESS_CARD_CREATED_AT, businessCard.getCreatedAt());
            Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
            businessCard.setId(key.intValue());
            return businessCard;
        } catch (Exception e) {
            LOGGER.error("saveBusinessCard: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return new BusinessCard();
    }

    @Override
    public BusinessCard findByUserId(long userId) {
        BusinessCard businessCard = new BusinessCard();
        try {
            businessCard = (BusinessCard) jdbcTemplate.queryForObject("SELECT * FROM "
                    + MySQLHelper.BUSINESS_CARD_TABLE + " WHERE " + MySQLHelper.USER_ID + " = " + "'" + userId + "'",
                    (rs, rowNum) -> {
                        BusinessCard bc = new BusinessCard();
                        bc.setId(rs.getLong(MySQLHelper.BUSINESS_CARD_ID));
                        bc.setProfessionId(rs.getLong(MySQLHelper.PROFESSION_ID));
                        bc.setTemplateId(rs.getLong(MySQLHelper.TEMPLATE_ID));
                        bc.setUserId(rs.getLong(MySQLHelper.USER_ID));
                        bc.setTitle(rs.getString(MySQLHelper.BUSINESS_CARD_TITLE));
                        bc.setDescription(rs.getString(MySQLHelper.BUSINESS_CARD_DESCRIPTION));
                        bc.setPhoneNumber1(rs.getString(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1));
                        bc.setPhoneNumber2(rs.getString(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2));
                        bc.setLinkedIn(rs.getString(MySQLHelper.BUSINESS_CARD_LINKEDIN));
                        bc.setWebsite(rs.getString(MySQLHelper.BUSINESS_CARD_WEBSITE));
                        bc.setUniversal(rs.getBoolean(MySQLHelper.BUSINESS_CARD_UNIVERSAL));
                        return bc;
                    });
        } catch (DataAccessException e) {
            LOGGER.error("findByUserId: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return businessCard;
    }

    @Override
    public BusinessCard findById(long businessCardId) {
        BusinessCard businessCard = new BusinessCard();
        try {
            businessCard = (BusinessCard) jdbcTemplate.queryForObject("SELECT * FROM "
                    + MySQLHelper.BUSINESS_CARD_TABLE + " WHERE " + MySQLHelper.BUSINESS_CARD_ID + " = " + "'" + businessCardId + "'",
                    (rs, rowNum) -> {
                        BusinessCard bc = new BusinessCard();
                        bc.setId(rs.getLong(MySQLHelper.BUSINESS_CARD_ID));
                        bc.setProfessionId(rs.getLong(MySQLHelper.PROFESSION_ID));
                        bc.setTemplateId(rs.getLong(MySQLHelper.TEMPLATE_ID));
                        bc.setUserId(rs.getLong(MySQLHelper.USER_ID));
                        bc.setTitle(rs.getString(MySQLHelper.BUSINESS_CARD_TITLE));
                        bc.setDescription(rs.getString(MySQLHelper.BUSINESS_CARD_DESCRIPTION));
                        bc.setPhoneNumber1(rs.getString(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1));
                        bc.setPhoneNumber2(rs.getString(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2));
                        bc.setLinkedIn(rs.getString(MySQLHelper.BUSINESS_CARD_LINKEDIN));
                        bc.setWebsite(rs.getString(MySQLHelper.BUSINESS_CARD_WEBSITE));
                        bc.setUniversal(rs.getBoolean(MySQLHelper.BUSINESS_CARD_UNIVERSAL));
                        return bc;
                    });
        } catch (DataAccessException e) {
            LOGGER.error("findById: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return businessCard;
    }

    @Override
    public List<BusinessCard> findByUserFullName(String firstName, String lastName) {
        String selectQuery = "SELECT "
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.PROFESSION_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.TEMPLATE_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_TITLE + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_DESCRIPTION + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1 + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2 + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_LINKEDIN + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_WEBSITE + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_UNIVERSAL
                + " FROM " + MySQLHelper.BUSINESS_CARD_TABLE
                + " INNER JOIN " + MySQLHelper.USER_TABLE
                + " ON "
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.USER_ID
                + "="
                + MySQLHelper.USER_TABLE + "." + MySQLHelper.USER_ID
                + " WHERE "
                + MySQLHelper.USER_TABLE + "." + MySQLHelper.USER_FIRSTNAME + "=" + "'" + firstName + "'"
                // case sensitive search for first name
                // + " COLLATE utf8_bin"
                + " AND "
                + MySQLHelper.USER_TABLE + "." + MySQLHelper.USER_LASTNAME + "=" + "'" + lastName + "'";
                // case sensitive search for last name
                // + " COLLATE utf8_bin";
        List<BusinessCard> bcs = new ArrayList<>();
        try {
            bcs = jdbcTemplate.query(selectQuery, (rs, rowNum) -> {
                BusinessCard bc = new BusinessCard();
                bc.setId(rs.getLong(MySQLHelper.BUSINESS_CARD_ID));
                bc.setProfessionId(rs.getLong(MySQLHelper.PROFESSION_ID));
                bc.setTemplateId(rs.getLong(MySQLHelper.TEMPLATE_ID));
                bc.setTitle(rs.getString(MySQLHelper.BUSINESS_CARD_TITLE));
                bc.setDescription(rs.getString(MySQLHelper.BUSINESS_CARD_DESCRIPTION));
                bc.setPhoneNumber1(rs.getString(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1));
                bc.setPhoneNumber2(rs.getString(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2));
                bc.setLinkedIn(rs.getString(MySQLHelper.BUSINESS_CARD_LINKEDIN));
                bc.setWebsite(rs.getString(MySQLHelper.BUSINESS_CARD_WEBSITE));
                bc.setUniversal(rs.getBoolean(MySQLHelper.BUSINESS_CARD_UNIVERSAL));
                return bc;
            });
        } catch (DataAccessException e) {
            LOGGER.error("findByUserFullName: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return bcs;
    }

    @Override
    public List<BusinessCard> findByUserFirstName(String firstName) {
        String selectQuery = "SELECT "
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.PROFESSION_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.TEMPLATE_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_TITLE + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_DESCRIPTION + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1 + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2 + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_LINKEDIN + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_WEBSITE + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_UNIVERSAL
                + " FROM " + MySQLHelper.BUSINESS_CARD_TABLE
                + " INNER JOIN " + MySQLHelper.USER_TABLE
                + " ON "
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.USER_ID
                + "="
                + MySQLHelper.USER_TABLE + "." + MySQLHelper.USER_ID
                + " WHERE "
                + MySQLHelper.USER_TABLE + "." + MySQLHelper.USER_FIRSTNAME + "=" + "'" + firstName + "'";
                // case sensitive search for first name
                // + " COLLATE utf8_bin";
        List<BusinessCard> bcs = new ArrayList<>();
        try {
            bcs = jdbcTemplate.query(selectQuery, (rs, rowNum) -> {
                BusinessCard bc = new BusinessCard();
                bc.setId(rs.getLong(MySQLHelper.BUSINESS_CARD_ID));
                bc.setProfessionId(rs.getLong(MySQLHelper.PROFESSION_ID));
                bc.setTemplateId(rs.getLong(MySQLHelper.TEMPLATE_ID));
                bc.setTitle(rs.getString(MySQLHelper.BUSINESS_CARD_TITLE));
                bc.setDescription(rs.getString(MySQLHelper.BUSINESS_CARD_DESCRIPTION));
                bc.setPhoneNumber1(rs.getString(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1));
                bc.setPhoneNumber2(rs.getString(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2));
                bc.setLinkedIn(rs.getString(MySQLHelper.BUSINESS_CARD_LINKEDIN));
                bc.setWebsite(rs.getString(MySQLHelper.BUSINESS_CARD_WEBSITE));
                bc.setUniversal(rs.getBoolean(MySQLHelper.BUSINESS_CARD_UNIVERSAL));
                return bc;
            });
        } catch (DataAccessException e) {
            LOGGER.error("findByUserFirstName: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return bcs;
    }

    @Override
    public List<BusinessCard> findByUserLastName(String lastName) {
        String selectQuery = "SELECT "
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.PROFESSION_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.TEMPLATE_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_TITLE + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_DESCRIPTION + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1 + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2 + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_LINKEDIN + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_WEBSITE + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_UNIVERSAL
                + " FROM " + MySQLHelper.BUSINESS_CARD_TABLE
                + " INNER JOIN " + MySQLHelper.USER_TABLE
                + " ON "
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.USER_ID
                + "="
                + MySQLHelper.USER_TABLE + "." + MySQLHelper.USER_ID
                + " WHERE "
                + MySQLHelper.USER_TABLE + "." + MySQLHelper.USER_LASTNAME + "=" + "'" + lastName + "'";
                // case sensitive search for last name
                // + " COLLATE utf8_bin";
        List<BusinessCard> bcs = new ArrayList<>();
        try {
            bcs = jdbcTemplate.query(selectQuery, (rs, rowNum) -> {
                BusinessCard bc = new BusinessCard();
                bc.setId(rs.getLong(MySQLHelper.BUSINESS_CARD_ID));
                bc.setProfessionId(rs.getLong(MySQLHelper.PROFESSION_ID));
                bc.setTemplateId(rs.getLong(MySQLHelper.TEMPLATE_ID));
                bc.setTitle(rs.getString(MySQLHelper.BUSINESS_CARD_TITLE));
                bc.setDescription(rs.getString(MySQLHelper.BUSINESS_CARD_DESCRIPTION));
                bc.setPhoneNumber1(rs.getString(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1));
                bc.setPhoneNumber2(rs.getString(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2));
                bc.setLinkedIn(rs.getString(MySQLHelper.BUSINESS_CARD_LINKEDIN));
                bc.setWebsite(rs.getString(MySQLHelper.BUSINESS_CARD_WEBSITE));
                bc.setUniversal(rs.getBoolean(MySQLHelper.BUSINESS_CARD_UNIVERSAL));
                return bc;
            });
        } catch (DataAccessException e) {
            LOGGER.error("findByUserLastName: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return bcs;
    }
    
    @Override
    public BusinessCard findByUserEmail(String email) {
        String selectQuery = "SELECT "
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.PROFESSION_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.TEMPLATE_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_TITLE + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_DESCRIPTION + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1 + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2 + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_LINKEDIN + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_WEBSITE + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_UNIVERSAL
                + " FROM " + MySQLHelper.BUSINESS_CARD_TABLE
                + " INNER JOIN " + MySQLHelper.USER_TABLE
                + " ON "
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.USER_ID
                + "="
                + MySQLHelper.USER_TABLE + "." + MySQLHelper.USER_ID
                + " WHERE "
                + MySQLHelper.USER_TABLE + "." + MySQLHelper.USER_EMAIL + "=" + "'" + email + "'"
                // case sensitive search for last name
                + " COLLATE utf8_bin";
        BusinessCard businessCard = new BusinessCard();
        try {
            businessCard = (BusinessCard) jdbcTemplate.queryForObject(selectQuery, (rs, rowNum) -> {
                BusinessCard bc = new BusinessCard();
                bc.setId(rs.getLong(MySQLHelper.BUSINESS_CARD_ID));
                bc.setProfessionId(rs.getLong(MySQLHelper.PROFESSION_ID));
                bc.setTemplateId(rs.getLong(MySQLHelper.TEMPLATE_ID));
                bc.setTitle(rs.getString(MySQLHelper.BUSINESS_CARD_TITLE));
                bc.setDescription(rs.getString(MySQLHelper.BUSINESS_CARD_DESCRIPTION));
                bc.setPhoneNumber1(rs.getString(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1));
                bc.setPhoneNumber2(rs.getString(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2));
                bc.setLinkedIn(rs.getString(MySQLHelper.BUSINESS_CARD_LINKEDIN));
                bc.setWebsite(rs.getString(MySQLHelper.BUSINESS_CARD_WEBSITE));
                bc.setUniversal(rs.getBoolean(MySQLHelper.BUSINESS_CARD_UNIVERSAL));
                return bc;
            });
        } catch (DataAccessException e) {
            LOGGER.error("findByUserEmail: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return businessCard;
    }

    @Override
    public List<BusinessCard> findByProfessionId(long id) {
        String selectQuery = "SELECT "
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.PROFESSION_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.TEMPLATE_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_TITLE + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_DESCRIPTION + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1 + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2 + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_LINKEDIN + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_WEBSITE + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_UNIVERSAL
                + " FROM " + MySQLHelper.BUSINESS_CARD_TABLE
                + " INNER JOIN " + MySQLHelper.USER_TABLE
                + " ON "
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.USER_ID
                + "="
                + MySQLHelper.USER_TABLE + "." + MySQLHelper.USER_ID
                + " WHERE "
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.PROFESSION_ID + "=" + "'" + id + "'";
        List<BusinessCard> bcs = new ArrayList<>();
        try {
            bcs = jdbcTemplate.query(selectQuery, (rs, rowNum) -> {
                BusinessCard bc = new BusinessCard();
                bc.setId(rs.getLong(MySQLHelper.BUSINESS_CARD_ID));
                bc.setProfessionId(rs.getLong(MySQLHelper.PROFESSION_ID));
                bc.setTemplateId(rs.getLong(MySQLHelper.TEMPLATE_ID));
                bc.setTitle(rs.getString(MySQLHelper.BUSINESS_CARD_TITLE));
                bc.setDescription(rs.getString(MySQLHelper.BUSINESS_CARD_DESCRIPTION));
                bc.setPhoneNumber1(rs.getString(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1));
                bc.setPhoneNumber2(rs.getString(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2));
                bc.setLinkedIn(rs.getString(MySQLHelper.BUSINESS_CARD_LINKEDIN));
                bc.setWebsite(rs.getString(MySQLHelper.BUSINESS_CARD_WEBSITE));
                bc.setUniversal(rs.getBoolean(MySQLHelper.BUSINESS_CARD_UNIVERSAL));
                return bc;
            });
        } catch (DataAccessException e) {
            LOGGER.error("findByProfessionId: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return bcs;
    }
    
    @Override
    public boolean updateBusinessCard(BusinessCard businessCard) {
        Integer rows = null;
        try {
            String updateQuery = " UPDATE "
                    + MySQLHelper.BUSINESS_CARD_TABLE
                    + " SET "
                    + MySQLHelper.PROFESSION_ID + "=?,"
                    + MySQLHelper.TEMPLATE_ID + "=?,"
                    + MySQLHelper.BUSINESS_CARD_TITLE + "=?,"
                    + MySQLHelper.BUSINESS_CARD_DESCRIPTION + "=?,"
                    + MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1 + "=?,"
                    + MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2 + "=?,"
                    + MySQLHelper.BUSINESS_CARD_LINKEDIN + "=?,"
                    + MySQLHelper.BUSINESS_CARD_WEBSITE + "=?,"
                    + MySQLHelper.BUSINESS_CARD_UNIVERSAL + "=?"
                    + " WHERE " + MySQLHelper.BUSINESS_CARD_ID + "=?";
            rows = jdbcTemplate.update(updateQuery,
                    new Object[]{
                        businessCard.getProfessionId(),
                        businessCard.getTemplateId(),
                        businessCard.getTitle(),
                        businessCard.getDescription(),
                        businessCard.getPhoneNumber1(),
                        businessCard.getPhoneNumber2(),
                        businessCard.getLinkedIn(),
                        businessCard.getWebsite(),
                        businessCard.isUniversal(),
                        businessCard.getId()
                    });
        } catch (DataAccessException e) {
            LOGGER.error("updateBusinessCard: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return rows != null && rows > 0;
    }

    @Override
    public boolean deleteBusinessCardById(long id) {
        Integer rows = null;
        try {
            String deleteQuery = "DELETE FROM " + MySQLHelper.BUSINESS_CARD_TABLE
                    + " WHERE " + MySQLHelper.BUSINESS_CARD_ID + " = " + "?";
            rows = jdbcTemplate.update(deleteQuery, new Object[]{id});
        } catch (DataAccessException e) {
            LOGGER.error("deleteBusinessCardById: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return rows != null && rows > 0;
    }
    
    @Override
    public boolean deleteBusinessCardByUserId(long id) {
        Integer rows = null;
        try {
            String deleteQuery = "DELETE FROM " + MySQLHelper.BUSINESS_CARD_TABLE
                    + " WHERE " + MySQLHelper.USER_ID + " = " + "?";
            rows = jdbcTemplate.update(deleteQuery, new Object[]{id});
        } catch (DataAccessException e) {
            LOGGER.error("deleteBusinessCardByUserId: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return rows != null && rows > 0;
    }
    
    @Override
    public boolean isBusinessCardExist(long id) {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM "
                + MySQLHelper.BUSINESS_CARD_TABLE + " WHERE "
                + MySQLHelper.BUSINESS_CARD_ID + " = " + "?"
                , Integer.class, id);
        return result != null && result > 0;
    }

    @Override
    public boolean isBusinessCardExistByUserId(long id) {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM "
                + MySQLHelper.BUSINESS_CARD_TABLE + " WHERE "
                + MySQLHelper.USER_ID + " = " + "?", Integer.class, id);
        return result != null && result > 0;
    }

    @Override
    public boolean isBusinessCardExistByUserEmail(String email) {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM "
                + MySQLHelper.BUSINESS_CARD_TABLE
                + " INNER JOIN " + MySQLHelper.USER_TABLE
                + " ON "
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.USER_ID
                + "="
                + MySQLHelper.USER_TABLE + "." + MySQLHelper.USER_ID
                + " WHERE "
                + MySQLHelper.USER_TABLE + "." + MySQLHelper.USER_EMAIL + "=" + "'" + email + "'"
                // case sensitive search for last name
                + " COLLATE utf8_bin"
                , Integer.class, email);
        return result != null && result > 0; 
    }

    @Override
    public boolean isBusinessCardBelongToUserById(long businessCardId, long userId) {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM "
                + MySQLHelper.BUSINESS_CARD_TABLE + " WHERE "
                + MySQLHelper.BUSINESS_CARD_ID + " = " + "?"
                + " AND "
                + MySQLHelper.USER_ID + " = " + "?",
                Integer.class, businessCardId, userId);
        return result != null && result > 0;
    }
       
}
