/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.dao;

import gr.bc.api.dao.interfaces.IProfessionDao;
import gr.bc.api.entity.Profession;
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
public class MySQLProfessionDao implements IProfessionDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLProfessionDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public Profession createProfession(Profession profession) {
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcInsert.withTableName(MySQLHelper.PROFESSION_TABLE).usingGeneratedKeyColumns(MySQLHelper.PROFESSION_ID);
            Map<String, Object> params = new HashMap<>();
            params.put(MySQLHelper.PROFESSION_NAME, profession.getName());
            params.put(MySQLHelper.PROFESSION_DESCRIPTION, profession.getDescription());
            Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
            profession.setId(key.intValue());
            return profession;
        } catch (Exception e) {
            LOGGER.error("createProfession: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return new Profession();
    }

    @Override
    public Profession getProfessionByName(String name) {
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
            LOGGER.error("getProfessionByName: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return profession;
    }

    @Override
    public Profession getProfessionById(long professionId) {
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
            LOGGER.error("getProfessionById: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return profession;
    }

    @Override
    public List<Profession> getAllProfessions() {
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
            LOGGER.error(e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return professions;
    }
    
    // Check if profession by given id exists
    @Override
    public boolean isProfessionExist(long id) {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM "
                + MySQLHelper.PROFESSION_TABLE + " WHERE "
                + MySQLHelper.PROFESSION_ID + " = " + "?", Integer.class, id);
        return result != null && result > 0;
    }
       
}
