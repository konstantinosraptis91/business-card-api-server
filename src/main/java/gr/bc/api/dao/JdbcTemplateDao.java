package gr.bc.api.dao;

import gr.bc.api.model.Template;
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
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 *
 * @author konstantinos
 */
@Repository
@Qualifier("MySQLTemplate")
public class JdbcTemplateDao extends JdbcDao implements TemplateDao {

    protected static final String TABLE_TEMPLATE = "template";
    protected static final String NAME = "name";
    protected static final String PRIMARY_COLOR = "primary_color";
    protected static final String SECONDARY_COLOR = "secondary_color";
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public long saveTemplate(Template template) throws DataAccessException {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName(TABLE_TEMPLATE).usingGeneratedKeyColumns(ID);
        Map<String, Object> params = new HashMap<>();
        params.put(NAME, template.getName());
        params.put(PRIMARY_COLOR, template.getPrimaryColor());
        params.put(SECONDARY_COLOR, template.getSecondaryColor());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        return key.longValue();
    }

    @Override
    public List<Template> findAllTemplates() throws DataAccessException {
        
        String selectQuery = "SELECT * FROM " + TABLE_TEMPLATE;
        
        List<Template> templates = jdbcTemplate.query(selectQuery, new JdbcTemplateDao.TemplateMapper());
        return templates;
    }

    @Override
    public Template findById(long id) throws DataAccessException {
        
        String selectQuery = "SELECT * FROM " + TABLE_TEMPLATE 
                + " WHERE " + ID + " = " + "'" + id + "'";
        
        Template template = jdbcTemplate.queryForObject(selectQuery, new JdbcTemplateDao.TemplateMapper());
        return template;
    }

    @Override
    public Template findByName(String name) throws DataAccessException {
        
        String selectQuery = "SELECT * FROM " + TABLE_TEMPLATE 
                + " WHERE " + NAME + " = " + "'" + name + "'";
        
        Template template = jdbcTemplate.queryForObject(selectQuery, new JdbcTemplateDao.TemplateMapper());
        return template;
    }

    @Override
    public List<Template> findByPrimaryColor(String primaryColor) throws DataAccessException {
        
        String selectQuery = "SELECT * FROM " + TABLE_TEMPLATE
                + " WHERE " + PRIMARY_COLOR + " = " + "'" + primaryColor + "'";
        
        List<Template> templates = jdbcTemplate.query(selectQuery, new JdbcTemplateDao.TemplateMapper());
        return templates;
    }

    @Override
    public List<Template> findBySecondaryColor(String secondaryColor) throws DataAccessException {
        
        String selectQuery = "SELECT * FROM " + TABLE_TEMPLATE
                + " WHERE " + SECONDARY_COLOR + " = " + "'" + secondaryColor + "'";
        
        List<Template> templates = jdbcTemplate.query(selectQuery, new JdbcTemplateDao.TemplateMapper());
        return templates;
    }

    @Override
    public boolean deleteTemplateById(long id) throws DataAccessException {
        
        String deleteQuery = "DELETE FROM " + TABLE_TEMPLATE
                + " WHERE " + ID + " = " + "?";
        
        int rows = jdbcTemplate.update(deleteQuery, new Object[]{id});
        return rows > 0;
    }

    @Override
    public boolean updateTemplate(long id, Template template) throws DataAccessException {
        ExtractionBundle bundle = extractNotNull(id, template);
        
        String updateQuery = " UPDATE " + TABLE_TEMPLATE
                + " SET " + bundle.getAttributes()
                + " WHERE " + ID + "=?";
        
        int rows = jdbcTemplate.update(updateQuery, bundle.getValues().toArray());
        return rows > 0;
    }

    public static ExtractionBundle extractNotNull(long id, Template template) {
        StringBuilder builder = new StringBuilder();
        List<Object> notNullList = new ArrayList<>();

        if (template.getName() != null) {
            builder.append(NAME + "=?,");
            notNullList.add(template.getName());
        }

        if (template.getPrimaryColor() != null) {
            builder.append(PRIMARY_COLOR + "=?,");
            notNullList.add(template.getPrimaryColor());
        }

        if (template.getSecondaryColor() != null) {
            builder.append(SECONDARY_COLOR + "=?");
            notNullList.add(template.getSecondaryColor());
        }

        // remove last comma
        String result = builder.toString().substring(0, builder.toString().length() - 1);
        // add id 
        notNullList.add(id);

        return new ExtractionBundle(result, notNullList);
    }

    public static final class TemplateMapper implements RowMapper<Template> {

        @Override
        public Template mapRow(ResultSet rs, int rowNum) throws SQLException {
            Template t = new Template();
            t.setId(rs.getLong(ID));
            t.setName(rs.getString(NAME));
            t.setPrimaryColor(rs.getString(PRIMARY_COLOR));
            t.setSecondaryColor(rs.getString(SECONDARY_COLOR));
            t.setLastUpdated(rs.getTimestamp(LAST_UPDATED));
            t.setCreatedAt(rs.getTimestamp(CREATED_AT));
            return t;
        }
        
    }
    
}
