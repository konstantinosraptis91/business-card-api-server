/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.dao;

import gr.bc.api.dao.interfaces.ITemplateDao;
import gr.bc.api.entity.Template;
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
@Qualifier("MySQLTemplate")
public class MySQLTemplateDao implements ITemplateDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLTemplateDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Template createTemplate(Template template) {
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcInsert.withTableName(MySQLHelper.TEMPLATE_TABLE).usingGeneratedKeyColumns(MySQLHelper.TEMPLATE_ID);
            Map<String, Object> params = new HashMap<>();
            params.put(MySQLHelper.TEMPLATE_NAME, template.getName());
            params.put(MySQLHelper.TEMPLATE_PRIMARY_COLOR, template.getPrimaryColor());
            params.put(MySQLHelper.TEMPLATE_SECONDARY_COLOR, template.getSecondaryColor());
            Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
            template.setId(key.intValue());
            return template;
        } catch (Exception e) {
            LOGGER.error("createTemplate: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return new Template();
    }

    @Override
    public List<Template> getAllTemplates() {
        List<Template> templates = new ArrayList<>();
        try {
            templates = jdbcTemplate.query("SELECT * FROM " + MySQLHelper.TEMPLATE_TABLE, 
                    (rs, rowNum) -> {
                        Template t = new Template();
                        t.setId(rs.getLong(MySQLHelper.TEMPLATE_ID));
                        t.setName(rs.getString(MySQLHelper.TEMPLATE_NAME));
                        t.setPrimaryColor(rs.getString(MySQLHelper.TEMPLATE_PRIMARY_COLOR));
                        t.setSecondaryColor(rs.getString(MySQLHelper.TEMPLATE_SECONDARY_COLOR));
                        return t;
                    });
        } catch (DataAccessException e) {
            LOGGER.error("getAllTemplates: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return templates;
    }

    @Override
    public Template getTemplateById(long id) {
        Template template = new Template();
        try {
            template = (Template) jdbcTemplate.queryForObject("SELECT * FROM "
                    + MySQLHelper.TEMPLATE_TABLE + " WHERE " + MySQLHelper.TEMPLATE_ID + " = " + "'" + id + "'",
                    (rs, rowNum) -> {
                        Template t = new Template();
                        t.setId(rs.getLong(MySQLHelper.TEMPLATE_ID));
                        t.setName(rs.getString(MySQLHelper.TEMPLATE_NAME));
                        t.setPrimaryColor(rs.getString(MySQLHelper.TEMPLATE_PRIMARY_COLOR));
                        t.setSecondaryColor(rs.getString(MySQLHelper.TEMPLATE_SECONDARY_COLOR));
                        return t;
                    });
        } catch (DataAccessException e) {
            LOGGER.error("getTemplateById: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return template;
    }

    @Override
    public Template getTemplateByName(String name) {
        Template template = new Template();
        try {
            template = (Template) jdbcTemplate.queryForObject("SELECT * FROM "
                    + MySQLHelper.TEMPLATE_TABLE + " WHERE " + MySQLHelper.TEMPLATE_NAME + " = " + "'" + name + "'",
                    (rs, rowNum) -> {
                        Template t = new Template();
                        t.setId(rs.getLong(MySQLHelper.TEMPLATE_ID));
                        t.setName(rs.getString(MySQLHelper.TEMPLATE_NAME));
                        t.setPrimaryColor(rs.getString(MySQLHelper.TEMPLATE_PRIMARY_COLOR));
                        t.setSecondaryColor(rs.getString(MySQLHelper.TEMPLATE_SECONDARY_COLOR));
                        return t;
                    });
        } catch (DataAccessException e) {
            LOGGER.error("getTemplateByName: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return template;
    }

}
