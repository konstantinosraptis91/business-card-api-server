package gr.bc.api.dao;

import gr.bc.api.model.Template;
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
 * @author konstantinos
 */
@Repository
@Qualifier("MySQLTemplate")
public class TemplateDaoMySQLImpl implements TemplateDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateDaoMySQLImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Template saveTemplate(Template template) throws DataAccessException {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName(MySQLHelper.TEMPLATE_TABLE).usingGeneratedKeyColumns(MySQLHelper.TEMPLATE_ID);
        Map<String, Object> params = new HashMap<>();
        params.put(MySQLHelper.TEMPLATE_NAME, template.getName());
        params.put(MySQLHelper.TEMPLATE_PRIMARY_COLOR, template.getPrimaryColor());
        params.put(MySQLHelper.TEMPLATE_SECONDARY_COLOR, template.getSecondaryColor());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        template.setId(key.intValue());
        return template;
    }

    @Override
    public List<Template> findAllTemplates() throws DataAccessException {
        List<Template> templates = jdbcTemplate.query("SELECT * FROM " + MySQLHelper.TEMPLATE_TABLE,
                (rs, rowNum) -> {
                    return extractTemplate(rs);
                });
        return templates;
    }

    @Override
    public Template findById(long id) throws DataAccessException {
        Template template = (Template) jdbcTemplate.queryForObject("SELECT * FROM "
                + MySQLHelper.TEMPLATE_TABLE + " WHERE " + MySQLHelper.TEMPLATE_ID + " = " + "'" + id + "'",
                (rs, rowNum) -> {
                    return extractTemplate(rs);
                });
        return template;
    }

    @Override
    public Template findByName(String name) throws DataAccessException {
        Template template = (Template) jdbcTemplate.queryForObject("SELECT * FROM "
                + MySQLHelper.TEMPLATE_TABLE + " WHERE " + MySQLHelper.TEMPLATE_NAME + " = " + "'" + name + "'",
                (rs, rowNum) -> {
                    return extractTemplate(rs);
                });
        return template;
    }

    @Override
    public List<Template> findByPrimaryColor(String primaryColor) throws DataAccessException {
        List<Template> templates = jdbcTemplate.query("SELECT * FROM " + MySQLHelper.TEMPLATE_TABLE
                + " WHERE " + MySQLHelper.TEMPLATE_PRIMARY_COLOR + " = " + "'" + primaryColor + "'",
                (rs, rowNum) -> {
                    return extractTemplate(rs);
                });
        return templates;
    }

    @Override
    public List<Template> findBySecondaryColor(String secondaryColor) throws DataAccessException {
        List<Template> templates = jdbcTemplate.query("SELECT * FROM " + MySQLHelper.TEMPLATE_TABLE
                + " WHERE " + MySQLHelper.TEMPLATE_SECONDARY_COLOR + " = " + "'" + secondaryColor + "'",
                (rs, rowNum) -> {
                    return extractTemplate(rs);
                });
        return templates;
    }

    @Override
    public boolean deleteTemplateById(long id) throws DataAccessException {
        String deleteQuery = "DELETE FROM " + MySQLHelper.TEMPLATE_TABLE
                + " WHERE " + MySQLHelper.TEMPLATE_ID + " = " + "?";
        int rows = jdbcTemplate.update(deleteQuery, new Object[]{id});
        return rows > 0;
    }

    @Override
    public boolean updateTemplate(long id, Template template) throws DataAccessException {
        ExtractionBundle bundle = extractNotNull(id, template);
        
        String updateQuery = " UPDATE "
                + MySQLHelper.TEMPLATE_TABLE
                + " SET "
                + bundle.getAttributes()
                + " WHERE " + MySQLHelper.TEMPLATE_ID + "=?";
        int rows = jdbcTemplate.update(updateQuery, bundle.getValues().toArray());
        return rows > 0;
    }

    public static Template extractTemplate(ResultSet rs) throws DataAccessException {
        Template t = new Template();
        try {
            t.setId(rs.getLong(MySQLHelper.TEMPLATE_ID));
            t.setName(rs.getString(MySQLHelper.TEMPLATE_NAME));
            t.setPrimaryColor(rs.getString(MySQLHelper.TEMPLATE_PRIMARY_COLOR));
            t.setSecondaryColor(rs.getString(MySQLHelper.TEMPLATE_SECONDARY_COLOR));
            t.setLastUpdated(rs.getTimestamp(MySQLHelper.TEMPLATE_LAST_UPDATED));
            t.setCreatedAt(rs.getTimestamp(MySQLHelper.TEMPLATE_CREATED_AT));
        } catch (SQLException ex) {
            LOGGER.error("extractTemplate: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return t;
    }

    public static ExtractionBundle extractNotNull(long id, Template template) {
        StringBuilder builder = new StringBuilder();
        List<Object> notNullList = new ArrayList<>();

        if (template.getName() != null) {
            builder.append(MySQLHelper.TEMPLATE_NAME + "=?,");
            notNullList.add(template.getName());
        }

        if (template.getPrimaryColor() != null) {
            builder.append(MySQLHelper.TEMPLATE_PRIMARY_COLOR + "=?,");
            notNullList.add(template.getPrimaryColor());
        }

        if (template.getSecondaryColor() != null) {
            builder.append(MySQLHelper.TEMPLATE_SECONDARY_COLOR + "=?");
            notNullList.add(template.getSecondaryColor());
        }

        // remove last comma
        String result = builder.toString().substring(0, builder.toString().length() - 1);
        // add id 
        notNullList.add(id);

        return new ExtractionBundle(result, notNullList);
    }

}
